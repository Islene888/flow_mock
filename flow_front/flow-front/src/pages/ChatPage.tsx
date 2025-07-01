import { useState } from "react";
import { Typography, Input, Button, Avatar } from "antd";
const { Title } = Typography;

const ChatPage = () => {
    const [messages, setMessages] = useState([
        { id: 1, role: 'bot', content: '你好，我是日报助理，可以帮你生成周报。' },
        { id: 2, role: 'user', content: '帮我写个今日周报' }
    ]);
    const [input, setInput] = useState("");

    const handleSend = () => {
        if (!input.trim()) return;
        setMessages([...messages, { id: Date.now(), role: "user", content: input }]);
        setInput("");
        // 这里可以接后端，补充AI回复
    };

    return (
        <div style={{ padding: 40, maxWidth: 780, margin: '0 auto' }}>
            <Title level={3} style={{ color: "#fff" }}>与助手对话</Title>
            <div style={{
                minHeight: 360, background: "#222", borderRadius: 18,
                padding: 20, marginBottom: 24, color: "#fff"
            }}>
                {messages.map(msg => (
                    <div key={msg.id} style={{
                        display: "flex", alignItems: "flex-start",
                        margin: "14px 0", flexDirection: msg.role === "user" ? "row-reverse" : "row"
                    }}>
                        <Avatar style={{ background: msg.role === "user" ? "#4d8ef7" : "#ffc53d" }}>
                            {msg.role === "user" ? "U" : "B"}
                        </Avatar>
                        <div style={{
                            background: msg.role === "user" ? "#324fff" : "#444",
                            color: "#fff", padding: "7px 16px",
                            borderRadius: 16, margin: "0 12px", maxWidth: 480
                        }}>
                            {msg.content}
                        </div>
                    </div>
                ))}
            </div>
            <div style={{ display: "flex", gap: 12 }}>
                <Input
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    onPressEnter={handleSend}
                    placeholder="请输入内容..."
                />
                <Button type="primary" onClick={handleSend}>发送</Button>
            </div>
        </div>
    );
};
export default ChatPage;
