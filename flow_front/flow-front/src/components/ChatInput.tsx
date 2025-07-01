import { useState } from 'react';
import { Button, Input } from 'antd';
import styles from './ChatInput.module.css'; // 引入 CSS Module

interface ChatInputProps {
    onSend: (msg: string) => void;
    loading?: boolean; // 接收 loading 状态
}

const ChatInput = ({ onSend, loading = false }: ChatInputProps) => {
    const [msg, setMsg] = useState('');

    const send = () => {
        if (!msg || loading) return; // 加载中或消息为空时不允许发送
        onSend(msg);
        setMsg('');
    };

    return (
        <div className={styles.chatInputContainer}>
            <Input
                value={msg}
                onChange={e => setMsg(e.target.value)}
                onPressEnter={send}
                placeholder="请输入你的问题…"
                disabled={loading}
                size="large"
            />
            <Button type="primary" onClick={send} loading={loading} size="large">
                发送
            </Button>
        </div>
    );
};

export default ChatInput;