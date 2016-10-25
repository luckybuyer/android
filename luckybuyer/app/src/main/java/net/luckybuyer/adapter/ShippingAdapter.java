package net.luckybuyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.bean.ShippingAddressBean;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;
import net.luckybuyer.view.JustifyTextView;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/8.
 */
public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ViewHolder> {
    int WHAT = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };
    private Context context;
    private List<ShippingAddressBean.ShippingBean> list;

    public ShippingAdapter(Context context, List<ShippingAddressBean.ShippingBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shipping, null);
        ViewHolder holder = new ViewHolder(view);
        Log.e("TAG", "hahaha");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_shipping_name.setText(list.get(position).getName() + "");
        holder.tv_shipping_telnum.setText(list.get(position).getPhone());
        holder.jtv_shipping_detailed.setText(list.get(position).getAddress());
        if (list.get(position).isIs_default()) {
            holder.cb_shipping_select.setChecked(true);
        } else {
            holder.cb_shipping_select.setChecked(false);
        }

        holder.cb_shipping_select.setOnClickListener(new MyOnClickListener(position));
        holder.tv_shipping_edit.setOnClickListener(new MyOnClickListener(position));
        holder.tv_shipping_delete.setOnClickListener(new MyOnClickListener(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cb_shipping_select:       //更换默认接口

                    String url = MyApplication.url + "/v1/addresses/" + list.get(position).getId() + "?timezone=" + MyApplication.utc;

                    String json = "{\"address\": \"" + list.get(position).getAddress() + "\",\"id\": " + list.get(position).getId() + ",\"is_default\": true,\"name\": \"" + list.get(position).getName() + "\",\"phone\": \"" + list.get(position).getPhone() + "\",\"zipcode\": " + "\"\"" + "}";
                    Map map = new HashMap();
                    String mToken = Utils.getSpData("token", context);
                    map.put("Authorization", "Bearer " + mToken);
                    HttpUtils.getInstance().startNetworkWaiting(context);
                    HttpUtils.getInstance().putJson(url, json, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String response) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < list.size(); i++) {
                                        list.get(i).setIs_default(false);
                                    }
                                    list.get(position).setIs_default(true);
                                    handler.sendEmptyMessage(WHAT);

                                    HttpUtils.getInstance().stopNetWorkWaiting();
                                    Utils.MyToast(context, "Set Success");

                                    if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
                                        ((SecondPagerActivity) context).switchPage(7);
                                    }
                                }
                            });
                        }

                        @Override
                        public void error(final int code, final String message) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpUtils.getInstance().stopNetWorkWaiting();
                                    Utils.MyToast(context, "Setup failed");
                                }
                            });
                        }

                        @Override
                        public void failure(Exception exception) {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            Utils.MyToast(context, "Setup failed");
                        }


                    });
                    break;
                case R.id.tv_shipping_edit:
                    ((SecondPagerActivity) context).switchPage(8);
                    ((SecondPagerActivity) context).from = "shippingaddress";
                    break;
                case R.id.tv_shipping_delete:
                    url = MyApplication.url + "/v1/addresses/"+list.get(position).getId()+"?timezone=" + MyApplication.utc;

                    map = new HashMap();
                    mToken = Utils.getSpData("token", context);
                    map.put("Authorization", "Bearer " + mToken);
                    HttpUtils.getInstance().startNetworkWaiting(context);
                    HttpUtils.getInstance().deleteResponse(url, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String response) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpUtils.getInstance().stopNetWorkWaiting();
                                }
                            });
                        }

                        @Override
                        public void error(final int code, final String message) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(code == 204) {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    HttpUtils.getInstance().stopNetWorkWaiting();
                                }
                            });
                        }

                        @Override
                        public void failure(Exception exception) {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                        }


                    });
                    break;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_shipping_name;
        private TextView tv_shipping_telnum;
        private JustifyTextView jtv_shipping_detailed;
        private CheckBox cb_shipping_select;
        private TextView tv_shipping_edit;
        private TextView tv_shipping_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_shipping_name = (TextView) itemView.findViewById(R.id.tv_shipping_name);
            tv_shipping_telnum = (TextView) itemView.findViewById(R.id.tv_shipping_telnum);
            jtv_shipping_detailed = (JustifyTextView) itemView.findViewById(R.id.jtv_shipping_detailed);
            cb_shipping_select = (CheckBox) itemView.findViewById(R.id.cb_shipping_select);
            tv_shipping_edit = (TextView) itemView.findViewById(R.id.tv_shipping_edit);
            tv_shipping_delete = (TextView) itemView.findViewById(R.id.tv_shipping_delete);
        }
    }
}
