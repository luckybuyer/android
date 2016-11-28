package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.bean.AllOrderBean;
import net.iwantbuyer.view.JustifyTextView;

import java.util.List;

/**
 * Created by admin on 2016/9/22.
 */
public class MePagerLuckyAdapter extends RecyclerView.Adapter<MePagerLuckyAdapter.ViewHolder> {
    private Context context;
    public List<AllOrderBean.AllorderBean> list;
    private ScrollView sv_me;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    public MePagerLuckyAdapter(Context context, List list, ScrollView sv_me) {
        this.context = context;
        this.list = list;
        this.sv_me = sv_me;
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
        holder.tv_lucky_participation.setText("My participation:" + list.get(position).getGame().getLucky_order().getTotal_shares() + "");
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
        holder.rl_top.setOnClickListener(new MyOnClickListener(position));

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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sv_me.scrollTo(0,0);
                }
            },500);
            switch (view.getId()){
                case R.id.tv_lucky_goview:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from","participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    ((MainActivity)context).startActivity(intent);
                    break;
                case R.id.iv_lucky_goview:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from","participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    ((MainActivity)context).startActivity(intent);
                    break;
                case R.id.rl_lucky_address:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "dispatchpager");
                    intent.putExtra("dispatch_game_id", list.get(position).getGame().getLucky_order().getId());
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.rl_top:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("batch_id", list.get(position).getGame().getBatch_id());
                    context.startActivity(intent);
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
        private RelativeLayout rl_top;

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
            rl_top = (RelativeLayout) itemView.findViewById(R.id.rl_top);

            rl_lucky_address = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_address);
        }
    }
}

