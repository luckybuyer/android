package net.luckybuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.BuyCoinAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.bean.BuyCoinBean;
import net.luckybuyer.bean.HaloPayBean;
import net.luckybuyer.bean.PaypalSuccessBean;
import net.luckybuyer.bean.User;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {
    private static PayPalConfiguration config = new PayPalConfiguration()
            // 沙盒测试(ENVIRONMENT_SANDBOX)，生产环境(ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .merchantName("nihao")
            //你创建的测试应用Client ID  
            .clientId("Adzkv-hUMlgx54740-FtAa-crh3rG7r2V-6Ezvx5cOOyODxl1wbF_ZC1Ups9EzkHJWGd5aQWk4iSCVjf");


    private RelativeLayout rl_buycoins_header;
    private ImageView iv_buycoins_back;
    private TextView tv_buycoins_back;
    private TextView tv_buycoins_balance;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;
    private RecyclerView rv_buycoins;

    private ImageView iv_buycoins_paypal;
    private ImageView iv_buycoins_cashu;
    private RelativeLayout rl_buycoins_cashu;
    private RelativeLayout rl_buycoins_paypal;
    private boolean flag;
    private RelativeLayout rl_buycoins_ok;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private TextView rl_buycoins_email;                    //发送邮件
    private BuyCoinBean buyCoinBean;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);

        flag = context instanceof SecondPagerActivity;
        if (context instanceof SecondPagerActivity) {
            ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
            ((SecondPagerActivity) context).from = "coindetailpager";
        }


        findView();

        //paypal支付
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        context.startService(intent);
        iv_buycoins_paypal.setVisibility(View.VISIBLE);
        iv_buycoins_paypal.setHovered(true);

        if(flag) {
            HaloPay.getInstance().init (((SecondPagerActivity)context),HaloPay.PORTRAIT, "3000600754");                 //holypay支付
                    HaloPay.getInstance().setLang(((SecondPagerActivity)context), "BR", "BRL", "PT");
        }else {
            HaloPay.getInstance().init (((MainActivity)context),HaloPay.PORTRAIT, "3000600754");                 //holypay支付
            HaloPay.getInstance().setLang(((MainActivity)context), "AE", "AED", "AR");
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
                        View view = View.inflate(context,R.layout.alertdialog_buycoins,null);
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
        rl_buycoins_cashu = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_cashu);
        rl_buycoins_paypal = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_paypal);
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
        tv_net_again.setOnClickListener(new MyOnClickListener());
        rl_buycoins_email.setOnClickListener(new MyOnClickListener());

        if (!flag) {
            iv_buycoins_back.setVisibility(View.GONE);
            tv_buycoins_back.setVisibility(View.GONE);
        }
    }

    int money = 0;
    int topup_option_id = -1;
    int id_coins = 0;

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"buycoins\":" + response + "}";
        buyCoinBean = gson.fromJson(response, BuyCoinBean.class);
        BuyCoinAdapter buyCoinAdapter =  new BuyCoinAdapter(context, buyCoinBean.getBuycoins());
        rv_buycoins.setAdapter(buyCoinAdapter);
        rv_buycoins.setLayoutManager(new GridLayoutManager(context, 2));

