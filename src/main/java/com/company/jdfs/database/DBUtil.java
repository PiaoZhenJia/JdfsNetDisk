package com.company.jdfs.database;

import com.company.app.common.utils.Md5Util;
import com.company.app.common.utils.SerializableUtil;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.entity.Identity;
import com.company.jdfs.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class DBUtil {

    @Autowired
    private SerializableUtil serializableUtil;
    @Autowired
    private Md5Util md5Util;

    /**
     * 通过单例方法获取数据库实例
     */
    private InnerDB getInstance() {
        return InnerDB.getInstance();
    }

    /**
     * 序列化数据库到磁盘
     */
    public void serializeDB() {
        try {
            serializableUtil.objectToFile(InnerDB.getInstance(), JdfsConstant.JDFS_DB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘序列化文件加载数据库
     */
    public void deserializeDB() throws IOException, ClassNotFoundException, RuntimeException {
        InnerDB.setDb((InnerDB) serializableUtil.fileToObject(JdfsConstant.JDFS_DB));
        InnerDB.getInstance().setTemp(new HashMap<>());
    }

    /**
     * 核对用户名密码
     */
    public boolean checkUser(String userName, String password) {
        HashSet<User> users = InnerDB.getInstance().getUsers();
        User need = new User(userName, md5Util.createMd5(password, JdfsConstant.MD5_SALT), null);
        return users.contains(need);
    }

    /**
     * 检测用户名是否存在
     *
     * @return 有重名返回false 无重名返回true
     */
    public boolean checkNameIsNotExist(String userName) {
        HashSet<User> users = InnerDB.getInstance().getUsers();
        Iterator<User> iterator = users.iterator();
        //查找重名
        while (iterator.hasNext()) {
            if (iterator.next().getUserName().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 修改用户密码
     */
    public void changeUserPassWord(String userName, String passWord) {
        HashSet<User> users = InnerDB.getInstance().getUsers();
        users.forEach(e -> {
            if (e.getUserName().equals(userName)) {
                e.setPassword(md5Util.createMd5(passWord, JdfsConstant.MD5_SALT));
            }
        });
        serializeDB();
    }

    /**
     * 修改用户名
     */
    public boolean changeUserName(String oldName, String userName) {
        if (checkNameIsNotExist(userName)) {
            HashSet<User> users = InnerDB.getInstance().getUsers();
            users.forEach(e -> {
                if (e.getUserName().equals(oldName)) {
                    e.setUserName(userName);
                }
            });
            serializeDB();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否文件已经被分享
     *
     * @param shareUri 文件路径
     * @return 如果已经被分享 返回已有分享码 否则返回null
     */
    public String isSharedUri(String shareUri) {
        HashMap<String, String> share = InnerDB.getInstance().getShare();
        String[] result = new String[1];
        if (share.containsValue(shareUri)) {
            share.forEach((k, v) -> {
                if (v.equals(shareUri)) {
                    result[0] = k;
                }
            });
        }
        return result[0];
    }

    /**
     * 将分享码保存至数据库
     */
    public void insertShare(String shareId, String shareUri) {
        InnerDB.getInstance().getShare().put(shareId, shareUri);
        serializeDB();
    }

    /**
     * 根据分享码获取文件路径
     */
    public String getShareFilePathByKey(String shareKey) {
        return InnerDB.getInstance().getShare().get(shareKey);
    }

    /**
     * 数据库重置
     */
    public void setDBToDefaultState() {
        InnerDB db = getInstance();
        db.setUsers(new HashSet<>());
        db.setShare(new HashMap<>());
        db.setTemp(new HashMap<>());
        db.getUsers().add(new User("admin", md5Util.createMd5("admin", JdfsConstant.MD5_SALT), Identity.ADMIN));
        serializeDB();
    }

    /**
     * 添加临时信息到数据库
     */
    public void addTemp(String key, String value) {
        InnerDB.getInstance().getTemp().put(key, value);
    }

    /**
     * 从临时库中获取信息
     * @param param
     * @return
     */
    public String getTempValueByKey(String param) {
        return InnerDB.getInstance().getTemp().get(param);
    }
}
