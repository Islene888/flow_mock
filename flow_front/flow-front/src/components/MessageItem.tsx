import React from 'react';
import { type Message } from '../types';
import styles from './MessageItem.module.css';

const MessageItem: React.FC<{ message: Message }> = ({ message }) => {
    const isUser = message.role === 'user';

    // 使用 classnames 库可以更优雅地处理多个 class
    const messageClass = isUser ? `${styles.message} ${styles.user}` : `${styles.message} ${styles.assistant}`;

    return (
        <div className={messageClass}>
            <div className={styles.content}>{message.content}</div>
        </div>
    );
};

export default MessageItem;