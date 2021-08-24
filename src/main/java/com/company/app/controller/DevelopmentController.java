package com.company.app.controller;

import com.company.app.common.base.R;
import com.company.app.common.utils.Md5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Api(tags = "功能测试控制器")
@RestController
@RequestMapping("/dev")
public class DevelopmentController {

    @Value("${my-param.security.open-dev-controller}")
    private Boolean openDevController;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Md5Util md5Util;

    @ApiOperation(value = "安全关闭JVM",
            notes = "************************ FBI WARRNING ************************\n" +
                    "如果你不知道你正在做什么，请不要调用这个接口\n" +
                    "如果你不知道你正在做什麼，請不要調用這個介面\n" +
                    "如果你唔知你喺度做咩呀，唔该唔好调用呢个接口\n" +
                    "If you don't know what you're doing, please don't call this interface.\n" +
                    "あなたが何をしているか分からないなら、このインターフェースを使わないでください。\n" +
                    "만약 네가 무엇을 하고 있는지 모르면 이 인터페이스를 호출해서는 안 된다\n" +
                    "Wenn Sie nicht wissen, was Sie tun, rufen Sie bitte nicht diese Schnittstelle an.\n" +
                    "Si vous ne savez pas ce que vous faites, n 'appelez pas cette interface.\n" +
                    "если вы не знаете, ч" +
                    "то вы делаете, пожалуйста, не называйте этот интерфейс \n" +
                    "Se non sai cosa stai facendo, per favore non chiamare questa interfaccia.")
    @GetMapping("/kill/jvm")
    public R killJVM() {
        if (!openDevController) return null;
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.exit(SpringApplication.exit(applicationContext));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return new R(666, "服务器将于3秒后尝试自我关闭");
    }

    @ApiOperation("返回HTTP错误码")
    @GetMapping("/errCode")
    public void errCode(HttpServletResponse response, Integer code) throws IOException {
        response.sendError(code);
    }

    @ApiOperation("Md5生成")
    @GetMapping("/md5")
    public R Md5(@ApiParam("密文") String base, @RequestParam(required = false) @ApiParam("盐值") String salt) {
        if (!openDevController) return null;
        return new R(md5Util.createMd5(base, salt));
    }

    @ApiOperation("获取系统详细信息")
    @GetMapping("/systemInfo")
    public R<HashMap<String, String>> systemInfo() throws UnknownHostException {
        if (!openDevController) return null;

        HashMap<String, String> result = new HashMap<>();

        Properties props = System.getProperties();

        result.put("用户名", System.getenv().get("USERNAME"));
        result.put("计算机名", System.getenv().get("COMPUTERNAME"));
        result.put("计算机域名", System.getenv().get("USERDOMAIN"));
        result.put("本地ip地址", InetAddress.getLocalHost().getHostAddress());
        result.put("本地主机名", InetAddress.getLocalHost().getHostName());
        result.put("JVM可以使用的总内存", String.valueOf(Runtime.getRuntime().totalMemory()));
        result.put("JVM可以使用的剩余内存", String.valueOf(Runtime.getRuntime().freeMemory()));
        result.put("JVM可以使用的处理器个数", String.valueOf(Runtime.getRuntime().availableProcessors()));
        result.put("Java的运行环境版本", props.getProperty("java.version"));
        result.put("Java的运行环境供应商", props.getProperty("java.vendor"));
        result.put("Java供应商的URL", props.getProperty("java.vendor.url"));
        result.put("Java的安装路径", props.getProperty("java.home"));
        result.put("Java的虚拟机规范版本", props.getProperty("java.vm.specification.version"));
        result.put("Java的虚拟机规范供应商", props.getProperty("java.vm.specification.vendor"));
        result.put("Java的虚拟机规范名称", props.getProperty("java.vm.specification.name"));
        result.put("Java的虚拟机实现版本", props.getProperty("java.vm.version"));
        result.put("Java的虚拟机实现供应商", props.getProperty("java.vm.vendor"));
        result.put("Java的虚拟机实现名称", props.getProperty("java.vm.name"));
        result.put("Java运行时环境规范版本", props.getProperty("java.specification.version"));
        result.put("Java运行时环境规范供应商", props.getProperty("java.specification.vender"));
        result.put("Java运行时环境规范名称", props.getProperty("java.specification.name"));
        result.put("Java的类格式版本号", props.getProperty("java.class.version"));
//        result.put("Java的类路径", props.getProperty("java.class.path"));
//        result.put("加载库时搜索的路径列表", props.getProperty("java.library.path"));
        result.put("默认的临时文件路径", props.getProperty("java.io.tmpdir"));
        result.put("一个或多个扩展目录的路径", props.getProperty("java.ext.dirs"));
        result.put("操作系统的名称", props.getProperty("os.name"));
        result.put("操作系统的构架", props.getProperty("os.arch"));
        result.put("操作系统的版本", props.getProperty("os.version"));
        result.put("文件分隔符", props.getProperty("file.separator"));
        result.put("路径分隔符", props.getProperty("path.separator"));
        result.put("行分隔符", props.getProperty("line.separator"));
        result.put("用户的账户名称", props.getProperty("user.name"));
        result.put("用户的主目录", props.getProperty("user.home"));
        result.put("用户的当前工作目录", props.getProperty("user.dir"));

        return new R().setDataValue(result);
    }
}
