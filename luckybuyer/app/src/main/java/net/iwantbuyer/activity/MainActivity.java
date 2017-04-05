package net.iwantbuyer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
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
import com.auth0.android.result.Delegation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.payssion.android.sdk.PayssionActivity;
import com.payssion.android.sdk.model.PayResponse;

import net.iwantbuyer.R;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.bean.FCMBean;
import net.iwantbuyer.bean.GiftHasGiven;
import net.iwantbuyer.bean.TokenBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.pager.HomePager;
import net.iwantbuyer.pager.MePager;
import net.iwantbuyer.pager.ShowPager;
import net.iwantbuyer.pager.WinningPager;
import net.iwantbuyer.secondpager.BuyCoinPager;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.util.Purchase;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.MyBase64;
import net.iwantbuyer.utils.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import com.inthecheesefactory.lib.fblike.widget.FBLikeView;

public class MainActivity extends FragmentActivity {

    private FrameLayout fl_main;
    public RadioGroup rg_main;
    private List<Fragment> list;
    public Lock lock;

    private RadioButton rb_main_homepager;
    private RadioButton rb_main_buycoins;
    private RadioButton rb_main_newresult;
    private RadioButton rb_main_show;
    private RadioButton rb_main_me;
    private RelativeLayout rl_main;


    private FragmentManager fragmentManager;
    private HomePager homePager;
    private BuyCoinPager buyCoinPager;
    private WinningPager winningPager;
    private ShowPager showPager;
    private MePager mePager;

    public int id;

    AuthenticationAPIClient client;
    Auth0 auth0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);

        MyApplication.url = Utils.getSpData("service", this);
        MyApplication.client_id = Utils.getSpData("client_id", this);
        MyApplication.domain = Utils.getSpData("domain", this);


        auth0 = new Auth0(MyApplication.client_id, MyApplication.domain);

        client = new AuthenticationAPIClient(auth0);
        FacebookAuthProvider provider = new FacebookAuthProvider(client);
//        provider.setPermissions(Arrays.asList("public_profile", "user_photos"));
        FacebookAuthHandler handler = new FacebookAuthHandler(provider);

        GoogleAuthProvider provide = new GoogleAuthProvider("945656636977-9ag21vtt9k6do5f6nlptn0u96q10v2lh.apps.googleusercontent.com", client);
//        provide.setScopes(new Scope(DriveScopes.DRIVE_METADATA_READONLY));
//        provide.setRequiredPermissions(new String[]{"android.permission.GET_ACCOUNTS"});

        GoogleAuthHandler handle = new GoogleAuthHandler(provide);


        lock = Lock.newBuilder(auth0, callback)
                .withAuthHandlers(handler, handle)
                .closable(true)
//                .withTheme(Theme.newBuilder().withDarkPrimaryColor(R.color.text_black).withHeaderColor(R.color.auth0_header).withHeaderLogo(R.mipmap.ic_launcher).withHeaderTitle(R.string.app_name).withHeaderTitleColor(R.color.text_black).withPrimaryColor(R.color.bg_ff4f3c).build())
                .withAuthButtonSize(AuthButtonSize.BIG)
