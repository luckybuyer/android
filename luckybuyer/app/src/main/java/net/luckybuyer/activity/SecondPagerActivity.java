package net.luckybuyer.activity;

import android.annotation.TargetApi;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.bean.PreviousWinnerBean;
import net.luckybuyer.bean.ShippingAddressBean;
import net.luckybuyer.bean.TokenBean;
import net.luckybuyer.bean.User;
import net.luckybuyer.secondpager.AddAddressPager;
import net.luckybuyer.secondpager.BuyCoinPager;
import net.luckybuyer.secondpager.CoinDetailPager;
import net.luckybuyer.secondpager.DispatchPager;
import net.luckybuyer.secondpager.PreviousWinnersPager;
import net.luckybuyer.secondpager.ProductDetailPager;
import net.luckybuyer.secondpager.ProductInformationPager;
import net.luckybuyer.secondpager.SetPager;
import net.luckybuyer.secondpager.ShippingAddressPager;
import net.luckybuyer.secondpager.WinnersSharingPager;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.StatusBarUtils;
import net.luckybuyer.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondPagerActivity extends FragmentActivity {

    public RelativeLayout rl_secondpager_header;
    //    private TextView tv_second_share;
    private TextView tv_second_back;
    private List<Fragment> list;
    public int batch_id;
    public int game_id;
    public List allList = new ArrayList();
    public int position;
    public ShippingAddressBean.ShippingBean shippingBean;

    //需要去哪
    public String from;

    public Lock lock;

    public CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        new StatusBarUtils(this).statusBar();
        setContentView(R.layout.activity_second_pager);

        //auth0登陆
        Auth0 auth0 = new Auth0("HmF3R6dz0qbzGQoYtTuorgSmzgu6Aua1", "staging-luckybuyer.auth0.com");
        this.lock = Lock.newBuilder(auth0, callback)
                // Add parameters to the Lock Builder
                .build();
        this.lock.onCreate(this);


        batch_id = getIntent().getIntExtra("batch_id", -1);
        game_id = getIntent().getIntExtra("game_id", -1);
        position = getIntent().getIntExtra("position", -1);
        allList = (ArrayList) getIntent().getSerializableExtra("alllist");
        setData();
        //发现视图  设置监听
        findView();
        setHeadMargin();
        selectPager();
    }

    //设置数据
    private void setData() {
        list = new ArrayList<>();
        list.add(new ProductDetailPager());

        //商品详情介绍页面                 1
        list.add(new ProductInformationPager());
        //晒单分享界面                     2
        list.add(new WinnersSharingPager());
        //往期揭晓界面                     3
        list.add(new PreviousWinnersPager());
        //设置界面                         4
        list.add(new SetPager());
        //金币详情页面                     5
        list.add(new CoinDetailPager());
        //购买金币页面                     6
        list.add(new BuyCoinPager());
        //物流分发界面                     7
        list.add(new DispatchPager());
        //增加地址页面                     8
        list.add(new AddAddressPager());
        //选择地址页面                     9
        list.add(new ShippingAddressPager());
    }

    //发现视图  设置监听
    private void findView() {
        rl_secondpager_header = (RelativeLayout) findViewById(R.id.rl_secondpager_header);
//        tv_second_share = (TextView) findViewById(R.id.tv_second_share);
        tv_second_back = (TextView) findViewById(R.id.tv_second_back);

        tv_second_back.setOnClickListener(new MyOnClickListener());
//        tv_second_share.setOnClickListener(new MyOnClickListener());
    }


    //选择哪个界面
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_secondpager, fragment);
        fragmentTransaction.commit();
    }

    private void selectPager() {
        from = getIntent().getStringExtra("from");
        if ("productdetail".equals(from)) {
            switchPage(0);
        } else if ("setpager".equals(from)) {
            switchPage(4);
        } else if ("coindetailpager".equals(from)) {
            switchPage(5);
        }else if ("dispatchpager".equals(from)) {
            switchPage(7);
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_second_back:
                    finish();
                    break;
//                case R.id.tv_second_share:
//                    Utils.MyToast(SecondPagerActivity.this, "SHARE");
//                    break;
            }
        }
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

            Utils.setSpData("token", token, SecondPagerActivity.this);
            Utils.setSpData("token_num", tokenBean.getExp() + "", SecondPagerActivity.this);
            String url = MyApplication.url + "/v1/users/me/?timezone=utc";
            Map map = new HashMap<String, String>();
            map.put("Authorization", "Bearer " + token);
            //请求登陆接口
            HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    Utils.setSpData("id", user.getId() + "", SecondPagerActivity.this);
                    Utils.setSpData("user_id", user.getAuth0_user_id(), SecondPagerActivity.this);
                    Utils.setSpData("balance", user.getBalance() + "", SecondPagerActivity.this);
                    Utils.setSpData("name", user.getProfile().getName(), SecondPagerActivity.this);
                    Utils.setSpData("picture", user.getProfile().getPicture(), SecondPagerActivity.this);
                    Utils.setSpData("social_link", user.getProfile().getSocial_link(), SecondPagerActivity.this);
                }

                @Override
                public void error(int requestCode, String message) {
                    Log.e("TAG", requestCode + "");
                    Log.e("TAG", message);
                    Utils.setSpData("token", null, SecondPagerActivity.this);
                    Utils.setSpData("token_num", null,SecondPagerActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.MyToast(SecondPagerActivity.this, "Login failed, please login again");
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Utils.setSpData("token", null,SecondPagerActivity.this);
                    Utils.setSpData("token_num", null,SecondPagerActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.MyToast(SecondPagerActivity.this, "Login failed, please login again");
                        }
                    });
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

    //根据版本判断是否 需要设置据顶部状态栏高度
    @TargetApi(19)
    private void setHeadMargin() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();

        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = sbar;
        rl_secondpager_header.setLayoutParams(lp);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        this.lock.onDestroy(this);
        this.lock = null;
        if (countDownTimer != null) {
            countDownTimer.onFinish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("productdetail".equals(from)) {
                switchPage(0);
                from = "";
                return false;
            } else if ("coindetailpager".equals(from)) {
                switchPage(5);
                from = "";
                return false;
            }else if ("dispatchpager".equals(from)) {
                switchPage(7);
                from = "";
                return false;
            }
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }
}
