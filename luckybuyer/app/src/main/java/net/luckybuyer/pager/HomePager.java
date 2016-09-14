package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.adapter.HomeImagePageAdapter;
import net.luckybuyer.adapter.HomeProductAdapter;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.GameProductBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.AutoTextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
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
    public List productList;
    private List mStringArray;
    int mLoopCount = 1;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case WHAT:
                    vp_home.setCurrentItem(vp_home.getCurrentItem() + 1);
                    handler.sendEmptyMessageDelayed(WHAT, 5000);
                    break;
                case WHAT_AUTO:
                    int i = mLoopCount % mStringArray.size();
                    atv_home_marquee.next();
                    atv_home_marquee.setText(mStringArray.get(i) + "");
                    mLoopCount ++;
                    handler.sendEmptyMessageDelayed(WHAT_AUTO,3000);
                    Log.e("TAG", "进来auto");
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

        handler.sendEmptyMessageDelayed(WHAT,5000);
        return inflate;


    }


    @Override
    public void initData() {
        super.initData();
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
        tv_home_share = (TextView) inflate.findViewById(R.id.tv_home_share);
        vp_home = (ViewPager) inflate.findViewById(R.id.vp_home);
        ll_home_point = (LinearLayout) inflate.findViewById(R.id.ll_home_point);
        rv_home_producer = (RecyclerView) inflate.findViewById(R.id.rv_home_producer);
        atv_home_marquee = (AutoTextView) inflate.findViewById(R.id.atv_home_marquee);


        //设置监听
        tv_home_share.setOnClickListener(new MyOnClickListener());
        vp_home.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void setView() {
        ImageView imageView;
        for (int i = 0;i < imageList.size();i++){
            imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.homepager_point_selector);
            ll_home_point.addView(imageView);
            if(i < 5) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = DensityUtil.px2dip(context,20);
                imageView.setLayoutParams(lp);
            }
        }
        ll_home_point.getChildAt(0).setEnabled(false);

        //设置viewpager
        vp_home.setAdapter(new HomeImagePageAdapter(imageList,context,vp_home));
        vp_home.setCurrentItem(imageList.size() * 100);

        //设置recycleView
        HomeProductAdapter homeProductAdapter = new HomeProductAdapter(context, productList);
        rv_home_producer.setAdapter(homeProductAdapter);

        //设置 recycleviewde manager   重写canScrollVertically方法是为了解决潜逃scrollview的卡顿问题
        rv_home_producer.setLayoutManager(new GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        homeProductAdapter.setOnClickListener(new HomeProductAdapter.OnClickListener() {
            @Override
            public void onclick(View view, int position) {
                Utils.MyToast(context, position + "长安");
            }

            @Override
            public void onLongClick(View view, int position) {
                Utils.MyToast(context, position + "长按");
            }
        });

        atv_home_marquee.setText(mStringArray.get(0) + "");
        handler.sendEmptyMessageDelayed(WHAT_AUTO, 3000);

    }

    private void startRequestGame() {
        String url = "https://api-staging.luckybuyer.net/v1/games/?per_page=20&page=1&timezone=utc";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            //解决乱码问题
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String data = new String(response.data, "UTF-8");
                    return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }

        };
        requestQueue.add(stringRequest);
    }

    //解析数据
    private void processData(String s) {
        Gson gson = new Gson();
        String game = "{\"game\":" + s + "}";
        GameProductBean gameProductBean = gson.fromJson(game, GameProductBean.class);
        //设置数据
        setData(gameProductBean);
        //设置视图
        setView();
    }

    private void setData(GameProductBean gameProductBean) {
        imageList = new ArrayList();

        for (int i = 0; i < 5; i++) {
            String detail_image = "http:" + gameProductBean.getGame().get(i).getProduct().getDetail_image();
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

        //产品列表数据
        productList = gameProductBean.getGame();
    }

    //点击监听
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_home_share:
                    Utils.MyToast(context, "SHARED");
                    break;
            }
        }
    }

    int curPostion = 0;

    //ViewPager页面改变监听
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            position = position%imageList.size();
            ll_home_point.getChildAt(curPostion).setEnabled(true);
            ll_home_point.getChildAt(position).setEnabled(false);
            curPostion = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state ==ViewPager.SCROLL_STATE_DRAGGING){
                handler.removeMessages(WHAT);
            }else if(state ==ViewPager.SCROLL_STATE_IDLE) {
                handler.removeMessages(WHAT);
                handler.sendEmptyMessageDelayed(WHAT,5000);
            }else if(state ==ViewPager.SCROLL_STATE_SETTLING){
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
