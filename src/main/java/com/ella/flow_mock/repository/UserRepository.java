// src/main/java/com/ella/flow_mock/repository/UserRepository.java
package com.ella.flow_mock.repository;

import com.ella.flow_mock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { User findByUsername(String username); }
