package com.steven.download;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.steven.download.download.DownloadFacade;
import com.steven.download.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 0x088;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSelfPermission();
        initView();

    }

    private void initView() {
        findViewById(R.id.btn_single).setOnClickListener(v -> startActivity(new Intent(this, SingleDownloadActivity.class)));
        findViewById(R.id.btn_list).setOnClickListener(v -> startActivity(new Intent(this, ListDownloadActivity.class)));
    }

    private void checkSelfPermission() {
        int isPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (isPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            // 有三个类需要用户去关注，后面我们有可能会自己去更新代码，用户就需要换调用方式
            // 调用的方式 门面设计模式
            DownloadFacade.getFacade().init(this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                ToastUtil.toast(MainActivity.this, "需要存储权限");
            } else {
                DownloadFacade.getFacade().init(this);
            }
        }
    }
}