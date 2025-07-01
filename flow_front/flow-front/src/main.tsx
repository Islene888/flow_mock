// src/main.tsx

import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';

// 添加下面这一行，引入 antd 的核心样式
import 'antd/dist/reset.css';

import './index.css';


createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <App />
    </StrictMode>,
);