package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.MePagerAllAdapter;
import net.luckybuyer.adapter.MePagerLuckyAdapter;
import net.luckybuyer.adapter.MePagerShowAdapter;
import net.luckybuyer.adapter.MePagerViewPagerAdapter;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.AllOrderBean;
import net.luckybuyer.bean.User;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.CircleImageView;
import net.luckybuyer.view.RecycleViewDivider;

import java.sql.DataTruncation;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_me, null);
        findView();
        setHeadMargin();

        setView();
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);
        String token = Utils.getSpData("token", context);
        String url = "https://api-staging.luckybuyer.net/v1/game-orders/?per_page=10&page=1&timezone=utc";
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {
//                        String str = response.substring(0,response.length()/10);
//                        String str1 = response.substring(response.length()/10,response.length()/5);
//                        String str2 = response.substring(response.length()/5,response.length()*3/10);
//                        String str3 = response.substring(response.length()*3/10,response.length()*4/10);
//                        String str4 = response.substring(response.length()*4/10,response.length()*5/10);
//                        String str5 = response.substring(response.length()*5/10,response.length()*6/10);
//                        String str6 = response.substring(response.length()*6/10,response.length()*7/10);
//                        String str7 = response.substring(response.length()*7/10,response.length()*8/10);
//                        String str8 = response.substring(response.length()*8/10,response.length()*9/10);
//                        String str9 = response.substring(response.length()*9/10,response.length());
//                        Log.e("TAG1", str);
//                        Log.e("TAG2", str1);
//                        Log.e("TAG3", str2);
//                        Log.e("TAG4", str3);
//                        Log.e("TAG5", str4);
//                        Log.e("TAG6", str5);
//                        Log.e("TAG7", str6);
//                        Log.e("TAG8", str7);
//                        Log.e("TAG9", str8);
//                        Log.e("TAG0", str9);

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        processData(response);
                                        HttpUtils.getInstance().stopNetWorkWaiting();


                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {
                        Log.e("TAG", requestCode + "");
                        Log.e("TAG", message);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();

                            }
                        });
                    }
                }

        );


    }

    private void processData(String response) {
        response = "{\"allorder\":" + response + "}";
        Gson gson = new Gson();
        AllOrderBean allOrderBean = gson.fromJson(response, AllOrderBean.class);

        List listLucky = new ArrayList();
        for (int i = 0; i < allOrderBean.getAllorder().size(); i++) {
            if (allOrderBean.getAllorder().get(i).getGame().getLucky_user() != null && Utils.getSpData("id", context).equals(allOrderBean.getAllorder().get(i).getGame().getLucky_user().getId() + "")) {
                listLucky.add(allOrderBean.getAllorder().get(i));
            }
        }
        vpList = new ArrayList();

        //加载All页面
        View view = View.inflate(context, R.layout.pager_me_recycle_all, null);
        RecyclerView rv_me_all = (RecyclerView) view.findViewById(R.id.rv_me_all);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        rv_me_all.setLayoutManager(linearLayoutManager);
        rv_me_all.setAdapter(new MePagerAllAdapter(context, allOrderBean.getAllorder()));
        vpList.add(view);

        //加载lucky界面
        RecyclerView recyclerView = new RecyclerView(context);
        linearLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MePagerLuckyAdapter(context, listLucky));
        vpList.add(recyclerView);

//        //加载Show界面
//        recyclerView = new RecyclerView(context);
//        linearLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(new MePagerShowAdapter(context, allList));
//        vpList.add(recyclerView);
//
//
//        TextView textView = new TextView(context);
//        textView.setText("这是第History个页面");
//        vpList.add(textView);


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
        tv_me_gold.setOnClickListener(new MyOnClickListener());
    }

    private void setView() {
        String user_id = Utils.getSpData("user_id", context);
        String balance = Utils.getSpData("balance", context);
        String name = Utils.getSpData("name", context);
        final String picture = Utils.getSpData("picture", context);

        tv_me_name.setText(name);
        tv_me_fbcode.setText(user_id);
        tv_me_gold.setText("Gold:$" + balance);
        Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                civ_me_header.setImageBitmap(resource);
            }
        });
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
                case R.id.tv_me_gold:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "coindetailpager");
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
