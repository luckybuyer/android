package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.luckybuyer.bean.ShippingAddressBean;

import java.util.List;

/**
 * Created by admin on 2016/10/8.
 */
public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ViewHolder> {
    private Context context;
    private List<ShippingAddressBean.ShippingBean> list;
    public ShippingAdapter(Context context, List<ShippingAddressBean.ShippingBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
