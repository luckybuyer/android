package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.WelcomeActivity;
import net.iwantbuyer.bean.ServerBean;
import net.iwantbuyer.utils.Utils;

import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by admin on 2016/12/27.
 */
public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.MyViewHolder> {
    private Context context;
    private List<String> list = new ArrayList<>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };
    int count;
    private final ServerBean serverBean;

    public interface OnClickListener {
        void onclick(String country);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ServersAdapter(Context context, String response) {
        this.context = context;
        serverBean = new Gson().fromJson(response, ServerBean.class);
        for (int i = 0; i < serverBean.getServers().size(); i++) {
//            count = serverBean.getServers().get(0).getCountries().size();
            list.addAll(serverBean.getServers().get(i).getCountries());
        }

//        count = serverBean.getServers().size();

        for (int i = 0; i < serverBean.getServers().size(); i++) {
            listCount.add(count);
            count += serverBean.getServers().get(i).getCountries().size();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position < count) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//        if (viewType == 0) {
//            view = LayoutInflater.from(context).inflate(R.layout.item_servers_one, null);
//        } else {
//            view = LayoutInflater.from(context).inflate(R.layout.item_sercers_two, null);
//        }
        view = LayoutInflater.from(context).inflate(R.layout.item_servers_one, null);
        MyViewHolder holder = new MyViewHolder(view, viewType);
        return holder;
    }

    int tag = 0;
    int currentPostion = -1;
    List<Integer> listCount = new ArrayList();
    boolean flag = false;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        for (int i = 0; i < listCount.size(); i++) {
            if (position == listCount.get(i)) {
                flag = true;
                holder.tv_severs_store.setText(serverBean.getServers().get(i).getName()+"");
            }
        }
        if (!flag) {
            holder.rl_server.setVisibility(View.GONE);
        } else {
            holder.rl_server.setVisibility(View.VISIBLE);
        }
        flag = false;
//        if(getItemViewType(position) == 0) {
//            if(position > 0 && position < serverBean.getServers().get(0).getCountries().size()) {
//                holder.rl_server.setVisibility(View.GONE);
//            }
//        }
//
//        if(getItemViewType(position) == 1 && position > 0 && getItemViewType(position -1) == 0) {
//            holder.rl_server.setVisibility(View.VISIBLE);
//        }else {
//
//        }
//        if(getItemViewType(position) == 1 && position > center+1) {
//            holder.rl_center.setVisibility(View.GONE);
//        }
        if (currentPostion == position) {
            holder.iv_country.setVisibility(View.VISIBLE);
        } else {
            if (list.get(position).equals(Utils.getSpData("country", context)) && tag == 0) {
                holder.iv_country.setVisibility(View.VISIBLE);
                tag++;
            } else {
                holder.iv_country.setVisibility(View.GONE);
            }
        }

        try {
            Field field = R.drawable.class.getField(list.get(position).replace(" ", "_").toLowerCase());
            int i = field.getInt(new R.drawable());
            holder.iv_country_flag.setImageResource(i);
//            Log.e("TAG", image + "");
        } catch (Exception e) {

        }

        holder.tv_country.setText(list.get(position) + "");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onclick(list.get(position));
                currentPostion = position;
                notifyDataSetChanged();
            }
        });
        if (getItemViewType(position) == 1) {
            holder.rl_server_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onclick(list.get(position));
                    currentPostion = position;
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_country_flag;
        private TextView tv_country;
        private ImageView iv_country;
        private View view;
        private TextView tv_severs_store;
        private RelativeLayout rl_center;
        private RelativeLayout rl_server;
        private RelativeLayout rl_server_two;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            iv_country_flag = (ImageView) itemView.findViewById(R.id.iv_country_flag);
            tv_country = (TextView) itemView.findViewById(R.id.tv_country);
            iv_country = (ImageView) itemView.findViewById(R.id.iv_country);
            rl_server = (RelativeLayout) itemView.findViewById(R.id.rl_server);
            tv_severs_store = (TextView) itemView.findViewById(R.id.tv_severs_store);
//            if(viewType == 1) {
//                rl_center = (RelativeLayout) itemView.findViewById(R.id.rl_center);
//                rl_server_two = (RelativeLayout) itemView.findViewById(R.id.rl_server_two);
//            }
            view = itemView;


        }
    }
}
