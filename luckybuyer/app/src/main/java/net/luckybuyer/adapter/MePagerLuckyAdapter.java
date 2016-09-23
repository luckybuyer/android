package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.luckybuyer.R;

import java.util.List;

/**
 * Created by admin on 2016/9/22.
 */
public class MePagerLuckyAdapter extends RecyclerView.Adapter<MePagerLuckyAdapter.ViewHolder> {
    private Context context;
    private List list;

    public MePagerLuckyAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (position % 2 != 0) {
            return 0;
        } else if (position % 2 == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public MePagerLuckyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_me_lucky, null);
        ViewHolder viewHolder = new ViewHolder(inflate);
        viewHolder = new ViewHolder(inflate);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
