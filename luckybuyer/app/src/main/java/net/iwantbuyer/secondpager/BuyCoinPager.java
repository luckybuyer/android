package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.halopay.interfaces.callback.IPayResultCallback;
import com.halopay.sdk.main.HaloPay;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.umeng.analytics.MobclickAgent;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.BuyCoinAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.bean.BuyStateBean;
import net.iwantbuyer.bean.CashuBean;
import net.iwantbuyer.bean.HaloPayBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.util.Purchase;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.MyBase64;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.JustifyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {
    private static final int RESULT_PAYMENT_OK = 1001;       //google pay回调监听
    private static PayPalConfiguration config = new PayPalConfiguration()
            // 沙盒测试(ENVIRONMENT_SANDBOX)，生产环境(ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .merchantName("KangQian")
            //你创建的测试应用Client ID  
            .clientId("Adzkv-hUMlgx54740-FtAa-crh3rG7r2V-6Ezvx5cOOyODxl1wbF_ZC1Ups9EzkHJWGd5aQWk4iSCVjf");


    private RelativeLayout rl_buycoins_header;
    public LinearLayout ll_buycoins_back;
    private ImageView iv_help;
    public TextView tv_buycoins_balance;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;
    private RecyclerView rv_buycoins;
    public TextView tv_title;                      //标题

    private ImageView iv_buycoins_paypal;
    private ImageView iv_buycoins_halopay;
    private ImageView iv_buycoins_goole;
    private ImageView iv_buycoins_cashu;
    private ImageView iv_buycoins_visa;
    private RelativeLayout rl_buycoins_halopay;
    private RelativeLayout rl_buycoins_paypal;
    private RelativeLayout rl_buycoins_google;
    private RelativeLayout rl_buycoins_cashu;
    private RelativeLayout rl_buycoins_visa;
    private boolean flag;
    private RelativeLayout rl_buycoins_ok;
    private ProgressBar pb_buycoins_progress;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private BuyCoinBean buyCoinBean;

    public WebView wv_buycoins_cashu;

    public IabHelper mHelper;                                     //谷歌支付

    public AlertDialog show;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);

        flag = context instanceof SecondPagerActivity;
        if (context instanceof SecondPagerActivity) {
            ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        }


        findView();

        //paypal支付
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        context.startService(intent);

        //holypay  支付
        if (flag) {
            HaloPay.getInstance().init(((SecondPagerActivity) context), HaloPay.PORTRAIT, "3000600754");                 //holypay支付
            HaloPay.getInstance().setLang(((SecondPagerActivity) context), "AE", "AED", "EN");
        } else {
            HaloPay.getInstance().init(((MainActivity) context), HaloPay.PORTRAIT, "3000600754");                 //holypay支付
            HaloPay.getInstance().setLang(((MainActivity) context), "AE", "AED", "EN");
        }


        //谷歌支付初始化
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsNdFCBJ/+rP52eYwXardjTwcX+39SQ/87d+SWnLfIH2UghectXo5yPG9FqM3Egc9R0YKtKN/KtBvnZMCQBrdNYaOc44h81fOnckBMstDskDRLnHsM6+xmOCemN14OeWLntij4IRgS75LOctWnzFf6ombNtnjvcJZ+ax60dyzKi0b/GoYP8VLvrRpzYnUuJWAohPVTfn415qOEuAXD8DGijKp7ciilv76Up5z5pRMUdbK9HVusMLECWwzoPsiPObCGLS4szYgjdD5DKVpPItV4sSsZH9kXVlxaGzm7iPxfDPE3cxy4WG1FWerxkbOxWI8jG9a/pUA3+uatapZwSMKBQIDAQAB";
        mHelper = new IabHelper(context, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.e("TAG_startSetup", result + "");

                if (!result.isSuccess()) {
                    return;
                }

                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
//                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        //判断打开哪些方法
        String method = Utils.getSpData("paymentmethod", context);
        if (method.contains("android-inapp")) {
            rl_buycoins_google.setVisibility(View.VISIBLE);
            iv_buycoins_goole.setEnabled(true);
            iv_buycoins_paypal.setEnabled(false);
            iv_buycoins_visa.setEnabled(false);
            iv_buycoins_cashu.setEnabled(false);
            iv_buycoins_halopay.setEnabled(false);

        }
        if (method.contains("paypal")) {
            rl_buycoins_paypal.setVisibility(View.VISIBLE);
            iv_buycoins_paypal.setEnabled(false);
        }
        if (method.contains("visa")) {
            rl_buycoins_visa.setVisibility(View.VISIBLE);
            iv_buycoins_visa.setEnabled(false);
        }

        if (method.contains("cashu")) {
            rl_buycoins_cashu.setVisibility(View.VISIBLE);
            iv_buycoins_cashu.setEnabled(false);
        }

        if (method.contains("halopay")) {
            rl_buycoins_halopay.setVisibility(View.VISIBLE);
            iv_buycoins_halopay.setEnabled(false);
        }


        Log.e("TAG", iv_buycoins_goole.isHovered() + "00" + iv_buycoins_paypal.isHovered() + iv_buycoins_cashu.isHovered() + iv_buycoins_halopay.isHovered());
        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track("PAGE:buy_coins", props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        //AppFlyer 埋点
        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context, "PAGE:topup",eventValue);

        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        rl_keepout.setVisibility(View.VISIBLE);
        rl_nodata.setVisibility(View.GONE);
        rl_neterror.setVisibility(View.GONE);
        rl_loading.setVisibility(View.VISIBLE);

        String url = MyApplication.url + "/v1/topup-options/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                        rl_keepout.setVisibility(View.GONE);
                        View view = View.inflate(context, R.layout.alertdialog_buycoins, null);
                        rl_buycoins_ok = (RelativeLayout) view.findViewById(R.id.rl_buycoins_ok);
                        rl_buycoins_ok.setOnClickListener(new MyOnClickListener());
                        show = StartAlertDialog(view);
                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });

            }

        });
    }

    public void findView() {
        rl_buycoins_header = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_header);
        ll_buycoins_back = (LinearLayout) inflate.findViewById(R.id.ll_buycoins_back);
        iv_help = (ImageView) inflate.findViewById(R.id.iv_help);
        tv_buycoins_balance = (TextView) inflate.findViewById(R.id.tv_buycoins_balance);
        tv_buycoins_count = (TextView) inflate.findViewById(R.id.tv_buycoins_count);
        tv_buycoins_buy = (TextView) inflate.findViewById(R.id.tv_buycoins_buy);
        rv_buycoins = (RecyclerView) inflate.findViewById(R.id.rv_buycoins);
        tv_title = (TextView) inflate.findViewById(R.id.tv_title);

        iv_buycoins_paypal = (ImageView) inflate.findViewById(R.id.iv_buycoins_paypal);
        iv_buycoins_halopay = (ImageView) inflate.findViewById(R.id.iv_buycoins_halopay);
        iv_buycoins_goole = (ImageView) inflate.findViewById(R.id.iv_buycoins_goole);
        iv_buycoins_cashu = (ImageView) inflate.findViewById(R.id.iv_buycoins_cashu);
        iv_buycoins_visa = (ImageView) inflate.findViewById(R.id.iv_buycoins_visa);
        rl_buycoins_halopay = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_halopay);
        rl_buycoins_paypal = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_paypal);
        rl_buycoins_google = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_google);
        rl_buycoins_cashu = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_cashu);
        rl_buycoins_visa = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_visa);
        wv_buycoins_cashu = (WebView) inflate.findViewById(R.id.wv_buycoins_cashu);
        pb_buycoins_progress = (ProgressBar) inflate.findViewById(R.id.pb_buycoins_progress);


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);


        //设置监听
        ll_buycoins_back.setOnClickListener(new MyOnClickListener());
        iv_help.setOnClickListener(new MyOnClickListener());
        tv_buycoins_buy.setOnClickListener(new MyOnClickListener());
        rl_buycoins_halopay.setOnClickListener(new MyOnClickListener());
        rl_buycoins_paypal.setOnClickListener(new MyOnClickListener());
        rl_buycoins_google.setOnClickListener(new MyOnClickListener());
        rl_buycoins_cashu.setOnClickListener(new MyOnClickListener());
        rl_buycoins_visa.setOnClickListener(new MyOnClickListener());

        tv_net_again.setOnClickListener(new MyOnClickListener());

        if (!flag) {
            ll_buycoins_back.setVisibility(View.GONE);
        }
    }

    //开始google play支付
    private void startRegistered() {
        try {
            if (flag) {
                mHelper.flagEndAsync();
                mHelper.launchPurchaseFlow(((SecondPagerActivity) context), topup_option_id + "", RESULT_PAYMENT_OK, mPurchaseFinishedListener);
//                mHelper.launchPurchaseFlow(((SecondPagerActivity) context), "test_two", RESULT_PAYMENT_OK, mPurchaseFinishedListener);
            } else {
                mHelper.flagEndAsync();
                mHelper.launchPurchaseFlow(((MainActivity) context), topup_option_id + "", RESULT_PAYMENT_OK, mPurchaseFinishedListener);
//                mHelper.launchPurchaseFlow(((MainActivity) context), "test_two", RESULT_PAYMENT_OK, mPurchaseFinishedListener);
            }
        } catch (Exception e) {
            Utils.MyToast(context, "Sorry, temporarily unable to make Google payments");
//            e.printStackTrace();
            Log.e("TAG+错误", e + "");

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
                mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                    @Override
                    public void onConsumeFinished(Purchase purchase, IabResult result) {
                        Log.e("TAG_消耗", result.isSuccess() + "");
                    }
                });

            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }

        }
    };


    int money = 0;
    int topup_option_id = -1;
    int id_coins = 0;

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"buycoins\":" + response + "}";
        buyCoinBean = gson.fromJson(response, BuyCoinBean.class);
        BuyCoinAdapter buyCoinAdapter = new BuyCoinAdapter(context, buyCoinBean.getBuycoins());
        rv_buycoins.setAdapter(buyCoinAdapter);
        rv_buycoins.setLayoutManager(new GridLayoutManager(context, 2));

