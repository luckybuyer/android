package net.smartbuyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import net.smartbuyer.R;
import net.smartbuyer.activity.MainActivity;
import net.smartbuyer.activity.SecondPagerActivity;
import net.smartbuyer.activity.ThirdPagerActivity;
import net.smartbuyer.app.MyApplication;
import net.smartbuyer.bean.AllOrderBean;
import net.smartbuyer.utils.HttpUtils;
import net.smartbuyer.utils.Utils;
import net.smartbuyer.view.JustifyTextView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by admin on 2016/9/21.
 */
public class MePagerAllAdapter extends RecyclerView.Adapter<MePagerAllAdapter.ViewHolder> {
    private Context context;
    public List<AllOrderBean.AllorderBean> list;

    public MePagerAllAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        String status = list.get(position).getGame().getStatus();
        if ("running".equals(status)) {
            return 0;
        } else if ("closed".equals(status)) {
            return 1;
        } else if ("finished".equals(status)) {
            String id = list.get(position).getGame().getLucky_user().getId() + "";
            if (list.get(position).getDelivery() != null) {
                return 3;
            }
            return 2;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if (viewType == 0) {
            View inflate = View.inflate(context, R.layout.item_me_all, null);
            viewHolder = new ViewHolder(inflate, viewType);
        } else if (viewType == 1) {
            View inflate = View.inflate(context, R.layout.item_me_all_countdown, null);
            viewHolder = new ViewHolder(inflate, viewType);
        } else if (viewType == 2) {
            View inflate = View.inflate(context, R.layout.item_me_all_lucky, null);
            viewHolder = new ViewHolder(inflate, viewType);
        } else if (viewType == 3) {
            View inflate = View.inflate(context, R.layout.item_me_all_lucky, null);
            viewHolder = new ViewHolder(inflate, viewType);
        }

        return viewHolder;
    }

