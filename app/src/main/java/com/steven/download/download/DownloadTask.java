package com.steven.download.download;

import android.util.Log;

import com.steven.download.entity.DownloadEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:每个apk的下载，这个类需要复用的
 * Data：4/19/2018-1:45 PM
 *
 * @author: yanzhiwen
 */
public class DownloadTask {
    private static final String TAG = "DownloadTask";
    //文件下载的url
    private final String url;
    //文件的名称
    private final String name;
    //文件的大小
    private final long contentLength;
    //下载文件的线程的个数
    private final int threadSize;
    //线程下载成功的个数，AtomicInteger
    private final AtomicInteger successNumber = new AtomicInteger();
    //总进度=每个线程的进度的和
    private long totalProgress;
    //正在执行下载任务的runnable
    private final List<DownloadRunnable> downloadRunnables;
    private final DownloadCallback downloadCallback;

    public DownloadTask(String name, String url, int threadSize, long contentLength, DownloadCallback callBack) {
        this.name = name;
        this.url = url;
        this.threadSize = threadSize;
        this.contentLength = contentLength;
        this.downloadRunnables = new ArrayList<>();
        this.downloadCallback = callBack;
    }

    public void init() {
        this.totalProgress = 0;
        //初始化的时候，需要读取数据库
        List<DownloadEntity> entities = DaoManagerHelper.getManager().queryAll(url);
        //每个线程的下载的大小threadSize
        long threadSize = contentLength / this.threadSize;
        for (int i = 0; i < this.threadSize; i++) {
            //开始下载的位置
            long start = i * threadSize;
            //结束下载的位置
            long end = start + threadSize;
            if (i == this.threadSize - 1) {
                end = contentLength;
            }
            DownloadEntity downloadEntity = getEntity(i, entities);
            if (downloadEntity == null) {
                downloadEntity = new DownloadEntity(start, end, url, i, 0, contentLength);
            } else {
                Log.d(TAG, "init: 上次保存的进度progress=" + downloadEntity.toString());
                start = start + downloadEntity.getProgress();
            }
            totalProgress = totalProgress + downloadEntity.getProgress();
            if (threadSize == downloadEntity.getProgress()) {
                successNumber.incrementAndGet();
                return;
            }
            DownloadRunnable downloadRunnable = new DownloadRunnable(name, url, contentLength, i, start, end,
                    downloadEntity.getProgress(), downloadEntity, new DownloadCallback() {
                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "onFailure: " + e.getMessage());
                    //有一个线程发生异常，下载失败，需要把其它线程停止掉
                    downloadCallback.onFailure(e);
                    stopDownload();
                }

                @Override
                public void onSuccess(File file) {
                    successNumber.incrementAndGet();
                    if (successNumber.get() == DownloadTask.this.threadSize) {
                        Log.d(TAG, name + "下载成功");
                        downloadCallback.onSuccess(file);
                        DownloadDispatcher.getInstance().recyclerTask();
                        //如果下载完毕，清除数据库
                        DaoManagerHelper.getManager().remove(url);
                        totalProgress = 0;
                        successNumber.set(0);
                    }
                }

                @Override
                public void onProgress(long progress, long currentLength) {
                    //叠加下progress，实时去更新进度条,这里需要synchronized下
                    synchronized (DownloadTask.this) {
                        totalProgress = totalProgress + progress;
                        Log.d(TAG, "totalProgress" + totalProgress);
                        downloadCallback.onProgress(totalProgress, currentLength);
                    }
                }

                @Override
                public void onPause(long progress, long currentLength) {
                    downloadCallback.onPause(totalProgress, currentLength);
                    DownloadDispatcher.getInstance().recyclerTask();
                }
            });
            //通过线程池去执行
            DownloadDispatcher.getInstance().executorService().execute(downloadRunnable);
            downloadRunnables.add(downloadRunnable);

        }
    }


    private DownloadEntity getEntity(int threadId, List<DownloadEntity> entities) {
        for (DownloadEntity entity : entities) {
            if (threadId == entity.getThreadId()) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 停止下载
     */
    public void stopDownload() {
        Log.d(TAG, "停止下载");
        for (DownloadRunnable runnable : downloadRunnables) {
            runnable.stop();
        }
    }

    public String getUrl() {
        return url;
    }
}
