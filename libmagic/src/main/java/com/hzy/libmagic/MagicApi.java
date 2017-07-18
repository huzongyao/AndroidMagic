package com.hzy.libmagic;

/**
 * Created by HZY on 2017/7/8.
 */

public class MagicApi {

    public static final int MAGIC_NONE = 0x0000000; /* No flags */
    public static final int MAGIC_DEBUG = 0x0000001; /* Turn on debugging */
    public static final int MAGIC_SYMLINK = 0x0000002; /* Follow symlinks */
    public static final int MAGIC_COMPRESS = 0x0000004;/* Check inside compressed files */
    public static final int MAGIC_DEVICES = 0x0000008;/* Look at the contents of devices */
    public static final int MAGIC_MIME_TYPE = 0x0000010;/* Return the MIME type */
    public static final int MAGIC_CONTINUE = 0x0000020;/* Return all matches */
    public static final int MAGIC_CHECK = 0x0000040; /* Print warnings to stderr */
    public static final int MAGIC_PRESERVE_ATIME = 0x0000080;/* Restore access time on exit */
    public static final int MAGIC_RAW = 0x0000100; /* Don't convert unprintable chars */
    public static final int MAGIC_ERROR = 0x0000200;/* Handle ENOENT etc as real errors */
    public static final int MAGIC_MIME_ENCODING = 0x0000400;/* Return the MIME encoding */
    public static final int MAGIC_MIME = (MAGIC_MIME_TYPE | MAGIC_MIME_ENCODING);
    public static final int MAGIC_APPLE = 0x0000800; /* Return the Apple creator/type */
    public static final int MAGIC_EXTENSION = 0x1000000; /* Return a /-separated list of extensions */
    public static final int MAGIC_COMPRESS_TRANSP = 0x2000000; /* Check inside compressed files but not report compression */
    public static final int MAGIC_NODESC = (MAGIC_EXTENSION | MAGIC_MIME | MAGIC_APPLE);

    public static int loadFromFile(String magicPath) {
        return loadFromFile(magicPath, MAGIC_NONE);
    }

    public static int loadFromBytes(byte[] magicBytes) {
        return loadFromBytes(magicBytes, MAGIC_NONE);
    }

    public static native int getMagicVersion();

    public static native int loadFromFile(String magicPath, int flag);

    public static native int loadFromBytes(byte[] magicBytes, int flag);

    public static native String magicFile(String filePath);

    public static native int close();

    static {
        System.loadLibrary("magic");
    }
}
