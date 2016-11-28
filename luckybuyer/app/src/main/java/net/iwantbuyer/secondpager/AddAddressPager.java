package net.iwantbuyer.secondpager;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.AddAddressAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
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
    private ImageView iv_addaddress_back;
    private TextView tv_addaddress_back;
    private TextView tv_addaddress_save;                          //保存地址

    private RelativeLayout rl_addaddress_locationtype;
    private EditText et_addaddress_firstname;
    private EditText et_addaddress_lastname;
    private EditText et_addaddress_country;
    private EditText et_addaddress_city;
    private EditText et_addaddress_area;                          //area
    private EditText et_addaddress_street;                        //街道信息
    private EditText et_addaddress_build;                         //房屋号码
    private EditText et_addaddress_shippingnote;                 //装运通知单
    private EditText et_addaddress_mobile;

    private TextView tv_addaddress_locationtype;
    private TextView tv_addaddress_firstname;
    private TextView tv_addaddress_lastname;
    private TextView tv_addaddress_country;
    private TextView tv_addaddress_city;
    private TextView tv_addaddress_area;
    private TextView tv_addaddress_street;
    private TextView tv_addaddress_build;
    private TextView tv_addaddress_type;
    private TextView tv_addaddress_mobile;

    private View view_addaddress_firstname;
    private View view_addaddress_lastname;
    private View view_addaddress_country;
    private View view_addaddress_city;
    private View view_addaddress_area;
    private View view_addaddress_street;
    private View view_addaddress_build;
    private View view_addaddress_locationtype;
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
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        address_id = ((SecondPagerActivity) context).address_id;
        findView();
//        setHeadMargin();
        et_addaddress_lastname.clearFocus();

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
        et_addaddress_country = (EditText) inflate.findViewById(R.id.et_addaddress_country);
        et_addaddress_city = (EditText) inflate.findViewById(R.id.et_addaddress_city);
        et_addaddress_area = (EditText) inflate.findViewById(R.id.et_addaddress_area);
        et_addaddress_street = (EditText) inflate.findViewById(R.id.et_addaddress_street);
        et_addaddress_build = (EditText) inflate.findViewById(R.id.et_addaddress_build);
        et_addaddress_shippingnote = (EditText) inflate.findViewById(R.id.et_addaddress_shippingnote);
        et_addaddress_mobile = (EditText) inflate.findViewById(R.id.et_addaddress_mobile);

        rl_addaddress_locationtype = (RelativeLayout) inflate.findViewById(R.id.rl_addaddress_locationtype);
        tv_addaddress_locationtype = (TextView) inflate.findViewById(R.id.tv_addaddress_locationtype);
        tv_addaddress_firstname = (TextView) inflate.findViewById(R.id.tv_addaddress_firstname);
        tv_addaddress_lastname = (TextView) inflate.findViewById(R.id.tv_addaddress_lastname);
        tv_addaddress_country = (TextView) inflate.findViewById(R.id.tv_addaddress_country);
        tv_addaddress_city = (TextView) inflate.findViewById(R.id.tv_addaddress_city);
        tv_addaddress_area = (TextView) inflate.findViewById(R.id.tv_addaddress_area);
        tv_addaddress_street = (TextView) inflate.findViewById(R.id.tv_addaddress_street);
        tv_addaddress_build = (TextView) inflate.findViewById(R.id.tv_addaddress_build);
        tv_addaddress_type = (TextView) inflate.findViewById(R.id.tv_addaddress_type);
        tv_addaddress_mobile = (TextView) inflate.findViewById(R.id.tv_addaddress_mobile);


        view_addaddress_firstname = (View) inflate.findViewById(R.id.view_addaddress_firstname);
        view_addaddress_lastname = (View) inflate.findViewById(R.id.view_addaddress_lastname);
        view_addaddress_country = (View) inflate.findViewById(R.id.view_addaddress_country);
        view_addaddress_city = (View) inflate.findViewById(R.id.view_addaddress_city);
        view_addaddress_area = (View) inflate.findViewById(R.id.view_addaddress_area);
        view_addaddress_street = (View) inflate.findViewById(R.id.view_addaddress_street);
        view_addaddress_build = (View) inflate.findViewById(R.id.view_addaddress_build);
        view_addaddress_locationtype = (View) inflate.findViewById(R.id.view_addaddress_locationtype);
        view_addaddress_mobile = (View) inflate.findViewById(R.id.view_addaddress_mobile);


        tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        sv_addaddress = (ScrollView) inflate.findViewById(R.id.sv_addaddress);
        rl_addaddress = (RelativeLayout) inflate.findViewById(R.id.rl_addaddress);


        iv_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_back.setOnClickListener(new MyOnClickListener());
        tv_addaddress_save.setOnClickListener(new MyOnClickListener());
        rl_addaddress_locationtype.setOnClickListener(new MyOnClickListener());


        et_addaddress_firstname.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        et_addaddress_lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    et_addaddress_lastname.setHint("Type in last name");
                } else {
                    et_addaddress_lastname.setHint("");
                }
            }
        });

        et_addaddress_firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    et_addaddress_firstname.setHint("Type in first name");
                    Log.e("TAG", "丢失焦点");
                } else {
                    et_addaddress_firstname.setHint("");
                    Log.e("TAG", "获取焦点");
                }
            }
        });
        et_addaddress_area.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    et_addaddress_area.setHint("Type in area");
                } else {
                    et_addaddress_area.setHint("");
                    Log.e("TAG", "area 获取焦点");
                }
            }
        });

        if (address_id != -1) {
            tv_title.setText("Edit Address");
        } else {
            tv_title.setText("Add Address");
        }

        //软键盘的监听
