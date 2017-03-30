package net.iwantbuyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.BannersBean;
import net.iwantbuyer.bean.ShownBean;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.RoundCornerImageView;

import org.json.JSONObject;

import java.text.ParsePosition;
import java.util.List;

/**
 * Created by admin on 2016/12/7.
 */
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.MyViewHolder> {
    private Context context;
    public List<ShownBean.ShowBean> list;
    private View view;
    boolean flag;

    public ShowAdapter(Context context, List list, View view_show) {
        this.context = context;
        this.list = list;
        this.view = view_show;
//        list.add("http://static-sgp.luckybuyer.net/images/25a40675d17b41958867b3377f6bb760");
        if(context instanceof MainActivity) {
            flag = true;
        }else {
            flag = false;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUser().getProfile().getPicture()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.riv_show_head.setImageBitmap(resource);
            }
        });
        holder.tv_show_name.setText(list.get(position).getUser().getProfile().getName() + "");
        holder.tv_show_time.setText(list.get(position).getCreated_at().substring(0,19).replace("T"," ") + "");
        holder.tv_show_title.setText(list.get(position).getContent());
        if(list.get(position).getImages().size() ==1 ) {
            Glide.with(context).load("https:"+list.get(position).getImages().get(0)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImage(holder.iv_show_one, resource);
                }
            });
        }

        holder.iv_show_two.setVisibility(View.GONE);
        holder.iv_show_three.setVisibility(View.GONE);
        if(list.get(position).getImages().size() ==2) {
            holder.iv_show_two.setVisibility(View.VISIBLE);
            Glide.with(context).load("https:"+list.get(position).getImages().get(0)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImage(holder.iv_show_one, resource);
                }
            });
            Glide.with(context).load("https:"+list.get(position).getImages().get(1)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImage(holder.iv_show_two, resource);
                }
            });
        }
        if(list.get(position).getImages().size() == 3) {
            holder.iv_show_two.setVisibility(View.VISIBLE);
            holder.iv_show_three.setVisibility(View.VISIBLE);
            Glide.with(context).load("https:"+list.get(position).getImages().get(0)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImage(holder.iv_show_one, resource);
                }
            });
            Glide.with(context).load("https:"+list.get(position).getImages().get(1)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImage(holder.iv_show_two, resource);
                }
            });
            Glide.with(context).load("https:"+list.get(position).getImages().get(2)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    setImage(holder.iv_show_three, resource);
                }
            });
        }


        holder.iv_show_one.setOnClickListener(new MyOnClickListener(position));
        holder.iv_show_two.setOnClickListener(new MyOnClickListener(position));
        holder.iv_show_three.setOnClickListener(new MyOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyOnClickListener implements View.OnClickListener {
        int position;
        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_show_one:
                    if(flag) {
                        startPPW((MainActivity)context,position);
                    }else {
                        startPPW((ThirdPagerActivity)context,position);
                    }
                    if(vp_show != null) {
                        vp_show.setCurrentItem(0);
                        current = 0;
                    }
                    point();
                    break;
                case R.id.iv_show_two:
                    if(flag) {
                        startPPW((MainActivity)context,position);
                    }else {
                        startPPW((ThirdPagerActivity)context,position);
                    }
                    if(vp_show != null) {
                        vp_show.setCurrentItem(1);
                        current = 1;
                    }
                    point();
                    break;
                case R.id.iv_show_three:
                    if(flag) {
                        startPPW((MainActivity)context,position);
                    }else {
                        startPPW((ThirdPagerActivity)context,position);
                    }
                    if(vp_show != null) {
                        vp_show.setCurrentItem(2);
                        current = 2;
                    }
                    point();
                    break;

            }
        }
    }

    public void point(){
        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track("CLICK:show_image", props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }
    /**
     * 创建PopupWindow
     */
    ViewPager vp_show;
    LinearLayout ll_show_point;
    int current;
    public PopupWindow startPPW(final Activity activity,int position) {
        current = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        View viewPPW = LayoutInflater.from(context).inflate(R.layout.ppw_show, null);
        final PopupWindow mPopupWindow = new PopupWindow(viewPPW, screenWidth, screenHeight);

        vp_show = (ViewPager) viewPPW.findViewById(R.id.vp_show);
        ll_show_point = (LinearLayout) viewPPW.findViewById(R.id.ll_show_point);
        vp_show.setAdapter(new MyAdapter(list.get(position).getImages(),context,mPopupWindow));

        //设置底部圆点
        ImageView imageView;
        for (int i = 0; i < list.get(position).getImages().size(); i++) {
            imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.show_point_selector);
            ll_show_point.addView(imageView);
            if (i < list.size()) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.leftMargin = DensityUtil.px2dip(context, 20);
                imageView.setLayoutParams(lp);
            }
        }
        ll_show_point.getChildAt(current).setEnabled(false);
        vp_show.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ll_show_point.getChildAt(current).setEnabled(true);
                ll_show_point.getChildAt(position).setEnabled(false);
                current = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
        public PopupWindow popupWindow;

        public MyAdapter(List list, Context context, PopupWindow mPopupWindow) {
            this.list = list;
            this.context = context;
            this.popupWindow = mPopupWindow;
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
            final ImageView imageview = (ImageView) View.inflate(context,R.layout.item_show_viewpager,null);
            container.addView(imageview);
            Glide.with(context).load("https:" + list.get(position)).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imageview.setImageBitmap(resource);
                }
            });
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
            return imageview;
        }
    }
}
