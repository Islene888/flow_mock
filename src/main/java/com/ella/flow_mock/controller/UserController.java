package com.ella.flow_mock.controller;

import com.ella.flow_mock.entity.User;
import com.ella.flow_mock.repository.UserRepository;
import com.ella.flow_mock.service.UserCacheService;
import com.ella.flow_mock.common.ApiResponse; // 你的ApiResponse路径
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCacheService cacheService;

    // 注册接口
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ApiResponse.error(1, "用户名已存在");
        }
        User saved = userRepository.save(user);
        cacheService.saveUserToCache(saved);
        return ApiResponse.success(saved);
    }

    // 登录接口（推荐前端传JSON {username, password}）
    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody User user) {
        User u = userRepository.findByUsername(user.getUsername());
        if (u == null || !u.getPassword().equals(user.getPassword())) {
            return ApiResponse.error(2, "用户名或密码错误");
        }
        return ApiResponse.success(u);
    }

    // 获取用户详情（支持缓存）
    @GetMapping("/get/{id}")
    public ApiResponse<User> get(@PathVariable Long id) {
        User cached = cacheService.getUserFromCache(id);
        User user = (cached != null ? cached : userRepository.findById(id).orElse(null));
        if (user == null) return ApiResponse.error(3, "用户不存在");
        return ApiResponse.success(user);
    }

    // 通用save（不建议线上用，可以不用ApiResponse）
    @PostMapping("/save")
    public ApiResponse<User> save(@RequestBody User user) {
        User u = userRepository.save(user);
        cacheService.saveUserToCache(u);
        return ApiResponse.success(u);
    }
}
