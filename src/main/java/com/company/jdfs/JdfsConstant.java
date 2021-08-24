package com.company.jdfs;

import java.io.File;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
public class JdfsConstant {
    /**
     * 项目外置文件夹路径
     */
    public static final String JDFS_WORK_SPACE = "." + File.separator + ".JdfsWorkSpace";
    /**
     * 共享路径
     */
    public static final String COMMON_FILE_FOLDER = ".common_file_dir";
    public static final String COMMON_FILE_DIR = JDFS_WORK_SPACE + File.separator + COMMON_FILE_FOLDER;
    /**
     * 私有路径
     */
    public static final String PRIVATE_FILE_FOLDER = ".private_file_dir";
    public static final String PRIVATE_FILE_DIR = JDFS_WORK_SPACE + File.separator + PRIVATE_FILE_FOLDER;
    /**
     * 数据文件路径
     */
    public static final String JDFS_DB = JDFS_WORK_SPACE + File.separator + "jdfs.db";
    /**
     * 说明文档路径
     */
    public static final String READ_ME = JDFS_WORK_SPACE + File.separator + "readme.txt";
    /**
     * 密码加密盐值
     */
    public static final String MD5_SALT = "-SALT-PZJ-NB";
    /**
     * session登录状态key值
     */
    public static final String SESSION_LOGIN_FLAG = "SESSION_IS_LOGIN";
}
