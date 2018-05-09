package com.steven.download.download;

import android.support.annotation.NonNull;
import android.util.Log;

import com.steven.download.okhttp.OkHttpManager;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Description:
 * Data：4/19/2018-1:45 PM
 *
 * @author: yanzhiwen
 */
public class DownloadDispatcher {
    private static final String TAG = "DownloadDispatcher";
    private static volatile DownloadDispatcher sDownloadDispatcher;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_SIZE = Math.max(3, Math.min(CPU_COUNT - 1, 5));
    //核心线程数
    private static final int CORE_POOL_SIZE = THREAD_SIZE;
    //线程池
    private ExecutorService mExecutorService;
    //private final Deque<DownloadTask> readyTasks = new ArrayDeque<>();
    private final Deque<DownloadTask> runningTasks = new ArrayDeque<>();
    //private final Deque<DownloadTask> stopTasks = new ArrayDeque<>();


    private DownloadDispatcher() {
    }

    public static DownloadDispatcher getInstance() {
        if (sDownloadDispatcher == null) {
            synchronized (DownloadDispatcher.class) {
                if (sDownloadDispatcher == null) {
                    sDownloadDispatcher = new DownloadDispatcher();
                }
            }
        }
        return sDownloadDispatcher;
    }

    /**
     * 创建线程池
     *
     * @return mExecutorService
     */
    public synchronized ExecutorService executorService() {
        if (mExecutorService == null) {
            mExecutorService = new ThreadPoolExecutor(CORE_POOL_SIZE, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return mExecutorService;
    }


    /**
     * @param name     文件名
     * @param url      下载的地址
     * @param callBack 回调接口
     */
    public void startDownload(final String name, final String url, final DownloadCallback callBack) {
        Call call = OkHttpManager.getInstance().asyncCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                //获取文件的大小
                long contentLength = response.body().contentLength();
                Log.i(TAG, "contentLength=" + contentLength);
                if (contentLength <= -1) {
                    return;
                }
                DownloadTask downloadTask = new DownloadTask(name, url, THREAD_SIZE, contentLength, callBack);
                downloadTask.init();
                runningTasks.add(downloadTask);
            }
        });
    }

    /**
     * 根据url 去暂停那个
     *
     * @param url
     */
    public void stopDownLoad(String url) {
        //这个停止是不是这个正在下载的
        for (DownloadTask runningTask : runningTasks) {
            if (runningTask.getUrl().equals(url)) {
                runningTask.stopDownload();
            }
        }
    }

    /**
     * @param downLoadTask 下载任务
     */
    public void recyclerTask(DownloadTask downLoadTask) {
        runningTasks.remove(downLoadTask);
        //参考OkHttp的Dispatcher()的源码
        //readyTasks.
    }


}
