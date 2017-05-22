package net.iwantbuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.widget.LikeView;
import com.google.gson.Gson;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.MainActivity;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.MePagerAllAdapter;
import net.iwantbuyer.adapter.MePagerLuckyAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.AllOrderBean;
import net.iwantbuyer.bean.User;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.BottomScrollView;
import net.iwantbuyer.view.CircleImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/13.
 */
public class MePager extends BaseNoTrackPager {

    public RelativeLayout rl_me_title;
    private CircleImageView civ_me_header;
    private CircleImageView civ_me_header_src;
    private ImageView i_me_set;                  //设置
    private TextView tv_me_name;
    private TextView tv_me_gold;
    public RecyclerView rv_me;
    private TabLayout tl_me;
    public BottomScrollView sv_me;
    private TextView tv_me_topup;
    private TextView tv_me_login;
    public View inflate;
    private RelativeLayout rl_me_coins;

    //下拉加载更多
    private LinearLayout ll_loading_data;
    private ProgressBar pb_loading_data;
    private TextView tv_loading_data;


    private RelativeLayout rl_list_keepout;
    private RelativeLayout rl_list_neterror;
    private RelativeLayout rl_list_nodata;
    private RelativeLayout rl_list_loading;
    private TextView tv_list_net_again;

    private TextView tv_me_id;

//    private FBLikeView fb_shipping_facebook;

    private List vpList;
    public RecyclerView recyclerView;
    public MePagerAllAdapter mePagerAllAdapter;
    public MePagerLuckyAdapter mePagerLuckyAdapter;

