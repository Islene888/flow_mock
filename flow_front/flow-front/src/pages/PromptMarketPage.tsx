import React, { useEffect, useState } from 'react';
import { Alert, Spin, Typography } from 'antd';
import axios from 'axios';
import PromptCard from '../components/PromptCard';
import { useAuth } from '../hooks/useAuth';
import { type Prompt } from '../types'; // 关键：从全局类型文件导入 Prompt

import styles from './PromptMarketPage.module.css';

const { Title } = Typography;

const PromptMarketPage: React.FC = () => {
    const [prompts, setPrompts] = useState<Prompt[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { user } = useAuth();

    useEffect(() => {
        // 我们恢复真实的 API 请求逻辑，这样 setError 就会被使用
        axios.get('/api/prompts') // 假设这是您获取列表的 API
            .then(res => {
                // 确保后端返回的数据结构与 Prompt 类型一致
                setPrompts(res.data || []);
            })
            .catch((err) => {
                console.error(err);
                // 这里使用了 setError，黄色的警告就会消失
                setError("无法加载提示词列表，请稍后再试。");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) return <div className={styles.center}><Spin size="large" /></div>;
    if (error) return <div className={styles.center}><Alert message={error} type="error" showIcon /></div>;

    return (
        <div className={styles.pageContainer}>
            <Title level={2} style={{ color: '#e5e5e5', marginBottom: 24 }}>提示词市场</Title>
            <div className={styles.gridContainer}>
                {prompts.map(prompt => (
                    <PromptCard prompt={prompt} key={prompt.id} userId={user?.id} />
                ))}
            </div>
        </div>
    );
};

export default PromptMarketPage;