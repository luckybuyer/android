package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.halopay.androidlocation.NetworkLocationManager;
import com.halopay.interfaces.callback.IPayResultCallback;
import com.halopay.sdk.main.HaloPay;
import com.mol.payment.MOLConst;
import com.mol.payment.MOLPayment;
import com.mol.payment.PaymentListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.payssion.android.sdk.PayssionActivity;
import com.payssion.android.sdk.model.PayRequest;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.BuyCoinAdapter;
import net.iwantbuyer.adapter.BuyCoinMethodAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.bean.BuyCoinsMethodBean;
import net.iwantbuyer.bean.CashuBean;
import net.iwantbuyer.bean.HaloPayBean;
import net.iwantbuyer.bean.PayssionBean;
import net.iwantbuyer.bean.PayssionIdBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {
    private static final int RESULT_PAYMENT_OK = 1001;       //google pay回调监听
    private static PayPalConfiguration config = new PayPalConfiguration()
            // 沙盒测试(ENVIRONMENT_SANDBOX)，生产环境(ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .merchantName("KangQian")
            //你创建的测试应用Client ID  
            .clientId("Adzkv-hUMlgx54740-FtAa-crh3rG7r2V-6Ezvx5cOOyODxl1wbF_ZC1Ups9EzkHJWGd5aQWk4iSCVjf");     //正式
//            .clientId("AfRWBJE7IMMtA0PeZ-fNA4VNKHQ96OzEi8zQhWiU62isFIK7u839fulj1HlwC1xQrl0PB3S4Sxfv-v_v");


    private RelativeLayout rl_buycoins_header;
    public LinearLayout ll_buycoins_back;
    private ImageView iv_help;
    public TextView tv_buycoins_balance;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;
    private RecyclerView rv_buycoins;
    public TextView tv_title;                      //标题

    private boolean flag;
    private RelativeLayout rl_buycoins_ok;
    private ProgressBar pb_buycoins_progress;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;
    private RelativeLayout rl_buycoins_google;
    private RelativeLayout rl_buycoins_method;
    private ImageView iv_buycoins_method_pay;
    private TextView tv_buycoins_method;

    private BuyCoinBean buyCoinBean;

    public WebView wv_buycoins_cashu;

    public IabHelper mHelper;                                     //谷歌支付

    public AlertDialog show;

    List list;
    private String view;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);

        flag = context instanceof SecondPagerActivity;

        findView();

        //paypal支付
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        context.startService(intent);

        String country = "AE";
        String currency = "AED";
        //判断是那个服务器
        Log.e("TAG_server", Utils.getSpData("service", context));
        if (Utils.getSpData("service", context) != null && Utils.getSpData("service", context).contains("api-my")) {
            country = "MY";
            currency = "MYR";
        } else if (Utils.getSpData("service", context) != null && Utils.getSpData("service", context).contains("api-sg")) {
            country = "AE";
            currency = "AED";
        }
        //holypay  支付
        if (flag) {
            HaloPay.getInstance().init(((SecondPagerActivity) context), HaloPay.PORTRAIT, "3000600753");                 //holypay支付
            HaloPay.getInstance().setLang(((SecondPagerActivity) context), country, currency, "EN");
        } else {
            HaloPay.getInstance().init(((MainActivity) context), HaloPay.PORTRAIT, "3000600753");                 //holypay支付
            HaloPay.getInstance().setLang(((MainActivity) context), country, currency, "EN");
        }

        //谷歌支付初始化
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiIp/UfmHgk42bRgx2E2HngH3m5iD4tBkqF98E4nO2x2Pw+bqnHCCNYVNerjdwuN3Djc0XKCC5OHKqpzpscC5hHPxSOGTDTfjN9f6i8hMeC19fuQt2J86nty8loCkP6R3FOsfI8XTXtolUaLhekVSaiZp2aE3aMlaMyggeNUclVh/PA4yhLIEY12f//upuA/gbr+8RtyQRxa0rVCkIWLP8dZI32tfLKxDYGCIBfssTKgagL+NdMqBYaQXq5rmwV2DSvTIFEfOjs99JkQYZCezgmDSJWYQtqhZ0lpUsOX9D5IlCyfp7NnVXxG6FUiAAYu4ZpkHLYE4tGFrN9YoQZ60BwIDAQAB";
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

