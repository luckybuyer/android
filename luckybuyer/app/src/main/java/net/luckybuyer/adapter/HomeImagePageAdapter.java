package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class HomeImagePageAdapter extends PagerAdapter {

    public List<View> list;
    public Context context;
    public ViewPager vp_home;
    public HomeImagePageAdapter(List list, Context context, ViewPager vp_home) {
        this.list = list;
        this.context = context;
        this.vp_home = vp_home;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position%list.size()));
        return list.get(position%list.size());
    }
}
