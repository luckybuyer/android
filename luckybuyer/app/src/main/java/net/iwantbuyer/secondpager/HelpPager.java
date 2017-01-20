package net.iwantbuyer.secondpager;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.utils.Utils;

/**
 * Created by admin on 2016/12/13.
 */
public class HelpPager extends BaseNoTrackPager{
    private RelativeLayout rl_help_back;
    private TextView rl_buycoins_email;
    private TextView tv_help_coin;
    private View inflate;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_help,null);
        ((SecondPagerActivity) context).rl_secondpager_header.setVisibility(View.GONE);
        findView();
        setView();
        return inflate;
    }

    private void setView() {
        if(Utils.getSpData("service",context)!= null && Utils.getSpData("service",context).contains("api-my")) {
            tv_help_coin.setText(context.getString(R.string.CoinsExplanMYR));
        }else {
            tv_help_coin.setText(context.getString(R.string.CoinsExplanUSD));
        }
    }

    private void findView() {
        rl_help_back = (RelativeLayout) inflate.findViewById(R.id.rl_help_back);
        rl_buycoins_email = (TextView) inflate.findViewById(R.id.rl_buycoins_email);
        tv_help_coin = (TextView) inflate.findViewById(R.id.tv_help_coin);

        rl_help_back.setOnClickListener(new MyOnClickListener());
        rl_buycoins_email.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_help_back:
                    if("buycoinpager".equals(((SecondPagerActivity)context).from)) {
                        ((SecondPagerActivity)context).fragmentManager.popBackStack();
                    }else {
                        ((SecondPagerActivity)context).finish();
                    }
                    break;
                case R.id.rl_buycoins_email:
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:contact@luckybuyer.net"));
                    startActivity(data);
                    break;
            }
        }
    }
}
