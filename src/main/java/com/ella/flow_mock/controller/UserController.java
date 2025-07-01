package com.ella.flow_mock.controller;

import com.ella.flow_mock.entity.User;
import com.ella.flow_mock.repository.UserRepository;
import com.ella.flow_mock.service.UserCacheService;
import com.ella.flow_mock.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication; // 用于 whoami

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCacheService cacheService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ApiResponse.error(1, "用户名已存在");
        }
        User saved = userRepository.save(user);
        cacheService.saveUserToCache(saved);
        return ApiResponse.success(saved);
    }

    /**
     * 用户登录，返回 JWT+用户信息（JWT生成待补充）
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody User user) {
        User u = userRepository.findByUsername(user.getUsername());
        if (u == null || !u.getPassword().equals(user.getPassword())) {
            return ApiResponse.error(2, "用户名或密码错误");
        }
        // 预留：生成JWT Token
        // String token = JwtUtil.generateToken(u.getId(), u.getUsername(), ...);
        // Map<String, Object> data = new HashMap<>();
        // data.put("token", token);
        // data.put("user", u);
        // return ApiResponse.success(data);
        return ApiResponse.success(u); // 暂时只返回用户对象
    }

    /**
     * 查询当前登录用户（whoami，JWT登录后可直接返回 userId 或用户名）
     */
    @GetMapping("/whoami")
    public ApiResponse<?> whoami(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(401, "未登录");
        }
        Object principal = authentication.getPrincipal();
        // 这里假定你的 principal 是用户名
        return ApiResponse.success(principal.toString());
        // 如果你自定义了UserDetailsImpl，可返回userId等
    }

    /**
     * 获取用户详情（支持缓存）
     */
    @GetMapping("/get/{id}")
    public ApiResponse<User> get(@PathVariable Long id) {
        User cached = cacheService.getUserFromCache(id);
        User user = (cached != null ? cached : userRepository.findById(id).orElse(null));
        if (user == null) return ApiResponse.error(3, "用户不存在");
        return ApiResponse.success(user);
    }

    /**
     * 通用保存（开发测试用，生产可不暴露）
     */
    @PostMapping("/save")
    public ApiResponse<User> save(@RequestBody User user) {
        User u = userRepository.save(user);
        cacheService.saveUserToCache(u);
        return ApiResponse.success(u);
    }
}
