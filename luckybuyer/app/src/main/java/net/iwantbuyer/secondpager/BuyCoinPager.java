package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;
import com.payssion.android.sdk.PayssionActivity;
import com.payssion.android.sdk.model.PayRequest;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.BuyCoinAdapter;
import net.iwantbuyer.adapter.BuyCoinsMethodAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.bean.BuyCoinMolBean;
import net.iwantbuyer.bean.BuyCoinsMethodBean;
import net.iwantbuyer.bean.PayssionBean;
import net.iwantbuyer.bean.PayssionIdBean;
import net.iwantbuyer.util.IabHelper;
import net.iwantbuyer.util.IabResult;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private View view_inbank;

    //mol
    private RelativeLayout rl_buycoins_carrier;
    private ImageView iv_buycoins_carrier;
    private RecyclerView rv_buycoins_carrer;
    private View view_carrier;

    //E-wallet
    private RelativeLayout rl_buycoins_wallet;
    private ImageView iv_buycoins_wallet;
    private RecyclerView rv_buycoins_wallet;
    private View view_wallet;

    private TextView tv_buycoins_amount;
    private RecyclerView rv_buycoins_amount;
    public RelativeLayout rl_buycoins_mol;
    private RelativeLayout rl_mol_header;
    private WebView wv_buycoins;
    private ProgressBar pb_buycoins_mol;

    //购买
    private RelativeLayout rl_buycoins_topup;
    private TextView tv_buycoins_buy;
    private ProgressBar pb_buycoins_topup;

    private RelativeLayout rl_buycoins_google;
    private RelativeLayout rl_buycoins_prompt;                 //点击短代的时候 提示语
    private TextView tv_buycoins_prompt;                 //点击短代的时候 提示语
    public LinearLayout ll_buycoins_back;

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

    String method;
    private BuyCoinAdapter buyCoinsAdapter;

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

        setView();

        return inflate;
    }

    BuyCoinsMethodAdapter buyCoinsMethodBank;
    BuyCoinsMethodAdapter buyCoinsMethodMol;
    BuyCoinsMethodAdapter buyCoinsMethodCash;

    //设置试图
    private void setView() {
        if (!flag) {
            ll_buycoins_back.setVisibility(View.GONE);
        }
        Gson gson = new Gson();
        String method = Utils.getSpData("paymentmethod", context);
        Log.e("TAG333", method);
        BuyCoinsMethodBean buyCoinsMethodBean = null;
        if (method != null) {
            buyCoinsMethodBean = gson.fromJson(method, BuyCoinsMethodBean.class);
        }

        Log.e("TAG3332", Utils.getSpData("method", context) + "");
        if (Utils.getSpData("method", context) != null) {
            BuyCoinPager.this.method = Utils.getSpData("method", context);
            if (BuyCoinPager.this.method.equals("mol_3") || BuyCoinPager.this.method.equals("mol_804") || BuyCoinPager.this.method.equals("mol_805") || BuyCoinPager.this.method.equals("mol_806") || BuyCoinPager.this.method.equals("mol_815")) {
                rl_buycoins_prompt.setVisibility(View.VISIBLE);
                promat(BuyCoinPager.this.method);
            } else {
                rl_buycoins_prompt.setVisibility(View.GONE);
            }
        } else {
            BuyCoinPager.this.method = "maybank2u_my";
            Utils.setSpData("method", BuyCoinPager.this.method, context);
        }

        if (buyCoinsMethodBean.getBank() == null && buyCoinsMethodBean.getMol() == null) {
            rl_buycoins_inbank.setVisibility(View.GONE);
            rv_buycoins_inbank.setVisibility(View.GONE);
            rl_buycoins_carrier.setVisibility(View.GONE);
            rv_buycoins_carrer.setVisibility(View.GONE);
            rl_buycoins_wallet.setVisibility(View.GONE);
            rv_buycoins_wallet.setVisibility(View.GONE);
            rl_buycoins_google.setVisibility(View.VISIBLE);
            BuyCoinPager.this.method = "android-inapp";
        } else {
            String useMethod = Utils.getSpData("method", context);
            if (useMethod == null) {
                rv_buycoins_inbank.setVisibility(View.VISIBLE);
                view_inbank.setVisibility(View.VISIBLE);
            } else if (buyCoinsMethodBean.getMol().contains(useMethod)) {
                rv_buycoins_carrer.setVisibility(View.VISIBLE);
                view_carrier.setVisibility(View.VISIBLE);
            } else if (buyCoinsMethodBean.getCash().contains(useMethod)) {
                rv_buycoins_wallet.setVisibility(View.VISIBLE);
                view_wallet.setVisibility(View.VISIBLE);
            } else {
                rv_buycoins_inbank.setVisibility(View.VISIBLE);
                view_inbank.setVisibility(View.VISIBLE);
            }
        }

        buyCoinsMethodBank = new BuyCoinsMethodAdapter(context, buyCoinsMethodBean.getBank());
        rv_buycoins_inbank.setAdapter(buyCoinsMethodBank);
        rv_buycoins_inbank.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsMethodBank.setBuyCoinMethodOnClickListener(new MyBuyCoinMethodOnClickListener());

        buyCoinsMethodMol = new BuyCoinsMethodAdapter(context, buyCoinsMethodBean.getMol());
        rv_buycoins_carrer.setAdapter(buyCoinsMethodMol);
        rv_buycoins_carrer.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsMethodMol.setBuyCoinMethodOnClickListener(new MyBuyCoinMethodOnClickListener());

        buyCoinsMethodCash = new BuyCoinsMethodAdapter(context, buyCoinsMethodBean.getCash());
        rv_buycoins_wallet.setAdapter(buyCoinsMethodCash);
        rv_buycoins_wallet.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsMethodCash.setBuyCoinMethodOnClickListener(new MyBuyCoinMethodOnClickListener());

    }

    public void promat(String method) {
//        String ratio = null;String coin = null;
//    if(method.equals("mol_3")) {
//        method = "Mol point";
//        ratio = "20%";
//        coin = "4";
//    }else if(method.equals("mol_804")) {
//        method = "Maxis";
//        ratio = "30%";
//        coin = "3";
//    }else if(method.equals("mol_805")) {
//        method = "Celcom";
//        ratio = "45%";
//        coin = "2";
//    }else if(method.equals("mol_806")) {
//        method = "Digi";
//        ratio = "30%";
//        coin = "3";
//    }else if(method.equals("mol_815")) {
//        method = "U Mobile";
//        ratio = "35%";
//        coin = "3";
//    }
//    String content = context.getString(R.string.promat0) + ratio + context.getString(R.string.promat1) + method + context.getString(R.string.promat2) + "5" + context.getString(R.string.promat3) + coin  + context.getString(R.string.coins);
//    SpannableStringBuilder builder = new SpannableStringBuilder(content);
//    ForegroundColorSpan redSpan = new ForegroundColorSpan(ContextCompat.getColor(context,R.color.all_orange));
//    ForegroundColorSpan redSpan1 = new ForegroundColorSpan(ContextCompat.getColor(context,R.color.all_orange));
//    ForegroundColorSpan blackSpan = new ForegroundColorSpan(ContextCompat.getColor(context,R.color.text_gray));
//    int length = (context.getString(R.string.promat0) + ratio + context.getString(R.string.promat1) + method + context.getString(R.string.promat2)).length();
//    int secondLength = (context.getString(R.string.promat0) + ratio + context.getString(R.string.promat1) + method + context.getString(R.string.promat2) + "5" + context.getString(R.string.promat3)).length();
//    builder.setSpan(blackSpan,0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    builder.setSpan(redSpan,length,length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    builder.setSpan(blackSpan,length + 1,secondLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    builder.setSpan(redSpan1,secondLength,secondLength + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    builder.setSpan(blackSpan,secondLength + 1,content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (method.equals("mol_806")) {
            tv_buycoins_prompt.setText(context.getText(R.string.digipromat));
        } else {
            tv_buycoins_prompt.setText(context.getText(R.string.promat));
        }

    }

    class MyBuyCoinMethodOnClickListener implements BuyCoinsMethodAdapter.BuyCoinMethodOnClickListener {
        @Override
        public void onClick(View view, String method) {
            Utils.setSpData("method", method, context);
            BuyCoinPager.this.method = method;
            if (method.equals("mol_3") || method.equals("mol_804") || method.equals("mol_805") || method.equals("mol_806") || method.equals("mol_815")) {
                rl_buycoins_prompt.setVisibility(View.VISIBLE);
                promat(BuyCoinPager.this.method);
            } else {
                rl_buycoins_prompt.setVisibility(View.GONE);
            }
            if (buyCoinBean != null) {
                Log.e("TAGbuyCoinBean", buyCoinBean.getBuycoins().size() + "");
                Log.e("TAGbuyCoinBean", buyCoinBean.getBuycoins().toString() + "");
                buyCoinsAdapter = new BuyCoinAdapter(context, buyCoinBean.getBuycoins(), BuyCoinPager.this.method);
                rv_buycoins_amount.setAdapter(buyCoinsAdapter);
                rv_buycoins_amount.setLayoutManager(new GridLayoutManager(context, 3));

                buyCoinsAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
                    @Override
                    public void onClick(View view, int topup_option_id) {
                        BuyCoinPager.this.topup_option_id = topup_option_id;
                        Log.e("TAGtopup_option_id", BuyCoinPager.this.topup_option_id + "");
                    }
                });

                for (int i = 0; i < buyCoinBean.getBuycoins().size(); i++) {
                    if (buyCoinBean.getBuycoins().get(i).getCategory().contains(method)) {
                        BuyCoinPager.this.topup_option_id = buyCoinBean.getBuycoins().get(i).getId();
                        return;
                    }
                }
            }
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
            public void success(final String response, String link) {
                Log.e("TAG_response.", response);
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

        rl_buycoins_inbank = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_inbank);
        iv_buycoins_inbank = (ImageView) inflate.findViewById(R.id.iv_buycoins_inbank);
        rv_buycoins_inbank = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_inbank);
        view_inbank = (View) inflate.findViewById(R.id.view_inbank);
        rl_buycoins_carrier = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_carrier);
        iv_buycoins_carrier = (ImageView) inflate.findViewById(R.id.iv_buycoins_carrier);
        rv_buycoins_carrer = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_carrer);
        view_carrier = (View) inflate.findViewById(R.id.view_carrier);
        rl_buycoins_wallet = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_wallet);
        iv_buycoins_wallet = (ImageView) inflate.findViewById(R.id.iv_buycoins_wallet);
        rv_buycoins_wallet = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_wallet);
        view_wallet = (View) inflate.findViewById(R.id.view_wallet);

        tv_buycoins_amount = (TextView) inflate.findViewById(R.id.tv_buycoins_amount);
        rv_buycoins_amount = (RecyclerView) inflate.findViewById(R.id.rv_buycoins_amount);
        rl_buycoins_mol = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_mol);
        rl_mol_header = (RelativeLayout) inflate.findViewById(R.id.rl_mol_header);
        wv_buycoins = (WebView) inflate.findViewById(R.id.wv_buycoins);
        pb_buycoins_mol = (ProgressBar) inflate.findViewById(R.id.pb_buycoins_mol);

        rl_buycoins_topup = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_topup);
        tv_buycoins_buy = (TextView) inflate.findViewById(R.id.tv_buycoins_buy);
        pb_buycoins_topup = (ProgressBar) inflate.findViewById(R.id.pb_buycoins_topup);

        rl_buycoins_google = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_google);
        rl_buycoins_prompt = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_prompt);
        tv_buycoins_prompt = (TextView) inflate.findViewById(R.id.tv_buycoins_prompt);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);


        //设置监听
        ll_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());

        rl_buycoins_inbank.setOnClickListener(new MyOnClickListener());
        rl_buycoins_carrier.setOnClickListener(new MyOnClickListener());
        rl_buycoins_wallet.setOnClickListener(new MyOnClickListener());
        rl_buycoins_topup.setOnClickListener(new MyOnClickListener());
        rl_mol_header.setOnClickListener(new MyOnClickListener());

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
    BuyCoinBean buyCoinBean;

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"buycoins\":" + response + "}";
        buyCoinBean = gson.fromJson(response, BuyCoinBean.class);
        //设置金额
        buyCoinsAdapter = new BuyCoinAdapter(context, buyCoinBean.getBuycoins(), BuyCoinPager.this.method);
        rv_buycoins_amount.setAdapter(buyCoinsAdapter);
        rv_buycoins_amount.setLayoutManager(new GridLayoutManager(context, 3));
        buyCoinsAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int topup_option_id) {
                BuyCoinPager.this.topup_option_id = topup_option_id;
                Log.e("TAGtopup_option_id", BuyCoinPager.this.topup_option_id + "");
            }
        });
        for (int i = 0; i < buyCoinBean.getBuycoins().size(); i++) {
            if (buyCoinBean.getBuycoins().get(i).getCategory().contains(method)) {
                BuyCoinPager.this.topup_option_id = buyCoinBean.getBuycoins().get(i).getId();
                return;
            }
        }

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
                case R.id.rl_buycoins_inbank:
                    if (rv_buycoins_inbank.getVisibility() != View.VISIBLE) {
                        if (rv_buycoins_carrer.getVisibility() == View.VISIBLE) {
                            iv_buycoins_carrier.clearAnimation();
//                            rotateAnim(iv_buycoins_carrier,-90f);
                            buyCoinsMethodMol.notifyDataSetChanged();
                        } else if (rv_buycoins_wallet.getVisibility() == View.VISIBLE) {
                            iv_buycoins_wallet.clearAnimation();
//                            rotateAnim(iv_buycoins_wallet,-90f);
                            buyCoinsMethodCash.notifyDataSetChanged();
                        }
                        rv_buycoins_inbank.setVisibility(View.VISIBLE);
                        view_inbank.setVisibility(View.VISIBLE);
                        view_carrier.setVisibility(View.GONE);
                        view_wallet.setVisibility(View.GONE);
                        rv_buycoins_carrer.setVisibility(View.GONE);
                        rv_buycoins_wallet.setVisibility(View.GONE);
                        rotateAnim(iv_buycoins_inbank, 180f);
                    } else {
                        rv_buycoins_inbank.setVisibility(View.GONE);
                        view_inbank.setVisibility(View.GONE);
                        iv_buycoins_inbank.clearAnimation();
                    }
                    rl_buycoins_prompt.setVisibility(View.GONE);
                    break;
                case R.id.rl_buycoins_carrier:
                    if (rv_buycoins_carrer.getVisibility() != View.VISIBLE) {
                        if (rv_buycoins_inbank.getVisibility() == View.VISIBLE) {
                            iv_buycoins_inbank.clearAnimation();
//                            rotateAnim(iv_buycoins_inbank,-90f);
                            buyCoinsMethodBank.notifyDataSetChanged();
                        } else if (rv_buycoins_wallet.getVisibility() == View.VISIBLE) {
                            iv_buycoins_wallet.clearAnimation();
//                            rotateAnim(iv_buycoins_wallet,-90f);
                            buyCoinsMethodCash.notifyDataSetChanged();
                        }
                        rv_buycoins_carrer.setVisibility(View.VISIBLE);
                        view_carrier.setVisibility(View.VISIBLE);
                        view_inbank.setVisibility(View.GONE);
                        view_wallet.setVisibility(View.GONE);
                        rv_buycoins_inbank.setVisibility(View.GONE);
                        rv_buycoins_wallet.setVisibility(View.GONE);
                        rotateAnim(iv_buycoins_carrier, 180f);
                        if (BuyCoinPager.this.method.equals("mol_3") || BuyCoinPager.this.method.equals("mol_804") || BuyCoinPager.this.method.equals("mol_805") || BuyCoinPager.this.method.equals("mol_806") || BuyCoinPager.this.method.equals("mol_815")) {
                            rl_buycoins_prompt.setVisibility(View.VISIBLE);
                            promat(BuyCoinPager.this.method);
                        } else {
                            rl_buycoins_prompt.setVisibility(View.GONE);
                        }
                    } else {
                        rv_buycoins_carrer.setVisibility(View.GONE);
                        view_carrier.setVisibility(View.GONE);
                        iv_buycoins_carrier.clearAnimation();
                        rl_buycoins_prompt.setVisibility(View.GONE);
                    }
                    break;
                case R.id.rl_buycoins_wallet:
                    if (rv_buycoins_wallet.getVisibility() != View.VISIBLE) {
                        if (rv_buycoins_inbank.getVisibility() == View.VISIBLE) {
                            iv_buycoins_inbank.clearAnimation();
//                            rotateAnim(iv_buycoins_inbank,-90f);
                            buyCoinsMethodBank.notifyDataSetChanged();
                        } else if (rv_buycoins_carrer.getVisibility() == View.VISIBLE) {
                            iv_buycoins_carrier.clearAnimation();
//                            rotateAnim(iv_buycoins_carrier,-90f);
                            buyCoinsMethodMol.notifyDataSetChanged();
                        }
                        rv_buycoins_carrer.setVisibility(View.GONE);
                        rv_buycoins_inbank.setVisibility(View.GONE);
                        rv_buycoins_wallet.setVisibility(View.VISIBLE);
                        view_wallet.setVisibility(View.VISIBLE);
                        view_inbank.setVisibility(View.GONE);
                        view_carrier.setVisibility(View.GONE);
                        rotateAnim(iv_buycoins_wallet, 180f);
                    } else {
                        rv_buycoins_wallet.setVisibility(View.GONE);
                        view_wallet.setVisibility(View.GONE);
                        iv_buycoins_wallet.clearAnimation();
                    }
                    rl_buycoins_prompt.setVisibility(View.GONE);
                    break;
                case R.id.rl_buycoins_topup:                         //支付
                    String token_s = Utils.getSpData("token_num", context);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }

                    if (token > System.currentTimeMillis() / 1000) {
                        if ("android-inapp".equals(method)) {
                            startRegistered();
                        } else if (method != null && method.contains("_my")) {
                            startPayssion(method);
                        } else if (method != null && method.contains("mol")) {
                            String[] split = method.split("_");
                            startMol(split[1]);
                        }

                    } else {
                        Map eventValue;
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
                case R.id.rl_mol_header:
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).rg_main.setVisibility(View.VISIBLE);
                    }
                    rl_buycoins_mol.setVisibility(View.GONE);
                    break;
            }
        }
    }

    long mExitTime;

    private void startAlert(boolean flag) {
        if ((System.currentTimeMillis() - mExitTime) < 3000) {
            mExitTime = System.currentTimeMillis();
            return;
        }

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
            public void success(final String response, String link) {
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
        tv_buycoins_buy.setEnabled(false);
        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.VISIBLE);
        tv_buycoins_buy.setBackgroundResource(R.color.D12F1D);

        Log.e("TAGtopup_option_id", topup_option_id + "");
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
            public void success(final String response, String link) {
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
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
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

    private void startMol(String s) {
        tv_buycoins_buy.setEnabled(false);
        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.VISIBLE);
        tv_buycoins_buy.setBackgroundResource(R.color.D12F1D);

        int id = Integer.parseInt(s);
        String url = MyApplication.url + "/v1/payments/mol/?timezone=" + MyApplication.url;
        String json;
        if (id == 3 || id == 804 || id == 805 || id == 806 || id == 815) {
            json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": " + id + ",\"success_url\": \"http://net.luckybuyer.success\"}";
        } else {
            json = "{\"failure_url\": \"http://net.luckybuyer.failure\",\"method\": " + id + ",\"success_url\": \"http://net.luckybuyer.success\",\"topup_option_id\": " + topup_option_id + "}";
        }
        Log.e("TAG333", id + "" + json);
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        map.put("LK-CLIENT-TYPE", "appsflyer_android");
        map.put("LK-APP-ID", "net.iwantbuyer");
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response, String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_MOL", response);
                        processMol(response);
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
                        Log.e("TAG_method", code + message);
                        Utils.MyToast(context, context.getString(R.string.Networkfailure) + code);
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                        inflate.findViewById(R.id.pb_buycoins_topup).setVisibility(View.GONE);
                        tv_buycoins_buy.setBackgroundResource(R.color.all_orange);
                        tv_buycoins_buy.setEnabled(true);
                    }
                });
            }
        });
    }

    private void processMol(String response) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).rg_main.setVisibility(View.GONE);
        }
        rl_buycoins_mol.setVisibility(View.VISIBLE);
        wv_buycoins.removeAllViews();
        Gson gson = new Gson();
        BuyCoinMolBean bean = gson.fromJson(response, BuyCoinMolBean.class);
        WebSettings webSettings = wv_buycoins.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv_buycoins.loadUrl(bean.getPayment_url());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_buycoins.setWebViewClient(new MyWebViewClient());
        wv_buycoins.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb_buycoins_mol.setVisibility(View.VISIBLE);
                pb_buycoins_mol.setProgress(newProgress);
                if (newProgress == 100) {
                    pb_buycoins_mol.setVisibility(View.GONE);
                }
            }
        });
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//网页页面开始加载的时候
            super.onPageStarted(view, url, favicon);
            if (url.contains("http://net.luckybuyer.success")) {   //请求成功
                rl_buycoins_mol.setVisibility(View.GONE);
                startAlert(true);
                if (context instanceof MainActivity) {
                    ((MainActivity) context).rg_main.setVisibility(View.VISIBLE);
                }
            } else if (url.contains("http://net.luckybuyer.failure")) {  //请求失败
                rl_buycoins_mol.setVisibility(View.GONE);
                startAlert(false);
                if (context instanceof MainActivity) {
                    ((MainActivity) context).rg_main.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void rotateAnim(ImageView imageview, float f) {
        Animation anim = new RotateAnimation(0f, f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        imageview.startAnimation(anim);
    }
}
