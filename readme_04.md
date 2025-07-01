
## 04 新增功能说明（2024/07）

### 1. **提示词市场 Market 页面**

* 路径：`/market`
* 作用：展示所有 Prompt（提示词）卡片，支持分页。
* 每个卡片包含：

    * 标题
    * 描述（可选）
    * 作者（后端已返回 creatorId，可扩展）
    * 标签（如有，可扩展）
    * 点赞/收藏按钮及数量

### 2. **Prompt 点赞与收藏功能**

#### 前端：

* 每个 PromptCard 组件增加“点赞”“收藏”按钮（点击切换状态，数量实时同步）
* 登录后才能操作（未登录会有提示）

#### 后端接口：

* `POST /api/prompts/{id}/like?userId=xxx`   —— 点赞
* `DELETE /api/prompts/{id}/like?userId=xxx` —— 取消点赞
* `POST /api/prompts/{id}/favorite?userId=xxx`   —— 收藏
* `DELETE /api/prompts/{id}/favorite?userId=xxx` —— 取消收藏
* `GET /api/prompts/{id}/stats` —— 获取某 Prompt 的点赞/收藏数量

### 3. **用户交互与体验**

* 登录状态下才允许点赞/收藏，操作有实时反馈（Antd message）
* 点赞/收藏数据**后端实时保存**，不会丢失

### 4. **后端数据库表结构**

* `prompt`：提示词主表，新增 `like_count`、`favorite_count` 字段（如果需要可自动汇总）
* `user_prompt_action`：存储用户与提示词的点赞、收藏关系

### 5. **常用 API 总览**

| 接口                                                  | 说明           |
| --------------------------------------------------- | ------------ |
| `/api/prompt/list?page=0&size=20`                   | 获取 prompt 列表 |
| `/api/prompts/{id}/like?userId=1` (POST/DELETE)     | 点赞/取消点赞      |
| `/api/prompts/{id}/favorite?userId=1` (POST/DELETE) | 收藏/取消收藏      |
| `/api/prompts/{id}/stats`                           | 点赞/收藏数量      |

---

**示例截图/交互效果**
建议在 readme 里贴上 market 页面的 UI 截图（如你刚刚的图），让别人直观理解。

---

## 升级说明

如果你是从03版本升级，请：

1. 前端 `PromptMarketPage` 页面新增（或升级）`PromptCard` 组件，支持点赞收藏按钮。
2. 后端补充相关接口（参考 controller 示例）。
3. 确保数据库表 user\_prompt\_action 已存在且字段匹配。

---

## 05 新增功能说明（2024/07）

### 3. **Prompt 编辑功能 / Bot 预设配置**

* **入口**：

  * Market 页面、我的Bot列表、Chat页顶部等位置，**仅Bot创建者本人可见“编辑”按钮**
  * 支持跳转至专用编辑页面或在页面内弹出编辑面板

* **可编辑内容：**

  * 标题、内容正文、描述
  * 标签（支持多个标签，可批量编辑）
  * **Bot 角色/预设字段**（重点）

    * 角色设定（如：“你是一位耐心的AI导师”）
    * 行为指令（instructions）
    * 系统Prompt（systemPrompt，影响AI回复风格）
    * 默认开场白（greeting）
    * 是否公开（isPublic）

* **编辑体验：**

  * 编辑页面表单支持字段实时编辑与预览
  * 保存后立即生效，前端自动刷新并展示最新内容
  * 后端接口有严格权限校验（只有作者本人可编辑）
  * 编辑失败有详细错误提示

* **AI 聊天页集成：**

  * Bot 预设面板会在聊天页顶部或侧边栏**实时展示当前Bot的全部预设内容**
  * 用户可随时修改并保存，修改后下一轮AI回复立即体现新预设
  * systemPrompt（角色/风格）将作为 `system` 消息传递到大模型，实现**定制化AI对话体验**
  * greeting 字段作为AI的自动问候语（首次对话自动展示）

* **权限与安全：**

  * 只有Bot创建者本人可见和操作编辑入口
  * 后端二次校验用户身份，防止越权操作

* **可扩展性：**

  * 未来可支持富文本编辑、插图、样例对话等高级设置
  * Bot 预设可导出/导入，便于跨项目复用

---
