package com.hzy.magic.app;

import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hzy.libmagic.MagicApi;
import com.hzy.magic.app.adapter.FileItemAdapter;
import com.hzy.magic.app.adapter.PathItemAdapter;
import com.hzy.magic.app.bean.FileInfo;
import com.hzy.magic.app.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.security.auth.login.LoginException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.main_storage_path)
    RecyclerView mPathList;

    @Bind(R.id.main_storage_list)
    RecyclerView mFileList;

    @Bind(R.id.main_storage_refresh)
    SwipeRefreshLayout mSwipRefresh;
    private PathItemAdapter mPathAdapter;
    private FileItemAdapter mFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        initMagicFromAssets();
        loadPathInfo(Environment.getExternalStorageDirectory().getPath());
    }

    private void initUI() {
        mPathList.setAdapter(mPathAdapter = new PathItemAdapter(this, this));
        mPathList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFileList.setAdapter(mFileAdapter = new FileItemAdapter(this, this));
        mFileList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadPathInfo(String path) {
        List<FileInfo> infoList = FileUtils.getInfoListFromPath(path);
        mFileAdapter.setDataList(infoList);
        mPathAdapter.setPathView(path);
        mPathList.scrollToPosition(mPathAdapter.getItemCount() - 1);
        mFileList.smoothScrollToPosition(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MagicApi.close();
    }

    private void initMagicFromAssets() {
        try {
            InputStream inputStream = getAssets().open("magic.mgc");
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            if (inputStream.read(buffer) > 0) {
                MagicApi.loadFromBytes(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof String) {
            loadPathInfo((String) v.getTag());
        } else {
            FileInfo info = (FileInfo) v.getTag();
            FileInfo.FileType type = info.getFileType();
            if (type == FileInfo.FileType.folderEmpty
                    || type == FileInfo.FileType.folderFull) {
                loadPathInfo(info.getFilePath());
            }
        }
    }
}
