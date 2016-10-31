package net.luckybuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.adapter.WinningAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.PreviousWinnerBean;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/9/13.
 */
public class WinningPager extends BaseNoTrackPager {

    private View inflate;
    private RelativeLayout rl_winning_header;
    private RecyclerView rv_winning;
    private SwipeRefreshLayout srl_winning;
    boolean isNeedNetWaiting = true;

    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private Button bt_net_again;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_winning, null);
        findView();
        setHeadMargin();
        srl_winning.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        isNeedNetWaiting = true;
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();

        if (isNeedNetWaiting) {
            HttpUtils.getInstance().startNetworkWaiting(context);
            isNeedNetWaiting = false;
        } else {
            srl_winning.setRefreshing(true);
        }

        String url = MyApplication.url + "/v1/games/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        srl_winning.setRefreshing(false);


                        if (response.length() > 10) {
                            processData(response);
                            rl_keepout.setVisibility(View.GONE);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void error(int code, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_winning.setRefreshing(false);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl_winning.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                    }
                });
            }
        });
    }

    private void findView() {
        rl_winning_header = (RelativeLayout) inflate.findViewById(R.id.rl_winning_header);
        rv_winning = (RecyclerView) inflate.findViewById(R.id.rv_winning);
        srl_winning = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_winning);
        bt_net_again = (Button) inflate.findViewById(R.id.bt_net_again);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        bt_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNeedNetWaiting = true;
                initData();
            }
        });
    }

    private void processData(String response) {
        response = "{\"previous\":" + response + "}";
        Gson gson = new Gson();
        PreviousWinnerBean previousWinnerBean = gson.fromJson(response, PreviousWinnerBean.class);
        rv_winning.setAdapter(new WinningAdapter(context, previousWinnerBean.getPrevious()));
        rv_winning.setLayoutManager(new GridLayoutManager(context, 1));
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = sbar;
        rl_winning_header.setLayoutParams(lp);
    }
}
