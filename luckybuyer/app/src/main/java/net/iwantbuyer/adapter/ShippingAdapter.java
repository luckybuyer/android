package net.iwantbuyer.adapter;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.ShippingAddressBean;
import net.iwantbuyer.secondpager.DispatchPager;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.JustifyTextView;

import java.util.HashMap;
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
        holder.jtv_shipping_detailed.setText(list.get(position).getAddress() + "-" + list.get(position).getZipcode());
        if (list.get(position).isIs_default()) {
            holder.cb_shipping_select.setChecked(true);
            ((SecondPagerActivity)context).shippingBean = list.get(position);
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
                    map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
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
                                    Utils.MyToast(context, context.getString(R.string.setsuccess));

                                    if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
                                        ((SecondPagerActivity) context).switchPage(7);
                                        ((DispatchPager)(((SecondPagerActivity) context).list.get(7))).initData();
                                    }
                                }
                            });

                            Log.e("TAG+更换默认接口", response);
                        }

                        @Override
                        public void error(final int code, final String message) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpUtils.getInstance().stopNetWorkWaiting();
                                    Utils.MyToast(context,context.getString(R.string.Networkfailure) + code + "address");
                                }
                            });
                        }

                        @Override
                        public void failure(Exception exception) {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            Utils.MyToast(context, context.getString(R.string.Networkfailure));
                        }


                    });
                    break;
                case R.id.tv_shipping_edit:
                    ((SecondPagerActivity) context).switchPage(8);
                    ((SecondPagerActivity) context).address_id = list.get(position).getId();
                    break;
                case R.id.tv_shipping_delete:
                    StartAlertDialog(position);
                    break;
                case R.id.tv_deleteadd_cancel:
                    if(show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_deleteadd_ok:
                    if(show.isShowing()) {
                        show.dismiss();
                    }

                    url = MyApplication.url + "/v1/addresses/"+list.get(position).getId()+"?timezone=" + MyApplication.utc;

                    map = new HashMap();
                    mToken = Utils.getSpData("token", context);
                    map.put("Authorization", "Bearer " + mToken);
                    map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
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

    private AlertDialog show;
    private void StartAlertDialog(int position) {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View view = View.inflate(context, R.layout.alertdialog_shippingaddress_delete, null);
        TextView tv_deleteadd_cancel = (TextView) view.findViewById(R.id.tv_deleteadd_cancel);
        TextView tv_deleteadd_ok = (TextView) view.findViewById(R.id.tv_deleteadd_ok);

        tv_deleteadd_cancel.setOnClickListener(new MyOnClickListener(position));
        tv_deleteadd_ok.setOnClickListener(new MyOnClickListener(position));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
