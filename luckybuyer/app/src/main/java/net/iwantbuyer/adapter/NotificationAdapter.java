package net.iwantbuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.iwantbuyer.R;

import java.util.ArrayList;

/**
 * Created by admin on 2017/2/20.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    public NotificationAdapter(Context context, ArrayList arrayList) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        String event= "";
//        if("game_closed".equals(event)) {
//            return 0;
//        }else if("user_won".equals(event)) {
//            return 1;
//        }else if("delivery_shipping".equals(event)) {
//            return 2;
//        }else if("delivery_finished".equals(event)) {
//            return 3;
//        }else {
//            return 4;
//        }

        if(position == 0) {
            return 0;
        }else if(position == 1) {
            return 1;
        }else if(position == 2) {
            return 2;
        }else if(position == 3) {
            return 3;
        }else {
            return 4;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        if(viewType == 0 || viewType == 1|| viewType == 2|| viewType == 3) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification_all, null);
            holder = new ViewHolder(view,viewType);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification_noimg, null);
            holder = new ViewHolder(view,viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.view.setOnClickListener(new MyOnClickListener(getItemViewType(position)));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    
    class MyOnClickListener implements View.OnClickListener{
        
        private int viewType;
        public MyOnClickListener(int itemViewType) {
            this.viewType = viewType;
        }

        @Override
        public void onClick(View v) {
            if(viewType == 0) {
                //产品详情页
            }else if(viewType == 1) {
                //dispatch 页面
            }else if(viewType == 2) {
                //dispatch 页面
            }else if(viewType == 3) {
                //editshow 页面
            }else {
                
            }
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView tv_notification_time;
        private TextView item_notification_type;

        //all
        private ImageView iv_notification_all_img;
        private TextView tv_notification_all_discribe;

        //no img
        private TextView tv_notification_noimg_discribe;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            view = itemView;
            tv_notification_time = (TextView) itemView.findViewById(R.id.tv_notification_time);
            item_notification_type = (TextView) itemView.findViewById(R.id.item_notification_type);
            if(viewType == 0 || viewType == 1|| viewType == 2|| viewType == 3) {
                iv_notification_all_img = (ImageView) itemView.findViewById(R.id.iv_notification_all_img);
                tv_notification_all_discribe = (TextView) itemView.findViewById(R.id.tv_notification_all_discribe);
            }else {
                tv_notification_noimg_discribe = (TextView) itemView.findViewById(R.id.tv_notification_noimg_discribe);
            }
        }
    }
}
