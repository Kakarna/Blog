#!/bin/bash
# 宝塔面板自动化部署脚本

echo "🎯 开始宝塔面板部署..."

# 检查目录是否存在
if [ ! -d "/www/wwwroot" ]; then
    echo "❌ 宝塔面板目录不存在，请确保已安装宝塔面板"
    exit 1
fi

# 配置变量
BACKEND_DIR="/www/wwwroot/blog-backend"
FRONTEND_DIR="/www/wwwroot/blog-frontend"
DB_NAME="my-blog"
DB_USER="blog_user"

echo "📁 创建项目目录..."
mkdir -p $BACKEND_DIR
mkdir -p $FRONTEND_DIR

echo "🐳 检查环境依赖..."

# 检查Java
if ! command -v java &> /dev/null; then
    echo "❌ Java未安装，请通过宝塔软件商店安装"
    exit 1
fi

# 检查Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven未安装，正在安装Maven..."
    wget https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
    tar -xzf apache-maven-3.8.6-bin.tar.gz -C /opt
    ln -s /opt/apache-maven-3.8.6/bin/mvn /usr/bin/mvn
    rm apache-maven-3.8.6-bin.tar.gz
fi

# 检查Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js未安装，请通过宝塔软件商店安装"
    exit 1
fi

echo "📦 开始构建后端..."
cd my-blog-back

# 清理并打包
echo "🔨 编译Spring Boot应用..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ 后端构建失败"
    exit 1
fi

# 复制jar文件
JAR_FILE=$(find target -name "*.jar" | head -1)
if [ -f "$JAR_FILE" ]; then
    echo "📤 复制后端文件到宝塔目录..."
    cp $JAR_FILE $BACKEND_DIR/
    cp src/main/resources/application.properties.template $BACKEND_DIR/application.properties
    
    # 创建启动脚本
    cat > $BACKEND_DIR/start.sh << 'EOF'
#!/bin/bash
JAVA_OPTS="-Xms256m -Xmx512m -Dspring.profiles.active=prod"
nohup java $JAVA_OPTS -jar *.jar > app.log 2>&1 &
echo $! > app.pid
EOF
    
    chmod +x $BACKEND_DIR/start.sh
else
    echo "❌ 未找到生成的jar文件"
    exit 1
fi

echo "🎨 开始构建前端..."
cd ../my-blog-front

echo "📦 安装前端依赖..."
npm install

if [ $? -ne 0 ]; then
    echo "❌ 前端依赖安装失败"
    exit 1
fi

echo "🔨 编译Vue应用..."
npm run build

if [ $? -ne 0 ]; then
    echo "❌ 前端构建失败"
    exit 1
fi

echo "📤 复制前端文件到宝塔目录..."
rm -rf $FRONTEND_DIR/*
cp -r dist/* $FRONTEND_DIR/

# 设置文件权限
echo "🔒 设置文件权限..."
chown -R www:www $BACKEND_DIR
chown -R www:www $FRONTEND_DIR
chmod -R 755 $BACKEND_DIR
chmod -R 755 $FRONTEND_DIR

echo "✅ 部署完成！"
echo ""
echo "📋 后续步骤："
echo "1. 编辑配置文件: $BACKEND_DIR/application.properties"
echo "2. 设置数据库密码和JWT密钥"
echo "3. 在宝塔创建MySQL数据库: $DB_NAME"
echo "4. 导入数据库结构: mysql/init.sql"
echo "5. 配置Nginx反向代理"
echo "6. 启动后端服务: cd $BACKEND_DIR && ./start.sh"
echo ""
echo "🌐 访问地址："
echo "前端: http://your-domain.com"
echo "后端API: http://your-domain.com:8080"
echo ""
echo "📖 详细配置请参考 baota-deploy.md"