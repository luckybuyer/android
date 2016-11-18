package net.smartbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import net.smartbuyer.R;
import net.smartbuyer.activity.SecondPagerActivity;
import net.smartbuyer.activity.ThirdPagerActivity;
import net.smartbuyer.adapter.ProductDetailAdapter;
import net.smartbuyer.app.MyApplication;
import net.smartbuyer.base.BaseNoTrackPager;
import net.smartbuyer.bean.BroadcastBean;
import net.smartbuyer.bean.BuyStateBean;
import net.smartbuyer.bean.MyBuyBean;
import net.smartbuyer.bean.ProductOrderBean;
import net.smartbuyer.bean.ProductDetailBean;
import net.smartbuyer.utils.DensityUtil;
import net.smartbuyer.utils.HttpUtils;
import net.smartbuyer.utils.Utils;
import net.smartbuyer.view.AutoTextView;
import net.smartbuyer.view.JustifyTextView;
import net.smartbuyer.view.MyScrollView;
import net.smartbuyer.view.RoundCornerImageView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by admin on 2016/9/17.
 */
public class ProductDetailPager extends BaseNoTrackPager {

    public static final int WHAT_AUTO = 1;
    private RelativeLayout rl_productdetail_allview;
    private TextView tv_productdetail_producttitle;
    private TextView tv_productdetail_issue;           //当前轮
    private TextView tv_productdetail_totalicon;      //总数
    private TextView tv_productdetail_total;      //总数
    private TextView tv_productdetail_icon;           //剩余金币
    private ProgressBar pb_productdetail_progress;    //比率
    private RelativeLayout rl_productdetail_announced;           //往期揭晓
    private RecyclerView rv_productdetail;                       //参与记录
    private ImageView iv_productdetail_image;                    //顶部图片
    private RelativeLayout rl_productdetail_indsertcoins;
    private TextView tv_productdetail_inprogress;                 //无意义  描述  黄边
    private RelativeLayout rl_productdetail_mybuy;
    private TextView tv_productdetal_again;                   //buy it now
    private RelativeLayout rl_productdetail_participation_me;     //我中奖 view go
    private RelativeLayout rl_productdetail_participation_lucky;  //别人中奖view go
    private ImageView iv_productdetail_number;                    //最低购买份数 图片
    private AutoTextView atv_productdetail_broadcast;             //广播图
    private RelativeLayout rl_productdetail_back;                 //返回按钮
    private RelativeLayout rl_insert_warn;                        //相对布局警告
    private TextView tv_insert_warn;                              //textview警告
    private ImageView iv_insert_warn;                             //imageview警告
    private TextView tv_productdetail_discribe;                   //title 下面忽悠文件
    private ProgressBar pb_insert;
    private MyScrollView sv_productdetail;
    private View inflate;

    //倒計時  開獎
    private RelativeLayout rl_productdetail_countdown;
//    private RelativeLayout rl_productdetail_calculation;
    private TextView tv_productdetail_countdownissue;
    private LinearLayout ll_productdetail_buyit;

    private RelativeLayout rl_productdetail_lucky;


    //网络连接错误 与没有数据
    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;             //等待
    private TextView tv_net_again;

    //popupWindow中空间
    private RelativeLayout rl_insert_delete;
    private RelativeLayout rl_insert_add;
    private TextView tv_insert_two;
    private TextView tv_insert_five;
    private TextView tv_insert_ten;
    private TextView tv_insert_all;
    private TextView tv_insert_buy;
    private EditText et_insert_count;
    private TextView tv_productdetail_percentage;
    private TextView tv_productdetail_percent;
    private TextView tv_productdetail_remain;
    private RelativeLayout rl_productdetail_header;            //标题

    private List imageList = new ArrayList();
    private PopupWindow popupWindow;
    private View PPW;
    private SwipeRefreshLayout srl_productdetail_refresh;


    //产品  数据
    private ProductDetailBean productDetailBean;

