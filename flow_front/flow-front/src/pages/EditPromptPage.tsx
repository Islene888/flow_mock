import { useEffect, useState } from 'react';
import { Form, Input, Button, message, Spin } from 'antd';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const EditPromptPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    const [loading, setLoading] = useState(true);
    const [form] = Form.useForm();

    useEffect(() => {
        axios.get(`/api/prompt/get/${id}`).then(res => {
            form.setFieldsValue(res.data);
        }).finally(() => setLoading(false));
    }, [id, form]);

    const onFinish = async (values: any) => {
        try {
            await axios.put(`/api/prompt/update/${id}?userId=${user.id}`, values);
            message.success('修改成功！');
            navigate('/market');
        } catch (e: any) {
            if (e.response?.status === 403) {
                message.error('无权限修改！');
            } else {
                message.error('修改失败');
            }
        }
    };

    if (loading) return <Spin />;

    return (
        <div style={{ maxWidth: 600, margin: '0 auto', padding: 24 }}>
            <h2>编辑 Prompt</h2>
            <Form form={form} layout="vertical" onFinish={onFinish}>
                <Form.Item name="title" label="标题" rules={[{ required: true, message: '请输入标题' }]}>
                    <Input />
                </Form.Item>
                <Form.Item name="content" label="内容" rules={[{ required: true, message: '请输入内容' }]}>
                    <Input.TextArea rows={4} />
                </Form.Item>
                <Form.Item name="description" label="描述">
                    <Input.TextArea rows={2} />
                </Form.Item>
                <Form.Item name="tags" label="标签">
                    <Input placeholder="用英文逗号分隔，如 chat,write,code" />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">保存修改</Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default EditPromptPage;