    String token;
    List list = new ArrayList();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_me, null);
        findView();
        token = Utils.getSpData("token", context);
        Log.e("TAG_me", token + "");
        setView();




        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        String token = Utils.getSpData("token", context);

        if (token == null) {

        } else {
            Login(token);
            startAll(token);
        }


        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        LikeView likeView = (LikeView) inflate.findViewById(R.id.lv_me);
        likeView.setObjectIdAndType(
                "https://www.facebook.com/luckybuyer.net",
                LikeView.ObjectType.PAGE);


    }

    String link;

    private void startAll(String token) {
        String url = MyApplication.url + "/v1/game-orders/?per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);

        //请求登陆接口
        final String finalToken = token;
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response, final String link) {

                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //我的中奖  接口(lucky)
                                        HttpUtils.getInstance().stopNetWorkWaiting();
                                        if (response.length() > 10) {
                                            MePager.this.link = link;
                                            processData(response);
                                            rl_list_keepout.setVisibility(View.GONE);
                                            tl_me.getTabAt(0).select();
                                        } else {
                                            rl_list_nodata.setVisibility(View.VISIBLE);
                                            rl_list_neterror.setVisibility(View.GONE);
                                            rl_list_loading.setVisibility(View.GONE);
                                        }
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(final int requestCode, String message) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                rl_list_keepout.setVisibility(View.VISIBLE);
                                rl_list_nodata.setVisibility(View.GONE);
                                rl_list_neterror.setVisibility(View.VISIBLE);
                                rl_list_loading.setVisibility(View.GONE);
                                Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "game-orders");
                                list.clear();
                                if (mePagerAllAdapter != null) {
                                    mePagerAllAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                rl_list_keepout.setVisibility(View.VISIBLE);
                                rl_list_nodata.setVisibility(View.GONE);
                                rl_list_neterror.setVisibility(View.VISIBLE);
                                rl_list_loading.setVisibility(View.GONE);
                                Utils.MyToast(context, context.getString(R.string.Networkfailure));
                                list.clear();
                                if (mePagerAllAdapter != null) {
                                    mePagerAllAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }

        );
    }

    private void startLucky(String token) {
        String url = MyApplication.url + "/v1/game-orders/?lucky=true&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);

        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response, final String link) {
                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        HttpUtils.getInstance().stopNetWorkWaiting();
                                        if (response.length() > 10) {
                                            MePager.this.link = link;
                                            processData(response);
                                            rl_list_keepout.setVisibility(View.GONE);
                                            tl_me.getTabAt(1).select();
                                        } else {
                                            rl_list_nodata.setVisibility(View.VISIBLE);
                                            rl_list_neterror.setVisibility(View.GONE);
                                            rl_list_loading.setVisibility(View.GONE);
                                        }
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(final int requestCode, String message) {
                        Log.e("TAG", requestCode + "");
                        Log.e("TAG", message);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                rl_list_keepout.setVisibility(View.VISIBLE);
                                rl_list_nodata.setVisibility(View.GONE);
                                rl_list_neterror.setVisibility(View.VISIBLE);
                                rl_list_loading.setVisibility(View.GONE);
                                list.clear();
                                if (mePagerAllAdapter != null) {
                                    mePagerAllAdapter.notifyDataSetChanged();
                                }
                                Utils.MyToast(context, context.getString(R.string.Networkfailure) + requestCode + "game-orders");

                            }
                        });
                    }

                    @Override
                    public void failure(Exception exception) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                rl_list_keepout.setVisibility(View.VISIBLE);
                                rl_list_nodata.setVisibility(View.GONE);
                                rl_list_neterror.setVisibility(View.VISIBLE);
                                rl_list_loading.setVisibility(View.GONE);
                                Utils.MyToast(context, context.getString(R.string.Networkfailure));
                                list.clear();
                                if (mePagerAllAdapter != null) {
                                    mePagerAllAdapter.notifyDataSetChanged();
                                }

                            }
                        });
                    }
                }

        );
    }

    private void startMore(String url) {
        Map map = new HashMap();

        map.put("Authorization", "Bearer " + token);
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String string, final String link) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG_lll", link);
                        isNeedpull = true;
                        MePager.this.link = link;
                        Gson gson = new Gson();
                        String response = "{\"allorder\":" + string + "}";
                        AllOrderBean allOrderBean = gson.fromJson(response, AllOrderBean.class);
                        //产品列表数据
                        list.addAll(allOrderBean.getAllorder());
                        mePagerAllAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void error(final int requestCode, final String message) {
                Log.e("TAG_me", requestCode + message);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isNeedpull = true;
                        pb_loading_data.setVisibility(View.GONE);
                        tv_loading_data.setText(context.getString(R.string.Networkfailure));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_loading_data.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isNeedpull = true;
                        pb_loading_data.setVisibility(View.GONE);
                        tv_loading_data.setText(context.getString(R.string.Networkfailure));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_loading_data.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }
                });

            }

        });
    }

    private void processData(String response) {
        response = "{\"allorder\":" + response + "}";
        Gson gson = new Gson();
        AllOrderBean allOrderBean = gson.fromJson(response, AllOrderBean.class);

        vpList = new ArrayList();

        //加载All页面
        rv_me.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        list = allOrderBean.getAllorder();
        mePagerAllAdapter = new MePagerAllAdapter(context, list, sv_me);
        rv_me.setAdapter(mePagerAllAdapter);

        //下拉加载
        sv_me.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {

                String next = "";
                String last = null;
                String[] str = link.split(",");
                for (int i = 0; i < str.length; i++) {
                    if (str[i].contains("rel=\"next\"")) {
                        next = str[i].substring(str[i].indexOf("<") + 1, str[i].indexOf(">"));
                    }
                    if (str[i].contains("rel=\"last\"")) {
                        last = str[i].substring(str[i].indexOf("<") + 1, str[i].indexOf(">"));
                    }
                }

                if (link.contains("rel=\"next\"") && isNeedpull && isBottom && !next.equals(last)) {
                    isNeedpull = false;
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText(context.getString(R.string.loading___));

                    String[] st = link.split(",");
                    for (int i = 0; i < str.length; i++) {
                        if (st[i].contains("rel=\"next\"")) {
                            String url = st[i].substring(st[i].indexOf("<") + 1, st[i].indexOf(">"));
                            startMore(url);
                        }
                    }
                } else if (link.contains("rel=\"last\"") && next.equals(last)) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.GONE);
                    tv_loading_data.setText(context.getString(R.string.nomoredata));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll_loading_data.setVisibility(View.GONE);
                        }
                    }, 3000);
                } else if (!link.contentEquals("rel=\"next\"")) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.GONE);
                    tv_loading_data.setText(context.getString(R.string.nomoredata));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll_loading_data.setVisibility(View.GONE);
                        }
                    }, 3000);
                }


            }
        });


    }

    boolean isNeedpull = true;


    private void findView() {
        rl_me_title = (RelativeLayout) inflate.findViewById(R.id.rl_me_title);
        civ_me_header = (CircleImageView) inflate.findViewById(R.id.civ_me_header);
        civ_me_header_src = (CircleImageView) inflate.findViewById(R.id.civ_me_header_src);
        i_me_set = (ImageView) inflate.findViewById(R.id.i_me_set);
        tv_me_name = (TextView) inflate.findViewById(R.id.tv_me_name);
        tv_me_gold = (TextView) inflate.findViewById(R.id.tv_me_gold);
        tl_me = (TabLayout) inflate.findViewById(R.id.tl_me);
        rv_me = (RecyclerView) inflate.findViewById(R.id.rv_me);
        tv_me_login = (TextView) inflate.findViewById(R.id.tv_me_login);
//        fb_shipping_facebook = (FBLikeView) inflate.findViewById(R.id.fb_shipping_facebook);
        sv_me = (BottomScrollView) inflate.findViewById(R.id.sv_me);
        rl_list_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_list_keepout);
        rl_list_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_list_neterror);
        rl_list_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_list_nodata);
        rl_list_loading = (RelativeLayout) inflate.findViewById(R.id.rl_list_loading);
        tv_list_net_again = (TextView) inflate.findViewById(R.id.tv_list_net_again);
        tv_me_topup = (TextView) inflate.findViewById(R.id.tv_me_topup);

        ll_loading_data = (LinearLayout) inflate.findViewById(R.id.ll_loading_data);
        pb_loading_data = (ProgressBar) inflate.findViewById(R.id.pb_loading_data);
        tv_loading_data = (TextView) inflate.findViewById(R.id.tv_loading_data);
        rl_me_coins = (RelativeLayout) inflate.findViewById(R.id.rl_me_coins);

        tv_me_id = (TextView) inflate.findViewById(R.id.tv_me_id);                                  //用户id


        i_me_set.setOnClickListener(new MyOnClickListener());
        tv_me_login.setOnClickListener(new MyOnClickListener());
        tv_me_topup.setOnClickListener(new MyOnClickListener());
        tv_list_net_again.setOnClickListener(new MyOnClickListener());
        rl_me_coins.setOnClickListener(new MyOnClickListener());

        tl_me.addTab(tl_me.newTab().setText(context.getString(R.string.All)), 0);
        tl_me.addTab(tl_me.newTab().setText(context.getString(R.string.LuckyRecords)), 1);
        tl_me.addOnTabSelectedListener(new MyOnTabSelectedListener());
    }

    private void Login(final String token) {
        String url = MyApplication.url + "/v1/users/me/?timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);

        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(String response, String link) {


                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                Utils.setSpData("id", user.getId() + "", context);
                Utils.setSpData("user_id", user.getAuth0_user_id(), context);
                Utils.setSpData("balance", user.getBalance() + "", context);
                Utils.setSpData("name", user.getProfile().getName(), context);
                Utils.setSpData("picture", user.getProfile().getPicture(), context);
                Utils.setSpData("social_link", user.getProfile().getSocial_link(), context);

                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setView();
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

    public void setView() {
        if (Utils.getSpData("token", context) == null) {
            tv_me_id.setVisibility(View.GONE);
            tv_me_name.setVisibility(View.GONE);
            tv_me_gold.setText("0");
            tv_me_login.setVisibility(View.VISIBLE);

            civ_me_header_src.setVisibility(View.VISIBLE);
            civ_me_header.setImageResource(R.drawable.civ_shape);
            rl_list_keepout.setVisibility(View.VISIBLE);
            rl_list_nodata.setVisibility(View.VISIBLE);
            rl_list_neterror.setVisibility(View.GONE);
            rl_list_loading.setVisibility(View.GONE);

            if(mePagerAllAdapter != null) {
                list.clear();
                mePagerAllAdapter.notifyDataSetChanged();
            }


        } else if (Utils.getSpData("id", context) != null) {
            String user_id = Utils.getSpData("user_id", context);
            String id__ = Utils.getSpData("id", context);
            String balance = Utils.getSpData("balance", context);
            String name = Utils.getSpData("name", context);
            final String picture = Utils.getSpData("picture", context);


            //增加id
            int id = Integer.parseInt(id__) + 10000000;
            String _id = "";
            char[] id_ = (id + "").toCharArray();
            for (int i = 0; i < id_.length; i++) {
                if (i % 4 == 3) {
                    _id = _id + id_[i] + "  ";
                } else {
                    _id = _id + id_[i];
                }

            }
            tv_me_id.setVisibility(View.VISIBLE);
            tv_me_name.setVisibility(View.VISIBLE);
            tv_me_login.setVisibility(View.GONE);
            civ_me_header_src.setVisibility(View.GONE);

            tv_me_id.setText(context.getString(R.string.ID_) + _id);
            tv_me_name.setText(name);
            tv_me_gold.setText("" + balance);
            if (!((MainActivity) context).isDestroyed()) {
                Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        civ_me_header.setImageBitmap(resource);
                        civ_me_header_src.setVisibility(View.GONE);
                    }
                });
            }

        }

    }

    //点击监听
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.i_me_set:

                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "setpager");
                    startActivityForResult(intent, 0);
                    break;
                case R.id.tv_me_topup:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "buycoins");
                    startActivity(intent);
                    break;
                case R.id.tv_me_login:
                    //登录
                    context.startActivity(((MainActivity) context).lock.newIntent(((MainActivity) context)));
//
                    break;
                case R.id.tv_list_net_again:
                    int id = tl_me.getSelectedTabPosition();
                    if (id == 0) {
                        startAll(token);
                    } else if (id == 1) {
                        startLucky(token);
                    }
                    break;
                case R.id.rl_me_coins:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "coindetailpager");
                    startActivity(intent);
                    break;
            }
        }
    }

    //点击tablayout的监听
    class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            list.clear();
            switch (tab.getPosition()) {
                case 0:
                    //请求ALL

                    if (Utils.getSpData("token", context) != null) {
                        startAll(Utils.getSpData("token", context));
                        HttpUtils.getInstance().startNetworkWaiting(context);
                    }

                    break;
                case 1:
                    //请求Progress
                    if (Utils.getSpData("token", context) != null) {
                        startLucky(Utils.getSpData("token", context));
                        HttpUtils.getInstance().startNetworkWaiting(context);
                    }
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    //根据版本判断是否 需要设置据顶部状态栏高度
    @TargetApi(19)
    private void setHeadMargin() {
        Rect frame = new Rect();
        //获取状态栏高度
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 315));
        lp.topMargin = statusBarHeight;
        sv_me.setLayoutParams(lp);
    }

}
