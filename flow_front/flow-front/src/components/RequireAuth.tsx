import { useAuth } from '../hooks/useAuth';
import { Navigate, useLocation } from 'react-router-dom';
import type { JSX } from "react";

export default function RequireAuth({ children }: { children: JSX.Element }) {
    const { user, token } = useAuth();
    const location = useLocation();

    // 如果未登录或者没有有效 token，则跳转登录页
    if (!user || !token) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }
    return children;
}
