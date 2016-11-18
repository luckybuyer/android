package net.smartbuyer.secondpager;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.smartbuyer.R;
import net.smartbuyer.activity.SecondPagerActivity;
import net.smartbuyer.adapter.CoinDetailPagerAdapter;
import net.smartbuyer.app.MyApplication;
import net.smartbuyer.base.BasePager;
import net.smartbuyer.bean.CoinDetailBean;
import net.smartbuyer.utils.HttpUtils;
import net.smartbuyer.utils.Utils;

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

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private List list = new ArrayList();

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_coindetail, null);
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

        String token = Utils.getSpData("token", context);
        String url = MyApplication.url + "/v1/gold-records/?per_page=20&page=1&timezone=" + MyApplication.utc;
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
                                        rl_keepout.setVisibility(View.GONE);
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
                                rl_nodata.setVisibility(View.GONE);
                                rl_neterror.setVisibility(View.VISIBLE);
                                rl_loading.setVisibility(View.GONE);

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

                            }
                        });
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
        GridLayoutManager gridlayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false) {
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
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading= (RelativeLayout) inflate.findViewById(R.id.rl_loading);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        tv_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        //设置监听
        tv_coindetail_back.setOnClickListener(new MyOnClickListener());
        iv_coindetail_back.setOnClickListener(new MyOnClickListener());
        tv_coindetail_buycoins.setOnClickListener(new MyOnClickListener());
    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_coindetail_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.iv_coindetail_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.tv_coindetail_buycoins:
                    ((SecondPagerActivity) context).switchPage(6);
                    break;
            }
        }
    }
}
