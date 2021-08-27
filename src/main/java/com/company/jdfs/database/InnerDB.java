package com.company.jdfs.database;

import com.company.jdfs.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Data
public class InnerDB implements Serializable {

    private static InnerDB db;

    private HashSet<User> users;

    private HashMap<String,String> share;

    /**
     * 临时使用的数据 声明transient不参与序列化
     */
    private transient HashMap<String,String> temp;

    /**
     * 单例模式-私有化构造器
     */
    private InnerDB() {
    }

    /**
     * 单例模式-获取数据库对象
     * protected保证只有DBUtil能访问
     */
    protected static InnerDB getInstance() {
        if (null == db) {
            db = new InnerDB();
        }
        return db;
    }

    /**
     * 设置数据库对象 用于反序列化
     */
    protected static void setDb(InnerDB dbInstance) {
        db = dbInstance;
    }

}
