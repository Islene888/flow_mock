import React, { useState } from 'react';
import { Card, Button, Avatar, Tag, Space, Tooltip } from 'antd';
import { HeartOutlined, HeartFilled, StarOutlined, StarFilled, EditOutlined, InfoCircleOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { type Prompt } from '../types';

interface Props {
    prompt: Prompt;
    userId?: string | number;
    onShowDetail?: () => void;
}

const PromptCard: React.FC<Props> = ({ prompt, userId, onShowDetail }) => {
    const navigate = useNavigate();
    const [liked, setLiked] = useState(false);
    const [favorited, setFavorited] = useState(false);

    return (
        <Card
            hoverable
            style={{
                borderRadius: 18,
                boxShadow: "0 4px 24px #0002",
                background: '#24243c',
                color: '#fff',
                minHeight: 220
            }}
            bodyStyle={{ padding: 20, display: "flex", flexDirection: "column" }}
        >
            <div style={{ display: "flex", alignItems: "center", marginBottom: 10 }}>
                <Avatar src={prompt.author?.avatar || undefined} size={38} style={{ background: '#888' }}>
                    {prompt.author?.name?.[0] || 'U'}
                </Avatar>
                <span style={{ marginLeft: 12, color: "#fff", fontWeight: 600 }}>
                    {prompt.author?.name || '未知用户'}
                </span>
                <span style={{ marginLeft: "auto", fontSize: 14, color: "#bbb" }}>
                    {prompt.language || ""}
                </span>
            </div>
            <div style={{ fontSize: 17, fontWeight: 600, color: "#e5e5e5", marginBottom: 6 }}>
                {prompt.content}
            </div>
            <div style={{ color: "#aaa", fontSize: 15, marginBottom: 10 }}>{prompt.description}</div>
            <div style={{ marginBottom: 12 }}>
                {(prompt.tags ?? []).map(tag => <Tag color="geekblue" key={tag}>{tag}</Tag>)}
            </div>
            <Space>
                <Tooltip title={liked ? "取消点赞" : "点赞"}>
                    <Button
                        icon={liked ? <HeartFilled style={{ color: "#ff4d4f" }} /> : <HeartOutlined />}
                        type="text"
                        onClick={() => setLiked(l => !l)}
                    />
                </Tooltip>
                <span style={{ color: "#ffbaba" }}>{prompt.likeCount + (liked ? 1 : 0)}</span>
                <Tooltip title={favorited ? "取消收藏" : "收藏"}>
                    <Button
                        icon={favorited ? <StarFilled style={{ color: "#ffc53d" }} /> : <StarOutlined />}
                        type="text"
                        onClick={() => setFavorited(f => !f)}
                    />
                </Tooltip>
                <span style={{ color: "#ffeaa7" }}>{prompt.favoriteCount + (favorited ? 1 : 0)}</span>
                <Tooltip title="详情">
                    <Button icon={<InfoCircleOutlined />} type="text" onClick={onShowDetail} />
                </Tooltip>
                {userId && Number(userId) === Number(prompt.creatorId) && (
                    <Tooltip title="编辑">
                        <Button
                            icon={<EditOutlined />}
                            type="text"
                            onClick={() => navigate(`/prompt/edit/${prompt.id}`)}
                        />
                    </Tooltip>
                )}
            </Space>
        </Card>
    );
};
export default PromptCard;
