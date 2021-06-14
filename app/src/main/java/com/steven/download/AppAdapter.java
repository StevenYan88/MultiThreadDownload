package com.steven.download;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.steven.download.download.DownloadCallback;
import com.steven.download.download.DownloadFacade;
import com.steven.download.entity.AppEntity;
import com.steven.download.utils.Utils;
import com.steven.download.widget.CircleProgressbar;
import com.steven.download.widget.recyclerView.CommonRecycleAdapter;
import com.steven.download.widget.recyclerView.CommonViewHolder;

import java.io.File;
import java.util.List;

/**
 * Description:
 * Data：4/11/2019-11:05 AM
 *
 * @author yanzhiwen
 */
public class AppAdapter extends CommonRecycleAdapter<AppEntity> {
    private static final String TAG = AppAdapter.class.getSimpleName();

    public AppAdapter(Context context, List<AppEntity> mDatas, int layoutId) {
        super(context, mDatas, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, AppEntity appEntity, int position) {
        ImageView appIcon = holder.getView(R.id.icon);
        Glide.with(appIcon.getContext())
                .load(appEntity.appIcon)
                .into(appIcon);
        holder.setText(R.id.name, appEntity.name)
                .setText(R.id.size, "大小:" + appEntity.size)
                .setText(R.id.downloadCount, "下载次数:" + appEntity.downLoadCount);
        CircleProgressbar progressbar = holder.getView(R.id.pb);
        progressbar.setOnClickListener(v -> {
            if (appEntity.downloadStatus == DownloadStatus.IDLE
                    || appEntity.downloadStatus == DownloadStatus.PAUSE
                    || appEntity.downloadStatus == DownloadStatus.FAIL) {
                DownloadFacade.getFacade().startDownload(appEntity.url, appEntity.name, new DownloadCallback() {
                    @Override
                    public void onSuccess(File file) {
                        appEntity.downloadStatus = DownloadStatus.SUCCESS;
                        progressbar.setText("成功");
                        Log.d(TAG, file.getName() + "下载成功");

                    }

                    @Override
                    public void onFailure(Exception e) {
                        appEntity.downloadStatus = DownloadStatus.FAIL;
                        progressbar.setText("失败");
                        Log.d(TAG, "下载失败" + e.getMessage());

                    }

                    @Override
                    public void onProgress(long progress, long currentLength) {
                        appEntity.downloadStatus = DownloadStatus.DOWNLOADING;
                        progressbar.setCurrentProgress(Utils.keepTwoBit((float) progress / currentLength));
                    }

                    @Override
                    public void onPause(long progress, long currentLength) {
                        appEntity.downloadStatus = DownloadStatus.PAUSE;
                        progressbar.setText("继续");
                    }
                });
            } else if (appEntity.downloadStatus == DownloadStatus.DOWNLOADING) {
                DownloadFacade.getFacade().stopDownload(appEntity.url);
            }
        });
    }
}
