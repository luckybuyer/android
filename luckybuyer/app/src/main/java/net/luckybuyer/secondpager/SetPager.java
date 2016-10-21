package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;

/**
 * Created by admin on 2016/9/18.
 */
public class SetPager extends BasePager {
    private RelativeLayout rl_set_header;
    private ImageView iv_set_back;
    private TextView tv_set_back;
    private RelativeLayout rl_set_address;
    //    private RelativeLayout rl_set_help;
//    private RelativeLayout rl_set_us;
    private TextView tv_set_login;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_set, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        ((SecondPagerActivity) context).from = "setpager";
        findView();
        setHeadMargin();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void findView() {
        rl_set_header = (RelativeLayout) inflate.findViewById(R.id.rl_set_header);
        iv_set_back = (ImageView) inflate.findViewById(R.id.iv_set_back);
        tv_set_back = (TextView) inflate.findViewById(R.id.tv_set_back);
        rl_set_address = (RelativeLayout) inflate.findViewById(R.id.rl_set_address);
//        rl_set_help = (RelativeLayout) inflate.findViewById(R.id.rl_set_help);
//        rl_set_us = (RelativeLayout) inflate.findViewById(R.id.rl_set_us);
        tv_set_login = (TextView) inflate.findViewById(R.id.tv_set_login);

        iv_set_back.setOnClickListener(new MyOnClickListener());
        tv_set_back.setOnClickListener(new MyOnClickListener());
        rl_set_address.setOnClickListener(new MyOnClickListener());
//        rl_set_help.setOnClickListener(new MyOnClickListener());
//        rl_set_us.setOnClickListener(new MyOnClickListener());
        tv_set_login.setOnClickListener(new MyOnClickListener());

        String token = Utils.getSpData("token", context);

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_set_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.tv_set_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.rl_set_address:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "setpager";
                    break;
//                case R.id.rl_set_help:
//                    Utils.MyToast(context,"帮助");
//                    break;
//                case R.id.rl_set_us:
//                    Utils.MyToast(context,"关于我们");
//                    break;
                case R.id.tv_set_login:
                    Intent intent = new Intent();
                    intent.putExtra("go","homepager");
                    ((SecondPagerActivity)context).setResult(0,intent);
                    Utils.setSpData("token", null, context);
                    Utils.setSpData("token_num", null, context);
                    ((SecondPagerActivity) context).finish();
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = sbar;
        rl_set_header.setLayoutParams(lp);
    }
}
