package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.DispatchAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/30.
 */
public class DispatchPager extends BaseNoTrackPager {

    private RelativeLayout rl_dispatch_header;
    private LinearLayout ll_dispatch_back;
//    private RelativeLayout rl_dispatch_address;
//    private RelativeLayout rl_dispatch_participate;
    //商品信息
    private ImageView iv_dispatch_icon;
    private TextView jtv_dispatch_title;
    private TextView tv_dispatch_issue;
    private TextView tv_dispatch_participate;

    private View inflate;
    private int dispatch_game_id;

    private RecyclerView rv_dispatch;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;


    private int shared_coins;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_dispatch, null);
        ((SecondPagerActivity) context).from = "dispatchpager";
        findView();
        ((SecondPagerActivity) context).from = "";

        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        dispatch_game_id = ((SecondPagerActivity) context).dispatch_game_id;

        rl_keepout.setVisibility(View.VISIBLE);
        rl_nodata.setVisibility(View.GONE);
        rl_neterror.setVisibility(View.GONE);
        rl_loading.setVisibility(View.VISIBLE);

        String token = Utils.getSpData("token", context);
        String url = MyApplication.url + "/v1/game-orders/" + dispatch_game_id + "?timezone=" + MyApplication.utc;
        Log.e("TAG_orderid", url);
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        //请求登陆接口
        final String finalToken = token;
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response,String link) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_keepout.setVisibility(View.GONE);
                                        setView(response);
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(final int requestCode, final String message) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rl_nodata.setVisibility(View.GONE);
                                rl_neterror.setVisibility(View.VISIBLE);
                                rl_loading.setVisibility(View.GONE);
                                Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "game-orders");
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
                }
        );

    }

    private void findView() {
        rl_dispatch_header = (RelativeLayout) inflate.findViewById(R.id.rl_dispatch_header);
        ll_dispatch_back = (LinearLayout) inflate.findViewById(R.id.ll_dispatch_back);
        iv_dispatch_icon = (ImageView) inflate.findViewById(R.id.iv_dispatch_icon);
        jtv_dispatch_title = (TextView) inflate.findViewById(R.id.jtv_dispatch_title);
        tv_dispatch_issue = (TextView) inflate.findViewById(R.id.tv_dispatch_issue);
        tv_dispatch_participate = (TextView) inflate.findViewById(R.id.tv_dispatch_participate);

        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);


        rv_dispatch = (RecyclerView) inflate.findViewById(R.id.rv_dispatch);

        ll_dispatch_back.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
    }

    DispatchGameBean dispatchGameBean;
    DispatchAdapter dispatchAdapter;
    //设置视图
    public void setView(String response) {

        Gson gson = new Gson();
        dispatchGameBean = gson.fromJson(response, DispatchGameBean.class);
        dispatchAdapter = new DispatchAdapter(context,dispatchGameBean);
        rv_dispatch.setAdapter(dispatchAdapter);
        rv_dispatch.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        dispatchAdapter.setDispatchOnClickListener(new DispatchAdapter.DispatchOnClickListener() {
            @Override
            public void onClick(String method) {
                if("setCurrentAddress".equals(method)) {
                    setCurrentAddress();
                }else if("currentAddress".equals(method)) {
                    currentAddress();
                }else if("sharedFacebook".equals(method)) {
                    sharedFacebook();
                }
            }
        });


        jtv_dispatch_title.setText(dispatchGameBean.getGame().getProduct().getTitle() + "");
        tv_dispatch_issue.setText("" + dispatchGameBean.getGame().getIssue_id() + "");
        tv_dispatch_participate.setText("" + dispatchGameBean.getGame().getLucky_order().getTotal_shares() + "");
        if(!((SecondPagerActivity)context).isDestroyed()) {
            Glide.with(context).load("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if(!((SecondPagerActivity)context).isDestroyed()) {
                    iv_dispatch_icon.setImageBitmap(resource);}
                }
            });
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_dispatch_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.tv_address_ok:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    setCurrentAddress();
                    break;
                case R.id.tv_address_cancel:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_net_again:
                    initData();
                    break;
                case R.id.tv_dispatch_confirm:
                    currentAddress();
                    break;
                case R.id.tv_dispatch_isshow:
                    //开始展示
                    ((SecondPagerActivity) context).switchPage(10);
                    ((SecondPagerActivity) context).order_id = dispatchGameBean.getId();
                    break;
            }
        }
    }
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    public void sharedFacebook(){
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e("TAG", result.getPostId()+"");
                Log.e("TAG", result.toString());
            }

            @Override
            public void onCancel() {
                Log.e("TAG", "0.0.0.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("TAG", error.toString());
            }
        });
        File file = new File("/sdcard/DCIM/P61005-115348.jpg");
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("hahah")
                    .setContentDescription(
                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=net.iwantbuyer"))
                    .setImageUrl(Uri.parse("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()))
                    .build();

            shareDialog.show(linkContent);
//            ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
//            shareButton.setShareContent(linkContent);
        }
    }

    //确认地址
    private void currentAddress() {
        HttpUtils.getInstance().startNetworkWaiting(context);
        String token = Utils.getSpData("token", context);
        String url = "";
        String json = "";
        url = MyApplication.url + "/v1/deliveries/" + dispatchGameBean.getDelivery().getId() + "?timezone=" + MyApplication.utc;
        json = "{\"status\": \"finished\"}";


        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        //请求登陆接口
        final String finalToken = token;
        HttpUtils.getInstance().patchJson(url, json, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response,String link) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        HttpUtils.getInstance().stopNetWorkWaiting();
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(final int requestCode, final String message) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                if (requestCode == 204) {
                                    initData();
                                } else {
                                    Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "deliveries");
                                }
                                Log.e("TAG_delivery", requestCode + message);
                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                Utils.MyToast(context, context.getString(R.string.Networkfailure));
                            }
                        });
                    }
                }

        );
    }

    //setCurrentAddress  当点击ok按钮时
    private void setCurrentAddress() {
        HttpUtils.getInstance().startNetworkWaiting(context);
        String token = Utils.getSpData("token", context);
        String url = "";
        String json = "";
        if ("virtual_mobile".equals(dispatchGameBean.getDelivery().getType())) {
            url = MyApplication.url + "/v1/deliveries/" + dispatchGameBean.getDelivery().getId() + "?timezone=" + MyApplication.utc;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("phone", dispatchAdapter.et_dispatch_phonenum.getText() + "");
                jsonObject.accumulate("vendor", dispatchAdapter.et_dispatch_operator.getText() + "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String address = jsonObject.toString();
//            Log.e("TAG_address", address);

            JSONObject object = new JSONObject();
            try {
                object.accumulate("address", jsonObject);
                object.accumulate("status", "processing");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = object.toString();
            Log.e("TAG_json", json);
        } else if ("post".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() != null) {
            url = MyApplication.url + "/v1/deliveries/" + dispatchGameBean.getDelivery().getId() + "?timezone=" + MyApplication.utc;
            json = "{\"status\": \"processing\"}";
        } else if ("post".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() == null && ((SecondPagerActivity) context).shippingBean != null) {
            url = MyApplication.url + "/v1/deliveries/" + dispatchGameBean.getDelivery().getId() + "?timezone=" + MyApplication.utc;
            json = "{\"status\": \"processing\",\"address_id\":  " + ((SecondPagerActivity) context).shippingBean.getId() + "}";
        }

        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        //请求登陆接口
        final String finalToken = token;
        HttpUtils.getInstance().patchJson(url, json, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response,String link) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        HttpUtils.getInstance().stopNetWorkWaiting();
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(final int requestCode, final String message) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TAG_更新成功", requestCode + message);
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                if (requestCode == 204) {
                                    initData();
                                } else {
                                    Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "deliveries");
                                }
                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        Utils.MyToast(context, context.getString(R.string.Networkfailure));
                    }
                }

        );
    }

    AlertDialog show;

    private void StartAlertDialog(View inflate) {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        TextView tv_address_cancel = (TextView) inflate.findViewById(R.id.tv_address_cancel);
        TextView tv_address_ok = (TextView) inflate.findViewById(R.id.tv_address_ok);

        tv_address_cancel.setOnClickListener(new MyOnClickListener());
        tv_address_ok.setOnClickListener(new MyOnClickListener());

        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

