package net.luckybuyer.app;

import android.app.Application;

import com.auth0.android.result.Credentials;

/**
 * Created by admin on 2016/9/20.
 */
public class MyApplication extends Application{

    public static String utc = "utc";
    public static String url = "https://api-sg.luckybuyer.net";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
