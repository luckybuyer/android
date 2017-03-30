package net.iwantbuyer.activity;

import android.annotation.TargetApi;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.bean.FCMBean;
import net.iwantbuyer.bean.GiftHasGiven;
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

    private RelativeLayout rl_secondpager;
    //    private TextView tv_second_share;
    public List<Fragment> list;
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

        String notification = getIntent().getStringExtra("notification");
        if(notification != null && notification.equals("nihao")) {
            switchPage(11);
        }

        Log.e("TAG_FCM", getIntent().getExtras()+"");
        if(getIntent().getExtras() != null) {
            String event= getIntent().getExtras().getString("event");
            if(event != null && event.equals("user_won")) {                                //进入产品详情页面
                dispatch_game_id = Integer.parseInt(getIntent().getExtras().getString("order_id"));
                switchPage(7);
            }else if(event != null && event.equals("game_closed")) {                       //进入产品详情页面
                Log.e("TAG", getIntent().getStringExtra("game_id"));
                game_id = Integer.parseInt(getIntent().getExtras().getString("game_id"));
                switchPage(0);
            }else if(event != null && event.equals("delivery_shipping")) {                 //进入dispatch页面
                dispatch_game_id = Integer.parseInt(getIntent().getExtras().getString("order_id"));
                switchPage(7);
            }else if(event != null && event.equals("delivery_finished")) {                      //进入编辑晒单页面
                order_id = Integer.parseInt(getIntent().getExtras().getString("order_id"));
                switchPage(10);
            }else if(event != null && event.equals("payment_succeeded")) {                 //进入金币详情页面
                switchPage(5);
            }
        }
    }

    DispatchPager dispatchPager;
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
        dispatchPager = new DispatchPager();
        list.add(dispatchPager);
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
        rl_secondpager = (RelativeLayout) findViewById(R.id.rl_secondpager);
