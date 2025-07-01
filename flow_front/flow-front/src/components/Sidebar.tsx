// src/components/Sidebar.tsx
import { NavLink } from 'react-router-dom';
import { Menu } from 'antd';
import {
    MessageOutlined,
    AppstoreOutlined,
    StarOutlined,
    UserOutlined,
    HomeOutlined,
} from '@ant-design/icons';

const Sidebar = () => (
    <Menu
        mode="inline"
        theme="dark"
        defaultSelectedKeys={['/']}
        style={{ height: '100%', borderRight: 0, background: '#18181c', color: '#fff' }}
        items={[
            {
                key: '/',
                icon: <HomeOutlined />,
                label: <NavLink to="/">推荐首页</NavLink>,
            },
            {
                key: '/market',
                icon: <AppstoreOutlined />,
                label: <NavLink to="/market">Prompt市场</NavLink>,
            },
            {
                key: '/mybots',
                icon: <StarOutlined />,
                label: <NavLink to="/mybots">我的助手</NavLink>,
            },
            {
                key: '/history',
                icon: <MessageOutlined />,
                label: <NavLink to="/history">历史会话</NavLink>,
            },
            {
                key: '/profile',
                icon: <UserOutlined />,
                label: <NavLink to="/profile">个人中心</NavLink>,
            },
        ]}
    />
);

export default Sidebar;