    //当 请求initdata的时候   看是否需要开始progresssdialog
    boolean isNeedNetWaiting = true;
    int mLoopCount = 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_AUTO:
                    if (bcList != null) {
                        handler.removeCallbacksAndMessages(null);
                        int i = mLoopCount % bcList.size();
                        atv_productdetail_broadcast.next();
                        atv_productdetail_broadcast.setText(bcList.get(i));
                        mLoopCount++;
                        handler.sendEmptyMessageDelayed(WHAT_AUTO, 3000);
                    }
                    break;
            }
        }
    };

    @Override
    public View initView() {
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        inflate = View.inflate(context, R.layout.pager_productdetail, null);
        ((SecondPagerActivity) context).from = null;
        findView();
        srl_productdetail_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (newData == null) {
                    initData();
//                } else {
//                    processData(newData);
//                }
            }
        });

        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        additionRequest();
        if (((SecondPagerActivity) context).game_id == -1) {
            if (((SecondPagerActivity) context).batch_id != -1) {
                NewData();
                tv_productdetail_inprogress.setVisibility(View.VISIBLE);
                tv_productdetail_issue.setVisibility(View.VISIBLE);
                tv_productdetail_totalicon.setVisibility(View.VISIBLE);
                tv_productdetail_icon.setVisibility(View.VISIBLE);
                pb_productdetail_progress.setVisibility(View.VISIBLE);
                rl_productdetail_indsertcoins.setVisibility(View.VISIBLE);

                //隐藏一些控件
                rl_productdetail_countdown.setVisibility(View.GONE);
//                rl_productdetail_calculation.setVisibility(View.GONE);
                ll_productdetail_buyit.setVisibility(View.GONE);
                rl_productdetail_lucky.setVisibility(View.GONE);
                additionRequest();
            }
            return;
        }

        if (isNeedNetWaiting) {
            rl_keepout.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.GONE);
            rl_neterror.setVisibility(View.GONE);
            rl_loading.setVisibility(View.VISIBLE);
            isNeedNetWaiting = false;
        } else {
            srl_productdetail_refresh.setRefreshing(true);
        }


        String url = MyApplication.url + "/v1/games/" + ((SecondPagerActivity) context).game_id + "?timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_productdetail_allview.setVisibility(View.VISIBLE);
                        srl_productdetail_refresh.setRefreshing(false);

                        if (response.length() > 10) {
                            processData(response);
                            rl_keepout.setVisibility(View.GONE);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            rl_neterror.setVisibility(View.GONE);
                            rl_loading.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void error(int code, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        srl_productdetail_refresh.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        srl_productdetail_refresh.setRefreshing(false);
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void additionRequest() {
        //我购买的期次号码列表
        String MyBuyUrl = MyApplication.url + "/v1/game-orders/?game_id=" + ((SecondPagerActivity) context).game_id + "&per_page=20&page=1&timezone=" + MyApplication.utc;
        String token = Utils.getSpData("token", context);
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(MyBuyUrl, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        processMybuyData(response);
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {

                    }

                    @Override
                    public void failure(Exception exception) {

                    }
                }

        );


        String listUrl = MyApplication.url + "/v1/games/" + ((SecondPagerActivity) context).game_id + "/public-orders/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(listUrl, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processListData(response);
                    }
                });
            }

            @Override
            public void error(int code, String message) {

            }

            @Override
            public void failure(Exception exception) {

            }
        });

        //请求  广播列表
        String broadcastUrl = MyApplication.url + "/v1/broadcasts/?per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(broadcastUrl, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processbroadcastData(response);

                    }
                });
            }

            @Override
            public void error(int requestCode, String message) {

            }

            @Override
            public void failure(Exception exception) {

            }

        });
    }

    private List<SpannableStringBuilder> bcList;

    private void processbroadcastData(String response) {
        handler.removeCallbacksAndMessages(null);
        response = "{\"broad\":" + response + "}";
        Gson gson = new Gson();
        final BroadcastBean broadcastBean = gson.fromJson(response, BroadcastBean.class);
        bcList = new ArrayList();
        for (int i = 0; i < broadcastBean.getBroad().size(); i++) {
            final SpannableStringBuilder sb = new SpannableStringBuilder();

            final int finalI = i;
            if (!((SecondPagerActivity) context).isDestroyed()) {
                Glide.with(context).load(broadcastBean.getBroad().get(finalI).getData().getUser_icon()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int le = sb.length();
                        String expristion = "  b/12";
                        Drawable drawable = new BitmapDrawable(resource);
                        drawable.setBounds(0, 0, 40, 40);//设置图片大小
                        sb.append(expristion);
                        sb.setSpan(new ImageSpan(drawable), le, sb.length(), Spannable.SPAN_COMPOSING);

                        //设置文字
                        sb.append(" ");
                        sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)),//字体颜色
                                0, " ".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        int len = sb.length();

                        //皇冠图片
                        if (broadcastBean.getBroad().get(finalI).getContent().contains("Congratulations!")) {
                            Drawable d = getResources().getDrawable(R.drawable.me_lucky);
                            d.setBounds(0, 0, 36, 30);//设置图片大小
                            sb.append(expristion);
                            sb.setSpan(new ImageSpan(d), len, sb.length(), Spannable.SPAN_COMPOSING);

                        }
                        //设置文字
                        sb.append(" " + broadcastBean.getBroad().get(finalI).getContent());
                        sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)),//字体颜色
                                0, broadcastBean.getBroad().get(finalI).getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        bcList.add(sb);

                        if (finalI == 19) {
                            atv_productdetail_broadcast.setVisibility(View.VISIBLE);
                            atv_productdetail_broadcast.setText(bcList.get(0));
                            handler.sendEmptyMessageDelayed(WHAT_AUTO, 3000);
                        }

                    }
                });
            }
        }

    }


    //发现视图  设置监听
    private void findView() {
        rl_productdetail_allview = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_allview);
        tv_productdetail_producttitle = (TextView) inflate.findViewById(R.id.tv_productdetail_producttitle);
        tv_productdetail_issue = (TextView) inflate.findViewById(R.id.tv_productdetail_issue);
        tv_productdetail_totalicon = (TextView) inflate.findViewById(R.id.tv_productdetail_totalicon);
        tv_productdetail_icon = (TextView) inflate.findViewById(R.id.tv_productdetail_icon);
        pb_productdetail_progress = (ProgressBar) inflate.findViewById(R.id.pb_productdetail_progress);
        rl_productdetail_announced = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_announced);
        rv_productdetail = (RecyclerView) inflate.findViewById(R.id.rv_productdetail);
        iv_productdetail_image = (ImageView) inflate.findViewById(R.id.iv_productdetail_image);
        rl_productdetail_indsertcoins = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_indsertcoins);
        tv_productdetail_inprogress = (TextView) inflate.findViewById(R.id.tv_productdetail_inprogress);
        rl_productdetail_mybuy = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_mybuy);
        tv_productdetal_again = (TextView) inflate.findViewById(R.id.tv_productdetal_again);
        rl_productdetail_participation_me = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_participation_me);
        rl_productdetail_participation_lucky = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_participation_lucky);
        tv_productdetail_percentage = (TextView) inflate.findViewById(R.id.tv_productdetail_percentage);
        tv_productdetail_total = (TextView) inflate.findViewById(R.id.tv_productdetail_total);
        tv_productdetail_percent = (TextView) inflate.findViewById(R.id.tv_productdetail_percent);
        tv_productdetail_remain = (TextView) inflate.findViewById(R.id.tv_productdetail_remain);
        rl_productdetail_header = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_header);
        iv_productdetail_number = (ImageView) inflate.findViewById(R.id.iv_productdetail_number);
        atv_productdetail_broadcast = (AutoTextView) inflate.findViewById(R.id.atv_productdetail_broadcast);
        rl_productdetail_back = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_back);
        tv_productdetail_discribe = (TextView) inflate.findViewById(R.id.tv_productdetail_discribe);
        sv_productdetail = (MyScrollView) inflate.findViewById(R.id.sv_productdetail);


        srl_productdetail_refresh = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_productdetail_refresh);

        //倒计时  開獎
        rl_productdetail_countdown = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_countdown);
