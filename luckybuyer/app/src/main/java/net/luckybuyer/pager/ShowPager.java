package net.luckybuyer.pager;

import android.view.View;
import android.widget.TextView;

import net.luckybuyer.base.BasePager;

/**
 * Created by admin on 2016/9/13.
 */
public class ShowPager extends BasePager{
    TextView textView;
    @Override
    public View initView() {
        textView = new TextView(context);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("SHOW PAGER");
    }
}
