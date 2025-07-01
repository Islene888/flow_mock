// src/types/index.ts

export interface Prompt {
    id: number;
    content: string;
    description?: string;
    tags: string[]; // 必选，建议后端给 []
    language?: string; // ← 让 TS 支持 prompt.language
    likeCount: number;
    favoriteCount: number;
    creatorId: number;
    author: {
        name: string;
        avatar: string;
    };
    // Bot 相关字段
    role?: string;
    instructions?: string;
    greeting?: string;
    systemPrompt?: string;
    isPublic?: boolean;
}

export interface User {
    id: number;
    name: string;
    avatar?: string;
}

export interface Message {
    id: number | string;
    role: 'user' | 'assistant' | 'system';
    content: string;
}
