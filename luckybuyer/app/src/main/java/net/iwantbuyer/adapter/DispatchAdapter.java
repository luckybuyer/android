package net.iwantbuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.bean.DispatchGameBean;
import net.iwantbuyer.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by admin on 2017/3/21.
 */
public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.DispatchHolder> {
    public DispatchOnClickListener dispatchOnClickListener;
    public EditText et_dispatch_phonenum;
    public EditText et_dispatch_operator;
    AlertDialog show;
    private Context context;
    private DispatchGameBean dispatchGameBean;

    public DispatchAdapter(Context context, DispatchGameBean dispatchGameBean) {
        this.context = context;
        this.dispatchGameBean = dispatchGameBean;
    }

    public void setDispatchOnClickListener(DispatchOnClickListener dispatchOnClickListener) {
        this.dispatchOnClickListener = dispatchOnClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if(position == 0) {
            type = 0;
        }else if(position == 1) {
            type = 1;
        }else if(position == 2) {
            type = 2;
        }else if(position == 3) {
            type = 3;
        }else if(position == 4) {
            type = 4;
        }else if(position == 5) {
            type = 5;
        }else if(position == 6) {
            type = 6;
        }else {
            type = 7;
        }
        return type;
    }

    @Override
    public DispatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 0) {
            view = View.inflate(context, R.layout.item_dispatch_discribe,null);
        }else {
            view = View.inflate(context, R.layout.item_dispatch,null);
        }
        DispatchHolder viewHolder = new DispatchHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DispatchHolder holder, int position) {
        Log.e("TAG_sssss", dispatchGameBean.getDelivery().getStatus() + "");
        if(getItemViewType(position) ==1) {
            View processing = LayoutInflater.from(context).inflate(R.layout.item_dispatch_pending,null);
            ((TextView)processing.findViewById(R.id.tv_dispatch_time)).setText(dispatchGameBean.getDelivery().getCreated_at().substring(0, 20).replace("T", "\t ") + "");
            holder.view_dispatch_top.setVisibility(View.GONE);
            holder.view_delivered.setEnabled(false);
            holder.iv_disaptch_delivered.setEnabled(false);
            holder.rl_disaptch.addView(processing);
        }else if(getItemViewType(position)==2) {
            setAddress(holder);
            holder.view_dispatch_top.setEnabled(false);
            if("pending".equals(dispatchGameBean.getDelivery().getStatus())) {
                holder.iv_disaptch_delivered.setEnabled(true);
                holder.view_delivered.setEnabled(true);
            }else {
                holder.iv_disaptch_delivered.setEnabled(false);
                holder.view_delivered.setEnabled(false);
                holder.view_dispatch_top.setEnabled(false);
            }
        }else if(getItemViewType(position)==3) {
            View processing = LayoutInflater.from(context).inflate(R.layout.item_dispatch_processing,null);
            if("processing".equals(dispatchGameBean.getDelivery().getStatus())) {
                ((TextView)processing.findViewById(R.id.tv_dispatch_processing)).setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
                ((TextView)processing.findViewById(R.id.tv_dispach_processing_warn)).setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
                holder.iv_disaptch_delivered.setEnabled(true);
                holder.view_delivered.setEnabled(true);
            }else {
                String status = dispatchGameBean.getDelivery().getStatus();
                if("pending".equals(status)) {
                    ((TextView)processing.findViewById(R.id.tv_dispatch_processing)).setTextColor(ContextCompat.getColor(context,R.color.text_gray));
                    ((TextView)processing.findViewById(R.id.tv_dispach_processing_warn)).setTextColor(ContextCompat.getColor(context,R.color.text_gray));
                }else {
                    ((TextView)processing.findViewById(R.id.tv_dispatch_processing)).setTextColor(ContextCompat.getColor(context,R.color.text_black));
                    ((TextView)processing.findViewById(R.id.tv_dispach_processing_warn)).setTextColor(ContextCompat.getColor(context,R.color.text_black));
                }
                holder.iv_disaptch_delivered.setEnabled(false);
                holder.view_delivered.setEnabled(false);
                if("pending".equals(dispatchGameBean.getDelivery().getStatus())) {
                    holder.view_dispatch_top.setEnabled(true);
                }else {
                    holder.view_dispatch_top.setEnabled(false);
                }
            }
            holder.rl_disaptch.addView(processing);
        }else if(getItemViewType(position)==4) {
            View shipping = LayoutInflater.from(context).inflate(R.layout.item_dispatch_shipping,null);
            if("shipping".equals(dispatchGameBean.getDelivery().getStatus())) {
                ((TextView)shipping.findViewById(R.id.tv_dispatch_delivered)).setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
                shipping.findViewById(R.id.tv_dispatch_confirm).setVisibility(View.VISIBLE);
                shipping.findViewById(R.id.tv_dispatch_confirm).setOnClickListener(new MyOnClickListener());
                holder.iv_disaptch_delivered.setEnabled(true);
                holder.view_delivered.setEnabled(true);
            }else {
                String status = dispatchGameBean.getDelivery().getStatus();
                if("pending".equals(status) || "processing".equals(status)) {
                    ((TextView)shipping.findViewById(R.id.tv_dispatch_delivered)).setTextColor(ContextCompat.getColor(context,R.color.text_gray));
                }else {
                    ((TextView)shipping.findViewById(R.id.tv_dispatch_delivered)).setTextColor(ContextCompat.getColor(context,R.color.text_black));
                }

                holder.iv_disaptch_delivered.setEnabled(false);
                holder.view_delivered.setEnabled(false);
                if("processing".equals(dispatchGameBean.getDelivery().getStatus())) {
                    holder.view_dispatch_top.setEnabled(true);
                }else {
                    holder.view_dispatch_top.setEnabled(false);
                }
            }
            holder.rl_disaptch.addView(shipping);
        }else if(getItemViewType(position)==5) {
            View finished = LayoutInflater.from(context).inflate(R.layout.item_dispatch_finished,null);
            TextView tv_dispatch_5coins = (TextView) finished.findViewById(R.id.tv_dispatch_5coins);
            String str = Utils.getSpData("gifts_post_share", context);
            int shared_coins = Integer.parseInt(str);
            if (shared_coins == 0) {
                tv_dispatch_5coins.setVisibility(View.GONE);
            } else {
                tv_dispatch_5coins.setText(context.getString(R.string.showitstart) + shared_coins + context.getString(R.string.showitlast));
            }
            if("finished".equals(dispatchGameBean.getDelivery().getStatus())) {
                ((TextView)finished.findViewById(R.id.tv_dispatch_show)).setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
                ((TextView)finished.findViewById(R.id.tv_dispatch_5coins)).setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
                finished.findViewById(R.id.tv_dispatch_isshow).setVisibility(View.VISIBLE);
                finished.findViewById(R.id.tv_dispatch_isshow).setOnClickListener(new MyOnClickListener());
                holder.iv_disaptch_delivered.setEnabled(true);
                holder.view_delivered.setEnabled(true);
            }else {
                String status = dispatchGameBean.getDelivery().getStatus();
                if("pending".equals(status) || "processing".equals(status)|| "shipping".equals(status)) {
                    ((TextView)finished.findViewById(R.id.tv_dispatch_show)).setTextColor(ContextCompat.getColor(context,R.color.text_gray));
                    ((TextView)finished.findViewById(R.id.tv_dispatch_5coins)).setTextColor(ContextCompat.getColor(context,R.color.text_gray));
                }else {
                    ((TextView)finished.findViewById(R.id.tv_dispatch_show)).setTextColor(ContextCompat.getColor(context,R.color.text_black));
                    ((TextView)finished.findViewById(R.id.tv_dispatch_5coins)).setTextColor(ContextCompat.getColor(context,R.color.text_black));
                }

                holder.iv_disaptch_delivered.setEnabled(false);
                holder.view_delivered.setEnabled(false);
                if("shipping".equals(dispatchGameBean.getDelivery().getStatus())) {
                    holder.view_dispatch_top.setEnabled(true);
                }else {
                    holder.view_dispatch_top.setEnabled(false);
                }
            }
            holder.rl_disaptch.addView(finished);

        }else if(getItemViewType(position)==6) {
            View shared = LayoutInflater.from(context).inflate(R.layout.item_dispatch_shared,null);
            holder.view_delivered.setBackgroundColor(ContextCompat.getColor(context,R.color.bg_while));
            if("shared".equals(dispatchGameBean.getDelivery().getStatus())) {
                ((TextView)shared.findViewById(R.id.tv_dispatch_fcshare)).setTextColor(ContextCompat.getColor(context,R.color.ff9c05));
                shared.findViewById(R.id.tv_dispatch_isfcshare).setVisibility(View.VISIBLE);
                shared.findViewById(R.id.tv_dispatch_isfcshare).setOnClickListener(new MyOnClickListener());
                holder.iv_disaptch_delivered.setEnabled(true);
                holder.view_delivered.setEnabled(true);
            }else {
                String status = dispatchGameBean.getDelivery().getStatus();
                if("pending".equals(status) || "processing".equals(status)|| "shipping".equals(status)|| "finished".equals(status)) {
                    ((TextView)shared.findViewById(R.id.tv_dispatch_fcshare)).setTextColor(ContextCompat.getColor(context,R.color.text_gray));
                }else {
                    ((TextView)shared.findViewById(R.id.tv_dispatch_fcshare)).setTextColor(ContextCompat.getColor(context,R.color.text_black));
                }

                holder.iv_disaptch_delivered.setEnabled(false);
                if("finished".equals(dispatchGameBean.getDelivery().getStatus())) {
                    holder.view_dispatch_top.setEnabled(true);
                }else {
                    holder.view_dispatch_top.setEnabled(false);
                }
            }
            holder.view_dispatch_top.setEnabled(false);
            holder.rl_disaptch.addView(shared);
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    private void setAddress(DispatchHolder holder){
        //当 物品为  虚拟物品的时候
        if ("virtual_mobile".equals(dispatchGameBean.getDelivery().getType())) {
            View card = View.inflate(context,R.layout.pager_dispatch_address_card,null);
            et_dispatch_phonenum = (EditText) card.findViewById(R.id.et_dispatch_phonenum);
            et_dispatch_operator = (EditText) card.findViewById(R.id.et_dispatch_operator);
            TextView tv_dispatch_card_submit = (TextView) card.findViewById(R.id.tv_dispatch_card_submit);
            holder.rl_disaptch.addView(card);
            tv_dispatch_card_submit.setOnClickListener(new MyOnClickListener());
            if (!"pending".equals(dispatchGameBean.getDelivery().getStatus()
            )) {
                card.findViewById(R.id.tv_dispatch_card_submit).setVisibility(View.GONE);
                if (dispatchGameBean.getDelivery().getAddress() != null) {
                    et_dispatch_phonenum.setEnabled(false);
                    et_dispatch_phonenum.setText(dispatchGameBean.getDelivery().getAddress().getPhone() + "");
                    et_dispatch_operator.setEnabled(false);
                    et_dispatch_operator.setText(dispatchGameBean.getDelivery().getAddress().getVendor() + "");
                }
            }
        }

        if ("post".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() == null && ((SecondPagerActivity) context).shippingBean == null) {

            View addaddress = View.inflate(context, R.layout.pager_dispatch_address, null);
            RelativeLayout rl_dispatch_choose = (RelativeLayout) addaddress.findViewById(R.id.rl_dispatch_choose);
            holder.rl_disaptch.addView(addaddress);
            rl_dispatch_choose.setOnClickListener(new MyOnClickListener());
        }  else if ("post".equals(dispatchGameBean.getDelivery().getType()) && ((SecondPagerActivity) context).shippingBean != null) {
            View curaddress = View.inflate(context, R.layout.pager_dispatch_address_default, null);
            ((TextView)curaddress.findViewById(R.id.tv_disaptch_name)).setText(((SecondPagerActivity) context).shippingBean.getName() + "");
            ((TextView)curaddress.findViewById(R.id.tv_disaptch_telnum)).setText(((SecondPagerActivity) context).shippingBean.getPhone() + "");
            ((TextView)curaddress.findViewById(R.id.tv_disaptch_add_detailed)).setText(((SecondPagerActivity) context).shippingBean.getAddress() + "");

            if (!"pending".equals(dispatchGameBean.getDelivery().getStatus())) {
                curaddress.findViewById(R.id.rl_dispatch_submit).setVisibility(View.GONE);
            } else {
                curaddress.findViewById(R.id.rl_dispatch_submit).setVisibility(View.VISIBLE);
            }
            holder.rl_disaptch.addView(curaddress);
            curaddress.findViewById(R.id.rl_disaptch_address).setOnClickListener(new MyOnClickListener());
            curaddress.findViewById(R.id.tv_dispatch_submit).setOnClickListener(new MyOnClickListener());
        }else if ("post".equals(dispatchGameBean.getDelivery().getType()) && dispatchGameBean.getDelivery().getAddress() != null) {
            View curaddress = View.inflate(context, R.layout.pager_dispatch_address_default, null);
            ((TextView)curaddress.findViewById(R.id.tv_disaptch_name)).setText(dispatchGameBean.getDelivery().getAddress().getName() + "");
            ((TextView)curaddress.findViewById(R.id.tv_disaptch_telnum)).setText(dispatchGameBean.getDelivery().getAddress().getPhone() + "");
            ((TextView)curaddress.findViewById(R.id.tv_disaptch_add_detailed)).setText(dispatchGameBean.getDelivery().getAddress().getAddress() + "");

            Log.e("TAG_ss", dispatchGameBean.getDelivery().getType() + "");
            if (!"pending".equals(dispatchGameBean.getDelivery().getStatus())) {
                curaddress.findViewById(R.id.rl_dispatch_submit).setVisibility(View.GONE);
            } else {
                curaddress.findViewById(R.id.rl_dispatch_submit).setVisibility(View.VISIBLE);
            }
            holder.rl_disaptch.addView(curaddress);
            curaddress.findViewById(R.id.rl_disaptch_address).setOnClickListener(new MyOnClickListener());
            curaddress.findViewById(R.id.tv_dispatch_submit).setOnClickListener(new MyOnClickListener());
        }

    }

    private void StartAlertDialog(View inflate) {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        TextView tv_address_cancel = (TextView) inflate.findViewById(R.id.tv_address_cancel);
        TextView tv_address_ok = (TextView) inflate.findViewById(R.id.tv_address_ok);

        tv_address_cancel.setOnClickListener(new MyOnClickListener());
        tv_address_ok.setOnClickListener(new MyOnClickListener());

        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface DispatchOnClickListener {
        public void onClick(String method);
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_dispatch_choose:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "dispatchpager";
                    break;
                case R.id.rl_disaptch_address:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "dispatchpager";
                    break;
                case R.id.tv_dispatch_submit:
                    View viewAddress = View.inflate(context, R.layout.alertdialog_current_address, null);
                    StartAlertDialog(viewAddress);
                    break;
                case R.id.tv_address_ok:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    dispatchOnClickListener.onClick("setCurrentAddress");
                    break;
                case R.id.tv_address_cancel:
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_dispatch_confirm:
                    dispatchOnClickListener.onClick("currentAddress");
                    break;
                case R.id.tv_dispatch_isshow:
                    //开始展示
                    ((SecondPagerActivity) context).switchPage(10);
                    ((SecondPagerActivity) context).order_id = dispatchGameBean.getId();
                    break;
                case R.id.tv_dispatch_card_submit:
                    if (et_dispatch_phonenum != null && et_dispatch_phonenum.getText().toString() == null) {
                        Utils.MyToast(context, context.getString(R.string.numbernullwarn));
                    } else if(et_dispatch_operator != null&& et_dispatch_operator.getText().toString() == null) {
                        Utils.MyToast(context, context.getString(R.string.operatornummwarn));
                    }else {
                        dispatchOnClickListener.onClick("setCurrentAddress");
                    }
                    break;
                case R.id.tv_dispatch_isfcshare:
                    dispatchOnClickListener.onClick("sharedFacebook");
                    break;
            }
        }
    }


    class DispatchHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl_disaptch;
        private ImageView iv_disaptch_delivered;
        private View view_delivered;
        private View view_dispatch_top;
        public DispatchHolder(View itemView) {
            super(itemView);
            rl_disaptch = (RelativeLayout) itemView.findViewById(R.id.rl_disaptch);
            iv_disaptch_delivered = (ImageView) itemView.findViewById(R.id.iv_disaptch_delivered);
            view_delivered = (View) itemView.findViewById(R.id.view_delivered);
            view_dispatch_top = (View) itemView.findViewById(R.id.view_dispatch_top);
        }
    }

}
