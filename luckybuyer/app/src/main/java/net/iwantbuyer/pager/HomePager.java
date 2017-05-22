package net.iwantbuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.adapter.HomeImagePageAdapter;
import net.iwantbuyer.adapter.HomeProductAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.BannersBean;
import net.iwantbuyer.bean.BroadcastBean;
import net.iwantbuyer.bean.GameProductBean;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.AutoTextHomeView;
import net.iwantbuyer.view.BottomScrollView;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class HomePager extends BaseNoTrackPager {
    private static final int WHAT = 1;
    private static final int WHAT_AUTO = 2;
    private RelativeLayout rl_home_header;
    private ViewPager vp_home;                //轮播图
    private LinearLayout ll_home_point;       //轮播图上面的标记点
    private RecyclerView rv_home_producer;    //产品
    private AutoTextHomeView atv_home_marquee;    //跑马灯
    private View inflate;
    private SwipeRefreshLayout srl_home_refresh;
    private TextView tv_net_again;
    private TextView tv_list_net_again;
    private BottomScrollView sv_home;
    public TextView tv_home_country;
    private TabLayout tl_home_products;

    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;

    //网络连接错误 与没有数据
    private RelativeLayout rl_list_keepout;
    private RelativeLayout rl_list_neterror;
    private RelativeLayout rl_list_nodata;
    private RelativeLayout rl_list_loading;

    //下拉加载更多
    private LinearLayout ll_home_loading;
    private ProgressBar pb_loading_data;
    private TextView tv_loading_data;

    public List<GameProductBean.GameBean> productList = new ArrayList();
    private List<SpannableStringBuilder> mStringArray;
    int mLoopCount = 1;

    private ImageView iv_home_problem;

    private int batch_id;
    private int game_id;

    boolean isWaiting = true;

    int pager = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    if (bannersBean != null && bannersBean.getBanner().size() == 1) {
                        return;
                    }
                    vp_home.setCurrentItem(vp_home.getCurrentItem() + 1);
                    handler.sendEmptyMessageDelayed(WHAT, 5000);
                    break;
                case WHAT_AUTO:
                    handler.removeMessages(WHAT_AUTO);
                    if (mStringArray != null && mStringArray.size() != 0) {
                        int i = mLoopCount % mStringArray.size();
                        atv_home_marquee.next();
                        atv_home_marquee.setText(mStringArray.get(i));
                        mLoopCount++;
                        handler.sendEmptyMessageDelayed(WHAT_AUTO, 5000);
                    }
                    break;
            }
        }
    };
    private BroadcastBean broadcastBean;
    private BannersBean bannersBean;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_home, null);
        //发现视图  设置监听
        findView();
        srl_home_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });



        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        handler.removeCallbacksAndMessages(null);
        if (isWaiting) {
            rl_keepout.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.GONE);
            rl_neterror.setVisibility(View.GONE);
            rl_loading.setVisibility(View.VISIBLE);
            isWaiting = false;
        } else {
            srl_home_refresh.setRefreshing(true);
            rv_home_producer.setFocusable(false);            //让recycleview时区焦点 不然show fragment的时候recycleview会顶在顶部
        }

        //重置 请求  下拉刷新数据
        isMoreData = true;
        isNeedpull = true;
        page = 2;
        //请求接口
        startRequestGame();
        if (pager == 0) {
            startAll();
        } else if (pager == 1) {
            startProgress();
        } else if (pager == 2) {
            startNew();
        }

        isLast = false;
    }


    //加载视图   并设置监听
    private void findView() {
        rl_home_header = (RelativeLayout) inflate.findViewById(R.id.rl_home_header);
//        tv_home_share = (TextView) inflate.findViewById(R.id.tv_home_share);
        vp_home = (ViewPager) inflate.findViewById(R.id.vp_home);
        ll_home_point = (LinearLayout) inflate.findViewById(R.id.ll_home_point);
        rv_home_producer = (RecyclerView) inflate.findViewById(R.id.rv_home_producer);
        atv_home_marquee = (AutoTextHomeView) inflate.findViewById(R.id.atv_home_marquee);
        srl_home_refresh = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_home_refresh);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        tv_list_net_again = (TextView) inflate.findViewById(R.id.tv_list_net_again);
        sv_home = (BottomScrollView) inflate.findViewById(R.id.sv_home);
        tv_home_country = (TextView) inflate.findViewById(R.id.tv_home_country);
        tl_home_products = (TabLayout) inflate.findViewById(R.id.tl_home_products);

        ll_home_loading = (LinearLayout) inflate.findViewById(R.id.ll_home_loading);
        pb_loading_data = (ProgressBar) inflate.findViewById(R.id.pb_loading_data);
        tv_loading_data = (TextView) inflate.findViewById(R.id.tv_loading_data);


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);

        rl_list_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_list_keepout);
        rl_list_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_list_neterror);
        rl_list_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_list_nodata);
        rl_list_loading = (RelativeLayout) inflate.findViewById(R.id.rl_list_loading);

        iv_home_problem = (ImageView) inflate.findViewById(R.id.iv_home_problem);

        tv_home_country.setText(Utils.getSpData("country", context));

        //设置监听
        vp_home.setOnPageChangeListener(new MyOnPageChangeListener());

        atv_home_marquee.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
        tv_list_net_again.setOnClickListener(new MyOnClickListener());
        tv_home_country.setOnClickListener(new MyOnClickListener());

        iv_home_problem.setOnClickListener(new MyOnClickListener());
        tl_home_products.addTab(tl_home_products.newTab().setText(context.getString(R.string.All)), 0);
        tl_home_products.addTab(tl_home_products.newTab().setText(context.getString(R.string.progress)), 1);
        tl_home_products.addTab(tl_home_products.newTab().setText(context.getString(R.string.New)), 2);
        tl_home_products.addOnTabSelectedListener(new MyOnTabSelectedListener());

        vp_home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        srl_home_refresh.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        srl_home_refresh.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.atv_home_marquee:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("game_id", broadcastBean.getBroad().get(mLoopCount % mStringArray.size()).getGame_id());
                    context.startActivity(intent);
                    break;
                case R.id.tv_net_again:
                    isWaiting = true;
                    initData();
                    break;
                case R.id.tv_home_country:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "countrypager");
                    startActivity(intent);
                    break;
                case R.id.iv_home_problem:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "problem");
                    startActivity(intent);
                    break;
                case R.id.tv_list_net_again:
                    int id = tl_home_products.getSelectedTabPosition();
                    Log.e("TAG_listagain", "" + id);
                    if (id == 0) {
                        startAll();
                    } else if (id == 1) {
                        startProgress();
                    } else if (id == 2) {
                        startNew();
                    }
                    break;
            }
        }
    }

    //点击tablayout的监听
    class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            productList.clear();
            HttpUtils.getInstance().startNetworkWaiting(context);
            switch (tab.getPosition()) {
                case 0:
                    //请求ALL
                    startAll();
                    isLast = false;
                    break;
                case 1:
                    //请求Progress
                    productList.clear();
                    isLast = false;
                    startProgress();
                    break;
                case 2:
                    //New
                    isLast = false;
                    productList.clear();
                    startNew();
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private void startRequestGame() {
        String spString = Utils.getSpData("locale", context);
        String language = "en";
        if (spString != null && "ms".equals(spString.split("-")[0] + "")) {
            language = "ms";
        } else if (spString != null && "zh".equals(spString.split("-")[0] + "")) {
            language = "zh";
        } else if (spString != null && "en".equals(spString.split("-")[0] + "")) {
            language = "en";
        }

        //请求  广播列表
        String broadcastUrl = MyApplication.url + "/v1/broadcasts/?per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap();

        HttpUtils.getInstance().getRequest(broadcastUrl, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response, String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.length() > 10) {
                            rl_keepout.setVisibility(View.GONE);
                            processbroadcastData(response);

                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                            rl_loading.setVisibility(View.GONE);
                        }

                    }
                });
            }

            @Override
            public void error(final int requestCode, String message) {
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

        });


        //请求 产品轮播图
        responseBanner();

    }

    public void startAll() {
        //请求  产品  列表
        String url = MyApplication.url + "/v1/games/?status=running&order_by=all&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map ma = new HashMap();
        HttpUtils.getInstance().getRequest(url, ma, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response, final String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        HttpUtils.getInstance().stopNetWorkWaiting();
                        srl_home_refresh.setRefreshing(false);

                        if (response.length() > 10) {
                            rl_list_keepout.setVisibility(View.GONE);
                            HomePager.this.link = link;
                            processData(response);
//                            tl_home_products.getTabAt(0).select();
                            pager = 0;
                        } else {
                            rl_list_nodata.setVisibility(View.VISIBLE);
                            rl_list_neterror.setVisibility(View.GONE);
                            rl_list_loading.setVisibility(View.GONE);
                        }

                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_home_refresh.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_list_keepout.setVisibility(View.VISIBLE);
                        rl_list_nodata.setVisibility(View.GONE);
                        rl_list_neterror.setVisibility(View.VISIBLE);
                        rl_list_loading.setVisibility(View.GONE);
                        productList.clear();
                        if (homeProductAdapter != null) {
                            homeProductAdapter.notifyDataSetChanged();
                        }

                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_home_refresh.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_list_keepout.setVisibility(View.VISIBLE);
                        rl_list_nodata.setVisibility(View.GONE);
                        rl_list_neterror.setVisibility(View.VISIBLE);
                        rl_list_loading.setVisibility(View.GONE);
                        productList.clear();
                        if (homeProductAdapter != null) {
                            homeProductAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

        });
    }

    public void startProgress() {
        //请求  产品  列表
    String url = MyApplication.url + "/v1/games/?status=running&order_by=progress&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map ma = new HashMap();
        HttpUtils.getInstance().getRequest(url, ma, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response, final String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        srl_home_refresh.setRefreshing(false);

                        if (response.length() > 10) {
                            rl_list_keepout.setVisibility(View.GONE);
                            HomePager.this.link = link;
                            processData(response);
//                            tl_home_products.getTabAt(1).select();
                            pager = 1;
                        } else {
                            rl_list_nodata.setVisibility(View.VISIBLE);
                            rl_list_neterror.setVisibility(View.GONE);
                            rl_list_loading.setVisibility(View.GONE);
//                            }
                        }

                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_home_refresh.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_list_keepout.setVisibility(View.VISIBLE);
                        rl_list_nodata.setVisibility(View.GONE);
                        rl_list_neterror.setVisibility(View.VISIBLE);
                        rl_list_loading.setVisibility(View.GONE);
                        productList.clear();
                        if (homeProductAdapter != null) {
                            homeProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_home_refresh.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_list_keepout.setVisibility(View.VISIBLE);
                        rl_list_nodata.setVisibility(View.GONE);
                        rl_list_neterror.setVisibility(View.VISIBLE);
                        rl_list_loading.setVisibility(View.GONE);
                        productList.clear();
                        if (homeProductAdapter != null) {
                            homeProductAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

        });
    }

    public void startNew() {
        //请求  产品  列表
        String url = MyApplication.url + "/v1/games/?status=running&order_by=latest&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map ma = new HashMap();
        HttpUtils.getInstance().getRequest(url, ma, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response, final String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        srl_home_refresh.setRefreshing(false);

                        if (response.length() > 10) {
                            rl_list_keepout.setVisibility(View.GONE);
                            HomePager.this.link = link;
                            processData(response);
//                            tl_home_products.getTabAt(2).select();
                            pager = 2;
                        } else {
                            rl_list_nodata.setVisibility(View.VISIBLE);
                            rl_list_neterror.setVisibility(View.GONE);
                            rl_list_loading.setVisibility(View.GONE);
//                            }
                        }

                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_home_refresh.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_list_keepout.setVisibility(View.VISIBLE);
                        rl_list_nodata.setVisibility(View.GONE);
                        rl_list_neterror.setVisibility(View.VISIBLE);
                        rl_list_loading.setVisibility(View.GONE);
                        productList.clear();
                        if (homeProductAdapter != null) {
                            homeProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_home_refresh.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_list_keepout.setVisibility(View.VISIBLE);
                        rl_list_nodata.setVisibility(View.GONE);
                        rl_list_neterror.setVisibility(View.VISIBLE);
                        rl_list_loading.setVisibility(View.GONE);
                        productList.clear();
                        if (homeProductAdapter != null) {
                            homeProductAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

        });
    }

    private void startMore(String url) {
        Map map = new HashMap();

        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String string, final String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_lll", link);
                        isNeedpull = true;
                        HomePager.this.link = link;
                        Gson gson = new Gson();
                        final String game = "{\"game\":" + string + "}";
                        GameProductBean gameProductBean = gson.fromJson(game, GameProductBean.class);

                        //产品列表数据
                        productList.addAll(gameProductBean.getGame());
                        homeProductAdapter.notifyDataSetChanged();
                        ll_home_loading.setVisibility(View.GONE);


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
                                ll_home_loading.setVisibility(View.GONE);
                            }
                        }, 3000);
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
                                ll_home_loading.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }
                });

            }

        });
    }

    //请求产品轮播图
    public void responseBanner() {
        String bannersUrl = MyApplication.url + "/v1/banners/?per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap();
        HttpUtils.getInstance().getRequest(bannersUrl, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response, String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processBannerData(response);

                    }
                });
            }

            @Override
            public void error(final int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

        });
    }

    //广播请求
    private void processbroadcastData(String response) {
        response = "{\"broad\":" + response + "}";
        Gson gson = new Gson();
        broadcastBean = gson.fromJson(response, BroadcastBean.class);

        SpannableStringBuilder builder = null;
        //需要变颜色的字符串
        //跑马灯数据
        mStringArray = new ArrayList();
        for (int i = 0; i < broadcastBean.getBroad().size(); i++) {
            String str = broadcastBean.getBroad().get(i).getTemplate();
            //获取内容字符串
            String content = broadcastBean.getBroad().get(i).getContent();
            builder = new SpannableStringBuilder(content);
            ForegroundColorSpan redSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ff9c05));
            ForegroundColorSpan blackSpan = null;
            builder.setSpan(redSpan, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String[] st = str.split("\\}");
            for (int j = 0; j < st.length; j++) {
                int begin = 0;
                int end = 0;
                if (st[j].contains("{")) {
                    String[] s = st[j].split("\\{");
                    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                    begin = content.indexOf(s[0]);
                    end = begin + s[0].length();

                }
                blackSpan = new ForegroundColorSpan(Color.BLACK);
                builder.setSpan(blackSpan, begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mStringArray.add(builder);
        }

        if (mStringArray.size() > 0) {
            atv_home_marquee.setText(mStringArray.get(0));
            handler.sendEmptyMessageDelayed(WHAT_AUTO, 5000);
        }

    }

    private void processBannerData(String response) {
        Gson gson = new Gson();
        String banner = "{\"banner\":" + response + "}";
        bannersBean = gson.fromJson(banner, BannersBean.class);

        ll_home_point.removeAllViews();
        ImageView imageView;
        for (int i = 0; i < bannersBean.getBanner().size(); i++) {
            imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.homepager_point_selector);
            ll_home_point.addView(imageView);
            if (i < bannersBean.getBanner().size()) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.leftMargin = DensityUtil.px2dip(context, 20);
                imageView.setLayoutParams(lp);
            }
        }
        if (ll_home_point.getChildCount() > 0) {
            ll_home_point.getChildAt(0).setEnabled(false);
        }


        //设置viewpager
        vp_home.setAdapter(new HomeImagePageAdapter(context, bannersBean.getBanner()));
        if (bannersBean.getBanner().size() <= 1) {
            handler.removeMessages(WHAT);
            vp_home.setCurrentItem(0);
        }
