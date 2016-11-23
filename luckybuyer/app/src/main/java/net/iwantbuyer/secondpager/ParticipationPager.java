package net.iwantbuyer.secondpager;

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

import net.iwantbuyer.R;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.adapter.ParticipationAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BasePager;
import net.iwantbuyer.bean.ProductOrderBean;
import net.iwantbuyer.utils.HttpUtils;

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

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;


    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_participation, null);
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        rl_keepout.setVisibility(View.VISIBLE);
        rl_nodata.setVisibility(View.GONE);
        rl_neterror.setVisibility(View.GONE);
        rl_loading.setVisibility(View.VISIBLE);

        int user_id = ((ThirdPagerActivity)context).user_id;
        String url = null;
        if(user_id == -1) {
            url = MyApplication.url + "/v1/games/"+((ThirdPagerActivity)context).game_id+"/public-orders/?per_page=20&page=1&timezone=" + MyApplication.utc;
        }else {
            url = MyApplication.url + "/v1/games/"+((ThirdPagerActivity)context).game_id+"/public-orders/?user_id="+user_id+"&per_page=20&page=1&timezone=" + MyApplication.utc;
        }
        Log.e("TAG", url);
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_keepout.setVisibility(View.GONE);
                            processData(response);
                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }


            @Override
            public void failure(final Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
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


        tv_participation_discribe.setText(((ThirdPagerActivity) context).title + "");
        tv_participation_period.setText("Participation period:" + productOrderBean.getProductorder().size());
        if(!((ThirdPagerActivity) context).isDestroyed()) {
            Glide.with(context).load("http:" + ((ThirdPagerActivity) context).title_image).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
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

        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading= (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);

        iv_participation_back.setOnClickListener(new MyOnClickListener());
        tv_participation_back.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_participation_back:
                    ((ThirdPagerActivity)context).finish();
                    break;
                case R.id.tv_participation_back:
                    ((ThirdPagerActivity)context).finish();
                    break;
                case R.id.tv_net_again:
                    initData();
                    break;
            }
        }
    }
}
