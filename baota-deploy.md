# 🎯 宝塔面板部署指南

## 环境要求

- 宝塔面板 7.9+
- Nginx 1.20+
- MySQL 8.0+
- Java 11+
- Node.js 18+

## 部署前准备

### 1. 宝塔面板配置

```bash
# 安装必要软件
- Nginx
- MySQL  
- Java（通过软件商店安装Tomcat或手动安装JDK）
- Node.js（通过软件商店安装PM2）
- Redis（可选，推荐安装）
```

### 2. 创建网站和数据库

1. **创建MySQL数据库**
   - 数据库名: `my-blog`
   - 用户名: `blog_user`
   - 密码: 设置强密码

2. **创建Java站点**
   - 域名: 您的域名或IP
   - JDK版本: 11
   - 端口: 8080（或其他可用端口）

3. **创建前端站点**
   - 域名: 您的域名
   - PHP版本: 纯静态
   - 运行目录: `/dist`

## 后端部署 (Spring Boot)

### 1. 打包后端

```bash
cd my-blog-back
mvn clean package -DskipTests
```

### 2. 上传部署

1. **上传文件到宝塔**
   - 将 `target/*.jar` 上传到Java站点目录
   - 创建 `application.properties` 配置文件

2. **配置文件示例** (`/www/wwwroot/your-domain/application.properties`)
```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/my-blog?useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=blog_user
spring.datasource.password=your_mysql_password

# JWT配置
jwt.secret=your_jwt_secret_key

# 服务器配置
server.port=8080
server.servlet.context-path=/api

# 文件上传
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

3. **启动脚本** (`/www/wwwroot/your-domain/start.sh`)
```bash
#!/bin/bash
JAVA_OPTS="-Xms256m -Xmx512m -Dspring.profiles.active=prod"
nohup java $JAVA_OPTS -jar your-app.jar > app.log 2>&1 &
```

### 3. 宝塔Java项目配置

1. 在宝塔Java项目管理中：
   - 选择JDK版本: 11
   - 项目路径: 上传的jar文件位置
   - 项目端口: 8080
   - 启动命令: `java -jar your-app.jar`

## 前端部署 (Vue.js)

### 1. 构建前端

```bash
cd my-blog-front
npm install
npm run build
```

### 2. 上传文件

1. 将 `dist/` 目录所有文件上传到前端站点目录
2. 配置Nginx反向代理

### 3. Nginx配置

在宝塔Nginx配置中添加：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    root /www/wwwroot/your-domain;
    index index.html;

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # Vue Router history模式
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API代理到后端
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 文件上传大小限制
        client_max_body_size 10M;
    }
}
```

## 数据库初始化

### 1. 导入数据库结构

在宝塔MySQL管理中：
1. 选择创建的数据库
2. 导入 `mysql/init.sql` 文件
3. 或手动执行SQL语句

### 2. 验证数据库连接

```sql
-- 测试数据库连接
SELECT COUNT(*) FROM users;
```

## 自动化部署脚本

### 宝塔WebHook自动部署

1. **创建WebHook**
   - 在宝塔面板 → 软件商店 → 安装WebHook插件
   - 添加新的WebHook

2. **部署脚本** (`/www/server/panel/script/deploy-blog.sh`)
```bash
#!/bin/bash

# 拉取最新代码
cd /www/wwwroot/blog-backend
git pull origin main

# 构建后端
cd my-blog-back
mvn clean package -DskipTests

# 重启Java服务
btjava restart your-app-name

# 构建前端  
cd ../my-blog-front
npm install
npm run build

# 复制到前端目录
rm -rf /www/wwwroot/blog-frontend/*
cp -r dist/* /www/wwwroot/blog-frontend/

echo "✅ 部署完成: $(date)"
```

## 监控和维护

### 1. 日志查看

```bash
# 查看后端日志
tail -f /www/wwwroot/your-domain/app.log

# 查看Nginx访问日志
tail -f /www/wwwlogs/your-domain.log
```

### 2. 性能监控

在宝塔面板中：
- 监控CPU、内存、磁盘使用情况
- 设置流量限制和防火墙规则
- 配置定时任务备份数据库

### 3. 安全配置

1. **SSL证书**
   - 在宝塔SSL管理中申请免费证书
   - 强制HTTPS跳转

2. **防火墙**
   - 只开放80, 443端口
   - 限制API访问频率

3. **定期备份**
   - 设置数据库自动备份
   - 备份应用配置文件

## 故障排除

### 常见问题

1. **端口冲突**
   ```bash
   # 检查端口占用
   netstat -tlnp | grep 8080
   
   # 修改应用端口
   server.port=9090
   ```

2. **数据库连接失败**
   - 检查MySQL用户权限
   - 验证数据库连接字符串

3. **静态资源404**
   - 检查Nginx配置中的root路径
   - 验证文件权限

4. **内存不足**
   ```bash
   # 调整JVM参数
   JAVA_OPTS="-Xms256m -Xmx512m"
   ```

## 优化建议

### 性能优化

1. **CDN加速**
   - 配置静态资源CDN
   - 启用Gzip压缩

2. **数据库优化**
   - 添加合适索引
   - 配置查询缓存

3. **前端优化**
   - 启用组件懒加载
   - 配置资源预加载

### 安全优化

1. **定期更新**
   - 保持系统和依赖更新
   - 监控安全漏洞

2. **访问控制**
   - 配置API访问限制
   - 启用登录验证码

---

**📞 需要帮助？** 查看宝塔日志或联系技术支持。