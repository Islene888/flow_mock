// src/main/java/com/ella/flow_mock/service/PromptService.java
package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.repository.PromptRepository;
import jakarta.persistence.criteria.Predicate; // 请确保导入这个类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class PromptService {


    @Autowired
    private PromptRepository promptRepository;

    public Prompt createPrompt(Prompt prompt) {
        return promptRepository.save(prompt);
    }

    public Prompt getPrompt(Long id) {
        return promptRepository.findById(id).orElse(null);
    }

    /**
     * 列表：查询中文的 Prompt，并支持按 creatorId 和 keyword 过滤，支持分页。
     *
     * @param creatorId 可选的创建者ID
     * @param keyword   可选的搜索关键词
     * @param pageable  分页参数
     * @return 分页的 Prompt 列表
     */
    public Page<Prompt> list(
            Long creatorId,
            String keyword,
            Pageable pageable
    ) {
        Specification<Prompt> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 不强制 language 过滤，避免查不到数据
            // 如果未来有 language 参数，可以加条件判断：

            // if (language != null) {
            //     predicates.add(criteriaBuilder.equal(root.get("language"), language));
            // }

            if (creatorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("creatorId"), creatorId));
            }
            if (StringUtils.hasText(keyword)) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Predicate contentLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), likePattern);
                Predicate descriptionLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
                predicates.add(criteriaBuilder.or(contentLike, descriptionLike));
            }

            if (predicates.isEmpty()) {
                return null;  // 无过滤条件时返回null，查询所有
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return promptRepository.findAll(spec, pageable);
    }
}