//        tv_second_share = (TextView) findViewById(R.id.tv_second_share);
//        tv_second_share.setOnClickListener(new MyOnClickListener());
    }

    //选择哪个界面
    public FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment;
    public void switchPage(int checkedId) {
        fragment = list.get(checkedId);
        Log.e("TAG_isadd", fragment.isAdded() + "");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_secondpager, fragment);
        if(checkedId != 4 && checkedId != 0  && checkedId != 11 && checkedId != 7 && checkedId != 5 && checkedId != 10) {
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
        } else if("buycoins".equals(from)) {
            switchPage(6);
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
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
                case R.id.iv_winning_close:
                    if (winningShow != null && winningShow.isShowing()) {
                        winningShow.dismiss();
                    }
                    break;
                case R.id.iv_winning_go:
                    if (winningShow != null && winningShow.isShowing()) {
                        winningShow.dismiss();
                    }
                    if(fragment.getClass().getSimpleName() != null && !fragment.getClass().getSimpleName().equals(new DispatchPager().getClass().getSimpleName())) {
                        switchPage(7);
                    }
                    break;

                case R.id.tv_cheat_devicehas:
                    if(deviceHasGift != null && deviceHasGift.isShowing()) {
                        deviceHasGift.dismiss();
                    }
                    break;
                case R.id.iv_cheat_close:
                    if(deviceHasGift != null && deviceHasGift.isShowing()) {
                        deviceHasGift.dismiss();
                    }
                    break;
            }
        }
    }

    //auth0登陆回调
    private LockCallback call = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {

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

            Login(token);

            FCMregist(token);
        }

        @Override
        public void onCanceled() {
            // Login Cancelled response

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(SecondPagerActivity.this, "LOGIN:logged_in cancel",eventValue);
        }

        @Override
        public void onError(LockException error) {

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(SecondPagerActivity.this, "LOGIN:logged_in failed",eventValue);

            Utils.MyToast(SecondPagerActivity.this,"Login failed");
        }
    };

    //FCM注册
    private void FCMregist(String mToken) {
        String token = Utils.getSpData("refreshedToken",this);
        mToken = Utils.getSpData("token", this);
        if(mToken == null || "".equals(mToken) || token == null) {
            return;
        }
        Utils.setSpData("refreshedToken",token,this);
        String lang = Utils.getSpData("locale", this);
        if(lang != null && !lang.equals("")) {
            lang = lang.split("-")[0] + "";
        }
        String url = MyApplication.url + "/v1/fcm/registrations/?timezone=" + MyApplication.utc;
        FCMBean fcm = new FCMBean(lang,"android",token);
        String json = fcm.toString();

        Map map = new HashMap();
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
            }

            @Override
            public void error(final int code, final String message) {
                SecondPagerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_FCM", code + message);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
            }
        });
    }

    //Auth0登陆成功后  调用我们自己的api
    private void Login(String token) {
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(String response) {
                SecondPagerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(SecondPagerActivity.this,SecondPagerActivity.this.getString(R.string.loginsuccess));

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

                //判断是否  新手 送礼包
                Log.e("TAG_gift", user.isHas_given_new_user_gift() + "");
                if(!user.isHas_given_new_user_gift()) {
                    startGift();
                }

            }

            @Override
            public void error(final int requestCode, String message) {
                Log.e("TAG", requestCode + "");
                Log.e("TAG", message);
                Utils.setSpData("token", null, SecondPagerActivity.this);
                Utils.setSpData("token_num", null, SecondPagerActivity.this);
                runOnUiThread(


                        new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(SecondPagerActivity.this, SecondPagerActivity.this.getString(R.string.loginfailed) + requestCode + "me");
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
                        Utils.MyToast(SecondPagerActivity.this, SecondPagerActivity.this.getString(R.string.loginfailed));
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
            if ("buycoins".equals(from)) {
                finish();
            }

            //为了 解决华为手机 出现  Fragment already added IllegalStateException
            if(fragment == dispatchPager) {
                finish();
                return false;
            }
//            finish();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG——requestcode", requestCode + "" + resultCode);
        if (requestCode == 1001) {
            //google play 支付
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");



//            googleMyService(purchaseData, dataSignature);
            buyCoinPager.googleMyService(purchaseData, dataSignature);
        }

        // Pass on the activity result to the helper for handling
        if (requestCode != 770 && requestCode != 0 && buyCoinPager != null && buyCoinPager.mHelper != null && !buyCoinPager.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.e("TAG", "onActivityResult handled by IABUtil.");
        }

        //推送过来中奖 在secondpagerActivity页面
        if(resultCode == 6) {
            dispatch_game_id = data.getIntExtra("order_id",-1);
            if(fragment.getClass().getSimpleName() != null && !fragment.getClass().getSimpleName().equals(new DispatchPager().getClass().getSimpleName())) {
                switchPage(7);
            }
        }
    }

    // Callback for when a purchase is finished
    public IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                return;
            }
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
        Log.e("TAG_giftis", Utils.isEmulator() + "");
        if(Utils.isEmulator()) {
            //是虚拟机需要怎么处理
            return;
        }

//        TelephonyManager telephonemanager = (TelephonyManager) SecondPagerActivity.this
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = telephonemanager.getDeviceId();
        String token = Utils.getSpData("refreshedToken", this);
        String url = MyApplication.url + "/v1/gifts/new-user2/?timezone=" + MyApplication.utc;
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", this);
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        String json = "{\"device_id\": \""+token+"\"}";

        HttpUtils.getInstance().postJson(url, json,map, new HttpUtils.OnRequestListener() {
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
                        Log.e("TAG_gift", code + message);
                        if (code == 409) {
                            Gson gson = new Gson();
                            GiftHasGiven giftHasGiven = gson.fromJson(message, GiftHasGiven.class);
                            if("GiftGivenToUser".equals(giftHasGiven.getType())) {

                            }else if("GiftGivenToDevice".equals(giftHasGiven.getType())) {
                                startDeviceHasGift();
                            }
                        } else {
                            Utils.MyToast(SecondPagerActivity.this, SecondPagerActivity.this.getString(R.string.Networkfailure) + code + "gifts");
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

        if ("1".equals(Utils.getSpData("gifts_new_user", this))) {
            iv_home_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.home_gift_one));
        } else if ("2".equals(Utils.getSpData("gifts_new_user", this))) {
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

    AlertDialog deviceHasGift;
    private AlertDialog startDeviceHasGift(){
        View inflate = View.inflate(this, R.layout.alertdialog_home_cheat, null);
        TextView tv_cheat_devicehas = (TextView) inflate.findViewById(R.id.tv_cheat_devicehas);
        ImageView iv_cheat_close = (ImageView) inflate.findViewById(R.id.iv_cheat_close);
        tv_cheat_devicehas.setOnClickListener(new MyOnClickListener());
        iv_cheat_close.setOnClickListener(new MyOnClickListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        if (!this.isDestroyed()) {
            deviceHasGift = builder.show();
            deviceHasGift.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
            deviceHasGift.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = deviceHasGift.getWindow();
            window.setGravity(Gravity.CENTER);
//        show.getWindow().setLayout(3 * screenWidth / 4, 1 * screenHeight / 2);
            deviceHasGift.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return deviceHasGift;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //通过广播将firebase传递过来的消息传到ui县城
        IntentFilter filter = new IntentFilter();
        filter.addAction("Message");
        registerReceiver(MessageReceiver, filter);
        Log.e("TAG_onresume", "onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MessageReceiver);
    }

    AlertDialog winningShow;
    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            if(response != null) {
                Gson gson = new Gson();
                Log.e("TAG_fcc", response + "");
                DispatchGameBean dispatchGameBean = gson.fromJson(response, DispatchGameBean.class);
                dispatch_game_id = dispatchGameBean.getId();

                View view = View.inflate(SecondPagerActivity.this, R.layout.alertdialog_winning, null);
                ImageView iv_winning_close = (ImageView) view.findViewById(R.id.iv_winning_close);
                ImageView iv_winning_win = (ImageView) view.findViewById(R.id.iv_winning_win);
                final ImageView iv_winning_icon = (ImageView) view.findViewById(R.id.iv_winning_icon);
                TextView tv_winning_code = (TextView) view.findViewById(R.id.tv_winning_code);
                TextView tv_winning_discribe = (TextView) view.findViewById(R.id.tv_winning_discribe);
                ImageView iv_winning_go = (ImageView) view.findViewById(R.id.iv_winning_go);

                if(Utils.getSpData("locale",context) !=null && Utils.getSpData("locale",context).contains("zh")) {
                    iv_winning_win.setBackgroundResource(R.drawable.winning_zh);
                }else if(Utils.getSpData("locale",context) !=null && Utils.getSpData("locale",context).contains("ms")) {
                    iv_winning_win.setBackgroundResource(R.drawable.winning_ms);
                }else {
                    iv_winning_win.setBackgroundResource(R.drawable.winning_en);
                }

                Glide.with(context).load("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv_winning_icon.setImageBitmap(resource);
                    }
                });
                Log.e("TAG", dispatchGameBean.getGame().getIssue_id() + "");
                tv_winning_code.setText(dispatchGameBean.getGame().getIssue_id() + "");
                tv_winning_discribe.setText(dispatchGameBean.getGame().getProduct().getTitle() + "");
                iv_winning_close.setOnClickListener(new MyOnClickListener());
                iv_winning_go.setOnClickListener(new MyOnClickListener());

                winningShow = Utils.StartAlertDialog(SecondPagerActivity.this, view, Utils.getScreenWidth(context) * 3 / 4, Utils.getStatusHeight(context) * 3 / 4);
            }

        }
    };
}
