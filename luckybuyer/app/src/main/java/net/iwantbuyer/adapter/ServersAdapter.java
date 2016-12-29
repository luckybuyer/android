package net.iwantbuyer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public interface OnClickListener {
        void onclick(String country);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public ServersAdapter(Context context, String response) {
        this.context = context;
        ServerBean serverBean = new Gson().fromJson(response, ServerBean.class);
        for (int i = 0; i < serverBean.getServers().size(); i++) {
            count = serverBean.getServers().get(0).getCountries().size();
            list.addAll(serverBean.getServers().get(i).getCountries());
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
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_servers_one, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_sercers_two, null);
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    int tag = 0;
    int currentPostion = -1;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if(tag < list.size()) {
//            if (list.get(position).equals(Utils.getSpData("country", context))) {
//                holder.iv_country.setVisibility(View.VISIBLE);
//            }else {
//                holder.iv_country.setVisibility(View.GONE);
//            }
//            tag++;
//        }else {
//            holder.iv_country.setVisibility(View.GONE);
//        }
        if(currentPostion == position) {
            holder.iv_country.setVisibility(View.VISIBLE);
        }else {
            if (list.get(position).equals(Utils.getSpData("country", context)) && tag == 0) {
                holder.iv_country.setVisibility(View.VISIBLE);
                tag ++;
            }else {
                holder.iv_country.setVisibility(View.GONE);
            }
        }

        try {
            Field field = R.drawable.class.getField(list.get(position).replace(" ","_").toLowerCase());
            int i = field.getInt(new R.drawable());
            holder.iv_country_flag.setImageResource(i);
//            Log.e("TAG", image + "");
        } catch (Exception e) {

        }

        holder.tv_country.setText(list.get(position) + "");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG_position", position +"");
                onClickListener.onclick(list.get(position));
                currentPostion = position;
                notifyDataSetChanged();
            }
        });
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

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_country_flag = (ImageView) itemView.findViewById(R.id.iv_country_flag);
            tv_country = (TextView) itemView.findViewById(R.id.tv_country);
            iv_country = (ImageView) itemView.findViewById(R.id.iv_country);
            view = itemView;
        }
    }
}
