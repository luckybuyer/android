package net.iwantbuyer.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
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
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.halopay.sdk.main.HaloPay;

//import com.inthecheesefactory.lib.fblike.widget.FBLikeView;
import com.umeng.analytics.MobclickAgent;

import net.iwantbuyer.R;
import net.iwantbuyer.adapter.GuideAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.bean.TokenBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.pager.HomePager;
import net.iwantbuyer.pager.MePager;
import net.iwantbuyer.pager.ShowPager;
import net.iwantbuyer.pager.WinningPager;
import net.iwantbuyer.secondpager.BuyCoinPager;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.MyBase64;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.NoScrollViewPager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private FrameLayout fl_main;
    public RadioGroup rg_main;
    private NoScrollViewPager vp_main;
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

    private FacebookAuthProvider provider;

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


        Auth0 auth0 = new Auth0(MyApplication.client_id, MyApplication.domain);

        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);
        FacebookAuthProvider provider = new FacebookAuthProvider(client);
//        provider.setPermissions(Arrays.asList("public_profile", "user_photos"));
        FacebookAuthHandler handler = new FacebookAuthHandler(provider);

        GoogleAuthProvider provide = new GoogleAuthProvider("945656636977-9ag21vtt9k6do5f6nlptn0u96q10v2lh.apps.googleusercontent.com", client);
//        provide.setScopes(new Scope(DriveScopes.DRIVE_METADATA_READONLY));
//        provide.setRequiredPermissions(new String[]{"android.permission.GET_ACCOUNTS"});

        GoogleAuthHandler handle = new GoogleAuthHandler(provide);


        if (lock == null) {
            lock = Lock.newBuilder(auth0, callback)
                    .withAuthHandlers(handler, handle)
                    .closable(true)
//                .withTheme(Theme.newBuilder().withDarkPrimaryColor(R.color.text_black).withHeaderColor(R.color.auth0_header).withHeaderLogo(R.mipmap.ic_launcher).withHeaderTitle(R.string.app_name).withHeaderTitleColor(R.color.text_black).withPrimaryColor(R.color.bg_ff4f3c).build())
                    .withAuthButtonSize(AuthButtonSize.BIG)
//                // Add parameters to the Lock Builder
                    .useBrowser(false)
                    .build(this);
        }


//        auth0登陆
//        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id_text), getString(R.string.auth0_domain_text));
//        Auth0 auth0 = new Auth0(MyApplication.client_id, MyApplication.domain);
////        Auth0 auth0 = new Auth0("HmF3R6dz0qbzGQoYtTuorgSmzgu6Aua1", "staging-luckybuyer.auth0.com");
//        this.lock = Lock.newBuilder(auth0, callback)
//                .closable(true)
////                .withTheme(Theme.newBuilder().withDarkPrimaryColor(R.color.text_black).withHeaderColor(R.color.auth0_header).withHeaderLogo(R.mipmap.ic_launcher).withHeaderTitle(R.string.app_name).withHeaderTitleColor(R.color.text_black).withPrimaryColor(R.color.bg_ff4f3c).build())
//                .withAuthButtonSize(AuthButtonSize.BIG)
////                // Add parameters to the Lock Builder
//                .useBrowser(false)
//                .build(this);
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

        //新手 引导页面
        if (Utils.getSpData("guide", this) == null) {
            vp_main.setAdapter(new GuideAdapter(this, vp_main));
            Utils.setSpData("guide", "noguide", this);
            vp_main.setVisibility(View.GONE);
        } else {
            vp_main.setVisibility(View.GONE);
        }


        rb_main_homepager.setChecked(true);
        if (Utils.checkDeviceHasNavigationBar(MainActivity.this)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getNavigationBarHeight(MainActivity.this));
            rl_main.setLayoutParams(lp);
        }
        if (Utils.checkDeviceHasNavigationBar(MainActivity.this)) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.bottomMargin = Utils.getNavigationBarHeight(MainActivity.this);
            vp_main.setLayoutParams(lp);
        }

        HaloPay.getInstance().init(this, HaloPay.PORTRAIT, "3000600754");

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            Log.e("TAG_000", token);
        }


//        getsp();
        Utils.setSpData("main_pager", null, this);       //当editshow 返回时候的临时变量 一定要删除
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
        vp_main = (NoScrollViewPager) findViewById(R.id.vp_main);
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
                    if (buyCoinPager != null && buyCoinPager.tv_buycoins_balance != null) {
                        if (Utils.getSpData("balance", MainActivity.this) == null) {
                            buyCoinPager.tv_buycoins_balance.setVisibility(View.GONE);
                        } else {
                            buyCoinPager.tv_buycoins_balance.setVisibility(View.VISIBLE);
                        }

                        buyCoinPager.tv_buycoins_balance.setText(Utils.getSpData("balance", MainActivity.this) + "");
                    }

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


