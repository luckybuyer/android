package net.luckybuyer.secondpager;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.HomeProductAdapter;
import net.luckybuyer.adapter.ProductDetailAdapter;
import net.luckybuyer.bean.ProductOrderBean;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.ProductDetailBean;
import net.luckybuyer.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/17.
 */
public class ProductDetailPager extends BasePager{

    public static final int WHAT = 1;
    private ViewPager vp_productdetail;               //轮播图    暂时只有一张图片  暂时不设置
    private LinearLayout ll_productdetail_point;      //轮播图点
    private TextView tv_productdetail_producttitle;
    private TextView tv_productdetail_issue;           //当前轮
    private TextView tv_productdetail_totalicon;      //总数
    private TextView tv_productdetail_icon;           //剩余金币
    private ProgressBar pb_productdetail_progress;    //比率
    private RelativeLayout rl_productdetail_iteminformation;     //商品详情介绍
    private RelativeLayout rl_productdetail_share;               //晒单分享
    private RelativeLayout rl_productdetail_announced;           //往期揭晓
    private RecyclerView rv_productdetail;                       //参与记录
    private ImageView iv_productdetail_image;                    //顶部图片
    private RelativeLayout rl_productdetail_indsertcoins;
    private View inflate;

    private List imageList = new ArrayList();

//    private Handler handler = new Handler(){
//
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case WHAT:
//                    vp_productdetail.setCurrentItem(vp_productdetail.getCurrentItem() + 1);
//                    handler.sendEmptyMessageDelayed(WHAT, 5000);
//                    break;
//            }
//        }
//    };

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_productdetail, null);
        findView();

        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);

        int batch_id = ((SecondPagerActivity)context).batch_id;
        String url = "https://api-staging.luckybuyer.net/v1/games/?status=running&batch_id=" + batch_id +"&per_page=20&page=1&timezone=utc";
        HttpUtils.getInstance().getRequest(url, new HttpUtils.OnRequestListener() {
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
            public void error(String error) {

            }
        });

        String listUrl = "https://api-staging.luckybuyer.net/v1/games/"+ 10 +"/public-orders/?per_page=20&page=1&timezone=utc";
        HttpUtils.getInstance().getRequest(listUrl, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processListData(response);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                    }
                });
            }

            @Override
            public void error(String error) {

            }
        });
    }


    //发现视图  设置监听
    private void findView() {
        vp_productdetail = (ViewPager) inflate.findViewById(R.id.vp_productdetail);
        ll_productdetail_point = (LinearLayout) inflate.findViewById(R.id.ll_productdetail_point);
        tv_productdetail_producttitle = (TextView) inflate.findViewById(R.id.tv_productdetail_producttitle);
        tv_productdetail_issue = (TextView) inflate.findViewById(R.id.tv_productdetail_issue);
        tv_productdetail_totalicon = (TextView) inflate.findViewById(R.id.tv_productdetail_totalicon);
        tv_productdetail_icon = (TextView) inflate.findViewById(R.id.tv_productdetail_icon);
        pb_productdetail_progress = (ProgressBar) inflate.findViewById(R.id.pb_productdetail_progress);
        rl_productdetail_iteminformation = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_iteminformation);
        rl_productdetail_share = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_share);
        rl_productdetail_announced = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_announced);
        rv_productdetail = (RecyclerView) inflate.findViewById(R.id.rv_productdetail);
        iv_productdetail_image = (ImageView) inflate.findViewById(R.id.iv_productdetail_image);
        rl_productdetail_indsertcoins = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_indsertcoins);


        rl_productdetail_indsertcoins.setOnClickListener(new MyOnClickListener());
        rl_productdetail_iteminformation.setOnClickListener(new MyOnClickListener());
        rl_productdetail_share.setOnClickListener(new MyOnClickListener());
        rl_productdetail_announced.setOnClickListener(new MyOnClickListener());

