package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.mol.payment.MOLConst;
import com.mol.payment.MOLPayment;
import com.mol.payment.PaymentListener;
import com.payssion.android.sdk.PayssionActivity;
import com.payssion.android.sdk.model.PayRequest;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.BuyCoinsMethodAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.BuyCoinsMethodBean;
import net.iwantbuyer.bean.MolBean;
import net.iwantbuyer.bean.PayssionBean;
import net.iwantbuyer.bean.PayssionIdBean;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {
    private static final int RESULT_PAYMENT_OK = 1001;       //google pay回调监听
    private boolean flag;

    //网络银行
    private RelativeLayout rl_buycoins_inbank;
    private ImageView iv_buycoins_inbank;
    private RecyclerView rv_buycoins_inbank;

    //mol
    private RelativeLayout rl_buycoins_carrier;
    private ImageView iv_buycoins_carrier;
    private RecyclerView rv_buycoins_carrer;

    //E-wallet
    private RelativeLayout rl_buycoins_wallet;
    private ImageView iv_buycoins_wallet;
    private RecyclerView rv_buycoins_wallet;

    private RecyclerView rv_buycoins_amount;

    public LinearLayout ll_buycoins_back;
    private ImageView iv_help;

    private View inflate;
    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    public IabHelper mHelper;                                     //谷歌支付
    public AlertDialog show;

    String country = "MY";
    String currency = "MYR";

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);
        flag = context instanceof SecondPagerActivity;
        findView();

        //判断是那个服务器
        Log.e("TAG_server", Utils.getSpData("service", context));
        if (Utils.getSpData("service", context) != null && Utils.getSpData("service", context).contains("api-my")) {
            country = "MY";
            currency = "MYR";
        } else if (Utils.getSpData("service", context) != null && Utils.getSpData("service", context).contains("api-sg")) {
            country = "AE";
            currency = "AED";
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
            }
        });

        //AppFlyer 埋点
        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context, "PAGE:topup", eventValue);
        MOLPayment.setTestMode(true);

        setView();

        return inflate;
    }

    //设置试图
    private void setView() {
        if (!flag) {
            ll_buycoins_back.setVisibility(View.GONE);
        }
        Gson gson = new Gson();
        String method = Utils.getSpData("paymentmethod",context);
        Log.e("TAG——method", method);
        BuyCoinsMethodBean buyCoinsMethodBean = null;
        if(method != null) {
            buyCoinsMethodBean = gson.fromJson(method, BuyCoinsMethodBean.class);
        }
        BuyCoinsMethodAdapter buyCoinsMethodBank = new BuyCoinsMethodAdapter(context, buyCoinsMethodBean.getBank());
        rv_buycoins_inbank.setAdapter(buyCoinsMethodBank);
        rv_buycoins_inbank.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsMethodBank.setBuyCoinMethodOnClickListener(new MyBuyCoinMethodOnClickListener());

        BuyCoinsMethodAdapter buyCoinsMethodMol = new BuyCoinsMethodAdapter(context, buyCoinsMethodBean.getMol());
        rv_buycoins_carrer.setAdapter(buyCoinsMethodMol);
        rv_buycoins_carrer.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsMethodMol.setBuyCoinMethodOnClickListener(new MyBuyCoinMethodOnClickListener());

        BuyCoinsMethodAdapter buyCoinsMethodCash = new BuyCoinsMethodAdapter(context, buyCoinsMethodBean.getCash());
        rv_buycoins_wallet.setAdapter(buyCoinsMethodCash);
        rv_buycoins_wallet.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsMethodCash.setBuyCoinMethodOnClickListener(new MyBuyCoinMethodOnClickListener());
    }

    class MyBuyCoinMethodOnClickListener implements BuyCoinsMethodAdapter.BuyCoinMethodOnClickListener {
        @Override
        public void onClick(View view, String method) {
            Log.e("TAG_method", method);
        }
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
        ll_buycoins_back = (LinearLayout) inflate.findViewById(R.id.ll_buycoins_back);
        iv_help = (ImageView) inflate.findViewById(R.id.iv_help);

        rl_buycoins_inbank = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_inbank);
        iv_buycoins_inbank = (ImageView) inflate.findViewById(R.id.iv_buycoins_inbank);
        rv_buycoins_inbank = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_inbank);
        rl_buycoins_carrier = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_carrier);
        iv_buycoins_carrier = (ImageView) inflate.findViewById(R.id.iv_buycoins_carrier);
        rv_buycoins_carrer = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_carrer);
        rl_buycoins_wallet = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_wallet);
        iv_buycoins_wallet = (ImageView) inflate.findViewById(R.id.iv_buycoins_wallet);
        rv_buycoins_wallet = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_wallet);

        rv_buycoins_amount = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_amount);


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);


        //设置监听
        ll_buycoins_back.setOnClickListener(new MyOnClickListener());
        iv_help.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());

        rl_buycoins_inbank.setOnClickListener(new MyOnClickListener());
        rl_buycoins_carrier.setOnClickListener(new MyOnClickListener());
        rl_buycoins_wallet.setOnClickListener(new MyOnClickListener());

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


    int topup_option_id = -1;
    String payKey = "";
    String icon_discribe = "";

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"buycoins\":" + response + "}";
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
                    if (context instanceof SecondPagerActivity) {
                        ((SecondPagerActivity) context).fragmentManager.popBackStack();
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
                        intent.putExtra("from", "helppager");
                        startActivity(intent);
                    }
                    break;
                case R.id.rl_buycoins_inbank:
                    if(rv_buycoins_inbank.getVisibility() != View.VISIBLE) {
                        if(rv_buycoins_carrer.getVisibility() == View.VISIBLE) {
                            iv_buycoins_carrier.clearAnimation();
//                            rotateAnim(iv_buycoins_carrier,-90f);
                        }else if(rv_buycoins_wallet.getVisibility() == View.VISIBLE) {
                            iv_buycoins_wallet.clearAnimation();
//                            rotateAnim(iv_buycoins_wallet,-90f);
                        }
                        rv_buycoins_inbank.setVisibility(View.VISIBLE);
                        rv_buycoins_carrer.setVisibility(View.GONE);
                        rv_buycoins_wallet.setVisibility(View.GONE);
                        rotateAnim(iv_buycoins_inbank,90f);
                    }
                    break;
                case R.id.rl_buycoins_carrier:
                    if(rv_buycoins_carrer.getVisibility() != View.VISIBLE) {
                        if(rv_buycoins_inbank.getVisibility() == View.VISIBLE) {
                            iv_buycoins_inbank.clearAnimation();
//                            rotateAnim(iv_buycoins_inbank,-90f);
                        }else if(rv_buycoins_wallet.getVisibility() == View.VISIBLE) {
                            iv_buycoins_wallet.clearAnimation();
//                            rotateAnim(iv_buycoins_wallet,-90f);
                        }
                        rv_buycoins_carrer.setVisibility(View.VISIBLE);
                        rv_buycoins_inbank.setVisibility(View.GONE);
                        rv_buycoins_wallet.setVisibility(View.GONE);
                        rotateAnim(iv_buycoins_carrier,90f);
                    }
                    break;
                case R.id.rl_buycoins_wallet:
                    if(rv_buycoins_wallet.getVisibility() != View.VISIBLE) {
                        if(rv_buycoins_inbank.getVisibility() == View.VISIBLE) {
                            iv_buycoins_inbank.clearAnimation();
//                            rotateAnim(iv_buycoins_inbank,-90f);
                        }else if(rv_buycoins_carrer.getVisibility() == View.VISIBLE) {
                            iv_buycoins_carrier.clearAnimation();
//                            rotateAnim(iv_buycoins_carrier,-90f);
                        }
                        rv_buycoins_carrer.setVisibility(View.GONE);
                        rv_buycoins_inbank.setVisibility(View.GONE);
                        rv_buycoins_wallet.setVisibility(View.VISIBLE);
                        rotateAnim(iv_buycoins_wallet,90f);
                    }
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
        show.getWindow().setLayout(3 * screenWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


    private void startPayssion(final String payssionM) {

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

    //进行payssion支付
    private void processPayssion(String response, String method) {
        Gson gson = new Gson();
        PayssionIdBean payssionIdBean = gson.fromJson(response, PayssionIdBean.class);

        Intent intent = new Intent(context, PayssionActivity.class);
        intent.putExtra(PayssionActivity.ACTION_REQUEST,
                new PayRequest()
                        .setLiveMode(true) //false if you are using sandbox environment
                        .setAPIKey("82323460c3e4a7ab") //Your API Key
                        .setAmount(payssionIdBean.getAmount())
                        .setCurrency(payssionIdBean.getCurrency())
                        .setPMId(method)
                        .setDescription("luckybuyer")
                        .setOrderId(payssionIdBean.getOrder_id()) //Your order id
                        .setSecretKey("32be922cd8d27d5717cbe313a6493964"));
//                        .setPayerEmail("example@demo.com")
//                        .setPayerName("example name"));
        if (context instanceof SecondPagerActivity) {
            ((SecondPagerActivity) context).startActivityForResult(intent, 0);
        } else {
            ((MainActivity) context).startActivityForResult(intent, 0);
        }

    }

    private final static String Secret_Key = "bNCnV1CJ5nyRDNX45YMAel6wynuOEGsm";
    private final static String Application_Code = "wqfQDcfm5Q1ugqF45GpazO6Z1QXrhgVP";

    private void startMol() {

        String url = MyApplication.url + "/v1/payments/mol/mobile/?timezone=" + MyApplication.url;

        String json = "{\"topup_option_id\": " + topup_option_id + "}";
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
                        Log.e("TAG_MOL", response);
                        processMol(response);
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

    //进行Mol支付
    private void processMol(String response) {
//        String Secret_Key = "Ziu61T9xY227aazS530Pk8C5424y663r";
//        String Application_Code = "3f2504e04f8911d39a0c0305e82c3301";

        Gson gson = new Gson();
        MolBean molbean = gson.fromJson(response, MolBean.class);
        MOLPayment molPayment = new MOLPayment(context, Secret_Key, Application_Code);
        Bundle inputBundle = new Bundle();
        inputBundle.putString(MOLConst.B_Key_ReferenceId, molbean.getReference_id());    // Required
        inputBundle.putLong(MOLConst.B_Key_Amount, molbean.getAmount() * 100);                        // Required
        inputBundle.putString(MOLConst.B_Key_CurrencyCode, molbean.getCurrency());                // Required
        inputBundle.putString(MOLConst.B_Key_CustomerId, Utils.getSpData("id", context) + "");        // Required
        inputBundle.putString(MOLConst.B_Key_Description, icon_discribe);    // Optional
        Log.e("TAG_mol", molbean.getReference_id());
        try {
            molPayment.pay(context, inputBundle, new PaymentListener() {
                @Override
                public void onBack(int action, Bundle outputBundle) {
                    // TODO Auto-generated method stub
                    if ("A10000".equals(outputBundle.get("result"))) {
                        startAlert(true);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private String getMethodName(String meth) {
        String methed = "";
        switch (meth) {
            case "android-inapp":
                methed = "Google Play";
                break;
            case "fpx_my":
                methed = "Myclear FPX";
                break;
            case "halopay":
                methed = "Other payment";
                break;
            case "mol_wallet":
                methed = "MOLWallet";
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
            case "digi_my":
                methed = "DIGI";
                break;
            case "maxis_my":
                methed = "Maxis";
                break;
            case "celcom_my":
                methed = "Celcom";
                break;
            case "paypal":
                methed = "paypal";
                break;
        }

        return methed;
    }

    public void rotateAnim(ImageView imageview,float f) {
        Animation anim =new RotateAnimation(0f, f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        imageview.startAnimation(anim);
    }
}
