package com.company.jdfs.afterstart;

import com.company.app.common.utils.FileUtil;
import com.company.app.common.utils.LogUtil;
import com.company.app.common.utils.Md5Util;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.database.DBUtil;
import com.company.jdfs.database.InnerDB;
import com.company.jdfs.entity.Identity;
import com.company.jdfs.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class JdfsApplicationStart implements ApplicationRunner {

    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    private LogUtil logUtil;
    @Autowired
    private Md5Util md5Util;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        //检查必要的工作目录
        fileUtil.createDirIfNotExist(JdfsConstant.JDFS_WORK_SPACE);
        fileUtil.createDirIfNotExist(JdfsConstant.COMMON_FILE_DIR);
        fileUtil.createDirIfNotExist(JdfsConstant.PRIVATE_FILE_DIR);
        //说明文件每次覆盖
        fileUtil.createFileIfNotExist(JdfsConstant.READ_ME);
        coverReadMe();
        //检查数据文件是否存在
        boolean dbExist = fileUtil.createFileIfNotExist(JdfsConstant.JDFS_DB);
        dbInit(dbExist);
    }

    private void coverReadMe() throws IOException {
        InputStream is = fileUtil.getInputStreamFromFileInResources("readme.txt");
        byte[] temp = new byte[is.available()];
        is.read(temp);
        fileUtil.coverFileAsByte(temp, new File(JdfsConstant.READ_ME));
    }

    private void dbInit(boolean exist) {
        if (exist) {
            try {
                dbUtil.deserializeDB();
                logUtil.info(this, "数据文件反序列化成功");
                return;
            } catch (RuntimeException | IOException | ClassNotFoundException e) {
                logUtil.info(this, "反序列化数据失败 尝试重写数据文件");
            }
        } else {
            logUtil.info(this, "数据文件不存在");
        }
        InnerDB db = dbUtil.getInstance();
        db.setUsers(new HashSet<>());
        db.setShare(new HashMap<>());
        db.getUsers().add(new User("admin", md5Util.createMd5("admin", JdfsConstant.MD5_SALT), Identity.ADMIN));
        dbUtil.serializeDB();
        logUtil.info(this, "数据文件已配置为默认");
    }

}
