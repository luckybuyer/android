package net.iwantbuyer.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.util.Log;


import net.iwantbuyer.utils.Utils;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 2016/9/20.
 */
public class MyApplication extends MultiDexApplication {

    public static String utc;
//    public static String url = "https://api-staging.luckybuyer.net";                      //测试服务器
    //    public static String url = "https://ap、i-sg.luckybuyer.net";                           //新加波服务器
//    public static String url = "https://api-usw.luckybuyer.net";                          //美国服务器
    public static String url = "https://api-my.luckybuyer.net";                            //美国服务器
//    public static String url = "http://19、2.16e、8.166.236:8000";
    public static String client_id;
    public static String domain;


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the SDK before executing any other operations,
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);


        //多语言切换
        setLanguage();
//        Log.e("TAG_language", getResources().getConfiguration().locale.getLanguage()+"");
        String spS = Utils.getSpData("locale", this);
        String language = "en";
        if (spS != null && "ms".equals(spS.split("-")[0] + "")) {
            language = "ms";
        } else if (spS != null && "zh".equals(spS.split("-")[0] + "")) {
            language = "zh";
        } else if (spS != null && "en".equals(spS.split("-")[0] + "")) {
            language = "en";
        }

        TimeZone tz = TimeZone.getDefault();
        utc = tz.getID() + "&lang=" + language;
//        utc = "shnaghai"+ "&lang=" + language;
    }

    public void setLanguage() {
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language != null && language.contains("ms")) {
            Utils.setSpData("locale","ms-MY",this);
        } else if (language != null && language.contains("zh")) {
            Utils.setSpData("locale","zh-CN",this);
        }

        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        String spString = Utils.getSpData("locale", this);

        if (spString != null) {
            config.locale = new Locale(spString.split("-")[0], spString.split("-")[1]);
            resources.updateConfiguration(config, dm);
        }
    }
}
