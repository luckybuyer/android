package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.activity.ThirdPagerActivity;
import net.luckybuyer.adapter.previousWinnersAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.PreviousWinnerBean;
import net.luckybuyer.bean.ProductDetailBean;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/9/18.
 */
public class PreviousWinnersPager extends BasePager {
    private RelativeLayout rl_previous_header;
    private ImageView iv_previous_back;
    private TextView tv_previous_back;
    private RecyclerView rv_previous;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_previouswinner, null);
        findView();
        setHeadMargin();
        return inflate;
    }
    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);
        String url = MyApplication.url + "/v1/games/?status=finished&batch_id="+ ((ThirdPagerActivity)context).batch_id+"&per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                        HttpUtils.getInstance().stopNetWorkWaiting();
//                        ((MainActivity) context).homeProduct = response;
//                        srl_home_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        Utils.MyToast(context, "网络连接错误，请检查网络");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        Utils.MyToast(context, "网络连接错误，请检查网络");
                    }
                });

            }

        });
    }

    private void processData(String response) {
        Gson gson = new Gson();
        response = "{\"previous\":" + response + "}";
        PreviousWinnerBean previousWinnerBean = gson.fromJson(response, PreviousWinnerBean.class);

        if(response.length() > 100) {
            Log.e("TAG", response);
            rv_previous.setAdapter(new previousWinnersAdapter(context, previousWinnerBean.getPrevious()));
            rv_previous.setLayoutManager(new GridLayoutManager(context,1));
        }else {
            rv_previous.setVisibility(View.GONE);
        }

    }

    private void findView() {
        iv_previous_back = (ImageView) inflate.findViewById(R.id.iv_previous_back);
        tv_previous_back = (TextView) inflate.findViewById(R.id.tv_previous_back);
        rv_previous = (RecyclerView) inflate.findViewById(R.id.rv_previous);
        rl_previous_header = (RelativeLayout) inflate.findViewById(R.id.rl_previous_header);

        iv_previous_back.setOnClickListener(new MyOnClickListener());
        tv_previous_back.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_previous_back:
                    ((ThirdPagerActivity)context).finish();
                    break;
                case R.id.tv_previous_back:
                    ((ThirdPagerActivity)context).finish();
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
        Log.e("TAG", sbar + "");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 38));
        lp.topMargin = sbar;
        rl_previous_header.setLayoutParams(lp);
    }
}
