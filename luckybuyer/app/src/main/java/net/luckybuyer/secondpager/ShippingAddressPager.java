package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.ShippingAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.bean.ShippingAddressBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/8.
 */
public class ShippingAddressPager extends BaseNoTrackPager {

    private RelativeLayout rl_shipping_header;
    private ImageView iv_shipping_back;
    private TextView tv_shipping_back;
    private TextView tv_shipping_newadd;
    private RecyclerView rv_shipping_address;
    private View inflate;


    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_shipping, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        ((SecondPagerActivity) context).from= "dispatchpager";
        findView();
        setHeadMargin();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        String MyBuyUrl = MyApplication.url + "/v1/addresses/?per_page=20&page=1&timezone=" + "utc";
        String token = Utils.getSpData("token", context);
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(MyBuyUrl, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        processData(response);
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {

                    }

                    @Override
                    public void failure(Exception exception) {

                    }
                }

        );
    }

    private void findView() {
        rl_shipping_header = (RelativeLayout) inflate.findViewById(R.id.rl_shipping_header);
        iv_shipping_back = (ImageView) inflate.findViewById(R.id.iv_shipping_back);
        tv_shipping_back = (TextView) inflate.findViewById(R.id.tv_shipping_back);
        tv_shipping_newadd = (TextView) inflate.findViewById(R.id.tv_shipping_newadd);
        rv_shipping_address = (RecyclerView) inflate.findViewById(R.id.rv_shipping_address);

        iv_shipping_back.setOnClickListener(new MyOnClickListener());
        tv_shipping_back.setOnClickListener(new MyOnClickListener());
        tv_shipping_newadd.setOnClickListener(new MyOnClickListener());
    }

    private void processData(String response){
        Gson gson = new Gson();
        response = "{\"shipping\":" + response + "}";
        Log.e("TAG", response);
        ShippingAddressBean shippingAddressBean = gson.fromJson(response, ShippingAddressBean.class);

        rv_shipping_address.setAdapter(new ShippingAdapter(context, shippingAddressBean.getShipping()));
        rv_shipping_address.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_shipping_back:
                    ((SecondPagerActivity)context).switchPage(7);
                    break;
                case R.id.tv_shipping_back:
                    ((SecondPagerActivity)context).switchPage(7);
                    break;
                case R.id.tv_shipping_newadd:

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
        rl_shipping_header.setLayoutParams(lp);

    }
}