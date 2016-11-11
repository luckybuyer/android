package net.luckybuyer.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.auth0.android.result.Credentials;
import com.facebook.FacebookSdk;

import java.util.TimeZone;

/**
 * Created by admin on 2016/9/20.
 */
public class MyApplication extends MultiDexApplication{

    public static String utc;
    public static String url = "https://api-staging.luckybuyer.net";
//    public static String url = "http://192.168.166.236:8000";
    @Override
    public void onCreate() {
        super.onCreate();
        TimeZone tz = TimeZone.getDefault();
        utc = tz.getID();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

}
