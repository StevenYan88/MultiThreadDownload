package com.steven.download.entity;


import com.steven.download.DownloadStatus;

/**
 * Description:
 * Data：4/11/2019-10:28 AM
 *
 * @author yanzhiwen
 */
public class AppEntity {
    public String url;
    public String appIcon;
    public String name;
    public String version;
    public String size;
    public String downLoadCount;
    public DownloadStatus downloadStatus = DownloadStatus.IDLE;


    public AppEntity(String url, String appIcon, String name, String version, String size, String downLoadCount) {
        this.url = url;
        this.appIcon = appIcon;
        this.name = name;
        this.version = version;
        this.size = size;
        this.downLoadCount = downLoadCount;
    }
}
