import { useCallback, useState } from "react";

const USER_KEY = 'flow_user';
const TOKEN_KEY = 'flow_token';

export function useAuth() {
    // 用户信息
    const [user, setUser] = useState(() => {
        const val = localStorage.getItem(USER_KEY);
        return val ? JSON.parse(val) : null;
    });
    // token
    const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY) || '');

    // 登录
    const login = useCallback(async (username: string, password: string) => {
        try {
            const resp = await fetch('/api/user/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });
            const data = await resp.json();
            // 推荐后端返回 { code:0, data:{ user, token }, msg }
            if (data.code === 0 && data.data) {
                setUser(data.data.user);
                setToken(data.data.token);
                localStorage.setItem(USER_KEY, JSON.stringify(data.data.user));
                localStorage.setItem(TOKEN_KEY, data.data.token);
                return { success: true, user: data.data.user, token: data.data.token };
            } else {
                return { success: false, message: data.msg || '用户名或密码错误' };
            }
        } catch (e: any) {
            return { success: false, message: '网络错误' };
        }
    }, []);

    // 登出
    const logout = useCallback(() => {
        setUser(null);
        setToken('');
        localStorage.removeItem(USER_KEY);
        localStorage.removeItem(TOKEN_KEY);
    }, []);

    // 手动同步（用于多 Tab 切换时刷新 user/token 状态）
    const syncUser = useCallback(() => {
        const u = localStorage.getItem(USER_KEY);
        const t = localStorage.getItem(TOKEN_KEY);
        setUser(u ? JSON.parse(u) : null);
        setToken(t || '');
    }, []);

    return { user, token, login, logout, setUser, setToken, syncUser };
}
