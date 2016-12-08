package net.iwantbuyer.secondpager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import net.iwantbuyer.R;
import net.iwantbuyer.adapter.ShowAdapter;
import net.iwantbuyer.base.BaseNoTrackPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/7.
 */
public class ShowPager extends BaseNoTrackPager{

    private RecyclerView rv_show;
    private LinearLayout ll_show_back;
    private View view_show;
    private View inflate;

    private List list;
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_show,null);
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList();

        rv_show.setAdapter(new ShowAdapter(context,list,view_show));
        rv_show.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
    }

    private void findView() {
        rv_show = (RecyclerView) inflate.findViewById(R.id.rv_show);
        ll_show_back = (LinearLayout) inflate.findViewById(R.id.ll_show_back);
        view_show = (View) inflate.findViewById(R.id.view_show);
        ll_show_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
