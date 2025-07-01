import { useState } from 'react';
import axios from 'axios';
import { type Message, type Prompt } from '../types';

const initialMessages: Message[] = [
    { id: 1, role: 'user', content: '你好' },
    { id: 2, role: 'assistant', content: '您好，有什么可以帮您？' },
];

export const useChat = (bot?: Prompt) => {
    const [messages, setMessages] = useState<Message[]>(initialMessages);
    const [loading, setLoading] = useState(false);

    const handleSend = async (msg: string) => {
        const userMessage: Message = { id: Date.now(), role: 'user', content: msg };

        // 拼接 system prompt
        let systemContent = '';
        if (bot) {
            systemContent = [bot.role, bot.instructions, bot.systemPrompt]
                .filter(Boolean)
                .join('。');
        }

        // 只在第一次对话时加 greeting
        const isFirst = messages.length === 0;
        const newMessages: Message[] = [
            ...(systemContent
                ? [{ id: Date.now() - 2, role: 'system' as const, content: systemContent }]
                : []),
            ...(isFirst && bot && bot.greeting
                ? [{ id: Date.now() - 1, role: 'assistant' as const, content: bot.greeting }]
                : []),
            ...messages,
            userMessage
        ];




        setMessages(newMessages);
        setLoading(true);

        try {
            // 发送全部消息和 botId
            const res = await axios.post('/api/chat', {
                messages: newMessages,
                botId: bot?.id,
            });

            const assistantMessage: Message = {
                id: Date.now() + 1,
                role: res.data.role,
                content: res.data.content,
            };

            setMessages(prevMessages => [...prevMessages, assistantMessage]);
        } catch (e) {
            console.error("请求失败:", e);
            setMessages(prevMessages => [
                ...prevMessages,
                { id: Date.now() + 2, role: 'assistant', content: '请求失败，请稍后再试。' }
            ]);
        } finally {
            setLoading(false);
        }
    };

    return { messages, loading, handleSend };
};
