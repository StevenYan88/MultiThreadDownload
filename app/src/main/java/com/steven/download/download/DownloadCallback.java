package com.steven.download.download;

import java.io.File;

/**
 * Description:
 * Data：4/19/2018-1:46 PM
 *
 * @author: yanzhiwen
 */
public interface DownloadCallback {
    /**
     * 下载成功
     *
     * @param file
     */
    void onSuccess(File file);

    /**
     * 下载失败
     *
     * @param e
     */
    void onFailure(Exception e);

    /**
     * 下载进度
     *
     * @param progress
     */
    void onProgress(long progress, long currentLength);

    /**
     * 暂停
     *
     * @param progress
     * @param currentLength
     */
    void onPause(long progress, long currentLength);
}
