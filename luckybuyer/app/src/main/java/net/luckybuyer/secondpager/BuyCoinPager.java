package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.BuyCoinAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.bean.BuyCoinBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {

    private RelativeLayout rl_buycoins_header;
    private ImageView iv_buycoins_back;
    private TextView tv_buycoins_back;
    private TextView tv_buycoins_balance;
    private TextView tv_buycoins_rate;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;
    private RecyclerView rv_buycoins;
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        ((SecondPagerActivity)context).from = "coindetailpager";
        findView();
        setHeadMargin();
        HttpUtils.getInstance().startNetworkWaiting(context);
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        String url = "https://api-staging.luckybuyer.net/v1/topup-options/?per_page=20&page=1&timezone=" + MyApplication.utc;
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
        //设置监听
        iv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_buy.setOnClickListener(new MyOnClickListener());
    }

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"buycoins\":" + response + "}";
        final BuyCoinBean buyCoinBean = gson.fromJson(response, BuyCoinBean.class);
        BuyCoinAdapter buyCoinAdapter = new BuyCoinAdapter(context,buyCoinBean.getBuycoins());
        rv_buycoins.setAdapter(buyCoinAdapter);
        rv_buycoins.setLayoutManager(new GridLayoutManager(context, 2));
        buyCoinAdapter.setBuyCoinOnClickListener(new BuyCoinAdapter.BuyCoinOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                double coins = buyCoinBean.getBuycoins().get(position).getAmount()* 3.6726;
                coins=((int)(coins*100))/100;
                tv_buycoins_count.setText("Total:" + buyCoinBean.getBuycoins().get(position).getAmount() + "≈" + coins + "AED");
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_buycoins_back:
                    ((SecondPagerActivity)context).switchPage(5);
                    break;
                case R.id.tv_buycoins_back:
                    ((SecondPagerActivity)context).switchPage(5);
                    break;
                case R.id.tv_buycoins_buy:
                    
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context,38));
        lp.topMargin = sbar;
        rl_buycoins_header.setLayoutParams(lp);
    }
}
