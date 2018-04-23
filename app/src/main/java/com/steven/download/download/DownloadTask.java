package com.steven.download.download;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:每个apk的下载，这个类需要复用的
 * Data：4/19/2018-1:45 PM
 *
 * @author: yanzhiwen
 */
public class DownloadTask {
    private String url;
    private long mContentLength;
    private List<DownloadRunnable> mDownloadRunnables;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //最大线程数
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 3));
    //核心线程数
    private static final int CORE_POOL_SIZE = 3;
    private DownloadCallback mDownloadCallback;
    //线程池
    private ExecutorService mExecutorService;
    //线程下载成功的个数
    private volatile int mSuccessNumber;
    //总进度=每个线程的进度的和 变量加一个volatile
    private volatile long mTotalProgress;

    /**
     * 创造线程池
     *
     * @return mExecutorService
     */
    private synchronized ExecutorService executorService() {
        mExecutorService = new ThreadPoolExecutor(0, THREAD_SIZE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                thread.setName("DownloadTask");
                return thread;
            }
        });
        return mExecutorService;
    }

    public DownloadTask(String url, long contentLength, DownloadCallback callBack) {
        this.url = url;
        this.mContentLength = contentLength;
        this.mDownloadRunnables = new ArrayList<>();
        this.mDownloadCallback = callBack;
    }

    public void init() {
        for (int i = 0; i < THREAD_SIZE; i++) {
            //初始化的时候，需要读取数据库
            //每个线程的下载的大小threadSize
            long threadSize = mContentLength / THREAD_SIZE;
            //开始下载的位置
            long start = i * threadSize;
            //结束下载的位置
            long end = start + threadSize - 1;
            if (i == THREAD_SIZE - 1) {
                end = mContentLength - 1;
            }
            DownloadRunnable downloadRunnable = new DownloadRunnable(url, mContentLength, i, start, end, new DownloadCallback() {
                @Override
                public void onFailure(Exception e) {
                    //有一个线程发生异常，下载失败，需要把其它线程停止掉
                    mDownloadCallback.onFailure(e);
                    stopDownload();
                }

                @Override
                public void onSuccess(File file) {
                    mSuccessNumber = mSuccessNumber + 1;
                    if (mSuccessNumber == THREAD_SIZE) {
                        mDownloadCallback.onSuccess(file);
                        DownloadDispatcher.getInstance().recyclerTask(DownloadTask.this);
                        //如果下载完毕，清除数据库  todo
                    }
                }

                @Override
                public void onProgress(long progress, long currentLength) {
                    //叠加下progress，实时去更新进度条
                    mTotalProgress = mTotalProgress + progress;
                    mDownloadCallback.onProgress(mTotalProgress, currentLength);
                }
            });
            //通过线程池去执行
            executorService().execute(downloadRunnable);
            mDownloadRunnables.add(downloadRunnable);


        }
    }

    /**
     * 停止下载
     */
    public void stopDownload() {
        for (DownloadRunnable runnable : mDownloadRunnables) {
            runnable.stop();
        }
    }
}