//        double coins = buyCoinBean.getBuycoins().get(0).getAmount() * 3.6726;                                汇率
//        coins = ((int) (coins * 100)) / 100;
        tv_buycoins_balance.setText(Utils.getSpData("balance", context));
        if(buyCoinBean.getBuycoins().get(0).getAmount() == 1) {
            tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(0).getAmount() + " coin = " + buyCoinBean.getBuycoins().get(0).getPrice()+ " USD");
        }else {
            tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(0).getAmount() + " coins = " + buyCoinBean.getBuycoins().get(0).getPrice()+ " USD");
        }

        money = buyCoinBean.getBuycoins().get(0).getPrice();
        topup_option_id = buyCoinBean.getBuycoins().get(0).getId();
        buyCoinAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                double coins = buyCoinBean.getBuycoins().get(position).getAmount() * 3.6726;
                coins = ((int) (coins * 100)) / 100;
                if(buyCoinBean.getBuycoins().get(position).getAmount() == 1) {
                    tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(position).getAmount() + " coin = " + buyCoinBean.getBuycoins().get(position).getPrice()+ " USD");
                }else {
                    tv_buycoins_count.setText(buyCoinBean.getBuycoins().get(position).getAmount() + " coins = " + buyCoinBean.getBuycoins().get(position).getPrice()+ " USD");
                }
                money = buyCoinBean.getBuycoins().get(position).getPrice();
                topup_option_id = buyCoinBean.getBuycoins().get(position).getId();
                id_coins = position;
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
                            PayPalPayment payment = new PayPalPayment(new BigDecimal(money + ""), buyCoinBean.getBuycoins().get(id_coins).getCurrency(), "Luckybuyer",
                                    PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(context, PaymentActivity.class);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                            startActivityForResult(intent, 0);
                        } else if (iv_buycoins_cashu.isHovered()) {
                            StartHalopay();
                        }
                    } else {
                        if (context instanceof SecondPagerActivity) {
                            context.startActivity(((SecondPagerActivity) context).lock.newIntent(((SecondPagerActivity) context)));
                        } else if (context instanceof MainActivity) {
                            context.startActivity(((MainActivity) context).lock.newIntent(((MainActivity) context)));
                        }
                    }
                    break;
                case R.id.rl_buycoins_cashu:
                    iv_buycoins_cashu.setVisibility(View.VISIBLE);
                    iv_buycoins_paypal.setVisibility(View.GONE);
                    iv_buycoins_cashu.setHovered(true);
                    iv_buycoins_paypal.setHovered(false);
                    break;
                case R.id.rl_buycoins_paypal:
                    iv_buycoins_cashu.setHovered(false);
                    iv_buycoins_paypal.setHovered(true);
                    iv_buycoins_cashu.setVisibility(View.GONE);
                    iv_buycoins_paypal.setVisibility(View.VISIBLE);
                    break;
                case R.id.rl_buycoins_ok:
                    if(show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_net_again:
                    initData();
                    break;
                case R.id.rl_buycoins_email:
                    Intent data=new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:contact@luckybuyer.net"));
                    startActivity(data);
                    break;
                case R.id.tv_buycoins_success:
                    if(show.isShowing()) {
                        show.dismiss();
                    }
                    break;
            }
        }
    }

    //paysession 支付
    private void StartHalopay() {
        String url = MyApplication.url+"/v1/payments/halopay/?timezone=" + MyApplication.utc;
//        String json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": \"alipay_cn\",\"success_url\": \"http://net.luckybuyer.success\",\"topup_option_id\": " + topup_option_id + "}";
        String json = "{ \"topup_option_id\": "+topup_option_id+"}";
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
        if(flag) {
            HaloPay.getInstance().startPay(((SecondPagerActivity)context), "transid="+haloPayBean.getTransaction_id()+"&appid=3000600754", new MyIPayResultCallback());
        }else {
            HaloPay.getInstance().startPay((MainActivity)context, "transid="+haloPayBean.getTransaction_id()+"&appid=3000600754", new MyIPayResultCallback());
        }
    }
    class MyIPayResultCallback implements IPayResultCallback{

        @Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            switch (resultCode) {
                case HaloPay.PAY_SUCCESS:
                    requestCoins();
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

    //开始paypal 请求接口
    private void paypalSever(final String id) {
        String url = MyApplication.url + "/v1/payments/paypal/confirmed/?timezone=" + MyApplication.utc;
        String json = "{\"paypal_payment_id\": \""+id+"\",\"topup_option_id\": "+topup_option_id+"}";
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_response", response);
                        View view = View.inflate(context,R.layout.alertdialog_buycoins_success,null);
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
