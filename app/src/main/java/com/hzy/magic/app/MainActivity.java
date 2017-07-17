package com.hzy.magic.app;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_storage_path)
    RecyclerView mPathList;

    @Bind(R.id.main_storage_list)
    RecyclerView mFileList;

    @Bind(R.id.main_storage_refresh)
    SwipeRefreshLayout mSwipRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
