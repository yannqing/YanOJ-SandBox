package com.yannqing.yanojcodesandbox.controller;

import com.yannqing.yanojcodesandbox.service.DockerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 容器视图
 * @author: yannqing
 * @create: 2024-08-22 09:25
 * @from: <更多资料：yannqing.com>
 **/
@RestController
@RequestMapping("/container")
public class DockerController {


    @Resource
    private DockerService dockerService;


    @GetMapping("/request")
    public String request() {

        dockerService.pullImage();

        return "xxx";
    }
    @PostMapping("/create")
    public String createContainer(String name) {

        String containerId = dockerService.createContainer(name, "/tmpCode");


        return containerId;
    }

    @PostMapping("/test")
    public String testContainer(String name) {

    }

}
