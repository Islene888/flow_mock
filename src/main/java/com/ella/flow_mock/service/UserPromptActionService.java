// src/main/java/com/ella/flow_mock/service/UserPromptActionService.java
package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.entity.UserPromptAction;
import com.ella.flow_mock.repository.PromptRepository;
import com.ella.flow_mock.repository.UserPromptActionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// 正确的导入
import org.springframework.data.domain.Pageable;
import java.util.Objects;

@Service
public class UserPromptActionService {

    // 动作类型常量，避免硬编码字符串
    private static final String ACTION_TYPE_LIKE = "like";
    private static final String ACTION_TYPE_FAVORITE = "favorite";

    @Autowired
    private UserPromptActionRepository actionRepository;

    @Autowired
    private PromptRepository promptRepository;

    /**
     * 用户点赞一个 Prompt。
     * 此操作是幂等的，重复点赞不会产生副作用。
     * @param userId 用户ID
     * @param promptId Prompt ID
     */
    @Transactional
    public void likePrompt(Long userId, Long promptId) {
        performAction(userId, promptId, ACTION_TYPE_LIKE);
    }

    /**
     * 用户取消点赞一个 Prompt。
     * @param userId 用户ID
     * @param promptId Prompt ID
     */
    @Transactional
    public void unlikePrompt(Long userId, Long promptId) {
        undoAction(userId, promptId, ACTION_TYPE_LIKE);
    }

    /**
     * 用户收藏一个 Prompt。
     * 此操作是幂等的，重复收藏不会产生副作用。
     * @param userId 用户ID
     * @param promptId Prompt ID
     */
    @Transactional
    public void favoritePrompt(Long userId, Long promptId) {
        performAction(userId, promptId, ACTION_TYPE_FAVORITE);
    }

    /**
     * 用户取消收藏一个 Prompt。
     * @param userId 用户ID
     * @param promptId Prompt ID
     */
    @Transactional
    public void unfavoritePrompt(Long userId, Long promptId) {
        undoAction(userId, promptId, ACTION_TYPE_FAVORITE);
    }

    /**
     * 私有辅助方法，用于执行点赞或收藏操作。
     * 使用 @Transactional 注解确保数据一致性。
     */
    private void performAction(Long userId, Long promptId, String actionType) {
        // 1. 检查是否已存在该动作，如果存在则直接返回，保证幂等性
        if (actionRepository.existsByUserIdAndPromptIdAndActionType(userId, promptId, actionType)) {
            return;
        }

        // 2. 查找 Prompt，如果不存在则抛出异常
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new EntityNotFoundException("Prompt not found with id: " + promptId));

        // 3. 根据动作类型，增加对应的计数
        if (Objects.equals(actionType, ACTION_TYPE_LIKE)) {
            prompt.setLikeCount(prompt.getLikeCount() + 1);
        } else if (Objects.equals(actionType, ACTION_TYPE_FAVORITE)) {
            prompt.setFavoriteCount(prompt.getFavoriteCount() + 1);
        }

        // 4. 保存 Prompt 的更新
        promptRepository.save(prompt);

        // 5. 创建并保存新的动作记录
        UserPromptAction newAction = new UserPromptAction();
        newAction.setUserId(userId);
        newAction.setPromptId(promptId);
        newAction.setActionType(actionType);
        actionRepository.save(newAction);
    }

    /**
     * 私有辅助方法，用于撤销点赞或收藏操作。
     */
    private void undoAction(Long userId, Long promptId, String actionType) {
        // 1. 检查动作是否存在，如果不存在则直接返回
        if (!actionRepository.existsByUserIdAndPromptIdAndActionType(userId, promptId, actionType)) {
            return;
        }

        // 2. 查找 Prompt
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new EntityNotFoundException("Prompt not found with id: " + promptId));

        // 3. 根据动作类型，减少对应的计数（并确保不小于0）
        if (Objects.equals(actionType, ACTION_TYPE_LIKE)) {
            prompt.setLikeCount(Math.max(0, prompt.getLikeCount() - 1));
        } else if (Objects.equals(actionType, ACTION_TYPE_FAVORITE)) {
            prompt.setFavoriteCount(Math.max(0, prompt.getFavoriteCount() - 1));
        }

        // 4. 保存 Prompt 的更新
        promptRepository.save(prompt);

        // 5. 删除动作记录
        actionRepository.deleteByUserIdAndPromptIdAndActionType(userId, promptId, actionType);
    }
    /**
     * 分页查询用户点赞的所有 Prompt。
     * @param userId 用户ID
     * @param pageable 分页信息
     * @return 分页的 Prompt 列表
     */
    public Page<Prompt> findLikedPromptsByUserId(Long userId, Pageable pageable) {
        return promptRepository.findPromptsByUserIdAndActionType(userId, ACTION_TYPE_LIKE, pageable);
    }

    /**
     * 分页查询用户收藏的所有 Prompt。
     * @param userId 用户ID
     * @param pageable 分页信息
     * @return 分页的 Prompt 列表
     */
    public Page<Prompt> findFavoritedPromptsByUserId(Long userId, Pageable pageable) {
        return promptRepository.findPromptsByUserIdAndActionType(userId, ACTION_TYPE_FAVORITE, pageable);
    }
}