//                // Add parameters to the Lock Builder
                .useBrowser(false)
                .build(this);


        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用

            //从fragmentManager里面找到fragment
            homePager = (HomePager) fragmentManager.findFragmentByTag(HomePager.class.getName());
            buyCoinPager = (BuyCoinPager) fragmentManager.findFragmentByTag(BuyCoinPager.class.getName());
            winningPager = (WinningPager) fragmentManager.findFragmentByTag(WinningPager.class.getName());
            showPager = (ShowPager) fragmentManager.findFragmentByTag(ShowPager.class.getName());
            mePager = (MePager) fragmentManager.findFragmentByTag(MePager.class.getName());

            //解决重叠问题show里面可以指定恢复的页面
            fragmentManager.beginTransaction()
                    .show(homePager)
                    .hide(buyCoinPager)
                    .hide(winningPager)
                    .hide(showPager)
                    .hide(mePager)
                    .commit();

            //把当前显示的fragment记录下来
            currentFragment = homePager;

        } else { //正常启动时调用

            homePager = new HomePager();
            buyCoinPager = new BuyCoinPager();
            winningPager = new WinningPager();
            showPager = new ShowPager();
            mePager = new MePager();

            showFragment(homePager);
        }

        //设置数据
        setData();
        //发现视图  设置监听
        findView();

        //注册送礼包弹窗
        if (!"0".equals(Utils.getSpData("gifts_new_user", this)) && Utils.getSpData("guide", this) == null) {
            StartAlertDialog();
        }

        //新手 引导页面
        if (Utils.getSpData("guide", this) == null) {
            Utils.setSpData("guide", "noguide", this);
        }

        rb_main_homepager.setChecked(true);
        if (Utils.checkDeviceHasNavigationBar(MainActivity.this)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getNavigationBarHeight(MainActivity.this));
            rl_main.setLayoutParams(lp);
        }

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            Log.e("TAG_000", token);
        }

        Utils.setSpData("main_pager", null, this);       //当editshow 返回时候的临时变量 一定要删除

        StartUpdateAlertDialog();

        refreshToken();                                  //Auth0  刷新token

    }

    //设置数据
    private void setData() {
        list = new ArrayList<>();
        list.add(homePager);
        list.add(buyCoinPager);
        list.add(winningPager);
        list.add(showPager);
        list.add(mePager);
    }

    //试图初始化 与设置监听
    private void findView() {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        rb_main_homepager = (RadioButton) findViewById(R.id.rb_main_homepager);
        rb_main_buycoins = (RadioButton) findViewById(R.id.rb_main_buycoins);
        rb_main_newresult = (RadioButton) findViewById(R.id.rb_main_newresult);
        rb_main_show = (RadioButton) findViewById(R.id.rb_main_show);
        rb_main_me = (RadioButton) findViewById(R.id.rb_main_me);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);

        rb_main_homepager.setOnClickListener(new MyOnclickListener());
        rb_main_buycoins.setOnClickListener(new MyOnclickListener());
        rb_main_newresult.setOnClickListener(new MyOnclickListener());
        rb_main_me.setOnClickListener(new MyOnclickListener());

        //设置监听
        rb_main_homepager.setOnClickListener(new MyOnclickListener());
        rb_main_buycoins.setOnClickListener(new MyOnclickListener());
        rb_main_newresult.setOnClickListener(new MyOnclickListener());
        rb_main_show.setOnClickListener(new MyOnclickListener());
        rb_main_me.setOnClickListener(new MyOnclickListener());

    }

    private int login_id = 0;

    class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_main_homepager:

                    id = 0;
                    showFragment(list.get(0));
                    rb_main_homepager.setChecked(true);
                    rb_main_buycoins.setChecked(false);
                    rb_main_newresult.setChecked(false);
                    rb_main_show.setChecked(false);
                    rb_main_me.setChecked(false);
                    setPoint("CLICK:homepage");

                    break;
                case R.id.rb_main_buycoins:
                    id = 1;
                    showFragment(list.get(1));
                    rb_main_homepager.setChecked(false);
                    rb_main_buycoins.setChecked(true);
                    rb_main_newresult.setChecked(false);
                    rb_main_show.setChecked(false);
                    rb_main_me.setChecked(false);
                    login_id = 1;
                    setPoint("CLICK:buy_coins");
                    break;
                case R.id.rb_main_newresult:
                    id = 2;
                    showFragment(list.get(2));
                    rb_main_homepager.setChecked(false);
                    rb_main_buycoins.setChecked(false);
                    rb_main_newresult.setChecked(true);
                    rb_main_show.setChecked(false);
                    rb_main_me.setChecked(false);
                    setPoint("CLICK:new_result");
                    break;
                case R.id.rb_main_show:
                    id = 3;
                    showFragment(list.get(3));
                    rb_main_homepager.setChecked(false);
                    rb_main_buycoins.setChecked(false);
                    rb_main_newresult.setChecked(false);
                    rb_main_show.setChecked(true);
                    rb_main_me.setChecked(false);
                    setPoint("CLICK:show");
                    break;
                case R.id.rb_main_me:
                    setPoint("CLICK:my_account");
                    String token_s = Utils.getSpData("token_num", MainActivity.this);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }

                    //判断是否登陆  未登陆  先登录  登陆 进入me页面
                    if (token > System.currentTimeMillis() / 1000) {
                        showFragment(list.get(4));
                        rb_main_homepager.setChecked(false);
                        rb_main_buycoins.setChecked(false);
                        rb_main_newresult.setChecked(false);
                        rb_main_me.setChecked(true);
                        id = 4;
                    } else {
                        MainActivity.this.startActivity(MainActivity.this.lock.newIntent(MainActivity.this));
                        login_id = 4;
                        //埋点
                        try {
                            JSONObject props = new JSONObject();
                            MyApplication.mixpanel.track("LOGIN:showpage", props);
                        } catch (Exception e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        //AppFlyer 埋点
                        Map<String, Object> eventValue = new HashMap<String, Object>();
                        AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "Page：Login", eventValue);
                    }
                    break;
                case R.id.iv_home_get:
                    MainActivity.this.startActivity(MainActivity.this.lock.newIntent(MainActivity.this));
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }

                    //AppFlyer 埋点
                    Map<String, Object> eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "Click：gift_GET IT NOW", eventValue);

                    break;
                case R.id.iv_gift_close:
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    //AppFlyer 埋点
                    eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "Click：gift_closed", eventValue);
                    break;
                case R.id.iv_home_use:
                    if (currentFragment == homePager) {
                        if (showUse != null && showUse.isShowing()) {
                            showUse.dismiss();
                        }

                        rb_main_homepager.setChecked(true);
                        rb_main_buycoins.setChecked(false);
                        rb_main_newresult.setChecked(false);
                        rb_main_show.setChecked(false);
                        rb_main_me.setChecked(false);

                        id = 0;

                    } else {
                        showFragment(homePager);

                        rb_main_homepager.setChecked(true);
                        rb_main_buycoins.setChecked(false);
                        rb_main_newresult.setChecked(false);
                        rb_main_show.setChecked(false);
                        rb_main_me.setChecked(false);

                        id = 0;
                        if (showUse != null && showUse.isShowing()) {
                            showUse.dismiss();
                        }
                    }

                    break;
                case R.id.iv_gift__use_close:
                    if (showUse != null && showUse.isShowing()) {
                        showUse.dismiss();

                    }
                    break;
                case R.id.tv_forceupdate_ok:
                    String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    break;
                case R.id.tv_uddate_cancel:
                    if (updateShow != null && updateShow.isShowing()) {
                        updateShow.dismiss();
                    }
                    break;
                case R.id.tv_update_ok:
                    appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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

                    Intent intent = new Intent(MainActivity.this, SecondPagerActivity.class);
                    intent.putExtra("from", "dispatchpager");
                    intent.putExtra("dispatch_game_id", order_id);
                    startActivity(intent);
                    break;

                case R.id.tv_cheat_devicehas:
                    if (deviceHasGift != null && deviceHasGift.isShowing()) {
                        deviceHasGift.dismiss();
                    }
                    break;
                case R.id.iv_cheat_close:
                    if (deviceHasGift != null && deviceHasGift.isShowing()) {
                        deviceHasGift.dismiss();
                    }
                    break;
            }
        }
    }


    private void setPoint(String page) {
        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track(page, props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    private Fragment currentFragment;
    private boolean flag = true;

    private void showFragment(Fragment fg) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (flag) {
            transaction.add(R.id.fl_main, fg);
            flag = false;
        } else {
            //如果之前没有添加过
            if (!fg.isAdded()) {
                transaction
                        .hide(currentFragment)
                        .add(R.id.fl_main, fg, fg.getClass().getName());
            } else {
                if (fg == homePager) {
                    homePager.initData();
                    if (buyCoinPager != null && buyCoinPager.show != null && buyCoinPager.show.isShowing()) {
                        buyCoinPager.show.dismiss();
                    }
                    if (homePager.tv_home_country != null) {
                        homePager.tv_home_country.setText(Utils.getSpData("country", this));
                    }
                } else if (fg == mePager) {
//                    mePager.initData();
                    if (buyCoinPager != null && buyCoinPager.show != null && buyCoinPager.show.isShowing()) {
                        buyCoinPager.show.dismiss();
                    }
                    if (mePager != null) {
                        mePager.setView();
                    }
                } else if (fg == buyCoinPager) {

                } else if (fg == winningPager) {
                    if (buyCoinPager != null && buyCoinPager.show != null && buyCoinPager.show.isShowing()) {
                        buyCoinPager.show.dismiss();
                    }
                }
                transaction.hide(currentFragment).show(fg);
                if (mePager != null && mePager.vp_me != null) {
                    mePager.setView();
                    mePager.initData();
                }
            }
        }

        //全局变量，记录当前显示的fragment
        currentFragment = fg;
        boolean flag = this.isDestroyed();
        if (!flag) {
            transaction.commitAllowingStateLoss();
        }

    }


    //auth0登陆回调
    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            // Base64 解码：idtoken
            String token = credentials.getIdToken();
            Utils.setSpData("idtoken",token,MainActivity.this);
//            byte[] mmmm = Base64.decode(token.getBytes(), Base64.URL_SAFE);
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

            Utils.setSpData("token", token, MainActivity.this);
            Utils.setSpData("token_num", tokenBean.getExp() + "", MainActivity.this);

            //赠送金币成功
//            startGift();

            //FCM注册
            FCMregist(token);

            Login(token);

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "LOGIN:logged_in success", eventValue);


        }

        @Override
        public void onCanceled() {
            if (Utils.isInLauncher(MainActivity.this)) {
                return;
            }

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "LOGIN:logged_in cancel", eventValue);
            selectPager();
        }

        @Override
        public void onError(LockException error) {
            Log.e("TAG_error", error.getMessage());
            if (Utils.isInLauncher(MainActivity.this)) {
                return;
            }

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "LOGIN:logged_in failed", eventValue);
            selectPager();


            Utils.MyToast(MainActivity.this, MainActivity.this.getString(R.string.loginfailed));
            selectPager();
        }
    };

    //auth0刷新token
    private void refreshToken() {
        String idToken = Utils.getSpData("idtoken",MainActivity.this);
        if(idToken == null || client ==null) {
            return;
        }
        client.delegationWithIdToken(idToken)
//                .setScope("openid email")
                .start(new BaseCallback<Delegation, AuthenticationException>() {
                    @Override
                    public void onSuccess(Delegation delegation) {
                        //SUCCESS
                        String token = delegation.getIdToken();
                        Utils.setSpData("idtoken",token,MainActivity.this);
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

                        Utils.setSpData("token", token, MainActivity.this);
                        Utils.setSpData("token_num", tokenBean.getExp() + "", MainActivity.this);
                        Log.e("TAG_刷新token", token + "");
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        //FAILURE
                        Log.e("TAG_刷新token", error.toString() + "");
                    }
                });


    }

    //FCM注册
    private void FCMregist(String mToken) {
        String token = Utils.getSpData("refreshedToken", this);
        Log.e("TAG", token + "--------" + mToken);
        if (mToken == null || "".equals(mToken) || token == null) {
            return;
        }
        String lang = Utils.getSpData("locale", this);
        if (lang != null && !lang.equals("")) {
            lang = lang.split("-")[0] + "";
        }
        String url = MyApplication.url + "/v1/fcm/registrations/?timezone=" + MyApplication.utc;

        FCMBean fcm = new FCMBean(lang, "android", token);
        String json = fcm.toString();
        Log.e("TAG_FCM", url + json);
        Map map = new HashMap();
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
            }

            @Override
            public void error(final int code, final String message) {
                MainActivity.this.runOnUiThread(new Runnable() {
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


    //auth0登陆成功后  登陆我们自己的api
    private void Login(String token) {
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(String response) {
                //埋点
                try {
                    JSONObject props = new JSONObject();
                    MyApplication.mixpanel.track("LOGIN:loggedin", props);
                } catch (Exception e) {
                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                }
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                Utils.setSpData("id", user.getId() + "", MainActivity.this);
                Utils.setSpData("user_id", user.getAuth0_user_id(), MainActivity.this);
                Utils.setSpData("balance", user.getBalance() + "", MainActivity.this);
                Utils.setSpData("name", user.getProfile().getName(), MainActivity.this);
                Utils.setSpData("picture", user.getProfile().getPicture(), MainActivity.this);
                Log.e("TAG", user.getProfile().getPicture() + "" + user.getProfile().getName());
                Utils.setSpData("social_link", user.getProfile().getSocial_link(), MainActivity.this);

                //登陆成功重新请求 banner  为了隐藏首充送礼
//                homePager.responseBanner();

                goPager(user);

            }

            @Override
            public void error(final int requestCode, String message) {
                Utils.setSpData("token", null, MainActivity.this);
                Utils.setSpData("token_num", null, MainActivity.this);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(MainActivity.this, MainActivity.this.getString(R.string.loginfailed) + requestCode + "user");
                        selectPager();
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                Utils.setSpData("token", null, MainActivity.this);
                Utils.setSpData("token_num", null, MainActivity.this);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(MainActivity.this, MainActivity.this.getString(R.string.loginfailed));
                        selectPager();
                    }
                });
            }
        });
    }

    //请求我们自己的me 接口后  所进行的操作
    private void goPager(final User user) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG_gift", user.isHas_given_new_user_gift() + "");
                if (!user.isHas_given_new_user_gift()) {
                    startGift();
                }

                Utils.MyToast(MainActivity.this, MainActivity.this.getString(R.string.loginsuccess));
                //登陆成功  直接进入me页面
                showFragment(list.get(login_id));
                if (login_id == 1) {
                    rb_main_buycoins.setChecked(true);
                    rb_main_me.setChecked(false);
                    rb_main_homepager.setChecked(false);
                    rb_main_newresult.setChecked(false);
                } else if (login_id == 4) {
                    rb_main_buycoins.setChecked(false);
                    rb_main_me.setChecked(true);
                    rb_main_homepager.setChecked(false);
                    rb_main_newresult.setChecked(false);
                    if (mePager != null && mePager.mePagerAllAdapter != null) {
                        mePager.mePagerAllAdapter.list.clear();
                        mePager.mePagerAllAdapter.notifyDataSetChanged();
                    }
                    if (mePager != null && mePager.mePagerLuckyAdapter != null) {
                        mePager.mePagerLuckyAdapter.list.clear();
                        mePager.mePagerLuckyAdapter.notifyDataSetChanged();
                    }

                } else {
                    rb_main_homepager.setChecked(true);
                    rb_main_buycoins.setChecked(false);
                    rb_main_me.setChecked(false);
                    rb_main_show.setChecked(false);
                    rb_main_newresult.setChecked(false);

                }
