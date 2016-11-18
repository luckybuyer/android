package net.smartbuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.smartbuyer.R;

import java.util.List;

/**
 * Created by admin on 2016/10/21.
 */
public class AddAddressAdapter extends RecyclerView.Adapter<AddAddressAdapter.ViewHolder> {
    private Context context;
    private List<String> list;
    public interface OnClickListener{
        public void OnClick(String area);
    }
    public OnClickListener onClickListener;
    public void SetOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public AddAddressAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_addaddress, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_addadress_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null) {
                    onClickListener.OnClick(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_addadress_selector;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_addadress_selector = (TextView) itemView.findViewById(R.id.tv_addadress_selector);
        }
    }
}
