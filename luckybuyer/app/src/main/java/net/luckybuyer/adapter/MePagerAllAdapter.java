package net.luckybuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.luckybuyer.R;

import java.util.List;

/**
 * Created by admin on 2016/9/21.
 */
public class MePagerAllAdapter extends RecyclerView.Adapter<MePagerAllAdapter.ViewHolder> {
    private Context context;
    private List list;

    public MePagerAllAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if(position % 2 != 0) {
            return 0;
        }else if(position % 2 == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if(viewType == 0) {
            View inflate = View.inflate(context, R.layout.item_me_all_countdown,null);
            viewHolder = new ViewHolder(inflate);
        }else if(viewType == 1) {
            View inflate = View.inflate(context, R.layout.item_me_all,null);
            viewHolder = new ViewHolder(inflate);
        }

        return viewHolder;
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
