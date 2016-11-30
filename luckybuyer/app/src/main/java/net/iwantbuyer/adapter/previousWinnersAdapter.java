package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
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

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.bean.PreviousWinnerBean;
import net.iwantbuyer.view.CircleImageView;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_previous_issue.setText(list.get(position).getIssue_id() + "");
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
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("game_id",list.get(position).getId());
                intent.putExtra("batch_id",list.get(position).getBatch_id());
                intent.putExtra("from","productdetail");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView tv_previous_issue;
        private TextView tv_previous_time;
        private TextView tv_previous_name;
        private TextView tv_previous_participation;
        private TextView tv_previous_luckynum;
        private CircleImageView civ_provious_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_previous_issue = (TextView) itemView.findViewById(R.id.tv_previous_issue);
            tv_previous_time = (TextView) itemView.findViewById(R.id.tv_previous_time);
            tv_previous_name = (TextView) itemView.findViewById(R.id.tv_previous_name);
            tv_previous_participation = (TextView) itemView.findViewById(R.id.tv_previous_participation);
            tv_previous_luckynum = (TextView) itemView.findViewById(R.id.tv_previous_luckynum);
            civ_provious_icon = (CircleImageView) itemView.findViewById(R.id.civ_provious_icon);
        }
    }
}
