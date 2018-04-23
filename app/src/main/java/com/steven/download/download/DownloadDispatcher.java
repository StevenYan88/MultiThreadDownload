package com.steven.download.download;

import android.support.annotation.NonNull;
import android.util.Log;

import com.steven.download.okhttp.OkHttpManager;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Description:
 * Data：4/19/2018-1:44 PM
 *
 * @author: yanzhiwen
 */
public class DownloadDispatcher {
    private static final String TAG = "DownloadDispatcher";
    private static final DownloadDispatcher INSTANCE = new DownloadDispatcher();
    private final Deque<DownloadTask> readyTasks = new ArrayDeque<>();

    private final Deque<DownloadTask> runningTasks = new ArrayDeque<>();

    private final Deque<DownloadTask> stopTasks = new ArrayDeque<>();

    private DownloadDispatcher() {
    }

    public static DownloadDispatcher getInstance() {
        return INSTANCE;
    }

    /**
     * @param url      下载的地址
     * @param callBack 回调接口
     */
    public void startDownload(final String url, final DownloadCallback callBack) {
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
                Log.i(TAG, "contentLength="+contentLength); //48508967  47250751
                if (contentLength <= -1) {
                    return;
                }
                DownloadTask downloadTask = new DownloadTask(url, contentLength, callBack);
                downloadTask.init();
                runningTasks.add(downloadTask);
            }
        });
    }


    /**
     * @param downLoadTask
     */
    public void recyclerTask(DownloadTask downLoadTask) {
        runningTasks.remove(downLoadTask);
        //参考OkHttp的Dispatcher()的源码
        //readyTasks.
    }

    public void stopDownLoad(String url) {
        //这个停止是不是这个正在下载的
    }
}
