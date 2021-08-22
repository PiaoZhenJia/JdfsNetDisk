package com.company.jdfs.database;

import com.company.jdfs.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;

@Data
public class InnerDB implements Serializable {

    private static InnerDB db;

    private HashSet<User> users;

    private InnerDB() {
    }

    protected static InnerDB getInstance(){
        if (null == db){
            db = new InnerDB();
        }
        return db;
    }

    protected static void setDb(InnerDB dbInstance){
        db = dbInstance;
    }


}