//    //选择哪个界面
//    public void switchPage(int checkedId) {
//        Fragment fragment = list.get(checkedId);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fl_main, fragment);
////        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commitAllowingStateLoss();
////        fragmentTransaction.addToBackStack(null);
////        fragmentTransaction.commit();
//    }


    long LoginTime;
    //auth0登陆回调
    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {


            if (Utils.isInLauncher(MainActivity.this)) {
                return;
            }
            //友盟登陆通缉
            LogChannel("Login_Success");

            // Base64 解码：
            String token = credentials.getIdToken();
            Log.e("TAG", token);

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


            if ((System.currentTimeMillis() - LoginTime) > 3000) {
                Log.e("TAG_time", LoginTime + "");
                LoginTime = System.currentTimeMillis();
                Login(token);

                //Appflyer 统计
                Map<String, Object> eventValue = new HashMap<String, Object>();
                AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "LOGIN:logged_in success", eventValue);
            }
        }

        @Override
        public void onCanceled() {
            if (Utils.isInLauncher(MainActivity.this)) {
                return;
            }
            //友盟登陆通缉
            LogChannel("Login_Canceled");
//            //Appflyer 统计
//            Map<String, Object> eventValue = new HashMap<String, Object>();
//            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "Login_Canceled",eventValue);

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "LOGIN:logged_in cancel", eventValue);
            selectPager();
        }

        @Override
        public void onError(LockException error) {
            if (Utils.isInLauncher(MainActivity.this)) {
                return;
            }
            //友盟登陆通缉
            LogChannel("Login_Error");

            //Appflyer 统计
            Map<String, Object> eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "Login_Error", eventValue);

            //Appflyer 统计
            eventValue = new HashMap<String, Object>();
            AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "LOGIN:logged_in failed", eventValue);
            selectPager();


            Utils.MyToast(MainActivity.this, "Login failed");
            selectPager();
        }
    };

    private void LogChannel(String string) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("channel", getString(R.string.channel));
        MobclickAgent.onEvent(this, string, map);
    }

    //auth0登陆成功后  登陆我们自己的api
    private void Login(String token) {
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
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
                Utils.setSpData("social_link", user.getProfile().getSocial_link(), MainActivity.this);

                Log.e("TAG", user.getProfile().getPicture());

                Log.e("TAG_id", id + "");
                Log.e("TAG_login_id", login_id + "");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(MainActivity.this, "Login in successfully");
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

//                                if(mePager != null) {
//                                    mePager.initData();
//                                }
                        } else {
                            rb_main_homepager.setChecked(true);
                            rb_main_buycoins.setChecked(false);
                            rb_main_me.setChecked(false);
                            rb_main_newresult.setChecked(false);

                        }
                        rb_main_homepager.setChecked(false);
                        rb_main_newresult.setChecked(false);

                    }
                });


            }

            @Override
            public void error(int requestCode, String message) {
                Log.e("TAG", requestCode + "");
                Log.e("TAG", message);
                Utils.setSpData("token", null, MainActivity.this);
                Utils.setSpData("token_num", null, MainActivity.this);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(MainActivity.this, "Login in failed");
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
                        Utils.MyToast(MainActivity.this, "Login failed, please login again");
                        selectPager();
                    }
                });
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
            if (buyCoinPager != null && buyCoinPager.wv_buycoins_cashu != null && buyCoinPager.wv_buycoins_cashu.getVisibility() == View.VISIBLE) {
                buyCoinPager.wv_buycoins_cashu.setVisibility(View.GONE);
                buyCoinPager.ll_buycoins_back.setVisibility(View.GONE);
                buyCoinPager.tv_title.setText(getString(R.string.BuyCoins));
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 3000) {
                    Toast.makeText(this, "click again to exit", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

                } else {
                    finish();
                }
            }

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("show".equals(Utils.getSpData("main_pager", this))) {
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

        MobclickAgent.onResume(this);                      //统计时长   友盟

    }

    @Override
    protected void onPause() {
        super.onPause();
//        AppEventsLogger.deactivateApp(this);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG——activityresult", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        super.onActivityResult(requestCode, resultCode, data);
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
        if (buyCoinPager != null && buyCoinPager.mHelper != null && !buyCoinPager.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.e("TAG", "onActivityResult handled by IABUtil.");
        }
    }


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
//
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

}
