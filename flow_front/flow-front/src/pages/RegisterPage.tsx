import { useState } from 'react';
import { register } from '../api/auth';
import { Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';

export default function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async () => {
        setLoading(true);
        try {
            const res = await register(username, password, name);
            if (res.data.success) {
                message.success('注册成功，即将跳转登录页');
                setTimeout(() => navigate('/login'), 1200); // 注册成功1.2秒后跳转
            } else {
                message.error(res.data.message || '注册失败');
            }
        } catch {
            message.error('接口异常');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: 'auto', paddingTop: 100 }}>
            <h2>注册</h2>
            <Input
                placeholder="用户名"
                value={username}
                onChange={e => setUsername(e.target.value)}
                style={{ marginBottom: 16 }}
            />
            <Input
                placeholder="昵称"
                value={name}
                onChange={e => setName(e.target.value)}
                style={{ marginBottom: 16 }}
            />
            <Input.Password
                placeholder="密码"
                value={password}
                onChange={e => setPassword(e.target.value)}
                style={{ marginBottom: 16 }}
            />
            <Button block type="primary" loading={loading} onClick={handleRegister}>
                注册
            </Button>
            <div style={{ marginTop: 24, textAlign: 'center' }}>
                已有账号？<a href="/login">去登录</a>
            </div>
        </div>
    );
}
