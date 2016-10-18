package net.luckybuyer.app;

import android.app.Application;

import com.auth0.android.result.Credentials;
import com.facebook.FacebookSdk;

/**
 * Created by admin on 2016/9/20.
 */
public class MyApplication extends Application{

    public static String utc = "utc";
    public static String url = "https://api-staging.luckybuyer.net";
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
