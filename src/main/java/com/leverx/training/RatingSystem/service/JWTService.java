package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey(){
       return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateVerifyToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateLoginToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", user.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, User user){
        String email = extractEmail(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claimsTFunction.apply(claims);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }
}
