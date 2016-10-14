package net.luckybuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.bean.PreviousWinnerBean;
import net.luckybuyer.view.JustifyTextView;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2016/10/13.
 */
public class WinningAdapter extends RecyclerView.Adapter<WinningAdapter.ViewHolder> {
    private List<PreviousWinnerBean.PreviousBean> list;
    private Context context;
    public WinningAdapter(Context context, List<PreviousWinnerBean.PreviousBean> list) {
        this.list = list;
        this.context = context;
        for (int i = 0;i < list.size();i++){
            if("running".equals(list.get(i).getStatus())) {
                list.remove(i);
                i--;
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        String status = list.get(position).getStatus();
        if("closed".equals(status)) {
            return 0;
        }else if("finished".equals(status)) {
            return 1;
        }
        return super.getItemViewType(position);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if(viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_winning_closed, null);
            viewHolder = new ViewHolder(view,viewType);
        }else if(viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_winning_finished, null);
            viewHolder = new ViewHolder(view,viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.jtv_winning_title.setText(list.get(position).getProduct().getTitle());
        holder.item_winning_issue.setText("Issue:" + list.get(position).getIssue_id());
        Glide.with(context).load("http:" + list.get(position).getProduct().getDetail_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.iv_winning_icon.setImageBitmap(resource);
            }
        });
        int type = getItemViewType(position);
        if(type == 0) {

        }else if(type == 1) {
            holder.tv_winning_luckynum.setText(list.get(position).getLucky_number());
            holder.tv_winning_winningname.setText(list.get(position).getLucky_user().getProfile().getName() + "");
            holder.tv_winning_participations.setText(list.get(position).getLucky_order().getNumbers().size() + "");
            holder.tv_winning_announced.setText(list.get(position).getFinished_at().substring(0,19).replace("T"," ") + "");
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("from","productdetail");
                intent.putExtra("game_id",list.get(position).getId());
                intent.putExtra("batch_id",list.get(position).getBatch_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", list.size()+"231");
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //公共的
        View view;
        ImageView iv_winning_icon;
        JustifyTextView jtv_winning_title;
        TextView item_winning_issue;

        //倒计时
        TextView tv_winning_countdown1;
        TextView tv_winning_countdown2;
        TextView tv_winning_countdown3;
        TextView tv_winning_countdown4;
        TextView tv_winning_countdown5;
        TextView tv_winning_countdown6;

        //已经完事的
        TextView tv_winning_luckynum;
        TextView tv_winning_winningname;
        TextView tv_winning_participations;
        TextView tv_winning_announced;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 0) {
                view = itemView;
                iv_winning_icon = (ImageView) itemView.findViewById(R.id.iv_winning_icon);
                jtv_winning_title = (JustifyTextView) itemView.findViewById(R.id.jtv_winning_title);
                item_winning_issue = (TextView) itemView.findViewById(R.id.item_winning_issue);
                tv_winning_countdown1 = (TextView) itemView.findViewById(R.id.tv_winning_countdown1);
                tv_winning_countdown2 = (TextView) itemView.findViewById(R.id.tv_winning_countdown2);
                tv_winning_countdown3 = (TextView) itemView.findViewById(R.id.tv_winning_countdown3);
                tv_winning_countdown4 = (TextView) itemView.findViewById(R.id.tv_winning_countdown4);
                tv_winning_countdown5 = (TextView) itemView.findViewById(R.id.tv_winning_countdown5);
                tv_winning_countdown6 = (TextView) itemView.findViewById(R.id.tv_winning_countdown6);
            }else if(viewType == 1) {
                view = itemView;
                iv_winning_icon = (ImageView) itemView.findViewById(R.id.iv_winning_icon);
                jtv_winning_title = (JustifyTextView) itemView.findViewById(R.id.jtv_winning_title);
                item_winning_issue = (TextView) itemView.findViewById(R.id.item_winning_issue);
                tv_winning_luckynum = (TextView) itemView.findViewById(R.id.tv_winning_luckynum);
                tv_winning_winningname = (TextView) itemView.findViewById(R.id.tv_winning_winningname);
                tv_winning_participations = (TextView) itemView.findViewById(R.id.tv_winning_participations);
                tv_winning_announced = (TextView) itemView.findViewById(R.id.tv_winning_announced);

            }
        }
    }
}
