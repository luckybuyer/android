package net.luckybuyer.app;

import android.app.Application;
import android.util.Log;

import com.auth0.android.result.Credentials;
import com.facebook.FacebookSdk;

import java.util.TimeZone;

/**
 * Created by admin on 2016/9/20.
 */
public class MyApplication extends Application{

    public static String utc;
    public static String url = "https://api-staging.luckybuyer.net";
    @Override
    public void onCreate() {
        super.onCreate();
        TimeZone tz = TimeZone.getDefault();
        utc = tz.getID();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
