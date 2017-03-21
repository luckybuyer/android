package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.appsflyer.AppsFlyerLib;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.adapter.AddAddressAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.base.BasePager;
import net.iwantbuyer.bean.Address;
import net.iwantbuyer.utils.DensityUtil;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.SoftKeyboardUtil;
import net.iwantbuyer.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/8.
 */
public class AddAddressPager extends BaseNoTrackPager {
    private RelativeLayout rl_addaddress_header;
    private TextView tv_addaddress_save;                          //保存地址
    private LinearLayout ll_addaddress_back;

    //名字
    private EditText et_addaddress_name;
    private TextView tv_addaddress_name;

    //地址
    private EditText et_addaddress_address;
    private TextView tv_addaddress_address;
    private TextView tv_addaddress_topaddress;

    //编码
    private EditText et_addaddress_postcode;
    private TextView tv_addaddress_postcode;
    private TextView tv_addaddress_toppostcode;

    //城市
    private EditText et_addaddress_city;
    private TextView tv_addaddress_topcity;
    private TextView tv_addaddress_city;

    //省
    private EditText et_addaddress_province;
    private TextView tv_addaddress_topprovince;
    private TextView tv_addaddress_province;

    //电话号码
    private EditText et_addaddress_mobile;
    private TextView tv_addaddress_topmobile;
    private TextView tv_addaddress_mobile;

    private View view_addaddress_name;
    private View view_addaddress_address;
    private View view_addaddress_postcode;
    private View view_addaddress_city;
    private View view_addaddress_province;
    private View view_addaddress_mobile;

    private TextView tv_title;
    private ScrollView sv_addaddress;
    private RelativeLayout rl_addaddress;

    private View inflate;
    private int address_id;
    private PopupWindow mPopupWindow;
    ;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_addaddress, null);
        address_id = ((SecondPagerActivity) context).address_id;
        findView();
