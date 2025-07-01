// 这个文件用来存放项目中所有共享的 TypeScript 类型

export interface Prompt {
    id: number;
    content: string;
    description?: string;
    tags?: string[];
    likeCount: number;
    creatorId: number | string;
    favoriteCount: number;
    author: {
        name: string;
        avatar: string;
    };

    // 👇 新增 Bot 预设相关字段
    role?: string;
    instructions?: string;
    greeting?: string;
    systemPrompt?: string;
    isPublic?: boolean;
}

export interface User {
    id: string;
    name: string;
    avatar?: string;
}

// 👇 现在新增 Message 类型
export interface Message {
    id: number | string;
    role: 'user' | 'assistant' | 'system';
    content: string;
}
