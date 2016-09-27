package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.HomeImagePageAdapter;
import net.luckybuyer.adapter.HomeProductAdapter;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.BannersBean;
import net.luckybuyer.bean.GameProductBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.AutoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class HomePager extends BasePager {
    private static final int WHAT = 1;
    private static final int WHAT_AUTO = 2;
    private RelativeLayout rl_home_header;
    private TextView tv_home_share;
    private ViewPager vp_home;                //轮播图
    private LinearLayout ll_home_point;       //轮播图上面的标记点
    private RecyclerView rv_home_producer;    //产品
    private AutoTextView atv_home_marquee;    //跑马灯
    private View inflate;

    //轮播图集合
    public List imageList;
    public List<GameProductBean.GameBean> productList;
    private List mStringArray;
    int mLoopCount = 1;

    private int batch_id;
    private int game_id;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    vp_home.setCurrentItem(vp_home.getCurrentItem() + 1);
                    handler.sendEmptyMessageDelayed(WHAT, 5000);
                    break;
                case WHAT_AUTO:
                    int i = mLoopCount % mStringArray.size();
                    atv_home_marquee.next();
                    atv_home_marquee.setText(mStringArray.get(i) + "");
                    mLoopCount++;
                    handler.sendEmptyMessageDelayed(WHAT_AUTO, 3000);
                    break;
            }
        }
    };

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_home, null);
        //发现视图  设置监听
        findView();
        setHeadMargin();

        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);
        //请求接口
        startRequestGame();

        //跑马灯数据
        mStringArray = new ArrayList<String>();
        mStringArray.add("139****9152 获得iPhone SE一部");
        mStringArray.add("159****8139 获得iPad2一部");
        mStringArray.add("134****7602 获得周杰伦演唱会门票一张");
        mStringArray.add("170****7758 获得MacBookPro一台");
    }


    //加载视图   并设置监听
    private void findView() {
        rl_home_header = (RelativeLayout) inflate.findViewById(R.id.rl_home_header);
//        tv_home_share = (TextView) inflate.findViewById(R.id.tv_home_share);
        vp_home = (ViewPager) inflate.findViewById(R.id.vp_home);
        ll_home_point = (LinearLayout) inflate.findViewById(R.id.ll_home_point);
        rv_home_producer = (RecyclerView) inflate.findViewById(R.id.rv_home_producer);
        atv_home_marquee = (AutoTextView) inflate.findViewById(R.id.atv_home_marquee);


        //设置监听
//        tv_home_share.setOnClickListener(new MyOnClickListener());
        vp_home.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    private void startRequestGame() {
        //请求 产品轮播图
        String bannersUrl = "https://api-staging.luckybuyer.net/v1/banners/?per_page=20&page=1&timezone=utc";
        HttpUtils.getInstance().getRequest(bannersUrl,null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processBannerData(response);
                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {

            }

        });

        //请求  产品  列表
        String url = "https://api-staging.luckybuyer.net/v1/games/?status=running&per_page=20&page=1&timezone=utc";
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {

            }

        });
    }

    private void processBannerData(String response) {
        Gson gson = new Gson();
        String banner = "{\"banner\":" + response + "}";
        BannersBean bannersBean = gson.fromJson(banner, BannersBean.class);

        handler.sendEmptyMessageDelayed(WHAT, 5000);       //开始轮播
        imageList = new ArrayList();
        for (int i = 0; i < bannersBean.getBanner().size()+2; i++) {
            String detail_image = "http:" + bannersBean.getBanner().get(0).getImage();
            ImageView image_header = new ImageView(context);
            Glide.with(context).load(detail_image).into(image_header);
            imageList.add(image_header);
            image_header.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://手指按下
                            handler.removeMessages(WHAT);
                            break;
                        case MotionEvent.ACTION_MOVE://手指滑动
                            handler.removeMessages(WHAT);
                            handler.sendEmptyMessageDelayed(WHAT, 5000);
                            break;
                        case MotionEvent.ACTION_CANCEL://事件丢失
//                            handler.removeCallbacksAndMessages(null);
//                            handler.sendEmptyMessageDelayed(0,3000);
                            break;
                        case MotionEvent.ACTION_UP://手指离开图片
                            handler.removeMessages(WHAT);
                            handler.sendEmptyMessageDelayed(WHAT, 5000);
                            break;
                    }
                    return true;
                }
            });

        }

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
        if(ll_home_point.getChildCount() > 0) {
            ll_home_point.getChildAt(0).setEnabled(false);
        }


        //设置viewpager
        vp_home.setAdapter(new HomeImagePageAdapter(imageList, context, vp_home));
        vp_home.setCurrentItem(imageList.size() * 100);
    }

    //解析数据
    private void processData(String s) {
        Gson gson = new Gson();
        String game = "{\"game\":" + s + "}";
        GameProductBean gameProductBean = gson.fromJson(game, GameProductBean.class);
        //设置视图

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
                Utils.MyToast(context, position + "长按");
            }
        });

        atv_home_marquee.setText(mStringArray.get(0) + "");
        handler.sendEmptyMessageDelayed(WHAT_AUTO, 3000);
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


    //根据版本判断是否 需要设置据顶部状态栏高度
    @TargetApi(19)
    private void setHeadMargin() {
        Rect frame = new Rect();
        //获取状态栏高度
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.e("TAhome", statusBarHeight + "");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = statusBarHeight;
        rl_home_header.setLayoutParams(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