//        SoftKeyboardUtil.observeSoftKeyboard((SecondPagerActivity) context, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
//            @Override
//            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
//                if (visible) {
//                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    lp.bottomMargin = softKeybardHeight - 80;
//                    lp.topMargin = DensityUtil.dip2px(context, 73);
//                    sv_addaddress.setLayoutParams(lp);
//                } else {
//                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    lp.topMargin = DensityUtil.dip2px(context, 73);
//                    lp.bottomMargin = 0;
//                    sv_addaddress.setLayoutParams(lp);
//
//                    et_addaddress_firstname.clearFocus();
//                    et_addaddress_lastname.clearFocus();
//                    et_addaddress_area.clearFocus();
//                    et_addaddress_firstname.setHint("Type in first name");
//                    et_addaddress_lastname.setHint("Type in last name");
//                    et_addaddress_area.setHint("Type in last area");
//                }
//
//            }
//        });

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
                    selectSave();
//                    startSaveOrEdit();
                    break;
                case R.id.rl_addaddress_locationtype:
                    startLocationType();
                    break;
                case R.id.tv_addaddress_home:
                    tv_addaddress_locationtype.setText("Home");
                    if(mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    break;
                case R.id.tv_addaddress_business:
                    tv_addaddress_locationtype.setText("Business");
                    if(mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    break;
            }
        }
    }

    //选择locationtype
    private void startLocationType() {
        int width = Utils.getScreenWidth(context);
        int height = Utils.getScreenHeight(context);
        View view = View.inflate(context,R.layout.ppw_locationtype,null);
        TextView tv_addaddress_home = (TextView) view.findViewById(R.id.tv_addaddress_home);
        TextView tv_addaddress_business = (TextView) view.findViewById(R.id.tv_addaddress_business);

        tv_addaddress_home.setOnClickListener(new MyOnClickListener());
        tv_addaddress_business.setOnClickListener(new MyOnClickListener());

        mPopupWindow = new PopupWindow(view, width, 13*height/80);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOutsideTouchable(false);

        int[] location = new int[2];
        view_addaddress_locationtype.getLocationOnScreen(location);
        if(height - location[1] > 13*height/80) {
            mPopupWindow.showAsDropDown(view_addaddress_locationtype,0,-13*height/80);
        }else {
            mPopupWindow.showAsDropDown(view_addaddress_locationtype);
        }


        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = ((SecondPagerActivity)context).getWindow().getAttributes();
        lp.alpha = 0.6f;
        ((SecondPagerActivity)context).getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((SecondPagerActivity)context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((SecondPagerActivity)context).getWindow().setAttributes(lp);
            }
        });

    }

    //检测是否可以保存
    private void selectSave() {
        int count = 0;
        switch (-1) {
            default:
            case R.id.et_addaddress_firstname:
                if (et_addaddress_firstname.getText() != null && et_addaddress_firstname.getText().length() > 0) {
                    count++;
                    view_addaddress_firstname.setEnabled(true);
                } else {
                    view_addaddress_firstname.setEnabled(false);
                }
            case R.id.et_addaddress_lastname:
                if (et_addaddress_lastname.getText() != null && et_addaddress_lastname.getText().length() > 0) {
                    count++;
                    view_addaddress_lastname.setEnabled(true);
                } else {
                    view_addaddress_lastname.setEnabled(false);
                }
            case R.id.et_addaddress_country:
                if (et_addaddress_country.getText() != null && et_addaddress_country.getText().length() > 0) {
                    count++;
                    view_addaddress_country.setEnabled(true);
                    tv_addaddress_country.setVisibility(View.GONE);
                } else {
                    view_addaddress_country.setEnabled(false);
                    tv_addaddress_country.setVisibility(View.VISIBLE);
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
            case R.id.et_addaddress_area:
                if (et_addaddress_area.getText() != null && et_addaddress_area.getText().length() > 0) {
                    count++;
                    view_addaddress_area.setEnabled(true);
                    tv_addaddress_area.setVisibility(View.GONE);
                } else {
                    view_addaddress_area.setEnabled(false);
                    tv_addaddress_area.setVisibility(View.VISIBLE);
                }
            case R.id.et_addaddress_street:
                if (et_addaddress_street.getText() != null && et_addaddress_street.getText().length() > 0) {
                    count++;
                    view_addaddress_street.setEnabled(true);
                    tv_addaddress_street.setVisibility(View.GONE);
                } else {
                    view_addaddress_street.setEnabled(false);
                    tv_addaddress_street.setVisibility(View.VISIBLE);
                }
            case R.id.et_addaddress_build:
                if (et_addaddress_build.getText() != null && et_addaddress_build.getText().length() > 0) {
                    count++;
                    view_addaddress_build.setEnabled(true);
                    tv_addaddress_build.setVisibility(View.GONE);
                } else {
                    view_addaddress_build.setEnabled(false);
                    tv_addaddress_build.setVisibility(View.VISIBLE);
                }
            case R.id.tv_addaddress_locationtype:
                if (tv_addaddress_locationtype.getText() != null && tv_addaddress_locationtype.getText().length() > 0) {
                    count++;
                    view_addaddress_locationtype.setEnabled(true);
                    tv_addaddress_type.setVisibility(View.GONE);
                } else {
                    view_addaddress_build.setEnabled(false);
                    tv_addaddress_type.setVisibility(View.VISIBLE);
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

        if (count == 9) {
            startSaveOrEdit();
        } else {

        }
    }

    //保存地址
    private void startSaveOrEdit() {
        if (address_id != -1) {
            HttpUtils.getInstance().startNetworkWaiting(context);
            String url = MyApplication.url + "/v1/addresses/" + address_id + "?timezone=" + MyApplication.utc;
            String json = "{\"address\": \"" + et_addaddress_country.getText() + et_addaddress_city.getText() + et_addaddress_area.getText() + et_addaddress_street.getText() + et_addaddress_build.getText() + "\",\"name\": \"" + et_addaddress_lastname.getText() + " " + et_addaddress_lastname.getText() + "\",\"phone\": \"" + "+" + et_addaddress_mobile.getText() + "\",\"zipcode\": \"" + "123456" + "\"}";
            Map map = new HashMap();
            String mToken = Utils.getSpData("token", context);
            map.put("Authorization", "Bearer " + mToken);
            HttpUtils.getInstance().putJson(url, json, map, new HttpUtils.OnRequestListener() {
                @Override
                public void success(final String response) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            ((SecondPagerActivity) context).switchPage(9);
                            Utils.MyToast(context, "Successfully modified");
                        }
                    });

                }

                @Override
                public void error(final int code, final String message) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.getInstance().stopNetWorkWaiting();
                            Utils.MyToast(context, "Fail to edit");
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    HttpUtils.getInstance().stopNetWorkWaiting();
                    Utils.MyToast(context, "Fail to edit");
                }
            });
        } else {
            HttpUtils.getInstance().startNetworkWaiting(context);
            String url = MyApplication.url + "/v1/addresses/?timezone=" + MyApplication.utc;
            String json = "{\"address\": \"" + et_addaddress_country.getText() + et_addaddress_city.getText() + et_addaddress_area.getText() + et_addaddress_street.getText() + et_addaddress_build.getText() + "\",\"name\": \"" + et_addaddress_lastname.getText() + " " + et_addaddress_lastname.getText() + "\",\"phone\": \"" + "+" + et_addaddress_mobile.getText() + "\",\"zipcode\": \"" + "123456" + "\"}";
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
                            Utils.MyToast(context, "Saved successfully");
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
                            Utils.MyToast(context, "The network connection failed. Please try again");
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    HttpUtils.getInstance().stopNetWorkWaiting();
                    Utils.MyToast(context, "The network connection failed. Please try again");
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
        show.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


}
