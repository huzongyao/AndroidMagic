package com.hzy.libmagic;

/**
 * Created by HZY on 2017/7/8.
 */

public class MagicApi {
    public static native int getMagicVersion();

    public static native int loadFromFile(String magicPath);

    public static native int loadFromBytes(byte[] magicBytes);

    public static native String magicFile(String filePath);

    public static native int close();

    static {
        System.loadLibrary("magic");
    }
}
