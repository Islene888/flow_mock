import { Card, Form, Input, Button, message, Switch } from 'antd';
import { useState } from 'react';
import axios from 'axios';
import type {Prompt} from '../types';

interface BotPresetPanelProps {
    bot: Prompt;
    editable: boolean;
    onUpdated?: (bot: Prompt) => void;
}

const BotPresetPanel: React.FC<BotPresetPanelProps> = ({ bot, editable, onUpdated }) => {
    const [editing, setEditing] = useState(false);
    const [form] = Form.useForm();

    const handleEdit = () => {
        form.setFieldsValue(bot);
        setEditing(true);
    };

    const handleFinish = async (values: any) => {
        try {
            const { data } = await axios.put(`/api/prompts/${bot.id}`, { ...bot, ...values });
            message.success('保存成功');
            setEditing(false);
            onUpdated?.(data); // 通知父组件刷新
        } catch (e) {
            message.error('保存失败');
        }
    };

    if (!editing) {
        return (
            <Card
                title="Bot 预设配置"
                extra={editable && <Button size="small" onClick={handleEdit}>编辑</Button>}
                style={{ marginBottom: 16 }}
            >
                <div><b>角色：</b>{bot.role || '—'}</div>
                <div><b>自定义指令：</b>{bot.instructions || '—'}</div>
                <div><b>开场白：</b>{bot.greeting || '—'}</div>
                <div><b>系统 Prompt：</b>{bot.systemPrompt || '—'}</div>
                <div><b>是否公开：</b>{bot.isPublic ? '是' : '否'}</div>
            </Card>
        );
    }

    return (
        <Card title="编辑 Bot 预设配置">
            <Form form={form} layout="vertical" initialValues={bot} onFinish={handleFinish}>
                <Form.Item name="role" label="角色"><Input /></Form.Item>
                <Form.Item name="instructions" label="自定义指令"><Input.TextArea rows={2} /></Form.Item>
                <Form.Item name="greeting" label="开场白"><Input /></Form.Item>
                <Form.Item name="systemPrompt" label="系统 Prompt"><Input.TextArea rows={2} /></Form.Item>
                <Form.Item name="isPublic" label="是否公开" valuePropName="checked"><Switch /></Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">保存</Button>
                    <Button style={{ marginLeft: 8 }} onClick={() => setEditing(false)}>取消</Button>
                </Form.Item>
            </Form>
        </Card>
    );
};

export default BotPresetPanel;
