package com.ella.flow_mock.controller;

import com.ella.flow_mock.common.ApiResponse;
import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.entity.UserPromptAction;
import com.ella.flow_mock.repository.UserPromptActionRepository;
import com.ella.flow_mock.service.UserPromptActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    // ================== Prompt 相关 ==================

    /** 点赞 */
    @PostMapping("/prompts/{promptId}/like")
    public ApiResponse<Void> like(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.likePrompt(userId, promptId);
        return ApiResponse.success(null);
    }
    /** 取消点赞 */
    @DeleteMapping("/prompts/{promptId}/like")
    public ApiResponse<Void> unlike(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.unlikePrompt(userId, promptId);
        return ApiResponse.success(null);
    }

    /** 收藏 */
    @PostMapping("/prompts/{promptId}/favorite")
    public ApiResponse<Void> favorite(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.favoritePrompt(userId, promptId);
        return ApiResponse.success(null);
    }
    /** 取消收藏 */
    @DeleteMapping("/prompts/{promptId}/favorite")
    public ApiResponse<Void> unfavorite(@PathVariable Long promptId, @RequestParam Long userId) {
        userPromptActionService.unfavoritePrompt(userId, promptId);
        return ApiResponse.success(null);
    }

    /** 获取某个 Prompt 的点赞/收藏数 */
    @GetMapping("/prompts/{promptId}/stats")
    public ApiResponse<Map<String, Long>> promptStats(@PathVariable Long promptId) {
        long likes = actionRepo.countByPromptIdAndActionType(promptId, "like");
        long favs = actionRepo.countByPromptIdAndActionType(promptId, "favorite");
        return ApiResponse.success(Map.of("likes", likes, "favorites", favs));
    }

    // ================== 用户相关 ==================

    /** 某用户点赞过的 promptId */
    @GetMapping("/users/{userId}/liked-prompt-ids")
    public ApiResponse<List<Long>> likedPromptIds(@PathVariable Long userId) {
        List<Long> ids = actionRepo.findByUserIdAndActionType(userId, "like")
                .stream().map(UserPromptAction::getPromptId).toList();
        return ApiResponse.success(ids);
    }

    /** 某用户收藏过的 promptId */
    @GetMapping("/users/{userId}/favorited-prompt-ids")
    public ApiResponse<List<Long>> favoritedPromptIds(@PathVariable Long userId) {
        List<Long> ids = actionRepo.findByUserIdAndActionType(userId, "favorite")
                .stream().map(UserPromptAction::getPromptId).toList();
        return ApiResponse.success(ids);
    }

    /** 某用户点赞过的 Prompt（分页） */
    @GetMapping("/users/{userId}/liked-prompts")
    public ApiResponse<Page<Prompt>> getLikedPrompts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Prompt> p = userPromptActionService.findLikedPromptsByUserId(userId, pageable);
        return ApiResponse.success(p);
    }

    /** 某用户收藏过的 Prompt（分页） */
    @GetMapping("/users/{userId}/favorited-prompts")
    public ApiResponse<Page<Prompt>> getFavoritedPrompts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Prompt> p = userPromptActionService.findFavoritedPromptsByUserId(userId, pageable);
        return ApiResponse.success(p);
    }
}
