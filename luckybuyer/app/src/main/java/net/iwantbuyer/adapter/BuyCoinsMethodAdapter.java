package net.iwantbuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.iwantbuyer.R;
import net.iwantbuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by admin on 2017/3/23.
 */
public class BuyCoinsMethodAdapter extends RecyclerView.Adapter<BuyCoinsMethodAdapter.MyViewholder> {
    private Context context;
    private List<String> list;

    public interface BuyCoinMethodOnClickListener {
        public void onClick(View view, String method);
    }

    public BuyCoinMethodOnClickListener buyCoinMethodOnClickListener;

    public void setBuyCoinMethodOnClickListener(BuyCoinMethodOnClickListener buyCoinMethodOnClickListener) {
        this.buyCoinMethodOnClickListener = buyCoinMethodOnClickListener;
    }

    public BuyCoinsMethodAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.itme_buycoins_method, null);
        MyViewholder holder = new MyViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, final int position) {
        Log.e("TAGandroid", list.get(position));
        if ("android-inapp".equals(list.get(position))) {
            holder.iv_buycoins_method.setBackgroundResource(R.drawable.buycoins_google);
        } else {
            try {
                Field field = R.drawable.class.getField("buycoins_" + list.get(position).replace(" ", "_").toLowerCase());
                int i = field.getInt(new R.drawable());
                holder.iv_buycoins_method.setBackgroundResource(i);
            } catch (Exception e) {
            }
        }
        if(list.get(position).equals(Utils.getSpData("method",context))) {
            holder.iv_buycoins_method.setHovered(true);
            holder.iv_buycoins_method_jiao.setVisibility(View.VISIBLE);
        }else {
            holder.iv_buycoins_method.setHovered(false);
            holder.iv_buycoins_method_jiao.setVisibility(View.GONE);
        }
        holder.iv_buycoins_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCoinMethodOnClickListener.onClick(v,list.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder {

        private ImageView iv_buycoins_method;
        private ImageView iv_buycoins_method_jiao;

        public MyViewholder(View itemView) {
            super(itemView);
            iv_buycoins_method = (ImageView) itemView.findViewById(R.id.iv_buycoins_method);
            iv_buycoins_method_jiao = (ImageView) itemView.findViewById(R.id.iv_buycoins_method_jiao);
        }
    }
}
