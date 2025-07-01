package com.ella.flow_mock.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/prompts")
public class PromptController {
    // 用于演示：全局存储 Prompt 列表（模拟数据库）
    private static final List<PromptDto> promptList = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    // 内部类定义
    public static class PromptDto {
        public int id;
        public String content;
        public String description;
        public List<String> tags;
        public int likeCount;
        public int favoriteCount;
        public Map<String, String> author;
        public int creatorId;

        // —— 新增 bot 预设相关字段 ——
        public String role;           // 角色设定
        public String instructions;   // 行为指令
        public String greeting;       // 开场白
        public String systemPrompt;   // 系统 prompt
        public boolean isPublic;      // 是否公开

        public PromptDto() {}

        // 完整构造函数
        public PromptDto(
                int id, String content, String description, List<String> tags, Map<String, String> author, int creatorId,
                String role, String instructions, String greeting, String systemPrompt, boolean isPublic
        ) {
            this.id = id;
            this.content = content;
            this.description = description;
            this.tags = tags;
            this.author = author;
            this.creatorId = creatorId;
            this.likeCount = ThreadLocalRandom.current().nextInt(10, 501);
            this.favoriteCount = ThreadLocalRandom.current().nextInt(5, 301);

            this.role = role;
            this.instructions = instructions;
            this.greeting = greeting;
            this.systemPrompt = systemPrompt;
            this.isPublic = isPublic;
        }
    }

    // 初始化部分模拟数据（首次访问时执行一次）
    static {
        if (promptList.isEmpty()) {
            promptList.add(new PromptDto(
                    idCounter.getAndIncrement(),
                    "周报生成器",
                    "只需输入几个关键词，就能为你生成一份结构完整、内容充实的周报。",
                    List.of("工作", "周报", "效率"),
                    Map.of("name", "产品经理A", "avatar", "https://api.dicebear.com/8.x/miniavs/svg?seed=1"),
                    1,  // creatorId
                    "你是一位乐观高效的日报助理",               // role
                    "请输出结构化的周报摘要，语气积极",           // instructions
                    "Hi，很高兴帮你生成今日周报！",                // greeting
                    "你是一个日报机器人，专注帮助团队高效沟通。",   // systemPrompt
                    true
            ));
            promptList.add(new PromptDto(
                    idCounter.getAndIncrement(),
                    "小红书风格文案",
                    "输入产品名和特点，立刻生成爆款小红书风格的种草文案。",
                    List.of("营销", "小红书", "文案"),
                    Map.of("name", "市场专员B", "avatar", "https://api.dicebear.com/8.x/miniavs/svg?seed=2"),
                    2,
                    "你是一位时尚种草达人",
                    "文风要轻松有梗，适合年轻人传播",
                    "嘿！快来看看这款超棒的好物！",
                    "你是一个小红书爆款文案助手，擅长用真实体验打动人心。",
                    true
            ));
            promptList.add(new PromptDto(
                    idCounter.getAndIncrement(),
                    "代码解释器",
                    "粘贴一段看不懂的代码，让AI逐行给你解释。",
                    List.of("编程", "学习", "工具"),
                    Map.of("name", "后端工程师C", "avatar", "https://api.dicebear.com/8.x/miniavs/svg?seed=3"),
                    3,
                    "你是一位耐心的代码讲解专家",
                    "请详细解释每一行代码，并用例子辅助说明",
                    "你好，准备好学习代码了吗？",
                    "你是一名编程教师，善于将复杂内容讲明白。",
                    false
            ));
            promptList.add(new PromptDto(
                    idCounter.getAndIncrement(),
                    "面试官模拟器",
                    "模拟不同岗位的技术面试，帮你提前准备，提升面试成功率。",
                    List.of("求职", "面试", "模拟"),
                    Map.of("name", "HR大佬D", "avatar", "https://api.dicebear.com/8.x/miniavs/svg?seed=4"),
                    4,
                    "你是一位专业的面试官",
                    "请随机提问，并根据回答给出点评建议",
                    "欢迎参加模拟面试，请自信作答！",
                    "你是一名严谨的技术面试官，评价客观公正。",
                    true
            ));
        }
    }

    // 获取所有 Prompt
    @GetMapping
    public List<PromptDto> getPrompts() {
        return promptList;
    }

    // 根据 id 获取单个 Prompt（编辑页面或聊天页面用）
    @GetMapping("/{id}")
    public PromptDto getPromptById(@PathVariable int id) {
        return promptList.stream().filter(p -> p.id == id).findFirst().orElse(null);
    }

    // 更新指定 id 的 Prompt（包括全部字段）
    @PutMapping("/{id}")
    public PromptDto updatePrompt(@PathVariable int id, @RequestBody PromptDto newPrompt) {
        for (PromptDto prompt : promptList) {
            if (prompt.id == id) {
                prompt.content = newPrompt.content;
                prompt.description = newPrompt.description;
                prompt.tags = newPrompt.tags;
                prompt.role = newPrompt.role;
                prompt.instructions = newPrompt.instructions;
                prompt.greeting = newPrompt.greeting;
                prompt.systemPrompt = newPrompt.systemPrompt;
                prompt.isPublic = newPrompt.isPublic;
                // 其他可选：author/likeCount/favoriteCount 不建议前端直接改
                return prompt;
            }
        }
        throw new NoSuchElementException("Prompt not found");
    }

    // 新增 Prompt
    @PostMapping
    public PromptDto createPrompt(@RequestBody PromptDto dto) {
        dto.id = idCounter.getAndIncrement();
        promptList.add(dto);
        return dto;
    }
}
