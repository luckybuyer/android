package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.CoinDetailPagerAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BasePager;
import net.iwantbuyer.bean.CoinDetailBean;
import net.iwantbuyer.bean.GameProductBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.BottomScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/18.
 */
public class CoinDetailPager extends BasePager {

    private RelativeLayout rl_coindetail_header;
    private LinearLayout ll_coindetail_back;
    private TextView tv_coindetail_balance;
    private TextView tv_coindetail_buycoins;
    private RecyclerView rv_coindetail;
    private View inflate;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;
    private BottomScrollView sv_coindetail;

    //下拉加载更多
    private LinearLayout ll_loading_data;
    private ProgressBar pb_loading_data;
    private TextView tv_loading_data;

    private List list = new ArrayList();

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_coindetail, null);
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
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
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
                    public void error(final int requestCode, String message) {
                        Log.e("TAG", requestCode + "");
                        Log.e("TAG", message);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rl_nodata.setVisibility(View.GONE);
                                rl_neterror.setVisibility(View.VISIBLE);
                                rl_loading.setVisibility(View.GONE);
                                Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "gold-records");

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

    boolean isMoreData = true;
    boolean isNeedpull = true;
    int page = 2;
    //处理数据
    private void processData(String response) {
        final Gson gson = new Gson();
        String str = "{\"coin\":" + response + "}";
        CoinDetailBean coinDetailBean = gson.fromJson(str, CoinDetailBean.class);

        if(coinDetailBean.getCoin().size() < 20) {
            ll_loading_data.setVisibility(View.GONE);
        }

        final CoinDetailPagerAdapter coinDetailPagerAdapter = new CoinDetailPagerAdapter(context, coinDetailBean.getCoin());
        rv_coindetail.setAdapter(coinDetailPagerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_coindetail.setLayoutManager(linearLayoutManager);


        //下拉加载
        sv_coindetail.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom && isMoreData && isNeedpull) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText(context.getString(R.string.loading___));

                    isNeedpull = false;
                    String url = MyApplication.url + "/v1/gold-records/?per_page=20&page= "+page+"&timezone=" + MyApplication.utc;
                    String token = Utils.getSpData("token", context);
                    Map map = new HashMap<String, String>();
                    map.put("Authorization", "Bearer " + token);
                    map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
                    HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String string) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    if (string.length() > 10) {
                                        String str = "{\"coin\":" + string + "}";
                                        CoinDetailBean coinDetail = gson.fromJson(str, CoinDetailBean.class);
                                        for (int i = 0; i < coinDetail.getCoin().size(); i++) {
                                            coinDetailPagerAdapter.list.add(coinDetail.getCoin().get(i));
                                            if (i == coinDetail.getCoin().size() - 1) {
                                                coinDetailPagerAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (coinDetail.getCoin().size() < 20) {
                                            isMoreData = false;
                                            pb_loading_data.setVisibility(View.GONE);
                                        }
                                        page++;
                                    } else {
                                        ll_loading_data.setVisibility(View.VISIBLE);
                                        pb_loading_data.setVisibility(View.GONE);
                                        tv_loading_data.setText(context.getString(R.string.Alreadyfullyloaded));
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ll_loading_data.setVisibility(View.GONE);
                                            }
                                        },3000);
                                    }

                                }
                            });
                        }

                        @Override
                        public void error(final int requestCode, final String message) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    pb_loading_data.setVisibility(View.GONE);
                                    tv_loading_data.setText(context.getString(R.string.Networkfailure));
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_loading_data.setVisibility(View.GONE);
                                        }
                                    },3000);
                                }
                            });
                        }

                        @Override
                        public void failure(Exception exception) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    pb_loading_data.setVisibility(View.GONE);
                                    tv_loading_data.setText(context.getString(R.string.Networkfailure));
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_loading_data.setVisibility(View.GONE);
                                        }
                                    },3000);
                                }
                            });

                        }

                    });
                }

            }
        });
    }

    private void findView() {
        rl_coindetail_header = (RelativeLayout) inflate.findViewById(R.id.rl_coindetail_header);
        ll_coindetail_back = (LinearLayout) inflate.findViewById(R.id.ll_coindetail_back);
        tv_coindetail_balance = (TextView) inflate.findViewById(R.id.tv_coindetail_balance);
        tv_coindetail_buycoins = (TextView) inflate.findViewById(R.id.tv_coindetail_buycoins);
        rv_coindetail = (RecyclerView) inflate.findViewById(R.id.rv_coindetail);

        tv_coindetail_balance.setText("$" + Utils.getSpData("balance", context));
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading= (RelativeLayout) inflate.findViewById(R.id.rl_loading);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        sv_coindetail = (BottomScrollView) inflate.findViewById(R.id.sv_coindetail);
        ll_loading_data = (LinearLayout) inflate.findViewById(R.id.ll_loading_data);
        pb_loading_data = (ProgressBar) inflate.findViewById(R.id.pb_loading_data);
        tv_loading_data = (TextView) inflate.findViewById(R.id.tv_loading_data);
        tv_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        //设置监听
        ll_coindetail_back.setOnClickListener(new MyOnClickListener());
        tv_coindetail_buycoins.setOnClickListener(new MyOnClickListener());
    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_coindetail_back:
                    ((SecondPagerActivity) context).finish();
//                    ((SecondPagerActivity)context).fragmentManager.popBackStack();
                    break;
                case R.id.tv_coindetail_buycoins:
                    ((SecondPagerActivity) context).from = "coindetailpager";
                    ((SecondPagerActivity) context).switchPage(6);
                    break;
            }
        }
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