//        rl_productdetail_calculation = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_calculation);
        tv_productdetail_countdownissue = (TextView) inflate.findViewById(R.id.tv_productdetail_countdownissue);
        ll_productdetail_buyit = (LinearLayout) inflate.findViewById(R.id.ll_productdetail_buyit);
        rl_productdetail_lucky = (RelativeLayout) inflate.findViewById(R.id.rl_productdetail_lucky);


        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);

        rl_productdetail_indsertcoins.setOnClickListener(new MyOnClickListener());
        tv_productdetal_again.setOnClickListener(new MyOnClickListener());
        rl_productdetail_announced.setOnClickListener(new MyOnClickListener());
        rl_productdetail_participation_me.setOnClickListener(new MyOnClickListener());
        rl_productdetail_participation_lucky.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
        rl_productdetail_back.setOnClickListener(new MyOnClickListener());


        sv_productdetail.setOnScrollChanged(new MyScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                int y = (int) (t / 2);
                if(y <= 255) {
                    rl_productdetail_header.setBackgroundColor(Color.argb(y, 255, 255, 255));
                }else if(y > 255) {
                    rl_productdetail_header.setBackgroundColor(Color.argb(255, 255, 255, 255));
                }
            }
        });
    }

    //解析数据
    private void processData(String s) {
        Log.e("s", s + "");
        //刷新完成
        srl_productdetail_refresh.setRefreshing(false);
        Gson gson = new Gson();
        productDetailBean = gson.fromJson(s, ProductDetailBean.class);
        ((SecondPagerActivity) context).game_id = productDetailBean.getId();

        Log.e("TAG_状态", productDetailBean.getStatus());
        if ("running".equals(productDetailBean.getStatus())) {
            tv_productdetail_inprogress.setVisibility(View.VISIBLE);
            tv_productdetail_issue.setVisibility(View.VISIBLE);
            pb_productdetail_progress.setVisibility(View.VISIBLE);
            rl_productdetail_indsertcoins.setVisibility(View.VISIBLE);

            tv_productdetail_totalicon.setVisibility(View.VISIBLE);
            tv_productdetail_total.setVisibility(View.VISIBLE);
            tv_productdetail_icon.setVisibility(View.VISIBLE);
            tv_productdetail_percent.setVisibility(View.VISIBLE);
            tv_productdetail_percentage.setVisibility(View.VISIBLE);
            tv_productdetail_remain.setVisibility(View.VISIBLE);

            //隐藏一些控件
            rl_productdetail_countdown.setVisibility(View.GONE);
//            rl_productdetail_calculation.setVisibility(View.GONE);
            ll_productdetail_buyit.setVisibility(View.GONE);
            rl_productdetail_lucky.setVisibility(View.GONE);


        } else if ("closed".equals(productDetailBean.getStatus())) {
            //获取下一期 数据 判断是否有下一期 数据
            NewData();
            String finishTime = productDetailBean.getFinished_at();
            //如果  是正数  说明需要倒计时  如果为负数  说明已经开奖
            long time = Utils.Iso8601ToLong(finishTime);
            rl_productdetail_countdown.setVisibility(View.VISIBLE);
//            rl_productdetail_calculation.setVisibility(View.VISIBLE);
            tv_productdetail_countdownissue.setText("Issue:" + productDetailBean.getIssue_id());
            ((SecondPagerActivity) context).countDownTimer = new MyCountDownTimer(time, 10).start();

            ll_productdetail_buyit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            //原先控件隐藏
            rl_productdetail_indsertcoins.setVisibility(View.GONE);
            tv_productdetail_inprogress.setVisibility(View.GONE);
            tv_productdetail_issue.setVisibility(View.GONE);
            pb_productdetail_progress.setVisibility(View.GONE);
            tv_productdetail_totalicon.setVisibility(View.GONE);
            tv_productdetail_total.setVisibility(View.GONE);
            tv_productdetail_icon.setVisibility(View.GONE);
            tv_productdetail_percent.setVisibility(View.GONE);
            tv_productdetail_percentage.setVisibility(View.GONE);
            tv_productdetail_remain.setVisibility(View.GONE);
        } else if ("finished".equals(productDetailBean.getStatus())) {
            NewData();
            //显示一些 控件
            rl_productdetail_lucky.setVisibility(View.VISIBLE);
//            rl_productdetail_calculation.setVisibility(View.VISIBLE);
            tv_productdetail_inprogress.setVisibility(View.VISIBLE);
            tv_productdetail_issue.setVisibility(View.VISIBLE);

            if (isMyBuy) {
                tv_productdetail_inprogress.setText("Announced");
            } else {
                tv_productdetail_inprogress.setText("Announced");
            }
            //隐藏一些控件
            rl_productdetail_indsertcoins.setVisibility(View.GONE);
            rl_productdetail_countdown.setVisibility(View.GONE);
            pb_productdetail_progress.setVisibility(View.GONE);
            tv_productdetail_totalicon.setVisibility(View.GONE);
            tv_productdetail_total.setVisibility(View.GONE);
            tv_productdetail_icon.setVisibility(View.GONE);
            tv_productdetail_percent.setVisibility(View.GONE);
            tv_productdetail_percentage.setVisibility(View.GONE);
            tv_productdetail_remain.setVisibility(View.GONE);

            setLucky();
        }

        setView(s);
    }

    //设置视图
    private void setView(final String response) {
        srl_productdetail_refresh.setRefreshing(false);
        Gson gson = new Gson();
//        String game = "{\"productdetail\":" + s + "}";
        productDetailBean = gson.fromJson(response, ProductDetailBean.class);

        String imgUrl = "http:" + productDetailBean.getProduct().getDetail_image();

        boolean flag = ((SecondPagerActivity) context).isDestroyed();
        if (!flag) {
//            Glide.with(context).load(imgUrl).into(iv_productdetail_image);
            Glide.with(context).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv_productdetail_image.setImageBitmap(resource);
                }
            });
        }

        tv_productdetail_producttitle.setText(productDetailBean.getProduct().getTitle());
        tv_productdetail_discribe.setText(productDetailBean.getProduct().getDetail());

        tv_productdetail_issue.setText("Issue:" + productDetailBean.getIssue_id());

        tv_productdetail_totalicon.setText(productDetailBean.getShares() + "");

        tv_productdetail_icon.setText(productDetailBean.getLeft_shares() + "");
        int precentage = (int) (productDetailBean.getShares() - productDetailBean.getLeft_shares()) * 100 / productDetailBean.getShares();
        tv_productdetail_percentage.setText(precentage + "%");

        //后加的  产品描述

