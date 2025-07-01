package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.User;
import com.ella.flow_mock.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    // 构造器注入
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable("users")
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
