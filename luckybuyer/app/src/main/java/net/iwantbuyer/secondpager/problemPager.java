package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.appsflyer.AppsFlyerLib;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.adapter.ProblemAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.GameProductBean;
import net.iwantbuyer.bean.Problem;
import net.iwantbuyer.utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangshuyu on 2017/4/5.
 */
public class problemPager extends BaseNoTrackPager{
    private View inflate;
    private RelativeLayout rl_problem_return;
    private RecyclerView rv_problem;
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_problem,null);
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        HttpUtils.getInstance().startNetworkWaiting(context);
        String url = MyApplication.url + "/v1/faqs/?per_page=20&timezone=" + MyApplication.utc;
        Log.e("TAG", url);
        Map map = new HashMap();
        map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String string, final String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        processData(string);
                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
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

        });
    }

    //初始化 视图 并设置监听
    private void findView() {
        rl_problem_return = (RelativeLayout) inflate.findViewById(R.id.rl_problem_return);
        rv_problem = (RecyclerView) inflate.findViewById(R.id.rv_problem);

        rl_problem_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ThirdPagerActivity) context).finish();
            }
        });
    }

    private void processData(String resonse){
        Gson gson = new Gson();
        String pro = "{\"problem\":" + resonse + "}";
        Log.e("TAG——problem", pro);
        Problem problem = gson.fromJson(pro, Problem.class);
        List<Problem.ProblemBean> list = problem.getProblem();
        Log.e("TAG——problem", list.size() + "");
        Log.e("TAG——problem",list.get(list.size()-1).getQuestion());
        rv_problem.setAdapter(new ProblemAdapter(context,list));
        rv_problem.setLayoutManager(new GridLayoutManager(context,LinearLayoutManager.VERTICAL));
    }
}
