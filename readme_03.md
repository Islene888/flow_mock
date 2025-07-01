
# Flow Mock 微服务 Demo - 第三阶段：前后端联调与 AI 聊天能力

## 阶段简介

在前两个阶段基础上，**本阶段聚焦于前后端分离架构与 OpenAI 智能对话集成**，实现了完整的业务闭环：用户通过 Web 前端页面输入消息，后端自动转发请求到 OpenAI API，返回自然语言回复，真正具备了生产级 AI 聊天机器人雏形。

---

## 主要特性与亮点

### 1. 前后端分离 + 跨端联调

* **前端采用 React + Vite**，开发体验极致、热更新快。
* 使用 Vite `proxy` 功能，开发环境下自动将 `/api` 请求转发至 Spring Boot 后端，无需手动解决跨域问题。

### 2. 前端对接 Chat API

* 封装 `useChat` hook，实现消息历史管理和自动滚动、加载动画等体验优化。
* 对接 `/api/chat` 接口，实现用户问题的多轮上下文提问和 AI 智能回复。

### 3. OpenAI 聊天能力集成

* 后端新增 `/api/chat` POST 接口，接收前端消息历史，自动调用 OpenAI GPT 模型生成回复。
* 支持中英文混合提问、长上下文多轮会话。
* 统一返回格式，便于前端渲染和后续多模态扩展。

---

## 前端开发快速指南

### 1. 安装依赖并启动开发环境

```bash
cd flow_front/flow-front
npm install
npm run dev
```

默认本地地址：[http://localhost:5173](http://localhost:5173)

### 2. 关键配置（`vite.config.js`）

```js
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
            },
        },
    },
});
```

### 3. 核心前端逻辑（`useChat.ts`）

```ts
import axios from 'axios';

const handleSend = async (msg: string) => {
    // 前端发送多轮历史
    const res = await axios.post('/api/chat', {
        messages: [...历史消息, { role: 'user', content: msg }]
    });
    // 处理 AI 返回内容
    setMessages([...messages, { role: 'assistant', content: res.data.content }]);
};
```

### 4. 效果预览

* 支持流畅中英文对话、多轮上下文记忆。
* 聊天历史本地管理，界面响应式。
* 服务启动后，输入内容即可实时获取 AI 回复。

---

## 测试与联调

1. **前端页面发送消息，查看是否能收到智能回复**
2. **后端日志实时打印请求流转过程，便于排查 API、数据库与 OpenAI 整体链路**
3. **可用 curl 或 Postman 直接测试后端 `/api/chat`，确认独立可用**

---

## 总结

本阶段完成后，系统已实现：

* 微服务架构、MySQL+Redis 持久化、复杂业务 API
* 前后端解耦开发与本地联调
* AI 智能对话能力闭环

后续可进一步扩展用户体系、内容审核、多模态输入输出等更强大场景。

---