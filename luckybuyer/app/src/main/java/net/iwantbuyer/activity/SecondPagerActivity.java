package net.iwantbuyer.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.facebook.FacebookAuthHandler;
import com.auth0.android.facebook.FacebookAuthProvider;
import com.auth0.android.google.GoogleAuthHandler;
import com.auth0.android.google.GoogleAuthProvider;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import net.iwantbuyer.R;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.ShippingAddressBean;
import net.iwantbuyer.bean.TokenBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.secondpager.AddAddressPager;
import net.iwantbuyer.secondpager.BuyCoinPager;
import net.iwantbuyer.secondpager.CoinDetailPager;
import net.iwantbuyer.secondpager.DispatchPager;
import net.iwantbuyer.secondpager.EditShowPager;
import net.iwantbuyer.secondpager.HelpPager;
import net.iwantbuyer.secondpager.PreviousWinnersPager;
import net.iwantbuyer.secondpager.ProductDetailPager;
import net.iwantbuyer.secondpager.ProductInformationPager;
import net.iwantbuyer.secondpager.SetPager;
import net.iwantbuyer.secondpager.ShippingAddressPager;
import net.iwantbuyer.secondpager.WinnersSharingPager;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.util.Purchase;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.MyBase64;
import net.iwantbuyer.utils.Utils;

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

    public int order_id;                 //进入addaddresspager页面   标记表明是增加地址还是修改

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_second_pager);


        MyApplication.url = Utils.getSpData("service", this);
        MyApplication.client_id = Utils.getSpData("client_id", this);
        MyApplication.domain = Utils.getSpData("domain", this);

        //auth0登陆
//        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id_text), getString(R.string.auth0_domain_text));
        Auth0 auth0 = new Auth0(MyApplication.client_id,MyApplication.domain);

        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);
        FacebookAuthProvider provider = new FacebookAuthProvider(client);
//        provider.setPermissions(Arrays.asList("public_profile", "user_photos"));
        FacebookAuthHandler handler = new FacebookAuthHandler(provider);

        GoogleAuthProvider provide = new GoogleAuthProvider("945656636977-9ag21vtt9k6do5f6nlptn0u96q10v2lh.apps.googleusercontent.com", client);
//        provide.setScopes(new Scope(DriveScopes.DRIVE_METADATA_READONLY));
//        provide.setRequiredPermissions(new String[]{"android.permission.GET_ACCOUNTS"});

        GoogleAuthHandler handle = new GoogleAuthHandler(provide);


        lock = Lock.newBuilder(auth0, call)
                .withAuthHandlers(handler,handle)
                .closable(true)
//                .withTheme(Theme.newBuilder().withDarkPrimaryColor(R.color.text_black).withHeaderColor(R.color.auth0_header).withHeaderLogo(R.mipmap.ic_launcher).withHeaderTitle(R.string.app_name).withHeaderTitleColor(R.color.text_black).withPrimaryColor(R.color.bg_ff4f3c).build())
                .withAuthButtonSize(AuthButtonSize.BIG)
//                // Add parameters to the Lock Builder
                .useBrowser(false)
                .build(this);

        batch_id = getIntent().getIntExtra("batch_id", -1);
        game_id = getIntent().getIntExtra("game_id", -1);
        address_id = getIntent().getIntExtra("address_id", -1);

        dispatch_game_id = getIntent().getIntExtra("dispatch_game_id", -1);

        setData();
        //发现视图  设置监听
        findView();
