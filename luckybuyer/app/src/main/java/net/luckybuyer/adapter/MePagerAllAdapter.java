package net.luckybuyer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;

import net.luckybuyer.R;
import net.luckybuyer.bean.AllOrderBean;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.JustifyTextView;

import java.util.List;

/**
 * Created by admin on 2016/9/21.
 */
public class MePagerAllAdapter extends RecyclerView.Adapter<MePagerAllAdapter.ViewHolder> {
    private Context context;
    private List<AllOrderBean.AllorderBean> list;

    public MePagerAllAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        String status = list.get(position).getGame().getStatus();
        if("running".equals(status)) {
            return 0;
        }else if("closed".equals(status)) {
            String finishTime = list.get(position).getGame().getFinished_at();
            long time = Utils.Iso8601ToLong(finishTime);
            return 1;
        }else if("finished".equals(status)) {
            String id = list.get(position).getGame().getLucky_user().getId() + "";
            if(id.equals(Utils.getSpData("id",context))) {
                return 3;
            }
            return 2;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if(viewType == 0) {
            View inflate = View.inflate(context, R.layout.item_me_all,null);
            viewHolder = new ViewHolder(inflate,viewType);
        }else if(viewType == 1) {
            View inflate = View.inflate(context, R.layout.item_me_all_countdown,null);
            viewHolder = new ViewHolder(inflate,viewType);
        }else if(viewType == 2) {
            View inflate = View.inflate(context, R.layout.item_me_all_lucky,null);
            viewHolder = new ViewHolder(inflate,viewType);
        }else if(viewType == 3) {
            View inflate = View.inflate(context, R.layout.item_me_all_lucky,null);
            viewHolder = new ViewHolder(inflate,viewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(type == 0) {
            String picture = "htpps:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_all_icon.setImageBitmap(resource);
                }
            });
            holder.jtv_all_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_all_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_all_participation.setText("My participation:" + list.get(position).getShares());
            holder.tv_all_shares.setText("Total demand:" + list.get(position).getGame().getShares());
            holder.tv_all_leftshares.setText("Remaining:" + list.get(position).getGame().getLeft_shares());
            holder.pb_all_progress.setMax(list.get(position).getGame().getShares());
            holder.pb_all_progress.setProgress(list.get(position).getGame().getShares() - list.get(position).getGame().getLeft_shares());

        }else if(type == 1) {

        }else if(type == 2) {
            String picture = "https:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_lucky_icon.setImageBitmap(resource);
                }
            });
            holder.jtv_lucky_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_lucky_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_lucky_participation.setText("My participation:" + list.get(position).getShares());
            holder.tv_lucky_name.setText(list.get(position).getGame().getLucky_user().getProfile().getName());
        }else if(type == 3) {
            String picture = "https:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_lucky_icon.setImageBitmap(resource);
                }
            });

