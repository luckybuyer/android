package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.MePagerAllAdapter;
import net.luckybuyer.adapter.MePagerViewPagerAdapter;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.CircleImageView;
import net.luckybuyer.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/13.
 */
public class MePager extends BasePager {

    private LinearLayout ll_me_vpcontrol;
    private RelativeLayout rl_me_title;
    private CircleImageView civ_me_header;
    private ImageView iv_me_voice;
    private ImageView i_me_set;                  //设置
    private TextView tv_me_name;
    private TextView tv_me_fbcode;
    private TextView tv_me_gold;
    private TextView tv_me_all;
    private TextView tv_me_lucky;
    private TextView tv_me_show;
    private TextView tv_me_history;
    private ViewPager vp_me;
    private ImageView iv_me_line;
    private View inflate;

    private List vpList;
    private List allList;
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_me, null);
        findView();
        setHeadMargin();
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        allList = new ArrayList();
        for (int i =0;i < 10;i++){
            allList.add(i);
        }
//        RecyclerView recyclerView = new RecyclerView(context);
//        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//
//        recyclerView.setAdapter(new MePagerAllAdapter(context, allList));
        View view = View.inflate(context,R.layout.pager_me_recycle_all,null);

        RecyclerView rv_me_all = (RecyclerView) view.findViewById(R.id.rv_me_all);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false);
        rv_me_all.setLayoutManager(linearLayoutManager);


        rv_me_all.setAdapter(new MePagerAllAdapter(context, allList));
        vpList = new ArrayList();
        vpList.add(view);
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(context);
            textView.setText("这是第" + i + "个页面");
            vpList.add(textView);
        }
        vp_me.setAdapter(new MePagerViewPagerAdapter(context, vpList));
    }

    private void findView() {
        rl_me_title = (RelativeLayout) inflate.findViewById(R.id.rl_me_title);
        civ_me_header = (CircleImageView) inflate.findViewById(R.id.civ_me_header);
        iv_me_voice = (ImageView) inflate.findViewById(R.id.iv_me_voice);
        i_me_set = (ImageView) inflate.findViewById(R.id.i_me_set);
        tv_me_name = (TextView) inflate.findViewById(R.id.tv_me_name);
        tv_me_fbcode = (TextView) inflate.findViewById(R.id.tv_me_fbcode);
        tv_me_gold = (TextView) inflate.findViewById(R.id.tv_me_gold);
        tv_me_all = (TextView) inflate.findViewById(R.id.tv_me_all);
        tv_me_lucky = (TextView) inflate.findViewById(R.id.tv_me_lucky);
        tv_me_show = (TextView) inflate.findViewById(R.id.tv_me_show);
        tv_me_history = (TextView) inflate.findViewById(R.id.tv_me_history);
        vp_me = (ViewPager) inflate.findViewById(R.id.vp_me);
        ll_me_vpcontrol = (LinearLayout) inflate.findViewById(R.id.ll_me_vpcontrol);
        iv_me_line = (ImageView) inflate.findViewById(R.id.iv_me_line);

        vp_me.setOnPageChangeListener(new MyOnPageChangeListener());
        i_me_set.setOnClickListener(new MyOnClickListener());
        iv_me_voice.setOnClickListener(new MyOnClickListener());
    }

    //点击监听
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.i_me_set:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from","setpager");
                    startActivity(intent);
                    break;
            }
        }
    }
    //viewpager的滚动监听
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int viewWidth = iv_me_line.getWidth();
            int width = Utils.getScreenWidth(context)/4;
            //红点移动的距离 = 页面移动的百分比 * 总间距
            int slideLeft = (int) (positionOffset * width);

            //真正在屏幕移动的距离 = 起始坐标 + 红点移动的距离
            slideLeft = (int) ((position + positionOffset) * width);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, 5);
            params.leftMargin = slideLeft + DensityUtil.dip2px(context, 12);
            iv_me_line.setLayoutParams(params);
            Log.e("TAG", iv_me_line + "");
        }

        @Override
        public void onPageSelected(int position) {

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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 218));
        lp.topMargin = statusBarHeight;
        rl_me_title.setLayoutParams(lp);
    }


}
