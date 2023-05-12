package com.nqff.drms.utils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static final String FILE_PATH = "~/DRMS";

    public static boolean isFileExisted(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    public static boolean getFile(String openStyle, String filePath, HttpServletResponse response) throws Exception {
        File file = new File(filePath);
        if(!file.exists()){
            return false;
        }
        FileInputStream is = new FileInputStream(file);
        // 附件下载
        if(openStyle.equals("attachment")){
            response.setContentType("application/force-download");// 设置强制下载不打开
        }
        response.setHeader("content-disposition", openStyle+";filename=" + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8));
        // 获取响应response输出流
        ServletOutputStream os = response.getOutputStream();
        // 文件拷贝
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) > 0) {
            os.write(b, 0, len);
        }
        is.close();
        return true;
    }
}
