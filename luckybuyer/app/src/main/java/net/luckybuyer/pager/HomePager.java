package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.HomeImagePageAdapter;
import net.luckybuyer.adapter.HomeProductAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.BannersBean;
import net.luckybuyer.bean.BroadcastBean;
import net.luckybuyer.bean.GameProductBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.AutoTextHomeView;
import net.luckybuyer.view.AutoTextView;
import net.luckybuyer.view.RefreshableHelper;
import net.luckybuyer.view.RefreshableView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;

    //轮播图集合
    public List imageList;
    public List<GameProductBean.GameBean> productList;
    private List<SpannableStringBuilder> mStringArray;
    int mLoopCount = 1;

    private int batch_id;
    private int game_id;

    boolean isWaiting = true;
    boolean isFirst = true;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    vp_home.setCurrentItem(vp_home.getCurrentItem() + 1);
                    handler.sendEmptyMessageDelayed(WHAT, 5000);
                    break;
                case WHAT_AUTO:
                    handler.removeMessages(WHAT_AUTO);
                    if(mStringArray != null) {
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
        //请求接口
        startRequestGame();

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


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);

        //设置监听
        vp_home.setOnPageChangeListener(new MyOnPageChangeListener());

        atv_home_marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("from", "productdetail");
                intent.putExtra("game_id", broadcastBean.getBroad().get(mLoopCount % mStringArray.size()).getGame_id());
                context.startActivity(intent);

            }
        });
        tv_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWaiting = true;
                initData();
            }
        });
    }


    private void startRequestGame() {
        //请求  广播列表
        String broadcastUrl = MyApplication.url + "/v1/broadcasts/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(broadcastUrl, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processbroadcastData(response);

                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {

            }

            @Override
            public void failure(Exception exception) {

            }

        });


        //请求 产品轮播图
        String bannersUrl = MyApplication.url + "/v1/banners/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(bannersUrl, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processBannerData(response);
                        Log.e("TAG_banner", response);

                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {

            }

            @Override
            public void failure(Exception exception) {

            }

        });

        //请求  产品  列表
        String url = MyApplication.url + "/v1/games/?status=running&per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        srl_home_refresh.setRefreshing(false);

                            if (response.length() > 10) {
                                rl_keepout.setVisibility(View.GONE);
                                processData(response);
                                Log.e("TAG", response.length()+"");
                            } else {
                                rl_nodata.setVisibility(View.VISIBLE);
                                rl_neterror.setVisibility(View.GONE);
                                rl_loading.setVisibility(View.GONE);
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
                        if (isFirst) {
                            rl_nodata.setVisibility(View.GONE);
                            rl_neterror.setVisibility(View.VISIBLE);
                            rl_loading.setVisibility(View.GONE);
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
                        if (isFirst) {
                            rl_nodata.setVisibility(View.GONE);
                            rl_neterror.setVisibility(View.VISIBLE);
                            rl_loading.setVisibility(View.GONE);
                        }
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
            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.ff9c05));
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

        if(mStringArray.size() > 0) {
            atv_home_marquee.setText(mStringArray.get(0));
            handler.sendEmptyMessageDelayed(WHAT_AUTO, 5000);
        }

    }

    private void processBannerData(String response) {
        Gson gson = new Gson();
        String banner = "{\"banner\":" + response + "}";
        BannersBean bannersBean = gson.fromJson(banner, BannersBean.class);

        handler.sendEmptyMessageDelayed(WHAT, 5000);       //开始轮播
        imageList = new ArrayList();
        for (int i = 0; i < bannersBean.getBanner().size(); i++) {
            String detail_image = "http:" + bannersBean.getBanner().get(0).getImage();
            final ImageView image_header = new ImageView(context);
            if (!((MainActivity) context).isDestroyed()) {
                Glide.with(context).load(detail_image).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        image_header.setImageBitmap(resource);
                    }
                });
            }

//            Glide.with(context).load(detail_image).into(image_header);
            imageList.add(image_header);

        }

        ll_home_point.removeAllViews();
        ImageView imageView;
        for (int i = 0; i < imageList.size(); i++) {
            imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.homepager_point_selector);
            ll_home_point.addView(imageView);
            if (i < imageList.size()) {
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
        vp_home.setAdapter(new HomeImagePageAdapter(imageList, context, bannersBean.getBanner()));
        if (imageList.size() <= 1) {
            handler.removeMessages(WHAT);
            vp_home.setCurrentItem(0);
        } else {
            vp_home.setCurrentItem(imageList.size() * 100);
        }

    }

    //解析数据
    private void processData(String s) {
        //停止刷新
        srl_home_refresh.setRefreshing(false);

        Gson gson = new Gson();
        String game = "{\"game\":" + s + "}";
        GameProductBean gameProductBean = gson.fromJson(game, GameProductBean.class);

        //产品列表数据
        productList = gameProductBean.getGame();
        //设置recycleView
        HomeProductAdapter homeProductAdapter = new HomeProductAdapter(context, productList);
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
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

    }


    int curPostion = 0;

    //ViewPager页面改变监听
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            position = position % imageList.size();
            ll_home_point.getChildAt(curPostion).setEnabled(true);
            ll_home_point.getChildAt(position).setEnabled(false);
            curPostion = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (imageList.size() > 1) {
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
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(WHAT, 5000);
        if(mStringArray != null && mStringArray.size() > 0) {
            handler.sendEmptyMessageDelayed(WHAT_AUTO, 5000);
        }
    }
}
