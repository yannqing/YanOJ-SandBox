package com.yannqing.yanojcodesandbox.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import com.yannqing.yanojcodesandbox.JavaDockerCodeSandbox;
import com.yannqing.yanojcodesandbox.JavaDockerCodeSandboxOld;
import com.yannqing.yanojcodesandbox.JavaNativeCodeSandbox;
import com.yannqing.yanojcodesandbox.JavaNativeCodeSandboxOld;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanojcodesandbox.model.ExecuteCodeResponse;
import com.yannqing.yanojcodesandbox.service.DockerService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @description: 容器视图
 * @author: yannqing
 * @create: 2024-08-22 09:25
 * @from: <更多资料：yannqing.com>
 **/
@RestController
@RequestMapping("/")
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

    @PostMapping("/test")
    public String testContainer(String name) {
        JavaDockerCodeSandboxOld javaDockerCodeSandboxOld = new JavaDockerCodeSandboxOld();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        log.info("code: {}", code);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");


        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandboxOld.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
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
