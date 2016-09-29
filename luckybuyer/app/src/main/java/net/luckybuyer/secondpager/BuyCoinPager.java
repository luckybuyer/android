package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/9/29.
 */
public class BuyCoinPager extends BaseNoTrackPager {

    private RelativeLayout rl_buycoins_header;
    private ImageView iv_buycoins_back;
    private TextView tv_buycoins_back;
    private TextView tv_buycoins_balance;
    private TextView tv_buycoins_rate;
    private TextView tv_buyconis_1coins;
    private TextView tv_buyconis_5coins;
    private TextView tv_buyconis_10coins;
    private TextView tv_buyconis_30coins;
    private TextView tv_buyconis_50coins;
    private TextView tv_buyconis_100coins;
    private TextView tv_buycoins_count;
    private TextView tv_buycoins_buy;
    private View inflate;

    private int count;
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_buycoins, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        ((SecondPagerActivity)context).from = "coindetailpager";
        findView();
        setHeadMargin();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void findView() {
        rl_buycoins_header = (RelativeLayout) inflate.findViewById(R.id.rl_buycoins_header);
        iv_buycoins_back = (ImageView) inflate.findViewById(R.id.iv_buycoins_back);
        tv_buycoins_back = (TextView) inflate.findViewById(R.id.tv_buycoins_back);
        tv_buycoins_balance = (TextView) inflate.findViewById(R.id.tv_buycoins_balance);
        tv_buycoins_rate = (TextView) inflate.findViewById(R.id.tv_buycoins_rate);
        tv_buyconis_1coins = (TextView) inflate.findViewById(R.id.tv_buyconis_1coins);
        tv_buyconis_5coins = (TextView) inflate.findViewById(R.id.tv_buyconis_5coins);
        tv_buyconis_10coins = (TextView) inflate.findViewById(R.id.tv_buyconis_10coins);
        tv_buyconis_30coins = (TextView) inflate.findViewById(R.id.tv_buyconis_30coins);
        tv_buyconis_50coins = (TextView) inflate.findViewById(R.id.tv_buyconis_50coins);
        tv_buyconis_100coins = (TextView) inflate.findViewById(R.id.tv_buyconis_100coins);
        tv_buycoins_count = (TextView) inflate.findViewById(R.id.tv_buycoins_count);
        tv_buycoins_buy = (TextView) inflate.findViewById(R.id.tv_buycoins_buy);

        //设置监听
        iv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buycoins_back.setOnClickListener(new MyOnClickListener());
        tv_buyconis_1coins.setOnClickListener(new MyOnClickListener());
        tv_buyconis_5coins.setOnClickListener(new MyOnClickListener());
        tv_buyconis_10coins.setOnClickListener(new MyOnClickListener());
        tv_buyconis_30coins.setOnClickListener(new MyOnClickListener());
        tv_buyconis_50coins.setOnClickListener(new MyOnClickListener());
        tv_buyconis_100coins.setOnClickListener(new MyOnClickListener());
        tv_buycoins_buy.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_buycoins_back:
                    ((SecondPagerActivity)context).switchPage(5);
                    break;
                case R.id.tv_buycoins_back:
                    ((SecondPagerActivity)context).switchPage(5);
                    break;
                case R.id.tv_buyconis_1coins:
                    count = 1;
                    tv_buyconis_1coins.setHovered(true);
                    tv_buyconis_5coins.setHovered(false);
                    tv_buyconis_10coins.setHovered(false);
                    tv_buyconis_30coins.setHovered(false);
                    tv_buyconis_50coins.setHovered(false);
                    tv_buyconis_100coins.setHovered(false);
                    tv_buycoins_count.setText("1 coin");
                    break;
                case R.id.tv_buyconis_5coins:
                    count = 5;
                    tv_buyconis_1coins.setHovered(false);
                    tv_buyconis_5coins.setHovered(true);
                    tv_buyconis_10coins.setHovered(false);
                    tv_buyconis_30coins.setHovered(false);
                    tv_buyconis_50coins.setHovered(false);
                    tv_buyconis_100coins.setHovered(false);
                    tv_buycoins_count.setText("5 coins");
                    break;
                case R.id.tv_buyconis_10coins:
                    count = 10;
                    tv_buyconis_1coins.setHovered(false);
                    tv_buyconis_5coins.setHovered(false);
                    tv_buyconis_10coins.setHovered(true);
                    tv_buyconis_30coins.setHovered(false);
                    tv_buyconis_50coins.setHovered(false);
                    tv_buyconis_100coins.setHovered(false);
                    tv_buycoins_count.setText("10 coins");
                    break;
                case R.id.tv_buyconis_30coins:
                    count = 30;
                    tv_buyconis_1coins.setHovered(false);
                    tv_buyconis_5coins.setHovered(false);
                    tv_buyconis_10coins.setHovered(false);
                    tv_buyconis_30coins.setHovered(true);
                    tv_buyconis_50coins.setHovered(false);
                    tv_buyconis_100coins.setHovered(false);
                    tv_buycoins_count.setText("30 coins");
                    break;
                case R.id.tv_buyconis_50coins:
                    count = 50;
                    tv_buyconis_1coins.setHovered(false);
                    tv_buyconis_5coins.setHovered(false);
                    tv_buyconis_10coins.setHovered(false);
                    tv_buyconis_30coins.setHovered(false);
                    tv_buyconis_50coins.setHovered(true);
                    tv_buyconis_100coins.setHovered(false);
                    tv_buycoins_count.setText("50 coins");
                    break;
                case R.id.tv_buyconis_100coins:
                    count = 100;
                    tv_buyconis_1coins.setHovered(false);
                    tv_buyconis_5coins.setHovered(false);
                    tv_buyconis_10coins.setHovered(false);
                    tv_buyconis_30coins.setHovered(false);
                    tv_buyconis_50coins.setHovered(false);
                    tv_buyconis_100coins.setHovered(true);
                    tv_buycoins_count.setText("100 coins");
                    break;
                case R.id.tv_buycoins_buy:
                    Utils.MyToast(context,"购买" + count + "个金币");
                    break;
            }
        }
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context,38));
        lp.topMargin = sbar;
        rl_buycoins_header.setLayoutParams(lp);
    }
}