//        double coins = buyCoinBean.getBuycoins().get(0).getAmount() * 3.6726;                                汇率
//        coins = ((int) (coins * 100)) / 100;
        tv_buycoins_balance.setText(Utils.getSpData("balance", context));
        tv_buycoins_count.setText("$" + buyCoinBean.getBuycoins().get(0).getPrice());

        money = buyCoinBean.getBuycoins().get(0).getPrice();
        topup_option_id = buyCoinBean.getBuycoins().get(0).getId();
        buyCoinAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                double coins = buyCoinBean.getBuycoins().get(position).getAmount() * 3.6726;
                coins = ((int) (coins * 100)) / 100;
                tv_buycoins_count.setText("$" + buyCoinBean.getBuycoins().get(position).getPrice());
                money = buyCoinBean.getBuycoins().get(position).getPrice();
                topup_option_id = buyCoinBean.getBuycoins().get(position).getId();
                Log.e("TAG_产品id", topup_option_id + "");
                id_coins = position;
                String coin = "";
                if (position == 0) {
                    coin = "CLICK:1 coin";
                } else if (position == 1) {
                    coin = "CLICK:10 coins";

                } else if (position == 2) {
                    coin = "CLICK:50 coins";

                } else if (position == 3) {
                    coin = "CLICK:100 coins";
                }
                setPoint(coin);

                //AppFlyer 埋点
                setAppflyerPoint(coin);
            }
        });
    }

    public void setAppflyerPoint(String coins){
        //AppFlyer 埋点
        Map<String, Object>  eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context,  coins ,eventValue);
    }
    public void setPoint(String coins) {
        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track(coins, props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_buycoins_back:
                    if (wv_buycoins_cashu.getVisibility() == View.VISIBLE) {
                        wv_buycoins_cashu.setVisibility(View.GONE);
                        tv_title.setText(context.getString(R.string.BuyCoins));
                    } else if (context instanceof SecondPagerActivity) {
//                        if ("coindetailpager".equals(((SecondPagerActivity) context).from)) {
//                            ((SecondPagerActivity) context).switchPage(5);
//                        } else if ("productdetail".equals(((SecondPagerActivity) context).from)) {
//                            ((SecondPagerActivity) context).switchPage(0);
//                        }else if("buycoinpager".equals(((SecondPagerActivity) context).from)) {
//                            ((SecondPagerActivity) context).switchPage(5);
//                        }
                        ((SecondPagerActivity)context).fragmentManager.popBackStack();
                    }
                    break;
                case R.id.tv_buycoins_buy:
                    //有盟  自定义事件
                    HashMap<String, String> map = new HashMap<String, String>();

                    //埋点
                    JSONObject props = new JSONObject();

                    //AppFlyer
                    Map<String, Object> eventValue = new HashMap<String, Object>();


                    String token_s = Utils.getSpData("token_num", context);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }
                    Log.e("TAG", iv_buycoins_goole.isHovered() + "00" + iv_buycoins_paypal.isHovered() + iv_buycoins_halopay.isHovered());
                    if (token > System.currentTimeMillis() / 1000) {
                        Log.e("TAG", iv_buycoins_goole.isHovered() + "00" + iv_buycoins_paypal.isHovered() + iv_buycoins_halopay.isHovered());
                        if (iv_buycoins_paypal.isEnabled()) {
                            PayPalPayment payment = new PayPalPayment(new BigDecimal(money + ""), buyCoinBean.getBuycoins().get(id_coins).getCurrency(), "LuckyBuyer",
                                    PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(context, PaymentActivity.class);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                            startActivityForResult(intent, 0);
                            try {
                                props.put("%method", "paypal");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map.put("method", "paypal");

                            eventValue.put("%method","paypal");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup",eventValue);
                        } else if (iv_buycoins_halopay.isEnabled()) {
                            StartHalopay();
                            try {
                                props.put("%method", "halopay");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map.put("method", "halopay");
                            eventValue.put("%method","halopay");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup",eventValue);
                        } else if (iv_buycoins_goole.isEnabled()) {
                            startRegistered();
                            try {
                                props.put("%method", "google_pay");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map.put("method", "google_pay");

                            eventValue.put("%method","google_pay");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup",eventValue);
                        } else if (iv_buycoins_cashu.isEnabled()) {
                            startCashu();
                            try {
                                props.put("%method", "cashu");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map.put("method", "cashu");

                            eventValue.put("%method","cashu");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup",eventValue);
                        }else if(iv_buycoins_visa.isEnabled()) {
                            PayPalPayment payment = new PayPalPayment(new BigDecimal(money + ""), buyCoinBean.getBuycoins().get(id_coins).getCurrency(), "LuckyBuyer",
                                    PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(context, PaymentActivity.class);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                            startActivityForResult(intent, 0);
                            try {
                                props.put("%method", "visa");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map.put("method", "visa");

                            eventValue.put("%method","Master or Visa");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup",eventValue);
                        }

                        //埋点
                        MyApplication.mixpanel.track("CLICK:buy", props);

                        MobclickAgent.onEvent(context, "Buy", map);
                    } else {
                        if (context instanceof SecondPagerActivity) {
                            context.startActivity(((SecondPagerActivity) context).lock.newIntent(((SecondPagerActivity) context)));
                            //埋点
                            try {
                                JSONObject prop = new JSONObject();
                                MyApplication.mixpanel.track("LOGIN:showpage", prop);
                            } catch (Exception e) {
                                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                            }
                            //AppFlyer 埋点
                            eventValue = new HashMap<String, Object>();
                            AppsFlyerLib.getInstance().trackEvent(context, "Page：Login",eventValue);
                        } else if (context instanceof MainActivity) {
                            context.startActivity(((MainActivity) context).lock.newIntent(((MainActivity) context)));
                            //AppFlyer 埋点
                            eventValue = new HashMap<String, Object>();
                            AppsFlyerLib.getInstance().trackEvent(context, "Page：Login",eventValue);
                        }
                    }

                    break;
                case R.id.rl_buycoins_halopay:
//                    iv_buycoins_halopay.setVisibility(View.VISIBLE);
//                    iv_buycoins_cashu.setVisibility(View.GONE);
//                    iv_buycoins_paypal.setVisibility(View.GONE);
//                    iv_buycoins_goole.setVisibility(View.GONE);
                    iv_buycoins_halopay.setEnabled(true);
                    iv_buycoins_paypal.setEnabled(false);
                    iv_buycoins_goole.setEnabled(false);
                    iv_buycoins_cashu.setEnabled(false);
                    iv_buycoins_visa.setEnabled(false);
                    setPoint("CLICK:halopay");
                    //AppFlyer 埋点
                    eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context,  "CLICK: Halo pay" ,eventValue);
                    break;
                case R.id.rl_buycoins_paypal:
//                    iv_buycoins_halopay.setVisibility(View.GONE);
//                    iv_buycoins_paypal.setVisibility(View.VISIBLE);
//                    iv_buycoins_goole.setVisibility(View.GONE);
//                    iv_buycoins_cashu.setVisibility(View.GONE);
                    iv_buycoins_halopay.setEnabled(false);
                    iv_buycoins_paypal.setEnabled(true);
                    iv_buycoins_goole.setEnabled(false);
                    iv_buycoins_cashu.setEnabled(false);
                    iv_buycoins_visa.setEnabled(false);
                    setPoint("CLICK:paypal");
                    //AppFlyer 埋点
                    eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context,  "CLICK:Paypal" ,eventValue);
                    break;
                case R.id.rl_buycoins_google:
//                    iv_buycoins_cashu.setVisibility(View.GONE);
//                    iv_buycoins_halopay.setVisibility(View.GONE);
//                    iv_buycoins_paypal.setVisibility(View.GONE);
//                    iv_buycoins_goole.setVisibility(View.VISIBLE);
                    iv_buycoins_cashu.setEnabled(false);
                    iv_buycoins_halopay.setEnabled(false);
                    iv_buycoins_paypal.setEnabled(false);
                    iv_buycoins_goole.setEnabled(true);
                    iv_buycoins_visa.setEnabled(false);
                    setPoint("CLICK:google_pay");
                    //AppFlyer 埋点
                    eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context,  "CLICK:Google Pay" ,eventValue);

                    break;
                case R.id.rl_buycoins_cashu:
//                    iv_buycoins_cashu.setVisibility(View.VISIBLE);
//                    iv_buycoins_paypal.setVisibility(View.GONE);
//                    iv_buycoins_goole.setVisibility(View.GONE);
//                    iv_buycoins_halopay.setVisibility(View.GONE);
                    iv_buycoins_cashu.setEnabled(true);
                    iv_buycoins_halopay.setEnabled(false);
                    iv_buycoins_paypal.setEnabled(false);
                    iv_buycoins_goole.setEnabled(false);
                    iv_buycoins_visa.setEnabled(false);
                    setPoint("CLICK:cashu");
                    break;
                case R.id.rl_buycoins_visa:
//                    iv_buycoins_cashu.setVisibility(View.VISIBLE);
//                    iv_buycoins_paypal.setVisibility(View.GONE);
//                    iv_buycoins_goole.setVisibility(View.GONE);
//                    iv_buycoins_halopay.setVisibility(View.GONE);
                    iv_buycoins_cashu.setEnabled(false);
                    iv_buycoins_halopay.setEnabled(false);
                    iv_buycoins_paypal.setEnabled(false);
                    iv_buycoins_goole.setEnabled(false);
                    iv_buycoins_visa.setEnabled(true);
                    eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context,  "CLICK: Master or Visa" ,eventValue);
                    setPoint("CLICK:visa");
                    break;
                case R.id.rl_buycoins_ok:
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_net_again:
                    initData();
                    break;
                case R.id.tv_buycoins_success:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_buycoins_failure:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.iv_help:                 //帮助
                    if (flag) {
                        ((SecondPagerActivity) context).switchPage(11);
                        ((SecondPagerActivity) context).from = "buycoinpager";

                    } else {
                        Intent intent = new Intent(context, SecondPagerActivity.class);
                        intent.putExtra("from","helppager");
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    //cashu支付
    private void startCashu() {
        HttpUtils.getInstance().startNetworkWaiting(context);
        String url = "https://api-staging.luckybuyer.net/v1/payments/cashu/?timezone=utc";
//        String json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": \"alipay_cn\",\"success_url\": \"http://net.luckybuyer.success\",\"topup_option_id\": " + topup_option_id + "}";
        String success_url = "http://net.luckybuyer.success";
        String failure_url = "http://net.luckybuyer.failure";
        String json = "{\"failure_url\": \"" + failure_url + "\",\"success_url\": \"" + success_url + "\",\"topup_option_id\": 1}";
        Map map = new HashMap();
        String mToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3N0YWdpbmctbHVja3lidXllci5hdXRoMC5jb20vIiwic3ViIjoiZmFjZWJvb2t8MTE4MDkzOTkxOTgzNzM1IiwiYXVkIjoiSG1GM1I2ZHowcWJ6R1FvWXRUdW9yZ1Ntemd1NkF1YTEiLCJleHAiOjE0ODA5MzQ1MTAsImlhdCI6MTQ4MDMyOTcxMH0.DBi7mYTrztdBCLTUlys8Pg0geFGcJkH55GFF9U7a710";
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_cashu", response);
                        processCashU(response);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                    }
                });
            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_cashu", code + message);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        startAlert(false);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        startAlert(false);
                    }
                });
            }
        });

    }

    private void processCashU(String response) {
        ll_buycoins_back.setVisibility(View.VISIBLE);
        tv_title.setText(context.getString(R.string.CashU));
        wv_buycoins_cashu.setVisibility(View.VISIBLE);
        Gson gson = new Gson();
        CashuBean cashuBean = gson.fromJson(response, CashuBean.class);

        WebSettings webSettings = wv_buycoins_cashu.getSettings();
        webSettings.setJavaScriptEnabled(true);


        String postData = "Transaction_Code=" + cashuBean.getTransaction_code();
        wv_buycoins_cashu.postUrl(cashuBean.getPayment_url(), postData.getBytes());
        wv_buycoins_cashu.setWebViewClient(new MyWebViewClient());

        wv_buycoins_cashu.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb_buycoins_progress.setVisibility(View.VISIBLE);
                pb_buycoins_progress.setProgress(newProgress);
                if (newProgress == 100) {
                    pb_buycoins_progress.setVisibility(View.GONE);
                }
            }

        });
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//网页页面开始加载的时候
            super.onPageStarted(view, url, favicon);
            Log.e("TAG_kaishi", url);
            if (url.contains("http://net.luckybuyer.failure")) {  //请求失败
                wv_buycoins_cashu.setVisibility(View.GONE);
                startAlert(false);
                ll_buycoins_back.setVisibility(View.GONE);
                tv_title.setText(context.getString(R.string.BuyCoins));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {//网页加载结束的时候
            super.onPageFinished(view, url);
            if ("http://net.luckybuyer.success".equals(url)) {   //请求成功
                wv_buycoins_cashu.setVisibility(View.GONE);
                requestCoins();
                startAlert(true);
                ll_buycoins_back.setVisibility(View.GONE);
                tv_title.setText(context.getString(R.string.BuyCoins));
            } else if ("http://net.luckybuyer.failure".equals(url)) {  //请求失败
                wv_buycoins_cashu.setVisibility(View.GONE);
                startAlert(false);
                ll_buycoins_back.setVisibility(View.GONE);
                tv_title.setText(context.getString(R.string.BuyCoins));
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //网页加载时的连接的网址
            Log.e("TAG_zhengzai", url);
            return false;
        }
    }

    //paysession 支付
    private void StartHalopay() {
        String url = MyApplication.url + "/v1/payments/halopay/?timezone=" + MyApplication.utc;
//        String json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": \"alipay_cn\",\"success_url\": \"http://net.luckybuyer.success\",\"topup_option_id\": " + topup_option_id + "}";
        String json = "{ \"topup_option_id\": " + topup_option_id + "}";
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processHalopay(response);
                    }
                });
            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", code + message);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {

            }
        });
    }

    private void processHalopay(String response) {
        Gson gson = new Gson();
        HaloPayBean haloPayBean = gson.fromJson(response, HaloPayBean.class);
        if (flag) {
            HaloPay.getInstance().startPay(((SecondPagerActivity) context), "transid=" + haloPayBean.getTransaction_id() + "&appid=3000600754", new MyIPayResultCallback());
        } else {
            HaloPay.getInstance().startPay((MainActivity) context, "transid=" + haloPayBean.getTransaction_id() + "&appid=3000600754", new MyIPayResultCallback());
        }
    }

    //halopay 支付 回调
    class MyIPayResultCallback implements IPayResultCallback {

        @Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            switch (resultCode) {
                case HaloPay.PAY_SUCCESS:
                    requestCoins();
                    break;
                case HaloPay.PAY_ERROR:
                    //支付失败
                    startAlert(false);
                    break;
                default:
                    Log.e("TAG", resultInfo);
                    Log.e("TAG", resultCode + "");
                    Log.e("TAG", signvalue + "");
                    break;
            }
        }

    }


    private AlertDialog StartAlertDialog(View view) {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, 1 * screenHeight / 2);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return show;
    }


    private void startAlert(boolean flag) {
        View view = null;
        if (flag) {
            view = View.inflate(context, R.layout.alertdialog_buycoins_success, null);
            TextView tv_buycoins_success = (TextView) view.findViewById(R.id.tv_buycoins_success);
            tv_buycoins_success.setOnClickListener(new MyOnClickListener());
        } else if (!flag) {
            view = View.inflate(context, R.layout.alertdialog_buycoins_failure, null);
            TextView tv_buycoins_failure = (TextView) view.findViewById(R.id.tv_buycoins_failure);
            tv_buycoins_failure.setOnClickListener(new MyOnClickListener());
        }

        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, 1 * screenHeight / 2);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