//        setHeadMargin();

        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void findView() {
        rl_addaddress_header = (RelativeLayout) inflate.findViewById(R.id.rl_addaddress_header);
        ll_addaddress_back = (LinearLayout) inflate.findViewById(R.id.ll_addaddress_back);
        tv_addaddress_save = (TextView) inflate.findViewById(R.id.tv_addaddress_save);

        et_addaddress_name = (EditText) inflate.findViewById(R.id.et_addaddress_name);
        tv_addaddress_name = (TextView) inflate.findViewById(R.id.tv_addaddress_name);
        et_addaddress_address = (EditText) inflate.findViewById(R.id.et_addaddress_address);
        tv_addaddress_address = (TextView) inflate.findViewById(R.id.tv_addaddress_address);
        tv_addaddress_topaddress = (TextView) inflate.findViewById(R.id.tv_addaddress_topaddress);
        et_addaddress_postcode = (EditText) inflate.findViewById(R.id.et_addaddress_postcode);
        tv_addaddress_postcode = (TextView) inflate.findViewById(R.id.tv_addaddress_postcode);
        tv_addaddress_toppostcode = (TextView) inflate.findViewById(R.id.tv_addaddress_toppostcode);
        et_addaddress_city = (EditText) inflate.findViewById(R.id.et_addaddress_city);
        tv_addaddress_topcity = (TextView) inflate.findViewById(R.id.tv_addaddress_topcity);
        tv_addaddress_city = (TextView) inflate.findViewById(R.id.tv_addaddress_city);
        et_addaddress_province = (EditText) inflate.findViewById(R.id.et_addaddress_province);
        tv_addaddress_topprovince = (TextView) inflate.findViewById(R.id.tv_addaddress_topprovince);
        tv_addaddress_province = (TextView) inflate.findViewById(R.id.tv_addaddress_province);
        et_addaddress_mobile = (EditText) inflate.findViewById(R.id.et_addaddress_mobile);
        tv_addaddress_topmobile = (TextView) inflate.findViewById(R.id.tv_addaddress_topmobile);
        tv_addaddress_mobile = (TextView) inflate.findViewById(R.id.tv_addaddress_mobile);

        view_addaddress_name = (View) inflate.findViewById(R.id.view_addaddress_name);
        view_addaddress_address = (View) inflate.findViewById(R.id.view_addaddress_address);
        view_addaddress_postcode = (View) inflate.findViewById(R.id.view_addaddress_postcode);
        view_addaddress_city = (View) inflate.findViewById(R.id.view_addaddress_city);
        view_addaddress_province = (View) inflate.findViewById(R.id.view_addaddress_province);
        view_addaddress_mobile = (View) inflate.findViewById(R.id.view_addaddress_mobile);


        tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        sv_addaddress = (ScrollView) inflate.findViewById(R.id.sv_addaddress);
        rl_addaddress = (RelativeLayout) inflate.findViewById(R.id.rl_addaddress);


        ll_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_save.setOnClickListener(new MyOnClickListener());


        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


        et_addaddress_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    et_addaddress_name.setHint(context.getString(R.string.Name));
                    Log.e("TAG", "丢失焦点");
                } else {
                    et_addaddress_name.setHint("");
                    Log.e("TAG", "获取焦点");
                }
            }
        });
        et_addaddress_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    et_addaddress_address.setHint(context.getString(R.string.EnterAddress));
                } else {
                    et_addaddress_address.setHint("");
                    Log.e("TAG", "area 获取焦点");
                }
            }
        });


        //firstname 的长度监听
        et_addaddress_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_addaddress_name.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_addaddress_name.setVisibility(View.VISIBLE);
                } else {
                    tv_addaddress_name.setVisibility(View.GONE);
                }
            }
        });
        //地址 的长度监听
        et_addaddress_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_addaddress_topaddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_addaddress_topaddress.setVisibility(View.VISIBLE);
                } else {
                    tv_addaddress_topaddress.setVisibility(View.GONE);
                }
            }
        });
        //country 的长度监听
        et_addaddress_postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_addaddress_toppostcode.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_addaddress_toppostcode.setVisibility(View.VISIBLE);
                } else {
                    tv_addaddress_toppostcode.setVisibility(View.GONE);
                }
            }
        });

        //city 的长度监听
        et_addaddress_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_addaddress_topcity.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_addaddress_topcity.setVisibility(View.VISIBLE);
                } else {
                    tv_addaddress_topcity.setVisibility(View.GONE);
                }
            }
        });
        //province 的长度监听
        et_addaddress_province.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_addaddress_topprovince.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_addaddress_topprovince.setVisibility(View.VISIBLE);
                } else {
                    tv_addaddress_topprovince.setVisibility(View.GONE);
                }
            }
        });

        //mobile 的长度监听
        et_addaddress_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_addaddress_topmobile.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tv_addaddress_topmobile.setVisibility(View.VISIBLE);
                } else {
                    tv_addaddress_topmobile.setVisibility(View.GONE);
                }
            }
        });


        if (address_id != -1) {
            tv_title.setText(context.getString(R.string.Editaddress));
        } else {
            tv_title.setText(context.getString(R.string.Addaddress));
        }


    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_addaddress_back:
//                    if ("dispatchpager".equals(((SecondPagerActivity) context).from)) {
//                        ((SecondPagerActivity) context).switchPage(7);
//                    } else if ("shippingaddress".equals(((SecondPagerActivity) context).from)) {
//                        ((SecondPagerActivity) context).switchPage(9);
//                    }

                    ((SecondPagerActivity) context).fragmentManager.popBackStack();
                    break;
                case R.id.tv_addaddress_save:
                    selectSave();
