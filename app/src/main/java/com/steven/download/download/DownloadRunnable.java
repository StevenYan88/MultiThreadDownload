package com.steven.download.download;

import android.os.Environment;
import android.util.Log;

import com.steven.download.download.db.DownloadEntity;
import com.steven.download.okhttp.OkHttpManager;
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
    //线程的状态
    private int mStatus = STATUS_DOWNLOADING;
    //文件下载的url
    private String url;
    //文件的名称
    private String name;
    //线程id
    private int threadId;
    //每个线程下载开始的位置
    private long start;
    //每个线程下载结束的位置
    private long end;
    //每个线程的下载进度
    private long mProgress;
    //文件的总大小 content-length
    private long mCurrentLength;
    private DownloadCallback downloadCallback;
    private DownloadEntity mDownloadEntity;


    public DownloadRunnable(String name, String url, long currentLength, int threadId, long start, long end,
                            long progress, DownloadEntity downloadEntity, DownloadCallback downloadCallback) {
        this.name = name;
        this.url = url;
        this.mCurrentLength = currentLength;
        this.threadId = threadId;
        this.start = start;
        this.end = end;
        this.mProgress = progress;
        this.mDownloadEntity = downloadEntity;
        this.downloadCallback = downloadCallback;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            Response response = OkHttpManager.getInstance().syncResponse(url, start, end);
            inputStream = response.body().byteStream();
            //保存文件的路径
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), name);
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //seek从哪里开始
            randomAccessFile.seek(start);
            int length;
            byte[] bytes = new byte[10 * 1024];
            while ((length = inputStream.read(bytes)) != -1) {
                if (mStatus == STATUS_STOP) {
                    downloadCallback.onPause(length, mCurrentLength);
                    saveToDb();
                    break;
                }
                //写入
                randomAccessFile.write(bytes, 0, length);
                this.mProgress = this.mProgress + length;
                Log.d(TAG, "run: mProgress=" + mProgress);
                //实时去更新下进度条
                downloadCallback.onProgress(length, mCurrentLength);
            }
            if (mStatus != STATUS_STOP) {
                downloadCallback.onSuccess(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onFailure(e);
        } finally {
            Utils.close(inputStream);
            Utils.close(randomAccessFile);
            //保存到数据库
            saveToDb();

        }
    }

    public void stop() {
        mStatus = STATUS_STOP;
    }

    private void saveToDb() {
        Log.d(TAG, "**************保存到数据库*******************");
        mDownloadEntity.setContentLength(mCurrentLength);
        mDownloadEntity.setThreadId(threadId);
        mDownloadEntity.setUrl(url);
        mDownloadEntity.setStart(start);
        mDownloadEntity.setEnd(end);
        mDownloadEntity.setProgress(mProgress);
        //保存到数据库
        DaoManagerHelper.getManager().addEntity(mDownloadEntity);
    }

}
