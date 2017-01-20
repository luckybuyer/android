package net.iwantbuyer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.iwantbuyer.R;
import net.iwantbuyer.bean.ProductOrderBean;
import net.iwantbuyer.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by admin on 2016/9/18.
 */
public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ViewHolder> {
    private Context context;
    public List<ProductOrderBean.ProductorderBean> list;
    int type;

    public interface OnClickListener {
        void onclick(View view, int position);

        void onLongClick(View view, int position);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ProductDetailAdapter(Context context, List productList) {
        this.context = context;
        this.list = productList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            type = 1;
        } else {
            type = 0;
        }
        return type;
    }

    @Override
    public ProductDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewType = type;
        ViewHolder holder = null;
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.header_productdetail, null);
            holder = new ViewHolder(view, viewType);
        } else if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_productdetail, null);
            holder = new ViewHolder(view, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            final String imgUrl = list.get(position - 1).getUser().getProfile().getPicture();
//            Glide.with(context).load(imgUrl).into(holder.civ_productdetail);
            Glide.with(context).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    int imageWidth = resource.getWidth();
//                    int imageHeight = resource.getHeight();
////                                int imageHeight = resource.getWidth()*9/16;
////                                int width=ScreenUtils.getScreenWidth(NewsInfoActivity.this);
//                    //得到屏幕的 尺寸 动态设置
//                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//                    int screenWidth = wm.getDefaultDisplay().getWidth();
//                    int screenHeight = wm.getDefaultDisplay().getHeight();
//                    int height = screenWidth * imageHeight/ imageWidth;
////                                Log.e("TAG", "height"+height);
//                    Log.e("TAG--", "imageWidth"+imageWidth);
//                    Log.e("TAG--", "imageHeight"+imageHeight);
//                    ViewGroup.LayoutParams para = holder.civ_productdetail.getLayoutParams();
////                                para.width=width;
//                    para.height = imageWidth;
//                    holder.civ_productdetail.setLayoutParams(para);
//                    Glide.with(context).load(imgUrl).asBitmap().into(holder.civ_productdetail);

                    holder.civ_productdetail.setImageBitmap(resource);
                }
            });

            holder.tv_productdetail_time.setText(list.get(position - 1).getCreated_at().substring(0, 19).replace("T", " "));
            holder.tv_productdetail_winnermoney.setText(list.get(position - 1).getShares() + "");
            holder.tv_productdetail_name.setText(list.get(position - 1).getUser().getProfile().getName());
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size() + 1;
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundCornerImageView civ_productdetail;
        private TextView tv_productdetail_time;
        private TextView tv_productdetail_winnermoney;
        private TextView tv_productdetail_name;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                civ_productdetail = (RoundCornerImageView ) view.findViewById(R.id.civ_productdetail);
                tv_productdetail_time = (TextView) view.findViewById(R.id.tv_productdetail_time);
                tv_productdetail_winnermoney = (TextView) view.findViewById(R.id.tv_productdetail_winnermoney);
                tv_productdetail_name = (TextView) view.findViewById(R.id.tv_productdetail_name);
            } else if (type == 1) {

            }
        }
    }
}
