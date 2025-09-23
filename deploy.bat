@echo off
echo 🚀 开始部署博客系统...

:: 检查Docker是否安装
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker未安装，请先安装Docker
    pause
    exit /b 1
)

:: 检查Docker Compose是否安装
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker Compose未安装，请先安装Docker Compose
    pause
    exit /b 1
)

:: 复制环境变量文件
if not exist .env (
    echo 📋 创建环境变量文件...
    copy .env.example .env
    echo ⚠️  请编辑 .env 文件配置您的环境变量
    pause
    exit /b 1
)

:: 构建和启动服务
echo 🐳 启动Docker容器...
docker-compose up -d --build

echo ✅ 部署完成！
echo 📊 服务状态：
docker-compose ps

echo 🌐 前端访问：http://localhost
echo 🔧 后端API：http://localhost:8080
echo 🗄️  数据库管理：localhost:3306

echo 📋 查看日志：docker-compose logs -f
pause