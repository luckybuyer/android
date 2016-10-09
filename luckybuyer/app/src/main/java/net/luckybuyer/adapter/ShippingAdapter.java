package net.luckybuyer.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.bean.ShippingAddressBean;
import net.luckybuyer.view.JustifyTextView;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by admin on 2016/10/8.
 */
public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ViewHolder> {
    int WHAT =1;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            notifyDataSetChanged();
        }
    };
    private Context context;
    private List<ShippingAddressBean.ShippingBean> list;
    public ShippingAdapter(Context context, List<ShippingAddressBean.ShippingBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shipping, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_shipping_name.setText(list.get(position).getName()+"");
        holder.tv_shipping_telnum.setText(list.get(position).getPhone());
        holder.jtv_shipping_detailed.setText(list.get(position).getAddress());
        if(list.get(position).isIs_default()) {
            holder.cb_shipping_select.setChecked(true);
        }else {
            holder.cb_shipping_select.setChecked(false);
        }
        holder.cb_shipping_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (int i = 0;i < list.size();i++){
                    list.get(i).setIs_default(false);

                }
                list.get(position).setIs_default(true);
                handler.sendEmptyMessage(WHAT);
                ((SecondPagerActivity)context).shippingBean = list.get(position);
                ((SecondPagerActivity)context).switchPage(7);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_shipping_name;
        private TextView tv_shipping_telnum;
        private JustifyTextView jtv_shipping_detailed;
        private CheckBox cb_shipping_select;
        private TextView tv_shipping_edit;
        private TextView tv_shipping_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_shipping_name = (TextView) itemView.findViewById(R.id.tv_shipping_name);
            tv_shipping_telnum = (TextView) itemView.findViewById(R.id.tv_shipping_telnum);
            jtv_shipping_detailed = (JustifyTextView) itemView.findViewById(R.id.jtv_shipping_detailed);
            cb_shipping_select = (CheckBox) itemView.findViewById(R.id.cb_shipping_select);
            tv_shipping_edit = (TextView) itemView.findViewById(R.id.tv_shipping_edit);
            tv_shipping_delete = (TextView) itemView.findViewById(R.id.tv_shipping_delete);
        }
    }
}
