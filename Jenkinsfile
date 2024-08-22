pipeline {
    agent any

    stages {
        stage('拉取代码') {
            steps {
                git branch: 'master', url: 'git@github.com:yannqing/YanOJ-SandBox.git'
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
                    def containerExists = sh(script: 'docker ps -a --format "{{.Names}}" | grep -q "^sandbox"', returnStatus: true) == 0
                    // 检查镜像是否存在
                    def imageExists = sh(script: 'docker images --format "{{.Repository}}:{{.Tag}}" | grep -q "^yanojsandbox:latest$"', returnStatus: true) == 0

                    // 如果容器存在，则停止和移除
                    if (containerExists) {
                        echo "容器存在"
                        def isRunning = sh(script: 'docker ps --format "{{.Names}}" | grep -q "^sandbox"', returnStatus: true) == 0
                        if (isRunning) {
                            sh "docker stop sandbox"
                        }
                        echo "删除容器"
                        sh "docker rm sandbox"
                    } else {
                        echo "容器不存在"
                    }

                    // 如果镜像存在，则移除
                    if (imageExists) {
                        echo "删除镜像"
                        sh "docker rmi yanojsandbox:latest"
                    } else {
                        echo "镜像不存在"
                    }
                }
            }
        }
        stage('构建镜像，创建并运行容器') {
            steps {
                // 基于 Dockerfile 进行构建
                sh "docker build -f Dockerfile.dockerfile -t yanojsandbox:latest ."
                sh "docker run -it --name sandbox -v yanojsandbox:/yannqing/yanojsandbox -p 8091:8080 -d yanojsandbox:latest"
            }
        }
    }
}