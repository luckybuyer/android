package net.smartbuyer.pager;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.smartbuyer.R;
import net.smartbuyer.adapter.WinningAdapter;
import net.smartbuyer.app.MyApplication;
import net.smartbuyer.base.BaseNoTrackPager;
import net.smartbuyer.bean.PreviousWinnerBean;
import net.smartbuyer.utils.HttpUtils;

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
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

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
        isNeedNetWaiting = true;
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();

        if (isNeedNetWaiting) {
            rl_keepout.setVisibility(View.VISIBLE);
            rl_neterror.setVisibility(View.GONE);
            rl_nodata.setVisibility(View.GONE);
            rl_loading.setVisibility(View.VISIBLE);
            isNeedNetWaiting = false;
        } else {
            srl_winning.setRefreshing(true);
        }

        String url = MyApplication.url + "/v1/games/?status=closed&status=finished&per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
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
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void findView() {
        rl_winning_header = (RelativeLayout) inflate.findViewById(R.id.rl_winning_header);
        rv_winning = (RecyclerView) inflate.findViewById(R.id.rv_winning);
        srl_winning = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_winning);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);

        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again.setOnClickListener(new View.OnClickListener() {
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

}
