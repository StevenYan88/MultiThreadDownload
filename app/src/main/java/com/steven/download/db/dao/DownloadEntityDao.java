package com.steven.download.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.steven.download.entity.DownloadEntity;

import java.util.List;

/**
 * @description:
 * @create: 2022-04-18
 * @author: yanzhiwen
 */
@Dao
public interface DownloadEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadEntity downloadEntity);

    @Query("SELECT * FROM DownloadEntity WHERE url=:url")
    List<DownloadEntity> queryAllByUrl(String url);

    @Query("DELETE FROM DownloadEntity WHERE url =:url")
    void deleteDownloadEntity(String url);
}
