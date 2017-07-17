package com.hzy.magic.app.bean;

/**
 * Created by huzongyao on 17-7-13.
 */

public class FileInfo {
    private String fileName;
    private String filePath;
    private boolean isFolder;

    public FileInfo() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

}
