// src/pages/PromptMarketPage.tsx
import React, { useEffect, useState } from 'react';
import { Alert, Spin, Typography, Input, Button, Modal } from 'antd';
import axios from 'axios';
import PromptCard from '../components/PromptCard';
import { useAuth } from '../hooks/useAuth';
import { type Prompt } from '../types';

const { Title } = Typography;

const PromptMarketPage: React.FC = () => {
    const [prompts, setPrompts] = useState<Prompt[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [search, setSearch] = useState('');
    const { user } = useAuth();

    // 详情弹窗
    const [detailPrompt, setDetailPrompt] = useState<Prompt | null>(null);

    useEffect(() => {
        axios.get('/api/prompts')
            .then(res => setPrompts(res.data || []))
            .catch((_err) => {
                setError("无法加载提示词列表，请稍后再试。");
            })
            .finally(() => setLoading(false));
    }, []);

    // 搜索过滤
    const filteredPrompts = prompts.filter(p =>
        p.content.toLowerCase().includes(search.toLowerCase()) ||
        (p.description && p.description.toLowerCase().includes(search.toLowerCase()))
    );

    if (loading) return <div style={{textAlign:'center',padding:'50px'}}><Spin size="large" /></div>;
    if (error) return <div style={{textAlign:'center',padding:'50px'}}><Alert message={error} type="error" showIcon /></div>;

    return (
        <div style={{ maxWidth: 1200, margin: '0 auto', padding: '40px 0' }}>
            <div style={{display:'flex',alignItems:'center',marginBottom:20}}>
                <Title level={2} style={{ color: '#fff', flex: 1 }}>提示词市场</Title>
                <Input.Search
                    placeholder="搜索内容/描述"
                    allowClear
                    style={{ width: 260, marginRight: 16, background:'#232336'}}
                    onChange={e => setSearch(e.target.value)}
                />
                <Button type="primary" >新建Prompt</Button>
            </div>
            <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))',
                gap: 32,
            }}>
                {filteredPrompts.map(prompt =>
                    <PromptCard
                        prompt={prompt}
                        key={prompt.id}
                        userId={user?.id}
                        onShowDetail={() => setDetailPrompt(prompt)}
                    />
                )}
            </div>
            {/* 详情弹窗 */}
            <Modal
                open={!!detailPrompt}
                onCancel={() => setDetailPrompt(null)}
                footer={null}
                width={520}
                centered
                title={<b>Prompt 详情</b>}
            >
                {detailPrompt && (
                    <div style={{padding:12}}>
                        <h3>{detailPrompt.content}</h3>
                        <p style={{color:'#888'}}>{detailPrompt.description}</p>
                        <div>标签：{detailPrompt.tags?.map(tag => <span key={tag} style={{marginRight:8}}>{tag}</span>)}</div>
                        <div style={{marginTop:8}}>作者：{detailPrompt.author?.name}</div>
                        {/* 其它内容随需补充 */}
                    </div>
                )}
            </Modal>
        </div>
    );
};
export default PromptMarketPage;
