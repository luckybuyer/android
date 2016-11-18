package net.smartbuyer.secondpager;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.smartbuyer.R;
import net.smartbuyer.activity.SecondPagerActivity;
import net.smartbuyer.base.BasePager;
import net.smartbuyer.utils.Utils;

/**
 * Created by admin on 2016/9/18.
 */
public class SetPager extends BasePager {
    private RelativeLayout rl_set_header;
    private ImageView iv_set_back;
    private TextView tv_set_back;
    private RelativeLayout rl_set_address;
        private RelativeLayout rl_set_email;
//    private RelativeLayout rl_set_us;
    private TextView tv_set_login;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_set, null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        ((SecondPagerActivity) context).from = "";
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void findView() {
        rl_set_header = (RelativeLayout) inflate.findViewById(R.id.rl_set_header);
        iv_set_back = (ImageView) inflate.findViewById(R.id.iv_set_back);
        tv_set_back = (TextView) inflate.findViewById(R.id.tv_set_back);
        rl_set_address = (RelativeLayout) inflate.findViewById(R.id.rl_set_address);
        rl_set_email = (RelativeLayout) inflate.findViewById(R.id.rl_set_email);
//        rl_set_us = (RelativeLayout) inflate.findViewById(R.id.rl_set_us);
        tv_set_login = (TextView) inflate.findViewById(R.id.tv_set_login);

        iv_set_back.setOnClickListener(new MyOnClickListener());
        tv_set_back.setOnClickListener(new MyOnClickListener());
        rl_set_address.setOnClickListener(new MyOnClickListener());
        rl_set_email.setOnClickListener(new MyOnClickListener());
//        rl_set_us.setOnClickListener(new MyOnClickListener());
        tv_set_login.setOnClickListener(new MyOnClickListener());

        String token = Utils.getSpData("token", context);

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_set_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.tv_set_back:
                    ((SecondPagerActivity) context).finish();
                    break;
                case R.id.rl_set_address:
                    ((SecondPagerActivity) context).switchPage(9);
                    ((SecondPagerActivity) context).from = "setpager";
                    break;
                case R.id.rl_set_email:
                    Intent data=new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:contact@luckybuyer.net"));
                    startActivity(data);
                    break;
//                case R.id.rl_set_us:
//                    Utils.MyToast(context,"关于我们");
//                    break;
                case R.id.tv_set_login:
                    Intent intent = new Intent();
                    intent.putExtra("go","homepager");
                    ((SecondPagerActivity)context).setResult(0,intent);
                    Utils.setSpData("token", null, context);
                    Utils.setSpData("token_num", null, context);
                    ((SecondPagerActivity) context).finish();
                    break;
            }
        }
    }

}
