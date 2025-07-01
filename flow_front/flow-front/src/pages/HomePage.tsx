import { useState } from 'react';
import { Layout, Card, Avatar, List, Typography, Input } from 'antd';
import { PlusCircleOutlined, SearchOutlined } from '@ant-design/icons';

const { Sider, Content } = Layout;
const {Text } = Typography;

// 模拟助手/会话列表
const assistantList = [
    {
        id: 1,
        name: '美食评论员',
        avatar: '😋',
        desc: '美食点评专家，帮你点评每一道菜。',
        lastMsg: '06-22',
    },
    {
        id: 2,
        name: '学术写作助手',
        avatar: '📄',
        desc: '自动帮你润色和生成学术论文段落。',
        lastMsg: '06-20',
    },
];

// 推荐助手卡片数据
const recommendList = [
    {
        avatar: '🐢',
        title: '海龟汤主持人',
        desc: '一个有趣的海龟汤主持人，需要已提供汤面、汤底与关键点（请中的判定条件）。'
    },
    {
        avatar: '😋',
        title: '美食评论员',
        desc: '美食点评专家'
    },
    {
        avatar: '📄',
        title: '学术写作助手',
        desc: '专业的学术研究论文写作和正式文档润色专家'
    },
    {
        avatar: '🔷',
        title: 'Minecraft资深开发者',
        desc: '擅长高级 Java 开发及 Minecraft 开发'
    }
];

const HomePage = () => {
    const [search, setSearch] = useState('');

    return (
        <Layout style={{ minHeight: '100vh', background: '#18181c' }}>
            <Sider width={260} style={{ background: '#232336', borderRight: '1px solid #23232c' }}>
                <div style={{ padding: 28, fontWeight: 700, fontSize: 24, color: '#fff', letterSpacing: 2 }}>
                    LobeHub
                </div>
                <div style={{ padding: '0 18px 12px 18px' }}>
                    <Input
                        prefix={<SearchOutlined />}
                        placeholder="搜索助手/会话"
                        style={{ borderRadius: 16, background: '#242432', color: '#fff' }}
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                    />
                </div>
                <List
                    style={{ background: 'transparent', color: '#fff', paddingLeft: 8 }}
                    itemLayout="horizontal"
                    dataSource={assistantList}
                    renderItem={item => (
                        <List.Item style={{ padding: '10px 0', cursor: 'pointer' }}>
                            <List.Item.Meta
                                avatar={<Avatar style={{ background: '#4747d1', fontSize: 20 }}>{item.avatar}</Avatar>}
                                title={<span style={{ color: '#fff', fontWeight: 500 }}>{item.name}</span>}
                                description={<span style={{ color: '#aaa' }}>{item.desc}</span>}
                            />
                            <span style={{ color: '#888', fontSize: 12 }}>{item.lastMsg}</span>
                        </List.Item>
                    )}
                />
                <div style={{ textAlign: 'center', margin: '24px 0' }}>
                    <PlusCircleOutlined style={{ color: '#4d8ef7', fontSize: 28 }} />
                    <div style={{ color: '#4d8ef7', marginTop: 4 }}>新建助手</div>
                </div>
            </Sider>
            <Layout>
                <Content style={{ padding: '32px 42px', minHeight: 800 }}>
                    <h2 style={{ color: '#222', fontSize: 36, fontWeight: 700 }}>
                        👋 晚上好
                    </h2>
                    <Text style={{ color: '#b0b1ba', fontSize: 18 }}>
                        我是您的私人智能助理 LobeChat。请问现在能帮您做什么？<br />
                        如果需要获得更加定制的助理，可以点击 <b style={{ color: '#4d8ef7' }}>创建自定义助手</b>。
                    </Text>
                    <div style={{ margin: '32px 0 8px 0', display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 28 }}>
                        {recommendList.map(item => (
                            <Card
                                key={item.title}
                                hoverable
                                style={{
                                    background: '#222232',
                                    borderRadius: 20,
                                    color: '#fff',
                                    boxShadow: '0 2px 24px #0003',
                                    minHeight: 120,
                                    display: 'flex',
                                    flexDirection: 'column',
                                    justifyContent: 'center',
                                }}
                            >
                                <div style={{ fontSize: 38, marginBottom: 8 }}>{item.avatar}</div>
                                <div style={{ fontWeight: 600, fontSize: 19 }}>{item.title}</div>
                                <div style={{ color: '#bbb', marginTop: 5 }}>{item.desc}</div>
                            </Card>
                        ))}
                    </div>
                </Content>
            </Layout>
        </Layout>
    );
};

export default HomePage;
