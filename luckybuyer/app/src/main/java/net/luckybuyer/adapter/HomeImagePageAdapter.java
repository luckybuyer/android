package net.luckybuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.bean.BannersBean;

import java.util.List;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class HomeImagePageAdapter extends PagerAdapter {

    public List<ImageView> list;
    public Context context;
    public List<BannersBean.BannerBean> gameList;

    public HomeImagePageAdapter(List list, Context context, List<BannersBean.BannerBean> gameList) {
        this.list = list;
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        if(list.size() < 2) {
            return list.size();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView image=list.get(position%list.size());
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp=image.getParent();
        if(vp!=null){
            ViewGroup vg=(ViewGroup) vp;
            vg.removeView(image);
        }

        container.addView(list.get(position % list.size()));


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("from", "productdetail");
                intent.putExtra("game_id", gameList.get(position % list.size()).getGame_id());
                context.startActivity(intent);
            }
        });

        return list.get(position % list.size());

    }
}
