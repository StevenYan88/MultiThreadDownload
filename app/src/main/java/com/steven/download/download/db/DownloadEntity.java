package com.steven.download.download.db;


public class DownloadEntity {

    private long mStart;

    private long mEnd;

    private String url;

    private int threadId;

    private long progress;

    private long contentLength;

    public DownloadEntity(long mStart, long mEnd,String url,
            int threadId, long progress, long contentLength) {
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.url = url;
        this.threadId = threadId;
        this.progress = progress;
        this.contentLength = contentLength;
    }

    public DownloadEntity(){}

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getStart() {
        return mStart;
    }

    public void setStart(long mStart) {
        this.mStart = mStart;
    }

    public long getEnd() {
        return mEnd;
    }

    public void setEnd(long mEnd) {
        this.mEnd = mEnd;
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

    public long getMStart() {
        return this.mStart;
    }

    public void setMStart(long mStart) {
        this.mStart = mStart;
    }

    public long getMEnd() {
        return this.mEnd;
    }

    public void setMEnd(long mEnd) {
        this.mEnd = mEnd;
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
                "mStart=" + mStart +
                ", mEnd=" + mEnd +
                ", url='" + url + '\'' +
                ", threadId=" + threadId +
                ", progress=" + progress +
                ", contentLength=" + contentLength +
                '}';
    }
}
