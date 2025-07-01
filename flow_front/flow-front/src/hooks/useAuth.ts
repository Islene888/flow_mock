import { useState, useCallback } from 'react';

const USER_KEY = 'flow_user';

export function useAuth() {
    // 初始化时从 localStorage 获取
    const [user, setUser] = useState(() => {
        const val = localStorage.getItem(USER_KEY);
        return val ? JSON.parse(val) : null;
    });

    // 登录
    const login = useCallback(async (username: string, password: string) => {
        try {
            const resp = await fetch('/api/user/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });
            const data = await resp.json();
            if (data.success) {
                setUser(data.data);
                localStorage.setItem(USER_KEY, JSON.stringify(data.data));
                return { success: true, user: data.data };
            } else {
                return { success: false, message: data.message || '用户名或密码错误' };
            }
        } catch (e: any) {
            return { success: false, message: '网络错误' };
        }
    }, []);

    // 登出
    const logout = useCallback(() => {
        setUser(null);
        localStorage.removeItem(USER_KEY);
    }, []);

    // 手动同步（比如切换 tab 或外部修改 localStorage 后）
    const syncUser = useCallback(() => {
        const val = localStorage.getItem(USER_KEY);
        setUser(val ? JSON.parse(val) : null);
    }, []);

    return { user, login, logout, setUser, syncUser };
}
