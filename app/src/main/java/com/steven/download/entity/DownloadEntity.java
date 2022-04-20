package com.steven.download.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DownloadEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long start;

    private long end;

    private String url;

    private int threadId;

    private long progress;

    private long contentLength;


    public DownloadEntity(long start, long end, String url, int threadId, long progress, long contentLength) {
        this.start = start;
        this.end = end;
        this.url = url;
        this.threadId = threadId;
        this.progress = progress;
        this.contentLength = contentLength;
    }

    public DownloadEntity() {

    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStart() {
        return start;
    }


    public long getEnd() {
        return end;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "start=" + start +
                ", end=" + end +
                ", url='" + url + '\'' +
                ", threadId=" + threadId +
                ", progress=" + progress +
                ", contentLength=" + contentLength +
                '}';
    }
}
