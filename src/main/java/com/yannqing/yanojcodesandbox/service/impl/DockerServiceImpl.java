package com.yannqing.yanojcodesandbox.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
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
    public void createContainer(String image, String name) {

        // 构建创建容器命令
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image).withName(name);

        // 执行创建容器命令
        String containerId = createContainerCmd.exec().getId();
        dockerClient.startContainerCmd(containerId).exec();
        log.info("create container success：{}", containerId);
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
