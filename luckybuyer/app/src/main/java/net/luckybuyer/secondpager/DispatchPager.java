package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.bean.AllOrderBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2016/9/30.
 */
public class DispatchPager extends BaseNoTrackPager {

    private RelativeLayout rl_dispatch_header;
    private TextView tv_dispatch_back;
    private ImageView iv_dispatch_back;
    private RelativeLayout rl_dispatch_address;
    //商品信息
    private ImageView iv_dispatch_icon;
    private TextView jtv_dispatch_title;
    private TextView tv_dispatch_issue;
    private TextView tv_dispatch_participate;

    private TextView tv_dispatch_adddiscribe;
    private RelativeLayout rl_dispatch_address_content;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_dispatch, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setView();
        setHeadMargin();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
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

        tv_dispatch_back.setOnClickListener(new MyOnClickListener());
        iv_dispatch_back.setOnClickListener(new MyOnClickListener());
    }

    //设置视图
    private void setView() {
        List<AllOrderBean.AllorderBean> list = ((SecondPagerActivity) context).allList;
        int position = ((SecondPagerActivity) context).position;


        View defaultaddress = View.inflate(context, R.layout.pager_dispatch_address_default, null);
        RelativeLayout rl_dispatch_selector_address = (RelativeLayout) defaultaddress.findViewById(R.id.rl_dispatch_selector_address);
        TextView tv_dispatch_selector_address = (TextView) defaultaddress.findViewById(R.id.tv_dispatch_selector_address);
        TextView tv_dispatch_current_address = (TextView) defaultaddress.findViewById(R.id.tv_dispatch_current_address);

        TextView tv_disaptch_name = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_name);
        TextView tv_disaptch_telnum = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_telnum);
        TextView tv_disaptch_add_detailed = (TextView) defaultaddress.findViewById(R.id.tv_disaptch_add_detailed);

        String status = list.get(position).getDelivery().getStatus();
        if ("pending".equals(status)) {
            setAddress(list, position, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed);

            //设置线的长度
            View view = inflate.findViewById(R.id.view_address);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 150));
            lp.leftMargin = DensityUtil.dip2px(context, 20);
            lp.topMargin = DensityUtil.dip2px(context, 8);
            view.setLayoutParams(lp);
        } else if ("processing".equals(status)) {
            //设置地址
            setAddress(list, position, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed);
            rl_dispatch_selector_address.setVisibility(View.GONE);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(true);
            ((TextView)inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.ff9c05));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(true);
        } else if ("shipping".equals(status)) {
            //设置地址
            setAddress(list, position, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed);
            rl_dispatch_selector_address.setVisibility(View.GONE);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(true);
            ((TextView)inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.ff9c05));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(true);
        } else if ("finished".equals(status)) {
            //设置地址
            setAddress(list, position, defaultaddress, tv_dispatch_selector_address, tv_dispatch_current_address, tv_disaptch_name, tv_disaptch_telnum, tv_disaptch_add_detailed);
            rl_dispatch_selector_address.setVisibility(View.GONE);
            inflate.findViewById(R.id.iv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.tv_disaptch_selector_address).setEnabled(false);
            inflate.findViewById(R.id.view_address).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_shipping).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_shipping).setEnabled(false);
            ((TextView)inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.text_black));
            inflate.findViewById(R.id.view_shipping).setBackgroundResource(R.drawable.selector_dispatch_view);
            inflate.findViewById(R.id.view_shipping).setEnabled(false);

            inflate.findViewById(R.id.iv_disaptch_delivered).setBackgroundResource(R.drawable.selector_dispatch_point);
            inflate.findViewById(R.id.iv_disaptch_delivered).setEnabled(true);
            ((TextView)inflate.findViewById(R.id.tv_dispatch_shipping)).setTextColor(getResources().getColor(R.color.ff9c05));
        }

        jtv_dispatch_title.setText(list.get(position).getGame().getProduct().getTitle() + "");
        tv_dispatch_issue.setText("Issue:" + list.get(position).getGame().getIssue_id() + "");
        tv_dispatch_participate.setText("I participation:" + list.get(position).getGame().getLucky_order().getNumbers().size() + "");
        Glide.with(context).load("https:" + list.get(position).getGame().getProduct().getTitle_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iv_dispatch_icon.setImageBitmap(resource);
            }
        });

    }

    //设置地址  包括有默认地址与无默认地址
    private void setAddress(List<AllOrderBean.AllorderBean> list, int position, View defaultaddress, TextView tv_dispatch_selector_address, TextView tv_dispatch_current_address, TextView tv_disaptch_name, TextView tv_disaptch_telnum, TextView tv_disaptch_add_detailed) {
        if (list.get(position).getDelivery().getAddress() == null) {
            View view = inflate.findViewById(R.id.view_address);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 85));
            lp.leftMargin = DensityUtil.dip2px(context, 20);
            lp.topMargin = DensityUtil.dip2px(context, 8);
            view.setLayoutParams(lp);

            View addView = View.inflate(context, R.layout.pager_dispatch_address, null);
            rl_dispatch_address_content.addView(addView);
            rl_dispatch_address_content.setOnClickListener(new MyOnClickListener());
        } else {
            View view = inflate.findViewById(R.id.view_address);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 1), DensityUtil.dip2px(context, 130));
            lp.leftMargin = DensityUtil.dip2px(context, 20);
            lp.topMargin = DensityUtil.dip2px(context, 8);
            view.setLayoutParams(lp);


            tv_disaptch_name.setText(list.get(position).getDelivery().getAddress().getName() + "");
            tv_disaptch_telnum.setText(list.get(position).getDelivery().getAddress().getPhone() + "");
            tv_disaptch_add_detailed.setText(list.get(position).getDelivery().getAddress().getAddress() + "");
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
                    ((SecondPagerActivity) context).switchPage(8);
                    break;
                case R.id.tv_dispatch_selector_address:
                    ((SecondPagerActivity) context).switchPage(9);
                    break;
                case R.id.tv_dispatch_current_address:
                    Utils.MyToast(context, "确认地址");
                    break;
            }
        }
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
        Log.e("TAG", sbar + "");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 38));
        lp.topMargin = sbar;
        rl_dispatch_header.setLayoutParams(lp);

    }
}

