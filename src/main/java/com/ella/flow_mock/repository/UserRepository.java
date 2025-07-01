// src/main/java/com/ella/flow_mock/repository/UserRepository.java
package com.ella.flow_mock.repository;

import com.ella.flow_mock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // 你的原写法，OK！

    // Optional 写法（推荐，但非必需，看你Controller层如何用）
    // Optional<User> findByUsername(String username);
}
