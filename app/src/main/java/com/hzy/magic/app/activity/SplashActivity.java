package com.hzy.magic.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hzy.libmagic.MagicApi;
import com.hzy.magic.app.R;

import java.io.InputStream;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ensureMagicAndStart();
    }

    private void ensureMagicAndStart() {
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getAssets().open("magic.mgc");
                    int length = inputStream.available();
                    byte[] buffer = new byte[length];
                    if (inputStream.read(buffer) > 0) {
                        if (MagicApi.loadFromBytes(buffer) == 0) {
                            startMainApp();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }.start();
    }

    private void startMainApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
