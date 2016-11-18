package net.smartbuyer.app;

import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;

import java.util.TimeZone;

/**
 * Created by admin on 2016/9/20.
 */
public class MyApplication extends MultiDexApplication{

    public static String utc;
//    public static String url = "https://api-staging.luckybuyer.net";                      //测试服务器
    public static String url = "https://api-sg.luckybuyer.net";                           //新加波服务器
//    public static String url = "https://api-usw.luckybuyer.net";                          //美国服务器
//    public static String url = "http://192.168.166.236:8000";
    @Override
    public void onCreate() {
        super.onCreate();
        TimeZone tz = TimeZone.getDefault();
        utc = tz.getID();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

}
