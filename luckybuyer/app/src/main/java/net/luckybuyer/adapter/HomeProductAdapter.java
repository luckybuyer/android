package net.luckybuyer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LogWriter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.luckybuyer.R;
import net.luckybuyer.bean.GameProductBean;

import java.util.List;

/**
 * Created by admin on 2016/9/13.
 */
public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {

    private Context context;
    private List<GameProductBean.GameBean> list;
    public interface OnClickListener{
        void onclick(View view ,int position);
        void onLongClick(View view,int position);
    }

    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public HomeProductAdapter(Context context, List productList) {
        this.context = context;
        this.list = productList;
    }

    @Override
    public HomeProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recycleview_product, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int precent = (int) ((list.get(position).getShares()-list.get(position).getLeft_shares())*100/list.get(position).getShares());
        holder.tv_producet_discribe.setText(list.get(position).getProduct().getTitle());
        holder.tv_product_progress.setText("Participate Progress:" + precent + "%");
        holder.pb_product_progress.setProgress((int) ((list.get(position).getShares() - list.get(position).getLeft_shares()) * 100 / list.get(position).getShares()));
        Glide.with(context).load("http:" + list.get(position).getProduct().getDetail_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.iv_product_icon.setImageBitmap(resource);
            }
        });
        Log.e("TAG22", "http:" + list.get(position).getProduct().getDetail_image());
//        Glide.with(context).load("http:" + list.get(position).getProduct().getDetail_image()).into(holder.iv_product_icon);
//        holder.iv_product_icon.setBackgroundResource(R.drawable.mainavtivity_gift);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onclick(view, position);
                Log.e("TAG", "dianji");
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onClickListener.onLongClick(view,position);
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_producet_discribe;
        private TextView tv_product_progress;
        private ProgressBar pb_product_progress;
        private ImageView iv_product_icon;
        private View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tv_producet_discribe = (TextView) view.findViewById(R.id.tv_producet_discribe);
            tv_product_progress = (TextView) view.findViewById(R.id.tv_product_progress);
            pb_product_progress = (ProgressBar) view.findViewById(R.id.pb_product_progress);
            iv_product_icon = (ImageView) view.findViewById(R.id.iv_product_icon);
        }
    }
}
