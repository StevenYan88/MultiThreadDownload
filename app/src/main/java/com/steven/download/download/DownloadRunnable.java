package com.steven.download.download;

import android.os.Environment;
import android.util.Log;

import com.steven.download.entity.DownloadEntity;
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
    private final String url;
    //文件的名称
    private final String name;
    //线程id
    private final int threadId;
    //每个线程下载开始的位置
    private final long start;
    //每个线程下载结束的位置
    private final long end;
    //每个线程的下载进度
    private long progress;
    //文件的总大小 content-length
    private final long currentLength;
    private final DownloadCallback downloadCallback;
    private final DownloadEntity downloadEntity;


    public DownloadRunnable(String name, String url, long currentLength, int threadId, long start, long end,
                            long progress, DownloadEntity downloadEntity, DownloadCallback downloadCallback) {
        this.name = name;
        this.url = url;
        this.currentLength = currentLength;
        this.threadId = threadId;
        this.start = start;
        this.end = end;
        this.progress = progress;
        this.downloadEntity = downloadEntity;
        this.downloadCallback = downloadCallback;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            Response response = OkHttpManager.getInstance().syncResponse(url, start, end);
            inputStream = response.body().byteStream();
            long contentLength = response.body().contentLength();
            Log.d(TAG, "start：" + start + ",end:" + end + "contentLength:" + contentLength + "progress =" + progress);
            //保存文件的路径
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), name);
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //seek从哪里开始
            randomAccessFile.seek(start);
            int length;
            byte[] bytes = new byte[10 * 1024];
            while ((length = inputStream.read(bytes)) != -1) {
                this.progress = this.progress + length;
                if (mStatus == STATUS_STOP) {
                    downloadCallback.onPause(length, currentLength);
                    break;
                }
                //写入
                randomAccessFile.write(bytes, 0, length);
                //实时去更新下进度条
                downloadCallback.onProgress(length, currentLength);
            }
            if (mStatus != STATUS_STOP) {
                Log.d(TAG, file.getName() + "线程id--" + threadId + "下载完成");
                downloadCallback.onSuccess(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onFailure(e);
        } finally {
            Utils.close(inputStream);
            Utils.close(randomAccessFile);
            //保存到数据库
            if (mStatus == STATUS_STOP) {
                saveToDb();
            }
        }
    }

    public void stop() {
        mStatus = STATUS_STOP;
    }

    private void saveToDb() {
        Log.d(TAG, "**************保存到数据库*******************");
        downloadEntity.setContentLength(currentLength);
        downloadEntity.setThreadId(threadId);
        downloadEntity.setUrl(url);
        downloadEntity.setStart(start);
        downloadEntity.setEnd(end);
        downloadEntity.setProgress(progress);
        //保存到数据库
        DaoManagerHelper.getManager().addEntity(downloadEntity);
    }

}
