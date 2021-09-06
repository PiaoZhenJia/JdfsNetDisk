package com.company.jdfs.controller;

import com.company.app.common.base.R;
import com.company.app.common.security.Authority;
import com.company.app.common.utils.FileUtil;
import com.company.app.common.utils.LogUtil;
import com.company.app.common.utils.Md5Util;
import com.company.app.common.utils.UUIDUtil;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.database.DBUtil;
import com.company.jdfs.entity.FileAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Api(tags = "文件控制器")
@RequestMapping("/api/file")
@RestController
public class FileController {

    @Autowired
    private LogUtil logUtil;
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    private UUIDUtil uuidUtil;
    @Autowired
    private Md5Util md5Util;

    @ApiOperation("查看文件夹")
    @PostMapping("/select/{baseFolder}")
    public R<FileAttribute> selectFolder(HttpServletRequest request, @PathVariable String baseFolder, @RequestBody String uri) {
        if (checkIfDirectoryTraversal(uri)) {
            return new R(403, "检测到路径穿越 日志已被记录 请勿玩火");
        }
        if (checkIfDirNeedLogin(request, baseFolder)) {
            return new R<>(401, "请先登录");
        }
        ArrayList<FileAttribute> result = new ArrayList<>();
        File folder = new File(switchBaseFolder(baseFolder) + uri);
        File[] files = folder.listFiles();
        if (null != files && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.add(new FileAttribute(true, file.getName(), file.list().length));
                } else if (file.isFile()) {
                    result.add(new FileAttribute(false, file.getName(), file.length()));
                }
            }
        }
        return new R().setDataValue(result);
    }

    @ApiOperation("下载文件")
    @GetMapping("/download/{baseFolder}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String baseFolder, String uri) throws IOException {
        if (checkIfDirectoryTraversal(uri)) {
            return;
        }
        if (checkIfDirNeedLogin(request, baseFolder)) {
            return;
        }
        File file = new File(switchBaseFolder(baseFolder) + uri);
        sendFile(response, file);
    }

    @Authority("user")
    @ApiOperation("新建文件(夹)")
    @GetMapping("/create")
    public R create(Boolean trueFolderFalseFile, String baseFolder, String uri, String name) {
        if (checkIfDirectoryTraversal(uri + name)) {
            return new R(403, "检测到路径穿越 日志已被记录 请勿玩火");
        }
        try {
            File file = new File(switchBaseFolder(baseFolder) + uri + File.separator + name);
            if (file.exists()) {
                return new R(500, "文件(夹)名称已经存在");
            }
            if (trueFolderFalseFile) {
                file.mkdir();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            return new R(500, "文件名或目录名语法不正确 必须输入合法的路径名称");
        }
        return new R("新建成功");
    }

    @Authority("user")
    @ApiOperation("删除文件(夹)")
    @GetMapping("/delete")
    public R delete(String baseFolder, String uri) {
        if (checkIfDirectoryTraversal(uri)) {
            return new R(403, "检测到路径穿越 日志已被记录 请勿玩火");
        }
        File file = new File(switchBaseFolder(baseFolder) + uri);
        file.delete();
        return new R("文件(夹)删除成功");
    }

    @Authority("user")
    @ApiOperation("修改文件(夹)名称")
    @GetMapping("/rename")
    public R rename(String baseFolder, String uri, String oldName, String newName) {
        if (checkIfDirectoryTraversal(uri + oldName) || checkIfDirectoryTraversal(newName)) {
            return new R(403, "检测到路径穿越 日志已被记录 请勿玩火");
        }
        File file = new File(switchBaseFolder(baseFolder) + uri + oldName);
        file.renameTo(new File(switchBaseFolder(baseFolder) + uri + newName));
        System.out.println(new File(switchBaseFolder(baseFolder) + uri + oldName).getPath());
        System.out.println(new File(switchBaseFolder(baseFolder) + uri + newName).getPath());
        return new R("修改成功");
    }

    @ApiOperation("分享文件")
    @GetMapping("/share")
    public R share(HttpServletRequest request, String baseFolder, String uri) {
        if (checkIfDirectoryTraversal(uri)) {
            return new R(403, "检测到路径穿越 日志已被记录 请勿玩火");
        }
        if (checkIfDirNeedLogin(request, baseFolder)) {
            return new R<>(401, "请先登录");
        }
        String shareUri = switchBaseFolder(baseFolder) + uri;
        String shareId = dbUtil.isSharedUri(shareUri);
        if (switchBaseFolder(baseFolder).equals(JdfsConstant.COMMON_FILE_DIR)) {
            //公共分享没有密码
            if (null == shareId) {
                //未被分享过 直接生成uuid作为key值
                shareId = uuidUtil.makeAppointBitUUID(8);
                dbUtil.insertShare(shareId, shareUri);
            }
        } else {
            //私有分享需要密码
            if (null == shareId) {
                //未被分享过 先生成分享key
                shareId = uuidUtil.makeAppointBitUUID(8);
                dbUtil.insertShare(shareId, shareUri);
            }
            //总是根据分享key生成4位md5作为密码
            shareId += md5Util.createMd5(shareId, JdfsConstant.MD5_SALT).substring(0, 4);
        }
        return new R(shareId);
    }

    @ApiOperation("分享提取-获取下载地址")
    @GetMapping("/shareCheck")
    public R shareCheck(String shareKey, String sharePwd) throws IOException {
        String key = shareKey.trim().toLowerCase();
        String sharePath = dbUtil.getShareFilePathByKey(key);
        if (null == sharePath) {
            return new R(403, "提取无效 请核查输入信息");
        }
        if (sharePath.startsWith(JdfsConstant.PRIVATE_FILE_DIR)) {
            //私有路径验证密码
            if (!md5Util.createMd5(key, JdfsConstant.MD5_SALT).substring(0, 4).equals(sharePwd.trim().toLowerCase())) {
                //密码错误
                return new R(403, "提取无效 请核查输入信息");
            }
        }
        String url = uuidUtil.make32BitUUID();
        dbUtil.addTemp(url, sharePath);
        return new R(new File(sharePath).getName()).setDataValue(url);
    }

    @ApiOperation("分享文件-根据临时地址下载")
    @GetMapping("/shareDownload")
    public void shareDownload(HttpServletResponse response, String param) throws IOException {
        String path = dbUtil.getTempValueByKey(param);
        if (null == path) {
            response.sendError(404);
            return;
        }
        sendFile(response, new File(path));
    }

    @Authority("user")
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(@RequestParam String baseFolder, @RequestParam String uri, @RequestPart MultipartFile file)//此处不能使用@RequestBody注解
            throws IOException {
        File needSave = null;
        try {
            if (checkIfDirectoryTraversal(uri)) {
                return new R(403, "检测到路径穿越 日志已被记录 请勿玩火");
            }
            needSave = new File(switchBaseFolder(baseFolder) + uri + File.separator + file.getOriginalFilename());
            if (needSave.exists()) {
                return new R(403, "同名文件(夹)已经存在,请更名后重新提交");
            }
            //transferTo方法在此有一个bug 会将相对路径生成的file指向容器在操作系统的临时文件目录 故先根据相对路径生成file 然后获取该file的绝对路径 然后再使用transferTo方法即可绕过
            file.transferTo(new File(needSave.getAbsolutePath()));
            return new R("上传成功");
        } catch (RuntimeException e) {
            if (null != needSave) {
                needSave.deleteOnExit();
            }
            return new R("上传失败");
        }
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

    /**
     * 校验前端输入的路径 谨防目录穿越漏洞的利用
     */
    private boolean checkIfDirectoryTraversal(String uri) {
        int t1 = uri.indexOf("../");//通过正斜杠穿越
        int t2 = uri.indexOf("..\\");//通过反斜杠穿越
        if (t1 > -1 || t2 > -1) {
            logUtil.warn(this, "尝试对路径穿越:" + uri);
            return true;
        }
        return false;
    }

    /**
     * 发送文件方法抽取
     */
    private void sendFile(HttpServletResponse response, File file) throws IOException {
        response.reset();
        //设置相应类型application/octet-stream
        response.setContentType("applicatoin/octet-stream");
        //设置头信息
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        //声明文件大小
        response.setHeader("Content-Length", ""+file.length());
        //文件下载缓存为4k 避免文件过大时造成堆内存溢出
        try (InputStream bis = new BufferedInputStream(new FileInputStream(file));
             OutputStream ops = new BufferedOutputStream(response.getOutputStream())) {
            byte[] body = new byte[1024 * 4];//缓存为4kb
            int i;
            while ((i = bis.read(body)) != -1) {
                ops.write(body, 0, i);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
