package com.yannqing.yanojcodesandbox.service;

import com.yannqing.yanojcodesandbox.model.ExecuteMessage;

import java.util.List;

/**
 * @description: 操作docker的业务代码
 * @author: yannqing
 * @create: 2024-08-21 19:41
 **/
public interface DockerService {

    String createContainer(String name, String userCodeParentPath);

    List<ExecuteMessage> execContainer(String containerId, List<String> inputList);

    void pullImage();

    void deleteContainer();

    void runContainer();
}
