package com.yannqing.yanojcodesandbox.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.yannqing.yanojcodesandbox.service.DockerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: yannqing
 * @create: 2024-08-21 19:42
 * @from: <更多资料：yannqing.com>
 **/
@Service
@Slf4j
public class DockerServiceImpl implements DockerService {

    private final DockerClient dockerClient;


    public DockerServiceImpl() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();
        this.dockerClient =  DockerClientImpl.getInstance(config, httpClient);
    }

    @Override
    public String createContainer(String image, String name) {

        // 创建容器配置
//        ContainerConfig containerConfig = new ContainerConfig();
//        containerConfig.withAttachStdin(true); // 模拟 -it 的 -i 选项
//        containerConfig.withImage(image);  // 使用的镜像
//        containerConfig.withTty(true); // 模拟 -it 的 -t 选项
//                .withCmd("sleep", "infinity") // 命令保持容器运行

        // 构建创建容器命令
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image).withName(name).withAttachStdin(true).withTty(true);

        // 执行创建容器命令
        String containerId = createContainerCmd.exec().getId();
        dockerClient.startContainerCmd(containerId).exec();
        log.info("create container success：{}", containerId);
        return containerId;
    }

    @Override
    public void pullImage() {
        log.info("测试成功！{}", dockerClient);
    }

    @Override
    public void deleteContainer() {

    }

    @Override
    public void runContainer() {

    }
}
