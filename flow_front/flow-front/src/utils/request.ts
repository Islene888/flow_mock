import axios from 'axios';

const instance = axios.create({
    // baseURL: '/api', // 有反向代理可不加
});

instance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('flow_token');
        if (token) config.headers['Authorization'] = `Bearer ${token}`;
        return config;
    },
    error => Promise.reject(error)
);

export default instance;
