package com.hzy.magic.app.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.hzy.libmagic.MagicApi;
import com.hzy.magic.app.R;
import com.hzy.magic.app.adapter.FileItemAdapter;
import com.hzy.magic.app.adapter.PathItemAdapter;
import com.hzy.magic.app.bean.FileInfo;
import com.hzy.magic.app.utils.FileUtils;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.main_storage_path)
    RecyclerView mPathList;

    @BindView(R.id.main_storage_list)
    RecyclerView mFileList;

    @BindView(R.id.main_storage_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private PathItemAdapter mPathAdapter;
    private FileItemAdapter mFileAdapter;
    private String mCurrentPath;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAboutDialog;
    private ExecutorService mThreadPool;
    private Stack<String> mHistoryStack;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mThreadPool = Executors.newFixedThreadPool(8);
        mHistoryStack = new Stack<>();
        initUI();
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        loadInitPath();
                    }

                    @Override
                    public void onDenied() {
                    }
                }).request();
    }


    private void initUI() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Please Wait...");
        mPathList.setAdapter(mPathAdapter = new PathItemAdapter(this, this));
        mPathList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mFileList.setAdapter(mFileAdapter = new FileItemAdapter(this, this));
        mFileList.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefresh.setOnRefreshListener(this);
    }

    @SuppressLint("CheckResult")
    private void loadInitPath() {
        mCurrentPath = Environment.getExternalStorageDirectory().getPath();
        mHistoryStack.push(mCurrentPath);
        loadPathInfo(mCurrentPath);
    }

    private void loadPathInfo(String path) {
        mThreadPool.submit(() -> {
            List<FileInfo> infoList = FileUtils.getInfoListFromPath(path);
            runOnUiThread(() -> {
                mCurrentPath = path;
                mFileAdapter.setDataList(infoList);
                mPathAdapter.setPathView(mCurrentPath);
                mPathList.scrollToPosition(mPathAdapter.getItemCount() - 1);
                mFileList.smoothScrollToPosition(0);
                mSwipeRefresh.setRefreshing(false);
                mProgressDialog.dismiss();
            });
        });
    }

    @Override
    protected void onDestroy() {
        mThreadPool.shutdownNow();
        MagicApi.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                return true;
            case R.id.menu_home:
                loadInitPath();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mHistoryStack.size() > 1) {
            mHistoryStack.pop();
            String backPath = mHistoryStack.peek();
            if (backPath != null) {
                loadPathInfo(backPath);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof String) {
            mProgressDialog.show();
            String path = (String) v.getTag();
            if (path != null) {
                if (mHistoryStack.empty() || !mHistoryStack.peek().equals(path)) {
                    mHistoryStack.push(path);
                }
                loadPathInfo(path);
            }
        } else {
            FileInfo info = (FileInfo) v.getTag();
            FileInfo.FileType type = info.getFileType();
            if (type == FileInfo.FileType.folderEmpty
                    || type == FileInfo.FileType.folderFull) {
                mProgressDialog.show();
                String path = info.getFilePath();
                mHistoryStack.push(path);
                loadPathInfo(path);
            }
        }
    }

    @Override
    public void onRefresh() {
        loadPathInfo(mCurrentPath);
    }

    private void showAboutDialog() {
        if (mAboutDialog == null) {
            String packageString = MagicApi.getPackageString();
            mAboutDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(packageString)
                    .create();
        }
        mAboutDialog.show();
    }
}
