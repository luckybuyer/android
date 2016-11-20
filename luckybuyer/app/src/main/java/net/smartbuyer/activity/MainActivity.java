package net.smartbuyer.activity;

import android.content.Intent;
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

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.Theme;
import com.auth0.android.lock.enums.SocialButtonStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.halopay.sdk.main.HaloPay;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;

import net.smartbuyer.R;
import net.smartbuyer.app.MyApplication;
import net.smartbuyer.bean.TokenBean;
import net.smartbuyer.bean.User;
import net.smartbuyer.pager.HomePager;
import net.smartbuyer.pager.MePager;
import net.smartbuyer.pager.WinningPager;
import net.smartbuyer.secondpager.BuyCoinPager;
import net.smartbuyer.util.IabHelper;
import net.smartbuyer.util.IabResult;
import net.smartbuyer.util.Purchase;
import net.smartbuyer.utils.HttpUtils;
import net.smartbuyer.utils.MyBase64;
import net.smartbuyer.utils.Utils;

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

    private RadioButton rb_main_homepager;
    private RadioButton rb_main_buycoins;
    private RadioButton rb_main_newresult;
    private RadioButton rb_main_me;
    private RelativeLayout rl_main;


    private FragmentManager fragmentManager;
    private HomePager homePager;
    private BuyCoinPager buyCoinPager;
    private WinningPager winningPager;
    private MePager mePager;

    public int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);

        Log.e("TAG_dimen", getResources().getDimension(R.dimen.dimen_10) + "");
        //auth0登陆
        Auth0 auth0 = new Auth0("6frbTA5t3o1djsPYLp0jPiDGx7cvIyVc", "luckybuyer.auth0.com");
