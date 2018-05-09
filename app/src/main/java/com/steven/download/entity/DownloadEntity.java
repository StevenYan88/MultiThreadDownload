package zhiwenyan.cmccaifu.com.android2017.okhttp.download.db;

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

    private long progroess;

    private long contentLength;

    public DownloadEntity(String url, long start, long end, int threadId, long progroess, long contentLength) {
        this.url = url;
        this.start = start;
        this.end = end;
        this.threadId = threadId;
        this.progroess = progroess;
        this.contentLength = contentLength;
    }
}
