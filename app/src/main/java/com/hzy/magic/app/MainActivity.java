package com.hzy.magic.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hzy.libmagic.MagicApi;
import com.hzy.magic.app.adapter.FileItemAdapter;
import com.hzy.magic.app.adapter.PathItemAdapter;
import com.hzy.magic.app.bean.FileInfo;
import com.hzy.magic.app.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        Consumer<List<FileInfo>> {

    @BindView(R.id.main_storage_path)
    RecyclerView mPathList;

    @BindView(R.id.main_storage_list)
    RecyclerView mFileList;

    @BindView(R.id.main_storage_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private PathItemAdapter mPathAdapter;
    private FileItemAdapter mFileAdapter;
    private String mCurPath;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAboutDialog;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 203);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 203) {
            loadInitPath();
        }
    }

    private void initUI() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Please Wait...");
        mPathList.setAdapter(mPathAdapter = new PathItemAdapter(this, this));
        mPathList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFileList.setAdapter(mFileAdapter = new FileItemAdapter(this, this));
        mFileList.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefresh.setOnRefreshListener(this);
    }

    private void loadInitPath() {
        final String path = Environment.getExternalStorageDirectory().getPath();
        Observable.create(new ObservableOnSubscribe<List<FileInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FileInfo>> e) throws Exception {
                if (initMagicFromAssets()) {
                    List<FileInfo> infoList = FileUtils.getInfoListFromPath(path);
                    mCurPath = path;
                    e.onNext(infoList);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private void loadPathInfo(final String path) {
        Observable.create(new ObservableOnSubscribe<List<FileInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FileInfo>> e) throws Exception {
                List<FileInfo> infoList = FileUtils.getInfoListFromPath(path);
                mCurPath = path;
                e.onNext(infoList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MagicApi.close();
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

    private boolean initMagicFromAssets() {
        try {
            InputStream inputStream = getAssets().open("magic.mgc");
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            if (inputStream.read(buffer) > 0) {
                return MagicApi.loadFromBytes(buffer) == 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof String) {
            mProgressDialog.show();
            loadPathInfo((String) v.getTag());
        } else {
            FileInfo info = (FileInfo) v.getTag();
            FileInfo.FileType type = info.getFileType();
            if (type == FileInfo.FileType.folderEmpty
                    || type == FileInfo.FileType.folderFull) {
                mProgressDialog.show();
                loadPathInfo(info.getFilePath());
            }
        }
    }

    @Override
    public void onRefresh() {
        loadPathInfo(mCurPath);
    }

    @Override
    public void accept(List<FileInfo> fileInfos) throws Exception {
        mFileAdapter.setDataList(fileInfos);
        mPathAdapter.setPathView(mCurPath);
        mPathList.scrollToPosition(mPathAdapter.getItemCount() - 1);
        mFileList.smoothScrollToPosition(0);
        mSwipeRefresh.setRefreshing(false);
        mProgressDialog.dismiss();
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
