package net.iwantbuyer.secondpager;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Trace;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.iwantbuyer.R;
import net.iwantbuyer.adapter.NotificationAdapter;
import net.iwantbuyer.base.BaseNoTrackPager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.util.ArrayList;

/**
 * Created by admin on 2017/2/20.
 */
public class PagerNews extends BaseNoTrackPager {

    private View inflate;
    private RelativeLayout rl_notification_goset;
    private RelativeLayout rl_news_back;
    private TextView tv_notification_goset;
    private ImageView tv_notification_close;
    private RecyclerView rv_notification;

    @Override
    public View initView() {
        inflate = View.inflate(context, R.layout.pager_news, null);
        findView();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        
    }

    public void processData(String response){
        if(isNotificationEnabled(context)) {
            rl_notification_goset.setVisibility(View.VISIBLE);
        }else {
            rl_notification_goset.setVisibility(View.GONE);
        }

        rv_notification.setAdapter(new NotificationAdapter(context,new ArrayList()));

    }
    private void findView() {
        rl_news_back = (RelativeLayout) inflate.findViewById(R.id.rl_news_back);
        rl_notification_goset = (RelativeLayout) inflate.findViewById(R.id.rl_notification_goset);
        tv_notification_goset = (TextView) inflate.findViewById(R.id.tv_notification_goset);
        tv_notification_close = (ImageView) inflate.findViewById(R.id.tv_notification_close);
        rv_notification = (RecyclerView) inflate.findViewById(R.id.rv_notification);

        rl_news_back.setOnClickListener(new MyOnClickListener());
        tv_notification_goset.setOnClickListener(new MyOnClickListener());
        tv_notification_close.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_news_back:

                    break;
                case R.id.tv_notification_goset:
                    goToSet();
                    break;
                case R.id.tv_notification_close:
                    rl_notification_goset.setVisibility(View.GONE);
                    break;
            }
        }
    }

    //去设置界面
    private void goToSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        }
    }

    //判断notification是否打开
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
             /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