//        //判断打开哪些方法
        String meth = Utils.getSpData("paymentmethod", context);
        if (meth.equals("android-inapp")) {
            rl_buycoins_google.setVisibility(View.VISIBLE);
            rl_buycoins_method.setVisibility(View.GONE);
        } else {
            rl_buycoins_google.setVisibility(View.GONE);
            rl_buycoins_method.setVisibility(View.VISIBLE);
        }

        if (Utils.getSpData("buymethod", context) == null) {
            method = "android-inapp";
        } else {
            method = Utils.getSpData("buymethod", context);
        }

        setView(Utils.getSpData("buymethod", context));
        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track("PAGE:buy_coins", props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        //AppFlyer 埋点
        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context, "PAGE:topup", eventValue);
        MOLPayment.setTestMode(true);
//        processMol("string");

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
        Map map = new HashMap();
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.length() > 10) {
                            processData(response);
                            rl_keepout.setVisibility(View.GONE);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                            rl_loading.setVisibility(View.GONE);
                        }
                        View view = View.inflate(context, R.layout.alertdialog_buycoins, null);
//                        rl_buycoins_ok = (RelativeLayout) view.findViewById(R.id.rl_buycoins_ok);
//                        rl_buycoins_ok.setOnClickListener(new MyOnClickListener());
//                        show = StartAlertDialog(view);
                    }
                });
            }

            @Override
            public void error(final int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);

                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode);


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
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
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

        wv_buycoins_cashu = (WebView) inflate.findViewById(R.id.wv_buycoins_cashu);
        pb_buycoins_progress = (ProgressBar) inflate.findViewById(R.id.pb_buycoins_progress);
//        rv_buycoins_methods = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_methods);
        rl_buycoins_method = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_method);
        iv_buycoins_method_pay = (ImageView) inflate.findViewById(R.id.iv_buycoins_method_pay);
        tv_buycoins_method = (TextView) inflate.findViewById(R.id.tv_buycoins_method);


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        rl_buycoins_google = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_google);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);


        //设置监听
        ll_buycoins_back.setOnClickListener(new MyOnClickListener());
        iv_help.setOnClickListener(new MyOnClickListener());
        tv_buycoins_buy.setOnClickListener(new MyOnClickListener());
        rl_buycoins_method.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
        rl_buycoins_google.setOnClickListener(new MyOnClickListener());

        if (!flag) {
            ll_buycoins_back.setVisibility(View.GONE);
        }
    }

    //开始google play支付
    private void startRegistered() {
        try {
            if (flag) {
                mHelper.flagEndAsync();
                mHelper.launchPurchaseFlow(((SecondPagerActivity) context), payKey + "", RESULT_PAYMENT_OK, ((SecondPagerActivity) context).mPurchaseFinishedListener);
//                mHelper.launchPurchaseFlow(((SecondPagerActivity) context), "test_two", RESULT_PAYMENT_OK, mPurchaseFinishedListener);
            } else {
                mHelper.flagEndAsync();
                mHelper.launchPurchaseFlow(((MainActivity) context), payKey + "", RESULT_PAYMENT_OK, ((MainActivity) context).mPurchaseFinishedListener);
//                mHelper.launchPurchaseFlow(((MainActivity) context), "test_two", RESULT_PAYMENT_OK, mPurchaseFinishedListener);
            }
        } catch (Exception e) {
            Utils.MyToast(context, "Sorry, temporarily unable to make Google payments");
//            e.printStackTrace();
            Log.e("TAG+错误", e + "");

        }
    }


    int money = 0;
    int topup_option_id = -1;
    String payKey = "";
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

        //判断服务器更改文案
        if (Utils.getSpData("service", context) != null && Utils.getSpData("service", context).contains("api-my")) {
            tv_buycoins_count.setText("RM " + buyCoinBean.getBuycoins().get(0).getPrice());
        } else {
            tv_buycoins_count.setText("$" + buyCoinBean.getBuycoins().get(0).getPrice());
        }


        money = buyCoinBean.getBuycoins().get(0).getPrice();
//        topup_option_id = buyCoinBean.getBuycoins().get(0).getId();
        topup_option_id = buyCoinBean.getBuycoins().get(0).getId();
        payKey = buyCoinBean.getBuycoins().get(0).getAndroid_product_id();
        Log.e("TAG_paykey", payKey);
        buyCoinAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                double coins = buyCoinBean.getBuycoins().get(position).getAmount() * 3.6726;
                coins = ((int) (coins * 100)) / 100;
