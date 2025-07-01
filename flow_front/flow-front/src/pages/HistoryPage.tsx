import { Card, Avatar, Button, Empty, Typography } from 'antd';
const { Title } = Typography;

const mockHistory = [
    { id: 1, bot: "日报助理", avatar: "https://api.dicebear.com/8.x/miniavs/svg?seed=1", lastMsg: "今天的周报如下..." },
    { id: 2, bot: "美食评论员", avatar: "https://api.dicebear.com/8.x/miniavs/svg?seed=2", lastMsg: "推荐你试试..." }
];

const HistoryPage = () => (
    <div style={{ padding: 40, maxWidth: 900, margin: '0 auto' }}>
        <Title level={2} style={{ color: "#fff" }}>🕑 历史会话</Title>
        <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(380px, 1fr))',
            gap: 28,
            marginTop: 32
        }}>
            {mockHistory.length === 0 ? (
                <Empty description="暂无历史会话" />
            ) : (
                mockHistory.map(item => (
                    <Card key={item.id} style={{ borderRadius: 16 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
                            <Avatar src={item.avatar} size={42} />
                            <div>
                                <div style={{ color: "#333", fontWeight: 600 }}>{item.bot}</div>
                                <div style={{ color: "#888", fontSize: 13 }}>{item.lastMsg}</div>
                            </div>
                            <Button type="link" style={{ marginLeft: 'auto' }}>进入会话</Button>
                        </div>
                    </Card>
                ))
            )}
        </div>
    </div>
);
export default HistoryPage;
