// src/main/java/com/ella/flow_mock/controller/UserPromptActionController.java
package com.ella.flow_mock.controller;

import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.entity.UserPromptAction;
import com.ella.flow_mock.repository.UserPromptActionRepository;
import com.ella.flow_mock.service.UserPromptActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserPromptActionController {

    @Autowired
    private UserPromptActionService userPromptActionService;
    @Autowired
    private UserPromptActionRepository actionRepo;

    // ========== Prompt 相关 ==========

    // 点赞/取消点赞
    @PostMapping("/prompts/{promptId}/like")
    public ResponseEntity<Void> like(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.likePrompt(userId, promptId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/prompts/{promptId}/like")
    public ResponseEntity<Void> unlike(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.unlikePrompt(userId, promptId);
        return ResponseEntity.noContent().build();
    }

    // 收藏/取消收藏
    @PostMapping("/prompts/{promptId}/favorite")
    public ResponseEntity<Void> favorite(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.favoritePrompt(userId, promptId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/prompts/{promptId}/favorite")
    public ResponseEntity<Void> unfavorite(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.unfavoritePrompt(userId, promptId);
        return ResponseEntity.noContent().build();
    }

    // 单个 Prompt 的点赞/收藏数
    @GetMapping("/prompts/{promptId}/stats")
    public Map<String, Long> promptStats(@PathVariable Long promptId) {
        long likes = actionRepo.countByPromptIdAndActionType(promptId, "like");
        long favs = actionRepo.countByPromptIdAndActionType(promptId, "favorite");
        return Map.of("likes", likes, "favorites", favs);
    }

    // ========== 用户相关 ==========

    // 用户点赞过的 promptId
    @GetMapping("/users/{userId}/liked-prompt-ids")
    public List<Long> likedPromptIds(@PathVariable Long userId) {
        return actionRepo.findByUserIdAndActionType(userId, "like")
                .stream().map(UserPromptAction::getPromptId).toList();
    }

    // 用户收藏过的 promptId
    @GetMapping("/users/{userId}/favorited-prompt-ids")
    public List<Long> favoritedPromptIds(@PathVariable Long userId) {
        return actionRepo.findByUserIdAndActionType(userId, "favorite")
                .stream().map(UserPromptAction::getPromptId).toList();
    }

    // 用户点赞过的 Prompt（分页，返回 Prompt 对象列表）
    @GetMapping("/users/{userId}/liked-prompts")
    public Page<Prompt> getLikedPrompts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userPromptActionService.findLikedPromptsByUserId(userId, pageable);
    }

    // 用户收藏过的 Prompt（分页，返回 Prompt 对象列表）
    @GetMapping("/users/{userId}/favorited-prompts")
    public Page<Prompt> getFavoritedPrompts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userPromptActionService.findFavoritedPromptsByUserId(userId, pageable);
    }
}
