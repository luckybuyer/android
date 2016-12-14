package net.iwantbuyer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import net.iwantbuyer.R;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.PaySwitchBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends Activity {

    private static final int WAHT = 1;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将标题栏设置城透
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_welcome);

        if(Utils.getSpData("token",WelcomeActivity.this)!= null) {
            String token = Utils.getSpData("token",WelcomeActivity.this);
            String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
            Map map = new HashMap<String, String>();
            map.put("Authorization", "Bearer " + token);
            //请求登陆接口
            HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(String response) {
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

        handler.sendEmptyMessageDelayed(WAHT,3000);

//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }
        if(!isTaskRoot()){
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if(mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)&&action.equals(Intent.ACTION_MAIN)){
                finish();
                return;
            }
        }

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

        
        //请求  产品  列表
        String url = MyApplication.url + "/v1/config/android-iwantbuyer-v" + versionName;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.setSpData("paymentmethod","android-inapp",WelcomeActivity.this);
                        Log.e("TAG", requestCode + message);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                WelcomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.setSpData("paymentmethod","android-inapp",WelcomeActivity.this);
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
        for (int i = 0;i < paySwitchBean.getPayment_methods().size();i++){
            method += paySwitchBean.getPayment_methods().get(i).getVendor();
        }
        Log.e("TAG_methos", method);
        Utils.setSpData("paymentmethod",method,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);                      //统计时长   友盟
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);                      //统计时长   友盟
    }
}
