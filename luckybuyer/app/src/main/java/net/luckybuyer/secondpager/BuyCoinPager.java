package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.BuyCoinAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.bean.BuyCoinBean;
import net.luckybuyer.bean.PayssionBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {
    private static PayPalConfiguration config = new PayPalConfiguration()
            // 沙盒测试(ENVIRONMENT_SANDBOX)，生产环境(ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .merchantName("Example Merchant")
            //你创建的测试应用Client ID  
            .clientId("AfRWBJE7IMMtA0PeZ-fNA4VNKHQ96OzEi8zQhWiU62isFIK7u839fulj1HlwC1xQrl0PB3S4Sxfv-v_v");


    private RelativeLayout rl_buycoins_header;
    private ImageView iv_buycoins_back;
    private TextView tv_buycoins_back;
    private TextView tv_buycoins_balance;
    private TextView tv_buycoins_rate;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;
    private RecyclerView rv_buycoins;
    public WebView wv_payssion;

    private ImageView iv_buycoins_paypal;
    private ImageView iv_buycoins_cashu;
    private boolean flag;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);

        flag = context instanceof SecondPagerActivity;
        if (context instanceof SecondPagerActivity) {
            ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
            ((SecondPagerActivity) context).from = "coindetailpager";
        }


        findView();
        setHeadMargin();
        HttpUtils.getInstance().startNetworkWaiting(context);

        //paypal支付
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        context.startService(intent);
        iv_buycoins_paypal.setHovered(true);
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        String url = MyApplication.url + "/v1/topup-options/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        processData(response);
                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
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
        tv_buycoins_rate = (TextView) inflate.findViewById(R.id.tv_buycoins_rate);
        tv_buycoins_count = (TextView) inflate.findViewById(R.id.tv_buycoins_count);
        tv_buycoins_buy = (TextView) inflate.findViewById(R.id.tv_buycoins_buy);
        rv_buycoins = (RecyclerView) inflate.findViewById(R.id.rv_buycoins);

        iv_buycoins_paypal = (ImageView) inflate.findViewById(R.id.iv_buycoins_paypal);
        iv_buycoins_cashu = (ImageView) inflate.findViewById(R.id.iv_buycoins_cashu);
        wv_payssion = (WebView) inflate.findViewById(R.id.wv_payssion);

        //设置监听
        iv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_buy.setOnClickListener(new MyOnClickListener());
        iv_buycoins_paypal.setOnClickListener(new MyOnClickListener());
        iv_buycoins_cashu.setOnClickListener(new MyOnClickListener());

        if (!flag) {
            iv_buycoins_back.setVisibility(View.GONE);
            tv_buycoins_back.setVisibility(View.GONE);
        }
        wv_payssion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    wv_payssion.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    int money = 0;
    int topup_option_id = -1;

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"buycoins\":" + response + "}";
        final BuyCoinBean buyCoinBean = gson.fromJson(response, BuyCoinBean.class);
        BuyCoinAdapter buyCoinAdapter = new BuyCoinAdapter(context, buyCoinBean.getBuycoins());
        rv_buycoins.setAdapter(buyCoinAdapter);
        rv_buycoins.setLayoutManager(new GridLayoutManager(context, 2));

        double coins = buyCoinBean.getBuycoins().get(0).getAmount() * 3.6726;
        coins = ((int) (coins * 100)) / 100;
        tv_buycoins_balance.setText("Coins balance: " + Utils.getSpData("balance", context));
        tv_buycoins_count.setText("Total:" + buyCoinBean.getBuycoins().get(0).getAmount() + "≈" + coins + "AED");
        money = buyCoinBean.getBuycoins().get(0).getPrice();
        topup_option_id = buyCoinBean.getBuycoins().get(0).getId();
        buyCoinAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                double coins = buyCoinBean.getBuycoins().get(position).getAmount() * 3.6726;
                coins = ((int) (coins * 100)) / 100;
                tv_buycoins_count.setText("Total:" + buyCoinBean.getBuycoins().get(position).getAmount() + "≈" + coins + "AED");
                money = buyCoinBean.getBuycoins().get(position).getPrice();
                topup_option_id = buyCoinBean.getBuycoins().get(position).getId();
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_buycoins_back:
                    if (context instanceof SecondPagerActivity) {
                        ((SecondPagerActivity) context).switchPage(5);
                    }
                    break;
                case R.id.tv_buycoins_back:
                    if (context instanceof SecondPagerActivity) {
                        ((SecondPagerActivity) context).switchPage(5);
                    }
                    break;
                case R.id.tv_buycoins_buy:
                    String token_s = Utils.getSpData("token_num", context);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }
                    if (token > System.currentTimeMillis() / 1000) {
                        if (iv_buycoins_paypal.isHovered()) {
                            PayPalPayment payment = new PayPalPayment(new BigDecimal(money + ""), "USD", "hipster jeans",
                                    PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(context, PaymentActivity.class);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                            startActivityForResult(intent, 0);
                        } else if (iv_buycoins_cashu.isHovered()) {
                            StartPayssion();
                        }
                    } else {
                        if (context instanceof SecondPagerActivity) {
                            context.startActivity(((SecondPagerActivity) context).lock.newIntent(((SecondPagerActivity) context)));
                        } else if (context instanceof MainActivity) {
                            context.startActivity(((MainActivity) context).lock.newIntent(((MainActivity) context)));
                        }
                    }
                    break;
                case R.id.iv_buycoins_cashu:
                    iv_buycoins_cashu.setHovered(true);
                    iv_buycoins_paypal.setHovered(false);
                    break;
                case R.id.iv_buycoins_paypal:
                    iv_buycoins_cashu.setHovered(false);
                    iv_buycoins_paypal.setHovered(true);
                    break;
            }
        }
    }

    //paysession 支付
    private void StartPayssion() {
        String url = MyApplication.url + "/v1/payments/payssion/?timezone=" + MyApplication.utc;
        String json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": \"alipay_cn\",\"success_url\": \"http://net.luckybuyer.success\",\"topup_option_id\": " + topup_option_id + "}";
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPayssion(response);
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

    private void processPayssion(String response) {
        Gson gson = new Gson();
        PayssionBean payssionBean = gson.fromJson(response, PayssionBean.class);
        String payment_url = payssionBean.getPayment_url();
        wv_payssion.setVisibility(View.VISIBLE);
        wv_payssion.getSettings().setJavaScriptEnabled(true);
        wv_payssion.loadUrl(payment_url);

        wv_payssion.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Log.e("TAG", url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("TAG_finished", url);
            }
        });

        wv_payssion.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e("TAG", newProgress + "  ");
            }
        });
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
                    // TODO: 发送支付ID到你的服务器进行验证  

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.stopService(new Intent(context, PayPalService.class));
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 38));
        lp.topMargin = sbar;
        rl_buycoins_header.setLayoutParams(lp);
    }

}