//        else {
//            vp_home.setCurrentItem(imageList.size() * 100);
//        }

    }

    boolean isMoreData = true;
    boolean isNeedpull = true;
    boolean isLast = false;
    int page = 2;
    String link;
    HomeProductAdapter homeProductAdapter;

    //解析数据
    private void processData(String s) {
        //停止刷新
        srl_home_refresh.setRefreshing(false);


        final Gson gson = new Gson();
        final String game = "{\"game\":" + s + "}";
        GameProductBean gameProductBean = gson.fromJson(game, GameProductBean.class);

        //产品列表数据
        productList = gameProductBean.getGame();

        if (productList != null && productList.size() < 20) {
            ll_home_loading.setVisibility(View.GONE);
            ll_home_loading.setVisibility(View.GONE);
        }

        //设置recycleView
        homeProductAdapter = new HomeProductAdapter(context, productList);
        rv_home_producer.setAdapter(homeProductAdapter);

        //设置 recycleviewde manager   重写canScrollVertically方法是为了解决潜逃scrollview的卡顿问题
        rv_home_producer.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        homeProductAdapter.setOnClickListener(new HomeProductAdapter.OnClickListener() {
            @Override
            public void onclick(View view, int position) {
                batch_id = productList.get(position).getBatch_id();
                game_id = productList.get(position).getId();
                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("from", "productdetail");
                intent.putExtra("batch_id", batch_id);
                intent.putExtra("game_id", game_id);
                startActivity(intent);


                //facebook打点
                AppEventsLogger logger = AppEventsLogger.newLogger(context);
                Bundle params = new Bundle();
                params.putString("id", productList.get(position).getProduct().getId() + "");
                logger.logEvent("Click_productID", params);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        //下拉加载
        sv_home.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {

                String next = "";
                String last = null;
                String[] str = link.split(",");
                for (int i = 0; i < str.length; i++) {
                    if (str[i].contains("rel=\"next\"")) {
                        next = str[i].substring(str[i].indexOf("<") + 1, str[i].indexOf(">"));
                    }
                    if (str[i].contains("rel=\"last\"")) {
                        last = str[i].substring(str[i].indexOf("<") + 1, str[i].indexOf(">"));
                    }
                }

                Log.e("TAG__", isLast + "--" + next.equals(last));
                if (link.contains("rel=\"next\"") && isNeedpull && isBottom && !isLast) {
                    isNeedpull = false;
                    ll_home_loading.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText(context.getString(R.string.loading___));

                    String[] st = link.split(",");
                    for (int i = 0; i < str.length; i++) {
                        if (st[i].contains("rel=\"next\"")) {
                            String url = st[i].substring(st[i].indexOf("<") + 1, st[i].indexOf(">"));
                            startMore(url);
                        }
                    }
                    if(next.equals(last)) {
                        isLast = true;
                    }
                } else if (link.contains("rel=\"last\"") && isLast && isNeedpull) {
                    isNeedpull = false;
                    ll_home_loading.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.GONE);
                    tv_loading_data.setText(context.getString(R.string.nomoreproduct));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll_home_loading.setVisibility(View.GONE);
                            isNeedpull = true;
                        }
                    }, 3000);
                }

            }
        });

    }


    //ViewPager页面改变监听
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int total = bannersBean.getBanner().size();
            for (int i = 0; i < total; i++) {
                if (i == position % total) {
                    ll_home_point.getChildAt(i).setEnabled(false);
                } else {
                    ll_home_point.getChildAt(i).setEnabled(true);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (bannersBean != null && bannersBean.getBanner().size() > 1) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    handler.removeMessages(WHAT);
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    handler.removeMessages(WHAT);
                    handler.sendEmptyMessageDelayed(WHAT, 5000);
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    handler.removeMessages(WHAT);
                }
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = sbar;
        rl_home_header.setLayoutParams(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        Log.e("TAG——fragment", "fragment_destroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(WHAT, 5000);
        if (mStringArray != null && mStringArray.size() > 0) {
            handler.sendEmptyMessageDelayed(WHAT_AUTO, 5000);
        }
    }
}
