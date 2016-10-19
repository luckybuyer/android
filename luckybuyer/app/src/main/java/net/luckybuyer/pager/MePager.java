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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.share.widget.LikeView;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.MePagerAllAdapter;
import net.luckybuyer.adapter.MePagerLuckyAdapter;
import net.luckybuyer.adapter.MePagerShowAdapter;
import net.luckybuyer.adapter.MePagerViewPagerAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.AllOrderBean;
import net.luckybuyer.bean.User;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.CircleImageView;
import net.luckybuyer.view.CustomViewPager;
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
    private CustomViewPager vp_me;
    private SlidingTabLayout stl_me_vpcontrol;
    private ScrollView sv_me;
    private View inflate;

    private FBLikeView fb_shipping_facebook;

    private List vpList;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_me, null);
        findView();
//        setHeadMargin();

        setView();
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);
        String token = Utils.getSpData("token", context);
        String url = MyApplication.url + "/v1/game-orders/?per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        final String finalToken = token;
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //我的中奖  接口(lucky)
                                        LuckyResponse(finalToken, response);
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

                    @Override
                    public void failure(Exception exception) {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                    }
                }

        );
    }

    private void LuckyResponse(String token, final String res) {
        String url = MyApplication.url + "/v1/game-orders/?lucky=true&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {
                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        processData(res, response);
                                        try {
                                            Thread.sleep(500);
                                            HttpUtils.getInstance().stopNetWorkWaiting();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
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

                    @Override
                    public void failure(Exception exception) {
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

    private void processData(String res, String resLucky) {
        res = "{\"allorder\":" + res + "}";
        Gson gson = new Gson();
        AllOrderBean allOrderBean = gson.fromJson(res, AllOrderBean.class);

        resLucky = "{\"allorder\":" + resLucky + "}";
        gson = new Gson();
        AllOrderBean luckyOrderBean = gson.fromJson(resLucky, AllOrderBean.class);
        vpList = new ArrayList();

        //加载All页面
        View view = View.inflate(context, R.layout.pager_me_recycle_all, null);
        RecyclerView rv_me_all = (RecyclerView) view.findViewById(R.id.rv_me_all);
        rv_me_all.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rv_me_all.setAdapter(new MePagerAllAdapter(context, allOrderBean.getAllorder()));
        vpList.add(view);

        //加载lucky界面
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setAdapter(new MePagerLuckyAdapter(context, luckyOrderBean.getAllorder()));
        vpList.add(recyclerView);


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
        vp_me = (CustomViewPager) inflate.findViewById(R.id.vp_me);
        stl_me_vpcontrol = (SlidingTabLayout) inflate.findViewById(R.id.stl_me_vpcontrol);
        fb_shipping_facebook = (FBLikeView) inflate.findViewById(R.id.fb_shipping_facebook);
        sv_me = (ScrollView) inflate.findViewById(R.id.sv_me);


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

        fb_shipping_facebook.getLikeView().setObjectIdAndType(
                "http://inthecheesefactory.com/blog/understand-android-activity-launchmode/en",
                LikeView.ObjectType.OPEN_GRAPH);
    }

    //点击监听
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.i_me_set:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "setpager");
                    startActivityForResult(intent,0);
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 315));
        lp.topMargin = statusBarHeight;
        sv_me.setLayoutParams(lp);
    }


}
