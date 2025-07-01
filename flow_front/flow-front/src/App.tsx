import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from 'antd';
import Sidebar from './components/Sidebar';
import ChatPage from './pages/ChatPage';
import PromptMarketPage from './pages/PromptMarketPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import RequireAuth from './components/RequireAuth'; // 新增
import './styles/App.css';

const { Sider, Content } = Layout;

function MainLayout() {
    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider width={220} style={{ background: '#111' }}>
                <Sidebar />
            </Sider>
            <Layout>
                <Content className="app-content" style={{ background: '#222', minWidth: 0 }}>
                    <Routes>
                        <Route path="/" element={<ChatPage />} />
                        <Route path="/chat/:botId" element={<ChatPage />} />
                        <Route path="/market" element={<PromptMarketPage />} />
                        <Route path="*" element={<Navigate to="/" />} />
                    </Routes>
                </Content>
            </Layout>
        </Layout>
    );
}

function App() {
    return (
        <Router>
            <Routes>
                {/* 登录/注册不做守卫 */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                {/* 其它页面加登录校验 */}
                <Route
                    path="/*"
                    element={
                        <RequireAuth>
                            <MainLayout />
                        </RequireAuth>
                    }
                />
            </Routes>
        </Router>
    );
}


export default App;
