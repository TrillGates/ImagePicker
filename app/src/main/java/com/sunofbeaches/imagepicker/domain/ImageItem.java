package com.sunofbeaches.imagepicker.domain;

public class ImageItem {

    private String path;

    private String title;

    private long date;

    public ImageItem(String path,String title,long date) {
        this.path = path;
        this.title = title;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
