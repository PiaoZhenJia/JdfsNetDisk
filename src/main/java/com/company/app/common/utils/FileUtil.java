package com.company.app.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Component
public class FileUtil {

    @Autowired
    private LogUtil logUtil;

    public byte[] multiPartFileToBeanByte(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        byte[] pic = new byte[(int) file.getSize()];
        is.read(pic);
        is.close();
        return pic;
    }

    public String getFileExt(String originalFilename) {
        return FilenameUtils.getExtension(originalFilename);
    }

    public void createDirIfNotExist(String needDir) {
        File dir = new File(needDir);
        if (dir.exists() && dir.isDirectory()) {
            logUtil.info(this, "文件夹已存在: " + dir);
        } else {
            logUtil.info(this, "文件夹不存在 即将创建: " + dir);
            boolean mkdir = dir.mkdir();
            if (mkdir) {
                logUtil.info(this, "创建文件夹成功: " + dir);
            } else {
                logUtil.error(this, "创建文件夹失败: " + dir);
            }
        }
    }

    /**
     * 如果文件存在会返回true
     *
     * @param needFile
     * @return
     * @throws IOException
     */
    public boolean createFileIfNotExist(String needFile) throws IOException {
        boolean exist = false;
        File file = new File(needFile);
        if (file.exists() && file.isFile()) {
            exist = true;
            logUtil.info(this, "文件已存在: " + file);
        } else {
            logUtil.info(this, "文件不存在 即将创建: " + file);
            boolean mkf = file.createNewFile();
            if (mkf) {
                logUtil.info(this, "创建文件成功: " + file);
            } else {
                logUtil.error(this, "创建文件失败: " + file);
            }
        }
        return exist;
    }

    public void coverFileAsByte(byte[] need, File file) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            out.write(need);
            logUtil.info(this, "文件已重置: " + file);
        } catch (IOException e) {
            logUtil.error(this, "文件覆盖失败: " + file);
        }
    }

    public InputStream getInputStreamFromFileInResources(String fileUrl) {
        return this.getClass().getClassLoader().getResourceAsStream(fileUrl);
    }

    public byte[] getByteFromFile(File file) {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
