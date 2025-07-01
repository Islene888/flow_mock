package com.ella.flow_mock.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user") // 数据库表名是 user
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private String name;

    // ----- 必须的无参构造器（JPA 规范） -----
    public User() {}

    // ----- getter/setter -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
