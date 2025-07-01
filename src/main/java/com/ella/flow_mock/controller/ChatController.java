package com.ella.flow_mock.controller;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAiService openAiService;

    // 构造函数：当ChatController被创建时，会从配置文件读取密钥并创建OpenAiService实例
    public ChatController(@Value("${openai.api.key}") String apiKey) {
        // 创建OpenAiService时，增加超时时间，避免长时间等待
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    // === 1. 定义前端发送给后端的数据结构 ===
    // 我们约定前端会发送一个包含所有历史消息的列表过来
    public static class ChatRequest {
        public List<ChatMessageDto> messages;
    }

    // 这个是内部使用，用来匹配前端传来的每条消息的格式
    public static class ChatMessageDto {
        public String role;
        public String content;
    }


    // === 2. 定义后端返回给前端的数据结构 ===
    // 这个结构要和我们之前在前端React代码里约定的一样
    public static class ChatResponse {
        public String role;
        public String content;
    }


    // === 3. API主方法 ===
    // @PostMapping注解表示这是一个处理POST请求的接口
    // ResponseEntity<ChatResponse> 表示我们会返回一个带有HTTP状态码的响应体
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {

        // 用 try-catch 把代码包起来，这样如果调用OpenAI出错，程序不会崩溃
        try {
            // --- 核心逻辑开始 ---

            // 将前端传来的消息列表(ChatMessageDto)转换成OpenAI库需要的消息列表(ChatMessage)
            List<ChatMessage> openAiMessages = req.messages.stream()
                    .map(dto -> new ChatMessage(dto.role, dto.content))
                    .collect(Collectors.toList());

            // 创建一个发送给OpenAI的请求对象，其中包含了模型名称、完整的消息历史和最大token数
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model("gpt-4-turbo")// 您可以根据需要更换模型
                    .messages(openAiMessages)
                    .maxTokens(1500)
                    .build();

            // 调用OpenAI API，获取返回结果
            // .getChoices().get(0).getMessage().getContent() 是从复杂的返回结果中提取出AI的回复文字
            String result = openAiService.createChatCompletion(completionRequest)
                    .getChoices().get(0).getMessage().getContent();

            // 按照我们和前端约定的格式，构建返回对象
            ChatResponse resp = new ChatResponse();
            resp.role = "assistant"; // AI的角色是"assistant"
            resp.content = result;   // AI的回复内容

            // --- 核心逻辑结束 ---

            // 如果一切顺利，返回 200 OK 状态码和成功的响应数据
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            // 如果在 try 代码块中发生了任何错误（比如网络问题、API密钥错误等）
            e.printStackTrace(); // 在服务器后台打印详细的错误日志，方便我们自己排查问题

            // 创建一个对用户友好的错误提示
            ChatResponse errorResp = new ChatResponse();
            errorResp.role = "assistant";
            errorResp.content = "抱歉，服务器出了一点小问题，请稍后再试。";

            // 向前端返回 500 Internal Server Error 状态码和上面的错误提示
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResp);
        }
    }
}