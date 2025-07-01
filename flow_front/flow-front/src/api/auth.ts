import axios from 'axios';

export const register = (username: string, password: string, name?: string) =>
    axios.post('/api/user/register', { username, password, name });

export const login = (username: string, password: string) =>
    axios.post('/api/user/login', { username, password });