//        vp_productdetail.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    //解析数据
    private void processData(String s) {
//        handler.sendEmptyMessageDelayed(WHAT, 5000);
        Gson gson = new Gson();
        String game = "{\"productdetail\":" + s + "}";
        ProductDetailBean productDetailBean = gson.fromJson(game, ProductDetailBean.class);
//
//        imageList = new ArrayList();
//
//        for (int i = 0; i < 2; i++) {
//            ImageView image_header = new ImageView(context);
//            String imgUrl = "http:" + productDetailBean.getProductdetail().get(0).getProduct().getDetail_image();
//            Log.e("TAG_imgurl", imgUrl);
//            Glide.with(context).load(imgUrl).into(image_header);
//            imageList.add(image_header);
//            image_header.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN://手指按下
//                            handler.removeMessages(WHAT);
//                            break;
//                        case MotionEvent.ACTION_MOVE://手指滑动
//                            handler.removeMessages(WHAT);
//                            handler.sendEmptyMessageDelayed(WHAT, 5000);
//                            break;
//                        case MotionEvent.ACTION_CANCEL://事件丢失
////                            handler.removeCallbacksAndMessages(null);
////                            handler.sendEmptyMessageDelayed(0,3000);
//                            break;
//                        case MotionEvent.ACTION_UP://手指离开图片
//                            handler.removeMessages(WHAT);
//                            handler.sendEmptyMessageDelayed(WHAT, 5000);
//                            break;
//                    }
//                    return true;
//                }
//            });
//        }
//        ImageView imageView;
//        for (int i = 0;i < imageList.size();i++){
//            imageView = new ImageView(context);
//            imageView.setBackgroundResource(R.drawable.homepager_point_selector);
//            ll_productdetail_point.addView(imageView);
//            if(i < imageList.size()) {
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                lp.leftMargin = DensityUtil.px2dip(context, 20);
//                imageView.setLayoutParams(lp);
//            }
//        }
//        ll_productdetail_point.getChildAt(0).setEnabled(false);
//
//        //设置viewpager
//        vp_productdetail.setAdapter(new HomeImagePageAdapter(imageList, context, vp_productdetail));
//        vp_productdetail.setCurrentItem(imageList.size() * 100);

        String imgUrl = "http:" + productDetailBean.getProductdetail().get(0).getProduct().getDetail_image();
        Glide.with(context).load(imgUrl).into(iv_productdetail_image);

        tv_productdetail_producttitle.setText(productDetailBean.getProductdetail().get(0).getProduct().getTitle());

        tv_productdetail_issue.setText("Issue:" + productDetailBean.getProductdetail().get(0).getIssue_id());

        tv_productdetail_totalicon.setText("Total demand:" + productDetailBean.getProductdetail().get(0).getShares() + "coins");

        tv_productdetail_icon.setText("Remaining:" + productDetailBean.getProductdetail().get(0).getLeft_shares());

        pb_productdetail_progress.setMax(productDetailBean.getProductdetail().get(0).getShares());
        pb_productdetail_progress.setProgress(productDetailBean.getProductdetail().get(0).getShares() - productDetailBean.getProductdetail().get(0).getLeft_shares());



    }

    private void processListData(String response) {
        Gson gson = new Gson();
        String productorder = "{\"productorder\":" + response + "}";
        ProductOrderBean productOrderBean = gson.fromJson(productorder, ProductOrderBean.class);


        ProductDetailAdapter productDetailAdapter = new ProductDetailAdapter(context, productOrderBean.getProductorder());
        rv_productdetail.setAdapter(productDetailAdapter);

        //设置 recycleviewde manager   重写canScrollVertically方法是为了解决潜逃scrollview的卡顿问题
        rv_productdetail.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            SecondPagerActivity activity = (SecondPagerActivity) context;
            switch (view.getId()){
                case R.id.rl_productdetail_iteminformation:   //商品详情介绍
                    activity.from = "productdetail";
                    activity.switchPage(1);
                    break;
                case R.id.rl_productdetail_share:             //晒单分享
                    activity.from = "productdetail";
                    activity.switchPage(2);
                    break;
                case R.id.rl_productdetail_announced:          //往期回顾
                    activity.from = "productdetail";
                    activity.switchPage(3);
                    break;
                case R.id.rl_productdetail_indsertcoins:
                    activity.from = "productdetail";
                    activity.switchPage(4);
                    break;
            }
        }
    }
//    int curPostion = 0;
//    //ViewPager页面改变监听
//    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            position = position%imageList.size();
//            ll_productdetail_point.getChildAt(curPostion).setEnabled(true);
//            ll_productdetail_point.getChildAt(position).setEnabled(false);
//            curPostion = position;
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            if(state ==ViewPager.SCROLL_STATE_DRAGGING){
//                handler.removeMessages(WHAT);
//            }else if(state ==ViewPager.SCROLL_STATE_IDLE) {
//                handler.removeMessages(WHAT);
//                handler.sendEmptyMessageDelayed(WHAT,5000);
//            }else if(state ==ViewPager.SCROLL_STATE_SETTLING){
//                handler.removeMessages(WHAT);
//            }
//        }
//    }
}
