package com.yannqing.yanojcodesandbox.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeResponse;
import com.yannqing.yanojcodesandbox.model.ExecuteMessage;
import com.yannqing.yanojcodesandbox.service.DockerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Value("${image}")
    private String image;

    public DockerServiceImpl() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();
        this.dockerClient =  DockerClientImpl.getInstance(config, httpClient);
    }

    @Override
    public String createContainer(String name, String userCodeParentPath) {

        // 创建容器配置
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1000 * 1000L);      // 指定内存限制为 100M
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);                    // 指定分配的 CPU 为 1
        // 设置挂载
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/yannqing/yanojsandbox")));

        // 构建创建容器命令
        CreateContainerCmd createContainerCmd = dockerClient
                .createContainerCmd(image)
                .withHostConfig(hostConfig)
                .withName(name)
                .withNetworkDisabled(true)   // 禁止网络使用
                .withReadonlyRootfs(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true);             // 创建可交互的窗口

        // 执行创建容器命令
        String containerId = createContainerCmd.exec().getId();
        dockerClient.startContainerCmd(containerId).exec();
        log.info("create container success：{}", containerId);
        return containerId;
    }

    /**
     * 执行命令并获取结果
     * @param containerId
     * @param inputList
     */
    @Override
    public List<ExecuteMessage> execContainer(String containerId, List<String> inputList) {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令，" + execCreateCmdResponse);

            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};

            String execId =execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback =new ExecStartResultCallback() {
                @Override
                public void onNext(
                        Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };
            try {
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
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
