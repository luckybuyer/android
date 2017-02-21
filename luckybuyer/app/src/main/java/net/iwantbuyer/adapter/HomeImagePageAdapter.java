package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.bean.BannersBean;
import net.iwantbuyer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class HomeImagePageAdapter extends PagerAdapter {

    public List<ImageView> list = new ArrayList();
    public Context context;
    public List<BannersBean.BannerBean> gameList;

    public HomeImagePageAdapter(List list, Context context, List<BannersBean.BannerBean> gameList) {
        this.context = context;
        this.gameList = gameList;
        Log.e("TAG_gamelistsize", list.size() + "");
        getList(list, context, gameList);
        if (gameList.size() == 2) {
            getList(list, context, gameList);
        }

    }

    private void getList(List list, Context context, List<BannersBean.BannerBean> gameList) {
        for (int i = 0; i < gameList.size(); i++) {
            String detail_image = "http:" + gameList.get(i).getImage();
            final ImageView image_header = new ImageView(context);
            if (!((MainActivity) context).isDestroyed()) {
                Glide.with(context).load(detail_image).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        image_header.setImageBitmap(resource);
                    }
                });
            }

//            Glide.with(context).load(detail_image).into(image_header);
            this.list.add(image_header);
        }
    }

    @Override
    public int getCount() {
        if (list.size() < 2) {
            return list.size();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        ImageView image = list.get(position % list.size());
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
//        ViewParent vp=image.getParent();
//        if(vp!=null){
//            ViewGroup vg=(ViewGroup) vp;
//            vg.removeView(image);
//        }

        container.addView(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("from", "productdetail");
                intent.putExtra("game_id", -1);
                intent.putExtra("batch_id", gameList.get(position % list.size()).getBatch_id());
                context.startActivity(intent);

            }
        });
        return image;
    }
}

