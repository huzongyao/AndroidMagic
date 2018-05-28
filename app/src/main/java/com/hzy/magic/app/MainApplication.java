package com.hzy.magic.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by huzongyao on 2018/5/28.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