//            String status = list.get(position).getDelivery().getStatus();
//            if ("pending".equals(status)) {
//                holder.tv_lucky_go.setText("Please confirm shipping address");
//            } else if ("processing".equals(status)) {
//                holder.tv_lucky_go.setText("Waiting for shippment");
//            } else if ("shipping".equals(status)) {
//                holder.tv_lucky_go.setText("Shipped, out for delivery");
//            } else if ("finished".equals(status)) {
//                holder.tv_lucky_go.setText("Delivered and show to win awards");
//            }
            holder.jtv_lucky_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_lucky_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_lucky_participation.setText("My participation:" + list.get(position).getShares());
            holder.tv_lucky_name.setText(list.get(position).getGame().getLucky_user().getProfile().getName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //all
        private ImageView iv_all_icon;
        private JustifyTextView jtv_all_discribe;
        private TextView tv_all_issue;
        private TextView tv_all_participation;
        private TextView tv_all_goview;
        private ImageView iv_all_goview;
        private TextView tv_all_shares;
        private TextView tv_all_leftshares;
        private ProgressBar pb_all_progress;
        private RelativeLayout rl_all_continue;
        //countdown
        private ImageView iv_countdown_icon;
        private JustifyTextView jtv_countdown_discribe;
        private TextView tv_countdown_issue;
        private TextView tv_countdown_participation;
        private TextView tv_countdown_goview;
        private ImageView iv_countdown_goview;
        private RelativeLayout rl_countdown_continue;

        //luckey
        private ImageView iv_lucky_icon;
        private JustifyTextView jtv_lucky_discribe;
        private TextView tv_lucky_issue;
        private TextView tv_lucky_participation;
        private TextView tv_lucky_goview;
        private ImageView iv_lucky_goview;
        private TextView tv_lucky_name;
        private RelativeLayout rl_lucky_address;
        private RelativeLayout rl_lucky_continue;
        private TextView tv_lucky_go;

        public ViewHolder(View itemView,int type) {
            super(itemView);
            if(type == 0) {
                iv_all_icon = (ImageView) itemView.findViewById(R.id.iv_all_icon);
                jtv_all_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_all_discribe);
                tv_all_issue = (TextView) itemView.findViewById(R.id.tv_all_issue);
                tv_all_participation = (TextView) itemView.findViewById(R.id.tv_all_participation);
                tv_all_goview = (TextView) itemView.findViewById(R.id.tv_all_goview);
                iv_all_goview = (ImageView) itemView.findViewById(R.id.iv_all_goview);
                tv_all_shares = (TextView) itemView.findViewById(R.id.tv_all_shares);
                tv_all_leftshares = (TextView) itemView.findViewById(R.id.tv_all_leftshares);
                pb_all_progress = (ProgressBar) itemView.findViewById(R.id.pb_all_progress);
                rl_all_continue = (RelativeLayout) itemView.findViewById(R.id.rl_all_continue);
            }else if(type == 1) {
                iv_countdown_icon = (ImageView) itemView.findViewById(R.id.iv_countdown_icon);
                jtv_countdown_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_countdown_discribe);
                tv_countdown_issue = (TextView) itemView.findViewById(R.id.tv_countdown_issue);
                tv_countdown_participation = (TextView) itemView.findViewById(R.id.tv_countdown_participation);
                tv_countdown_goview = (TextView) itemView.findViewById(R.id.tv_countdown_goview);
                iv_countdown_goview = (ImageView) itemView.findViewById(R.id.iv_countdown_goview);
                rl_countdown_continue = (RelativeLayout) itemView.findViewById(R.id.rl_countdown_continue);
            }else if(type == 2) {
                iv_lucky_icon = (ImageView) itemView.findViewById(R.id.iv_lucky_icon);
                jtv_lucky_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_lucky_discribe);
                tv_lucky_issue = (TextView) itemView.findViewById(R.id.tv_lucky_issue);
                tv_lucky_participation = (TextView) itemView.findViewById(R.id.tv_lucky_participation);
                tv_lucky_goview = (TextView) itemView.findViewById(R.id.tv_lucky_goview);
                iv_lucky_goview = (ImageView) itemView.findViewById(R.id.iv_lucky_goview);
                tv_lucky_name = (TextView) itemView.findViewById(R.id.tv_lucky_name);
                rl_lucky_address = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_address);
                rl_lucky_continue = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_continue);
                rl_lucky_continue.setVisibility(View.VISIBLE);
                rl_lucky_address.setVisibility(View.GONE);
            }else if(type == 3) {
                iv_lucky_icon = (ImageView) itemView.findViewById(R.id.iv_lucky_icon);
                jtv_lucky_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_lucky_discribe);
                tv_lucky_issue = (TextView) itemView.findViewById(R.id.tv_lucky_issue);
                tv_lucky_participation = (TextView) itemView.findViewById(R.id.tv_lucky_participation);
                tv_lucky_goview = (TextView) itemView.findViewById(R.id.tv_lucky_goview);
                iv_lucky_goview = (ImageView) itemView.findViewById(R.id.iv_lucky_goview);
                tv_lucky_name = (TextView) itemView.findViewById(R.id.tv_lucky_name);
                rl_lucky_address = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_address);
                rl_lucky_continue = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_continue);
                tv_lucky_go = (TextView) itemView.findViewById(R.id.tv_lucky_go);
                rl_lucky_continue.setVisibility(View.GONE);
                rl_lucky_address.setVisibility(View.VISIBLE);
            }
        }
    }
}
