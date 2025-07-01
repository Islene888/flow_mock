// src/main/java/com/ella/flow_mock/repository/PromptRepository.java
package com.ella.flow_mock.repository;

import com.ella.flow_mock.entity.Prompt;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long>, JpaSpecificationExecutor<Prompt> { // Add this
    Page<Prompt> findByLanguage(String language, Pageable pageable);
    Page<Prompt> findByCreatorId(Long creatorId, Pageable pageable);

    @Query("""
      SELECT p FROM Prompt p
       WHERE LOWER(p.content)   LIKE LOWER(CONCAT('%',:kw,'%'))
          OR LOWER(p.description) LIKE LOWER(CONCAT('%',:kw,'%'))
    """)
    Page<Prompt> searchByKeyword(@Param("kw") String keyword, Pageable pageable);


    @Query("""
      SELECT p FROM Prompt p JOIN UserPromptAction a ON p.id = a.promptId
       WHERE a.userId = :userId
         AND a.actionType = :actionType
    """)
    Page<Prompt> findPromptsByUserIdAndActionType(
            @Param("userId") Long userId,
            @Param("actionType") String actionType,
            Pageable pageable
    );
}
