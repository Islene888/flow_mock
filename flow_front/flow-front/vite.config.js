// vite.config.js (请使用这个最终版本)

import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            // 使用对象形式进行更详细的配置
            '/api': {
                target: 'http://localhost:8080', // 目标后端地址
                changeOrigin: true,              // 关键：必须设置为 true
            },
        },
    },
});