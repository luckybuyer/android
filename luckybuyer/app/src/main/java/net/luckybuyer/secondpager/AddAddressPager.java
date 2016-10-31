package net.luckybuyer.secondpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.luckybuyer.R;
import net.luckybuyer.activity.SecondPagerActivity;
import net.luckybuyer.adapter.AddAddressAdapter;
import net.luckybuyer.app.MyApplication;
import net.luckybuyer.base.BaseNoTrackPager;
import net.luckybuyer.utils.DensityUtil;
import net.luckybuyer.utils.HttpUtils;
import net.luckybuyer.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/8.
 */
public class AddAddressPager extends BaseNoTrackPager {
    private RelativeLayout rl_addaddress_header;
    private ImageView iv_addaddress_back;
    private TextView tv_addaddress_back;
    private TextView tv_addaddress_save;                          //保存地址
    private EditText et_addaddress_firstname;
    private EditText et_addaddress_lastname;
    private TextView tv_addaddress_country;                       //country
    private ImageView iv_addaddress_country;
    private ImageView iv_addaddress_city;                         //city
    private TextView tv_addaddress_city;
    private ImageView iv_addaddress_area;                         //area
    private TextView tv_addaddress_area;
    private EditText et_addaddress_street;                        //街道信息
    private EditText et_addaddress_build;                         //房屋号码

    private TextView tv_addaddress_business;                      //地址类型
    private TextView tv_addaddress_home;                          //地址类型

    private EditText et_addaddress_areacode;                     //区号
    private EditText et_addaddress_telnum;                       //电话号码

    private EditText et_addaddress_shippingnote;                 //装运通知单

