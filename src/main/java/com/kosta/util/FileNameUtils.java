package com.kosta.util;

public class FileNameUtils {
    public static String extractFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String extractFileType(String url) {
        String name = extractFileName(url);
        int dotIndex = name.lastIndexOf('.');
        return dotIndex != -1 ? name.substring(dotIndex + 1) : "unknown";
    }
}

