package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.bean.CoinDetailBean;

import java.util.List;

/**
 * Created by admin on 2016/9/28.
 */
public class CoinDetailPagerAdapter extends RecyclerView.Adapter<CoinDetailPagerAdapter.ViewHolder> {
    private Context context;
    private List<CoinDetailBean.CoinBean> list;
    public CoinDetailPagerAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_icondetail, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_icondetail_from.setText(list.get(position).getType());
        holder.tv_coindetail_time.setText(list.get(position).getCreated_at().substring(0, 20).replace("T", "\t"));
        holder.tv_ciondetail_many.setText(list.get(position).getAmount()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_icondetail_from;
        TextView tv_coindetail_time;
        TextView tv_ciondetail_many;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_icondetail_from= (TextView) itemView.findViewById(R.id.tv_icondetail_from);
            tv_coindetail_time= (TextView) itemView.findViewById(R.id.tv_coindetail_time);
            tv_ciondetail_many= (TextView) itemView.findViewById(R.id.tv_ciondetail_many);
        }
    }
}