//        setHeadMargin();
        selectPager();

        if (Utils.checkDeviceHasNavigationBar(SecondPagerActivity.this)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getNavigationBarHeight(SecondPagerActivity.this));
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
//        list.add(new ShowPager());
        //编辑show页面                    10
        list.add(new EditShowPager());
        //Help界面                        11
        list.add(new HelpPager());
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
    public FragmentManager fragmentManager = getSupportFragmentManager();
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_secondpager, fragment);
        if(checkedId != 4 && checkedId != 0  && checkedId != 11 && checkedId != 7 && checkedId != 5) {
            fragmentTransaction.addToBackStack(null);
        }

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
        } else if ("dispatchpager".equals(from)) {
            switchPage(7);
        } else if ("participation".equals(from)) {
            switchPage(10);
        } else if ("helppager".equals(from)) {
            switchPage(11);
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

                case R.id.iv_home_use:
                    if (showUse != null && showUse.isShowing()) {
                        showUse.dismiss();
                    }
                    break;
                case R.id.iv_gift__use_close:
                    if (showUse != null && showUse.isShowing()) {
                        showUse.dismiss();
                    }
                    break;
            }
        }
    }

    //auth0登陆回调
    private LockCallback call = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {

            //友盟登陆通缉
            LogChannel("Login_Success");

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(SecondPagerActivity.this, "LOGIN:logged_in success",eventValue);

            // Base64 解码：
            String token = credentials.getIdToken();
            Log.e("TAG_TOKEN_second", token);


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
            //赠送金币成功
            startGift();

            Login(token);

        }

        @Override
        public void onCanceled() {
            // Login Cancelled response
            //友盟登陆通缉
            LogChannel("Login_Canceled");

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(SecondPagerActivity.this, "LOGIN:logged_in cancel",eventValue);
        }

        @Override
        public void onError(LockException error) {
            //友盟登陆通缉
            LogChannel("Login_Error");

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(SecondPagerActivity.this, "LOGIN:logged_in failed",eventValue);

            Utils.MyToast(SecondPagerActivity.this,"Login failed");
        }
    };

    private void LogChannel(String string) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("channel",getString(R.string.channel));
        MobclickAgent.onEvent(this, string, map);
    }

    //Auth0登陆成功后  调用我们自己的api
    private void Login(String token) {
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);

        Log.e("TAG", "登陆成功");
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(String response) {
                SecondPagerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(SecondPagerActivity.this,"Log in successfully");

                    }
                });
                //埋点
                try {
                    JSONObject props = new JSONObject();
                    MyApplication.mixpanel.track("LOGIN:loggedin", props);
                } catch (Exception e) {
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
                Utils.setSpData("token_num", null, SecondPagerActivity.this);
                runOnUiThread(


                        new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(SecondPagerActivity.this, "Log in failed");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                Utils.setSpData("token", null, SecondPagerActivity.this);
                Utils.setSpData("token_num", null, SecondPagerActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(SecondPagerActivity.this, "Log in failed");
                    }
                });
            }
        });
    }

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

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ("productdetail".equals(from)) {
//                switchPage(0);
//                from = "";
//                return false;
//            } else if ("coindetailpager".equals(from)) {
//                switchPage(5);
//                from = "";
//                return false;
//            } else if ("dispatchpager".equals(from)) {
//                switchPage(7);
//                from = "";
//                return false;
//            } else if ("setpager".equals(from)) {
//                switchPage(4);
//                from = "";
//                return false;
//            } else if ("shippingaddress".equals(from)) {
//                switchPage(9);
//                from = "";
//                return false;
//            } else if ("buycoinpager".equals(from)) {
//                switchPage(6);
//                from = "";
//                return false;
//            }
//
//            finish();
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            //google play 支付
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");



//            googleMyService(purchaseData, dataSignature);
            buyCoinPager.googleMyService(purchaseData, dataSignature);
        }

        // Pass on the activity result to the helper for handling
        if (buyCoinPager != null && buyCoinPager.mHelper != null && !buyCoinPager.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.e("TAG", "onActivityResult handled by IABUtil.");
        }
    }

    // Callback for when a purchase is finished
    public IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d("TAG", "Purchase finished: " + result + ", purchase: " + purchase);
            Log.e("TAG_点击之后", result.toString());
            if (result.isFailure()) {
                return;
            }

            Log.e("TAG", result.getMessage());
            Log.e("TAG", result.toString());
            Log.e("TAG", purchase.getSku());
            Log.e("TAG", purchase.getOrderId().toString());
            Log.e("TAG", purchase.getSignature().toString());
            Log.e("TAG", purchase.getSignature().toString());
            //消耗产品
            try {
                if (buyCoinPager != null && buyCoinPager.mHelper != null) {
                    buyCoinPager.mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            Log.e("TAG_消耗", result.isSuccess() + "");
                        }
                    });
                }
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }

        }
    };


    private void startGift() {
        String url = MyApplication.url + "/v1/gifts/new_user/?timezone=" + MyApplication.utc;
        Log.e("TAG_gift..", url);
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", this);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                SecondPagerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartGift();
                    }
                });

            }

            @Override
            public void error(final int code, final String message) {
                SecondPagerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == 419) {
                            Log.e("TAG_gift...", "已经赠送过了");
                        }
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
            }
        });
    }

    AlertDialog showUse;
    private AlertDialog StartGift() {

        Log.e("TAG_gift..", "startGift");
        View inflate = View.inflate(this, R.layout.alertdialog_home_usegift, null);
        ImageView iv_home_use = (ImageView) inflate.findViewById(R.id.iv_home_use);
        ImageView iv_gift__use_close = (ImageView) inflate.findViewById(R.id.iv_gift__use_close);
        ImageView iv_home_coin = (ImageView) inflate.findViewById(R.id.iv_home_coin);
        iv_home_use.setOnClickListener(new MyOnClickListener());
        iv_gift__use_close.setOnClickListener(new MyOnClickListener());

        if ("2".equals(Utils.getSpData("gifts_new_user", this))) {
            iv_home_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.home_gift_two));
        } else if ("3".equals(Utils.getSpData("gifts_new_user", this))) {
            iv_home_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.home_gift_three));
        } else if ("4".equals(Utils.getSpData("gifts_new_user", this))) {
            iv_home_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.home_gift_four));
        } else if ("5".equals(Utils.getSpData("gifts_new_user", this))) {
            iv_home_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.home_gift_five));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        showUse = builder.show();
        showUse.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        showUse.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = showUse.getWindow();
        window.setGravity(Gravity.CENTER);
//        show.getWindow().setLayout(3 * screenWidth / 4, 1 * screenHeight / 2);
        showUse.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return showUse;
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
