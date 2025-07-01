package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.User;
import com.ella.flow_mock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 缓存 key = "users::123" 这种格式，缓存命中自动返回，无则查库
    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 还可以加更多方法，比如根据用户名查
    @Cacheable(value = "users", key = "'name:' + #username")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
