package com.steven.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.steven.download.utils.Utils;
import com.steven.download.widget.recyclerView.LinearLayoutItemDecoration;

public class ListDownloadActivity extends AppCompatActivity {
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
    }
}
