package net.iwantbuyer.secondpager;

import android.view.View;
import android.widget.TextView;

import net.iwantbuyer.base.BasePager;

/**
 * Created by admin on 2016/9/18.
 */
public class WinnersSharingPager extends BasePager {
    TextView textView;
    @Override
    public View initView() {
        textView = new TextView(context);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("WinnersSharingPager \n WinnersSharingPager \n WinnersSharingPager");
    }
}
