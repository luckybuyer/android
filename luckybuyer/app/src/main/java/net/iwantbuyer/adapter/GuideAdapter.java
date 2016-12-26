package net.iwantbuyer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;

/**
 * Created by admin on 2016/12/14.
 */
public class GuideAdapter extends PagerAdapter {
    private Context context;
    private ViewPager vp_main;
    private TextView tv_guide_count;
    float alpha = 1.0f;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
        }
    };
    private TextView tv_guide_buy;

    public GuideAdapter(Context context, ViewPager vp_main) {
        this.context = context;
        this.vp_main = vp_main;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private WindowManager.LayoutParams lp;
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null;
        if(position == 0) {
            view = View.inflate(context,R.layout.item_guide_first,null);
            ImageView iv_guide_arrow = (ImageView) view.findViewById(R.id.iv_guide_arrow);
            ImageView iv_guide_selector = (ImageView) view.findViewById(R.id.iv_guide_selector);
            ImageView iv_guide_close = (ImageView) view.findViewById(R.id.iv_guide_close);
            anim(iv_guide_arrow);
//            animAlpha(view.findViewById(R.id.iv_guide_select));
            iv_guide_selector.setOnClickListener(new MyOnClickListener());
            iv_guide_close.setOnClickListener(new MyOnClickListener());


        }else if(position == 1){
            view = View.inflate(context,R.layout.item_guide_two,null);
            view.findViewById(R.id.rl_guide_ppw).setVisibility(View.GONE);
            view.findViewById(R.id.iv_guide_morebuy).setVisibility(View.GONE);
            view.findViewById(R.id.iv_guide_arrow3).setVisibility(View.GONE);

            view.findViewById(R.id.rl_guide_four).setVisibility(View.GONE);

            view.findViewById(R.id.tv_guide_getitnow).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_guide_arrow2).setVisibility(View.VISIBLE);

            TextView tv_guide_getitnow = (TextView) view.findViewById(R.id.tv_guide_getitnow);
            ImageView iv_guide_arrow2 = (ImageView) view.findViewById(R.id.iv_guide_arrow2);
            ImageView iv_guide_close = (ImageView) view.findViewById(R.id.iv_guide_close);
            tv_guide_getitnow.setOnClickListener(new MyOnClickListener());
            iv_guide_close.setOnClickListener(new MyOnClickListener());
            anim(iv_guide_arrow2);
        }else if(position == 2) {
            view = View.inflate(context,R.layout.item_guide_two,null);
            view.findViewById(R.id.rl_guide_ppw).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_guide_morebuy).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_guide_arrow3).setVisibility(View.VISIBLE);
            view.findViewById(R.id.rl_guide_four).setVisibility(View.GONE);


            RelativeLayout rl_guide_delete = (RelativeLayout) view.findViewById(R.id.rl_guide_delete);
            RelativeLayout rl_guide_add = (RelativeLayout) view.findViewById(R.id.rl_guide_add);
            ImageView iv_guide_arrow3 = (ImageView) view.findViewById(R.id.iv_guide_arrow3);
            tv_guide_count = (TextView) view.findViewById(R.id.tv_guide_count);
            TextView tv_guide_two = (TextView) view.findViewById(R.id.tv_guide_two);
            TextView tv_guide_five = (TextView) view.findViewById(R.id.tv_guide_five);
            TextView tv_guide_ten = (TextView) view.findViewById(R.id.tv_guide_ten);
            TextView tv_guide_all = (TextView) view.findViewById(R.id.tv_guide_all);
            TextView tv_guide_count = (TextView) view.findViewById(R.id.tv_guide_count);
            tv_guide_buy = (TextView) view.findViewById(R.id.tv_guide_buy);

            rl_guide_delete.setOnClickListener(new MyOnClickListener());
            rl_guide_add.setOnClickListener(new MyOnClickListener());
            tv_guide_two.setOnClickListener(new MyOnClickListener());
            tv_guide_five.setOnClickListener(new MyOnClickListener());
            tv_guide_ten.setOnClickListener(new MyOnClickListener());
            tv_guide_all.setOnClickListener(new MyOnClickListener());
            tv_guide_count.setOnClickListener(new MyOnClickListener());
            tv_guide_buy.setOnClickListener(new MyOnClickListener());
            view.findViewById(R.id.iv_guide_close).setOnClickListener(new MyOnClickListener());
            anim(iv_guide_arrow3);
        }else if(position == 3) {
            view = View.inflate(context,R.layout.item_guide_two,null);
            view.findViewById(R.id.tv_guide_getitnow).setVisibility(View.GONE);
            view.findViewById(R.id.iv_guide_arrow2).setVisibility(View.GONE);

            view.findViewById(R.id.rl_guide_ppw).setVisibility(View.GONE);
            view.findViewById(R.id.iv_guide_morebuy).setVisibility(View.GONE);
            view.findViewById(R.id.iv_guide_arrow3).setVisibility(View.GONE);

            view.findViewById(R.id.iv_guide_login).setOnClickListener(new MyOnClickListener());
            view.findViewById(R.id.iv_guide_nologin).setOnClickListener(new MyOnClickListener());
            view.findViewById(R.id.iv_guide_close).setOnClickListener(new MyOnClickListener());
            anim(view.findViewById(R.id.iv_guide_arrow4));
        }
        container.addView(view);
        return view;
    }

    private void anim(View view){
        TranslateAnimation anim = new TranslateAnimation(0,
                0, 0, 12);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new CycleInterpolator(1f));
        anim.setStartOffset(1200);
        anim.setDuration( 500 );
        view.setAnimation(anim);
    }
    private void animAlpha(View view){
        AlphaAnimation anim = new AlphaAnimation(0,1);
//        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new CycleInterpolator(1f));
//        anim.setStartOffset(2000);
        anim.setDuration( 1000 );
        view.setAnimation(anim);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_guide_selector:
                    vp_main.setCurrentItem(1);
                    break;
                case R.id.iv_guide_close:
                    vp_main.setVisibility(View.GONE);
                    break;
                case R.id.tv_guide_getitnow:
                    vp_main.setCurrentItem(2);
                    break;
                case R.id.tv_guide_count:
                    Log.e("TAG", "不弹出软键盘");
                    break;
                case R.id.rl_guide_delete:
                    int count = Integer.parseInt(tv_guide_count.getText().toString());
                    if(count > 5) {
                        tv_guide_count.setText(count -5 + "");
                        tv_guide_buy.setText("Total " + tv_guide_count.getText()+ " coins");
                    }
                    break;
                case R.id.rl_guide_add:
                    count = Integer.parseInt(tv_guide_count.getText().toString());
                    if(count < 20) {
                        tv_guide_count.setText(count + 5 + "");
                        tv_guide_buy.setText("Total " + tv_guide_count.getText()+ " coins");
                    }
                    break;
                case R.id.tv_guide_two:
                    tv_guide_count.setText("10");
                    tv_guide_buy.setText("Total " + "10" + " coins");
                    break;
                case R.id.tv_guide_five:
                    tv_guide_count.setText("15");
                    tv_guide_buy.setText("Total " + "15" + " coins");
                    break;
                case R.id.tv_guide_ten:
                    tv_guide_count.setText("20");
                    tv_guide_buy.setText("Total " + "20" + " coins");
                    break;
                case R.id.tv_guide_all:
                    tv_guide_count.setText("20");
                    tv_guide_buy.setText("Total " + "20" + " coins");
                    break;
                case R.id.tv_guide_buy:
                    vp_main.setCurrentItem(3);
                    break;
                case R.id.iv_guide_login:
                    //登陆
                    context.startActivity(((MainActivity)context).lock.newIntent(((MainActivity)context)));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vp_main.setVisibility(View.GONE);
                        }
                    },500);
                    break;
                case R.id.iv_guide_nologin:
                    vp_main.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
