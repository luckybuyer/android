package net.luckybuyer.pager;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.bean.PreviousWinnerBean;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

/**
 * Created by admin on 2016/9/13.
 */
public class WinningPager extends BasePager{

    private View inflate;
    private RelativeLayout rl_winning_header;
    private RecyclerView rv_winning;
    private SwipeRefreshLayout srl_winning;
    boolean isNeedNetWaiting = true;
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_winning, null);
        findView();
        srl_winning.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();

        if (isNeedNetWaiting) {
            HttpUtils.getInstance().startNetworkWaiting(context);
            isNeedNetWaiting = false;
        }else {
            srl_winning.setRefreshing(true);
        }

        int batch_id = ((SecondPagerActivity) context).batch_id;
        String url = MyApplication.url + "/v1/games/?status=finished&per_page=20&page=1&timezone=utc";
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        if (isNeedNetWaiting) {
                            srl_winning.setRefreshing(false);
                        }
                    }
                });
            }

            @Override
            public void error(int code, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNeedNetWaiting) {
                            srl_winning.setRefreshing(false);
                        }
                        Utils.MyToast(context, "网络连接错误，请稍后重试");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNeedNetWaiting) {
                            srl_winning.setRefreshing(false);
                        }
                        Utils.MyToast(context, "网络连接错误，请稍后重试");
                    }
                });
            }
        });
    }

    private void findView() {
        rl_winning_header = (RelativeLayout) inflate.findViewById(R.id.rl_winning_header);
        rv_winning = (RecyclerView) inflate.findViewById(R.id.rv_winning);
        srl_winning = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_winning);
    }

    private void processData(String response) {
        response = "{\"previous\":" + response + "}";
        Gson gson = new Gson();
        PreviousWinnerBean previousWinnerBean = gson.fromJson(response, PreviousWinnerBean.class);

    }
}
