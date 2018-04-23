package com.steven.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.steven.download.download.DownloadCallback;
import com.steven.download.download.DownloadDispatcher;
import com.steven.download.utils.FileManager;
import com.steven.download.widget.CircleProgressbar;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String[] url = {"http://gdown.baidu.com/data/wisegame/f170a8c78bcf9aac/QQ_818.apk",
            "http://gdown.baidu.com/data/wisegame/89eb17d6287ae627/weixin_1300.apk",
            "http://gdown.baidu.com/data/wisegame/89fce26b620d8d43/QQkongjian_109.apk"};
    private CircleProgressbar mQQpb;
    private CircleProgressbar mWeChatPb;
    private CircleProgressbar mQzonePb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQQpb = findViewById(R.id.qq_pb);
        mWeChatPb = findViewById(R.id.wechat_pb);
        mQzonePb = findViewById(R.id.qzone_pb);
        mQQpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.getInstance().init(MainActivity.this);
                DownloadDispatcher.getInstance().startDownload(url[0], new DownloadCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i("DownLoadActivity", "onFailure: 多线程下载失败");

                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i("DownLoadActivity", "onSuccess:多线程下载成功 " + file.getAbsolutePath());
                    }

                    @Override
                    public void onProgress(final long totalProgress, final long currentLength) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.i(TAG, "totalProgress==" + totalProgress);
                                //更新进度条
                            }
                        });
                    }
                });
            }
        });
        mWeChatPb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.getInstance().init(MainActivity.this);
                DownloadDispatcher.getInstance().startDownload(url[1], new DownloadCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i("DownLoadActivity", "onFailure: 多线程下载失败");

                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i("DownLoadActivity", "onSuccess:多线程下载成功 " + file.getAbsolutePath());
                    }

                    @Override
                    public void onProgress(long progress, long currentLength) {

                    }
                });


            }
        });
        mQzonePb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.getInstance().init(MainActivity.this);
                DownloadDispatcher.getInstance().startDownload(url[2], new DownloadCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i("DownLoadActivity", "onFailure: 多线程下载失败");

                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i("DownLoadActivity", "onSuccess:多线程下载成功 " + file.getAbsolutePath());
                    }

                    @Override
                    public void onProgress(long progress, long currentLength) {

                    }
                });

            }
        });

    }
}