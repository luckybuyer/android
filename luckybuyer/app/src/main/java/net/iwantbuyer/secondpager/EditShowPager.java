package net.iwantbuyer.secondpager;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.adapter.EditShowAdapter;
import net.iwantbuyer.app.MyApplication;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.utils.HttpUtils;
import net.iwantbuyer.utils.MyBase64;
import net.iwantbuyer.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/6.
 */
public class EditShowPager extends BaseNoTrackPager {

    private LinearLayout ll_editshow_back;
    private TextView tv_editshow_send;
    private EditText et_editshow_discribe;
    private RecyclerView rv_editshow_icon;
    //
    private View inflate;

    private List list;
    private EditShowAdapter editShowAdapter;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_edit_show, null);
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList();
        Drawable d = ContextCompat.getDrawable(context, R.drawable.editshow_add);
        list.add(d);

        editShowAdapter = new EditShowAdapter(context, list);
        rv_editshow_icon.setAdapter(editShowAdapter);
        rv_editshow_icon.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));

        editShowAdapter.setOnClickListener(new EditShowAdapter.OnClickListener() {
            @Override
            public void onclick(View view, int position) {
                startAlertDialog();
            }
        });

        editShowAdapter.setOnClickDeleteListener(new EditShowAdapter.OnClickDeleteListener() {
            @Override
            public void onclick(View view, int position) {
                list.remove(position);
                editShowAdapter.notifyDataSetChanged();
            }
        });

    }

    private void findView() {
        ll_editshow_back = (LinearLayout) inflate.findViewById(R.id.ll_editshow_back);
        tv_editshow_send = (TextView) inflate.findViewById(R.id.tv_editshow_send);
        et_editshow_discribe = (EditText) inflate.findViewById(R.id.et_editshow_discribe);
        rv_editshow_icon = (RecyclerView) inflate.findViewById(R.id.rv_editshow_icon);

        ll_editshow_back.setOnClickListener(new MyOnClickListener());
        tv_editshow_send.setOnClickListener(new MyOnClickListener());

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_editshow_back:
                    ((SecondPagerActivity)context).switchPage(7);
                    break;
                case R.id.tv_editshow_send:
                    if (et_editshow_discribe.getText() != null && et_editshow_discribe.getText().length() == 0) {
                        Utils.MyToast(context, "PLease input content");
                    } else if (list.size() < 2) {
                        Utils.MyToast(context, "Upload at least one image");
                    } else {
                        sendShow();
                    }

                    break;
                case R.id.tv_editshow_camera:
                    camera();
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_editshow_photo:
                    fromAlbum();
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_editshow_cancel:
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_editshow_success_ok:
                    if (show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    ((SecondPagerActivity) context).switchPage(5);
                    break;
                case R.id.tv_editshow_success_cancel:
                    Log.e("TAG_quxiao", "什么鬼");
                    Utils.setSpData("main_pager", "show", context);
                    ((SecondPagerActivity) context).finish();
                    break;
            }
        }
    }

    private void sendShow() {
        startPPW();
        String url = MyApplication.url + "/v1/posts/?timezone=" + MyApplication.utc;
        String json = "";
        if (list.size() == 2) {
            json = "{\"content\": \"" + et_editshow_discribe.getText().toString() + "\",\"images\": [\"" + MyBase64.bitmapToBase64((Bitmap) list.get(0)) + "\"],\"order_id\": " + ((SecondPagerActivity) context).order_id + "}";
        } else if (list.size() == 3) {
            json = "{\"content\": \"" + et_editshow_discribe.getText().toString() + "\",\"images\": [\"" + MyBase64.bitmapToBase64((Bitmap) list.get(0)) + "\",\"" + MyBase64.bitmapToBase64((Bitmap) list.get(1)) + "\"],\"order_id\": " + ((SecondPagerActivity) context).order_id + "}";
        } else if (list.size() == 4) {
            json = "{\"content\": \"" + et_editshow_discribe.getText().toString() + "\",\"images\": [\"" + MyBase64.bitmapToBase64((Bitmap) list.get(0)) + "\",\"" + MyBase64.bitmapToBase64((Bitmap) list.get(1)) + "\",\"" + MyBase64.bitmapToBase64((Bitmap) list.get(2)) + "\"],\"order_id\": " + ((SecondPagerActivity) context).order_id + "}";
        }
        Log.e("TAG", json.substring(0, json.length() / 2));
        Log.e("TAG", json.substring(9 * json.length() / 10, json.length()));

        Map map = new HashMap();
        String mToken = Utils.getSpData("token", context);
        map.put("Authorization", "Bearer " + mToken);
        HttpUtils.getInstance().postJson(url, json, map, new HttpUtils.OnRequestListener() {
            @Override
            public void success(final String response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", response);
                        startSuccessAlertDialog();
                        if(mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                    }
                });

            }

            @Override
            public void error(final int code, final String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", code + message);
                        Utils.MyToast(context,"upload failed");
                        if(mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                    }
                });
            }

            @Override
            public void failure(Exception exception) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.MyToast(context,"upload failed");
                        if(mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                    }
                });
            }
        });
    }


    //从相册获取
    int PHOTO_REQUEST_GALLERY = 1;

    private void fromAlbum() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    int REQUEST_CODE_CAPTURE_CAMEIA = 2;

    public void camera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            Utils.MyToast(context, "Confirm to insert the SD card");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data == null) {
                return;
            }
            // 得到图片的全路径
            Uri uri = data.getData();
            Log.e("TAG_uri", uri + "");

            Bitmap bitmap = Utils.compressImageFromFile(Utils.getRealFilePath(context, uri));
            int position = list.size();
            list.add(list.size() - 1, bitmap);
            editShowAdapter.notifyDataSetChanged();

        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            // 从相册返回的数据
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap bitmap = (Bitmap) bundle.get("data"); //get bitmap
                    bitmap = Utils.comp(bitmap);
                    int position = list.size();
                    list.add(list.size() - 1, bitmap);
                    editShowAdapter.notifyDataSetChanged();
                }

            }
        }
    }


    private AlertDialog show;

    private void startAlertDialog() {
        View inflate = View.inflate(context, R.layout.alertdialog_editshow, null);
        TextView tv_editshow_camera = (TextView) inflate.findViewById(R.id.tv_editshow_camera);
        TextView tv_editshow_photo = (TextView) inflate.findViewById(R.id.tv_editshow_photo);
        TextView tv_editshow_cancel = (TextView) inflate.findViewById(R.id.tv_editshow_cancel);

        tv_editshow_camera.setOnClickListener(new MyOnClickListener());
        tv_editshow_photo.setOnClickListener(new MyOnClickListener());
        tv_editshow_cancel.setOnClickListener(new MyOnClickListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show = builder.show();
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = show.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.5f;
        window.setAttributes(lp);

        ObjectAnimator.ofFloat(inflate, "alpha", 0, 1).setDuration(400).start();
    }

    private void startSuccessAlertDialog() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View inflate = View.inflate(context, R.layout.alertdialog_show_success, null);
        TextView tv_editshow_success_cancel = (TextView) inflate.findViewById(R.id.tv_editshow_success_cancel);
        TextView tv_editshow_success_ok = (TextView) inflate.findViewById(R.id.tv_editshow_success_ok);

        tv_editshow_success_ok.setOnClickListener(new MyOnClickListener());
        tv_editshow_success_cancel.setOnClickListener(new MyOnClickListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflate);
        show = builder.show();
        show.getWindow().setLayout(3 * screenWidth / 4, 2 * screenHeight / 5);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        show.setCancelable(false);                                                     //点击哪里 都不消失
        Window window = show.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.5f;
        window.setAttributes(lp);
        ObjectAnimator.ofFloat(inflate, "alpha", 0, 1).setDuration(400).start();
    }

    public PopupWindow mPopupWindow;
    public void startPPW() {
        //得到屏幕的 尺寸 动态设置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        View view = View.inflate(context,R.layout.alertdialog_show_send,null);
        mPopupWindow = new PopupWindow(view, screenWidth*2/5, screenHeight/8);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOutsideTouchable(false);

        mPopupWindow.showAtLocation(tv_editshow_send, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("EditShowPager");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("EditShowPager");
    }
}
