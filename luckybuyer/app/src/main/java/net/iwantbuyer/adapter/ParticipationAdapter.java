package net.iwantbuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.bean.ProductOrderBean;

import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
public class ParticipationAdapter extends RecyclerView.Adapter<ParticipationAdapter.ViewHolder> {
    private Context context;
    private List<ProductOrderBean.ProductorderBean> list;
    public ParticipationAdapter(Context context, List<ProductOrderBean.ProductorderBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_participation, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_participation_time.setText(list.get(position).getCreated_at().substring(0,19).replace("T","\n"));
        holder.tv_participation_people.setText(list.get(position).getShares()+"");
        String str = "";
        for (int i = 0;i < list.get(position).getNumbers().size();i++){
            if((i+1)%5 != 0) {
                str += list.get(position).getNumbers().get(i)+"\t\t";
            }else {
                str += list.get(position).getNumbers().get(i) + "\n\n";
            }
        }
        holder.tv_participation_num.setText(str);
        holder.tv_participation_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.tv_participation_num.getVisibility() == View.GONE) {
                    holder.tv_participation_num.setVisibility(View.VISIBLE);
                    holder.tv_participation_detail.setText(context.getString(R.string.Fold));
                }else{
                    holder.tv_participation_num.setVisibility(View.GONE);
                    holder.tv_participation_detail.setText(context.getString(R.string.unFold));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_participation_time;
        private TextView tv_participation_people;
        private TextView tv_participation_detail;
        private TextView tv_participation_num;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_participation_time = (TextView) itemView.findViewById(R.id.tv_participation_time);
            tv_participation_people = (TextView) itemView.findViewById(R.id.tv_participation_people);
            tv_participation_detail = (TextView) itemView.findViewById(R.id.tv_participation_detail);
            tv_participation_num = (TextView) itemView.findViewById(R.id.tv_participation_num);
            tv_participation_num.setVisibility(View.GONE);
        }
    }
}
