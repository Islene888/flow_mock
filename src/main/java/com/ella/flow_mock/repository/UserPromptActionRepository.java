package com.ella.flow_mock.repository;

import com.ella.flow_mock.entity.UserPromptAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPromptActionRepository extends JpaRepository<UserPromptAction, Long> {
    // 查询某用户对某 prompt 的某类操作（like/favorite）是否存在
    boolean existsByUserIdAndPromptIdAndActionType(Long userId, Long promptId, String actionType);

    // 查询记录详情
    Optional<UserPromptAction> findByUserIdAndPromptIdAndActionType(Long userId, Long promptId, String actionType);

    // 某用户所有 like/favorite 的 prompt
    List<UserPromptAction> findByUserIdAndActionType(Long userId, String actionType);

    // 某 prompt 的 like/favorite 总数
    Long countByPromptIdAndActionType(Long promptId, String actionType);

    // 删除用户的 like/favorite 操作
    @Modifying
    @Transactional
    @Query("""
        delete from UserPromptAction a 
        where a.userId = :userId 
          and a.promptId = :promptId 
          and a.actionType = :actionType
        """)
    void deleteByUserIdAndPromptIdAndActionType(
            @Param("userId") Long userId,
            @Param("promptId") Long promptId,
            @Param("actionType") String actionType
    );
}
