package com.company.jdfs.database;

import com.company.app.common.utils.Md5Util;
import com.company.app.common.utils.SerializableUtil;
import com.company.jdfs.JdfsConstant;
import com.company.jdfs.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

@Component
public class DBUtil {

    @Autowired
    private SerializableUtil serializableUtil;
    @Autowired
    private Md5Util md5Util;

    public InnerDB getInstance() {
        return InnerDB.getInstance();
    }

    public void serializationDB() throws IOException {
        serializableUtil.objectToFile(InnerDB.getInstance(), JdfsConstant.JDFS_DB);
    }

    public void deserializationDB() throws IOException, ClassNotFoundException {
        InnerDB.setDb((InnerDB) serializableUtil.fileToObject(JdfsConstant.JDFS_DB));
    }

    public boolean checkUser(String userName,String password){
        HashSet<User> users = InnerDB.getInstance().getUsers();
        User need = new User(userName,md5Util.createMd5(password,JdfsConstant.MD5_SALT),null);
        return users.contains(need);
    }

    public boolean checkNameIsNotExist(String userName) {
        HashSet<User> users = InnerDB.getInstance().getUsers();
        Iterator<User> iterator = users.iterator();
        //查找重名
        while (iterator.hasNext()){
            if (iterator.next().getUserName().equals(userName)){
                return false;
            }
        }
        return true;
    }

    public void changeUserPassWord(String userName, String passWord) {
        HashSet<User> users = InnerDB.getInstance().getUsers();
        users.forEach(e->{
            if (e.getUserName().equals(userName)){
                e.setPassword(passWord);
            }
        });
    }

    public boolean changeUserName(String oldName, String userName) {
        if (checkNameIsNotExist(userName)){
            HashSet<User> users = InnerDB.getInstance().getUsers();
            users.forEach(e->{
                if (e.getUserName().equals(oldName)){
                    e.setUserName(userName);
                }
            });
            return true;
        }else {
            return false;
        }
    }
}
