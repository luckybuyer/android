package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/30.
 */
public class DispatchPager extends BaseNoTrackPager {

    private RelativeLayout rl_dispatch_header;
    private TextView tv_dispatch_back;
    private ImageView iv_dispatch_back;
    private RelativeLayout rl_dispatch_address;
    private RelativeLayout rl_dispatch_participate;
    //商品信息
    private ImageView iv_dispatch_icon;
    private TextView jtv_dispatch_title;
    private TextView tv_dispatch_issue;
    private TextView tv_dispatch_participate;

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
    private TextView tv_dispatch_delivered;

    private View defaultaddress;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_dispatch, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
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
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
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
                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                }

        );
    }

    private void findView() {
        rl_dispatch_header = (RelativeLayout) inflate.findViewById(R.id.rl_dispatch_header);
        tv_dispatch_back = (TextView) inflate.findViewById(R.id.tv_dispatch_back);
        iv_dispatch_back = (ImageView) inflate.findViewById(R.id.iv_dispatchs_back);
        iv_dispatch_icon = (ImageView) inflate.findViewById(R.id.iv_dispatch_icon);
        jtv_dispatch_title = (TextView) inflate.findViewById(R.id.jtv_dispatch_title);
        tv_dispatch_issue = (TextView) inflate.findViewById(R.id.tv_dispatch_issue);
        tv_dispatch_participate = (TextView) inflate.findViewById(R.id.tv_dispatch_participate);
        tv_dispatch_adddiscribe = (TextView) inflate.findViewById(R.id.tv_dispatch_adddiscribe);

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
        tv_dispatch_delivered = (TextView) inflate.findViewById(R.id.tv_dispatch_delivered);


        tv_dispatch_back.setOnClickListener(new MyOnClickListener());
        iv_dispatch_back.setOnClickListener(new MyOnClickListener());
        tv_dispatch_confirm.setOnClickListener(new MyOnClickListener());
        rl_dispatch_participate.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
    }

    DispatchGameBean dispatchGameBean;

    //设置视图
    private void setView(String response) {
        Gson gson = new Gson();
        Log.e("TAG_diaptch", response);
        dispatchGameBean = gson.fromJson(response, DispatchGameBean.class);
        Log.e("TAG_dispatch", response);

        TextView tv_dispatch_selector_address = (TextView) defaultaddress.findViewById(R.id.tv_dispatch_selector_address);
        Log.e("TAG", defaultaddress.toString());
        TextView tv_dispatch_current_address = (TextView) defaultaddress.findViewById(R.id.tv_dispatch_current_address);

        TextView tv_disaptch_name = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_name);
        TextView tv_disaptch_telnum = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_telnum);
        TextView tv_disaptch_add_detailed = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_add_detailed);

        String status = dispatchGameBean.getDelivery().getStatus();
        if ("pending".equals(status)) {
            Log.e("TAG", "pending");
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);

            //设置线的长度
//            View view = inflate.findViewById(R.id.view_address);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 150));
//            lp.leftMargin = DensityUtil.dip2px(context, 17);
//            lp.topMargin = DensityUtil.dip2px(context, 8);
//            view.setLayoutParams(lp);
        } else if ("processing".equals(status)) {
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            Log.e("TAG", "processing");
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.ff9c05));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(true);
        } else if ("shipping".equals(status)) {
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.ff9c05));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(true);

            tv_dispatch_confirm.setVisibility(View.VISIBLE);
        } else if ("finished".equals(status)) {
            //设置地址
            setAddress(dispatchGameBean, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed, status);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(false);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.text_black));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_delivered).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_delivered).setEnabled(true);
            ((TextView) inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.ff9c05));
        }

        jtv_dispatch_title.setText(dispatchGameBean.getGame().getProduct().getTitle() + "");
        tv_dispatch_issue.setText("Issue:" + dispatchGameBean.getGame().getIssue_id() + "");
        tv_dispatch_participate.setText("I participation:" + dispatchGameBean.getGame().getLucky_order().getTotal_shares() + "");
        Glide.with(context).load("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iv_dispatch_icon.setImageBitmap(resource);
            }
        });

    }

    //设置地址  包括有默认地址与无默认地址
    private void setAddress(DispatchGameBean dispatchGameBean, View defaultaddress, TextView tv_dispatch_selector_address, TextView tv_dispatch_current_address, TextView tv_disaptch_name, TextView tv_disaptch_telnum, TextView tv_disaptch_add_detailed, String status) {
        if (dispatchGameBean.getDelivery().getAddress() == null && ((SecondPagerActivity) context).shippingBean == null) {
            View view = inflate.findViewById(R.id.view_address);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 78));
            lp.leftMargin = DensityUtil.dip2px(context, 17);
            lp.topMargin = DensityUtil.dip2px(context, 8);
            view.setLayoutParams(lp);

            View addView = View.inflate(context, R.layout.pager_dispatch_address, null);
            rl_dispatch_address_content.addView(addView);
            rl_dispatch_address_content.setOnClickListener(new MyOnClickListener());
        } else if (dispatchGameBean.getDelivery().getAddress() != null) {
            View view = inflate.findViewById(R.id.view_address);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 130));
            lp.leftMargin = DensityUtil.dip2px(context, 17);
            lp.topMargin = DensityUtil.dip2px(context, 8);
            view.setLayoutParams(lp);

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
        } else if (dispatchGameBean.getDelivery().getAddress() == null && ((SecondPagerActivity) context).shippingBean != null) {
            View view = inflate.findViewById(R.id.view_address);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 130));
            lp.leftMargin = DensityUtil.dip2px(context, 17);
            lp.topMargin = DensityUtil.dip2px(context, 8);
            view.setLayoutParams(lp);


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
        }

    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_dispatch_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.iv_dispatchs_back:
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

        Log.e("TAG", json);
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
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
                                        tv_dispatch_delivered.setText("Delivered");
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(final int requestCode, final String message) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.MyToast(context,"Internet not available");
                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.MyToast(context,"Internet not available");
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
        if (dispatchGameBean.getDelivery().getAddress() != null) {
            url = MyApplication.url + "/v1/deliveries/" + dispatchGameBean.getDelivery().getId() + "?timezone=" + MyApplication.utc;
            json = "{\"status\": \"processing\"}";
        } else if (dispatchGameBean.getDelivery().getAddress() == null && ((SecondPagerActivity) context).shippingBean != null) {
            url = MyApplication.url + "/v1/deliveries/" + dispatchGameBean.getDelivery().getId() + "?timezone=" + MyApplication.utc;
            json = "{\"status\": \"processing\",\"address_id\":  " + ((SecondPagerActivity) context).shippingBean.getId() + "}";
        }

        Log.e("TAG", json);
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
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
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                if (requestCode == 204) {
                                    initData();
                                }
                                Log.e("TAG_dispatch", requestCode + message);
                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        Utils.MyToast(context, "Delivery update failed");
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

}
