package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.CoinDetailPagerAdapter;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.CoinDetailBean;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/18.
 */
public class CoinDetailPager extends BasePager {

    private RelativeLayout rl_coindetail_header;
    private TextView tv_coindetail_back;
    private ImageView iv_coindetail_back;
    private TextView tv_coindetail_balance;
    private TextView tv_coindetail_buycoins;
    private RecyclerView rv_coindetail;
    private View inflate;

    private List list = new ArrayList();
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_coindetail, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setHeadMargin();
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);

        String token = Utils.getSpData("token", context);
        String url = "https://api-staging.luckybuyer.net/v1/gold-records/?per_page=20&page=1&timezone=utc";
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {
                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        processData(response);
                                        HttpUtils.getInstance().stopNetWorkWaiting();
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {
                        Log.e("TAG", requestCode + "");
                        Log.e("TAG", message);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();

                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {

                    }
                }

        );
    }

    //处理数据
    private void processData(String response) {
        Gson gson = new Gson();
        String str = "{\"coin\":" + response + "}";
        CoinDetailBean coinDetailBean = gson.fromJson(str, CoinDetailBean.class);

        rv_coindetail.setAdapter(new CoinDetailPagerAdapter(context, coinDetailBean.getCoin()));
        GridLayoutManager gridlayoutManager = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_coindetail.setLayoutManager(gridlayoutManager);
    }

    private void findView() {
        rl_coindetail_header = (RelativeLayout) inflate.findViewById(R.id.rl_coindetail_header);
        tv_coindetail_back = (TextView) inflate.findViewById(R.id.tv_coindetail_back);
        iv_coindetail_back = (ImageView) inflate.findViewById(R.id.iv_coindetail_back);
        tv_coindetail_balance = (TextView) inflate.findViewById(R.id.tv_coindetail_balance);
        tv_coindetail_buycoins = (TextView) inflate.findViewById(R.id.tv_coindetail_buycoins);
        rv_coindetail = (RecyclerView) inflate.findViewById(R.id.rv_coindetail);

        tv_coindetail_balance.setText("$" + Utils.getSpData("balance", context));
        //设置监听
        tv_coindetail_back.setOnClickListener(new MyOnClickListener());
        iv_coindetail_back.setOnClickListener(new MyOnClickListener());
        tv_coindetail_buycoins.setOnClickListener(new MyOnClickListener());
    }



    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_coindetail_back:
                    ((SecondPagerActivity)context).finish();
                    break;
                case R.id.iv_coindetail_back:
                    ((SecondPagerActivity)context).finish();
                    break;
                case R.id.tv_coindetail_buycoins:
                    ((SecondPagerActivity)context).switchPage(6);
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = sbar;
        rl_coindetail_header.setLayoutParams(lp);
    }
}
