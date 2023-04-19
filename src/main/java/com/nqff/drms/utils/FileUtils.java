package com.nqff.drms.utils;

import java.io.File;

public class FileUtils {
    public static final String FILE_PATH = "~/DRMS";

    public static boolean isFileExisted(String filename) {
        File file = new File(filename);
        return file.exists();
    }
}
