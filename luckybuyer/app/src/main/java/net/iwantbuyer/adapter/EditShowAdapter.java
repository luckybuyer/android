package net.iwantbuyer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.view.RoundCornerImageView;

import java.util.List;

/**
 * Created by admin on 2016/12/7.
 */
public class EditShowAdapter extends RecyclerView.Adapter<EditShowAdapter.MyViewHolder> {
    private Context context;
    private List list;

    public interface OnClickListener{
        void onclick(View view ,int position);
    }

    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    public interface OnClickDeleteListener{
        void onclick(View view ,int position);
    }

    private OnClickDeleteListener onClickDeleteListener;
    public void setOnClickDeleteListener(OnClickDeleteListener onClickDeleteListener){
        this.onClickDeleteListener = onClickDeleteListener;
    }


    public EditShowAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if(list.get(position) instanceof Bitmap) {
            type = 1;
        }else if(list.get(position) instanceof Drawable) {
            type = 2;
        }
        return type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_editshow_icon, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_editshow_add, null);
        }
        MyViewHolder viewHolder = new MyViewHolder(view,viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.e("TAG000", list.get(position) + "");
        if(getItemViewType(position) == 1) {
            setImage(holder.iv_editshow_icon,(Bitmap) list.get(position));
            holder.iv_editview_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDeleteListener.onclick(v,position);
                }
            });
        }else {
            holder.iv_editshow.setBackground((Drawable) list.get(position));
            holder.iv_editshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onclick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if(list.size() <= 3) {
            count = list.size();
        }else {
            count = 3;
        }
        return count;
    }

    private void setImage(ImageView imageView,Bitmap bitmap) {
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        // 从源图片中截取需要的部分
        if (picWidth > picHeight) {
            bitmap = Bitmap.createBitmap(bitmap, (picWidth - picHeight) / 2, 0, picHeight, picHeight);
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 0, (picHeight - picWidth) / 2, picWidth, picWidth);
        }

//        BitmapDrawable bitmapD = new BitmapDrawable(bitmap);
        imageView.setImageBitmap(bitmap);
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        //type 为 1的时候
        private RoundCornerImageView iv_editshow_icon;
        private ImageView iv_editview_delete;
        //type 为 2的时候
        private ImageView iv_editshow;
        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 1) {
                iv_editshow_icon = (RoundCornerImageView) itemView.findViewById(R.id.iv_editshow_icon);
                iv_editview_delete = (ImageView) itemView.findViewById(R.id.iv_editview_delete);
            }else {
                iv_editshow = (ImageView) itemView.findViewById(R.id.iv_editshow);
            }
        }
    }
}
