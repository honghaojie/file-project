package com.hhj.fileproject.utils;

/**
 * @author: hong.hj
 * @createDate: 2023/12/11 17:20
 * @description:
 */
public class CommonUtil {
    private static final String CURRENT_OS = System.getProperty("os.name").toLowerCase();
    public static boolean isMacOS() {
        return CURRENT_OS.startsWith("mac") && CURRENT_OS.contains("os");
    }

    public static boolean isWindows() {
        return CURRENT_OS.startsWith("windows");
    }
}
