package net.luckybuyer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.luckybuyer.R;
import net.luckybuyer.bean.PreviousWinnerBean;
import net.luckybuyer.view.CircleImageView;
import net.luckybuyer.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
public class previousWinnersAdapter extends RecyclerView.Adapter<previousWinnersAdapter.ViewHolder> {
    private Context context;
    private List<PreviousWinnerBean.PreviousBean> list;

    public previousWinnersAdapter(Context context, List<PreviousWinnerBean.PreviousBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_previous, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_previous_issue.setText("[Issue:" + list.get(position).getIssue_id() + "]");
        holder.tv_previous_time.setText(list.get(position).getFinished_at().substring(0, 19).replace("T", " "));
        holder.tv_previous_name.setText(list.get(position).getLucky_user().getProfile().getName() + "");
        holder.tv_previous_participation.setText(list.get(position).getLucky_order().getTotal_shares() + "");
        holder.tv_previous_luckynum.setText(list.get(position).getLucky_number() + "");
        Glide.with(context).load(list.get(position).getLucky_user().getProfile().getPicture()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.civ_provious_icon.setImageBitmap(resource);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_previous_issue;
        private TextView tv_previous_time;
        private TextView tv_previous_name;
        private TextView tv_previous_participation;
        private TextView tv_previous_luckynum;
        private CircleImageView civ_provious_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_previous_issue = (TextView) itemView.findViewById(R.id.tv_previous_issue);
            tv_previous_time = (TextView) itemView.findViewById(R.id.tv_previous_time);
            tv_previous_name = (TextView) itemView.findViewById(R.id.tv_previous_name);
            tv_previous_participation = (TextView) itemView.findViewById(R.id.tv_previous_participation);
            tv_previous_luckynum = (TextView) itemView.findViewById(R.id.tv_previous_luckynum);
            civ_provious_icon = (CircleImageView) itemView.findViewById(R.id.civ_provious_icon);
        }
    }
}
