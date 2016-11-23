package net.iwantbuyer.pager;

import android.view.View;
import android.widget.TextView;

import net.iwantbuyer.base.BasePager;

/**
 * Created by admin on 2016/9/13.
 */
public class BuyChipsPager extends BasePager{
    TextView textView;
    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(100);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("BYE CHIPS/dyangshuyuyangshuyu/dyangshuyu");
    }
}
