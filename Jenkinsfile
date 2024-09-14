pipeline {
    agent any
    environment {
        GIT_URL = 'git@github.com:yannqing/YanOJ-SandBox.git'
        APP_NAME = 'sandbox'
        APP_PROFILE = 'prod'
        APP_IMAGE = 'yanojsandbox:latest'
        APP_PORT = '8091:8080'
    }

    stages {
        stage('拉取代码') {
            steps {
                git branch: 'master', url: GIT_URL
            }
        }
        stage('编译构建') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('移除镜像和容器') {
            steps {
                script {
                    // 检查容器是否存在
                    def containerExists = sh(script: 'docker ps -a --format "{{.Names}}" | grep -q "^${APP_NAME}"', returnStatus: true) == 0
                    // 检查镜像是否存在
                    def imageExists = sh(script: 'docker images --format "{{.Repository}}:{{.Tag}}" | grep -q "^${APP_IMAGE}$"', returnStatus: true) == 0

                    // 如果容器存在，则停止和移除
                    if (containerExists) {
                        echo "容器存在"
                        def isRunning = sh(script: 'docker ps --format "{{.Names}}" | grep -q "^${APP_NAME}"', returnStatus: true) == 0
                        if (isRunning) {
                            sh "docker stop ${APP_NAME}"
                        }
                        echo "删除容器"
                        sh "docker rm ${APP_NAME}"
                    } else {
                        echo "容器不存在"
                    }

                    // 如果镜像存在，则移除
                    if (imageExists) {
                        echo "删除镜像"
                        sh "docker rmi ${APP_IMAGE}"
                    } else {
                        echo "镜像不存在"
                    }
                }
            }
        }
        stage('构建镜像，创建并运行容器') {
            steps {
                // 基于 Dockerfile 进行构建
                sh "docker build -f Dockerfile.dockerfile -t ${APP_IMAGE} ."
                sh "docker run -it --name ${APP_NAME} --network mynetwork -v /yannqing/${APP_NAME}:/yannqing/${APP_NAME} -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker -p ${APP_PORT} -d ${APP_IMAGE}"
            }
        }
    }
}