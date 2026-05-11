#!/bin/bash
#===============================================================================
# SnowModel 插件项目初始化脚本
# 用法: ./init-project.sh <项目名> <包名> <主类名> [作者] [命令前缀] [权限前缀]
# 示例: ./init-project.sh SnowChat snowymc.top.snowchat SnowChat SnowyMC snowchat snow.chat
# 
# Windows 用户可使用 Git Bash 或 WSL 运行此脚本
#===============================================================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查参数
if [ $# -lt 3 ]; then
    echo -e "${RED}用法: $0 <项目名> <包名> <主类名> [作者] [命令前缀] [权限前缀]${NC}"
    echo -e "${YELLOW}示例: $0 SnowChat snowymc.top.snowchat SnowChat SnowyMC snowchat snow.chat${NC}"
    exit 1
fi

PROJECT_NAME="$1"
PACKAGE_NAME="$2"
MAIN_CLASS="$3"
AUTHOR="${4:-Unknown}"
CMD_PREFIX="${5:-${PROJECT_NAME,,}}"
PERM_PREFIX="${6:-${PROJECT_NAME,,}.${PROJECT_NAME,,}}"

# 转换包名为路径
PACKAGE_PATH="${PACKAGE_NAME//.//}"
OLD_PACKAGE_PATH="snowymc/top/snowmodel"

echo -e "${GREEN}🚀 初始化项目: $PROJECT_NAME${NC}"
echo "=================================="

# 1. 修改 settings.gradle
echo -e "${GREEN}✅ 修改 settings.gradle${NC}"
echo "rootProject.name = '$PROJECT_NAME'" > settings.gradle

# 2. 修改 gradle.properties
echo -e "${GREEN}✅ 修改 gradle.properties${NC}"
sed -i "s|pluginGroup=.*|pluginGroup=$PACKAGE_NAME|" gradle.properties
sed -i "s|pluginVersion=.*|pluginVersion=1.0.0|" gradle.properties
sed -i "s|author=.*|author=$AUTHOR|" gradle.properties
sed -i "s|commandPrefix=.*|commandPrefix=$CMD_PREFIX|" gradle.properties
sed -i "s|permissionPrefix=.*|permissionPrefix=$PERM_PREFIX|" gradle.properties
sed -i "s|mainClass=.*|mainClass=$PACKAGE_NAME.$MAIN_CLASS|" gradle.properties

# 3. 修改 project.yml
echo -e "${GREEN}✅ 修改 project.yml${NC}"
cat > src/main/resources/project.yml << EOF
name: $PROJECT_NAME
version: 1.0.0
author: $AUTHOR
description: $PROJECT_NAME 插件
command-prefix: $CMD_PREFIX
permission-prefix: $PERM_PREFIX
EOF

# 4. 创建新包目录
echo -e "${GREEN}✅ 创建包目录: $PACKAGE_PATH${NC}"
mkdir -p "src/main/java/$PACKAGE_PATH"

# 5. 移动并修改主类
echo -e "${GREEN}✅ 修改主类: $MAIN_CLASS.java${NC}"
if [ -f "src/main/java/$OLD_PACKAGE_PATH/SnowModel.java" ]; then
    sed -e "s|package snowymc.top.snowmodel;|package $PACKAGE_NAME;|g" \
        -e "s|SnowModel|$MAIN_CLASS|g" \
        -e "s|snowmodel|${PROJECT_NAME,,}|g" \
        -e "s|snowymc.top.snowmodel|$PACKAGE_NAME|g" \
        "src/main/java/$OLD_PACKAGE_PATH/SnowModel.java" > "src/main/java/$PACKAGE_PATH/$MAIN_CLASS.java"
    rm "src/main/java/$OLD_PACKAGE_PATH/SnowModel.java"
fi

# 6. 修改其他 Java 文件的包引用
echo -e "${GREEN}✅ 修改其他源码文件${NC}"
for file in $(find src/main/java -name "*.java" 2>/dev/null); do
    if [ -f "$file" ]; then
        # 检查是否需要修改
        if grep -q "snowymc.top.snowmodel\|SnowModel" "$file" 2>/dev/null; then
            sed -i "s|snowymc.top.snowmodel|$PACKAGE_NAME|g" "$file"
            sed -i "s|SnowModel|$MAIN_CLASS|g" "$file"
            echo "  ✅ 更新: $file"
        fi
    fi
done

# 7. 移动其他源码文件到新包目录
echo -e "${GREEN}✅ 移动源码文件到新包目录${NC}"
if [ -d "src/main/java/$OLD_PACKAGE_PATH" ]; then
    for file in src/main/java/$OLD_PACKAGE_PATH/*.java; do
        if [ -f "$file" ]; then
            filename=$(basename "$file")
            cp "$file" "src/main/java/$PACKAGE_PATH/$filename"
        fi
    done
    rm -rf "src/main/java/$OLD_PACKAGE_PATH"
fi

echo ""
echo -e "${GREEN}🎉 初始化完成！${NC}"
echo "=================================="
echo -e "${YELLOW}项目名:   $PROJECT_NAME${NC}"
echo -e "${YELLOW}包名:     $PACKAGE_NAME${NC}"
echo -e "${YELLOW}主类:     $MAIN_CLASS${NC}"
echo -e "${YELLOW}作者:     $AUTHOR${NC}"
echo -e "${YELLOW}命令前缀: $CMD_PREFIX${NC}"
echo -e "${YELLOW}权限前缀: $PERM_PREFIX${NC}"
echo "=================================="
echo -e "${GREEN}下一步: gradle build${NC}"
