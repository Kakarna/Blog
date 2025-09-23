# 🚀 博客系统部署指南

## 环境要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少 4GB 内存
- 至少 10GB 磁盘空间

## 快速开始

### 1. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，配置以下参数：
# - MYSQL_ROOT_PASSWORD: MySQL root密码
# - MYSQL_PASSWORD: 应用数据库用户密码  
# - JWT_SECRET: JWT密钥（至少32字符）
# - API_BASE_URL: 后端API地址
```

### 2. 启动所有服务

```bash
# 使用Docker Compose启动
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

### 3. 访问应用

- 🌐 **前端界面**: http://localhost
- 🔧 **后端API**: http://localhost:8080
- 🗄️ **数据库管理**: localhost:3306 (用户: blog_user)
- 📊 **Redis缓存**: localhost:6379

## 服务架构

```
用户请求 → Nginx (80) → Vue前端 (80)
                    ↘ Spring Boot后端 (8080) → MySQL (3306) + Redis (6379)
```

## 环境变量说明

### 必需配置
```env
MYSQL_ROOT_PASSWORD=your_mysql_root_password
MYSQL_PASSWORD=your_mysql_user_password  
JWT_SECRET=your_very_secure_jwt_secret_key
```

### 可选配置
```env
# 邮件服务（用于用户注册验证）
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password

# 文件上传
FILE_UPLOAD_DIR=/app/uploads
MAX_FILE_SIZE=10MB

# 自定义API地址
API_BASE_URL=http://your-domain.com:8080
```

## 生产环境部署

### 1. 域名和SSL配置
```bash
# 创建nginx配置目录
mkdir -p nginx/conf.d ssl

# 添加SSL证书到ssl目录
# 配置nginx/conf.d/default.conf
```

### 2. 数据库备份
```bash
# 备份数据库
docker exec blog-mysql mysqldump -u root -p my-blog > backup.sql

# 恢复数据库
docker exec -i blog-mysql mysql -u root -p my-blog < backup.sql
```

### 3. 监控和日志
```bash
# 查看容器日志
docker-compose logs -f backend
docker-compose logs -f frontend

# 监控资源使用
docker stats

# 进入容器调试
docker exec -it blog-backend bash
```

## 故障排除

### 常见问题

1. **端口冲突**
   ```bash
   # 修改docker-compose.yml中的端口映射
   ports:
     - "新端口:容器端口"
   ```

2. **数据库连接失败**
   ```bash
   # 检查MySQL容器状态
   docker-compose logs mysql
   
   # 重置数据库
   docker-compose down -v
   docker-compose up -d
   ```

3. **内存不足**
   ```bash
   # 增加Docker内存分配
   # 或优化JVM参数 in Dockerfile
   ```

4. **构建失败**
   ```bash
   # 清理缓存重新构建
   docker-compose build --no-cache
   ```

## 更新部署

```bash
# 拉取最新代码
git pull origin main

# 重新构建和部署
docker-compose up -d --build

# 只重启特定服务
docker-compose restart backend
```

## 备份和恢复

### 数据库备份
```bash
# 定时备份脚本
docker exec blog-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} my-blog > backup-$(date +%Y%m%d).sql
```

### 完整系统备份
```bash
# 备份整个项目
tar -czf blog-backup-$(date +%Y%m%d).tar.gz . --exclude=node_modules --exclude=target
```

## 性能优化

### 数据库优化
```sql
-- 添加索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_projects_author_id ON projects(author_id);
```

### JVM调优
```dockerfile
# 在Dockerfile中添加JVM参数
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
```

### Nginx缓存
```nginx
# 启用静态资源缓存
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

## 安全建议

1. **更改默认密码**：部署后立即修改所有默认密码
2. **启用SSL**：配置HTTPS加密传输
3. **防火墙配置**：只开放必要端口（80, 443, 8080）
4. **定期更新**：保持Docker镜像和依赖库最新
5. **监控访问**：设置日志监控和异常检测

---

**📞 需要帮助？** 查看日志文件或提交Issue到项目仓库。