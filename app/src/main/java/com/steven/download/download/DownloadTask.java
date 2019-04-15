package com.steven.download.download;

import android.util.Log;

import com.steven.download.download.db.DownloadEntity;

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
    private String url;
    //文件的名称
    private String name;
    //文件的大小
    private long mContentLength;
    //下载文件的线程的个数
    private int mThreadSize;
    //线程下载成功的个数，AtomicInteger
    private AtomicInteger mSuccessNumber = new AtomicInteger();
    //总进度=每个线程的进度的和
    private long mTotalProgress;
    //正在执行下载任务的runnable
    private List<DownloadRunnable> mDownloadRunnables;
    private DownloadCallback mDownloadCallback;
    private DownloadEntity downloadEntity;

    public DownloadTask(String name, String url, int threadSize, long contentLength, DownloadCallback callBack) {
        this.name = name;
        this.url = url;
        this.mThreadSize = threadSize;
        this.mContentLength = contentLength;
        this.mDownloadRunnables = new ArrayList<>();
        this.mDownloadCallback = callBack;
    }

    public void init() {
        //每个线程的下载的大小threadSize
        long threadSize = mContentLength / mThreadSize;
        List<DownloadEntity> entities = DaoManagerHelper.getManager().queryAll(url);
        for (int i = 0; i < mThreadSize; i++) {
            //初始化的时候，需要读取数据库
            //开始下载的位置
            long start = i * threadSize;
            //结束下载的位置
            long end = start + threadSize;
            if (i == mThreadSize - 1) {
                end = mContentLength;
            }
            downloadEntity = getEntity(i, entities);
            if (downloadEntity == null) {
                downloadEntity = new DownloadEntity(start, end, url, i, 0, mContentLength);
            } else {
                Log.d(TAG, "init: 上次保存的进度progress=" + downloadEntity.toString());
                start = start + downloadEntity.getProgress();
            }
            if (threadSize == downloadEntity.getProgress()) {
                mSuccessNumber.incrementAndGet();
                return;
            }
            DownloadRunnable downloadRunnable = new DownloadRunnable(name, url, mContentLength, i, start, end,
                    downloadEntity.getProgress(), downloadEntity, new DownloadCallback() {
                @Override
                public void onFailure(Exception e) {
                    //有一个线程发生异常，下载失败，需要把其它线程停止掉
                    mDownloadCallback.onFailure(e);
                    stopDownload();
                }

                @Override
                public void onSuccess(File file) {
                    mSuccessNumber.incrementAndGet();
                    if (mSuccessNumber.get() == mThreadSize) {
                        mDownloadCallback.onSuccess(file);
                        DownloadDispatcher.getInstance().recyclerTask(DownloadTask.this);
                        //如果下载完毕，清除数据库
                        DaoManagerHelper.getManager().remove(url);
                    }
                }

                @Override
                public void onProgress(long progress, long currentLength) {
                    //叠加下progress，实时去更新进度条
                    //这里需要synchronized下
                    synchronized (DownloadTask.this) {
                        mTotalProgress = mTotalProgress + progress;
                        Log.d(TAG, "onProgress: mTotalProgress" + mTotalProgress);
                        mDownloadCallback.onProgress(mTotalProgress, currentLength);
                    }
                }

                @Override
                public void onPause(long progress, long currentLength) {
                    mDownloadCallback.onPause(mTotalProgress, currentLength);

                }
            });
            //通过线程池去执行
            DownloadDispatcher.getInstance().executorService().execute(downloadRunnable);
            mDownloadRunnables.add(downloadRunnable);
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
        for (DownloadRunnable runnable : mDownloadRunnables) {
            runnable.stop();
        }
    }

    public String getUrl() {
        return url;
    }
}
