package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.bean.BannersBean;
import net.iwantbuyer.bean.ProductDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public class ProductDetailImagePageAdapter extends PagerAdapter {

    public List<ImageView> list = new ArrayList();
    public Context context;
    public List<String> gameList;

    public ProductDetailImagePageAdapter(Context context, List<String> gameList) {
        this.context = context;
        this.gameList = gameList;
        getList(context, gameList);
        if (gameList.size() == 2) {
            getList(context, gameList);
        }

        if(gameList.size() == 3) {
            getList(context, gameList);
        }

    }

    private void getList(Context context, List<String> gameList) {
        for (int i = 0; i < gameList.size(); i++) {
            String detail_image = "https:" + gameList.get(i);
            Log.e("TAG_detail", detail_image);
            final ImageView imageView = (ImageView) View.inflate(context, R.layout.item_productdetail_pager,null);
//            final ImageView image_header = new ImageView(context);
            if (!((SecondPagerActivity) context).isDestroyed()) {
                Glide.with(context).load(detail_image).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
            }
//            if(!((SecondPagerActivity)context).isDestroyed()) {
//                Glide.with(context).load(detail_image).into(imageView).onStart();
//            }

            this.list.add(imageView);
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
        return image;
    }
}

