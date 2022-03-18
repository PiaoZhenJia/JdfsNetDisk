package com.company.app;

import com.company.app.common.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class AfterApplicationStart implements ApplicationRunner {

    @Value("${my-param.security.open-dev-controller}")
    private Boolean openDevController;
    @Value("${springfox.documentation.enabled}")
    private Boolean openSwaggerUi;

    @Autowired
    private LogUtil logUtil;

    @Override
    public void run(ApplicationArguments args) {
        logUtil.info(this, "Springfox-Swagger 接口Api配置状态\t: " + (openSwaggerUi ? "开启" : "关闭"));
        logUtil.info(this, "风险控制器 [DevController] 配置状态\t: " + (openDevController ? "开启" : "关闭"));
    }
}