//        CharSequence charSequence=Html.fromHtml(productDetailBean.getProduct().getDetail());
//        tv_productdetail_discribe.setText(charSequence);
//        //该语句在设置后必加，不然没有任何效果  
//        tv_productdetail_discribe.setMovementMethod(LinkMovementMethod.getInstance());
        pb_productdetail_progress.setMax(productDetailBean.getShares());
        pb_productdetail_progress.setProgress(productDetailBean.getShares() - productDetailBean.getLeft_shares());
        if (productDetailBean.getShares_increment() == 5) {
            iv_productdetail_number.setBackgroundResource(R.drawable.homepager_5);
        } else if (productDetailBean.getShares_increment() == 10) {
            iv_productdetail_number.setBackgroundResource(R.drawable.homepager_10);
        }
    }

    boolean isMyBuy = false;

    //我自己购买的  号码
    private void processMybuyData(String response) {
        String st = "{\"mybuy\":" + response + "}";
        if (!st.contains("lucky_user")) {
            rl_productdetail_mybuy.setVisibility(View.GONE);
            return;
        }
        //盘算我是否参与
        isMyBuy = true;
        rl_productdetail_mybuy.setVisibility(View.VISIBLE);
        Gson gson = new Gson();
        MyBuyBean myBuyBean = gson.fromJson(st, MyBuyBean.class);

        if ("finished".equals(myBuyBean.getMybuy().get(0).getGame().getStatus())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = ((RoundCornerImageView) inflate.findViewById(R.id.civ_productdetail_lucky)).getWidth() + DensityUtil.px2dip(context, 34);
            rl_productdetail_mybuy.setLayoutParams(params);
        }
        String str = "Participation numbers:";
        int count = 0;
        for (int i =0;i < myBuyBean.getMybuy().size();i++){
            for (int j = 0;j < myBuyBean.getMybuy().get(i).getNumbers().size();j++){
                str += myBuyBean.getMybuy().get(i).getNumbers().get(j) + " ";
                count += 1;
            }
        }

        ((TextView) inflate.findViewById(R.id.tv_productdetail_mybuyparticipate)).setText("My participation:" + count);
        ((TextView) inflate.findViewById(R.id.tv_productdetail_mybuynum)).setText(str);

    }

    private void processListData(String response) {
        Gson gson = new Gson();
        String productorder = "{\"productorder\":" + response + "}";
        ProductOrderBean productOrderBean = gson.fromJson(productorder, ProductOrderBean.class);


        ProductDetailAdapter productDetailAdapter = new ProductDetailAdapter(context, productOrderBean.getProductorder());
        rv_productdetail.setAdapter(productDetailAdapter);

        //设置 recycleviewde manager   重写canScrollVertically方法是为了解决潜逃scrollview的卡顿问题
        rv_productdetail.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    private void setLucky() {
        ((TextView) inflate.findViewById(R.id.tv_productdetail_typesomething)).setText(productDetailBean.getLucky_user().getProfile().getName());
        ((TextView) inflate.findViewById(R.id.tv_productdetail_luckynum)).setText("Smart Number：" + productDetailBean.getLucky_number() + "");
        ((TextView) inflate.findViewById(R.id.tv_productdetail_luckyid)).setText("User ID:" + productDetailBean.getLucky_user().getId() + "");
        ((TextView) inflate.findViewById(R.id.tv_productdetail_luckytime)).setText("Lottery time:" + productDetailBean.getFinished_at().substring(0, 19).replace("T", "\t"));
        final RoundCornerImageView civ_productdetail_lucky = ((RoundCornerImageView) inflate.findViewById(R.id.civ_productdetail_lucky));
        boolean flag = ((SecondPagerActivity) context).isDestroyed();
        Log.e("TAG_胜利者图片", flag + "");
        Log.e("TAG_胜利者图片", productDetailBean.getLucky_user().getProfile().getPicture());
        if (!flag) {
            Glide.with((SecondPagerActivity) context).load(productDetailBean.getLucky_user().getProfile().getPicture()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    civ_productdetail_lucky.setImageBitmap(resource);
                }
            });
        }

    }

    //倒计时
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String time = formatter.format(l);

            ((TextView) inflate.findViewById(R.id.tv_countdown_1)).setText(time.substring(0, 1));
            ((TextView) inflate.findViewById(R.id.tv_countdown_2)).setText(time.substring(1, 2));
            ((TextView) inflate.findViewById(R.id.tv_countdown_3)).setText(time.substring(3, 4));
            ((TextView) inflate.findViewById(R.id.tv_countdown_4)).setText(time.substring(4, 5));
            ((TextView) inflate.findViewById(R.id.tv_countdown_5)).setText(time.substring(6, 7));
            ((TextView) inflate.findViewById(R.id.tv_countdown_6)).setText(time.substring(7, 8));
        }

        @Override
        public void onFinish() {
            ((TextView) inflate.findViewById(R.id.tv_countdown_6)).setText(0 + "");
            initData();
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            SecondPagerActivity activity = (SecondPagerActivity) context;
            switch (view.getId()) {
                case R.id.rl_productdetail_back:
                    ((SecondPagerActivity)context).finish();
                    break;
                case R.id.rl_productdetail_announced:          //往期回顾
                    Intent intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "previous");
                    intent.putExtra("batch_id", productDetailBean.getBatch_id());
                    startActivity(intent);
                    break;
                case R.id.tv_productdetal_again:      //buy  it  now
                    Log.e("TAG_gameid", ((SecondPagerActivity) context).game_id + "");
                    ((SecondPagerActivity)context).batch_id = productDetailBean.getBatch_id();
                    ((SecondPagerActivity)context).game_id = -1;
                    NewData();
//                    initData();
                    break;
                case R.id.tv_net_again:
                    isNeedNetWaiting = true;
                    initData();
                    break;
                case R.id.rl_productdetail_indsertcoins:
                    String token_s = Utils.getSpData("token_num", context);
                    int token = 0;
                    if (token_s != null) {
                        token = Integer.parseInt(token_s);
                    }

                    //判断是否登陆  未登陆  先登录  登陆 弹出popupwindow
                    if (token > System.currentTimeMillis() / 1000) {
                        View viewPPW = LayoutInflater.from(activity).inflate(R.layout.ppw_insert_coins, null);
                        int height = 0;
                        if(productDetailBean != null && productDetailBean.getShares_increment() < 5) {
                            height = 270;
                        }else {
                            height = 300;
                        }
                        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
                        popupWindow = Utils.startPPW((SecondPagerActivity) context, viewPPW, Utils.getScreenWidth(context), dip);

                        setPPW(viewPPW);
                    } else {
                        context.startActivity(((SecondPagerActivity) context).lock.newIntent(((SecondPagerActivity) context)));
                    }
                    break;
                case R.id.rl_productdetail_participation_lucky:
                    //别人中奖 view go

                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", productDetailBean.getId());
                    intent.putExtra("title_image", productDetailBean.getProduct().getTitle_image());
                    intent.putExtra("title", productDetailBean.getProduct().getTitle());
                    intent.putExtra("user_id", productDetailBean.getLucky_user().getId());
                    ((SecondPagerActivity) context).startActivity(intent);

                    break;
                case R.id.rl_productdetail_participation_me:
                    //我中奖 view go
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "participation");
                    intent.putExtra("game_id", productDetailBean.getId());
                    intent.putExtra("title_image", productDetailBean.getProduct().getTitle_image());
                    intent.putExtra("title", productDetailBean.getProduct().getTitle());
                    intent.putExtra("user_id", Integer.parseInt(Utils.getSpData("id", context)));
                    ((SecondPagerActivity) context).startActivity(intent);

                    break;
                case R.id.rl_insert_delete:
                    int count = Integer.parseInt(String.valueOf(et_insert_count.getText()));
                    if (count > productDetailBean.getShares_increment()) {
                        et_insert_count.setText(count - productDetailBean.getShares_increment() + "");
                    }
                    tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    break;
                case R.id.rl_insert_add:
                    count = Integer.parseInt(String.valueOf(et_insert_count.getText()));
                    if (count < productDetailBean.getLeft_shares()-productDetailBean.getShares_increment() ) {
                        et_insert_count.setText(count + productDetailBean.getShares_increment()  + "");
                        tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    }else{
                        et_insert_count.setText( productDetailBean.getLeft_shares()  + "");
                        tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    }
                    break;
                case R.id.tv_insert_two:
                    if (productDetailBean.getLeft_shares() >= productDetailBean.getShares_increment()*2) {
                        et_insert_count.setText(productDetailBean.getShares_increment()*2 + "");
                    } else if (productDetailBean.getLeft_shares() < productDetailBean.getShares_increment()*2) {
                        et_insert_count.setText(productDetailBean.getLeft_shares() + "");
                    }
                    tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    break;
                case R.id.tv_insert_five:
                    if (productDetailBean.getLeft_shares() >= productDetailBean.getShares_increment()*5) {
                        et_insert_count.setText(productDetailBean.getShares_increment()*5 + "");
                    } else if (productDetailBean.getLeft_shares() < 5) {
                        et_insert_count.setText(productDetailBean.getLeft_shares() + "");
                    }
                    tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    break;
                case R.id.tv_insert_ten:
                    if (productDetailBean.getLeft_shares() >= productDetailBean.getShares_increment()*10) {
                        et_insert_count.setText(productDetailBean.getShares_increment()*10 + "");
                    } else if (productDetailBean.getLeft_shares() < productDetailBean.getShares_increment()*10) {
                        et_insert_count.setText(productDetailBean.getLeft_shares() + "");
                    }
                    tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    break;
                case R.id.tv_insert_all:
                    et_insert_count.setText(productDetailBean.getLeft_shares() + "");
                    tv_insert_buy.setText("Total\t" + et_insert_count.getText().toString() + "\tcoins");
                    break;
                case R.id.tv_insert_buy:
                    tv_insert_buy.setClickable(false);
                    int money = Integer.parseInt(et_insert_count.getText().toString());

                    if (et_insert_count.getText() == null || et_insert_count.getText().length() == 0 || et_insert_count.getText().toString().equals("0")||money%productDetailBean.getShares_increment() != 0) {
                        rl_insert_warn.setVisibility(View.VISIBLE);
                        iv_insert_warn.setVisibility(View.VISIBLE);
                        tv_insert_warn.setEnabled(true);
                        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);//加载动画资源文件
                        shake.setDuration(200);
                        rl_insert_warn.startAnimation(shake); //给组件播放动画效果
                        tv_insert_buy.setClickable(true);
                        return;
                    }
                    //加载进度条 暗示用户  正在购买
                    pb_insert.setVisibility(View.VISIBLE);
                    tv_insert_buy.setBackgroundColor(Color.argb(255,226,226,226));
                    //购买商品接口
                    int shares = Integer.parseInt(et_insert_count.getText().toString());

                    String url = MyApplication.url + "/v1/game-orders/?timezone=" + MyApplication.utc;
                    String json = "{\"game_id\": " + ((SecondPagerActivity) context).game_id + ",\"shares\": " + shares + "}";
                    Map map = new HashMap();
                    String mToken = Utils.getSpData("token", context);
                    map.put("Authorization", "Bearer " + mToken);
                    HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String response) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startPrompt(response, true);
                                    tv_insert_buy.setClickable(true);
                                }
                            });

                        }

                        @Override
                        public void error(final int code, final String message) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (code == 409) {
                                        startPrompt(message, false);
                                        tv_insert_buy.setClickable(true);
                                    }
                                }
                            });
                        }

                        @Override
                        public void failure(Exception exception) {
                            tv_insert_buy.setClickable(true);
                        }
                    });
                    break;
                case R.id.rl_insert_ok:
                    if (show.isShowing()) {
                        show.dismiss();
                    }

                    break;
                case R.id.rl_insert_notsellgame:
                    //game 可能不足时处理
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_lessicons_cancel:
                    //金币不足时 取消
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_lessicons_ok:
                    //金币不足时  去充值
                    if (show.isShowing()) {
                        show.dismiss();
                    }
                    ((SecondPagerActivity) context).switchPage(6);       //跳入到购买金币页面
                    break;
            }
        }
    }

    private void startPrompt(String message, boolean flag) {
        View inflate = View.inflate(context, R.layout.alertdialog_insertcoins, null);
        if(flag == true) {
            TextView tv_insertcoins_title = (TextView) inflate.findViewById(R.id.tv_insertcoins_title);
            JustifyTextView jtv_insertcoins_discribe = (JustifyTextView) inflate.findViewById(R.id.jtv_insertcoins_discribe);
            RelativeLayout rl_insert_ok = (RelativeLayout) inflate.findViewById(R.id.rl_insert_ok);
            rl_insert_ok.setOnClickListener(new MyOnClickListener());

            //购买成功
            initData();
            int money = Integer.parseInt(et_insert_count.getText().toString());
            String balance = Utils.getSpData("balance", context);
            money = Integer.parseInt(balance);
            balance = money - balanceCount + "";
            Utils.setSpData("balance", balance, context);
            StartAlertDialog(inflate);
        }


        if (flag == false) {
            Gson gson = new Gson();
            BuyStateBean buyStateBean = gson.fromJson(message, BuyStateBean.class);
            if ("GameNotFound".equals(buyStateBean.getMessage())) {
                //产品没有发现

            } else if ("InsufficientGameShares".equals(buyStateBean.getMessage())) {
                //产品  数量不足
                Utils.MyToast(context,"Insufficient number of products");
            } else if ("GameNotSellable".equals(buyStateBean.getMessage())) {
                //产品不能出售  有可能是还没出售   还有可能已经售完
                //余额不足
                inflate = View.inflate(context, R.layout.alertdialog_insertcoins_notsellgame, null);
                RelativeLayout rl_insert_notsellgame = (RelativeLayout) inflate.findViewById(R.id.rl_insert_notsellgame);

                rl_insert_notsellgame.setOnClickListener(new MyOnClickListener());
                StartAlertDialog(inflate);
            } else if ("InsufficientBalance".equals(buyStateBean.getMessage())) {
                //余额不足
                inflate = View.inflate(context, R.layout.alertdialog_insertcoins_lessicons, null);
                TextView tv_lessicons_cancel = (TextView) inflate.findViewById(R.id.tv_lessicons_cancel);
                TextView tv_lessicons_ok = (TextView) inflate.findViewById(R.id.tv_lessicons_ok);

                tv_lessicons_cancel.setOnClickListener(new MyOnClickListener());
                tv_lessicons_ok.setOnClickListener(new MyOnClickListener());
                StartAlertDialog(inflate);
            }
        }

        if(popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

    }

    private AlertDialog show;

    private void StartAlertDialog(View inflate) {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show = builder.show();
        show.setCanceledOnTouchOutside(false);   //点击外部不消失
//        show.setCancelable(false);               //点击外部和返回按钮都不消失
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        show.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    private int balanceCount = 0;

    //对popupwindow进行操作
    public void setPPW(View PPW) {
        this.PPW = PPW;
        rl_insert_delete = (RelativeLayout) PPW.findViewById(R.id.rl_insert_delete);
        rl_insert_add = (RelativeLayout) PPW.findViewById(R.id.rl_insert_add);
        tv_insert_two = (TextView) PPW.findViewById(R.id.tv_insert_two);
        tv_insert_five = (TextView) PPW.findViewById(R.id.tv_insert_five);
        tv_insert_ten = (TextView) PPW.findViewById(R.id.tv_insert_ten);
        tv_insert_all = (TextView) PPW.findViewById(R.id.tv_insert_all);
        tv_insert_buy = (TextView) PPW.findViewById(R.id.tv_insert_buy);
        et_insert_count = (EditText) PPW.findViewById(R.id.et_insert_count);

        rl_insert_warn = (RelativeLayout) PPW.findViewById(R.id.rl_insert_warn);
        tv_insert_warn = (TextView) PPW.findViewById(R.id.tv_insert_warn);
        iv_insert_warn = (ImageView) PPW.findViewById(R.id.iv_insert_warn);
        pb_insert = (ProgressBar) PPW.findViewById(R.id.pb_insert);

        if(productDetailBean.getShares_increment() == 5) {
            tv_insert_warn.setText("Purchase quantity must be a multiple of 5");
        }else if(productDetailBean.getShares_increment() == 10) {
            tv_insert_warn.setText("Purchase quantity must be a multiple of 10");
        }else {
            rl_insert_warn.setVisibility(View.GONE);
        }

        //光标放在数字后面
        Editable ea = et_insert_count.getText();

        et_insert_count.setSelection(ea.length());

        et_insert_count.setText(productDetailBean.getShares_increment() + "");
        tv_insert_two.setText(productDetailBean.getShares_increment()*2 + "");
        tv_insert_five.setText(productDetailBean.getShares_increment()*5 + "");
        tv_insert_ten.setText(productDetailBean.getShares_increment()*10 + "");


        rl_insert_delete.setOnClickListener(new MyOnClickListener());
        rl_insert_add.setOnClickListener(new MyOnClickListener());

        tv_insert_buy.setText("Total" + et_insert_count.getText().toString() + "coins");

        tv_insert_two.setOnClickListener(new MyOnClickListener());
        tv_insert_five.setOnClickListener(new MyOnClickListener());
        tv_insert_ten.setOnClickListener(new MyOnClickListener());
        tv_insert_all.setOnClickListener(new MyOnClickListener());
        tv_insert_buy.setOnClickListener(new MyOnClickListener());

        et_insert_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && charSequence.toString().length() > 0) {
                    int count = Integer.parseInt(charSequence.toString());
                    if (count > productDetailBean.getLeft_shares()) {
                        et_insert_count.setText(productDetailBean.getLeft_shares() + "");

                    }
                    tv_insert_buy.setText("Total" + et_insert_count.getText().toString() + "coins");
                }
                if ("".equals(et_insert_count.getText().toString())) {
                    balanceCount = 0;
                } else {
                    balanceCount = Integer.parseInt(et_insert_count.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Editable ea = et_insert_count.getText();

                et_insert_count.setSelection(ea.length());
            }
        });
    }

    private String newData;

    //请求最新数据
    private void NewData() {
        if (((SecondPagerActivity) context).game_id == -1) {
            if (((SecondPagerActivity) context).batch_id != -1) {
                if (isNeedNetWaiting) {
                    rl_keepout.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    rl_neterror.setVisibility(View.GONE);
                    rl_loading.setVisibility(View.VISIBLE);
                    isNeedNetWaiting = false;
                }
            }
        }
        int batch_id = ((SecondPagerActivity) context).batch_id;
        String url = MyApplication.url + "/v1/games/?status=running&batch_id=" + batch_id + "&per_page=20&page=1&timezone=" + MyApplication.utc;
        HttpUtils.getInstance().getRequest(url, null, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.length() > 10) {
                            ll_productdetail_buyit.setVisibility(View.VISIBLE);
                            newData = response.replace("[", "");
                            newData = newData.replace("]", "");

                        } else {
                            ll_productdetail_buyit.setVisibility(View.GONE);
                        }
                        rl_keepout.setVisibility(View.GONE);

                        if (((SecondPagerActivity) context).game_id == -1) {
                            if (((SecondPagerActivity) context).batch_id != -1) {
                                ll_productdetail_buyit.setVisibility(View.GONE);
                                rl_productdetail_indsertcoins.setVisibility(View.VISIBLE);
                                processData(newData);

                                additionRequest();
                            }
                        }
                    }
                });
            }

            @Override
            public void error(final int code, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nodata.setVisibility(View.GONE);
                        rl_neterror.setVisibility(View.VISIBLE);
                        rl_loading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
