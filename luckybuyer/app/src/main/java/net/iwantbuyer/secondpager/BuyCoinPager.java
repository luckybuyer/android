package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.halopay.interfaces.callback.IPayResultCallback;
import com.halopay.sdk.main.HaloPay;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.BuyCoinAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.bean.HaloPayBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.util.Purchase;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

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
    private ImageView iv_buycoins_back;
    private TextView tv_buycoins_back;
    public TextView tv_buycoins_balance;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;
    private RecyclerView rv_buycoins;

    private ImageView iv_buycoins_paypal;
    private ImageView iv_buycoins_cashu;
    private ImageView iv_buycoins_goole;
    private RelativeLayout rl_buycoins_cashu;
    private RelativeLayout rl_buycoins_paypal;
    private RelativeLayout rl_buycoins_google;
    private boolean flag;
    private RelativeLayout rl_buycoins_ok;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private TextView rl_buycoins_email;                    //发送邮件
    private BuyCoinBean buyCoinBean;

    public IabHelper mHelper;                                     //谷歌支付

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
            iv_buycoins_goole.setVisibility(View.VISIBLE);
            iv_buycoins_goole.setHovered(true);

        }
        if (method.contains("paypal")) {
            rl_buycoins_paypal.setVisibility(View.VISIBLE);
            iv_buycoins_paypal.setVisibility(View.VISIBLE);
            iv_buycoins_goole.setVisibility(View.GONE);
            iv_buycoins_paypal.setHovered(true);
            iv_buycoins_goole.setHovered(false);
        }

        if (method.contains("halopay")) {
            rl_buycoins_cashu.setVisibility(View.VISIBLE);
            iv_buycoins_cashu.setVisibility(View.GONE);
        }

        Log.e("TAG", iv_buycoins_goole.isHovered() + "00" + iv_buycoins_paypal.isHovered() + iv_buycoins_cashu.isHovered());
        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track("PAGE:buy_coins", props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

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
                        StartAlertDialog(view);
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
        iv_buycoins_back = (ImageView) inflate.findViewById(R.id.iv_buycoins_back);
        tv_buycoins_back = (TextView) inflate.findViewById(R.id.tv_buycoins_back);
        tv_buycoins_balance = (TextView) inflate.findViewById(R.id.tv_buycoins_balance);
        tv_buycoins_count = (TextView) inflate.findViewById(R.id.tv_buycoins_count);
        tv_buycoins_buy = (TextView) inflate.findViewById(R.id.tv_buycoins_buy);
        rv_buycoins = (RecyclerView) inflate.findViewById(R.id.rv_buycoins);

        iv_buycoins_paypal = (ImageView) inflate.findViewById(R.id.iv_buycoins_paypal);
        iv_buycoins_cashu = (ImageView) inflate.findViewById(R.id.iv_buycoins_cashu);
        iv_buycoins_goole = (ImageView) inflate.findViewById(R.id.iv_buycoins_goole);
        rl_buycoins_cashu = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_cashu);
        rl_buycoins_paypal = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_paypal);
        rl_buycoins_google = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_google);
        rl_buycoins_email = (TextView) inflate.findViewById(R.id.rl_buycoins_email);


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);


        //设置监听
        iv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_buy.setOnClickListener(new MyOnClickListener());
        rl_buycoins_cashu.setOnClickListener(new MyOnClickListener());
        rl_buycoins_paypal.setOnClickListener(new MyOnClickListener());
        rl_buycoins_google.setOnClickListener(new MyOnClickListener());

        tv_net_again.setOnClickListener(new MyOnClickListener());
        rl_buycoins_email.setOnClickListener(new MyOnClickListener());

        if (!flag) {
            iv_buycoins_back.setVisibility(View.GONE);
            tv_buycoins_back.setVisibility(View.GONE);
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
        if (buyCoinBean.getBuycoins().get(0).getAmount() == 1) {
            tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(0).getAmount() + " coin = " + buyCoinBean.getBuycoins().get(0).getPrice() + " USD");
        } else {
            tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(0).getAmount() + " coins = " + buyCoinBean.getBuycoins().get(0).getPrice() + " USD");
        }

        money = buyCoinBean.getBuycoins().get(0).getPrice();
        topup_option_id = buyCoinBean.getBuycoins().get(0).getId();
        buyCoinAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                double coins = buyCoinBean.getBuycoins().get(position).getAmount() * 3.6726;
                coins = ((int) (coins * 100)) / 100;
                if (buyCoinBean.getBuycoins().get(position).getAmount() == 1) {
                    tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(position).getAmount() + " coin = " + buyCoinBean.getBuycoins().get(position).getPrice() + " USD");
                } else {
                    tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(position).getAmount() + " coins = " + buyCoinBean.getBuycoins().get(position).getPrice() + " USD");
                }
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
            }
        });
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
                case R.id.iv_buycoins_back:
                    if (context instanceof SecondPagerActivity) {
                        if (((SecondPagerActivity) context).from.equals("coindetailpager")) {
                            ((SecondPagerActivity) context).switchPage(5);
                        } else if (((SecondPagerActivity) context).from.equals("productdetail")) {
                            ((SecondPagerActivity) context).switchPage(0);
                        }
                    }
                    break;
                case R.id.tv_buycoins_back:
                    if (context instanceof SecondPagerActivity) {
                        if (((SecondPagerActivity) context).from.equals("coindetailpager")) {
                            ((SecondPagerActivity) context).switchPage(5);
                        } else if (((SecondPagerActivity) context).from.equals("productdetail")) {
                            ((SecondPagerActivity) context).switchPage(0);
                        }
                    }
                    break;
                case R.id.tv_buycoins_buy:
                    //埋点
                    JSONObject props = new JSONObject();
                    String token_s = Utils.getSpData("token_num", context);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }
                    Log.e("TAG", iv_buycoins_goole.isHovered() + "00" + iv_buycoins_paypal.isHovered() + iv_buycoins_cashu.isHovered());
                    if (token > System.currentTimeMillis() / 1000) {
                        Log.e("TAG", iv_buycoins_goole.isHovered() + "00" + iv_buycoins_paypal.isHovered() + iv_buycoins_cashu.isHovered());
                        if (iv_buycoins_paypal.isHovered()) {
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
                        } else if (iv_buycoins_cashu.isHovered()) {
                            StartHalopay();
                            try {
                                props.put("%method", "halopay");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (iv_buycoins_goole.isHovered()) {
                            startRegistered();
                            try {
                                props.put("%method", "google_pay");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        } else if (context instanceof MainActivity) {
                            context.startActivity(((MainActivity) context).lock.newIntent(((MainActivity) context)));
                        }
                    }

                    break;
                case R.id.rl_buycoins_cashu:
                    iv_buycoins_cashu.setVisibility(View.VISIBLE);
                    iv_buycoins_paypal.setVisibility(View.GONE);
                    iv_buycoins_goole.setVisibility(View.GONE);
                    iv_buycoins_cashu.setHovered(true);
                    iv_buycoins_paypal.setHovered(false);
                    iv_buycoins_goole.setHovered(false);
                    setPoint("CLICK:halopay");
                    break;
                case R.id.rl_buycoins_paypal:
                    iv_buycoins_cashu.setVisibility(View.GONE);
                    iv_buycoins_paypal.setVisibility(View.VISIBLE);
                    iv_buycoins_goole.setVisibility(View.GONE);
                    iv_buycoins_cashu.setHovered(false);
                    iv_buycoins_paypal.setHovered(true);
                    iv_buycoins_goole.setHovered(false);
                    setPoint("CLICK:paypal");
                    break;
                case R.id.rl_buycoins_google:
                    iv_buycoins_cashu.setVisibility(View.GONE);
                    iv_buycoins_paypal.setVisibility(View.GONE);
                    iv_buycoins_goole.setVisibility(View.VISIBLE);
                    iv_buycoins_cashu.setHovered(false);
                    iv_buycoins_paypal.setHovered(false);
                    iv_buycoins_goole.setHovered(true);
                    setPoint("CLICK:google_pay");
                    break;
                case R.id.rl_buycoins_ok:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_net_again:
                    initData();
                    break;
                case R.id.rl_buycoins_email:
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:contact@luckybuyer.net"));
                    startActivity(data);
                    break;
                case R.id.tv_buycoins_success:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
            }
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
                    break;
                default:
                    Log.e("TAG", resultInfo);
                    Log.e("TAG", resultCode + "");
                    Log.e("TAG", signvalue + "");
                    break;
            }
        }
    }

    public AlertDialog show;

    private void StartAlertDialog(View view) {
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
            Log.e("paymentExample", "The user canceled.");
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
                        Utils.MyToast(context, response);
                        View view = View.inflate(context, R.layout.alertdialog_buycoins_success, null);
                        TextView tv_buycoins_success = (TextView) view.findViewById(R.id.tv_buycoins_success);
                        tv_buycoins_success.setOnClickListener(new MyOnClickListener());
                        StartAlertDialog(view);

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
    public void onDestroy() {
        super.onDestroy();
        context.stopService(new Intent(context, PayPalService.class));
    }


}
