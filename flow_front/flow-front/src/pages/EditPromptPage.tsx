// EditPromptPage.tsx
import { useEffect, useState } from 'react';
import { Form, Input, Button, message, Spin, Switch } from 'antd';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
// import { useAuth } from '../hooks/useAuth';

const EditPromptPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    // const { user } = useAuth();
    const [loading, setLoading] = useState(true);
    const [form] = Form.useForm();

    useEffect(() => {
        axios.get(`/api/prompts/${id}`).then(res => {
            const data = res.data;
            form.setFieldsValue({
                ...data,
                tags: data.tags?.join(', '), // 数组转字符串显示
            });
        }).finally(() => setLoading(false));
    }, [id, form]);

    const onFinish = async (values: any) => {
        // 前端 tags 字符串转数组
        const tags = values.tags
            ? values.tags.split(',').map((t: string) => t.trim()).filter(Boolean)
            : [];
        const payload = { ...values, tags };
        try {
            await axios.put(`/api/prompts/${id}`, payload);
            message.success('修改成功！');
            navigate('/market');
        } catch (e: any) {
            message.error('修改失败');
        }
    };

    if (loading) return <Spin />;

    return (
        <div style={{ maxWidth: 650, margin: '0 auto', padding: 24 }}>
            <h2>编辑 Prompt</h2>
            <Form form={form} layout="vertical" onFinish={onFinish}>
                <Form.Item name="content" label="内容" rules={[{ required: true }]}>
                    <Input.TextArea rows={2} />
                </Form.Item>
                <Form.Item name="description" label="描述">
                    <Input.TextArea rows={2} />
                </Form.Item>
                <Form.Item name="tags" label="标签（英文逗号分隔）">
                    <Input />
                </Form.Item>
                <Form.Item name="role" label="角色设定">
                    <Input />
                </Form.Item>
                <Form.Item name="instructions" label="行为指令">
                    <Input.TextArea rows={2} />
                </Form.Item>
                <Form.Item name="greeting" label="开场白">
                    <Input />
                </Form.Item>
                <Form.Item name="systemPrompt" label="系统 Prompt">
                    <Input.TextArea rows={2} />
                </Form.Item>
                <Form.Item name="isPublic" label="是否公开" valuePropName="checked">
                    <Switch checkedChildren="公开" unCheckedChildren="私有" />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">保存修改</Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default EditPromptPage;
