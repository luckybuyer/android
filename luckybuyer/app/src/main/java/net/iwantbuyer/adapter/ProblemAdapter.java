package net.iwantbuyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import net.iwantbuyer.bean.Problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangshuyu on 2017/4/5.
 */
public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ViewHolder> {
    private Context context;
    private List<Problem.ProblemBean> list;

    public ProblemAdapter(Context context, List<Problem.ProblemBean> list) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("TAG", list.get(position).getQuestion());
        holder.tv_problem.setText(list.get(position).getQuestion() + "");
        holder.tv_problem_discribe.setText(list.get(position).getAnswer() + "");
        holder.rl_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tv_problem_discribe.getVisibility() == View.VISIBLE) {
                    holder.tv_problem_discribe.setVisibility(View.GONE);
                    rotateAnim(holder.iv_problem,0f);

                }else {
                    holder.tv_problem_discribe.setVisibility(View.VISIBLE);
                    rotateAnim(holder.iv_problem,180f);
                    Log.e("TAG..", position + "");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
