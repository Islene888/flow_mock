import { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { useNavigate } from 'react-router-dom';

export default function LoginPage() {
    const { user, login } = useAuth();
    const navigate = useNavigate();
    const [form, setForm] = useState({ username: '', password: '' });
    const [error, setError] = useState('');

    // 已登录自动跳首页
    useEffect(() => {
        if (user) navigate('/', { replace: true });
    }, [user, navigate]);

    async function handleLogin(e: React.FormEvent) {
        e.preventDefault();
        const res = await login(form.username, form.password);
        if (res.success) {
            // user 会自动变化，useEffect 会触发跳转
        } else {
            setError(res.message || '登录失败');
        }
    }

    return (
        <div className="login-page">
            <h2>登录</h2>
            <form onSubmit={handleLogin}>
                <input
                    value={form.username}
                    onChange={e => setForm(f => ({ ...f, username: e.target.value }))}
                    placeholder="用户名"
                />
                <input
                    type="password"
                    value={form.password}
                    onChange={e => setForm(f => ({ ...f, password: e.target.value }))}
                    placeholder="密码"
                />
                <button type="submit">登录</button>
                {error && <div style={{ color: 'red' }}>{error}</div>}
            </form>
            <div>
                没有账号？<a href="/register">注册</a>
            </div>
        </div>
    );
}
