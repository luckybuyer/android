package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BasePager;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/18.
 */
public class SetPager extends BasePager {
    private RelativeLayout rl_set_header;
    private RelativeLayout rl_set_address;
    private RelativeLayout rl_set_email;
    //    private RelativeLayout rl_set_us;
    private TextView tv_set_login;
    private TextView tv_set_cl;
    private View inflate;
    private RelativeLayout rl_set_back;
    private RelativeLayout rl_set_country;
    private RelativeLayout rl_set_problem;
    private ImageView iv_country;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_set, null);
        ((SecondPagerActivity) context).from = "";
        findView();

        String servicecount = Utils.getSpData("servicecount",context);
        if("1".equals(servicecount)) {
            tv_set_cl.setText(context.getString(R.string.Language));
            iv_country.setVisibility(View.GONE);
        }else {
            tv_set_cl.setText(context.getString(R.string.CountryLanguage));
            iv_country.setVisibility(View.GONE);
        }
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void findView() {
        rl_set_header = (RelativeLayout) inflate.findViewById(R.id.rl_set_header);
        rl_set_address = (RelativeLayout) inflate.findViewById(R.id.rl_set_address);
        rl_set_email = (RelativeLayout) inflate.findViewById(R.id.rl_set_email);
//        rl_set_us = (RelativeLayout) inflate.findViewById(R.id.rl_set_us);
        tv_set_login = (TextView) inflate.findViewById(R.id.tv_set_login);
        rl_set_back = (RelativeLayout) inflate.findViewById(R.id.rl_set_back);
        rl_set_country = (RelativeLayout) inflate.findViewById(R.id.rl_set_country);
        rl_set_problem = (RelativeLayout) inflate.findViewById(R.id.rl_set_problem);
        iv_country = (ImageView) inflate.findViewById(R.id.iv_country);
        tv_set_cl = (TextView) inflate.findViewById(R.id.tv_set_cl);


        try {
            Field field = R.drawable.class.getField(Utils.getSpData("country", context).replace(" ", "_").toLowerCase());
            int i = field.getInt(new R.drawable());
            iv_country.setImageResource(i);
//            Log.e("TAG", image + "");
        } catch (Exception e) {

        }

        rl_set_address.setOnClickListener(new MyOnClickListener());
        rl_set_email.setOnClickListener(new MyOnClickListener());
//        rl_set_us.setOnClickListener(new MyOnClickListener());
        tv_set_login.setOnClickListener(new MyOnClickListener());
        rl_set_back.setOnClickListener(new MyOnClickListener());
        rl_set_country.setOnClickListener(new MyOnClickListener());
        rl_set_problem.setOnClickListener(new MyOnClickListener());
        String token = Utils.getSpData("token", context);


    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_set_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.rl_set_address:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "setpager";
                    break;
                case R.id.rl_set_email:
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:contact@luckybuyer.net"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "User ID:" + Utils.getSpData("id", context) + "");
                    startActivity(data);
                    break;
//                case R.id.rl_set_us:
//                    Utils.MyToast(context,"关于我们");
//                    break;
                case R.id.tv_set_login:
                    logOut();
                    break;
                case R.id.rl_set_country:
                    Intent intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "clpager");
                    startActivity(intent);
                    break;
                case R.id.rl_set_problem:
                    intent = new Intent(context, ThirdPagerActivity.class);
                    intent.putExtra("from", "problem");
                    startActivity(intent);
                    break;
            }
        }
    }

    private void logOut() {
        HttpUtils.getInstance().startNetworkWaiting(context);
        String FCMID = Utils.getSpData("refreshedToken",context);
        String url = MyApplication.url + "/v1/fcm/registrations/"+FCMID+"?timezone=" + MyApplication.utc;

        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().startNetworkWaiting(context);
        HttpUtils.getInstance().deleteResponse(url, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response,String link) {
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
                        Log.e("TAG_logout", code + message);
                        if (code == 204) {
                            Intent intent = new Intent();
                            intent.putExtra("go", "homepager");
                            ((SecondPagerActivity) context).setResult(0, intent);
                            Utils.setSpData("token", null, context);
                            Utils.setSpData("token_num", null, context);
                            ((SecondPagerActivity) context).finish();
                        } else {
                            Utils.MyToast(context, "退出登陆失败");
                        }
                        HttpUtils.getInstance().stopNetWorkWaiting();

                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                Utils.MyToast(context, "推出登陆失败");
                HttpUtils.getInstance().stopNetWorkWaiting();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
