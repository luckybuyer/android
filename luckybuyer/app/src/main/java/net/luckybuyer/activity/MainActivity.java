package net.luckybuyer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.luckybuyer.R;
import net.luckybuyer.base.BasePager;
import net.luckybuyer.pager.BuyChipsPager;
import net.luckybuyer.pager.HomePager;
import net.luckybuyer.pager.MePager;
import net.luckybuyer.pager.ShowPager;
import net.luckybuyer.pager.WinningPager;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private FrameLayout fl_main;
    public RadioGroup rg_main;
    private List<Fragment> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        new StatusBarUtils(this).statusBar();
        setContentView(R.layout.activity_main);

        //设置数据
        setData();
        //发现视图  设置监听
        findView();
    }
    //设置数据
    private void setData() {
        list = new ArrayList<>();
        list.add(new HomePager());
        list.add(new BuyChipsPager());
        list.add(new WinningPager());
        list.add(new ShowPager());
        list.add(new MePager());
    }

    //试图初始化 与设置监听
    private void findView() {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        //设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = -1;
            if(checkedId == rg_main.getChildAt(0).getId()) {
                id = 0;
                switchPage(id);
            }else if(checkedId == rg_main.getChildAt(1).getId()) {
                id = 1;
                switchPage(id);
            }else if(checkedId == rg_main.getChildAt(2).getId()) {
                id = 2;
                switchPage(id);
            }else if(checkedId == rg_main.getChildAt(3).getId()) {
                id = 3;
                switchPage(id);
            }else if(checkedId == rg_main.getChildAt(4).getId()) {
                id = 4;
                switchPage(id);
            }
        }
    }


    //选择哪个界面
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, fragment);
        fragmentTransaction.commit();
    }

    long mExitTime;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
