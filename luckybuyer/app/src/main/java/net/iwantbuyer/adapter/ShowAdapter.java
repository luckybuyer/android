package net.iwantbuyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.bean.BannersBean;
import net.iwantbuyer.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by admin on 2016/12/7.
 */
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.MyViewHolder> {
    private Context context;
    private List list;
    private View view;

    public ShowAdapter(Context context, List list, View view_show) {
        this.context = context;
        this.list = list;
        this.view = view_show;
        list.add("http://static-sgp.luckybuyer.net/images/da5de8f54f304b4bba065a4840094976");
        list.add("http://static-sgp.luckybuyer.net/images/Products-iPhone-7.jpg");
        list.add("http://static-sgp.luckybuyer.net/images/25a40675d17b41958867b3377f6bb760");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(0)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                setImage(holder.iv_show_one, resource);
            }
        });
        Glide.with(context).load(list.get(1)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                setImage(holder.iv_show_two, resource);
            }
        });
        Glide.with(context).load(list.get(2)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                setImage(holder.iv_show_three, resource);
            }
        });

        holder.iv_show_one.setOnClickListener(new MyOnClickListener());
        holder.iv_show_two.setOnClickListener(new MyOnClickListener());
        holder.iv_show_three.setOnClickListener(new MyOnClickListener());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_show_one:
                    startPPW((SecondPagerActivity)context);
                    break;
                case R.id.iv_show_two:
                    startPPW((SecondPagerActivity)context);
                    break;
                case R.id.iv_show_three:
                    startPPW((SecondPagerActivity)context);
                    break;
            }
        }
    }

    /**
     * 创建PopupWindow
     */
    public PopupWindow startPPW(final Activity activity) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        View viewPPW = LayoutInflater.from(context).inflate(R.layout.ppw_show, null);
        ViewPager vp_show = (ViewPager) viewPPW.findViewById(R.id.vp_show);
        vp_show.setAdapter(new MyAdapter(list,context));

        final PopupWindow mPopupWindow = new PopupWindow(viewPPW, screenWidth, screenHeight);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
        return mPopupWindow;
    }


    private void setImage(ImageView imageView, Bitmap bitmap) {
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        // 从源图片中截取需要的部分
        if (picWidth > picHeight) {
            bitmap = Bitmap.createBitmap(bitmap, (picWidth - picHeight) / 2, 0, picHeight, picHeight);
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 0, (picHeight - picWidth) / 2, picWidth, picWidth);
        }

//        BitmapDrawable bitmapD = new BitmapDrawable(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RoundCornerImageView riv_show_head;
        private TextView tv_show_name;
        private TextView tv_show_time;
        private TextView tv_show_title;
        private ImageView iv_show_one;
        private ImageView iv_show_two;
        private ImageView iv_show_three;

        public MyViewHolder(View itemView) {
            super(itemView);
            riv_show_head = (RoundCornerImageView) itemView.findViewById(R.id.riv_show_head);
            tv_show_name = (TextView) itemView.findViewById(R.id.tv_show_name);
            tv_show_time = (TextView) itemView.findViewById(R.id.tv_show_time);
            tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
            iv_show_one = (ImageView) itemView.findViewById(R.id.iv_show_one);
            iv_show_two = (ImageView) itemView.findViewById(R.id.iv_show_two);
            iv_show_three = (ImageView) itemView.findViewById(R.id.iv_show_three);
        }
    }

    class MyAdapter extends PagerAdapter {

        public List list;
        public Context context;

        public MyAdapter(List list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageview = new ImageView(context);
            Glide.with(context).load(list.get(position)).load(imageview);
            return imageview;
        }
    }

}
