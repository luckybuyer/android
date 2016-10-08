package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.bean.AllOrderBean;
import net.luckybuyer.view.JustifyTextView;

import java.util.List;

/**
 * Created by admin on 2016/9/22.
 */
public class MePagerLuckyAdapter extends RecyclerView.Adapter<MePagerLuckyAdapter.ViewHolder> {
    private Context context;
    private List<AllOrderBean.AllorderBean> list;

    public MePagerLuckyAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        String status = list.get(position).getDelivery().getStatus();
        if ("pending".equals(status)) {
            return 0;
        } else if ("processing".equals(status)) {
            return 1;
        } else if ("shipping".equals(status)) {
            return 2;
        } else if ("finished".equals(status)) {
            return 3;
        }
        return super.getItemViewType(position);
    }

    @Override
    public MePagerLuckyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_me_all_lucky, null);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        viewType = 0;
        if(viewType == 0) {
            holder.tv_lucky_go.setText("Please confirm shipping address");
        }else if(viewType ==1) {
            holder.tv_lucky_go.setText("Waiting for shippment");
        }else if(viewType == 2) {
            holder.tv_lucky_go.setText("Shipped, out for delivery");
        }else if(viewType == 3) {
            holder.tv_lucky_go.setText("Delivered and show to win awards");
        }
    }

    @Override
    public int getItemCount() {
        if(list!= null) {
            return list.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //luckey
        private ImageView iv_lucky_icon;
        private JustifyTextView jtv_lucky_discribe;
        private TextView tv_lucky_issue;
        private TextView tv_lucky_participation;
        private TextView tv_lucky_goview;
        private ImageView iv_lucky_goview;
        private TextView tv_lucky_name;
        private TextView tv_lucky_go;
        private RelativeLayout rl_lucky_address;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_lucky_icon = (ImageView) itemView.findViewById(R.id.iv_lucky_icon);
            jtv_lucky_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_lucky_discribe);
            tv_lucky_issue = (TextView) itemView.findViewById(R.id.tv_lucky_issue);
            tv_lucky_participation = (TextView) itemView.findViewById(R.id.tv_lucky_participation);
            tv_lucky_goview = (TextView) itemView.findViewById(R.id.tv_lucky_goview);
            iv_lucky_goview = (ImageView) itemView.findViewById(R.id.iv_lucky_goview);
            tv_lucky_name = (TextView) itemView.findViewById(R.id.tv_lucky_name);
            tv_lucky_go = (TextView) itemView.findViewById(R.id.tv_lucky_go);

            rl_lucky_address = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_address);
        }
    }
}

