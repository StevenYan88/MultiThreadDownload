package com.steven.download.download;

import android.content.Context;
import android.util.Log;

import com.steven.download.download.db.DaoSupportFactory;
import com.steven.download.download.db.DownloadEntity;
import com.steven.download.download.db.IDaoSupport;

import java.util.List;


public final class DaoManagerHelper {
    private static final String TAG = DaoManagerHelper.class.getSimpleName();
    private final static DaoManagerHelper sManager = new DaoManagerHelper();
    private IDaoSupport<DownloadEntity> mDaoSupport;

    private DaoManagerHelper() {

    }

    public static DaoManagerHelper getManager() {
        return sManager;
    }

    public void init(Context context) {
        DaoSupportFactory.getFactory().init(context);
        mDaoSupport = DaoSupportFactory.getFactory().getDao(DownloadEntity.class);
    }

    public void addEntity(DownloadEntity entity) {
        long delete = mDaoSupport.delete("url = ? and threadId = ?", entity.getUrl(), entity.getThreadId() + "");
        long size = mDaoSupport.insert(entity);
        Log.i(TAG, "DaoManagerHelper: "+size);

    }

    public List<DownloadEntity> queryAll(String url) {
        Log.i(TAG, "queryAll: "+url);
        return mDaoSupport.querySupport().selection("url = ?").selectionArgs(url).query();
    }

    public void remove(String url) {
        mDaoSupport.delete("url = ?", url);
    }
}
