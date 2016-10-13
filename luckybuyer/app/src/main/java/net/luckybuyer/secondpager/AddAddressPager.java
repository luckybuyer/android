package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/10/8.
 */
public class AddAddressPager extends BaseNoTrackPager {
    private RelativeLayout rl_addaddress_header;
    private ImageView iv_addaddress_back;
    private TextView tv_addaddress_back;
    private EditText et_addaddress_name;
    private EditText et_addaddress_telnum;
    private EditText et_addaddress_detail;
    private TextView tv_addaddress_save;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_addaddress, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setHeadMargin();
        return inflate;
    }
    @Override
    public void initData() {
        super.initData();
    }

    private void findView() {
        rl_addaddress_header = (RelativeLayout) inflate.findViewById(R.id.rl_addaddress_header);
        iv_addaddress_back = (ImageView) inflate.findViewById(R.id.iv_addaddress_back);
        tv_addaddress_back = (TextView) inflate.findViewById(R.id.tv_addaddress_back);
        et_addaddress_name = (EditText) inflate.findViewById(R.id.et_addaddress_name);
        et_addaddress_telnum = (EditText) inflate.findViewById(R.id.et_addaddress_telnum);
        et_addaddress_detail = (EditText) inflate.findViewById(R.id.et_addaddress_detail);
        tv_addaddress_save = (TextView) inflate.findViewById(R.id.tv_addaddress_save);

        iv_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_save.setOnClickListener(new MyOnClickListener());
    }
    
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_addaddress_back:
                    if(((SecondPagerActivity)context).from.equals("dispatchpager")) {
                        ((SecondPagerActivity)context).switchPage(7);
                    }
                    break;
                case R.id.tv_addaddress_back:
                    if(((SecondPagerActivity)context).from.equals("dispatchpager")) {
                        ((SecondPagerActivity)context).switchPage(7);
                    }
                    break;
                case R.id.tv_addaddress_save:
                    if(et_addaddress_name.getText()== null || et_addaddress_name.getText().toString().length() == 0) {
                        Utils.MyToast(context,"Please type in you name");
                    }else if(et_addaddress_telnum == null || !Utils.isMobileNO(et_addaddress_telnum.getText().toString())) {
                        Utils.MyToast(context,"Please enter the phone number");
                    }else if(et_addaddress_detail.getText()== null|| et_addaddress_detail.getText().toString().length() == 0) {
                        Utils.MyToast(context,"Please enter the full address");
                    }else {

                        String url = MyApplication.url + "/v1/addresses/?timezone=" + MyApplication.utc;
                        String json = "{\"address\": \"" +et_addaddress_detail.getText().toString() +"\",\"name\": \""+ et_addaddress_name.getText().toString() +"\",\"phone\": \""+et_addaddress_telnum.getText().toString()+"\",\"zipcode\": "+ "\"\"" +"}";
                        Map map = new HashMap();
                        String mToken = Utils.getSpData("token", context);
                        map.put("Authorization", "Bearer " + mToken);
                        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
                            @Override
                            public void success(final String response) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("TAG_增加地址", response);
                                    }
                                });

                            }

                            @Override
                            public void error(final int code, final String message) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("TAG", code + message);
                                    }
                                });
                            }

                            @Override
                            public void failure(Exception exception) {

                            }
                        });
                    }
                    break;
            }
        }
    }
    
    //根据版本判断是否 需要设置据顶部状态栏高度
    @TargetApi(19)
    private void setHeadMargin() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();

        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 38));
        lp.topMargin = sbar;
        rl_addaddress_header.setLayoutParams(lp);

    }
}
