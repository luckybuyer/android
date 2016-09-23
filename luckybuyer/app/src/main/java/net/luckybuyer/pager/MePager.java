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

import com.flyco.tablayout.SlidingTabLayout;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.MePagerAllAdapter;
import net.luckybuyer.adapter.MePagerLuckyAdapter;
import net.luckybuyer.adapter.MePagerShowAdapter;
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

    private RelativeLayout rl_me_title;
    private CircleImageView civ_me_header;
    private ImageView iv_me_voice;
    private ImageView i_me_set;                  //设置
    private TextView tv_me_name;
    private TextView tv_me_fbcode;
    private TextView tv_me_gold;
    private ViewPager vp_me;
    private SlidingTabLayout stl_me_vpcontrol;
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
        vpList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            allList.add(i);
        }

        //加载All页面
        View view = View.inflate(context, R.layout.pager_me_recycle_all, null);
        RecyclerView rv_me_all = (RecyclerView) view.findViewById(R.id.rv_me_all);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        rv_me_all.setLayoutManager(linearLayoutManager);
        rv_me_all.setAdapter(new MePagerAllAdapter(context, allList));
        vpList.add(view);

        //加载lucky界面
        RecyclerView recyclerView = new RecyclerView(context);
        linearLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MePagerLuckyAdapter(context, allList));
        vpList.add(recyclerView);

        //加载Show界面
        recyclerView = new RecyclerView(context);
        linearLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MePagerShowAdapter(context, allList));
        vpList.add(recyclerView);


        TextView textView = new TextView(context);
        textView.setText("这是第History个页面");
        vpList.add(textView);


        vp_me.setAdapter(new MePagerViewPagerAdapter(context, vpList));
        stl_me_vpcontrol.setViewPager(vp_me);
    }

    private void findView() {
        rl_me_title = (RelativeLayout) inflate.findViewById(R.id.rl_me_title);
        civ_me_header = (CircleImageView) inflate.findViewById(R.id.civ_me_header);
        iv_me_voice = (ImageView) inflate.findViewById(R.id.iv_me_voice);
        i_me_set = (ImageView) inflate.findViewById(R.id.i_me_set);
        tv_me_name = (TextView) inflate.findViewById(R.id.tv_me_name);
        tv_me_fbcode = (TextView) inflate.findViewById(R.id.tv_me_fbcode);
        tv_me_gold = (TextView) inflate.findViewById(R.id.tv_me_gold);
        vp_me = (ViewPager) inflate.findViewById(R.id.vp_me);
        stl_me_vpcontrol = (SlidingTabLayout) inflate.findViewById(R.id.stl_me_vpcontrol);


        i_me_set.setOnClickListener(new MyOnClickListener());
        iv_me_voice.setOnClickListener(new MyOnClickListener());
    }

    //点击监听
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.i_me_set:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "setpager");
                    startActivity(intent);
                    break;
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 218));
        lp.topMargin = statusBarHeight;
        rl_me_title.setLayoutParams(lp);
    }

}
