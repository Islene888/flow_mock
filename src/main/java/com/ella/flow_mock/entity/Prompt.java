package com.ella.flow_mock.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String language;
    private String description;

    private Long creatorId;

    // 多标签存储，自动生成 prompt_tags 关联表
    @ElementCollection
    @CollectionTable(name = "prompt_tags", joinColumns = @JoinColumn(name = "prompt_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    // Bot 相关字段（可选）
    private String role;           // 角色设定
    private String instructions;   // 行为指令
    private String greeting;       // 开场白
    private String systemPrompt;   // 系统 prompt
    private Boolean isPublic = true;

    // 计数字段
    private Long likeCount = 0L;
    private Long favoriteCount = 0L;

    // --- getter/setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getGreeting() { return greeting; }
    public void setGreeting(String greeting) { this.greeting = greeting; }

    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }

    public Long getFavoriteCount() { return favoriteCount; }
    public void setFavoriteCount(Long favoriteCount) { this.favoriteCount = favoriteCount; }
}
