package com.ella.flow_mock.controller;

import com.ella.flow_mock.common.ApiResponse; // 记得import你自己的ApiResponse
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAiService openAiService;

    public ChatController(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    // 前端发过来的消息体
    public static class ChatRequest {
        public List<ChatMessageDto> messages;
    }
    public static class ChatMessageDto {
        public String role;
        public String content;
    }

    // 返回前端的数据体
    public static class ChatResponse {
        public String role;
        public String content;
    }

    @PostMapping
    public ApiResponse<ChatResponse> chat(@RequestBody ChatRequest req) {
        try {
            List<ChatMessage> openAiMessages = req.messages.stream()
                    .map(dto -> new ChatMessage(dto.role, dto.content))
                    .collect(Collectors.toList());

            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model("gpt-4-turbo")
                    .messages(openAiMessages)
                    .maxTokens(1500)
                    .build();

            String result = openAiService.createChatCompletion(completionRequest)
                    .getChoices().get(0).getMessage().getContent();

            ChatResponse resp = new ChatResponse();
            resp.role = "assistant";
            resp.content = result;

            return ApiResponse.success(resp);

        } catch (Exception e) {
            e.printStackTrace();
            // 返回标准的 error 响应
            return ApiResponse.error("抱歉，服务器出了一点小问题，请稍后再试。");
        }
    }
}
