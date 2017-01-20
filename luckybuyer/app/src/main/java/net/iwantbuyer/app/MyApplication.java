package net.iwantbuyer.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.mixpanel.android.util.StringUtils;

import net.iwantbuyer.utils.Utils;

import java.util.TimeZone;

/**
 * Created by admin on 2016/9/20.
 */
public class    MyApplication extends MultiDexApplication{

    public static String utc;
//    public static String url = "https://api-staging.luckybuyer.net";                      //测试服务器
//    public static String url = "https://api-sg.luckybuyer.net";                           //新加波服务器
//    public static String url = "https://api-usw.luckybuyer.net";                          //美国服务器
    public static String url = "https://api-my.luckybuyer.net";                            //美国服务器
//    public static String url = "http://192.168.166.236:8000";
    public static String client_id;
    public static String domain;

    public static MixpanelAPI mixpanel;
    @Override
    public void onCreate() {
        super.onCreate();


        TimeZone tz = TimeZone.getDefault();
        utc = tz.getID();
        // Initialize the SDK before executing any other operations,
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        //埋点
//        String projectToken = "79fb255c5fce0739c93fa063bd7990ca"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"     测试
        String projectToken = "1ad47d43bf1c4784bb8a037f420bebc3"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"     真的
        mixpanel = MixpanelAPI.getInstance(this, projectToken);
    }

}
