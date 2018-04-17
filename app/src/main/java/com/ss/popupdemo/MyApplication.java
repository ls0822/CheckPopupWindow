package com.ss.popupdemo;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by song on 2016/4/14.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CheckWindowManager.getInstance().init(getApplicationContext());
        CheckWindowManager.getInstance().setOnAppStatusListener(new CheckWindowManager.OnAppStatusListener() {
            @Override
            public void hasPopupWindow() {
                Toast.makeText(getApplicationContext(), "Has popup window", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAppNormal() {
                Toast.makeText(getApplicationContext(), "Return normal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInBackground() {
                Toast.makeText(getApplicationContext(), "In background", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CheckWindowManager.getInstance().release();

    }
}
