package net.iwantbuyer.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.secondpager.BuyCoinPager;

import java.util.ArrayList;
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
    String method;

    public interface BuyCoinOnClickListener{
        public void onClick(View view,int topup_option_id);
    }

    public BuyCoinOnClickListener buyCoinOnClickListener;
    public void setBuyCoinOnClickListener(BuyCoinOnClickListener buyCoinOnClickListener){
        this.buyCoinOnClickListener = buyCoinOnClickListener;
    }
    public BuyCoinAdapter(Context context, List<BuyCoinBean.BuycoinsBean> list, String method) {
        this.context = context;
        this.method = method;
        BuyCoinAdapter.this.list= new ArrayList();
        for (int i = 0;i < list.size();i++){
            if(list.get(i).getCategory().contains(method)) {
                BuyCoinAdapter.this.list.add(list.get(i));
            }
            list.get(i).setHovered(false);
        }
        if(BuyCoinAdapter.this.list.size() > 0) {
            BuyCoinAdapter.this.list.get(0).setHovered(true);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buycoins, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int amount = list.get(position).getAmount();
        if(amount == 1 || method.equals("mol_3") || method.equals("mol_804") || method.equals("mol_805") || method.equals("mol_806") || method.equals("mol_815")) {
            holder.tv_buyconis_coins.setText(list.get(position).getPrice() + "");
            holder.tv_buyconis_coins.setEnabled(true);
            holder.tv_buyconis_free.setVisibility(View.GONE);
        }else {
            holder.tv_buyconis_coins.setText(list.get(position).getPrice() +"");
            int free = list.get(position).getAmount()-list.get(position).getPrice();
            if(free > 0) {
                holder.tv_buyconis_free.setText(" + " + free + context.getString(R.string.free_coins));
            }else {
                holder.tv_buyconis_free.setVisibility(View.GONE);
            }
        }

        if(list.get(position).isHovered()) {
            holder.tv_buyconis_coins.setEnabled(true);
            holder.rl_buycoins_coins.setEnabled(true);
            holder.iv_buycoins_jiao.setVisibility(View.VISIBLE);
            holder.tv_buyconis_free.setEnabled(true);
            holder.rl_buycoins_coins.setHovered(true);
        }else {
            holder.tv_buyconis_coins.setEnabled(false);
            holder.rl_buycoins_coins.setEnabled(false);
            holder.iv_buycoins_jiao.setVisibility(View.GONE);
            holder.tv_buyconis_free.setEnabled(false);
            holder.rl_buycoins_coins.setHovered(false);
        }

        Log.e("TAG333", list.get(position).getId() + "");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buyCoinOnClickListener != null) {
                    for (int i = 0;i < list.size();i++) {
                        list.get(i).setHovered(false);
                    }
                    list.get(position).setHovered(true);
                    handler.sendEmptyMessage(WHAT);
                    buyCoinOnClickListener.onClick(holder.view,list.get(position).getId());
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
        private TextView tv_buyconis_free;
        private RelativeLayout rl_buycoins_coins;
        private ImageView iv_buycoins_jiao;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_buyconis_coins = (TextView) itemView.findViewById(R.id.tv_buyconis_coins);
            tv_buyconis_free = (TextView) itemView.findViewById(R.id.tv_buyconis_free);
            rl_buycoins_coins = (RelativeLayout) itemView.findViewById(R.id.rl_buycoins_coins);
            iv_buycoins_jiao = (ImageView) itemView.findViewById(R.id.iv_buycoins_jiao);
            view = itemView;
        }
    }
}