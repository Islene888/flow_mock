// src/components/RequireAuth.tsx
import { useAuth } from '../hooks/useAuth';
import { Navigate, useLocation } from 'react-router-dom';
import type {JSX} from "react";

export default function RequireAuth({ children }: { children: JSX.Element }) {
    const { user } = useAuth();
    const location = useLocation();
    if (!user) {
        // 跳转到登录，带上回跳路径
        return <Navigate to="/login" state={{ from: location }} replace />;
    }
    return children;
}
