package net.iwantbuyer.secondpager;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.iwantbuyer.R;
import net.iwantbuyer.adapter.EditShowAdapter;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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
        Drawable d = ContextCompat.getDrawable(context,R.drawable.editshow_add);
        list.add(d);

        editShowAdapter = new EditShowAdapter(context,list);
        rv_editshow_icon.setAdapter(editShowAdapter);
        rv_editshow_icon.setLayoutManager(new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false));

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

                    break;
                case R.id.tv_editshow_send:

                    break;
                case R.id.tv_editshow_camera:
                    fromAlbum();
                    if(show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_editshow_photo:
                    camera();
                    if(show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
                case R.id.tv_editshow_cancel:
                    if(show != null && show.isShowing()) {
                        show.dismiss();
                    }
                    break;
            }
        }
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
            Utils.MyToast(context,"Confirm to insert the SD card");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();

                Bitmap bitmap = Utils.compressImageFromFile(Utils.getRealFilePath(context, uri));
                int position = list.size();
                list.add(list.size() - 1,bitmap);
                editShowAdapter.notifyDataSetChanged();
            }
        }else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            // 从相册返回的数据
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data"); //get bitmap
                bitmap = Utils.comp(bitmap);
                int position = list.size();
                list.add(list.size() - 1,bitmap);
                editShowAdapter.notifyDataSetChanged();
            }
        }
    }


    private AlertDialog show;
    private void startAlertDialog(){
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
}
