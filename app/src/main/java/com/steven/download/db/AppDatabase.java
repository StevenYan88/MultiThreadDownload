package com.steven.download.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.steven.download.db.dao.DownloadEntityDao;
import com.steven.download.entity.DownloadEntity;

/**
 * @description:
 * @create: 2022-04-18
 * @author: yanzhiwen
 */
@Database(entities = {DownloadEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app.db";
    private static volatile AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = buildDatabase(context);
                }
            }
        }
        return appDatabase;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public abstract DownloadEntityDao downloadEntityDao();

}
