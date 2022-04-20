package com.steven.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.steven.download.download.DownloadCallback;
import com.steven.download.download.DownloadFacade;
import com.steven.download.entity.AppEntity;
import com.steven.download.utils.Utils;
import com.steven.download.widget.CircleProgressbar;

import java.io.File;

public class SingleDownloadActivity extends AppCompatActivity {
    private static final String TAG = SingleDownloadActivity.class.getSimpleName();
    private static final String url = "http://gdown.baidu.com/data/wisegame/f734f5b381b2f92a/shoujitaobao_239.apk";
    private static final String name = "手机淘宝.apk";
    private static final String iconUrl = "http://g.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=274fc2ab8c8ba61edfbbc0287318a038/b58f8c5494eef01fac53dca4eefe9925bc317d08.jpg";
    private CircleProgressbar circleProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_download);
        ImageView icon = findViewById(R.id.icon);
        Glide.with(this).load(iconUrl).into(icon);
        circleProgressbar = findViewById(R.id.pb);
        Log.d(TAG, "----onCreate----" + circleProgressbar);
        AppEntity appEntity = new AppEntity(url, iconUrl, name, "8.3.9", "97.8M", "10.8亿");
        circleProgressbar.setOnClickListener(v -> {
            if (appEntity.downloadStatus == DownloadStatus.IDLE || appEntity.downloadStatus == DownloadStatus.PAUSE) {
                startDownload(appEntity);
            } else if (appEntity.downloadStatus == DownloadStatus.DOWNLOADING) {
                stopDownload(appEntity);
            }
        });
    }

    private void startDownload(AppEntity appEntity) {
        DownloadFacade.getFacade().startDownload(appEntity.url, appEntity.name, new DownloadCallback() {
            @Override
            public void onSuccess(File file) {
                appEntity.downloadStatus = DownloadStatus.SUCCESS;
            }

            @Override
            public void onFailure(Exception e) {
                appEntity.downloadStatus = DownloadStatus.FAIL;
            }

            @Override
            public void onProgress(long progress, long currentLength) {
                appEntity.downloadStatus = DownloadStatus.DOWNLOADING;
                runOnUiThread(() -> circleProgressbar.setCurrentProgress(Utils.keepTwoBit((float) progress / currentLength)));
            }

            @Override
            public void onPause(long progress, long currentLength) {
                appEntity.downloadStatus = DownloadStatus.PAUSE;
                runOnUiThread(() -> circleProgressbar.setText("继续"));
            }
        });
    }

    private void stopDownload(AppEntity appEntity) {
        DownloadFacade.getFacade().stopDownload(appEntity.url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadFacade.getFacade().stopDownload(url);
    }
}
