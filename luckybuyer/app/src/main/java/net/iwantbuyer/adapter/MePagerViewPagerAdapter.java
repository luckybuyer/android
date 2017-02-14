package net.iwantbuyer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import net.iwantbuyer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/21.
 */
public class MePagerViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<View> list;

    private List<String> titleList = new ArrayList();
    public MePagerViewPagerAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        titleList.add("\n\n\n\n"+context.getString(R.string.All)+"\n\n\n");
        titleList.add(context.getString(R.string.LuckyRecords));
//        titleList.add("    Show   ");
//        titleList.add("   Hisory  ");
    }

    @Override
    public int getCount() {
        return list.size();
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
        container.addView(list.get(position));
        return list.get(position % list.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
