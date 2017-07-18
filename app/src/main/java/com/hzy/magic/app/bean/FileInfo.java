package com.hzy.magic.app.bean;

/**
 * Created by huzongyao on 17-7-13.
 */

public class FileInfo {
    private String fileName;
    private String filePath;
    private String magicInfo;
    private FileType fileType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMagicInfo() {
        return magicInfo;
    }

    public void setMagicInfo(String magicInfo) {
        this.magicInfo = magicInfo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public enum FileType {
        folderFull,
        folderEmpty,
        fileKnown
    }
}
