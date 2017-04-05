package net.iwantbuyer.activity;

import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.pager.ShowPager;
import net.iwantbuyer.secondpager.ClPager;
import net.iwantbuyer.secondpager.CountryPager;
import net.iwantbuyer.secondpager.LanguagePager;
import net.iwantbuyer.secondpager.NewsPager;
import net.iwantbuyer.secondpager.ParticipationPager;
import net.iwantbuyer.secondpager.PreviousWinnersPager;
import net.iwantbuyer.secondpager.problemPager;
import net.iwantbuyer.utils.StatusBarUtils;
import net.iwantbuyer.utils.Utils;

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
    public FragmentManager fragmentManager;

    public String language;
    public String country;
    public String server;
    public String store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        new StatusBarUtils(this).statusBar();
        setContentView(R.layout.activity_third_pager);
        batch_id = getIntent().getIntExtra("batch_id", -1);

        title_image = getIntent().getStringExtra("title_image");
        title = getIntent().getStringExtra("title");
        user_id = getIntent().getIntExtra("user_id", -1);
        game_id = getIntent().getIntExtra("game_id", -1);
        product_id = getIntent().getIntExtra("product_id", -1);
        setData();
        selectPager();
    }

    //设置数据
    private void setData() {
        list = new ArrayList<>();
        //往期中奖页面                     0
        list.add(new PreviousWinnersPager());
        //参与详情页面                     1
        list.add(new ParticipationPager());
        //参与详情页面                     2
        list.add(new ShowPager());
        //国家设置页面                     3
        list.add(new CountryPager());
        //选择国家或者语言                 4
        list.add(new ClPager());
        //语言设置页面                     5
        list.add(new LanguagePager());
        //语言设置页面                     6
        list.add(new NewsPager());
        //语言设置页面                     7
        list.add(new problemPager());
    }

    private void selectPager() {
        from = getIntent().getStringExtra("from");
        if ("previous".equals(from)) {
            switchPage(0);
        } else if ("participation".equals(from)) {
            switchPage(1);
        } else if ("show".equals(from)) {
            switchPage(2);
        } else if ("countrypager".equals(from)) {
            switchPage(3);
        } else if ("clpager".equals(from)) {
            switchPage(4);
        } else if ("news".equals(from)) {
            switchPage(6);
        }else if("problem".equals(from)) {
            switchPage(7);
        }
    }

    //选择哪个界面
    public void switchPage(int checkedId) {
        Fragment fragment = list.get(checkedId);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_third, fragment);
        if (checkedId != 0 && checkedId != 1 && checkedId != 2 && checkedId != 4 && checkedId != 6) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //通过广播将firebase传递过来的消息传到ui县城
        IntentFilter filter = new IntentFilter();
        filter.addAction("Message");
        registerReceiver(MessageReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MessageReceiver);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_winning_close:
                    if (winningShow != null && winningShow.isShowing()) {
                        winningShow.dismiss();
                    }
                    break;
                case R.id.iv_winning_go:
                    if (winningShow != null && winningShow.isShowing()) {
                        winningShow.dismiss();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("order_id", order_id);
                    // 设置结果，并进行传送
                    ThirdPagerActivity.this.setResult(6, intent);
                    ThirdPagerActivity.this.finish();
                    break;

            }
        }
    }

    AlertDialog winningShow;
    int order_id = 0;
    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String response = intent.getStringExtra("response");
            Gson gson = new Gson();
            DispatchGameBean dispatchGameBean = gson.fromJson(response, DispatchGameBean.class);
            order_id = dispatchGameBean.getId();

            View view = View.inflate(ThirdPagerActivity.this, R.layout.alertdialog_winning, null);
            ImageView iv_winning_close = (ImageView) view.findViewById(R.id.iv_winning_close);
            ImageView iv_winning_win = (ImageView) view.findViewById(R.id.iv_winning_win);
            final ImageView iv_winning_icon = (ImageView) view.findViewById(R.id.iv_winning_icon);
            TextView tv_winning_code = (TextView) view.findViewById(R.id.tv_winning_code);
            TextView tv_winning_discribe = (TextView) view.findViewById(R.id.tv_winning_discribe);
            ImageView iv_winning_go = (ImageView) view.findViewById(R.id.iv_winning_go);
            if(Utils.getSpData("locale",context) !=null && Utils.getSpData("locale",context).contains("zh")) {
                iv_winning_win.setBackgroundResource(R.drawable.winning_zh);
            }else if(Utils.getSpData("locale",context) !=null && Utils.getSpData("locale",context).contains("ms")) {
                iv_winning_win.setBackgroundResource(R.drawable.winning_ms);
            }else {
                iv_winning_win.setBackgroundResource(R.drawable.winning_en);
            }
            Glide.with(context).load("https:" + dispatchGameBean.getGame().getProduct().getTitle_image()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv_winning_icon.setImageBitmap(resource);
                }
            });
            tv_winning_code.setText(dispatchGameBean.getGame().getIssue_id() + "");
            tv_winning_discribe.setText(dispatchGameBean.getGame().getProduct().getTitle() + "");
            iv_winning_close.setOnClickListener(new MyOnClickListener());
            iv_winning_go.setOnClickListener(new MyOnClickListener());

            winningShow = Utils.StartAlertDialog(ThirdPagerActivity.this, view, Utils.getScreenWidth(context) * 3 / 4, Utils.getStatusHeight(context) * 3 / 4);

        }
    };
}
