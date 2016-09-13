package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.adapter.HomeImagePageAdapter;
import net.luckybuyer.adapter.HomeProductAdapter;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.Utils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class HomePager extends BasePager {
    private static final int WHAT = 1;
    private RelativeLayout rl_home_header;
    private TextView tv_home_share;
    private ViewPager vp_home;                //轮播图
    private LinearLayout ll_home_point;       //轮播图上面的标记点
    private RecyclerView rv_home_producer;    //产品
    private View inflate;

    //轮播图集合
    public List imageList;
    public List productList;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
//            vp_home.setCurrentItem((curPostion + 1)%imageList.size());
        }
    };
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_home, null);
        //加载数据
        initData();
        //发现视图  设置监听
        findView();
        setHeadMargin();

        //设置视图
        setView();

        handler.sendEmptyMessageDelayed(WHAT,2000);
        return inflate;


    }


    @Override
    public void initData() {
        super.initData();
        imageList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            ImageView im;
            if(i == 0) {
                im = new ImageView(context);
                im.setBackgroundResource(R.drawable.mainavtivity_me);
            }else {
                im = new ImageView(context);
                im.setBackgroundResource(R.drawable.mainavtivity_gift);
            }

            imageList.add(im);
        }

        productList = new ArrayList();
        for (int i = 0;i < 100;i++){
            productList.add(i);
        }
    }

    //加载视图   并设置监听
    private void findView() {
        rl_home_header = (RelativeLayout) inflate.findViewById(R.id.rl_home_header);
        tv_home_share = (TextView) inflate.findViewById(R.id.tv_home_share);
        vp_home = (ViewPager) inflate.findViewById(R.id.vp_home);
        ll_home_point = (LinearLayout) inflate.findViewById(R.id.ll_home_point);
        rv_home_producer = (RecyclerView) inflate.findViewById(R.id.rv_home_producer);


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
        vp_home.setCurrentItem(imageList.size()*100);

        //设置recycleView
        rv_home_producer.setAdapter(new HomeProductAdapter(context,productList));
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
}
