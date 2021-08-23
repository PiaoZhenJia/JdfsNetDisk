package com.company.jdfs.controller;

import com.company.app.common.base.R;
import com.company.app.common.utils.FileUtil;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.entity.FileAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


@Api(tags = "文件控制器")
@RestController("/file")
public class FileController {

    @Autowired
    private FileUtil fileUtil;

    @ApiOperation("查看文件夹")
    @PostMapping("/select/{baseFolder}")
    public R<FileAttribute> selectFolder(@PathVariable String baseFolder, @RequestBody String uri) {
        ArrayList<FileAttribute> result = new ArrayList<>();
        File folder = new File(switchBaseFolder(baseFolder) + uri);
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                result.add(new FileAttribute(true, files[i].getName(), files[i].list().length));
            } else if (files[i].isFile()) {
                result.add(new FileAttribute(false, files[i].getName(), files[i].length()));
            }
        }
        return new R().setDataValue(result);
    }

    @ApiOperation("下载文件")
    @GetMapping("/download/{baseFolder}")
    public void download(HttpServletResponse response, @PathVariable String baseFolder, String uri) throws IOException {
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

    @ApiOperation("新建文件(夹)")
    @GetMapping("/create")
    public R create(Boolean trueFolderFalseFile, String baseFolder, String uri, String name) throws IOException {
        File file = new File(switchBaseFolder(baseFolder) + uri + File.separator + name);
        if (file.exists()){
            return new R(500,"文件(夹)名称已经存在");
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
     * @return
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
}
