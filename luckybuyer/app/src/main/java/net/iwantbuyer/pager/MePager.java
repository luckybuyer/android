package net.iwantbuyer.pager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.share.widget.LikeView;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.inthecheesefactory.lib.fblike.widget.FBLikeView;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.MePagerAllAdapter;
import net.iwantbuyer.adapter.MePagerLuckyAdapter;
import net.iwantbuyer.adapter.MePagerViewPagerAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.bean.AllOrderBean;
import net.iwantbuyer.bean.CoinDetailBean;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;
import net.iwantbuyer.view.BottomScrollView;
import net.iwantbuyer.view.CircleImageView;
import net.iwantbuyer.view.CustomViewPager;

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
    private ImageView iv_me_voice;
    private ImageView i_me_set;                  //设置
    private TextView tv_me_name;
    private TextView tv_me_fbcode;
    private TextView tv_me_gold;
    public CustomViewPager vp_me;
    private SlidingTabLayout stl_me_vpcontrol;
    public BottomScrollView sv_me;
    public View inflate;

    //下拉加载更多
    private LinearLayout ll_loading_data;
    private ProgressBar pb_loading_data;
    private TextView tv_loading_data;


    private RelativeLayout rl_keepout;
    private RelativeLayout rl_neterror;
    private RelativeLayout rl_nodata;
    private RelativeLayout rl_loading;
    private TextView tv_net_again;

    private View view_me_top;                            //让视图指定做的临时

    private FBLikeView fb_shipping_facebook;

    private List vpList;
    public RecyclerView recyclerView;
    public MePagerAllAdapter mePagerAllAdapter;
    public MePagerLuckyAdapter mePagerLuckyAdapter;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_me, null);
        findView();

        setView();

        //埋点
        try {
            JSONObject props = new JSONObject();
            MyApplication.mixpanel.track("PAGE:my_account", props);
        } catch (Exception e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        vp_me.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ViewGroup.LayoutParams params = vp_me.getLayoutParams();
                if (position == 0) {
                    params.height = vp_me.allHeight;
                } else if (position == 1) {
                    params.height = vp_me.luckyHeight;
                }
                vp_me.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return inflate;
    }


    @Override
    public void initData() {
        super.initData();
        if(rl_keepout != null && rl_nodata != null && rl_neterror != null && rl_loading != null) {
            rl_keepout.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.GONE);
            rl_neterror.setVisibility(View.GONE);
            rl_loading.setVisibility(View.VISIBLE);
        }


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
                                        //我的中奖  接口(lucky)
                                        if (response.length() > 10) {
                                            LuckyResponse(finalToken, response);
                                        } else {
                                            rl_nodata.setVisibility(View.VISIBLE);
                                            rl_neterror.setVisibility(View.GONE);
                                            rl_loading.setVisibility(View.GONE);
                                        }
                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {
                        Log.e("TAG", requestCode + "");
                        Log.e("TAG", message);
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
                }

        );

    }

    private void LuckyResponse(String token, final String res) {
        String url = MyApplication.url + "/v1/game-orders/?lucky=true&per_page=20&page=1&timezone=" + MyApplication.utc;
        Map map = new HashMap<String, String>();
        map.put("Authorization", "Bearer " + token);
        //请求登陆接口
        HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                    @Override
                    public void success(final String response) {
                        ((Activity) context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.length() > 10) {
                                            rl_keepout.setVisibility(View.GONE);
                                            processData(res, response);
                                        } else {
                                            rl_nodata.setVisibility(View.VISIBLE);
                                            rl_neterror.setVisibility(View.GONE);
                                            rl_loading.setVisibility(View.GONE);
                                        }

                                    }
                                }
                        );
                    }

                    @Override
                    public void error(int requestCode, String message) {
                        Log.e("TAG", requestCode + "");
                        Log.e("TAG", message);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.getInstance().stopNetWorkWaiting();
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
                                HttpUtils.getInstance().stopNetWorkWaiting();
                                rl_nodata.setVisibility(View.GONE);
                                rl_neterror.setVisibility(View.VISIBLE);
                                rl_loading.setVisibility(View.GONE);

                            }
                        });
                    }
                }

        );
    }

    private void processData(String res, String resLucky) {
        res = "{\"allorder\":" + res + "}";
        Gson gson = new Gson();
        AllOrderBean allOrderBean = gson.fromJson(res, AllOrderBean.class);

        resLucky = "{\"allorder\":" + resLucky + "}";
        gson = new Gson();
        AllOrderBean luckyOrderBean = gson.fromJson(resLucky, AllOrderBean.class);
        vpList = new ArrayList();

        //加载All页面
        View view = View.inflate(context, R.layout.pager_me_recycle_all, null);
        RecyclerView rv_me_all = (RecyclerView) view.findViewById(R.id.rv_me_all);
        rv_me_all.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mePagerAllAdapter = new MePagerAllAdapter(context, allOrderBean.getAllorder(), sv_me);
        rv_me_all.setAdapter(mePagerAllAdapter);
        vpList.add(view);

        //加载lucky界面
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mePagerLuckyAdapter = new MePagerLuckyAdapter(context, luckyOrderBean.getAllorder(), sv_me);
        recyclerView.setAdapter(mePagerLuckyAdapter);
        vpList.add(recyclerView);


        vp_me.setAdapter(new MePagerViewPagerAdapter(context, vpList));
        stl_me_vpcontrol.setViewPager(vp_me);

        //判断是否需要显示底部加载进度条
        if (allOrderBean.getAllorder().size() < 20) {
            ll_loading_data.setVisibility(View.GONE);
        }
        if (luckyOrderBean.getAllorder().size() < 20 && vp_me.getCurrentItem() == 1) {
            ll_loading_data.setVisibility(View.GONE);
        }

        getMoreData(gson, mePagerLuckyAdapter, mePagerAllAdapter);
    }

    boolean isMoreLData = true;
    boolean isuckyLuckyNeedpull = true;
    int luckyPage = 2;

    boolean isMoreData = true;
    boolean isNeedpull = true;
    int page = 2;

    private void getMoreData(final Gson gson, final MePagerLuckyAdapter mePagerLuckyAdapter, final MePagerAllAdapter mePagerAllAdapter) {
        sv_me.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom && isMoreLData && isuckyLuckyNeedpull && vp_me.getCurrentItem() == 1) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText("loading...");

                    isuckyLuckyNeedpull = false;
                    String url = MyApplication.url + "/v1/game-orders/?lucky=true&per_page=20&page=" + luckyPage + "&timezone=" + MyApplication.utc;
                    Map map = new HashMap<String, String>();
                    String token = Utils.getSpData("token", context);
                    map.put("Authorization", "Bearer " + token);
                    //请求登陆接口
                    HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                                @Override
                                public void success(final String response) {
                                    ((Activity) context).runOnUiThread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    isuckyLuckyNeedpull = true;
                                                    if (response.length() > 10) {
                                                        String res = "{\"allorder\":" + response + "}";
                                                        AllOrderBean luckyOrder = gson.fromJson(res, AllOrderBean.class);
                                                        for (int i = 0; i < luckyOrder.getAllorder().size(); i++) {
                                                            mePagerLuckyAdapter.list.add(luckyOrder.getAllorder().get(i));
                                                            if (i == luckyOrder.getAllorder().size() - 1) {
                                                                mePagerLuckyAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                        if (luckyOrder.getAllorder().size() < 20) {
                                                            isMoreData = false;
                                                        }
                                                        luckyPage++;
                                                    } else {
                                                        ll_loading_data.setVisibility(View.VISIBLE);
                                                        pb_loading_data.setVisibility(View.GONE);
                                                        tv_loading_data.setText("no more data");
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ll_loading_data.setVisibility(View.GONE);
                                                            }
                                                        },3000);
                                                    }

                                                }
                                            }
                                    );
                                }

                                @Override
                                public void error(int requestCode, String message) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            isNeedpull = true;
                                            pb_loading_data.setVisibility(View.GONE);
                                            tv_loading_data.setText("Network failure");
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_loading_data.setVisibility(View.GONE);
                                                }
                                            },3000);

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
                                            tv_loading_data.setText("Network failure");
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_loading_data.setVisibility(View.GONE);
                                                }
                                            },3000);
                                        }
                                    });
                                }
                            }


                    );
                }


                if (isBottom && isMoreData && isNeedpull&&vp_me.getCurrentItem() == 0) {
                    ll_loading_data.setVisibility(View.VISIBLE);
                    pb_loading_data.setVisibility(View.VISIBLE);
                    tv_loading_data.setText("loading...");

                    isNeedpull = false;
                    String token = Utils.getSpData("token", context);
                    String url = MyApplication.url + "/v1/game-orders/?per_page=20&page=" + page + "&timezone=" + MyApplication.utc;
                    Log.e("TAG", url);
                    Map map = new HashMap<String, String>();
                    map.put("Authorization", "Bearer " + token);
                    HttpUtils.getInstance().getRequest(url, map, new HttpUtils.OnRequestListener() {
                        @Override
                        public void success(final String string) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    if (string.length() > 10) {
                                        String res = "{\"allorder\":" + string + "}";
                                        AllOrderBean allOrderBean = gson.fromJson(res, AllOrderBean.class);
                                        for (int i = 0; i < allOrderBean.getAllorder().size(); i++) {
                                            mePagerAllAdapter.list.add(allOrderBean.getAllorder().get(i));
                                            if (i == allOrderBean.getAllorder().size() - 1) {
                                                mePagerAllAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (allOrderBean.getAllorder().size() < 20) {
                                            isMoreData = false;
                                        }
                                        page++;
                                    } else {
                                        ll_loading_data.setVisibility(View.VISIBLE);
                                        pb_loading_data.setVisibility(View.GONE);
                                        tv_loading_data.setText("no more data");
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ll_loading_data.setVisibility(View.GONE);
                                            }
                                        },3000);
                                    }

                                }
                            });
                        }

                        @Override
                        public void error(final int requestCode, final String message) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isNeedpull = true;
                                    pb_loading_data.setVisibility(View.GONE);
                                    tv_loading_data.setText("Network failure");
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_loading_data.setVisibility(View.GONE);
                                        }
                                    },3000);
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
                                    tv_loading_data.setText("Network failure");
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_loading_data.setVisibility(View.GONE);
                                        }
                                    },3000);
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    private void findView() {
        rl_me_title = (RelativeLayout) inflate.findViewById(R.id.rl_me_title);
        civ_me_header = (CircleImageView) inflate.findViewById(R.id.civ_me_header);
        iv_me_voice = (ImageView) inflate.findViewById(R.id.iv_me_voice);
        i_me_set = (ImageView) inflate.findViewById(R.id.i_me_set);
        tv_me_name = (TextView) inflate.findViewById(R.id.tv_me_name);
        tv_me_fbcode = (TextView) inflate.findViewById(R.id.tv_me_fbcode);
        tv_me_gold = (TextView) inflate.findViewById(R.id.tv_me_gold);
        vp_me = (CustomViewPager) inflate.findViewById(R.id.vp_me);
        stl_me_vpcontrol = (SlidingTabLayout) inflate.findViewById(R.id.stl_me_vpcontrol);
        fb_shipping_facebook = (FBLikeView) inflate.findViewById(R.id.fb_shipping_facebook);
        sv_me = (BottomScrollView) inflate.findViewById(R.id.sv_me);
        rl_keepout = (RelativeLayout) inflate.findViewById(R.id.rl_keepout);
        rl_neterror = (RelativeLayout) inflate.findViewById(R.id.rl_neterror);
        rl_nodata = (RelativeLayout) inflate.findViewById(R.id.rl_nodata);
        rl_loading = (RelativeLayout) inflate.findViewById(R.id.rl_loading);
        tv_net_again = (TextView) inflate.findViewById(R.id.tv_net_again);
        view_me_top = (View) inflate.findViewById(R.id.view_me_top);

        ll_loading_data = (LinearLayout) inflate.findViewById(R.id.ll_loading_data);
        pb_loading_data = (ProgressBar) inflate.findViewById(R.id.pb_loading_data);
        tv_loading_data = (TextView) inflate.findViewById(R.id.tv_loading_data);


        i_me_set.setOnClickListener(new MyOnClickListener());
        iv_me_voice.setOnClickListener(new MyOnClickListener());
        tv_me_gold.setOnClickListener(new MyOnClickListener());
        tv_net_again.setOnClickListener(new MyOnClickListener());
    }

    public void setView() {

//        Log.e("TAG", "进来了  设置");
//        view_me_top.setFocusable(true);
//        view_me_top.setFocusableInTouchMode(true);
//        view_me_top.requestFocus();

        String user_id = Utils.getSpData("user_id", context);
        String balance = Utils.getSpData("balance", context);
        String name = Utils.getSpData("name", context);
        final String picture = Utils.getSpData("picture", context);


        tv_me_name.setText(name);
        tv_me_fbcode.setText(user_id);
        tv_me_gold.setText("" + balance);
        Glide.with(context).load(picture).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                civ_me_header.setImageBitmap(resource);
            }
        });

        fb_shipping_facebook.getLikeView().setObjectIdAndType(
                "https://www.facebook.com/ae.luckybuyer.net",
                LikeView.ObjectType.OPEN_GRAPH);
    }

    //点击监听
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.i_me_set:
                    //埋点
                    try {
                        JSONObject props = new JSONObject();
                        MyApplication.mixpanel.track("CLICK:setting", props);
                    } catch (Exception e) {
                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                    }

                    Intent intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "setpager");
                    startActivityForResult(intent, 0);
                    break;
                case R.id.tv_me_gold:
                    intent = new Intent(context, SecondPagerActivity.class);
                    intent.putExtra("from", "coindetailpager");
                    startActivity(intent);
                    break;
                case R.id.tv_net_again:
                    initData();
                    break;
                case R.id.iv_me_voice:
                    //埋点
                    try {
                        JSONObject props = new JSONObject();
                        MyApplication.mixpanel.track("CLICK:chat", props);
                    } catch (Exception e) {
                        Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                    }

                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:contact@luckybuyer.net"));
                    startActivity(data);
                    break;
            }
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
