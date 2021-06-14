package com.steven.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.steven.download.download.DownloadCallback;
import com.steven.download.download.DownloadFacade;
import com.steven.download.utils.Utils;
import com.steven.download.widget.CircleProgressbar;

import java.io.File;

public class SingleDownloadActivity extends AppCompatActivity {
    private static final String TAG = SingleDownloadActivity.class.getSimpleName();
    private static final String url = "http://gdown.baidu.com/data/wisegame/f734f5b381b2f92a/shoujitaobao_239.apk";
    private static final String name = "手机淘宝.apk";
    private static final String iconUrl = "http://g.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=274fc2ab8c8ba61edfbbc0287318a038/b58f8c5494eef01fac53dca4eefe9925bc317d08.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_download);
        initData();
        initView();

    }

    private void initData() {
        ImageView icon = findViewById(R.id.icon);
        Glide.with(this)
                .load(iconUrl)
                .into(icon);
    }

    private void initView() {
        CircleProgressbar circleProgressbar = findViewById(R.id.pb);
        circleProgressbar.setOnClickListener(v -> {
            if (circleProgressbar.getText().equals("继续") || circleProgressbar.getText().equals("下载")) {
                DownloadFacade.getFacade().startDownload(url, name, new DownloadCallback() {
                    @Override
                    public void onSuccess(File file) {
                        Log.d(TAG, file.getName() + "下载成功");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "下载失败" + e.getMessage());
                    }

                    @Override
                    public void onProgress(long progress, long currentLength) {
                        runOnUiThread(() -> circleProgressbar.setCurrentProgress(Utils.keepTwoBit((float) progress / currentLength)));
                    }

                    @Override
                    public void onPause(long progress, long currentLength) {
                        runOnUiThread(() -> circleProgressbar.setText("继续"));
                    }
                });
            } else {
                DownloadFacade.getFacade().stopDownload(url);
            }
        });
    }

}
