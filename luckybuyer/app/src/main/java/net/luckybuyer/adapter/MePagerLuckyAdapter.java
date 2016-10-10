package net.luckybuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import net.luckybuyer.R;
import net.luckybuyer.activity.MainActivity;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.bean.AllOrderBean;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.JustifyTextView;

import java.io.Serializable;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.jtv_lucky_discribe.setText(list.get(position).getGame().getProduct().getTitle() + "");
        holder.tv_lucky_issue.setText("Issue:" + list.get(position).getGame().getIssue_id() + "");
        holder.tv_lucky_participation.setText("My participation:" + list.get(position).getNumbers().size() + "");
        holder.tv_lucky_name.setText(list.get(position).getGame().getLucky_user().getProfile().getName() + "");
        String picture = "http:" + list.get(position).getGame().getProduct().getTitle_image();
        Glide.with(context).load(picture).into(holder.iv_lucky_icon);
//        Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Log.e("TAG", resource+"");
//                holder.iv_lucky_icon.setImageBitmap(resource);
//            }
//        });
        int viewType = getItemViewType(position);
        viewType = 0;
        if(viewType == 0) {
            holder.tv_lucky_go.setText("Confirm shipping address");
        }else if(viewType ==1) {
            holder.tv_lucky_go.setText("Waiting for shippment");
        }else if(viewType == 2) {
            holder.tv_lucky_go.setText("Shipped, out for delivery");
        }else if(viewType == 3) {
            holder.tv_lucky_go.setText("Delivered and show to win awards");
        }

        holder.tv_lucky_goview.setOnClickListener(new MyOnClickListener(position));
        holder.iv_lucky_goview.setOnClickListener(new MyOnClickListener(position));
        holder.rl_lucky_address.setOnClickListener(new MyOnClickListener(position));

    }

    @Override
    public int getItemCount() {
        if(list!= null) {
            return list.size();
        }
        return 0;
    }

    class MyOnClickListener implements View.OnClickListener {
        int position = -1;
        public MyOnClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_lucky_goview:
                    Utils.MyToast(context,"goview");
                    break;
                case R.id.iv_lucky_goview:
                    Utils.MyToast(context,"goview");
                    break;
                case R.id.rl_lucky_address:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from","dispatchpager");
                    intent.putExtra("alllist", (Serializable) list);
                    intent.putExtra("position", position);
                    ((MainActivity)context).startActivity(intent);
                    break;
            }
        }
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

