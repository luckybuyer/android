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
import java.util.logging.LogRecord;

/**
 * Created by admin on 2016/9/18.
 */
public class PreviousWinnersPager extends BasePager {
    private RelativeLayout rl_previous_header;
    private ImageView iv_previous_back;
    private TextView tv_previous_back;
    private RecyclerView rv_previous;
    private View inflate;

    private RelativeLayout rl_keepout;                     //联网
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_previouswinner, null);
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
        String url = MyApplication.url + "/v1/games/?status=finished&batch_id="+ ((ThirdPagerActivity)context).batch_id+"&per_page=20&page=1&timezone=" + MyApplication.utc;
        Log.e("TAG", url);
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(response);
                        rl_keepout.setVisibility(View.GONE);
                        Log.e("TAG_previous", response);
                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);;
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
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
        response = "{\"previous\":" + response + "}";
        PreviousWinnerBean previousWinnerBean = gson.fromJson(response, PreviousWinnerBean.class);

        if(response.length() > 100) {
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
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);

        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading= (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);

        tv_net_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

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

}
