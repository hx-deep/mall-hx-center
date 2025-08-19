# 使用官方 Java 基础镜像（推荐选择与开发环境一致的 JDK 版本，由于服务器内网环境无法拉取线上基础镜像故自行构建Java8镜像）
FROM openjdk:8-alpine

# 设置容器内工作目录（可选）
WORKDIR /opt/

# 复制 JAR 文件到容器中（注意名称与实际打包的 JAR 文件一致）（这里提前将app.jar通过oss复制进来放在了Dockerfile文件同级目录中，故无需指定target路径）
COPY app.jar app.jar

# 暴露应用端口（与 application.yml/properties 中 server.port 一致）
EXPOSE 8080

# 设置 JVM 启动参数（可选，如内存限制、时区）
ENV JAVA_OPTS="-Xmx512m -Duser.timezone=Asia/Shanghai"

# 启动命令（使用 exec 形式避免信号丢失）
ENTRYPOINT exec java ${JAVA_OPTS} -jar app.jar
