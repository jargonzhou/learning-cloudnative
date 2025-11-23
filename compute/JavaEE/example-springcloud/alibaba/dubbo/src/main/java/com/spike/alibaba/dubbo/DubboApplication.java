package com.spike.alibaba.dubbo;

import com.spike.alibaba.dubbo.service.DemoService;
import com.spike.alibaba.dubbo.service.impl.DemoServiceImpl;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.bootstrap.builders.ServiceBuilder;

// example: https://dubbo.apache.org/zh-cn/overview/mannual/java-sdk/tasks/develop/api/
public class DubboApplication {
    public static void main(String[] args) {
        // https://dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/config/api/api/
        DubboBootstrap.getInstance()
                .application("dubbo-demo-app")
                // 使用 Triple 作为通信 RPC 协议与并监听端口 50051
                .protocol(new ProtocolConfig(CommonConstants.TRIPLE, 50051))
                // 注册 Dubbo 服务到 DemoService server
                .service(ServiceBuilder.newBuilder()
                        .interfaceClass(DemoService.class)
                        .ref(new DemoServiceImpl())
                        .build())
                .start()
                .await();
    }
}
