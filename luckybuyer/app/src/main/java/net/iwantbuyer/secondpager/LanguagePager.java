package net.iwantbuyer.secondpager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.activity.SecondPagerActivity;
import net.iwantbuyer.activity.ThirdPagerActivity;
import net.iwantbuyer.base.BaseNoTrackPager;
import net.iwantbuyer.utils.Utils;

/**
 * Created by admin on 2017/2/6.
 */
public class LanguagePager extends BaseNoTrackPager {

    private View inflate;
    private RelativeLayout rl_language_english;
    private RelativeLayout rl_language_chinese;
    private RelativeLayout rl_language_Malay;

    private ImageView iv_language_english;
    private ImageView iv_language_chinese;
    private ImageView iv_language_Malay;
    
    private RelativeLayout rl_language_back;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_language,null);
        findView();
        setView();
        return inflate;
    }

    private void findView() {
        rl_language_english = (RelativeLayout) inflate.findViewById(R.id.rl_language_english);
        rl_language_chinese = (RelativeLayout) inflate.findViewById(R.id.rl_language_chinese);
        rl_language_Malay = (RelativeLayout) inflate.findViewById(R.id.rl_language_Malay);
        iv_language_english = (ImageView) inflate.findViewById(R.id.iv_language_english);
        iv_language_chinese = (ImageView) inflate.findViewById(R.id.iv_language_chinese);
        iv_language_Malay = (ImageView) inflate.findViewById(R.id.iv_language_Malay);
        rl_language_back = (RelativeLayout) inflate.findViewById(R.id.rl_language_back);

        rl_language_english.setOnClickListener(new MyOnClickListener());
        rl_language_chinese.setOnClickListener(new MyOnClickListener());
        rl_language_Malay.setOnClickListener(new MyOnClickListener());
        rl_language_back.setOnClickListener(new MyOnClickListener());
    }

    private void setView() {
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language != null && language.contains("ms")) {
            iv_language_english.setVisibility(View.GONE);
            iv_language_chinese.setVisibility(View.GONE);
            iv_language_Malay.setVisibility(View.VISIBLE);
        } else if (language != null && language.contains("zh")) {
            iv_language_english.setVisibility(View.GONE);
            iv_language_chinese.setVisibility(View.VISIBLE);
            iv_language_Malay.setVisibility(View.GONE);
        } else {
            iv_language_english.setVisibility(View.VISIBLE);
            iv_language_chinese.setVisibility(View.GONE);
            iv_language_Malay.setVisibility(View.GONE);
        }

        String spString = Utils.getSpData("locale",context);
        if(spString != null && "ms".equals(spString.split("-")[0])) {
            iv_language_english.setVisibility(View.GONE);
            iv_language_chinese.setVisibility(View.GONE);
            iv_language_Malay.setVisibility(View.VISIBLE);
        }else if(spString != null && "zh".equals(spString.split("-")[0])) {
            iv_language_english.setVisibility(View.GONE);
            iv_language_chinese.setVisibility(View.VISIBLE);
            iv_language_Malay.setVisibility(View.GONE);
        }else if(spString != null && "en".equals(spString.split("-")[0])){
            iv_language_english.setVisibility(View.VISIBLE);
            iv_language_chinese.setVisibility(View.GONE);
            iv_language_Malay.setVisibility(View.GONE);
        }
    }

    class MyOnClickListener implements View.OnClickListener{

        String spString = Utils.getSpData("locale",context);

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_language_english:
                    iv_language_english.setVisibility(View.VISIBLE);
                    iv_language_chinese.setVisibility(View.GONE);
                    iv_language_Malay.setVisibility(View.GONE);
                    ((ThirdPagerActivity)context).language = "en";
                    ((ThirdPagerActivity)context).fragmentManager.popBackStack();
                    break;
                case R.id.rl_language_chinese:
                    iv_language_english.setVisibility(View.GONE);
                    iv_language_chinese.setVisibility(View.VISIBLE);
                    iv_language_Malay.setVisibility(View.GONE);
                    ((ThirdPagerActivity)context).language = "zh";
                    ((ThirdPagerActivity)context).fragmentManager.popBackStack();
                    break;
                case R.id.rl_language_Malay:
                    iv_language_english.setVisibility(View.GONE);
                    iv_language_chinese.setVisibility(View.GONE);
                    iv_language_Malay.setVisibility(View.VISIBLE);
                    ((ThirdPagerActivity)context).language = "ms";
                    ((ThirdPagerActivity)context).fragmentManager.popBackStack();
                    break;
                case R.id.rl_language_back:
                    ((ThirdPagerActivity)context).fragmentManager.popBackStack();
                    break;
            }
        }
    }
}
