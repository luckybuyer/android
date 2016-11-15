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
import android.widget.LinearLayout;
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

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;
    private View inflate;
    private LinearLayout ll_shipping_return;


    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_shipping, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        rl_keepout.setVisibility(View.VISIBLE);
        rl_nodata.setVisibility(View.GONE);
        rl_neterror.setVisibility(View.GONE);
        rl_loading.setVisibility(View.VISIBLE);

        String MyBuyUrl = MyApplication.url + "/v1/addresses/?per_page=20&page=1&timezone=" + MyApplication.utc;
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
                                        rl_keepout.setVisibility(View.GONE);
                                        processData(response);
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {
                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_nodata.setVisibility(View.GONE);
                                        rl_neterror.setVisibility(View.VISIBLE);
                                        rl_loading.setVisibility(View.GONE);
                                    }
                                }
                        );

                    }

                    @Override
                    public void failure(Exception exception) {
                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_nodata.setVisibility(View.GONE);
                                        rl_neterror.setVisibility(View.VISIBLE);
                                        rl_loading.setVisibility(View.GONE);
                                    }
                                }
                        );
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

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        ll_shipping_return = (LinearLayout) inflate.findViewById(R.id.ll_shipping_return);

        iv_shipping_back.setOnClickListener(new MyOnClickListener());
        tv_shipping_back.setOnClickListener(new MyOnClickListener());
        tv_shipping_newadd.setOnClickListener(new MyOnClickListener());
        ll_shipping_return.setOnClickListener(new MyOnClickListener());
    }

    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"shipping\":" + response + "}";
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
//                case R.id.iv_shipping_back:
//                    Log.e("TAG", ((SecondPagerActivity) context).from + "");
//                    if ("setpager".equals(((SecondPagerActivity) context).from)) {
//                        ((SecondPagerActivity) context).switchPage(4);
//                    } else if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
//                        ((SecondPagerActivity) context).switchPage(7);
//                    }
//                    break;
//                case R.id.tv_shipping_back:
//                    if ("setpager".equals(((SecondPagerActivity) context).from)) {
//                        ((SecondPagerActivity) context).switchPage(4);
//                    } else if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
//                        ((SecondPagerActivity) context).switchPage(7);
//                    }
//                    break;
                case R.id.tv_shipping_newadd:
                    ((SecondPagerActivity) context).switchPage(8);
                    ((SecondPagerActivity) context).from = "shippingaddress";
                    break;
                case R.id.ll_shipping_return:
                    if ("setpager".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).switchPage(4);
                    } else if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).switchPage(7);
                    }
                    break;
            }
        }
    }

}