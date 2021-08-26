package com.company.app.common.utils;

import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class SerializableUtil {

    public void objectToFile(Object obj, String filePath) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
        oos.writeObject(obj);
        oos.close();
    }

    public Object fileToObject(String filePath) throws IOException, ClassNotFoundException, RuntimeException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filePath)));
        Object obj = ois.readObject();
        return obj;
    }
}
