package com.example.tong.jiaowuxitong.net;

/**
 * Created by TONG on 2017/1/9.
 */
public class TextUtil {
    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
