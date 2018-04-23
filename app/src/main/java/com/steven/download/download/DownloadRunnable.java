package com.steven.download.download;

import android.util.Log;

import com.steven.download.okhttp.OkHttpManager;
import com.steven.download.utils.FileManager;
import com.steven.download.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Description:
 * Data：4/19/2018-1:45 PM
 *
 * @author: yanzhiwen
 */
public class DownloadRunnable implements Runnable {
    private static final String TAG = "DownloadRunnable";
    private static final int STATUS_DOWNLOADING = 1;
    private static final int STATUS_STOP = 2;
    //下载的url
    private String url;
    //线程id
    private int threadId;
    //每个线程下载开始的位置
    private long start;
    //每个线程下载结束的位置
    private long end;
    private DownloadCallback downloadCallback;
    //线程的状态
    private int mStatus = STATUS_DOWNLOADING;
    //每个线程的下在进度
    private long mProgress;
    //文件的总大小 content-length
    private long mCurrentLength;

    public DownloadRunnable(String url, long currentLength, int threadId, long start, long end, DownloadCallback downloadCallback) {
        this.url = url;
        this.mCurrentLength = currentLength;
        this.threadId = threadId;
        this.start = start;
        this.end = end;
        this.downloadCallback = downloadCallback;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            Response response = OkHttpManager.getInstance().syncResponse(url, start, end);
            Log.i(TAG, "contentLength=" + response.body().contentLength()
                    + "start=" + start + "end=" + end + "threadId=" + threadId);
            inputStream = response.body().byteStream();
            File file = FileManager.getInstance().getFile(url);
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //seek从哪里开始
            randomAccessFile.seek(start);
            int length;
            byte[] bytes = new byte[10 * 1024 * 1024];
            while ((length = inputStream.read(bytes)) != -1) {
                if (mStatus == STATUS_STOP)
                    break;
                //保存下进度，做断点 todo
                mProgress = mProgress + length;
                randomAccessFile.write(bytes, 0, length);
                //实时去更新下进度条，将每次写入的length传出去
                downloadCallback.onProgress(length, mCurrentLength);
            }
            downloadCallback.onSuccess(file);
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onFailure(e);
        } finally {
            Utils.close(inputStream);
            Utils.close(randomAccessFile);
            //保存到数据库 怎么存？？ todo
        }
    }

    public void stop() {
        mStatus = STATUS_STOP;
    }
}
