package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.utils.DensityUtil;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/9/30.
 */
public class DispatchPager extends BaseNoTrackPager{

    private RelativeLayout rl_dispatch_header;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_dispatch, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setHeadMargin();
        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
    }
    private void findView() {
        rl_dispatch_header = (RelativeLayout) inflate.findViewById(R.id.rl_dispatch_header);
    }
    //根据版本判断是否 需要设置据顶部状态栏高度
    @TargetApi(19)
    private void setHeadMargin() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();

        }
        Log.e("TAG", sbar + "");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 38));
        lp.topMargin = sbar;
        rl_dispatch_header.setLayoutParams(lp);
    }
}
