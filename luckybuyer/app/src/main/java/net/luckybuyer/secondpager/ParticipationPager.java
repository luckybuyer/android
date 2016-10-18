package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import net.luckybuyer.adapter.ParticipationAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.ProductOrderBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/10/17.
 */
public class ParticipationPager extends BasePager{

    private RelativeLayout rl_paricipation_header;
    private ImageView iv_participation_back;
    private TextView tv_participation_back;
    private ImageView iv_participation_icon;
    private TextView tv_participation_discribe;
    private TextView tv_participation_period;              //购买日期
    private RecyclerView rv_participation;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_participation, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setHeadMargin();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();

        String url = MyApplication.url + "/v1/games/"+((SecondPagerActivity)context).game_id+"/public-orders/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
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

    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"productorder\":" + response + "}";
        ProductOrderBean productOrderBean = gson.fromJson(response,ProductOrderBean.class);
        rv_participation.setAdapter(new ParticipationAdapter(context, productOrderBean.getProductorder()));
        rv_participation.setLayoutManager(new GridLayoutManager(context, 1));


        tv_participation_discribe.setText(((SecondPagerActivity) context).title + "");
        tv_participation_period.setText("Participation period:" + productOrderBean.getProductorder().size());
        if(!((SecondPagerActivity) context).isDestroyed()) {
            Glide.with(context).load("http:" + ((SecondPagerActivity) context).title_image).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv_participation_icon.setImageBitmap(resource);
                }
            });
        }

    }

    private void findView() {
        rl_paricipation_header = (RelativeLayout) inflate.findViewById(R.id.rl_paricipation_header);
        iv_participation_back = (ImageView) inflate.findViewById(R.id.iv_participation_back);
        tv_participation_back = (TextView) inflate.findViewById(R.id.tv_participation_back);
        iv_participation_icon = (ImageView) inflate.findViewById(R.id.iv_participation_icon);
        tv_participation_discribe = (TextView) inflate.findViewById(R.id.tv_participation_discribe);
        tv_participation_period = (TextView) inflate.findViewById(R.id.tv_participation_period);
        rv_participation = (RecyclerView) inflate.findViewById(R.id.rv_participation);

        iv_participation_back.setOnClickListener(new MyOnClickListener());
        tv_participation_back.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_participation_back:
                    ((SecondPagerActivity)context).finish();
                    break;
                case R.id.tv_participation_back:
                    ((SecondPagerActivity)context).finish();
                    break;
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 38));
        lp.topMargin = sbar;
        rl_paricipation_header.setLayoutParams(lp);
    }
}