    private View inflate;


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            setSaveState();
        }
    };

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_addaddress, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setHeadMargin();
        tv_addaddress_save.setEnabled(false);
        tv_addaddress_home.setHovered(true);
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
        tv_addaddress_save = (TextView) inflate.findViewById(R.id.tv_addaddress_save);
        et_addaddress_firstname = (EditText) inflate.findViewById(R.id.et_addaddress_firstname);
        et_addaddress_lastname = (EditText) inflate.findViewById(R.id.et_addaddress_lastname);
        tv_addaddress_country = (TextView) inflate.findViewById(R.id.tv_addaddress_country);
        iv_addaddress_country = (ImageView) inflate.findViewById(R.id.iv_addaddress_country);
        iv_addaddress_city = (ImageView) inflate.findViewById(R.id.iv_addaddress_city);
        tv_addaddress_city = (TextView) inflate.findViewById(R.id.tv_addaddress_city);
        iv_addaddress_area = (ImageView) inflate.findViewById(R.id.iv_addaddress_area);
        tv_addaddress_area = (TextView) inflate.findViewById(R.id.tv_addaddress_area);
        et_addaddress_street = (EditText) inflate.findViewById(R.id.et_addaddress_street);
        et_addaddress_build = (EditText) inflate.findViewById(R.id.et_addaddress_build);
        tv_addaddress_business = (TextView) inflate.findViewById(R.id.tv_addaddress_business);
        tv_addaddress_home = (TextView) inflate.findViewById(R.id.tv_addaddress_home);
        et_addaddress_areacode = (EditText) inflate.findViewById(R.id.et_addaddress_areacode);
        et_addaddress_telnum = (EditText) inflate.findViewById(R.id.et_addaddress_telnum);
        et_addaddress_shippingnote = (EditText) inflate.findViewById(R.id.et_addaddress_shippingnote);

        iv_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_save.setOnClickListener(new MyOnClickListener());
        tv_addaddress_business.setOnClickListener(new MyOnClickListener());
        tv_addaddress_home.setOnClickListener(new MyOnClickListener());

        tv_addaddress_country.setOnClickListener(new MyOnClickListener());
        iv_addaddress_country.setOnClickListener(new MyOnClickListener());
        iv_addaddress_city.setOnClickListener(new MyOnClickListener());
        tv_addaddress_city.setOnClickListener(new MyOnClickListener());
        iv_addaddress_area.setOnClickListener(new MyOnClickListener());
        tv_addaddress_area.setOnClickListener(new MyOnClickListener());


        et_addaddress_firstname.addTextChangedListener(watcher);
        et_addaddress_lastname.addTextChangedListener(watcher);
        et_addaddress_street.addTextChangedListener(watcher);
        et_addaddress_build.addTextChangedListener(watcher);
        et_addaddress_areacode.addTextChangedListener(watcher);
        et_addaddress_telnum.addTextChangedListener(watcher);

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_addaddress_back:
                    if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).switchPage(7);
                    } else if ("shippingaddress".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).switchPage(9);
                    }

                    break;
                case R.id.tv_addaddress_back:
                    if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).switchPage(7);
                    } else if ("shippingaddress".equals(((SecondPagerActivity) context).from)) {
                        ((SecondPagerActivity) context).switchPage(9);
                    }
                    break;
                case R.id.tv_addaddress_save:
                    startSave();
                    break;
                case R.id.tv_addaddress_business:
                    tv_addaddress_business.setHovered(true);
                    tv_addaddress_home.setHovered(false);
                    break;
                case R.id.tv_addaddress_home:
                    tv_addaddress_business.setHovered(false);
                    tv_addaddress_home.setHovered(true);
                    break;
                case R.id.iv_addaddress_country:
                    List list = new ArrayList();
                    for (int i = 0; i < 20; i++) {
                        list.add("nihao" + i);
                    }
                    AreaSelector(list, tv_addaddress_country);
                    break;
                case R.id.tv_addaddress_country:
                    list = new ArrayList();
                    for (int i = 0; i < 20; i++) {
                        list.add("nihao" + i);
                    }
                    AreaSelector(list, tv_addaddress_country);
                    break;
                case R.id.iv_addaddress_city:
                    list = new ArrayList();
                    for (int i = 0; i < 20; i++) {
                        list.add("nihao" + i);
                    }
                    AreaSelector(list, tv_addaddress_city);
                    break;
                case R.id.tv_addaddress_city:
                    list = new ArrayList();
                    for (int i = 0; i < 20; i++) {
                        list.add("nihao" + i);
                    }
                    AreaSelector(list, tv_addaddress_city);
                    break;
                case R.id.iv_addaddress_area:
                    list = new ArrayList();
                    for (int i = 0; i < 20; i++) {
                        list.add("nihao" + i);
                    }
                    AreaSelector(list, tv_addaddress_area);
                    break;
                case R.id.tv_addaddress_area:
                    list = new ArrayList();
                    for (int i = 0; i < 20; i++) {
                        list.add("nihao" + i);
                    }
                    AreaSelector(list, tv_addaddress_area);
                    break;
            }
        }
    }

    //保存地址
    private void startSave() {
        HttpUtils.getInstance().startNetworkWaiting(context);
        String url = MyApplication.url + "/v1/addresses/?timezone=" + MyApplication.utc;
        String json = "{\"address\": \""+tv_addaddress_country.getText() + tv_addaddress_city.getText() + tv_addaddress_area.getText() + et_addaddress_street.getText() + et_addaddress_build.getText() + "\",\"name\": \""+et_addaddress_lastname.getText() + " " + et_addaddress_lastname.getText()+"\",\"phone\": \""+"+" + et_addaddress_areacode.getText() + " " + et_addaddress_telnum.getText()+"\",\"zipcode\": \""+"123456"+"\"}";
        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
                            ((SecondPagerActivity) context).switchPage(7);
                        } else if ("shippingaddress".equals(((SecondPagerActivity) context).from)) {
                            ((SecondPagerActivity) context).switchPage(9);
                        }
                        Utils.MyToast(context,"Saved successfully");
                    }
                });

            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.getInstance().stopNetWorkWaiting();
                        Utils.MyToast(context,"Failed to save. Please try again");
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                HttpUtils.getInstance().stopNetWorkWaiting();
                Utils.MyToast(context,"Failed to save. Please try again");
            }
        });
    }


    private void AreaSelector(List list, final TextView textView) {
        RecyclerView recycleView = new RecyclerView(context);
        AddAddressAdapter addAddressAdapter = new AddAddressAdapter(context, list);
        recycleView.setAdapter(addAddressAdapter);
        recycleView.setLayoutManager(new GridLayoutManager(context, 1));
        addAddressAdapter.SetOnClickListener(new AddAddressAdapter.OnClickListener() {
            @Override
            public void OnClick(String area) {
                if (show.isShowing()) {
                    show.dismiss();
                }
                if (textView == tv_addaddress_country) {
                    tv_addaddress_city.setText("");
                    tv_addaddress_area.setText("");
                } else if (textView == tv_addaddress_area) {
                    tv_addaddress_area.setText("");
                }
                textView.setText(area);
                setSaveState();
            }
        });
        StartAlertDialog(recycleView);
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

    private void setSaveState() {
        if (et_addaddress_firstname.getText() != null && et_addaddress_firstname.getText().length() > 0 &&
                et_addaddress_lastname.getText() != null && et_addaddress_lastname.getText().length() > 0 &&
                tv_addaddress_country.getText() != null && tv_addaddress_country.length() > 0 &&
                tv_addaddress_city.getText() != null && tv_addaddress_city.getText().length() > 0 &&
                tv_addaddress_area.getText() != null && tv_addaddress_area.getText().length() > 0 &&
                et_addaddress_build.getText() != null && et_addaddress_build.getText().length() > 0 &&
                et_addaddress_areacode.getText() != null && et_addaddress_areacode.getText().length() > 0 &&
                et_addaddress_telnum.getText() != null && et_addaddress_telnum.getText().length() > 0) {

            tv_addaddress_save.setEnabled(true);

        }else {
            tv_addaddress_save.setEnabled(false);
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
