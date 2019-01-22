package com.steven.download;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.steven.download.download.DownloadCallback;
import com.steven.download.download.DownloadFacade;
import com.steven.download.utils.Utils;
import com.steven.download.widget.CircleProgressbar;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION_CODE = 0x088;
    private String[] url = {"http://gdown.baidu.com/data/wisegame/f170a8c78bcf9aac/QQ_818.apk",
            "http://gdown.baidu.com/data/wisegame/89eb17d6287ae627/weixin_1300.apk",
            "http://gdown.baidu.com/data/wisegame/89fce26b620d8d43/QQkongjian_109.apk"};
    private String[] names = {"QQ_818.apk", "weixin_1300.apk", "QQkongjian_109.apk"};
    private CircleProgressbar mQQpb;
    private CircleProgressbar mWeChatPb;
    private CircleProgressbar mQzonePb;

    private static final String STATUS_DOWNLOADING = "downloading";
    private static final String STATUS_STOP = "stop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQQpb = findViewById(R.id.qq_pb);
        mQQpb.setTag(STATUS_DOWNLOADING);
        mWeChatPb = findViewById(R.id.wechat_pb);
        mWeChatPb.setTag(STATUS_DOWNLOADING);
        mQzonePb = findViewById(R.id.qzone_pb);
        mQzonePb.setTag(STATUS_DOWNLOADING);
        mQQpb.setOnClickListener(this);
        mWeChatPb.setOnClickListener(this);
        mQzonePb.setOnClickListener(this);
        int isPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (isPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            // 有三个类需要用户去关注，后面我们有可能会自己去更新代码，用户就需要换调用方式
            // 调用的方式 门面设计模式
            DownloadFacade.getFacade().init(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, "需要存储权限", Toast.LENGTH_SHORT).show();
                //没有授权的话，直接finish掉Activity
                finish();
            } else {
                DownloadFacade.getFacade().init(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_pb:
//                if (mQQpb.getTag().equals(STATUS_DOWNLOADING)) {
//                    mQQpb.setTag(STATUS_STOP);
                DownloadFacade.getFacade().startDownload(names[0], url[0], new DownloadCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "onFailure: 多线程下载失败");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i(TAG, "onSuccess:多线程下载成功 " + file.getAbsolutePath());
                    }

                    @Override
                    public void onProgress(final long totalProgress, final long currentLength) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mQQpb.setCurrentProgress(Utils.keepTwoBit(( float ) totalProgress / currentLength));
                            }
                        });
                    }

                    @Override
                    public void onPause(long progress, long currentLength) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, names[0] + "暂停下载", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //    }
//                else if (mQQpb.getTag().equals(STATUS_STOP)) {
//                    mQQpb.setTag(STATUS_DOWNLOADING);
//                    DownloadDispatcher.getInstance().stopDownLoad(url[0]);
//                    mQQpb.setText("继续");
//               }

                break;
            case R.id.wechat_pb:
//                if (mWeChatPb.getTag().equals(STATUS_DOWNLOADING)) {
//                    mWeChatPb.setTag(STATUS_STOP);
                DownloadFacade.getFacade().startDownload(names[1], url[1], new DownloadCallback() {
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
                                mWeChatPb.setCurrentProgress(Utils.keepTwoBit(( float ) totalProgress / currentLength));
                            }
                        });
                    }

                    @Override
                    public void onPause(long progress, long currentLength) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, names[1] + "暂停下载", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //     }
//                else if (mWeChatPb.getTag().equals(STATUS_STOP)) {
//                    mWeChatPb.setTag(STATUS_DOWNLOADING);
//                    DownloadDispatcher.getInstance().stopDownLoad(url[1]);
//                    mWeChatPb.setText("继续");
//                }
                break;
            case R.id.qzone_pb:
                DownloadFacade.getFacade().startDownload(names[2], url[2], new DownloadCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "onFailure: 多线程下载失败");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i(TAG, "onSuccess:多线程下载成功 " + file.getAbsolutePath());
                    }

                    @Override
                    public void onProgress(final long totalProgress, final long currentLength) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mQzonePb.setCurrentProgress(Utils.keepTwoBit(( float ) totalProgress / currentLength));
                            }
                        });
                    }

                    @Override
                    public void onPause(long progress, long currentLength) {

                    }
                });
                break;
        }
    }

}