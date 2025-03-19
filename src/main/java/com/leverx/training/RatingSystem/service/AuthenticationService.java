package com.leverx.training.RatingSystem.service;

import com.leverx.training.RatingSystem.db.dto.UserCreationDTO;
import com.leverx.training.RatingSystem.db.dto.converters.UserConverter;
import com.leverx.training.RatingSystem.db.model.User;
import com.leverx.training.RatingSystem.enums.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final JWTService jwtService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserConverter userConverter;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserService userService, JWTService jwtService, RedisService redisService, PasswordEncoder passwordEncoder, EmailService emailService, UserConverter userConverter, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userConverter = userConverter;
        this.authenticationManager = authenticationManager;
    }

    public void signUp(UserCreationDTO userDTO){
        if(userService.getByEmail(userDTO.getEmail()).isPresent()){
            throw new RuntimeException("The user with email " + userDTO.getEmail() + " already exists");
        }
        User user = userConverter.convertToEntity(userDTO);
        user.setRole(Role.USER);
        user.setEmailConfirmed(false);
        user.setApproved(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.insert(user);
        String token = jwtService.generateVerifyToken(user.getEmail());
        redisService.cacheEmailVerificationToken(token, user.getEmail());
        emailService.sendVerificationToken(user.getEmail(), token);
    }

    public void verifyEmail(String token){
        String email = redisService.getEmailByToken(token);
        if(email==null) throw new RuntimeException("Invalid token");
        Optional<User> user = userService.getByEmail(email);
        if(user.isPresent()){
            userService.confirmEmail(user.get().getId());
            redisService.deleteToken(token);
        } else throw new RuntimeException("The user not found");
    }

    public String login(String email, String password){
        if(userService.getByEmail(email).isEmpty())
            throw new RuntimeException("User with provided email not exist");
        if(!userService.getByEmail(email).get().getEmailConfirmed())
            throw new RuntimeException("Please confirm your email! The link was send to your email");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userService.getByEmail(email).get();
        return jwtService.generateLoginToken(user);
    }
}