//                tv_buycoins_count.setText("$" + buyCoinBean.getBuycoins().get(position).getPrice());
                if (Utils.getSpData("service", context) != null && Utils.getSpData("service", context).contains("api-my")) {
                    tv_buycoins_count.setText("RM " + buyCoinBean.getBuycoins().get(position).getPrice());
                } else {
                    tv_buycoins_count.setText("$" + buyCoinBean.getBuycoins().get(position).getPrice());
                }

                money = buyCoinBean.getBuycoins().get(position).getPrice();
                topup_option_id = buyCoinBean.getBuycoins().get(position).getId();
                payKey = buyCoinBean.getBuycoins().get(position).getAndroid_product_id();
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
                //AppFlyer 埋点
                setAppflyerPoint(coin);
            }
        });
    }

    public void setAppflyerPoint(String coins) {
        //AppFlyer 埋点
        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context, coins, eventValue);
    }

    public void setView(String methods) {
        if (methods == null) {
            return;
        }
        if (methods.equals("android-inapp")) {
            iv_buycoins_method_pay.setBackgroundResource(R.drawable.buycoins_google);
        } else {
            try {
                Field field = R.drawable.class.getField("buycoins_" + methods);
                int i = field.getInt(new R.drawable());
                iv_buycoins_method_pay.setBackgroundResource(i);
//            Log.e("TAG", image + "");
            } catch (Exception e) {

            }
        }
        String str = getMethodName(methods + "");
        tv_buycoins_method.setText(str);
    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_buycoins_back:
                    if ("buycoins".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).finish();
                        break;
                    }
                    if (wv_buycoins_cashu.getVisibility() == View.VISIBLE) {
                        wv_buycoins_cashu.setVisibility(View.GONE);
                        tv_title.setText(context.getString(R.string.BuyCoins));
                    } else if (context instanceof SecondPagerActivity) {
                        ((SecondPagerActivity) context).fragmentManager.popBackStack();
                    }
                    break;
                case R.id.tv_buycoins_buy:
                    //埋点
                    JSONObject props = new JSONObject();

                    //AppFlyer
                    Map<String, Object> eventValue = new HashMap<String, Object>();


                    String token_s = Utils.getSpData("token_num", context);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }

                    if (token > System.currentTimeMillis() / 1000) {
                        if ("paypal".equals(method)) {
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

                            eventValue.put("%method", "paypal");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup", eventValue);
                        } else if ("halopay".equals(method)) {
                            StartHalopay();
                            try {
                                props.put("%method", "halopay");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            eventValue.put("%method", "halopay");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup", eventValue);
                        } else if ("android-inapp".equals(method)) {
                            startRegistered();
                            try {
                                props.put("%method", "google_pay");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            eventValue.put("%method", "google_pay");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup", eventValue);
                        } else if ("cashu".equals(method)) {
                            startCashu();
                            try {
                                props.put("%method", "cashu");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            eventValue.put("%method", "cashu");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup", eventValue);
                        } else if ("visa".equals(method)) {
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

                            eventValue.put("%method", "Master or Visa");
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup", eventValue);
                        } else if (method != null && method.contains("_")) {
                            startPayssion(method);
                            eventValue.put("%method", method);
                            AppsFlyerLib.getInstance().trackEvent(context, "CLICK: topup", eventValue);
                        }

                        //埋点
                        MyApplication.mixpanel.track("CLICK:buy", props);

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
                            AppsFlyerLib.getInstance().trackEvent(context, "Page：Login", eventValue);
                        } else if (context instanceof MainActivity) {
                            context.startActivity(((MainActivity) context).lock.newIntent(((MainActivity) context)));
                            //AppFlyer 埋点
                            eventValue = new HashMap<String, Object>();
                            AppsFlyerLib.getInstance().trackEvent(context, "Page：Login", eventValue);
                        }
                    }

                    break;
//                case R.id.rl_buycoins_ok:
//                    if (show != null && show.isShowing()) {
//                        show.dismiss();
//                    }
//                    break;
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
                        intent.putExtra("from", "helppager");
                        startActivity(intent);
                    }
                    break;
                case R.id.iv_buycoins_close:
                    if (ppw != null && ppw.isShowing()) {
                        ppw.dismiss();
                    }
                    break;
                case R.id.rl_buycoins_method:
                    startPPW();
                    break;
            }
        }
    }

    String method;

    //点击支付方法
    class MyBuyMethodcoinsListener implements BuyCoinMethodAdapter.BuyCoinMethodsOnClickListener {

        @Override
        public void onClick(View view, String method) {
            BuyCoinPager.this.method = method;
            if ("android-inapp".equals(method)) {
                iv_buycoins_method_pay.setBackgroundResource(R.drawable.buycoins_google);
            } else {
                try {
                    Field field = R.drawable.class.getField("buycoins_" + method);
                    int i = field.getInt(new R.drawable());
                    iv_buycoins_method_pay.setBackgroundResource(i);
                } catch (Exception e) {

                }
            }
            if (ppw != null && ppw.isShowing()) {
                ppw.dismiss();
            }
            String str = getMethodName(method + "");
            tv_buycoins_method.setText(str);
            Utils.setSpData("buymethod", method, context);
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
        tv_buycoins_buy.setEnabled(false);
        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.VISIBLE);
        tv_buycoins_buy.setBackgroundResource(R.color.D12F1D);
        String url = MyApplication.url + "/v1/payments/halopay/?timezone=" + MyApplication.utc;
//        String json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": \"alipay_cn\",\"success_url\": \"http://net.luckybuyer.success\",\"topup_option_id\": " + topup_option_id + "}";
        String json = "{ \"topup_option_id\": " + topup_option_id + "}";
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-CLIENT-TYPE", "appsflyer_android");
        map.put("LK-APP-ID", "net.iwantbuyer");
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processHalopay(response);
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                    }
                });
            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                });
            }
        });
    }

    private void processHalopay(String response) {
        Gson gson = new Gson();
        HaloPayBean haloPayBean = gson.fromJson(response, HaloPayBean.class);
        if (flag) {
            HaloPay.getInstance().startPay(((SecondPagerActivity) context), "transid=" + haloPayBean.getTransaction_id() + "&appid=3000600753", new MyIPayResultCallback());
        } else {
            HaloPay.getInstance().startPay((MainActivity) context, "transid=" + haloPayBean.getTransaction_id() + "&appid=3000600753", new MyIPayResultCallback());
        }
    }

    //halopay 支付 回调
    class MyIPayResultCallback implements IPayResultCallback {

        @Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            Log.e("TAG_halopay", resultCode + signvalue + resultInfo);
            switch (resultCode) {
                case HaloPay.PAY_SUCCESS:
                    requestCoins();
                    startAlert(true);
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
        if (purchaseData == null || dataSignature == null) {
            return;
        }

        startAlert(true);

        String url = MyApplication.url + "/v1/payments/android-inapp/?timezone=" + MyApplication.utc;
        Log.e("TAG_google", url);
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
        map.put("LK-CLIENT-TYPE", "appsflyer_android");
        map.put("LK-APP-ID", "net.iwantbuyer");
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
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
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                });
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
        map.put("LK-CLIENT-TYPE", "appsflyer_android");
        map.put("LK-APP-ID", "net.iwantbuyer");
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        Log.e("TAG_paypal", json);
        Log.e("TAG_paypal", url);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_paypal", response);
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
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                });
            }
        });
    }

    private void requestCoins() {
        String token = Utils.getSpData("token", context);
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
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

    private void startPayssion(final String payssionM) {
        tv_buycoins_buy.setEnabled(false);
        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.VISIBLE);
        tv_buycoins_buy.setBackgroundResource(R.color.D12F1D);

        String url = MyApplication.url + "/v1/payments/payssion/mobile/?timezone=" + MyApplication.utc;

        PayssionBean payssionBean = new PayssionBean(payssionM, topup_option_id);
        String json = payssionBean.toString();
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-CLIENT-TYPE", "appsflyer_android");
        map.put("LK-APP-ID", "net.iwantbuyer");
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPayssion(response, payssionM);
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                    }


                });
            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_payssion", code + message);
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                });
            }
        });
    }

    //进行payssion支付
    private void processPayssion(String response, String method) {
        Gson gson = new Gson();
        PayssionIdBean payssionIdBean = gson.fromJson(response, PayssionIdBean.class);

        Intent intent = new Intent(context, PayssionActivity.class);
        intent.putExtra(PayssionActivity.ACTION_REQUEST,
                new PayRequest()
                        .setLiveMode(false) //false if you are using sandbox environment
                        .setAPIKey("4ab54b3610cb2f43") //Your API Key
                        .setAmount(payssionIdBean.getAmount())
                        .setCurrency(payssionIdBean.getCurrency())
                        .setPMId(method)
                        .setDescription("luckybuyer")
                        .setOrderId(payssionIdBean.getOrder_id()) //Your order id
                        .setSecretKey("8b9361c007c2df3eeb579a0523abd851"));
//                        .setPayerEmail("example@demo.com")
//                        .setPayerName("example name"));
        if (context instanceof SecondPagerActivity) {
            ((SecondPagerActivity) context).startActivityForResult(intent, 0);
        } else {
            ((MainActivity) context).startActivityForResult(intent, 0);
        }

    }

    private final static String Secret_Key = "Ziu61T9xY227aazS530Pk8C5424y663r";
    private final static String Application_Code = "3f2504e04f8911d39a0c0305e82c3301";

    //进行Mol支付
    private void processMol(String response) {
        MOLPayment molPayment = new MOLPayment(context, Secret_Key, Application_Code);
        Bundle inputBundle = new Bundle();
        inputBundle.putString(MOLConst.B_Key_ReferenceId, "RID" + (System.currentTimeMillis() & 0xFFFFFFFFL));    // Required
        inputBundle.putLong(MOLConst.B_Key_Amount, 5000);                        // Required
        inputBundle.putString(MOLConst.B_Key_CurrencyCode, "MYR");                // Required
        inputBundle.putString(MOLConst.B_Key_CustomerId, "12321144221");        // Required
        inputBundle.putString(MOLConst.B_Key_Description, "50000 Diamond");    // Optional
        try {
            molPayment.pay(context, inputBundle, new PaymentListener() {
                @Override
                public void onBack(int action, Bundle outputBundle) {
                    // TODO Auto-generated method stub
                    showInfo(outputBundle.toString());
                }
            });
        } catch (Exception e) {
            showInfo(e.getMessage());
        }
    }

    private void showInfo(String con) {
        new AlertDialog.Builder(context)
                .setTitle("Payment Result")
                .setMessage(con)
                .setPositiveButton("OK", null).show();
    }

    private PopupWindow ppw;

    public void startPPW() {
        int width = Utils.getScreenWidth(context);
        int height = Utils.getScreenHeight(context);
        View view = View.inflate(context, R.layout.ppw_buycoins_method, null);
        if (context instanceof SecondPagerActivity) {
            ppw = Utils.startPPW(((SecondPagerActivity) context), view, width, 3 * height / 5);
        } else {
            ppw = Utils.startPPW(((MainActivity) context), view, width, 3 * height / 5);
        }
        RecyclerView rv_buycoins_method = (RecyclerView) view.findViewById(R.id.rv_buycoins_method);
        ImageView iv_buycoins_close = (ImageView) view.findViewById(R.id.iv_buycoins_close);
        iv_buycoins_close.setOnClickListener(new MyOnClickListener());
        list = new ArrayList();
        list.clear();
        //判断打开哪些方法
        String methods = Utils.getSpData("paymentmethod", context);
        if (methods != null && methods.length() > 0) {
            String[] split = methods.split("\\.");
            for (int i = 0; i < split.length; i++) {
                if (method != null && split[i].equals(method)) {
                    list.add(new BuyCoinsMethodBean(true, split[i]));
                } else {
                    list.add(new BuyCoinsMethodBean(false, split[i]));
                }

            }
        }

        BuyCoinMethodAdapter buyCoinMethodAdapter = new BuyCoinMethodAdapter(context, list);
        rv_buycoins_method.setAdapter(buyCoinMethodAdapter);
        rv_buycoins_method.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        buyCoinMethodAdapter.setBuyCoinMethodOnClickListener(new MyBuyMethodcoinsListener());
    }

    private String getMethodName(String meth) {
        String methed = "";
        switch (meth) {
            case "android-inapp":
                methed = "Google Play";
                break;
            case "halopay":
                methed = "Other payment";
                break;
            case "visa":
                methed = "Other payment";
                break;
            case "7eleven_my":
                methed = "7-eleven";
                break;
            case "affinepg_my":
                methed = "Affin Bank";
                break;
            case "amb_my":
                methed = "Am online";
                break;
            case "cimb_my":
                methed = "CIMB Clicks";
                break;
            case "epay_my":
                methed = "epay";
                break;
            case "esapay_my":
                methed = "Esapay";
                break;
            case "hlb_my":
                methed = "Hong Leong";
                break;
            case "maybank2u_my":
                methed = "Maybank2u";
                break;
            case "rhb_my":
                methed = "RHB Now";
                break;
            case "webcash_my":
                methed = "Webcash";
                break;
        }

        return methed;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.stopService(new Intent(context, PayPalService.class));
    }

}
