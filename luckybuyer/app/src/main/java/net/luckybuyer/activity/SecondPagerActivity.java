package net.luckybuyer.activity;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.secondpager.PreviousWinnersPager;
import net.luckybuyer.secondpager.ProductDetailPager;
import net.luckybuyer.secondpager.ProductInformationPager;
import net.luckybuyer.secondpager.WinnersSharingPager;
import net.luckybuyer.utils.StatusBarUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SecondPagerActivity extends FragmentActivity {

    private RelativeLayout rl_secondpager_header;
    private TextView tv_second_share;
    private TextView tv_second_back;
    private List<Fragment> list;
    public int batch_id;

    //需要去哪
    public String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        new StatusBarUtils(this).statusBar();
        setContentView(R.layout.activity_second_pager);
        batch_id = getIntent().getIntExtra("batch_id",-1);
        setData();
        //发现视图  设置监听
        findView();
        setHeadMargin();
        selectPager();
    }

    //设置数据
    private void setData() {
        list = new ArrayList<>();
        list.add(new ProductDetailPager());

        //商品详情介绍页面
        list.add(new ProductInformationPager());
        //晒单分享界面
        list.add(new WinnersSharingPager());
        //往期揭晓界面
        list.add(new PreviousWinnersPager());
    }

    //发现视图  设置监听
    private void findView() {
        rl_secondpager_header = (RelativeLayout)findViewById(R.id.rl_secondpager_header);
        tv_second_share = (TextView)findViewById(R.id.tv_second_share);
        tv_second_back = (TextView)findViewById(R.id.tv_second_back);

        tv_second_back.setOnClickListener(new MyOnClickListener());
        tv_second_share.setOnClickListener(new MyOnClickListener());
    }


    //选择哪个界面
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_secondpager, fragment);
        fragmentTransaction.commit();
    }
    private void selectPager() {
        from = getIntent().getStringExtra("from");
        if("productdetail".equals(from)) {
            switchPage(0);
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_second_back:
                    finish();
                    break;
                case R.id.tv_second_share:
                    Utils.MyToast(SecondPagerActivity.this,"SHARE");
                    break;
            }
        }
    }
    //根据版本判断是否 需要设置据顶部状态栏高度
    @TargetApi(19)
    private void setHeadMargin() {
        Class<?> c =  null;
        Object obj =  null;
        Field field =  null;
        int  x = 0, sbar =  0;
        try  {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch(Exception e1) {
            e1.printStackTrace();

        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = sbar;
        rl_secondpager_header.setLayoutParams(lp);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if("productdetail".equals(from)) {
                switchPage(0);
                from = "";
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
