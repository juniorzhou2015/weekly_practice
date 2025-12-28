# PD2 管理后台门户（FastAPI + 原生前端 + Tailwind）

本项目位于仓库子目录：`pd2-admin-codebuddy-1221/`，用于实现“顶部一级导航 + 二级导航 + 动态内容区”的管理后台门户。

## 运行方式

### Docker Compose（推荐：一键启动 App + MySQL）

> **注意**：默认密码仅用于快速启动，外网部署请务必修改（见下方“外网可访问部署”）。

```bash
cd /workspace/pd2-admin-codebuddy-1221
docker compose up -d --build
```

访问：`http://<服务器公网IP>:8000/`

### 本地运行（需要可用的 MySQL）

1. 安装依赖

```bash
cd /workspace/pd2-admin-codebuddy-1221
pip install -r requirements.txt
```

2. 配置数据库（推荐用环境变量覆盖敏感信息）

- 环境变量优先：`PD2_MYSQL_DSN`
- 否则读取 `config.json` 的 `db.mysql_dsn`

示例：

```bash
export PD2_MYSQL_DSN='mysql+asyncmy://pd2:pd2@127.0.0.1:3306/pd2_portal?charset=utf8mb4'
```

3. 启动

```bash
uvicorn main:app --reload --port 8000
```

打开：`http://localhost:8000/`

### Docker 运行

```bash
docker build -t pd2-portal .
docker run --rm -p 8000:8000 -e PD2_MYSQL_DSN='mysql+asyncmy://pd2:pd2@<mysql-host>:3306/pd2_portal?charset=utf8mb4' pd2-portal
```

## 外网可访问部署（稳定&可维护）

- **最小方式（直接暴露端口）**
  - 服务器放行安全组/防火墙端口：`8000/tcp`
  - 启动：`docker compose up -d --build`
  - 访问：`http://<公网IP>:8000/`
- **推荐方式（域名 + Nginx 反代 + HTTPS）**
  - Nginx 监听 `443`，反代到 `127.0.0.1:8000`
  - 用 Let’s Encrypt 配置证书（生产更安全）
  - 只对外开放 `80/443`，不直接暴露 `8000`

## 目录结构

```text
pd2-admin-codebuddy-1221/
├── main.py
├── config.json
├── requirements.txt
├── Dockerfile
├── app/                # 后端代码（routers/models/schemas/db）
├── static/             # 前端静态资源（原生 JS + Tailwind CDN）
└── uploads/            # 上传文件落盘目录（容器内/本机）
```

## RESTful 路由概览（部分）

- 首页总览
  - `/api/home/business-boards`
  - `/api/home/team-members`（含：`/birthday-today`）
  - `/api/home/architecture-modules`
  - `/api/home/team-moments`（媒体上传/轮播）
- 目标管理（OKR）
  - `/api/goals/{category}/objectives`
  - `/api/goals/{category}/objectives/{id}/key-results`
  - `/api/goals/{category}/key-results/{id}/tasks`
  - `/api/goals/{category}/snapshots`
  - `/api/goals/{category}/evaluation`
- 值班管理
  - `/api/duty/events`（含：`/next` 提醒）
  - `/api/duty/schedules`
- 经验沉淀
  - `/api/experience/{category}/entries`
- 会议纪要（顶部右侧入口）
  - `/api/minutes`

## 数据一致性与可维护性说明

- 采用外键约束表达 OKR 三层关系（Objective → KeyResult → Task）
- `sort_order` 用于排序；`updated_at` 用于周报/筛选
- 启动时自动 `create_all` 建表（最小复杂度）；生产环境可替换为迁移工具（Alembic）

