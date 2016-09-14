package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_producet_discribe.setText(list.get(position).getProduct().getDetail());
        holder.tv_product_progress.setText("Lottery Progress:" + list.get(position).getResult_countdown() + "%");
        holder.pb_product_progress.setProgress(list.get(position).getResult_countdown());
        Glide.with(context).load("http:" + list.get(position).getProduct().getDetail_image()).into(holder.iv_product_icon);
        holder.iv_product_icon.setBackgroundResource(R.drawable.mainavtivity_gift);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onclick(view, position);
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
