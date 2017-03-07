package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;

import net.iwantbuyer.R;
import net.iwantbuyer.bean.BuyCoinBean;
import net.iwantbuyer.bean.BuyCoinsMethodBean;

import org.json.JSONException;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/3/6.
 */
public class BuyCoinMethodAdapter extends RecyclerView.Adapter<BuyCoinMethodAdapter.ViewHolder> {

    private Context context;
    private List<BuyCoinsMethodBean> list;

    public interface BuyCoinMethodsOnClickListener {
        public void onClick(View view, String method);
    }

    public BuyCoinMethodsOnClickListener buyCoinOnMethodClickListener;

    public void setBuyCoinMethodOnClickListener(BuyCoinMethodsOnClickListener buyCoinMethodsOnClickListener) {
        this.buyCoinOnMethodClickListener = buyCoinMethodsOnClickListener;
    }

    public BuyCoinMethodAdapter(Context context, List<BuyCoinsMethodBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buycoins_methods, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if ("android-inapp".equals(list.get(position).getMethod())) {
            holder.iv_buycoins_method_pay.setBackgroundResource(R.drawable.buycoins_google);
        } else if ("paypal".equals(list.get(position).getMethod())) {
            holder.iv_buycoins_method_pay.setBackgroundResource(R.drawable.buycoins_paypal);
        } else if ("visa".equals(list.get(position).getMethod())) {
            holder.iv_buycoins_method_pay.setBackgroundResource(R.drawable.buycoins_visa);
        } else if ("halopay".equals(list.get(position).getMethod())) {
            holder.iv_buycoins_method_pay.setBackgroundResource(R.drawable.buycoins_cashu);
        }
        if (list.get(position).isFlag()) {
            holder.iv_buycoins_method_mark.setVisibility(View.VISIBLE);
        } else {
            holder.iv_buycoins_method_mark.setVisibility(View.GONE);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCoinOnMethodClickListener.onClick(v, list.get(position).getMethod());
                String method = list.get(position).getMethod();
                if ("android-inapp".equals(method)) {
                    //AppFlyer 埋点
                    Map eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context, "CLICK:Google Pay", eventValue);
                } else if ("halopay".equals(method)) {
                    //AppFlyer 埋点
                    Map eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context, "CLICK: Halo pay", eventValue);
                } else if ("paypal".equals(method)) {
                    //AppFlyer 埋点
                    Map eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context, "CLICK:Paypal", eventValue);
                } else if ("visa".equals(method)) {
                    Map eventValue = new HashMap<String, Object>();
                    AppsFlyerLib.getInstance().trackEvent(context, "CLICK: Master or Visa", eventValue);
                }
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setFlag(false);
                }
                list.get(position).setFlag(true);
                BuyCoinMethodAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_buycoins_method_pay;
        private ImageView iv_buycoins_method_mark;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_buycoins_method_pay = (ImageView) itemView.findViewById(R.id.iv_buycoins_method_pay);
            iv_buycoins_method_mark = (ImageView) itemView.findViewById(R.id.iv_buycoins_method_mark);
            view = itemView;
        }
    }
}
