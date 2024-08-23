FROM konajdk/konajdk:17-tlinux-17.0.12

ENV TZ=Asia/Shanghai
RUN echo -e "${TZ}" > /etc/timezone && ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime
RUN mkdir -p /data/logs/gc

# 默认编码
ENV LANG zh_CN.UTF-8

# 设置工作目录
ENV workdir /app/
WORKDIR ${workdir}

# 应用自定义参数，如--spring.application.name=XX
ENV APP_PARAMS ''

ENV JAR_FILE_DIR ./target
ENV JAR_FILE_NAME lemon3-1.0.0-SNAPSHOT.jar
ENV PROFILES_ACTIVE dev
ENV JAVA_OPTS "-XX:+UseContainerSupport -XX:MaxRAMPercentage=70.0 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/oom.hprof"
COPY ${JAR_FILE_DIR}/${JAR_FILE_NAME} .

CMD ["sh", "-ec", "exec java -Xloggc:/data/logs/gc/gclog.log -XX:+PrintGCDetails -verbose:gc -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/oom.hprof ${JAVA_OPTS}  -jar ${JAR_FILE_NAME} --spring.profiles.active=${PROFILES_ACTIVE:-default} ${APP_PARAMS}"]