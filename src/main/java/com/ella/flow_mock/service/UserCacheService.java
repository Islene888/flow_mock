// src/main/java/com/ella/flow_mock/service/UserCacheService.java
package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserCacheService {
    @Autowired private RedisTemplate<String, User> redisTemplate;

    /**
     * 保存用户到缓存（带过期时间）
     * 推荐设置自动过期，防止缓存过期后“脏读”过时数据
     */
    public void saveUserToCache(User user) {
        try {
            redisTemplate.opsForValue().set("user:" + user.getId(), user, 30, TimeUnit.MINUTES); // 30分钟
        } catch (Exception e) {
            // 容错处理，防止缓存服务异常影响主流程
            System.err.println("Redis 缓存写入异常: " + e.getMessage());
        }
    }

    /**
     * 从缓存读取用户
     * 出现异常时返回 null，主流程可以继续查DB
     */
    public User getUserFromCache(Long id) {
        try {
            return redisTemplate.opsForValue().get("user:" + id);
        } catch (Exception e) {
            System.err.println("Redis 缓存读取异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 可选：缓存删除接口（比如用户信息变更、注销等场景下调用）
     */
    public void removeUserFromCache(Long id) {
        try {
            redisTemplate.delete("user:" + id);
        } catch (Exception e) {
            System.err.println("Redis 缓存删除异常: " + e.getMessage());
        }
    }
}
