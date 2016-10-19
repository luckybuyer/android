package net.luckybuyer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.bean.User;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

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
    }

}
