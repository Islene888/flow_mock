import React, { useEffect, useState } from 'react';
import { Typography, Button, Empty } from 'antd';
import PromptCard from '../components/PromptCard';
import { useAuth } from '../hooks/useAuth';
import axios from 'axios';
import { type Prompt } from '../types'; // 确保类型已引入

const { Title } = Typography;

const MyBotsPage: React.FC = () => {
    const { user } = useAuth();
    const [myPrompts, setMyPrompts] = useState<Prompt[]>([]); // 类型明确！

    useEffect(() => {
        if (!user?.id) return;
        axios.get('/api/prompts')
            .then(res => {
                // 只看自己创建的助手
                const allPrompts: Prompt[] = res.data || [];
                setMyPrompts(allPrompts.filter(p => String(p.creatorId) === String(user.id)));
            });
    }, [user?.id]);

    return (
        <div style={{ padding: 40, maxWidth: 1200, margin: "0 auto" }}>
            <Title level={2} style={{ color: "#fff" }}>⭐️ 我的助手</Title>
            <Button type="primary" style={{ margin: "12px 0" }}>新建助手</Button>
            <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))',
                gap: 32,
                marginTop: 30
            }}>
                {myPrompts.length === 0 ? (
                    <Empty description="还没有创建过助手" />
                ) : (
                    myPrompts.map(prompt =>
                        <PromptCard
                            prompt={prompt}
                            key={prompt.id}
                            userId={user?.id}
                        />
                    )
                )}
            </div>
        </div>
    );
};

export default MyBotsPage;
