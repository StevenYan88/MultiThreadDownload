package com.steven.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.steven.download.download.DownloadCallback;
import com.steven.download.download.DownloadFacade;
import com.steven.download.utils.Utils;
import com.steven.download.widget.CircleProgressbar;
import com.steven.download.widget.recyclerView.LinearLayoutItemDecoration;

import java.io.File;

public class ListDownloadActivity extends AppCompatActivity {
    private static final String TAG = "ListDownloadActivity";
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_download);
        rv = findViewById(R.id.rv);
        setupAdapter();
    }

    private void setupAdapter() {
        AppAdapter adapter = new AppAdapter(this, Utils.generateApp(), R.layout.app_item);
        rv.addItemDecoration(new LinearLayoutItemDecoration(this, R.drawable.linear_layout_item));
        rv.setAdapter(adapter);
        adapter.setOnCircleProgressbarClickListener((view, appEntity) -> {
            if (appEntity.downloadStatus == DownloadStatus.IDLE || appEntity.downloadStatus == DownloadStatus.PAUSE) {
                DownloadFacade.getFacade().startDownload(appEntity.url, appEntity.name, new DownloadCallback() {
                    @Override
                    public void onSuccess(File file) {
                        appEntity.downloadStatus = DownloadStatus.SUCCESS;
                        Log.d(TAG, file.getName() + "下载成功");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        appEntity.downloadStatus = DownloadStatus.FAIL;
                        runOnUiThread(() -> ((CircleProgressbar) view).setText("失败"));
                        Log.d(TAG, "下载失败" + e.getMessage());
                    }

                    @Override
                    public void onProgress(long progress, long currentLength) {
                        appEntity.downloadStatus = DownloadStatus.DOWNLOADING;
                        runOnUiThread(() -> ((CircleProgressbar) view).setCurrentProgress(Utils.keepTwoBit((float) progress / currentLength)));
                    }

                    @Override
                    public void onPause(long progress, long currentLength) {
                        appEntity.downloadStatus = DownloadStatus.PAUSE;
                        runOnUiThread(() -> ((CircleProgressbar) view).setText("继续"));
                    }
                });
            } else if (appEntity.downloadStatus == DownloadStatus.DOWNLOADING) {
                DownloadFacade.getFacade().stopDownload(appEntity.url);
            }
        });
    }
}
