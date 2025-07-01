import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useChat } from '../hooks/useChat';
import ChatInput from '../components/ChatInput';
import MessageItem from '../components/MessageItem';
import BotPresetPanel from '../components/BotPresetPanel';
import { useAuth } from '../hooks/useAuth';
import axios from 'axios';
import type { Prompt } from '../types';
import styles from './ChatPage.module.css';

const ChatPage: React.FC = () => {
    const { botId } = useParams();      // /chat/:botId
    const { user } = useAuth();
    const [bot, setBot] = useState<Prompt | null>(null);

    // 聊天 hooks
    const { messages, loading, handleSend } = useChat();

    useEffect(() => {
        if (botId) {
            // 有 botId 按原来逻辑
            axios.get(`/api/prompts/${botId}`).then(res => setBot(res.data));
        } else {
            // 没有 botId（首页/随便聊天），拉取第一个 bot 作为默认
            axios.get('/api/prompts').then(res => {
                if (res.data && res.data.length > 0) setBot(res.data[0]);
                else setBot(null);
            });
        }
    }, [botId]);

    return (
        <div className={styles.chatPage}>
            {/* 顶部展示 Bot 预设配置，无论首页还是指定 bot */}
            {bot && (
                <BotPresetPanel
                    bot={bot}
                    editable={user?.id && String(user.id) === String(bot.creatorId)}
                    onUpdated={setBot}
                />
            )}
            <div className={styles.messageList}>
                {messages.map(msg => (
                    <MessageItem key={msg.id} message={msg} />
                ))}
                {loading && <MessageItem message={{ id: 'loading', role: 'assistant', content: '正在思考...' }} />}
            </div>
            <ChatInput onSend={handleSend} loading={loading} />
        </div>
    );
};

export default ChatPage;