//                    Log.e("paymentExample", confirm.toJSONObject().toString(4));
                    Log.e("paymentExample", confirm.getPayment().toJSONObject().toString(4));
                    Gson gson = new Gson();
                    Map<String, Map<String, String>> map = gson.fromJson(confirm.toJSONObject().toString(), Map.class);
                    Log.e("TAG", map.get("response").get("id"));
                    // TODO: 发送支付ID到你的服务器进行验证  
                    paypalSever(map.get("response").get("id"));

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            startAlert(false);
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    //谷歌支付成功后 调用我们自己的接口
    public void googleMyService(String purchaseData, String dataSignature) {
        String url = MyApplication.url + "/v1/payments/android-inapp/?timezone=" + MyApplication.utc;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("data_signature", dataSignature);
            jsonObject.accumulate("purchase_data", purchaseData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        Log.e("TAG_json", json);
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_支付成功", response);
                        Utils.MyToast(context, "payment successful");
                        tv_buycoins_balance.setText(Utils.getSpData("balance", context) + buyCoinBean.getBuycoins().get(id_coins).getAmount());
                        //重新请求一下接口
                        requestCoins();
                    }
                });

            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", code + message);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {

            }
        });
    }

    //开始paypal 请求接口
    private void paypalSever(final String id) {
        String url = MyApplication.url + "/v1/payments/paypal/confirmed/?timezone=" + MyApplication.utc;
        String json = "{\"paypal_payment_id\": \"" + id + "\",\"topup_option_id\": " + topup_option_id + "}";
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startAlert(true);
                        tv_buycoins_balance.setText(Utils.getSpData("balance", context) + buyCoinBean.getBuycoins().get(id_coins).getAmount());
                        //重新请求一下接口
                        requestCoins();
                    }
                });

            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", code + message);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {

            }
        });
    }

    private void requestCoins() {
        String token = Utils.getSpData("token", context);
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                Utils.setSpData("id", user.getId() + "", context);
                Utils.setSpData("user_id", user.getAuth0_user_id(), context);
                Utils.setSpData("balance", user.getBalance() + "", context);
                Utils.setSpData("name", user.getProfile().getName(), context);
                Utils.setSpData("picture", user.getProfile().getPicture(), context);
                Utils.setSpData("social_link", user.getProfile().getSocial_link(), context);

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_buycoins_balance.setText(Utils.getSpData("balance", context));
                    }
                });

            }

            @Override
            public void error(int requestCode, String message) {
            }

            @Override
            public void failure(Exception exception) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BuyCoinPager");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BuyCoinPager");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.stopService(new Intent(context, PayPalService.class));
    }
}
