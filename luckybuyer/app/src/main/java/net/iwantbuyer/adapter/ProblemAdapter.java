package net.iwantbuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.iwantbuyer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangshuyu on 2017/4/5.
 */
public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ViewHolder> {
    private Context context;
    private List list;

    public ProblemAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_problem, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.rl_problem.setOnClickListener(new MyOnClickListener(holder));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyOnClickListener implements View.OnClickListener{
        private ViewHolder holder;
        public MyOnClickListener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_problem:
                    holder.tv_problem_discribe.setVisibility(View.VISIBLE);
                    rotateAnim(holder.iv_problem,180f);
                    break;
            }
        }
    }

    public void rotateAnim(ImageView imageview,float f) {
        Animation anim =new RotateAnimation(0f, f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        imageview.startAnimation(anim);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_problem;
        private TextView tv_problem;
        private ImageView iv_problem;
        private TextView tv_problem_discribe;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_problem = (RelativeLayout) itemView.findViewById(R.id.rl_problem);
            tv_problem = (TextView) itemView.findViewById(R.id.tv_problem);
            iv_problem = (ImageView) itemView.findViewById(R.id.iv_problem);
            tv_problem_discribe = (TextView) itemView.findViewById(R.id.tv_problem_discribe);
        }
    }
}
