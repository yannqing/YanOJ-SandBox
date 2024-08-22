package com.yannqing.yanojcodesandbox.service;

/**
 * @description: 操作docker的业务代码
 * @author: yannqing
 * @create: 2024-08-21 19:41
 **/
public interface DockerService {

    void createContainer(String image, String name);

    void pullImage();

    void deleteContainer();

    void runContainer();
}