//                    startSaveOrEdit();
                    break;
            }
        }
    }

    //检测是否可以保存
    private void selectSave() {
        int count = 0;
        switch (-1) {
            default:
            case R.id.et_addaddress_name:
                if (et_addaddress_name.getText() != null && et_addaddress_name.getText().length() > 0) {
                    count++;
                    view_addaddress_name.setEnabled(true);
                } else {
                    view_addaddress_name.setEnabled(false);
                }
            case R.id.et_addaddress_address:
                if (et_addaddress_address.getText() != null && et_addaddress_address.getText().length() > 0) {
                    count++;
                    view_addaddress_address.setEnabled(true);
                    tv_addaddress_address.setVisibility(View.GONE);
                } else {
                    view_addaddress_address.setEnabled(false);
                    tv_addaddress_address.setVisibility(View.VISIBLE);
                }
            case R.id.et_addaddress_postcode:
                if (et_addaddress_postcode.getText() != null && et_addaddress_postcode.getText().length() > 0) {
                    count++;
                    view_addaddress_postcode.setEnabled(true);
                    tv_addaddress_postcode.setVisibility(View.GONE);
                } else {
                    view_addaddress_postcode.setEnabled(false);
                    tv_addaddress_postcode.setVisibility(View.VISIBLE);
                }
            case R.id.et_addaddress_city:
                if (et_addaddress_city.getText() != null && et_addaddress_city.getText().length() > 0) {
                    count++;
                    view_addaddress_city.setEnabled(true);
                    tv_addaddress_city.setVisibility(View.GONE);
                } else {
                    view_addaddress_city.setEnabled(false);
                    tv_addaddress_city.setVisibility(View.VISIBLE);
                }

            case R.id.et_addaddress_province:
                if (et_addaddress_province.getText() != null && et_addaddress_province.getText().length() > 0) {
                    count++;
                    view_addaddress_province.setEnabled(true);
                    tv_addaddress_province.setVisibility(View.GONE);
                } else {
                    view_addaddress_province.setEnabled(false);
                    tv_addaddress_province.setVisibility(View.VISIBLE);
                }
            case R.id.et_addaddress_mobile:
                if (et_addaddress_mobile.getText() != null && et_addaddress_mobile.getText().length() > 0) {
                    count++;
                    view_addaddress_mobile.setEnabled(true);
                    tv_addaddress_mobile.setVisibility(View.GONE);
                } else {
                    view_addaddress_mobile.setEnabled(false);
                    tv_addaddress_mobile.setVisibility(View.VISIBLE);
                }
                break;
        }
        if (count == 6) {
            startSaveOrEdit();
        } else if (count <6) {
            Utils.MyToast(context, context.getString(R.string.completeinformation));
        }
    }

    //保存地址
    private void startSaveOrEdit() {
        if (address_id != -1) {
            HttpUtils.getInstance().startNetworkWaiting(context);
            String url = MyApplication.url + "/v1/addresses/" + address_id + "?timezone=" + MyApplication.utc;
            String addre = et_addaddress_address.getText() + "," + et_addaddress_province.getText() + "," + et_addaddress_city.getText();
            Address address = new Address();
            address.setAddress(addre);
            address.setName(et_addaddress_name.getText() + "");
            address.setPhone(et_addaddress_mobile.getText() + "");
            address.setZipcode(et_addaddress_postcode.getText().toString() + "");
            String json = address.toString();
            Log.e("TAG_address", json);
            Map map = new HashMap();
            String mToken = Utils.getSpData("token", context);
            map.put("Authorization", "Bearer " + mToken);
            map.put("LK-APPSFLYER-ID", AppsFlyerLib.getInstance().getAppsFlyerUID(context) + "");
            HttpUtils.getInstance().putJson(url, json, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(final String response) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            ((SecondPagerActivity) context).fragmentManager.popBackStack();
                            Utils.MyToast(context, context.getString(R.string.setsuccess));
                        }
                    });

                }

                @Override
                public void error(final int code, final String message) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("TAG", code + message);
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            Utils.MyToast(context, context.getString(R.string.Networkfailure) + code + "address");
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    HttpUtils.getInstance().stopNetWorkWaiting();
                    Utils.MyToast(context, context.getString(R.string.Networkfailure));
                }
            });
        } else {
            HttpUtils.getInstance().startNetworkWaiting(context);
            String url = MyApplication.url + "/v1/addresses/?timezone=" + MyApplication.utc;
            String addre = et_addaddress_address.getText() + "," + et_addaddress_province.getText() + "," + et_addaddress_city.getText();
            Address address = new Address();
            address.setAddress(addre);
            address.setName(et_addaddress_name.getText() + "");
            address.setPhone(et_addaddress_mobile.getText() + "");
            address.setZipcode(et_addaddress_postcode.getText().toString() + "");
            String json = address.toString();
            Log.e("TAG_address", json);
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
                            ((SecondPagerActivity) context).fragmentManager.popBackStack();
                            Utils.MyToast(context, context.getString(R.string.setsuccess));
                        }
                    });

                }

                @Override
                public void error(final int code, final String message) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            Log.e("TAG", code + message);
                            Utils.MyToast(context, context.getString(R.string.Networkfailure) + code + "address");
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    HttpUtils.getInstance().stopNetWorkWaiting();
                    Utils.MyToast(context, context.getString(R.string.Networkfailure));
                }
            });
        }
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
//                if (textView == tv_addaddress_country) {
//                    tv_addaddress_city.setText("");
//                    et_addaddress_area.setText("");
//                }
                textView.setText(area);
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
        show.getWindow().setLayout(3 * screenWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
