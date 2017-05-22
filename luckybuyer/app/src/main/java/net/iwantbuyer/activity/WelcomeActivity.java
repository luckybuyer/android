package net.iwantbuyer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.adapter.ServersAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.PaySwitchBean;
import net.iwantbuyer.bean.ServerBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends Activity {
    private static final int WAHT = 1;
    private FrameLayout fl_welcome;
    private RelativeLayout rl_main;
    private ImageView iv_welcome;
    private RelativeLayout rl_welcome_pb;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };


//    private Configuration config;
//    private Resources resources;
//    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将标题栏设置城透
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_welcome);


        fl_welcome = (FrameLayout) findViewById(R.id.fl_welcome);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
        rl_welcome_pb = (RelativeLayout) findViewById(R.id.rl_welcome_pb);



        if (!isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }


        //开启选定服务
        if(Utils.getSpData("guide",this) == null) {
            startSever();
        }else {
            MyApplication.url = Utils.getSpData("service",this);
            MyApplication.client_id = Utils.getSpData("client_id",this);
            MyApplication.domain = Utils.getSpData("domain",this);

            startConfig();
            startMe();
            handler.sendEmptyMessageDelayed(WAHT, 3000);
        }


    }

    private void startMe() {
        if (Utils.getSpData("token", WelcomeActivity.this) != null) {
            String token = Utils.getSpData("token", WelcomeActivity.this);
            String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
            Map map = new HashMap<String, String>();
            map.put("Authorization", "Bearer " + token);

            //请求登陆接口
            HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(String response,String link) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    Utils.setSpData("id", user.getId() + "", WelcomeActivity.this);
                    Utils.setSpData("user_id", user.getAuth0_user_id(), WelcomeActivity.this);
                    Utils.setSpData("balance", user.getBalance() + "", WelcomeActivity.this);
                    Utils.setSpData("name", user.getProfile().getName(), WelcomeActivity.this);
                    Utils.setSpData("picture", user.getProfile().getPicture(), WelcomeActivity.this);
                    Utils.setSpData("social_link", user.getProfile().getSocial_link(), WelcomeActivity.this);
                }

                @Override
                public void error(int requestCode, String message) {

                }

                @Override
                public void failure(Exception exception) {

                }
            });
        }
    }


    private void startSever() {
        //请求  充值列表
        String url = MyApplication.url + "/v1/servers/?per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap();

        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        processServicesData(response);

                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartAlertDialog();
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartAlertDialog();
                    }
                });

            }

        });
    }
    private void startConfig() {
        String pkName = this.getPackageName();
        String versionName = "";
        try {
            versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;

            Log.e("TAG", versionName + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String url;//请求  充值列表
        url = MyApplication.url + "/v1/config/android-iwantbuyer-v" + versionName;
        Map map = new HashMap();

        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                        handler.sendEmptyMessage(1);
                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.setSpData("paymentmethod", "{\"cash\":[\"android-inapp\"]}", WelcomeActivity.this);
                        if(!WelcomeActivity.this.isDestroyed()) {
                            StartAlertDialog();
                        }
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.setSpData("paymentmethod", "{\"cash\":[\"android-inapp\"]}", WelcomeActivity.this);
                        if(!WelcomeActivity.this.isDestroyed()) {
                            StartAlertDialog();
                        }
                    }
                });

            }

        });
    }


    private void processServicesData(String response) {
//        response = "[{\"api_server\": \"https://api-ca.luckybuyer.net\",\"countries\": [\"Canada\", \"Oman\", \"Qatar\", \"Bahrain\", \"Saudi Arabia\"], \"h5\": \"https://ca.luckybuyer.net\", \"name\": \"Canada\", \"region\": \"ca\"}, {\"api_server\": \"https://api-ca.luckybuyer.net\", \"countries\": [\"Canada\"], \"h5\": \"https://ca.luckybuyer.net\", \"name\": \"Canada\", \"region\": \"ca\"},{\"api_server\": \"https://api-sg.luckybuyer.net\", \"countries\": [\"United Arab Emirates\", \"Oman\", \"Qatar\", \"Bahrain\", \"Saudi Arabia\", \"Kuwait\", \"Egypt\"], \"h5\": \"https://ae.luckybuyer.net\", \"name\": \"MENA\", \"region\": \"ae\"}]";
//        response = "[{\"api_server\": \"https://api-my.luckybuyer.net\", \"countries\": [\"Malaysia\"], \"h5\": \"https://my.luckybuyer.net\", \"name\": \"Southeast Asia Store\", \"region\": \"my\"}, {\"api_server\": \"https://api-ca.luckybuyer.net\", \"countries\": [\"Canada\"], \"h5\": \"https://ca.luckybuyer.net\", \"name\": \"North America Store\", \"region\": \"ca\"}]";
        response = "{\"servers\":" + response + "}";
        Gson gson = new Gson();
        ServerBean serverbean = gson.fromJson(response,ServerBean.class);
        if(serverbean.getServers().size() > 1) {
            StartView(response);
        }else {
            MyApplication.url = serverbean.getServers().get(0).getApi_server();
            Utils.setSpData("service",serverbean.getServers().get(0).getApi_server(),WelcomeActivity.this);
            Utils.setSpData("servicecount","1",this);
            startConfig();
        }
    }

    private void processData(String response) {
        Gson gson = new Gson();
        PaySwitchBean paySwitchBean = gson.fromJson(response, PaySwitchBean.class);
//        String method = "";
//        for (int i = 0; i < paySwitchBean.getPayment_methods().size(); i++) {
//            if(i == 0) {
//                method += paySwitchBean.getPayment_methods().get(i).getVendor();
//            }else {
//                method += "." + paySwitchBean.getPayment_methods().get(i).getVendor();
//            }
//
//        }
//        Utils.setSpData("paymentmethod", method, this);
        JSONObject jsonObject = new JSONObject();
        for (int i = 0;i < paySwitchBean.getPayment_methods().size();i++){
            try {
                jsonObject.accumulate(paySwitchBean.getPayment_methods().get(i).getVendor() + "",paySwitchBean.getPayment_methods().get(i).getMethod());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Utils.setSpData("paymentmethod", jsonObject.toString(), this);
        if(paySwitchBean.getPayment_methods().size() == 1) {
            Utils.setSpData("paymentmethod", "{\"cash\": [\"android-inapp\",]}", this);
        }

        Utils.setSpData("client_id",paySwitchBean.getAuth0_client_id(),WelcomeActivity.this);
        Utils.setSpData("domain",paySwitchBean.getAuth0_domain(),WelcomeActivity.this);
        Utils.setSpData("gifts_new_user",paySwitchBean.getGifts_new_user()+"",WelcomeActivity.this);
        Utils.setSpData("gifts_post_share",paySwitchBean.getGifts_post_share()+"",WelcomeActivity.this);
        Utils.setSpData("minimum_version",paySwitchBean.getMinimum_version()+"",WelcomeActivity.this);
        Utils.setSpData("latest_version",paySwitchBean.getLatest_version()+"",WelcomeActivity.this);

        MyApplication.client_id = Utils.getSpData("client_id",this);
        MyApplication.domain = Utils.getSpData("domain",this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    String country = "";

    private void StartView(String response) {

        View inflate = View.inflate(WelcomeActivity.this, R.layout.pager_country, null);
        RecyclerView rv_country = (RecyclerView) inflate.findViewById(R.id.rv_country);
        if (Utils.checkDeviceHasNavigationBar(this)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getScreenHeight(this));
            fl_welcome.setLayoutParams(lp);
        }
        if (Utils.checkDeviceHasNavigationBar(this)) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getScreenHeight(this));
            lp.topMargin = Utils.getScreenHeight(this);
            rl_main.setLayoutParams(lp);
        }
        rl_main.setVisibility(View.VISIBLE);
//        iv_welcome.setBackgroundColor(getResources().getColor(R.color.text_black));
        fl_welcome.addView(inflate);

        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        fl_welcome.setAnimation(mShowAction);
        fl_welcome.setVisibility(View.VISIBLE);

//        RecyclerView rv_country = (RecyclerView) inflate.findViewById(R.id.rv_country);
        RelativeLayout rl_country_back = (RelativeLayout) inflate.findViewById(R.id.rl_country_back);
        RelativeLayout rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_country_back.setVisibility(View.GONE);
        rl_keepout.setVisibility(View.GONE);
        ServersAdapter serversAdapter = new ServersAdapter(this, response);
        rv_country.setAdapter(serversAdapter);
        rv_country.setLayoutManager(new LinearLayoutManager(WelcomeActivity.this,LinearLayoutManager.VERTICAL,false));

        serversAdapter.setOnClickListener(new ServersAdapter.OnClickListener() {
            @Override
            public void onclick(String country) {
                WelcomeActivity.this.country = country;
            }
        });

        TextView tv_country_apply = (TextView) inflate.findViewById(R.id.tv_country_apply);
        tv_country_apply.setOnClickListener(new MyOnClickListener(response));
    }

    class MyOnClickListener implements View.OnClickListener {

        String response;
        int count;
        public MyOnClickListener(String response) {
            this.response = response;
        }
        public MyOnClickListener() {
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_country_apply:
                    if("".equals(country)) {
                        Utils.MyToast(WelcomeActivity.this,"Please select country");
                        return;
                    }
                    ServerBean serverBean = new Gson().fromJson(response, ServerBean.class);
                    for (int i = 0; i < serverBean.getServers().size(); i++) {
                        if(serverBean.getServers().get(i).getCountries().contains(country)) {
                            MyApplication.url = serverBean.getServers().get(i).getApi_server();
                            Utils.setSpData("country",country,WelcomeActivity.this);
                            Utils.setSpData("service",serverBean.getServers().get(i).getApi_server(),WelcomeActivity.this);
                        }
                    }
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    mShowAction.setDuration(500);
                    fl_welcome.setAnimation(mShowAction);
                    fl_welcome.setVisibility(View.GONE);
                    MyApplication.url = Utils.getSpData("service",WelcomeActivity.this);
                    startConfig();
                    rl_welcome_pb.setVisibility(View.VISIBLE);

                    break;
                case R.id.tv_welcome_done:
                    if(show != null && show.isShowing()) {
                        show.dismiss();
                        if("".equals(country)) {
                            startSever();
                        }else {
                            startConfig();
                        }
                    }
                    break;
            }
        }
    }
    AlertDialog show;
    private void StartAlertDialog() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = View.inflate(this,R.layout.alertdialog_welcome_neterror,null);
        TextView tv_welcome_done = (TextView) inflate.findViewById(R.id.tv_welcome_done);
        tv_welcome_done.setOnClickListener(new MyOnClickListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fl_welcome.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
