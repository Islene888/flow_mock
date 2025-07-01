import React from 'react';
import { Card, Button, Avatar, Tag, Space, message, Tooltip } from 'antd';
import { HeartOutlined, HeartFilled, StarOutlined, StarFilled } from '@ant-design/icons';
import { useToggle, useRequest } from 'ahooks'; // ahooks 是一个优秀的 React Hooks 库，简化异步和状态逻辑
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // 新增

import { type Prompt } from '../types'; // 引入我们定义好的类型
import styles from './PromptCard.module.css';

interface PromptCardProps {
    prompt: Prompt;
    userId?: string;
}

// 模拟点赞/收藏的 API 调用
const handleUserAction = async (
    promptId: number,
    userId: string,
    action: 'like' | 'favorite',
    state: boolean
) => {
    const url = `/api/prompts/${promptId}/${action}?userId=${userId}`;
    if (state) {
        // 如果当前是激活状态，则取消
        return axios.delete(url);
    }
    // 否则，激活
    return axios.post(url);
};

const PromptCard: React.FC<PromptCardProps> = ({ prompt, userId }) => {
    // ahooks 的 useToggle 可以优雅地处理布尔值的切换
    const [liked, { toggle: toggleLike }] = useToggle(false);
    const [favorited, { toggle: toggleFavorite }] = useToggle(false);

    const navigate = useNavigate(); // 新增

    // ahooks 的 useRequest 可以优雅地处理请求的 loading 和 error 状态
    const { run: runLike, loading: loadingLike } = useRequest(
        () => handleUserAction(prompt.id, userId!, 'like', liked),
        {
            manual: true, // 手动触发
            onSuccess: () => toggleLike(),
            onError: () => message.error('操作失败'),
        }
    );

    const { run: runFav, loading: loadingFav } = useRequest(
        () => handleUserAction(prompt.id, userId!, 'favorite', favorited),
        {
            manual: true,
            onSuccess: () => toggleFavorite(),
            onError: () => message.error('操作失败'),
        }
    );

    const onActionClick = (action: 'like' | 'favorite') => {
        if (!userId) {
            message.warning('请先登录');
            return;
        }
        if (action === 'like') runLike();
        if (action === 'favorite') runFav();
    };

    return (
        <Card className={styles.promptCard} hoverable>
            <div className={styles.content}>{prompt.content}</div>
            {prompt.description && <p className={styles.description}>{prompt.description}</p>}

            <div className={styles.tags}>
                {prompt.tags?.map((tag) => <Tag key={tag}>{tag}</Tag>)}
            </div>

            <div className={styles.footer} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div className={styles.author}>
                    <Avatar src={prompt.author.avatar} size="small" />
                    <span className={styles.authorName}>{prompt.author.name}</span>
                </div>
                <Space className={styles.actions}>
                    <Tooltip title={liked ? '取消点赞' : '点赞'}>
                        <Button
                            icon={liked ? <HeartFilled style={{ color: '#ff4d4f' }} /> : <HeartOutlined />}
                            type="text"
                            shape="circle"
                            onClick={() => onActionClick('like')}
                            loading={loadingLike}
                        />
                    </Tooltip>
                    <span>{prompt.likeCount + (liked ? 1 : 0)}</span>

                    <Tooltip title={favorited ? '取消收藏' : '收藏'}>
                        <Button
                            icon={favorited ? <StarFilled style={{ color: '#ffc53d' }} /> : <StarOutlined />}
                            type="text"
                            shape="circle"
                            onClick={() => onActionClick('favorite')}
                            loading={loadingFav}
                        />
                    </Tooltip>
                    <span>{prompt.favoriteCount + (favorited ? 1 : 0)}</span>
                    {/* 只显示给自己 */}
                    {userId && Number(userId) === Number(prompt.creatorId) && (
                        <Button
                            type="link"
                            size="small"
                            style={{ marginLeft: 8 }}
                            onClick={() => navigate(`/prompt/edit/${prompt.id}`)}
                        >
                            编辑
                        </Button>
                    )}

                </Space>
            </div>
        </Card>
    );
};

export default PromptCard;
