package net.smartbuyer.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.Theme;
import com.auth0.android.lock.enums.SocialButtonStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;

import net.smartbuyer.R;
import net.smartbuyer.app.MyApplication;
import net.smartbuyer.bean.ShippingAddressBean;
import net.smartbuyer.bean.TokenBean;
import net.smartbuyer.bean.User;
import net.smartbuyer.secondpager.AddAddressPager;
import net.smartbuyer.secondpager.BuyCoinPager;
import net.smartbuyer.secondpager.CoinDetailPager;
import net.smartbuyer.secondpager.DispatchPager;
import net.smartbuyer.secondpager.PreviousWinnersPager;
import net.smartbuyer.secondpager.ProductDetailPager;
import net.smartbuyer.secondpager.ProductInformationPager;
import net.smartbuyer.secondpager.SetPager;
import net.smartbuyer.secondpager.ShippingAddressPager;
import net.smartbuyer.secondpager.WinnersSharingPager;
import net.smartbuyer.util.IabHelper;
import net.smartbuyer.util.IabResult;
import net.smartbuyer.utils.HttpUtils;
import net.smartbuyer.utils.MyBase64;
import net.smartbuyer.utils.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondPagerActivity extends FragmentActivity {

    public RelativeLayout rl_secondpager_header;
    private RelativeLayout rl_secondpager;
    //    private TextView tv_second_share;
    private TextView tv_second_back;
    private List<Fragment> list;
    public int batch_id;
    public int game_id;

    public int dispatch_game_id;

    public int address_id;                 //进入addaddresspager页面   标记表明是增加地址还是修改

    //需要去哪
    public String from;

    public Lock lock;

    public ShippingAddressBean.ShippingBean shippingBean;              //为了分发页面

    public CountDownTimer countDownTimer;

    private BuyCoinPager buyCoinPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
//        new StatusBarUtils(this).statusBar();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_second_pager);

        //auth0登陆
        Auth0 auth0 = new Auth0("6frbTA5t3o1djsPYLp0jPiDGx7cvIyVc", "luckybuyer.auth0.com");
        this.lock = Lock.newBuilder(auth0, callback)
                .closable(true)
                .withTheme(Theme.newBuilder().withDarkPrimaryColor(R.color.text_black).withHeaderColor(R.color.bg_ff4f3c).withHeaderLogo(R.mipmap.ic_launcher).withHeaderTitle(R.string.app_name).withHeaderTitleColor(R.color.text_white).withPrimaryColor(R.color.bg_ff4f3c).build())
                .withSocialButtonStyle(SocialButtonStyle.BIG)
                // Add parameters to the Lock Builder
                .build();
        this.lock.onCreate(this);


        batch_id = getIntent().getIntExtra("batch_id", -1);
        game_id = getIntent().getIntExtra("game_id", -1);
        address_id = getIntent().getIntExtra("address_id",-1);

        dispatch_game_id = getIntent().getIntExtra("dispatch_game_id", -1);

        setData();
        //发现视图  设置监听
        findView();
        setHeadMargin();
        selectPager();

        if(Utils.checkDeviceHasNavigationBar(SecondPagerActivity.this)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getNavigationBarHeight(SecondPagerActivity.this));
            rl_secondpager.setLayoutParams(lp);
        }

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
        buyCoinPager = new BuyCoinPager();
        list.add(buyCoinPager);
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
        rl_secondpager = (RelativeLayout) findViewById(R.id.rl_secondpager);
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
//        fragmentTransaction.addToBackStack(null);
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
        }else if("participation".equals(from)) {
            switchPage(10);
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


//            byte[] mmmm = Base64.decode(token, Base64.URL_SAFE);
            byte[] mmmm = MyBase64.decode(token.getBytes());
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
            String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
            Map map = new HashMap<String, String>();
            map.put("Authorization", "Bearer " + token);

            Log.e("TAG", "登陆成功");
            //请求登陆接口
            HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(String response) {
                    //埋点
                    try {
                        JSONObject props = new JSONObject();
                        MyApplication.mixpanel.track("LOGIN:loggedin", props);
                    }catch (Exception e){
                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                    }

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
            } else if ("setpager".equals(from)) {
                switchPage(4);
                from = "";
                return false;
            } else if ("shippingaddress".equals(from)) {
                switchPage(9);
                from = "";
                return false;
            }
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            //google play 支付
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");


            Log.e("TAG_huidiao", purchaseData + "");
            Log.e("TAG_huidiao", dataSignature + "");

//            googleMyService(purchaseData, dataSignature);
            buyCoinPager.googleMyService(purchaseData,dataSignature);
        }

        // Pass on the activity result to the helper for handling
        if (buyCoinPager!= null &&buyCoinPager.mHelper != null&&!buyCoinPager.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.e("TAG", "onActivityResult handled by IABUtil.");
        }
    }
}
