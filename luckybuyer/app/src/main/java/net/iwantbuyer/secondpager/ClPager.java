package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.FCMBean;
import net.iwantbuyer.bean.PaySwitchBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by admin on 2017/2/6.
 */
public class ClPager extends BaseNoTrackPager {

    private RelativeLayout rl_cl_country;
    private RelativeLayout rl_cl_language;
    private ImageView iv_cl_country;
    private TextView tv_cl_country;
    private TextView tv_cl_language;
    private TextView tv_cl_apply;
    private TextView tv_set_title;
    private RelativeLayout rl_cl_back;
    private View inflate;

    private Configuration config;
    private Resources resources;
    private DisplayMetrics dm;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_cl, null);
        resources = this.getResources();
        dm = resources.getDisplayMetrics();
        config = resources.getConfiguration();
        findView();
        setView();
        return inflate;
    }

    private void findView() {
        rl_cl_country = (RelativeLayout) inflate.findViewById(R.id.rl_cl_country);
        rl_cl_language = (RelativeLayout) inflate.findViewById(R.id.rl_cl_language);
        iv_cl_country = (ImageView) inflate.findViewById(R.id.iv_cl_country);
        tv_cl_country = (TextView) inflate.findViewById(R.id.tv_cl_country);
        tv_cl_language = (TextView) inflate.findViewById(R.id.tv_cl_language);
        tv_cl_apply = (TextView) inflate.findViewById(R.id.tv_cl_apply);
        tv_set_title = (TextView) inflate.findViewById(R.id.tv_set_title);
        rl_cl_back = (RelativeLayout) inflate.findViewById(R.id.rl_cl_back);

        rl_cl_country.setOnClickListener(new MyOnClickListener());
        rl_cl_language.setOnClickListener(new MyOnClickListener());
        tv_cl_apply.setOnClickListener(new MyOnClickListener());
        rl_cl_back.setOnClickListener(new MyOnClickListener());
    }

    private void setView() {
        String servicecount = Utils.getSpData("servicecount", context);
        if ("1".equals(servicecount)) {
            rl_cl_country.setVisibility(View.GONE);
            tv_set_title.setText(context.getString(R.string.Language));
        } else {
            rl_cl_country.setVisibility(View.VISIBLE);
            tv_set_title.setText(context.getString(R.string.CountryLanguage));
        }

        try {
            Field field = R.drawable.class.getField(Utils.getSpData("country", context).replace(" ", "_").toLowerCase());
            int i = field.getInt(new R.drawable());
            iv_cl_country.setImageResource(i);
//            Log.e("TAG", image + "");
        } catch (Exception e) {

        }

        if (((ThirdPagerActivity) context).country != null) {
            try {
                Field field = R.drawable.class.getField(((ThirdPagerActivity) context).country.replace(" ", "_").toLowerCase());
                int i = field.getInt(new R.drawable());
                iv_cl_country.setImageResource(i);
//            Log.e("TAG", image + "");
            } catch (Exception e) {

            }
        }


        tv_cl_country.setText(Utils.getSpData("country", context) + "");

        String language = getResources().getConfiguration().locale.getLanguage();
        if (language != null && language.contains("ms")) {
            tv_cl_language.setText("Malaysia");
        } else if (language != null && language.contains("zh")) {
            tv_cl_language.setText("中文");
        } else {
            tv_cl_language.setText("English");
        }


        String spString = Utils.getSpData("locale", context);
        if (spString != null && "ms".equals(spString.split("-")[0] + "")) {
            tv_cl_language.setText("Malaysia");
        } else if (spString != null && "zh".equals(spString.split("-")[0] + "")) {
            tv_cl_language.setText("中文");
        } else if (spString != null && "en".equals(spString.split("-")[0] + "")) {
            tv_cl_language.setText("English");
        }

        if (((ThirdPagerActivity) context).country != null) {
            tv_cl_country.setText(((ThirdPagerActivity) context).country);
        }

        if (((ThirdPagerActivity) context).language != null) {
            if ("ms".equals(((ThirdPagerActivity) context).language)) {
                tv_cl_language.setText("Malaysia");
            } else if ("zh".equals(((ThirdPagerActivity) context).language)) {
                tv_cl_language.setText("中文");
            } else {
                tv_cl_language.setText("English");
            }
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        String spString = Utils.getSpData("locale", context);

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_cl_country:
                    ((ThirdPagerActivity) context).switchPage(3);
                    break;
                case R.id.rl_cl_language:
                    ((ThirdPagerActivity) context).switchPage(5);
                    break;
                case R.id.tv_cl_apply:

                    if (((ThirdPagerActivity) context).server == null) {
                        HttpUtils.getInstance().startNetworkWaiting(context);
                        FCMregist();
                    } else if (((ThirdPagerActivity) context).server != null && ((ThirdPagerActivity) context).server.equals(Utils.getSpData("service", context))) {
                        Utils.setSpData("country", ((ThirdPagerActivity) context).country, context);
                        FCMregist();
                    } else {
                        StartAlertDialog();
                        return;
                    }
                    break;
                case R.id.tv_country_cancel:
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_country_ok:
                    if (show != null && show.isShowing()) {
                        startWaitDialog();
                        startConfig(((ThirdPagerActivity) context).server);
                        show.dismiss();
                    }
                    break;
                case R.id.tv_welcome_done:
                    if (show_error != null && show_error.isShowing()) {
                        show_error.dismiss();
                    }
                    break;
                case R.id.rl_cl_back:
                    ((ThirdPagerActivity) context).finish();
                    break;
            }
        }
    }

    //FCM注册
    private void FCMregist() {
        String token = Utils.getSpData("refreshedToken", context);
        String mToken = Utils.getSpData("token", context);
        if (mToken == null || "".equals(mToken) || token == null) {
            return;
        }
        Utils.setSpData("refreshedToken", token, context);

        TimeZone tz = TimeZone.getDefault();
        String str;
        String lang;
        String spString = Utils.getSpData("locale", context);
        if (((ThirdPagerActivity) context).language != null) {
            lang = ((ThirdPagerActivity) context).language;
            str = tz.getID() + "&lang=" + lang;
        } else if(spString != null) {
            lang = (spString.split("-")[0] + "");
            str = tz.getID() + "&lang=" + (spString.split("-")[0] + "");
        }else {
            str = MyApplication.utc;
            lang = Utils.getSpData("locale", context);
            if (lang != null && !lang.equals("")) {
                lang = lang.split("-")[0] + "";
            }
        }
        String url = MyApplication.url + "/v1/fcm/registrations/?timezone=" + str;
        FCMBean fcm = new FCMBean(lang, "android", token);
        String json = fcm.toString();
        Log.e("TAG_clpager", url + json);
        Map map = new HashMap();
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
            }

            @Override
            public void error(final int code, final String message) {
                ((ThirdPagerActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == 204) {
                            String spString = Utils.getSpData("locale", context);
                            if (((ThirdPagerActivity) context).language != null) {
                                cLanguage(((ThirdPagerActivity) context).language, ((ThirdPagerActivity) context).country);
                            } else if (spString != null) {
                                cLanguage((spString.split("-")[0] + ""), ((ThirdPagerActivity) context).country);
                            }
                            Intent intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                        }
                        Log.e("TAG_clpager", code  + message);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((ThirdPagerActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                });
            }
        });
    }

    //FCM注册
    private void FCM(final PaySwitchBean paySwitchBean, final String method) {
        String token = Utils.getSpData("refreshedToken", context);
        String mToken = Utils.getSpData("token", context);
        if (mToken == null || "".equals(mToken) || token == null) {
            return;
        }
        Utils.setSpData("refreshedToken", token, context);
        TimeZone tz = TimeZone.getDefault();
        String str;
        String lang;
        String spString = Utils.getSpData("locale", context);
        if (((ThirdPagerActivity) context).language != null) {
            lang = ((ThirdPagerActivity) context).language;
            str = tz.getID() + "&lang=" + lang;
        } else if(spString != null) {
            lang = (spString.split("-")[0] + "");
            str = tz.getID() + "&lang=" + (spString.split("-")[0] + "");
        }else {
            str = MyApplication.utc;
            lang = Utils.getSpData("locale", context);
            if (lang != null && !lang.equals("")) {
                lang = lang.split("-")[0] + "";
            }
        }

        String url = MyApplication.url + "/v1/fcm/registrations/?timezone=" + str;
        FCMBean fcm = new FCMBean(lang, "android", token);
        String json = fcm.toString();

        Map map = new HashMap();
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
            }

            @Override
            public void error(final int code, final String message) {
                ((ThirdPagerActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == 204) {
                            setinformation(paySwitchBean, method);
                        } else {
                            StartErrorDialog();
                        }
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((ThirdPagerActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartErrorDialog();
                    }
                });
            }
        });
    }

    //根据国家与语言 来改变语言
    private void cLanguage(String language, String country) {
        if ("ms".equals(language)) {
            country = "MY";
            Locale locale = new Locale(language, country);
            config.locale = locale;
            resources.updateConfiguration(config, dm);

            Utils.setSpData("locale", language + "-" + country, context);
        } else if ("zh".equals(language)) {
            country = "CN";
            Locale locale = new Locale(language, country);
            config.locale = locale;
            resources.updateConfiguration(config, dm);

            Utils.setSpData("locale", language + "-" + country, context);
        } else {
            Locale locale = new Locale("en");
            config.locale = locale;
            resources.updateConfiguration(config, dm);
            Utils.setSpData("locale", language + "-" + country, context);
        }


        String spS = Utils.getSpData("locale", context);
        String lang = "en";
        if (spS != null && "ms".equals(spS.split("-")[0] + "")) {
            lang = "ms";
        } else if (spS != null && "zh".equals(spS.split("-")[0] + "")) {
            lang = "zh";
        } else if (spS != null && "en".equals(spS.split("-")[0] + "")) {
            lang = "en";
        }

        TimeZone tz = TimeZone.getDefault();
        MyApplication.utc = tz.getID() + "&lang=" + lang;


    }

    AlertDialog show;

    private void StartAlertDialog() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = View.inflate(context, R.layout.alertdialog_country, null);
        TextView tv_country_cancel = (TextView) inflate.findViewById(R.id.tv_country_cancel);
        TextView tv_country_ok = (TextView) inflate.findViewById(R.id.tv_country_ok);
        TextView tv_country_warn = (TextView) inflate.findViewById(R.id.tv_country_warn);
        tv_country_cancel.setOnClickListener(new MyOnClickListener());
        tv_country_ok.setOnClickListener(new MyOnClickListener());

        tv_country_warn.setText(context.getString(R.string.OneStorestart) + ((ThirdPagerActivity) context).store + context.getString(R.string.OneStorestart));


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private AlertDialog show_wait;
    AlertDialog show_error;

    private void startWaitDialog() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = View.inflate(context, R.layout.alertdialog_country_wait, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show_wait = builder.show();
//        show.setCanceledOnTouchOutside(false);   //点击外部不消失
        show_wait.setCancelable(false);               //点击外部和返回按钮都不消失
        show_wait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show_wait.getWindow();
        window.setGravity(Gravity.CENTER);
        show_wait.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
        show_wait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void startConfig(String server) {
        String pkName = context.getPackageName();
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;

            Log.e("TAG", versionName + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String url;//请求  充值列表
        url = server + "/v1/config/android-iwantbuyer-v" + versionName;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);

                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (show_wait != null && show_wait.isShowing()) {
                            show_wait.dismiss();
                        }
                        StartErrorDialog();
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (show_wait != null && show_wait.isShowing()) {
                            show_wait.dismiss();
                        }
                        StartErrorDialog();
                    }
                });
            }

        });
    }

    private void processData(String response) {
        Gson gson = new Gson();
        PaySwitchBean paySwitchBean = gson.fromJson(response, PaySwitchBean.class);
        String method = "";
        for (int i = 0; i < paySwitchBean.getPayment_methods().size(); i++) {
            method += paySwitchBean.getPayment_methods().get(i).getVendor();
        }
        FCM(paySwitchBean, method);
    }

    //设置各种信息
    private void setinformation(PaySwitchBean paySwitchBean, String method) {
        Utils.setSpData("paymentmethod", method, context);
        Utils.setSpData("service", ((ThirdPagerActivity) context).server, context);
        Utils.setSpData("country", ((ThirdPagerActivity) context).country, context);
        Utils.setSpData("client_id", paySwitchBean.getAuth0_client_id(), context);
        Utils.setSpData("domain", paySwitchBean.getAuth0_domain(), context);

        MyApplication.url = Utils.getSpData("service", context);
        MyApplication.client_id = Utils.getSpData("client_id", context);
        MyApplication.domain = Utils.getSpData("domain", context);

        Utils.setSpData("token", null, context);
        Utils.setSpData("token_num", null, context);


        String spString = Utils.getSpData("locale", context);
        if (((ThirdPagerActivity) context).language != null) {
            cLanguage(((ThirdPagerActivity) context).language, ((ThirdPagerActivity) context).country);
        } else if (spString != null) {
            cLanguage((spString.split("-")[0] + ""), ((ThirdPagerActivity) context).country);
        }


        if (((ThirdPagerActivity) context).language != null) {
            cLanguage(((ThirdPagerActivity) context).language, ((ThirdPagerActivity) context).country);
        } else if (spString != null) {
            cLanguage((spString.split("-")[0] + ""), ((ThirdPagerActivity) context).country);
        }

        Intent intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void StartErrorDialog() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = View.inflate(context, R.layout.alertdialog_welcome_neterror, null);
        TextView tv_welcome_done = (TextView) inflate.findViewById(R.id.tv_welcome_done);
        tv_welcome_done.setOnClickListener(new MyOnClickListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show_error = builder.show();
        show_error.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show_error.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show_error.getWindow();
        window.setGravity(Gravity.CENTER);
        show_error.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
        show_error.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
