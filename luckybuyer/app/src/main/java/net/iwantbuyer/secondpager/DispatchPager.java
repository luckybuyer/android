package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.ShippingAddress;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/30.
 */
public class DispatchPager extends BaseNoTrackPager {

    private RelativeLayout rl_dispatch_header;
    private LinearLayout ll_dispatch_back;
    private RelativeLayout rl_dispatch_address;
    private RelativeLayout rl_dispatch_participate;
    //商品信息
    private ImageView iv_dispatch_icon;
    private TextView jtv_dispatch_title;
    private TextView tv_dispatch_issue;
    private TextView tv_dispatch_participate;
    private TextView tv_dispatch_time;

    private TextView tv_dispatch_adddiscribe;
    private RelativeLayout rl_dispatch_address_content;
    private View inflate;
    private int dispatch_game_id;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private TextView tv_dispatch_confirm;                    //确认收货
    private TextView tv_dispatch_isshow;                    //确认收货
    private TextView tv_dispatch_delivered;

    private EditText et_dispatch_phonenum;
    private EditText et_dispatch_operator;

    private TextView tv_dispatch_5coins;

    private View defaultaddress;
    private View cardView;
    private int shared_coins;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_dispatch, null);
        ((SecondPagerActivity) context).from = "dispatchpager";
        findView();
        ((SecondPagerActivity) context).from = "";

        String str = Utils.getSpData("gifts_post_share", context);
        shared_coins = Integer.parseInt(str);
        if (shared_coins == 0) {
            tv_dispatch_5coins.setVisibility(View.GONE);
        } else {
            tv_dispatch_5coins.setText(context.getString(R.string.showitstart) + shared_coins + context.getString(R.string.showitlast));
        }
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
                    public void success(final String response) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_keepout.setVisibility(View.GONE);
                                        setView(response);
                                        Log.e("TAG_dispatch", response);
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
        tv_dispatch_adddiscribe = (TextView) inflate.findViewById(R.id.tv_dispatch_adddiscribe);
        tv_dispatch_time = (TextView) inflate.findViewById(R.id.tv_dispatch_time);

        rl_dispatch_address_content = (RelativeLayout) inflate.findViewById(R.id.rl_dispatch_address_content);
        rl_dispatch_address = (RelativeLayout) inflate.findViewById(R.id.rl_dispatch_address);
        rl_dispatch_participate = (RelativeLayout) inflate.findViewById(R.id.rl_dispatch_participate);

        defaultaddress = View.inflate(context, R.layout.pager_dispatch_address_default, null);

        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);

        tv_dispatch_confirm = (TextView) inflate.findViewById(R.id.tv_dispatch_confirm);
        tv_dispatch_isshow = (TextView) inflate.findViewById(R.id.tv_dispatch_isshow);
        tv_dispatch_delivered = (TextView) inflate.findViewById(R.id.tv_dispatch_delivered);

        cardView = View.inflate(context, R.layout.pager_dispatch_address_card, null);              //卡的时候所需要的视图
        et_dispatch_operator = (EditText) cardView.findViewById(R.id.et_dispatch_operator);
        et_dispatch_phonenum = (EditText) cardView.findViewById(R.id.et_dispatch_phonenum);
        tv_dispatch_5coins = ((TextView) inflate.findViewById(R.id.tv_dispatch_5coins));

        ll_dispatch_back.setOnClickListener(new MyOnClickListener());
        tv_dispatch_confirm.setOnClickListener(new MyOnClickListener());
        tv_dispatch_isshow.setOnClickListener(new MyOnClickListener());
        rl_dispatch_participate.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
    }

    DispatchGameBean dispatchGameBean;

    //设置视图
    public void setView(String response) {
        Log.e("TAG_dispatch", response);
        Gson gson = new Gson();
        dispatchGameBean = gson.fromJson(response, DispatchGameBean.class);

        TextView tv_dispatch_selector_address = (TextView) defaultaddress.findViewById(R.id.tv_dispatch_selector_address);
        Log.e("TAG", defaultaddress.toString());
        TextView tv_dispatch_current_address = (TextView) defaultaddress.findViewById(R.id.tv_dispatch_current_address);

        TextView tv_disaptch_name = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_name);
        TextView tv_disaptch_telnum = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_telnum);
        TextView tv_disaptch_add_detailed = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_add_detailed);

        String status = dispatchGameBean.getDelivery().getStatus();
        if ("pending".equals(status)) {
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);

        } else if ("processing".equals(status)) {
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(ContextCompat.getColor(context, R.color.ff9c05));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(true);
        } else if ("shipping".equals(status)) {
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(false);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(ContextCompat.getColor(context, R.color.text_black));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(false);


            inflate.findViewById(R.id.iv_disaptch_delivered).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_delivered).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_delivered)).setTextColor(ContextCompat.getColor(context, R.color.ff9c05));
            inflate.findViewById(R.id.view_delivered).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_delivered).setEnabled(true);
            tv_dispatch_confirm.setVisibility(View.VISIBLE);
        } else if ("finished".equals(status)) {
            Log.e("TAG", "finished");
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(false);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(ContextCompat.getColor(context, R.color.text_black));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_delivered).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_delivered).setEnabled(false);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_delivered)).setTextColor(ContextCompat.getColor(context, R.color.text_black));
            inflate.findViewById(R.id.view_delivered).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_delivered).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_show).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_show).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_show)).setTextColor(ContextCompat.getColor(context, R.color.ff9c05));
            tv_dispatch_5coins.setTextColor(ContextCompat.getColor(context, R.color.ff9c05));
            tv_dispatch_isshow.setVisibility(View.VISIBLE);
        } else if ("shared".equals(status)) {
            rl_dispatch_participate.setVisibility(View.VISIBLE);
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_show)).setText(context.getString(R.string.Shared));
            if (shared_coins != 0) {
                tv_dispatch_5coins.setText(context.getString(R.string.shoSuccess) + ":" + shared_coins + context.getString(R.string.showSuccessDiscribe));
            }

            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(false);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(ContextCompat.getColor(context, R.color.text_black));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_delivered).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_delivered).setEnabled(false);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_delivered)).setTextColor(ContextCompat.getColor(context, R.color.text_black));
            inflate.findViewById(R.id.view_delivered).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_delivered).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_show).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_show).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_show)).setTextColor(ContextCompat.getColor(context, R.color.ff9c05));
            tv_dispatch_5coins.setTextColor(ContextCompat.getColor(context, R.color.ff9c05));

        }
        if ("virtual_mobile".equals(dispatchGameBean.getDelivery().getType())) {
            ((TextView) inflate.findViewById(R.id.tv_disaptch_selector_address)).setText(context.getString(R.string.editphoneoperator));
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setText(context.getString(R.string.WaitingforProcessing));
        } else {
            ((TextView) inflate.findViewById(R.id.tv_disaptch_selector_address)).setText(context.getString(R.string.confirmshippingaddress));
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setText(context.getString(R.string.Waitingforshippment));
        }
        jtv_dispatch_title.setText(dispatchGameBean.getGame().getProduct().getTitle() + "");
        tv_dispatch_issue.setText("" + dispatchGameBean.getGame().getIssue_id() + "");
        tv_dispatch_participate.setText("" + dispatchGameBean.getGame().getLucky_order().getTotal_shares() + "");
        tv_dispatch_time.setText(dispatchGameBean.getDelivery().getCreated_at().substring(0, 20).replace("T", "\t ") + "");
        Glide.with(context).load("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iv_dispatch_icon.setImageBitmap(resource);
            }
        });

    }

    //设置地址  包括有默认地址与无默认地址
    private void setAddress(DispatchGameBean dispatchGameBean, View defaultaddress, TextView tv_dispatch_selector_address, TextView tv_dispatch_current_address, TextView tv_disaptch_name, TextView tv_disaptch_telnum, TextView tv_disaptch_add_detailed, String status) {
        Log.e("TAG_type", dispatchGameBean.getDelivery().getType());
        //当 物品为  虚拟物品的时候
        if ("virtual_mobile".equals(dispatchGameBean.getDelivery().getType())) {
            setXian(145);
            rl_dispatch_address_content.removeAllViews();
            rl_dispatch_address_content.addView(cardView);
            cardView.findViewById(R.id.tv_dispatch_card_submit).setOnClickListener(new MyOnClickListener());
            if (!"pending".equals(status)) {
                cardView.findViewById(R.id.tv_dispatch_card_submit).setVisibility(View.GONE);
                if (dispatchGameBean.getDelivery().getAddress() != null) {
                    et_dispatch_phonenum.setEnabled(false);
                    et_dispatch_phonenum.setText(dispatchGameBean.getDelivery().getAddress().getPhone() + "");
                    et_dispatch_operator.setEnabled(false);
                    et_dispatch_operator.setText(dispatchGameBean.getDelivery().getAddress().getVendor() + "");
                }
            }
        }
//        else if ("virtual_mobile".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() != null){
//            setXian(100);
//            rl_dispatch_address_content.removeAllViews();
//            rl_dispatch_address_content.addView(cardView);
//
//            et_dispatch_phonenum.setEnabled(false);
//            et_dispatch_phonenum.setText(dispatchGameBean.getDelivery().getAddress().getPhone() +"");
//            et_dispatch_operator.setEnabled(false);
//            et_dispatch_operator.setText(dispatchGameBean.getDelivery().getAddress().getVendor() +"");
//            cardView.findViewById(R.id.tv_dispatch_card_submit).setVisibility(View.GONE);
//        }

        if ("post".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() == null && ((SecondPagerActivity) context).shippingBean == null) {
            setXian(80);

            View addView = View.inflate(context, R.layout.pager_dispatch_address, null);
            rl_dispatch_address_content.addView(addView);
            rl_dispatch_address_content.setOnClickListener(new MyOnClickListener());
        }  else if ("post".equals(dispatchGameBean.getDelivery().getType()) && ((SecondPagerActivity) context).shippingBean != null) {
            setXian(130);

            Log.e("TAG_qianmian", ((SecondPagerActivity) context).shippingBean + "");

            tv_disaptch_name.setText(((SecondPagerActivity) context).shippingBean.getName() + "");
            tv_disaptch_telnum.setText(((SecondPagerActivity) context).shippingBean.getPhone() + "");
            tv_disaptch_add_detailed.setText(((SecondPagerActivity) context).shippingBean.getAddress() + "");

            if (!"pending".equals(status)) {
                defaultaddress.findViewById(R.id.rl_dispatch_selector_address).setVisibility(View.GONE);
            } else {
                defaultaddress.findViewById(R.id.rl_dispatch_selector_address).setVisibility(View.VISIBLE);
            }
            rl_dispatch_address_content.removeAllViews();
            rl_dispatch_address_content.addView(defaultaddress);
            tv_dispatch_selector_address.setOnClickListener(new MyOnClickListener());
            tv_dispatch_current_address.setOnClickListener(new MyOnClickListener());
        }else if ("post".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() != null) {
            Log.e("TAG_地址高度", defaultaddress.getHeight() + "");
            setXian(150);

            tv_disaptch_name.setText(dispatchGameBean.getDelivery().getAddress().getName() + "");
            tv_disaptch_telnum.setText(dispatchGameBean.getDelivery().getAddress().getPhone() + "");
            tv_disaptch_add_detailed.setText(dispatchGameBean.getDelivery().getAddress().getAddress() + "");

            if (!"pending".equals(status)) {
                defaultaddress.findViewById(R.id.rl_dispatch_selector_address).setVisibility(View.GONE);
            } else {
                defaultaddress.findViewById(R.id.rl_dispatch_selector_address).setVisibility(View.VISIBLE);
            }
            rl_dispatch_address_content.removeAllViews();
            rl_dispatch_address_content.addView(defaultaddress);
            tv_dispatch_selector_address.setOnClickListener(new MyOnClickListener());
            tv_dispatch_current_address.setOnClickListener(new MyOnClickListener());
        }

    }

    //设置那条竖线的长度
    private void setXian(int i) {
        View view = inflate.findViewById(R.id.view_address);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, i));
        lp.leftMargin = DensityUtil.dip2px(context, 17);
        lp.topMargin = DensityUtil.dip2px(context, 8);
        view.setLayoutParams(lp);
    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_dispatch_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.rl_dispatch_address_content:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "dispatchpager";
                    break;
                case R.id.tv_dispatch_selector_address:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "dispatchpager";
                    break;
                case R.id.tv_dispatch_current_address:
                    View viewAddress = View.inflate(context, R.layout.alertdialog_current_address, null);
                    StartAlertDialog(viewAddress);
                    break;
                case R.id.rl_dispatch_participate:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("batch_id", dispatchGameBean.getGame().getBatch_id());
                    context.startActivity(intent);
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
                case R.id.tv_dispatch_card_submit:
                    if (et_dispatch_phonenum == null ) {
                        Utils.MyToast(context, context.getString(R.string.numbernullwarn));
                    } else if(et_dispatch_operator == null) {
                        Utils.MyToast(context, context.getString(R.string.operatornummwarn));
                    }else {
                        setCurrentAddress();
                    }
                    break;
            }
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
                    public void success(final String response) {

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
                                    tv_dispatch_delivered.setText(context.getString(R.string.Delivered));
                                    tv_dispatch_confirm.setVisibility(View.VISIBLE);
                                    initData();
                                    tv_dispatch_confirm.setVisibility(View.GONE);
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
                jsonObject.accumulate("phone", et_dispatch_phonenum.getText() + "");
                jsonObject.accumulate("vendor", et_dispatch_operator.getText() + "");

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
                    public void success(final String response) {

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
        show.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
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

