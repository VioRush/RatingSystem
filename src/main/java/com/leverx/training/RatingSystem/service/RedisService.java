package com.leverx.training.RatingSystem.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheEmailVerificationToken(String token, String email) {
        redisTemplate.opsForValue().set(token, email, Duration.ofHours(24));
    }

    public String getEmailByToken(String token){
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token){
        redisTemplate.delete(token);
    }

}
