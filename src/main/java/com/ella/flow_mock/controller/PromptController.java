package com.ella.flow_mock.controller;

import com.ella.flow_mock.common.ApiResponse;
import com.ella.flow_mock.entity.Prompt;
import com.ella.flow_mock.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompts")
public class PromptController {

    @Autowired
    private PromptService promptService;

    // 1. 分页+多条件查询（可用creatorId/keyword筛选）
    @GetMapping
    public ApiResponse<Page<Prompt>> list(
            @RequestParam(required = false) Long creatorId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Prompt> result = promptService.list(creatorId, keyword, pageable);
        return ApiResponse.success(result);
    }

    // 2. 查询单个 Prompt
    @GetMapping("/{id}")
    public ApiResponse<Prompt> get(@PathVariable Long id) {
        Prompt p = promptService.getPrompt(id);
        return (p != null) ? ApiResponse.success(p) : ApiResponse.error("Prompt不存在");
    }

    // 3. 新建 Prompt
    @PostMapping
    public ApiResponse<Prompt> create(@RequestBody Prompt prompt) {
        return ApiResponse.success(promptService.createPrompt(prompt));
    }

    // 4. 编辑 Prompt
    @PutMapping("/{id}")
    public ApiResponse<Prompt> update(@PathVariable Long id, @RequestBody Prompt req) {
        Prompt origin = promptService.getPrompt(id);
        if (origin == null) return ApiResponse.error("Prompt不存在");
        // 只更新允许的字段（如有权限判断要加上）
        origin.setContent(req.getContent());
        origin.setDescription(req.getDescription());
        origin.setTags(req.getTags());
        origin.setLanguage(req.getLanguage());
        origin.setRole(req.getRole());
        origin.setInstructions(req.getInstructions());
        origin.setGreeting(req.getGreeting());
        origin.setSystemPrompt(req.getSystemPrompt());
        origin.setIsPublic(req.getIsPublic());
        // ...可补充其它字段

        Prompt saved = promptService.createPrompt(origin);
        return ApiResponse.success(saved);
    }

    // 5. 删除 Prompt
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        Prompt p = promptService.getPrompt(id);
        if (p == null) return ApiResponse.error("Prompt不存在");
        // 权限校验建议放这里（如p.getCreatorId()==当前用户）
        promptService.deletePrompt(id);
        return ApiResponse.success("删除成功");
    }
}