//        Auth0 auth0 = new Auth0("HmF3R6dz0qbzGQoYtTuorgSmzgu6Aua1", "staging-luckybuyer.auth0.com");
        this.lock = Lock.newBuilder(auth0, callback)
                .closable(true)
                .withTheme(Theme.newBuilder().withDarkPrimaryColor(R.color.text_black).withHeaderColor(R.color.bg_ff4f3c).withHeaderLogo(R.mipmap.ic_launcher).withHeaderTitle(R.string.app_name).withHeaderTitleColor(R.color.text_white).withPrimaryColor(R.color.bg_ff4f3c).build())
                .withSocialButtonStyle(SocialButtonStyle.BIG)
                // Add parameters to the Lock Builder
                .build();
        this.lock.onCreate(this);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用

            //从fragmentManager里面找到fragment
            homePager = (HomePager) fragmentManager.findFragmentByTag(HomePager.class.getName());
            buyCoinPager = (BuyCoinPager) fragmentManager.findFragmentByTag(BuyCoinPager.class.getName());
            winningPager = (WinningPager) fragmentManager.findFragmentByTag(WinningPager.class.getName());
            mePager = (MePager) fragmentManager.findFragmentByTag(MePager.class.getName());

            //解决重叠问题show里面可以指定恢复的页面
            fragmentManager.beginTransaction()
                    .show(homePager)
                    .hide(buyCoinPager)
                    .hide(winningPager)
                    .hide(mePager)
                    .commit();

            //把当前显示的fragment记录下来
            currentFragment = homePager;

        } else { //正常启动时调用

            homePager = new HomePager();
            buyCoinPager = new BuyCoinPager();
            winningPager = new WinningPager();
            mePager = new MePager();

            showFragment(homePager);
        }

        //设置数据
        setData();
        //发现视图  设置监听
        findView();

        rb_main_homepager.setChecked(true);
        if (Utils.checkDeviceHasNavigationBar(MainActivity.this)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getNavigationBarHeight(MainActivity.this));
            rl_main.setLayoutParams(lp);
        }

        HaloPay.getInstance().init(this, HaloPay.PORTRAIT, "3000600754");

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            Log.e("TAG_000", token);
        }


    }

    //设置数据
    private void setData() {
        list = new ArrayList<>();
        list.add(homePager);
        list.add(buyCoinPager);
        list.add(winningPager);
        list.add(mePager);
    }

    //试图初始化 与设置监听
    private void findView() {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        rb_main_homepager = (RadioButton) findViewById(R.id.rb_main_homepager);
        rb_main_buycoins = (RadioButton) findViewById(R.id.rb_main_buycoins);
        rb_main_newresult = (RadioButton) findViewById(R.id.rb_main_newresult);
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
                    rb_main_me.setChecked(false);
                    break;
                case R.id.rb_main_buycoins:
                    id = 1;
                    showFragment(list.get(1));
                    rb_main_homepager.setChecked(false);
                    rb_main_buycoins.setChecked(true);
                    rb_main_newresult.setChecked(false);
                    rb_main_me.setChecked(false);
                    login_id = 1;
                    break;
                case R.id.rb_main_newresult:
                    id = 2;
                    showFragment(list.get(2));
                    rb_main_homepager.setChecked(false);
                    rb_main_buycoins.setChecked(false);
                    rb_main_newresult.setChecked(true);
                    rb_main_me.setChecked(false);

                    break;
                case R.id.rb_main_me:
                    String token_s = Utils.getSpData("token_num", MainActivity.this);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }

                    //判断是否登陆  未登陆  先登录  登陆 进入me页面
                    if (token > System.currentTimeMillis() / 1000) {
                        showFragment(list.get(3));
                        rb_main_homepager.setChecked(false);
                        rb_main_buycoins.setChecked(false);
                        rb_main_newresult.setChecked(false);
                        rb_main_me.setChecked(true);
                        id = 3;
                    } else {
                        MainActivity.this.startActivity(MainActivity.this.lock.newIntent(MainActivity.this));
                        login_id = 3;
                    }
                    break;
            }
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

    //auth0登陆回调
    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {

            Log.e("TAG", credentials + "");
            // Base64 解码：
            String token = credentials.getIdToken();

            Log.e("TAG_token错误", token);
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
            String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
            Map map = new HashMap<String, String>();
            map.put("Authorization", "Bearer " + token);
            //请求登陆接口
            HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    Utils.setSpData("id", user.getId() + "", MainActivity.this);
                    Utils.setSpData("user_id", user.getAuth0_user_id(), MainActivity.this);
                    Utils.setSpData("balance", user.getBalance() + "", MainActivity.this);
                    Utils.setSpData("name", user.getProfile().getName(), MainActivity.this);
                    Utils.setSpData("picture", user.getProfile().getPicture(), MainActivity.this);
                    Utils.setSpData("social_link", user.getProfile().getSocial_link(), MainActivity.this);

                    Log.e("TAG", user.getProfile().getPicture());

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //登陆成功  直接进入me页面
                            Log.e("TAG", "咱们登陆成功");
                            showFragment(list.get(login_id));
                            if (login_id == 1) {
                                rb_main_buycoins.setChecked(true);
                                rb_main_me.setChecked(false);
                            } else if (login_id == 3) {
                                rb_main_buycoins.setChecked(false);
                                rb_main_me.setChecked(true);
                            }
                            rb_main_homepager.setChecked(false);
                            rb_main_newresult.setChecked(false);

                        }
                    });

                    Log.e("TAG_用户信息", response);

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
                            Utils.MyToast(MainActivity.this, "Login failed, please login again");
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
                        }
                    });
                }
            });

        }

        @Override
        public void onCanceled() {
            Log.e("TAG_id_error", id + "");
            if (id == 0) {
                showFragment(list.get(0));
                rb_main_homepager.setChecked(true);
                rb_main_buycoins.setChecked(false);
                rb_main_newresult.setChecked(false);
                rb_main_me.setChecked(false);
            } else if (id == 1) {
                showFragment(list.get(1));
                rb_main_homepager.setChecked(false);
                rb_main_buycoins.setChecked(true);
                rb_main_newresult.setChecked(false);
                rb_main_me.setChecked(false);
            } else if (id == 2) {
                showFragment(list.get(2));
                rb_main_homepager.setChecked(false);
                rb_main_buycoins.setChecked(false);
                rb_main_newresult.setChecked(true);
                rb_main_me.setChecked(false);
            } else if (id == 3) {
                showFragment(list.get(0));
                rb_main_homepager.setChecked(true);
                rb_main_buycoins.setChecked(false);
                rb_main_newresult.setChecked(false);
                rb_main_me.setChecked(false);
            }
        }

        @Override
        public void onError(LockException error) {
            Log.e("TAG_id", id + "");
            Utils.MyToast(MainActivity.this,"Login failed");
            if (id == 0) {
                showFragment(list.get(0));
                rb_main_homepager.setChecked(true);
                rb_main_buycoins.setChecked(false);
                rb_main_newresult.setChecked(false);
                rb_main_me.setChecked(false);
            } else if (id == 1) {
                showFragment(list.get(1));
                rb_main_homepager.setChecked(false);
                rb_main_buycoins.setChecked(true);
                rb_main_newresult.setChecked(false);
                rb_main_me.setChecked(false);
            } else if (id == 2) {
                showFragment(list.get(2));
                rb_main_homepager.setChecked(false);
                rb_main_buycoins.setChecked(false);
                rb_main_newresult.setChecked(true);
                rb_main_me.setChecked(false);
            } else if (id == 3) {
                showFragment(list.get(0));
                rb_main_homepager.setChecked(true);
                rb_main_buycoins.setChecked(false);
                rb_main_newresult.setChecked(false);
                rb_main_me.setChecked(false);
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        showFragment(currentFragment);
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
            Log.e("TAG", "进入这个了");

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
            FBLikeView.onActivityResult(requestCode, resultCode, data);
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


}
