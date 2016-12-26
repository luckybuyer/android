package net.iwantbuyer.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import net.iwantbuyer.R;
import net.iwantbuyer.pager.ShowPager;
import net.iwantbuyer.secondpager.CountryPager;
import net.iwantbuyer.secondpager.ParticipationPager;
import net.iwantbuyer.secondpager.PreviousWinnersPager;
import net.iwantbuyer.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class ThirdPagerActivity extends FragmentActivity {
    private List<Fragment> list;
    public String from;

    public String title_image;                             //all界面点击view go用到
    public String title;                                   //all界面点击view go用到
    public int user_id = -1;
    public int game_id = -1;
    public int product_id = -1;                            //show界面

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
        product_id = getIntent().getIntExtra("product_id",-1);
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
        //参与详情页面                     2
        list.add(new ShowPager());
        //国家设置页面                     3
        list.add(new CountryPager());
    }

    private void selectPager() {
        from = getIntent().getStringExtra("from");
        if ("previous".equals(from)) {
            switchPage(0);
        }else if("participation".equals(from)) {
            switchPage(1);
        }else if("show".equals(from)) {
            switchPage(2);
        }else if("countrypager".equals(from)) {
            switchPage(3);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);                      //统计时长   友盟
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);                      //统计时长   友盟
    }
}
