package com.steven.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;


public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;


    // 持有外部数据库的引用
    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {

    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport<>();
        daoSupport.init(mSqLiteDatabase, clazz);
        return daoSupport;
    }

    public void init(Context context) {
        // 把数据库放到内存卡里面  判断是否有存储卡 6.0要动态申请权限
        File dbRoot = new File(context.getCacheDir() + File.separator + "database");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, "download.db");

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }
}