//                        rb_main_homepager.setChecked(false);
//                        rb_main_newresult.setChecked(false);

            }
        });
    }

    private void selectPager() {
        if (id == 0) {
            showFragment(list.get(0));
            rb_main_homepager.setChecked(true);
            rb_main_buycoins.setChecked(false);
            rb_main_newresult.setChecked(false);
            rb_main_show.setChecked(false);
            rb_main_me.setChecked(false);
        } else if (id == 1) {
            showFragment(list.get(1));
            rb_main_homepager.setChecked(false);
            rb_main_buycoins.setChecked(true);
            rb_main_newresult.setChecked(false);
            rb_main_show.setChecked(false);
            rb_main_me.setChecked(false);
        } else if (id == 2) {
            showFragment(list.get(2));
            rb_main_homepager.setChecked(false);
            rb_main_buycoins.setChecked(false);
            rb_main_newresult.setChecked(true);
            rb_main_show.setChecked(false);
            rb_main_me.setChecked(false);
        } else if (id == 3) {
            showFragment(list.get(3));
            rb_main_homepager.setChecked(false);
            rb_main_buycoins.setChecked(false);
            rb_main_newresult.setChecked(false);
            rb_main_show.setChecked(true);
            rb_main_me.setChecked(false);
        } else if (id == 4) {
            showFragment(list.get(0));
            rb_main_homepager.setChecked(true);
            rb_main_buycoins.setChecked(false);
            rb_main_newresult.setChecked(false);
            rb_main_show.setChecked(false);
            rb_main_me.setChecked(false);
        }
    }


    long mExitTime;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (show != null && show.isShowing()) {
                //AppFlyer 埋点
                Map eventValue = new HashMap<String, Object>();
                AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "Click：gift_closed", eventValue);
            }
            if (currentFragment == buyCoinPager && buyCoinPager != null && buyCoinPager.rl_buycoins_mol.getVisibility() == View.VISIBLE) {
                buyCoinPager.rl_buycoins_mol.setVisibility(View.GONE);
                this.rg_main.setVisibility(View.VISIBLE);
            } else if ((System.currentTimeMillis() - mExitTime) > 3000) {
                Toast.makeText(this, MainActivity.this.getString(R.string.Clickexit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //多语言切换
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        Log.e("TAG_locale", Utils.getSpData("locale", MainActivity.this) + "");
        String spString = Utils.getSpData("locale", MainActivity.this);
        Log.e("TAG_spString", spString + "");
        if (spString != null) {
            config.locale = new Locale(spString.split("-")[0], spString.split("-")[1]);
            Locale.setDefault(config.locale);
            resources.updateConfiguration(config, dm);
        }

        if ("show".equals(Utils.getSpData("main_pager", this) + "")) {
            Utils.setSpData("main_pager", null, this);       //当editshow 返回时候的临时变量 一定要删除
            id = 3;
            showFragment(showPager);
            showPager.initData();
            rb_main_homepager.setChecked(false);
            rb_main_buycoins.setChecked(false);
            rb_main_newresult.setChecked(false);
            rb_main_show.setChecked(true);
            rb_main_me.setChecked(false);
        } else {
            showFragment(currentFragment);
        }

//        AppEventsLogger.activateApp(this);                 //facebook统计

        //通过广播将firebase传递过来的消息传到ui县城
        IntentFilter filter = new IntentFilter();
        filter.addAction("Message");
        registerReceiver(MessageReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AppEventsLogger.deactivateApp(this);
        unregisterReceiver(MessageReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG_payssion", resultCode + "");
        if (requestCode == PayssionActivity.RESULT_OK) {
            if (null != data) {
                PayResponse response = (PayResponse) data.getSerializableExtra(PayssionActivity.RESULT_DATA);
                Log.e("TAG_pay", response.toString() + "");
            }
        }
        if (resultCode == 0 && data != null && "homepager".equals(data.getStringExtra("go"))) {
            rg_main.check(0);
//            switchPage(0);
            showFragment(list.get(0));
            rb_main_homepager.setChecked(true);
            rb_main_buycoins.setChecked(false);
            rb_main_newresult.setChecked(false);
            rb_main_me.setChecked(false);

        } else if (requestCode == 1001) {
            //google play 支付
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");


            Log.e("TAG_huidiao", purchaseData + "");
            Log.e("TAG_huidiao", dataSignature + "");
            buyCoinPager.googleMyService(purchaseData, dataSignature);
//            googleMyService(purchaseData, dataSignature);

        } else {
//            FBLikeView.onActivityResult(requestCode, resultCode, data);
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

        if (resultCode == 6) {
            Intent intent = new Intent(MainActivity.this, SecondPagerActivity.class);
            intent.putExtra("from", "dispatchpager");
            intent.putExtra("dispatch_game_id", data.getIntExtra("order_id", -1));
            startActivity(intent);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (buyCoinPager != null && buyCoinPager.mHelper != null) {
            try {
                buyCoinPager.mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        buyCoinPager.mHelper = null;
    }

    AlertDialog show;

    private AlertDialog StartAlertDialog() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = View.inflate(this, R.layout.alertdialog_home_gift, null);
        ImageView iv_home_get = (ImageView) inflate.findViewById(R.id.iv_home_get);
        ImageView iv_gift_close = (ImageView) inflate.findViewById(R.id.iv_gift_close);
        ImageView iv_home_gift = (ImageView) inflate.findViewById(R.id.iv_home_gift);
        String spS = Utils.getSpData("locale", this);
        if (spS != null && "ms".equals(spS.split("-")[0] + "")) {
            iv_home_gift.setBackgroundResource(R.drawable.home_gift_get_my);
        } else if (spS != null && "zh".equals(spS.split("-")[0] + "")) {
            iv_home_gift.setBackgroundResource(R.drawable.home_gift_get_zh);
        } else if (spS != null && "en".equals(spS.split("-")[0] + "")) {
            iv_home_gift.setBackgroundResource(R.drawable.home_gift_get_en);
        }
        iv_home_get.setOnClickListener(new MyOnclickListener());
        iv_gift_close.setOnClickListener(new MyOnclickListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setView(inflate);
        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        Window window = show.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
//        show.getWindow().setLayout(3 * screenWidth / 4, 1 * screenHeight / 2);
        return show;
    }

    AlertDialog showUse;

    private AlertDialog StartGift() {

        View inflate = View.inflate(this, R.layout.alertdialog_home_usegift, null);
        ImageView iv_home_use = (ImageView) inflate.findViewById(R.id.iv_home_use);
        ImageView iv_gift__use_close = (ImageView) inflate.findViewById(R.id.iv_gift__use_close);
        ImageView iv_home_coin = (ImageView) inflate.findViewById(R.id.iv_home_coin);
        ImageView iv_home_usegift = (ImageView) inflate.findViewById(R.id.iv_home_usegift);
        String spS = Utils.getSpData("locale", this);
        if (spS != null && "ms".equals(spS.split("-")[0] + "")) {
            iv_home_usegift.setBackgroundResource(R.drawable.home_gift_coins_my);
        } else if (spS != null && "zh".equals(spS.split("-")[0] + "")) {
            iv_home_usegift.setBackgroundResource(R.drawable.home_gift_coins_zh);
        } else if (spS != null && "en".equals(spS.split("-")[0] + "")) {
            iv_home_usegift.setBackgroundResource(R.drawable.home_gift_coins_en);
        }

        iv_home_use.setOnClickListener(new MyOnclickListener());
        iv_gift__use_close.setOnClickListener(new MyOnclickListener());

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
        if (!this.isDestroyed()) {
            showUse = builder.show();
            showUse.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
            showUse.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = showUse.getWindow();
            window.setGravity(Gravity.CENTER);
//        show.getWindow().setLayout(3 * screenWidth / 4, 1 * screenHeight / 2);
            showUse.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return showUse;
    }

    AlertDialog deviceHasGift;

    private AlertDialog startDeviceHasGift() {

        View inflate = View.inflate(this, R.layout.alertdialog_home_cheat, null);
        TextView tv_cheat_devicehas = (TextView) inflate.findViewById(R.id.tv_cheat_devicehas);
        ImageView iv_cheat_close = (ImageView) inflate.findViewById(R.id.iv_cheat_close);
        tv_cheat_devicehas.setOnClickListener(new MyOnclickListener());
        iv_cheat_close.setOnClickListener(new MyOnclickListener());

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


    private void startGift() {
        Log.e("TAG_giftis", Utils.isEmulator() + "");
        if (Utils.isEmulator()) {
            //是虚拟机需要怎么处理
            return;
        }


        String token = Utils.getSpData("refreshedToken", this);
        String url = MyApplication.url + "/v1/gifts/new-user2/?timezone=" + MyApplication.utc;
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", this);
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(this) + "");
        String json = "{\"device_id\": \"" + token + "\"}";
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Utils.isInLauncher(MainActivity.this)) {
                            StartGift();
                        }
                    }
                });

            }

            @Override
            public void error(final int code, final String message) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_gift", code + message);
                        if (code == 409) {
                            Gson gson = new Gson();
                            GiftHasGiven giftHasGiven = gson.fromJson(message, GiftHasGiven.class);
                            if ("GiftGivenToUser".equals(giftHasGiven.getType())) {

                            } else if ("GiftGivenToDevice".equals(giftHasGiven.getType())) {
                                if (!Utils.isInLauncher(MainActivity.this)) {
                                    startDeviceHasGift();
                                }
                            }
                        } else {
                            Utils.MyToast(MainActivity.this, MainActivity.this.getString(R.string.Networkfailure) + code + "gifts");
                        }

                    }
                });
            }

            @Override
            public void failure(Exception exception) {
            }
        });
    }

    AlertDialog updateShow;

    private void StartUpdateAlertDialog() {
        String pkName = this.getPackageName();
        int versionCode = 0;
        try {
            versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = null;

        Log.e("TAG_版本", Utils.getSpData("latest_version", this) + Utils.getSpData("minimum_version", this));
        boolean flag = false;
        if (Utils.getSpData("latest_version", this) != null && versionCode >= Integer.parseInt(Utils.getSpData("latest_version", this))) {
            return;
        } else if (Utils.getSpData("minimum_version", this) != null && versionCode < Integer.parseInt(Utils.getSpData("minimum_version", this))) {
            inflate = View.inflate(this, R.layout.alertdialog_main_forceupdate, null);
            TextView tv_forceupdate_ok = (TextView) inflate.findViewById(R.id.tv_forceupdate_ok);
            tv_forceupdate_ok.setOnClickListener(new MyOnclickListener());
            flag = true;
        } else if (Utils.getSpData("latest_version", this) != null && versionCode < Integer.parseInt(Utils.getSpData("latest_version", this))) {
            inflate = View.inflate(this, R.layout.alertdialog_main_update, null);
            TextView tv_uddate_cancel = (TextView) inflate.findViewById(R.id.tv_uddate_cancel);
            TextView tv_update_ok = (TextView) inflate.findViewById(R.id.tv_update_ok);
            tv_uddate_cancel.setOnClickListener(new MyOnclickListener());
            tv_update_ok.setOnClickListener(new MyOnclickListener());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflate);
        updateShow = builder.show();
        if (flag) {
            updateShow.setCancelable(false);               //点击外部和返回按钮都不消失
        } else {
            updateShow.setCanceledOnTouchOutside(false);   //点击外部不消失
        }
        updateShow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = updateShow.getWindow();
        window.setGravity(Gravity.CENTER);
        updateShow.getWindow().setLayout(3 * screenWidth / 4, 3 * screenHeight / 10);
        updateShow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    //    public void getsp() {
//        try {
//            Log.e("TAGhehe", "getsp");
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "net.iwantbuyer",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                Log.e("TAGhehe", "sign");
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("TAGhehe:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e("TAG000", e.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("TAG000", e.toString());
//        }
//    }
    AlertDialog winningShow;
    int order_id = 0;
    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();
            DispatchGameBean dispatchGameBean = gson.fromJson(response, DispatchGameBean.class);
            order_id = dispatchGameBean.getId();

            View view = View.inflate(MainActivity.this, R.layout.alertdialog_winning, null);
            ImageView iv_winning_close = (ImageView) view.findViewById(R.id.iv_winning_close);
            ImageView iv_winning_win = (ImageView) view.findViewById(R.id.iv_winning_win);
            final ImageView iv_winning_icon = (ImageView) view.findViewById(R.id.iv_winning_icon);
            TextView tv_winning_code = (TextView) view.findViewById(R.id.tv_winning_code);
            TextView tv_winning_discribe = (TextView) view.findViewById(R.id.tv_winning_discribe);
            ImageView iv_winning_go = (ImageView) view.findViewById(R.id.iv_winning_go);

            if (Utils.getSpData("locale", context) != null && Utils.getSpData("locale", context).contains("zh")) {
                iv_winning_win.setBackgroundResource(R.drawable.winning_zh);
            } else if (Utils.getSpData("locale", context) != null && Utils.getSpData("locale", context).contains("ms")) {
                iv_winning_win.setBackgroundResource(R.drawable.winning_ms);
            } else {
                iv_winning_win.setBackgroundResource(R.drawable.winning_en);
            }
            Glide.with(context).load("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv_winning_icon.setImageBitmap(resource);
                }
            });
            tv_winning_code.setText(dispatchGameBean.getGame().getIssue_id() + "");
            tv_winning_discribe.setText(dispatchGameBean.getGame().getProduct().getTitle() + "");
            iv_winning_close.setOnClickListener(new MyOnclickListener());
            iv_winning_go.setOnClickListener(new MyOnclickListener());

            winningShow = Utils.StartAlertDialog(MainActivity.this, view, Utils.getScreenWidth(context) * 3 / 4, Utils.getStatusHeight(context) * 3 / 4);

        }
    };
}
