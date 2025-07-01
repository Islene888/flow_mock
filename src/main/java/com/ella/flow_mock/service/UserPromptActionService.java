// src/main/java/com/ella/flow_mock/service/UserPromptActionService.java
package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.entity.UserPromptAction;
import com.ella.flow_mock.repository.PromptRepository;
import com.ella.flow_mock.repository.UserPromptActionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
public class UserPromptActionService {

    // 动作类型常量，避免硬编码
    private static final String ACTION_TYPE_LIKE = "like";
    private static final String ACTION_TYPE_FAVORITE = "favorite";

    @Autowired
    private UserPromptActionRepository actionRepository;
    @Autowired
    private PromptRepository promptRepository;

    /** 点赞 */
    @Transactional
    public void likePrompt(Long userId, Long promptId) {
        performAction(userId, promptId, ACTION_TYPE_LIKE);
    }

    /** 取消点赞 */
    @Transactional
    public void unlikePrompt(Long userId, Long promptId) {
        undoAction(userId, promptId, ACTION_TYPE_LIKE);
    }

    /** 收藏 */
    @Transactional
    public void favoritePrompt(Long userId, Long promptId) {
        performAction(userId, promptId, ACTION_TYPE_FAVORITE);
    }

    /** 取消收藏 */
    @Transactional
    public void unfavoritePrompt(Long userId, Long promptId) {
        undoAction(userId, promptId, ACTION_TYPE_FAVORITE);
    }

    /** 执行动作（点赞/收藏），幂等 */
    private void performAction(Long userId, Long promptId, String actionType) {
        if (actionRepository.existsByUserIdAndPromptIdAndActionType(userId, promptId, actionType)) {
            return;
        }
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new EntityNotFoundException("Prompt not found with id: " + promptId));
        if (Objects.equals(actionType, ACTION_TYPE_LIKE)) {
            prompt.setLikeCount(prompt.getLikeCount() + 1);
        } else if (Objects.equals(actionType, ACTION_TYPE_FAVORITE)) {
            prompt.setFavoriteCount(prompt.getFavoriteCount() + 1);
        }
        promptRepository.save(prompt);
        UserPromptAction newAction = new UserPromptAction();
        newAction.setUserId(userId);
        newAction.setPromptId(promptId);
        newAction.setActionType(actionType);
        actionRepository.save(newAction);
    }

    /** 撤销动作（取消点赞/收藏），幂等 */
    private void undoAction(Long userId, Long promptId, String actionType) {
        if (!actionRepository.existsByUserIdAndPromptIdAndActionType(userId, promptId, actionType)) {
            return;
        }
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new EntityNotFoundException("Prompt not found with id: " + promptId));
        if (Objects.equals(actionType, ACTION_TYPE_LIKE)) {
            prompt.setLikeCount(Math.max(0, prompt.getLikeCount() - 1));
        } else if (Objects.equals(actionType, ACTION_TYPE_FAVORITE)) {
            prompt.setFavoriteCount(Math.max(0, prompt.getFavoriteCount() - 1));
        }
        promptRepository.save(prompt);
        actionRepository.deleteByUserIdAndPromptIdAndActionType(userId, promptId, actionType);
    }

    /** 查询用户点赞的 prompt（分页） */
    public Page<Prompt> findLikedPromptsByUserId(Long userId, Pageable pageable) {
        return promptRepository.findPromptsByUserIdAndActionType(userId, ACTION_TYPE_LIKE, pageable);
    }

    /** 查询用户收藏的 prompt（分页） */
    public Page<Prompt> findFavoritedPromptsByUserId(Long userId, Pageable pageable) {
        return promptRepository.findPromptsByUserIdAndActionType(userId, ACTION_TYPE_FAVORITE, pageable);
    }
}
