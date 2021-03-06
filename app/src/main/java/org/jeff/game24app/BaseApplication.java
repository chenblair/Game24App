package org.jeff.game24app;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class BaseApplication extends Application {

    public static final String APP_ID = "ca-app-pub-3940256099942544~3347511713"; //test
    public static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"; //test

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(getApplicationContext(), APP_ID);
    }
}
