package net.iwantbuyer.secondpager;

import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.adapter.ProblemAdapter;
import net.iwantbuyer.base.BaseNoTrackPager;

import java.util.ArrayList;
import java.util.List;

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
        processData("123");
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
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
        List list = new ArrayList();
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");list.add("123");
        rv_problem.setAdapter(new ProblemAdapter(context,list));
        rv_problem.setLayoutManager(new LinearLayoutManager(context));
    }
}
