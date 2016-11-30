package net.iwantbuyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.bean.PreviousWinnerBean;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.JustifyTextView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by admin on 2016/10/13.
 */
public class WinningAdapter extends RecyclerView.Adapter<WinningAdapter.ViewHolder> {
    public List<PreviousWinnerBean.PreviousBean> list;
    private Context context;
    public WinningAdapter(Context context, List<PreviousWinnerBean.PreviousBean> list) {
        this.list = list;
        this.context = context;
        for (int i = 0;i < list.size();i++){
            if("running".equals(list.get(i).getStatus())) {
                list.remove(i);
                i--;
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        String status = list.get(position).getStatus();
        if("closed".equals(status)) {
            return 0;
        }else if("finished".equals(status)) {
            return 1;
        }
        return super.getItemViewType(position);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if(viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_winning_closed, null);
            viewHolder = new ViewHolder(view,viewType);
        }else if(viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_winning_finished, null);
            viewHolder = new ViewHolder(view,viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.jtv_winning_title.setText(list.get(position).getProduct().getTitle());
        holder.item_winning_issue.setText("" + list.get(position).getIssue_id());
        Glide.with(context).load("http:" + list.get(position).getProduct().getTitle_image()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.iv_winning_icon.setImageBitmap(resource);
            }
        });
        int type = getItemViewType(position);
        if(type == 0) {
            String finishTime = list.get(position).getFinished_at();
            long time = Utils.Iso8601ToLong(finishTime);
            new CountDownTimer(time, 10) {
                @Override
                public void onTick(long l) {
                    SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
                    formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                    String time = formatter.format(l);

                    holder.tv_winning_countdown1.setText(time.substring(0,1));
                    holder.tv_winning_countdown2.setText(time.substring(1, 2));
                    holder.tv_winning_countdown3.setText(time.substring(3, 4));
                    holder.tv_winning_countdown4.setText(time.substring(4, 5));
                    holder.tv_winning_countdown5.setText(time.substring(6, 7));
                    holder.tv_winning_countdown6.setText(time.substring(7, 8));
                }

                @Override
                public void onFinish() {
                    holder.tv_winning_countdown6.setText("0");
                    String token = Utils.getSpData("token", context);
                    String url = MyApplication.url + "/v1/games/?per_page=20&page=1&timezone=" + MyApplication.utc;
                    Map map = new HashMap<String, String>();
                    map.put("Authorization", "Bearer " + token);
                    //请求登陆接口
                    final String finalToken = token;
                    HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                                @Override
                                public void success(final String response) {

                                    ((Activity) context).runOnUiThread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    String str = "{\"previous\":" + response + "}";
                                                    Gson gson = new Gson();
                                                    PreviousWinnerBean previousWinnerBean = gson.fromJson(str, PreviousWinnerBean.class);
                                                    list = previousWinnerBean.getPrevious();
                                                    for (int i = 0;i < list.size();i++){
                                                        if("running".equals(list.get(i).getStatus())) {
                                                            list.remove(i);
                                                            i--;
                                                        }
                                                    }
                                                    notifyDataSetChanged();

                                                }
                                            }
                                    );
                                }

                                @Override
                                public void error(int requestCode, String message) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    });
                                }

                                @Override
                                public void failure(Exception exception) {
                                }
                            }

                    );

                }
            }.start();
        }else if(type == 1) {
            holder.tv_winning_luckynum.setText(list.get(position).getLucky_number());
            holder.tv_winning_winningname.setText(list.get(position).getLucky_user().getProfile().getName() + "");
            holder.tv_winning_participations.setText(list.get(position).getLucky_order().getTotal_shares() + "");
            holder.tv_winning_announced.setText(list.get(position).getFinished_at().substring(0,19).replace("T"," ") + "");
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SecondPagerActivity.class);
                intent.putExtra("from","productdetail");
                intent.putExtra("game_id",list.get(position).getId());
                intent.putExtra("batch_id",list.get(position).getBatch_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //公共的
        View view;
        ImageView iv_winning_icon;
        JustifyTextView jtv_winning_title;
        TextView item_winning_issue;

        //倒计时
        TextView tv_winning_countdown1;
        TextView tv_winning_countdown2;
        TextView tv_winning_countdown3;
        TextView tv_winning_countdown4;
        TextView tv_winning_countdown5;
        TextView tv_winning_countdown6;

        //已经完事的
        TextView tv_winning_luckynum;
        TextView tv_winning_winningname;
        TextView tv_winning_participations;
        TextView tv_winning_announced;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 0) {
                view = itemView;
                iv_winning_icon = (ImageView) itemView.findViewById(R.id.iv_winning_icon);
                jtv_winning_title = (JustifyTextView) itemView.findViewById(R.id.jtv_winning_title);
                item_winning_issue = (TextView) itemView.findViewById(R.id.item_winning_issue);
                tv_winning_countdown1 = (TextView) itemView.findViewById(R.id.tv_winning_countdown1);
                tv_winning_countdown2 = (TextView) itemView.findViewById(R.id.tv_winning_countdown2);
                tv_winning_countdown3 = (TextView) itemView.findViewById(R.id.tv_winning_countdown3);
                tv_winning_countdown4 = (TextView) itemView.findViewById(R.id.tv_winning_countdown4);
                tv_winning_countdown5 = (TextView) itemView.findViewById(R.id.tv_winning_countdown5);
                tv_winning_countdown6 = (TextView) itemView.findViewById(R.id.tv_winning_countdown6);
            }else if(viewType == 1) {
                view = itemView;
                iv_winning_icon = (ImageView) itemView.findViewById(R.id.iv_winning_icon);
                jtv_winning_title = (JustifyTextView) itemView.findViewById(R.id.jtv_winning_title);
                item_winning_issue = (TextView) itemView.findViewById(R.id.item_winning_issue);
                tv_winning_luckynum = (TextView) itemView.findViewById(R.id.tv_winning_luckynum);
                tv_winning_winningname = (TextView) itemView.findViewById(R.id.tv_winning_winningname);
                tv_winning_participations = (TextView) itemView.findViewById(R.id.tv_winning_participations);
                tv_winning_announced = (TextView) itemView.findViewById(R.id.tv_winning_announced);

            }
        }
    }
}
