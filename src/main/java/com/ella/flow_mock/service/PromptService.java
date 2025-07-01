package com.ella.flow_mock.service;

import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.repository.PromptRepository;
import jakarta.persistence.criteria.Predicate;
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

    // 新增：删除 Prompt
    public void deletePrompt(Long id) {
        promptRepository.deleteById(id);
    }

    /**
     * 支持多条件分页筛选
     * @param creatorId 可选创建者ID
     * @param keyword   可选关键词
     * @param pageable  分页参数
     */
    public Page<Prompt> list(Long creatorId, String keyword, Pageable pageable) {
        Specification<Prompt> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

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
                return null;  // 无过滤时返回所有
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return promptRepository.findAll(spec, pageable);
    }
}
