package com.company;

import java.io.UnsupportedEncodingException;

interface ObfuscatedUtil {
    static int i3f20y1OZk(String str1, String str2) {
        return str1.compareTo(str2);
    }

    static boolean i3f20y1OZl(String str1, String str2) {
        return str1.endsWith(str2);
    }

    static byte[] i3f20y1OZm(String str1, String str2) throws UnsupportedEncodingException {
        return str1.getBytes(str2);
    }

    static int i3f20y1OZn(String str1, String str2) {
        return str1.lastIndexOf(str2);
    }

    static String[] i3f20y1OZo(String str1, String str2) {
        return str1.split(str2);
    }

    static int i3f20y1OZp(String str1, String str2) {
        return str1.indexOf(str2);
    }

    static String i3f20y1OZq(String str1, String str2) {
        return str1.concat(str2);
    }
}