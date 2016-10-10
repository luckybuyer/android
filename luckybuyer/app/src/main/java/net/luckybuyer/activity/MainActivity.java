package net.luckybuyer.activity;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.bean.CourseBean;
import net.luckybuyer.bean.TokenBean;
import net.luckybuyer.bean.User;
import net.luckybuyer.pager.BuyChipsPager;
import net.luckybuyer.pager.HomePager;
import net.luckybuyer.pager.MePager;
import net.luckybuyer.pager.ShowPager;
import net.luckybuyer.pager.WinningPager;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.StatusBarUtils;
import net.luckybuyer.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private FrameLayout fl_main;
    public RadioGroup rg_main;
    private List<Fragment> list;
    public Lock lock;

    public String homeBanner;
    public String homeProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        new StatusBarUtils(this).statusBar();
        setContentView(R.layout.activity_main);

        //auth0登陆
        Auth0 auth0 = new Auth0("HmF3R6dz0qbzGQoYtTuorgSmzgu6Aua1", "staging-luckybuyer.auth0.com");
        this.lock = Lock.newBuilder(auth0, callback)
                // Add parameters to the Lock Builder
                .build();
        this.lock.onCreate(this);

        //设置数据
        setData();
        //发现视图  设置监听
        findView();


    }


    //设置数据
    private void setData() {
        list = new ArrayList<>();
        list.add(new HomePager());
        list.add(new BuyChipsPager());
        list.add(new WinningPager());
        list.add(new ShowPager());
        list.add(new MePager());
    }

    //试图初始化 与设置监听
    private void findView() {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        //设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = -1;
            if (checkedId == rg_main.getChildAt(0).getId()) {
                id = 0;
                switchPage(id);
            } else if (checkedId == rg_main.getChildAt(1).getId()) {
                id = 1;
                switchPage(id);
            } else if (checkedId == rg_main.getChildAt(2).getId()) {
                id = 2;
                switchPage(id);
            } else if (checkedId == rg_main.getChildAt(3).getId()) {
                id = 3;
                switchPage(id);
            } else if (checkedId == rg_main.getChildAt(4).getId()) {

                String token_s = Utils.getSpData("token_num", MainActivity.this);
                int token = 0;
                if (token_s != null) {
                    token = Integer.parseInt(token_s);
                }

                //判断是否登陆  未登陆  先登录  登陆 进入me页面
                if (token > System.currentTimeMillis() / 1000) {
                    id = 4;
                    switchPage(id);
                } else {
                    MainActivity.this.startActivity(MainActivity.this.lock.newIntent(MainActivity.this));
                }

            }
        }
    }


    //选择哪个界面
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    //auth0登陆回调
    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {

            // Base64 解码：
            String token = credentials.getIdToken();
            Log.e("TAG_TOKEN", token);


            byte[] mmmm = Base64.decode(token, Base64.URL_SAFE);
            String str = null;
            try {
                str = new String(mmmm, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String[] use = str.split("\\}");
            String user = use[1] + "}";

            TokenBean tokenBean = new Gson().fromJson(user, TokenBean.class);

            Utils.setSpData("token", token, MainActivity.this);
            Utils.setSpData("token_num", tokenBean.getExp() + "", MainActivity.this);
            String url = "https://api-staging.luckybuyer.net/v1/users/me/?timezone=utc";
            Map map = new HashMap<String, String>();
            map.put("Authorization", "Bearer " + token);
            //请求登陆接口
            HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    Utils.setSpData("id", user.getId()+"", MainActivity.this);
                    Utils.setSpData("user_id", user.getAuth0_user_id(), MainActivity.this);
                    Utils.setSpData("balance", user.getBalance() + "", MainActivity.this);
                    Utils.setSpData("name", user.getProfile().getName(), MainActivity.this);
                    Utils.setSpData("picture", user.getProfile().getPicture(), MainActivity.this);
                    Utils.setSpData("social_link", user.getProfile().getSocial_link(), MainActivity.this);

                    //登陆成功  直接进入me页面
                    switchPage(4);
                }

                @Override
                public void error(int requestCode, String message) {
                    Log.e("TAG", requestCode + "");
                    Log.e("TAG", message);
                }

                @Override
                public void failure(Exception exception) {

                }
            });

        }

        @Override
        public void onCanceled() {
            // Login Cancelled response
        }

        @Override
        public void onError(LockException error) {
            Log.e("TAG", error.toString());
        }
    };


    long mExitTime;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                Toast.makeText(this, "click again to exit", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
