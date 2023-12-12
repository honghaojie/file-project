package com.hhj.fileproject.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * @author: hong.hj
 * @createDate: 2023/12/12 10:14
 * @description:
 */
public class CommonUtil {
    public static String base64Decode(String str) {
        str = Optional.ofNullable(str).orElse("");
        byte[] decodeByteArr = Base64.getUrlDecoder().decode(str);
        return new String(decodeByteArr, StandardCharsets.UTF_8);
    }
}
