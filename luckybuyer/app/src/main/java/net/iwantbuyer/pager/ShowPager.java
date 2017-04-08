package net.iwantbuyer.pager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.adapter.ShowAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.ShownBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.BottomScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/7.
 */
public class ShowPager extends BaseNoTrackPager{

    private RecyclerView rv_show;
    private View view_show;
    private View inflate;
    private SwipeRefreshLayout srl_show;
    private BottomScrollView sv_show;

    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private LinearLayout ll_loading_data;
    private ProgressBar pb_loading_data;
    private TextView tv_loading_data;

    private List list;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_show,null);
        findView();
        srl_show.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        //AppFlyer 埋点
        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(context, "Page: Show",eventValue);

        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList();
        String url = null;
        if(context instanceof ThirdPagerActivity) {
            url = MyApplication.url + "/v1/posts/?product_id= "+((ThirdPagerActivity)context).product_id+"&per_page=20&page=1&timezone=" + MyApplication.utc;
        }else {
            url = MyApplication.url + "/v1/posts/?per_page=20&page=1&timezone=" + MyApplication.utc;
        }

        Map map = new HashMap();
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");

        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.length() > 10) {
                            processData(response);
                            rl_keepout.setVisibility(View.GONE);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                            rl_loading.setVisibility(View.GONE);
                        }
                        srl_show.setRefreshing(false);
                    }
                });

            }

            @Override
            public void error(final int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_show.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Utils.MyToast(context,context.getString(R.string.Networkfailure) + requestCode + "posts");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_show.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                        Utils.MyToast(context,context.getString(R.string.Networkfailure));
                    }
                });
            }
        });

        //重置 请求  下拉刷新数据
        isMoreData = true;
        isNeedpull = true;
        page = 2;

    }


    private void findView() {
        rv_show = (RecyclerView) inflate.findViewById(R.id.rv_show);
        view_show = (View) inflate.findViewById(R.id.view_show);
        srl_show = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_show);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);

        sv_show = (BottomScrollView) inflate.findViewById(R.id.sv_show);
        ll_loading_data = (LinearLayout) inflate.findViewById(R.id.ll_loading_data);
        pb_loading_data = (ProgressBar) inflate.findViewById(R.id.pb_loading_data);
        tv_loading_data = (TextView) inflate.findViewById(R.id.tv_loading_data);

        tv_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

    }
    boolean isMoreData = true;
    boolean isNeedpull = true;
    int page = 2;
    private void processData(String response) {
        final Gson gson = new Gson();
        response = "{\"show\":" + response + "}";
        ShownBean shownBean = gson.fromJson(response, ShownBean.class);

        final ShowAdapter showAdapter = new ShowAdapter(context,shownBean.getShow(),view_show);
        rv_show.setAdapter(showAdapter);
        rv_show.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        if(shownBean.getShow().size() <= 3) {
            ll_loading_data.setVisibility(View.GONE);
        }
        //下拉加载
        sv_show.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom && isMoreData && isNeedpull) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText(context.getString(R.string.loading___));

                    isNeedpull = false;
                    String url = null;
                    if(context instanceof ThirdPagerActivity) {
                        url = MyApplication.url + "/v1/posts/?product_id= "+((ThirdPagerActivity)context).product_id+"&per_page=20&page="+page+"&timezone=" + MyApplication.utc;
                        Log.e("TAG_晒单数据", url);
                    }else {
                        url = MyApplication.url + "/v1/posts/?per_page=20&page="+page+"&timezone=" + MyApplication.utc;
                    }

                    Map map = new HashMap();
                    map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");

                    HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String string,String link) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    if (string.length() > 10) {
                                        String str = "{\"show\":" + string + "}";
                                        ShownBean shownBean = gson.fromJson(str, ShownBean.class);
                                        for (int i = 0; i < shownBean.getShow().size(); i++) {
                                            showAdapter.list.add(shownBean.getShow().get(i));
                                            if (i == shownBean.getShow().size() - 1) {
                                                showAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (shownBean.getShow().size() < 20) {
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
