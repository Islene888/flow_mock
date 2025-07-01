import { Typography, Avatar, Button } from 'antd';
import { UserOutlined } from '@ant-design/icons';

const { Title } = Typography;

const ProfilePage = () => {
    return (
        <div style={{
            padding: 48,
            maxWidth: 700,
            margin: '0 auto',
            minHeight: 600
        }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
                <UserOutlined style={{ color: '#4d8ef7', fontSize: 38 }} />
                <Title level={1} style={{ color: "#fff", margin: 0, fontWeight: 800, fontSize: 34 }}>个人中心</Title>
            </div>
            <div style={{ marginTop: 36, marginBottom: 16 }}>
                <Avatar size={80} style={{ background: "#4d8ef7", fontSize: 36 }}>U</Avatar>
                <div style={{ color: "#fff", marginTop: 18, fontSize: 18 }}>用户名：Islene Zhao</div>
                <Button style={{ marginTop: 26 }}>编辑资料</Button>
            </div>
        </div>
    );
};
export default ProfilePage;
