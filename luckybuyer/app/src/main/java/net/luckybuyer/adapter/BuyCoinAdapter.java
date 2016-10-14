package net.luckybuyer.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.bean.BuyCoinBean;
import net.luckybuyer.bean.ProductOrderBean;

import java.util.List;

/**
 * Created by admin on 2016/10/10.
 */
public class BuyCoinAdapter extends RecyclerView.Adapter<BuyCoinAdapter.ViewHolder> {
    int WHAT =1;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            notifyDataSetChanged();
        }
    };
    
    private Context context;
    private List<BuyCoinBean.BuycoinsBean> list;

    public interface BuyCoinOnClickListener{
        public void onClick(View view,int position);
    }

    public BuyCoinOnClickListener buyCoinOnClickListener;
    public void setBuyCoinOnClickListener(BuyCoinOnClickListener buyCoinOnClickListener){
        this.buyCoinOnClickListener = buyCoinOnClickListener;
    }
    public BuyCoinAdapter(Context context, List<BuyCoinBean.BuycoinsBean> list) {
        this.context = context;
        this.list = list;
        list.get(0).setHovered(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buycoins, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_buyconis_coins.setText(list.get(position).getAmount() + "Coins");
        if(list.get(position).isHovered()) {
            holder.tv_buyconis_coins.setHovered(true);
        }else {
            holder.tv_buyconis_coins.setHovered(false);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buyCoinOnClickListener != null) {
                    for (int i = 0;i < list.size();i++) {
                        list.get(i).setHovered(false);

                    }
                    list.get(position).setHovered(true);
                    handler.sendEmptyMessage(WHAT);
                    buyCoinOnClickListener.onClick(holder.view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_buyconis_coins;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_buyconis_coins = (TextView) itemView.findViewById(R.id.tv_buyconis_coins);
            view = itemView;
        }
    }
}
