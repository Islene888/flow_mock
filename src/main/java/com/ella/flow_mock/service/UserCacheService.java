// src/main/java/com/ella/flow_mock/service/UserCacheService.java
package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {
    @Autowired private RedisTemplate<String, User> redisTemplate;

    public void saveUserToCache(User user) {
        redisTemplate.opsForValue().set("user:" + user.getId(), user);
    }

    public User getUserFromCache(Long id) {
        return redisTemplate.opsForValue().get("user:" + id);
    }
}
