package net.luckybuyer.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.luckybuyer.R;
import net.luckybuyer.secondpager.AddAddressPager;
import net.luckybuyer.secondpager.BuyCoinPager;
import net.luckybuyer.secondpager.CoinDetailPager;
import net.luckybuyer.secondpager.DispatchPager;
import net.luckybuyer.secondpager.ParticipationPager;
import net.luckybuyer.secondpager.PreviousWinnersPager;
import net.luckybuyer.secondpager.ProductDetailPager;
import net.luckybuyer.secondpager.ProductInformationPager;
import net.luckybuyer.secondpager.SetPager;
import net.luckybuyer.secondpager.ShippingAddressPager;
import net.luckybuyer.secondpager.WinnersSharingPager;
import net.luckybuyer.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class ThirdPagerActivity extends FragmentActivity {
    private List<Fragment> list;
    public String from;

    public String title_image;                             //all界面点击view go用到
    public String title;                                   //all界面点击view go用到
    public int user_id = -1;
    public int game_id = -1;

    public int batch_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        new StatusBarUtils(this).statusBar();
        setContentView(R.layout.activity_third_pager);
        batch_id = getIntent().getIntExtra("batch_id",-1);

        title_image = getIntent().getStringExtra("title_image");
        title = getIntent().getStringExtra("title");
        user_id = getIntent().getIntExtra("user_id",-1);
        game_id = getIntent().getIntExtra("game_id",-1);
        setData();
        selectPager();
    }

    //设置数据
    private void setData() {
        list = new ArrayList<>();
        //往期中奖页面
        list.add(new PreviousWinnersPager());
        //参与详情页面                     1
        list.add(new ParticipationPager());
    }

    private void selectPager() {
        from = getIntent().getStringExtra("from");
        if ("previous".equals(from)) {
            switchPage(0);
        }else if("participation".equals(from)) {
            switchPage(1);
        }
    }

    //选择哪个界面
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_third, fragment);
        fragmentTransaction.commit();
    }
}