    int type;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        type = getItemViewType(position);
        if (type == 0) {
            String picture = "https:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_all_icon.setImageBitmap(resource);
                }
            });
            holder.jtv_all_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_all_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_all_participation.setText("My participation:" + list.get(position).getShares());
            holder.tv_all_shares.setText("Total: " + list.get(position).getGame().getShares());
            holder.tv_all_leftshares.setText("Remain: " + list.get(position).getGame().getLeft_shares());
            holder.pb_all_progress.setMax(list.get(position).getGame().getShares());
            holder.pb_all_progress.setProgress(list.get(position).getGame().getShares() - list.get(position).getGame().getLeft_shares());
            holder.rl_all_continue.setOnClickListener(new MyOnClickListener(position));
            holder.iv_all_goview.setOnClickListener(new MyOnClickListener(position));
            holder.tv_all_goview.setOnClickListener(new MyOnClickListener(position));
            holder.rl_top.setOnClickListener(new MyOnClickListener(position));                                       //后加的 点击上半部分 进入本期

        } else if (type == 1) {
            String picture = "https:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_countdown_icon.setImageBitmap(resource);
                }
            });
            holder.jtv_countdown_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_countdown_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_countdown_participation.setText("My participation:" + list.get(position).getShares());
            String finishTime = list.get(position).getGame().getFinished_at();
            long time = Utils.Iso8601ToLong(finishTime);
            new CountDownTimer(time, 10) {
                @Override
                public void onTick(long l) {
                    SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
                    formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                    String time = formatter.format(l);

                    holder.tv_countdown_1.setText(time.substring(0, 1));
                    holder.tv_countdown_2.setText(time.substring(1, 2));
                    holder.tv_countdown_3.setText(time.substring(3, 4));
                    holder.tv_countdown_4.setText(time.substring(4, 5));
                    holder.tv_countdown_5.setText(time.substring(6, 7));
                    holder.tv_countdown_6.setText(time.substring(7, 8));
                }

                @Override
                public void onFinish() {
                    holder.tv_countdown_6.setText("0");
                    String token = Utils.getSpData("token", context);
                    String url = MyApplication.url + "/v1/game-orders/?per_page=20&page=1&timezone=" + MyApplication.utc;
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
                                                    Gson gson = new Gson();
                                                    String str = "{\"allorder\":" + response + "}";
                                                    AllOrderBean allOrderBean = gson.fromJson(str, AllOrderBean.class);
                                                    list = allOrderBean.getAllorder();

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

            holder.rl_countdown_continue.setOnClickListener(new MyOnClickListener(position));
            holder.iv_countdown_goview.setOnClickListener(new MyOnClickListener(position));
            holder.tv_countdown_goview.setOnClickListener(new MyOnClickListener(position));
            holder.rl_top.setOnClickListener(new MyOnClickListener(position));                                       //后加的 点击上半部分 进入本期
        } else if (type == 2) {
            String picture = "https:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_lucky_icon.setImageBitmap(resource);
                }
            });
            holder.jtv_lucky_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_lucky_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_lucky_participation.setText("My participation:" + list.get(position).getShares());
            holder.tv_lucky_name.setText(list.get(position).getGame().getLucky_user().getProfile().getName());

            holder.rl_lucky_continue.setOnClickListener(new MyOnClickListener(position));
            holder.iv_lucky_goview.setOnClickListener(new MyOnClickListener(position));
            holder.tv_lucky_goview.setOnClickListener(new MyOnClickListener(position));
            holder.rl_top.setOnClickListener(new MyOnClickListener(position));                                       //后加的 点击上半部分 进入本期
        } else if (type == 3) {
            String picture = "https:" + list.get(position).getGame().getProduct().getTitle_image();
            Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.iv_lucky_icon.setImageBitmap(resource);
                }
            });

            String status = list.get(position).getDelivery().getStatus();
            if ("pending".equals(status)) {
                holder.tv_lucky_go.setText("Confirm shipping address");
            } else if ("processing".equals(status)) {
                holder.tv_lucky_go.setText("Waiting for shippment");
            } else if ("shipping".equals(status)) {
                holder.tv_lucky_go.setText("Shipped, out for delivery");
            } else if ("finished".equals(status)) {
                holder.tv_lucky_go.setText("Delivered and show to win awards");
            }
            holder.jtv_lucky_discribe.setText(list.get(position).getGame().getProduct().getTitle());
            holder.tv_lucky_issue.setText("Issue:" + list.get(position).getGame().getIssue_id());
            holder.tv_lucky_participation.setText("My participation:" + list.get(position).getShares());
            holder.tv_lucky_name.setText(list.get(position).getGame().getLucky_user().getProfile().getName());

            holder.rl_lucky_address.setOnClickListener(new MyOnClickListener(position));
            holder.iv_lucky_goview.setOnClickListener(new MyOnClickListener(position));
            holder.tv_lucky_goview.setOnClickListener(new MyOnClickListener(position));
            holder.rl_top.setOnClickListener(new MyOnClickListener(position));                                       //后加的 点击上半部分 进入本期
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //设置监听
    class MyOnClickListener implements View.OnClickListener {
        int position = -1;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_top:
                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("batch_id", list.get(position).getGame().getBatch_id());
                    context.startActivity(intent);
                    break;
                case R.id.rl_all_continue:                    //还在进行中
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("batch_id", list.get(position).getGame().getBatch_id());
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.iv_all_goview:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.tv_all_goview:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.rl_lucky_continue:                   //别人中奖了
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("batch_id", list.get(position).getGame().getBatch_id());
                    context.startActivity(intent);
                    break;
                case R.id.iv_countdown_goview:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.tv_countdown_goview:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.rl_countdown_continue:
                    intent = new Intent(context, SecondPagerActivity.class);   //倒计时
                    intent.putExtra("from", "productdetail");
                    intent.putExtra("batch_id", list.get(position).getGame().getBatch_id());
                    context.startActivity(intent);
                    break;
                case R.id.iv_lucky_goview:

                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.tv_lucky_goview:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", list.get(position).getGame().getId());
                    intent.putExtra("title_image", list.get(position).getGame().getProduct().getTitle_image());
                    intent.putExtra("title", list.get(position).getGame().getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((MainActivity) context).startActivity(intent);
                    break;
                case R.id.rl_lucky_address:                     //我中奖了点击地址
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "dispatchpager");
                    intent.putExtra("dispatch_game_id", list.get(position).getGame().getLucky_order().getId());
                    ((MainActivity) context).startActivity(intent);
                    break;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //all
        private ImageView iv_all_icon;
        private JustifyTextView jtv_all_discribe;
        private TextView tv_all_issue;
        private TextView tv_all_participation;
        private TextView tv_all_goview;
        private ImageView iv_all_goview;
        private TextView tv_all_shares;
        private TextView tv_all_leftshares;
        private ProgressBar pb_all_progress;
        private RelativeLayout rl_all_continue;
        private RelativeLayout rl_top;
        //countdown
        private ImageView iv_countdown_icon;
        private JustifyTextView jtv_countdown_discribe;
        private TextView tv_countdown_issue;
        private TextView tv_countdown_participation;
        private TextView tv_countdown_goview;
        private ImageView iv_countdown_goview;
        private RelativeLayout rl_countdown_continue;
        private TextView tv_countdown_1;
        private TextView tv_countdown_2;
        private TextView tv_countdown_3;
        private TextView tv_countdown_4;
        private TextView tv_countdown_5;
        private TextView tv_countdown_6;

        //luckey
        private ImageView iv_lucky_icon;
        private JustifyTextView jtv_lucky_discribe;
        private TextView tv_lucky_issue;
        private TextView tv_lucky_participation;
        private TextView tv_lucky_goview;
        private ImageView iv_lucky_goview;
        private TextView tv_lucky_name;
        private RelativeLayout rl_lucky_address;
        private RelativeLayout rl_lucky_continue;
        private TextView tv_lucky_go;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type == 0) {
                iv_all_icon = (ImageView) itemView.findViewById(R.id.iv_all_icon);
                jtv_all_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_all_discribe);
                tv_all_issue = (TextView) itemView.findViewById(R.id.tv_all_issue);
                tv_all_participation = (TextView) itemView.findViewById(R.id.tv_all_participation);
                tv_all_goview = (TextView) itemView.findViewById(R.id.tv_all_goview);
                iv_all_goview = (ImageView) itemView.findViewById(R.id.iv_all_goview);
                tv_all_shares = (TextView) itemView.findViewById(R.id.tv_all_shares);
                tv_all_leftshares = (TextView) itemView.findViewById(R.id.tv_all_leftshares);
                pb_all_progress = (ProgressBar) itemView.findViewById(R.id.pb_all_progress);
                rl_all_continue = (RelativeLayout) itemView.findViewById(R.id.rl_all_continue);
                rl_top = (RelativeLayout) itemView.findViewById(R.id.rl_top);
            } else if (type == 1) {
                iv_countdown_icon = (ImageView) itemView.findViewById(R.id.iv_countdown_icon);
                jtv_countdown_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_countdown_discribe);
                tv_countdown_issue = (TextView) itemView.findViewById(R.id.tv_countdown_issue);
                tv_countdown_participation = (TextView) itemView.findViewById(R.id.tv_countdown_participation);
                tv_countdown_goview = (TextView) itemView.findViewById(R.id.tv_countdown_goview);
                iv_countdown_goview = (ImageView) itemView.findViewById(R.id.iv_countdown_goview);
                rl_countdown_continue = (RelativeLayout) itemView.findViewById(R.id.rl_countdown_continue);
                rl_top = (RelativeLayout) itemView.findViewById(R.id.rl_top);
                tv_countdown_1 = (TextView) itemView.findViewById(R.id.tv_countdown_1);
                tv_countdown_2 = (TextView) itemView.findViewById(R.id.tv_countdown_2);
                tv_countdown_3 = (TextView) itemView.findViewById(R.id.tv_countdown_3);
                tv_countdown_4 = (TextView) itemView.findViewById(R.id.tv_countdown_4);
                tv_countdown_5 = (TextView) itemView.findViewById(R.id.tv_countdown_5);
                tv_countdown_6 = (TextView) itemView.findViewById(R.id.tv_countdown_6);
            } else if (type == 2) {
                iv_lucky_icon = (ImageView) itemView.findViewById(R.id.iv_lucky_icon);
                jtv_lucky_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_lucky_discribe);
                tv_lucky_issue = (TextView) itemView.findViewById(R.id.tv_lucky_issue);
                tv_lucky_participation = (TextView) itemView.findViewById(R.id.tv_lucky_participation);
                tv_lucky_goview = (TextView) itemView.findViewById(R.id.tv_lucky_goview);
                iv_lucky_goview = (ImageView) itemView.findViewById(R.id.iv_lucky_goview);
                tv_lucky_name = (TextView) itemView.findViewById(R.id.tv_lucky_name);
                rl_lucky_address = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_address);
                rl_lucky_continue = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_continue);
                rl_top = (RelativeLayout) itemView.findViewById(R.id.rl_top);
                rl_lucky_continue.setVisibility(View.VISIBLE);
                rl_lucky_address.setVisibility(View.GONE);
            } else if (type == 3) {
                iv_lucky_icon = (ImageView) itemView.findViewById(R.id.iv_lucky_icon);
                jtv_lucky_discribe = (JustifyTextView) itemView.findViewById(R.id.jtv_lucky_discribe);
                tv_lucky_issue = (TextView) itemView.findViewById(R.id.tv_lucky_issue);
                tv_lucky_participation = (TextView) itemView.findViewById(R.id.tv_lucky_participation);
                tv_lucky_goview = (TextView) itemView.findViewById(R.id.tv_lucky_goview);
                iv_lucky_goview = (ImageView) itemView.findViewById(R.id.iv_lucky_goview);
                tv_lucky_name = (TextView) itemView.findViewById(R.id.tv_lucky_name);
                rl_lucky_address = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_address);
                rl_lucky_continue = (RelativeLayout) itemView.findViewById(R.id.rl_lucky_continue);
                tv_lucky_go = (TextView) itemView.findViewById(R.id.tv_lucky_go);
                rl_top = (RelativeLayout) itemView.findViewById(R.id.rl_top);
                rl_lucky_continue.setVisibility(View.GONE);
                rl_lucky_address.setVisibility(View.VISIBLE);
            }
        }
    }
}
