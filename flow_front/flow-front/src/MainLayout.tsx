// src/MainLayout.tsx
import { Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from 'antd';
import Sidebar from './components/Sidebar';
import ChatPage from './pages/ChatPage';
import PromptMarketPage from './pages/PromptMarketPage';
import HomePage from './pages/HomePage';
import MyBotsPage from './pages/MyBotsPage';
import HistoryPage from './pages/HistoryPage';
import ProfilePage from './pages/ProfilePage';

const { Sider, Content } = Layout;

function MainLayout() {
    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider width={220} style={{ background: '#18181c' }}>
                <Sidebar />
            </Sider>
            <Layout>
                <Content className="app-content" style={{ background: '#232336', minWidth: 0 }}>
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/market" element={<PromptMarketPage />} />
                        <Route path="/chat/:botId" element={<ChatPage />} />
                        <Route path="/mybots" element={<MyBotsPage />} />
                        <Route path="/history" element={<HistoryPage />} />
                        <Route path="/profile" element={<ProfilePage />} />
                        <Route path="*" element={<Navigate to="/" />} />
                    </Routes>
                </Content>
            </Layout>
        </Layout>
    );
}

export default MainLayout;
