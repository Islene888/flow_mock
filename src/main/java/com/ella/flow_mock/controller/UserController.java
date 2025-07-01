// src/main/java/com/ella/flow_mock/controller/UserController.java
package com.ella.flow_mock.controller;

import com.ella.flow_mock.entity.User;
import com.ella.flow_mock.repository.UserRepository;
import com.ella.flow_mock.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCacheService cacheService;

    // 注册接口
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> resp = new HashMap<>();
        // 查重
        if (userRepository.findByUsername(user.getUsername()) != null) {
            resp.put("success", false);
            resp.put("message", "用户名已存在");
            return resp;
        }
        User saved = userRepository.save(user);
        cacheService.saveUserToCache(saved);
        resp.put("success", true);
        resp.put("data", saved);
        return resp;
    }

    // 登录接口
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        Map<String, Object> resp = new HashMap<>();
        User u = userRepository.findByUsername(username);
        if (u == null || !u.getPassword().equals(password)) {
            resp.put("success", false);
            resp.put("message", "用户名或密码错误");
            return resp;
        }
        resp.put("success", true);
        resp.put("data", u);
        return resp;
    }

    // 获取用户详情（支持缓存）
    @GetMapping("/get/{id}")
    public User get(@PathVariable Long id) {
        User cached = cacheService.getUserFromCache(id);
        return (cached != null ? cached : userRepository.findById(id).orElse(null));
    }

    // 也可支持通用 save（不建议暴露到线上环境）
    @PostMapping("/save")
    public User save(@RequestBody User user) {
        User u = userRepository.save(user);
        cacheService.saveUserToCache(u);
        return u;
    }
}
