package com.company.jdfs.controller;

import com.company.app.common.base.R;
import com.company.app.common.security.Authority;
import com.company.app.common.utils.FileUtil;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.entity.FileAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Api(tags = "文件控制器")
@RestController("/file")
public class FileController {

    @Autowired
    private FileUtil fileUtil;

    @ApiOperation("查看文件夹")
    @PostMapping("/select/{baseFolder}")
    public R<FileAttribute> selectFolder(HttpServletRequest request, @PathVariable String baseFolder, @RequestBody String uri) {
        if (checkIfDirNeedLogin(request, baseFolder)) {
            return new R<>(401, "请先登录");
        }
        ArrayList<FileAttribute> result = new ArrayList<>();
        File folder = new File(switchBaseFolder(baseFolder) + uri);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                result.add(new FileAttribute(true, file.getName(), file.list().length));
            } else if (file.isFile()) {
                result.add(new FileAttribute(false, file.getName(), file.length()));
            }
        }
        return new R().setDataValue(result);
    }

    @ApiOperation("下载文件")
    @GetMapping("/download/{baseFolder}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String baseFolder, String uri) throws IOException {
        if (checkIfDirNeedLogin(request, baseFolder)) {
            return;
        }
        File file = new File(switchBaseFolder(baseFolder) + uri);
        byte[] bytes = fileUtil.getByteFromFile(file);
        response.reset();
        //设置相应类型application/octet-stream
        response.setContentType("applicatoin/octet-stream");
        //设置头信息
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        // 写入到流
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

    @Authority("user")
    @ApiOperation("新建文件(夹)")
    @GetMapping("/create")
    public R create(Boolean trueFolderFalseFile, String baseFolder, String uri, String name) throws IOException {
        File file = new File(switchBaseFolder(baseFolder) + uri + File.separator + name);
        if (file.exists()) {
            return new R(500, "文件(夹)名称已经存在");
        }
        if (trueFolderFalseFile) {
            file.mkdir();
        } else {
            file.createNewFile();
        }
        return new R("新建成功");
    }

    /**
     * 将前端baseFolder转换为后端路径
     *
     * @param baseFolder 前端对公、私有的定义名称
     */
    private String switchBaseFolder(String baseFolder) {
        switch (baseFolder) {
            case "common":
                return JdfsConstant.COMMON_FILE_DIR;
            case "private":
                return JdfsConstant.PRIVATE_FILE_DIR;
            default:
                throw new IllegalStateException("Unexpected value: " + baseFolder);
        }
    }

    /**
     * 检查要访问的路径是否需要登录权限
     */
    private boolean checkIfDirNeedLogin(HttpServletRequest request, String baseFolder) {
        if (switchBaseFolder(baseFolder).equals(JdfsConstant.COMMON_FILE_DIR)) {
            return false;
        }
        return !StringUtils.isNotEmpty((String) request.getSession().getAttribute(JdfsConstant.SESSION_LOGIN_FLAG));
    }

}
