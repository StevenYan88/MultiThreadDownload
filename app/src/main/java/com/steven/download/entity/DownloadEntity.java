package com.steven.download.entity;

/**
 * Description:
 * Data：3/21/2018-4:32 PM
 *
 * @author: yanzhiwen
 */
public class DownloadEntity {
    private String url;

    private long start;

    private long end;

    private int threadId;

    private long progress;

    private long contentLength;

    public DownloadEntity(String url, long start, long end, int threadId, long progress, long contentLength) {
        this.url = url;
        this.start = start;
        this.end = end;
        this.threadId = threadId;
        this.progress = progress;
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", threadId=" + threadId +
                ", progress=" + progress +
                ", contentLength=" + contentLength +
                '}';
    }
}
