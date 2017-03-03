package net.iwantbuyer.pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.adapter.WinningAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.CoinDetailBean;
import net.iwantbuyer.bean.PreviousWinnerBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.BottomScrollView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/13.
 */
public class WinningPager extends BaseNoTrackPager {

    private View inflate;
    private RelativeLayout rl_winning_header;
    private RecyclerView rv_winning;
    private SwipeRefreshLayout srl_winning;
    boolean isNeedNetWaiting = true;

    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private BottomScrollView sv_winning;

    private LinearLayout ll_loading_data;
    private ProgressBar pb_loading_data;
    private TextView tv_loading_data;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_winning, null);
        findView();
        srl_winning.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        isNeedNetWaiting = true;

        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track("PAGE:new_result", props);
        }catch (Exception e){
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        //AppFlyer 埋点
        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context, "Page:Results",eventValue);

        return inflate;
    }


    @Override
    public void initData() {
        super.initData();

        if (isNeedNetWaiting) {
            rl_keepout.setVisibility(View.VISIBLE);
            rl_neterror.setVisibility(View.GONE);
            rl_nodata.setVisibility(View.GONE);
            rl_loading.setVisibility(View.VISIBLE);
            isNeedNetWaiting = false;
        } else {
            srl_winning.setRefreshing(true);
        }

        String url = MyApplication.url + "/v1/games/?status=closed&status=finished&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap();
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_winning.setRefreshing(false);

                        if (response.length() > 10) {
                            processData(response);
                            rl_keepout.setVisibility(View.GONE);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                            rl_loading.setVisibility(View.GONE);


                        }
                    }
                });
            }

            @Override
            public void error(final int code, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_winning.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Utils.MyToast(context,context.getString(R.string.Networkfailure) + code + "games");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_winning.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Utils.MyToast(context,context.getString(R.string.Networkfailure));
                    }
                });
            }
        });
    }

    private void findView() {
        rl_winning_header = (RelativeLayout) inflate.findViewById(R.id.rl_winning_header);
        rv_winning = (RecyclerView) inflate.findViewById(R.id.rv_winning);
        srl_winning = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_winning);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        sv_winning = (BottomScrollView) inflate.findViewById(R.id.sv_winning);
        ll_loading_data = (LinearLayout) inflate.findViewById(R.id.ll_loading_data);
        pb_loading_data = (ProgressBar) inflate.findViewById(R.id.pb_loading_data);
        tv_loading_data = (TextView) inflate.findViewById(R.id.tv_loading_data);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNeedNetWaiting = true;
                initData();
            }
        });
    }

    boolean isMoreData = true;
    boolean isNeedpull = true;
    int page = 2;

    private void processData(String response) {
        response = "{\"previous\":" + response + "}";
        final Gson gson = new Gson();
        PreviousWinnerBean previousWinnerBean = gson.fromJson(response, PreviousWinnerBean.class);
        if(previousWinnerBean.getPrevious().size() < 20) {
            ll_loading_data.setVisibility(View.GONE);
        }
        final WinningAdapter winningAdapter = new WinningAdapter(context, previousWinnerBean.getPrevious());
        rv_winning.setAdapter(winningAdapter);
        rv_winning.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        //下拉加载
        sv_winning.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom && isMoreData && isNeedpull) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText(context.getString(R.string.loading___));

                    isNeedpull = false;
                    String url = MyApplication.url + "/v1/games/?status=closed&status=finished&per_page=20&page="+page+"&timezone=" + MyApplication.utc;
                    Map map = new HashMap();
                    map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
                    HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String string) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    if (string.length() > 10) {
                                        String str = "{\"previous\":" + string + "}";
                                        PreviousWinnerBean previousWinnerBean = gson.fromJson(str, PreviousWinnerBean.class);
                                        for (int i = 0; i < previousWinnerBean.getPrevious().size(); i++) {
                                            winningAdapter.list.add(previousWinnerBean.getPrevious().get(i));
                                            if (i == previousWinnerBean.getPrevious().size() - 1) {
                                                winningAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (previousWinnerBean.getPrevious().size() < 20) {
                                            isMoreData = false;
                                            ll_loading_data.setVisibility(View.GONE);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
