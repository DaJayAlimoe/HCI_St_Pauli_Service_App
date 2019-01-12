package com.service.hci.hci_service_app.zxing;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 *
 */
public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
