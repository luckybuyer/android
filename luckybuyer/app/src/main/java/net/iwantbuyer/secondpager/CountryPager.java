package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.activity.WelcomeActivity;
import net.iwantbuyer.adapter.ServersAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BasePager;
import net.iwantbuyer.bean.PaySwitchBean;
import net.iwantbuyer.bean.ServerBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/12/22.
 */
public class CountryPager extends BasePager {
    private View inflate;
    private RecyclerView rv_country;
    private RelativeLayout rl_country_back;
    private ServerBean serverBean;
    private AlertDialog show_wait;
    private AlertDialog show_error;

    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;
    private TextView tv_country_apply;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_country, null);
        findView();
        tv_country_apply.setVisibility(View.GONE);
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        rl_keepout.setVisibility(View.VISIBLE);
        rl_neterror.setVisibility(View.GONE);
        rl_nodata.setVisibility(View.GONE);
        rl_loading.setVisibility(View.VISIBLE);

        //请求  充值列表
        String url = MyApplication.url + "/v1/servers/?per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap();
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.length() > 10) {
                            StartView(response);
                            rl_keepout.setVisibility(View.GONE);
//                            tv_country_apply.setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
//                            tv_country_apply.setClickable(true);
//                            tv_country_apply.setEnabled(true);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                        }


                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "servers");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                });

            }

        });

    }

    private void findView() {
        rl_country_back = (RelativeLayout) inflate.findViewById(R.id.rl_country_back);
        tv_country_apply = (TextView) inflate.findViewById(R.id.tv_country_apply);
        rv_country = (RecyclerView) inflate.findViewById(R.id.rv_country);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);

//        tv_country_apply.setTextColor(getResources().getColor(R.color.text_gray));
//        tv_country_apply.setClickable(false);
//        tv_country_apply.setEnabled(false);
//
//        tv_country_apply.setOnClickListener(new MyOnClickListener());
        rl_country_back.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
    }

    String country = "";

    private void StartView(String response) {
        response = "{\"servers\":" + response + "}";
        serverBean = new Gson().fromJson(response, ServerBean.class);
        Log.e("TAG_country", response);
        ServersAdapter serversAdapter = new ServersAdapter(context, response);
        rv_country.setAdapter(serversAdapter);
        rv_country.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        serversAdapter.setOnClickListener(new ServersAdapter.OnClickListener() {
            @Override
            public void onclick(String country) {
                CountryPager.this.country = country;

                ((ThirdPagerActivity)context).country = country;
                for (int i = 0;i < serverBean.getServers().size();i++){
                    if (serverBean.getServers().get(i).getCountries().contains(country)) {
                        ((ThirdPagerActivity)context).server = serverBean.getServers().get(i).getApi_server();
                        ((ThirdPagerActivity)context).store = serverBean.getServers().get(i).getName();
                    }
                }

                ((ThirdPagerActivity)context).fragmentManager.popBackStack();
            }
        });

//        TextView tv_country_apply = (TextView) inflate.findViewById(R.id.tv_country_apply);
//        tv_country_apply.setOnClickListener(new MyOnClickListener());

    }


    int position = -1;
    //    String server = "";

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.tv_country_apply:
//
//                    if(Utils.getSpData("country",context).equals(country) || "".equals(country)) {
//                        Intent intent = new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                    for (int i = 0; i < serverBean.getServers().size(); i++) {
//                        if (serverBean.getServers().get(i).getCountries().contains(country)) {
//                            if (serverBean.getServers().get(i).getApi_server().equals(Utils.getSpData("service", context))) {
//                                Utils.setSpData("country",country,context);
//                                Intent intent = new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            } else {
//                                StartAlertDialog();
//                                position = i;
//                                return;
//                            }
//
//                        }
//                    }

//                    break;
                case R.id.rl_country_back:
                    ((ThirdPagerActivity) context).finish();
                    break;
                case R.id.tv_country_cancel:
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_country_ok:
                    if (show != null && show.isShowing()) {
                        String server = server = serverBean.getServers().get(position).getApi_server();

                        startWaitDialog();
                        startConfig(server);
                        show.dismiss();
                    }
                    break;
                case R.id.tv_welcome_done:
                    if (show_error != null && show_error.isShowing()) {
                        show_error.dismiss();
                    }
                    break;
            }
        }
    }


    private void startConfig(String server) {
        Log.e("TAG_config", server);
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
            public void success(final String response) {
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
        Log.e("TAG", response);
        Gson gson = new Gson();
        PaySwitchBean paySwitchBean = gson.fromJson(response, PaySwitchBean.class);
        String method = "";
        for (int i = 0; i < paySwitchBean.getPayment_methods().size(); i++) {
            method += paySwitchBean.getPayment_methods().get(i).getVendor();
        }
        Utils.setSpData("paymentmethod", method, context);
        Utils.setSpData("service", serverBean.getServers().get(position).getApi_server(), context);
        Utils.setSpData("country", country, context);
        Utils.setSpData("client_id", paySwitchBean.getAuth0_client_id(), context);
        Utils.setSpData("domain", paySwitchBean.getAuth0_domain(), context);

        MyApplication.url = Utils.getSpData("service", context);
        MyApplication.client_id = Utils.getSpData("client_id", context);
        MyApplication.domain = Utils.getSpData("domain", context);

        Utils.setSpData("token", null, context);
        Utils.setSpData("token_num", null, context);

        // 杀掉进程
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
//
//        Intent intent = new Intent(context, MainActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        
        if(serverBean.getServers().get(0).getCountries().contains(country)) {
            tv_country_warn.setText("Accounts are store specific. You may need a new account for North American Store. Proceed?");
        }else {
            tv_country_warn.setText("Accounts are store specific. You may need a new account for Middle East Store. Proceed?");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

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
