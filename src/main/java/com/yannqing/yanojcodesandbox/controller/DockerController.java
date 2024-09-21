package com.yannqing.yanojcodesandbox.controller;

import com.yannqing.yanojcodesandbox.JavaDockerCodeSandbox;
import com.yannqing.yanojcodesandbox.JavaNativeCodeSandbox;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeResponse;
import com.yannqing.yanojcodesandbox.service.DockerService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 容器视图
 * @author: yannqing
 * @create: 2024-08-22 09:25
 * @from: <更多资料：yannqing.com>
 **/
@RestController
public class DockerController {

    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    private static final Logger log = LoggerFactory.getLogger(DockerController.class);
    @Resource
    private DockerService dockerService;

    @Resource
    private JavaDockerCodeSandbox javaDockerCodeSandbox;

    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @PostMapping("/create")
    public String createContainer(String name) {

        String containerId = dockerService.createContainer(name, "/tmpCode");


        return containerId;
    }

    @GetMapping("/test")
    public String test() {

        return "ok";
    }

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug("ExecuteCodeRequest:{}", executeCodeRequest);
        // 基本的认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }

}
