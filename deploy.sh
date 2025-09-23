#!/bin/bash

# 部署脚本
set -e

echo "🚀 开始部署博客系统..."

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ Docker未安装，请先安装Docker"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 复制环境变量文件
if [ ! -f .env ]; then
    echo "📋 创建环境变量文件..."
    cp .env.example .env
    echo "⚠️  请编辑 .env 文件配置您的环境变量"
    exit 1
fi

# 构建和启动服务
echo "🐳 启动Docker容器..."
docker-compose up -d --build

echo "✅ 部署完成！"
echo "📊 服务状态："
docker-compose ps

echo "🌐 前端访问：http://localhost"
echo "🔧 后端API：http://localhost:8080"
echo "🗄️  数据库管理：localhost:3306 (用户: blog_user)"

# 显示日志
echo "📋 查看日志：docker-compose logs -f"