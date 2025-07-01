package com.ella.flow_mock.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_prompt_action")
public class UserPromptAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long promptId;

    // "like" 或 "favorite"
    private String actionType;

    private LocalDateTime actionTime;

    // ----- JPA 必须的无参构造器 -----
    public UserPromptAction() {}

    // ----- 新增：自动填充 actionTime -----
    @PrePersist
    public void prePersist() {
        if (this.actionTime == null) {
            this.actionTime = LocalDateTime.now();
        }
    }

    // --- getter/setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPromptId() { return promptId; }
    public void setPromptId(Long promptId) { this.promptId = promptId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }
}
