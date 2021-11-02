package com.android.whatsappsaver;

public class StatusModel {
    String AbsolutePath;
    String path;
    int type;

    public StatusModel(String AbsolutePath) {
        this.AbsolutePath = AbsolutePath;
    }

    public StatusModel(String AbsolutePath, String path) {
        this.AbsolutePath = AbsolutePath;
        this.path = path;
    }

    public StatusModel(String AbsolutePath, String path, int type) {
        this.AbsolutePath = AbsolutePath;
        this.path = path;
        this.type = type;
    }

    public String getAbsolutePath() {
        return AbsolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        AbsolutePath = absolutePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
