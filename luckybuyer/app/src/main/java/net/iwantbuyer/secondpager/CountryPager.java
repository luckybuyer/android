package net.iwantbuyer.secondpager;

import android.view.View;

import net.iwantbuyer.R;
import net.iwantbuyer.base.BasePager;

/**
 * Created by admin on 2016/12/22.
 */
public class CountryPager extends BasePager{

    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_country, null);
        findView();
        return inflate;
    }

    private void findView() {

    }
}
