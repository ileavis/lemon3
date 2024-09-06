# 定义变量
MAVEN_CMD=mvn
DOCKER_CMD=docker
SETTINGS_FILE=./settings.xml
POM_FILE=./pom.xml
DOCKERFILE=./Dockerfile
IMAGE_NAME=lemon3:v1
POD_NAME=lemon3
KUBE_CMD=kubectl
SERVICE_NAME=lemon3-service

# 默认目标
all: package docker-build clean

# 目标：使用指定的 settings 文件运行 Maven 安装
install:
	$(MAVEN_CMD) clean install -s $(SETTINGS_FILE) -f $(POM_FILE)

# 目标：使用指定的 settings 文件运行 Maven 打包
package:
	$(MAVEN_CMD) clean package -s $(SETTINGS_FILE) -f $(POM_FILE)

# 目标：清理 Docker 镜像
docker-clean:
	$(DOCKER_CMD) rmi $(IMAGE_NAME)

# 目标：从指定的 Dockerfile 构建 Docker 镜像
docker-build:
	$(DOCKER_CMD) build -t $(IMAGE_NAME) -f $(DOCKERFILE) .

# 清理目标：清理项目
clean:
	$(MAVEN_CMD) clean -s $(SETTINGS_FILE) -f $(POM_FILE)

# 目标：运行 Docker 镜像
docker-run:
	$(DOCKER_CMD) run -d -p 8080:8080 -p 8888:8888 $(IMAGE_NAME)

# 目标：在 Kubernetes 中运行 Pod
kube-run:
	$(KUBE_CMD) run $(POD_NAME) --image=$(IMAGE_NAME)

# 目标：在 Kubernetes 中暴露 Pod 为 NodePort 服务
kube-expose:
	$(KUBE_CMD) expose pod $(POD_NAME) --type=NodePort --port=8080 --name=$(SERVICE_NAME)
	$(KUBE_CMD) expose pod $(POD_NAME) --type=NodePort --port=8888 --name=$(SERVICE_NAME)-8888

kube-delete:
	$(KUBE_CMD) delete pod $(POD_NAME)

# 帮助目标：列出可用的目标
help:
	@echo "可用的目标:"
	@echo "  all         - 运行 install, package, 和 build-docker"
	@echo "  install     - 使用指定的 settings 文件运行 Maven 安装"
	@echo "  package     - 使用指定的 settings 文件运行 Maven 打包"
	@echo "  docker-build- 从指定的 Dockerfile 构建 Docker 镜像"
	@echo "  clean       - 清理项目"
	@echo "  docker-run  - 运行 Docker 镜像"
	@echo "  docker-clean- 清理 Docker 镜像"
	@echo "  kube-run    - 在 Kubernetes 中运行 Pod"
	@echo "  kube-expose - 在 Kubernetes 中暴露 Pod 为 NodePort 服务"
	@echo "  kube-delete - 在 Kubernetes 中删除 Pod"