package net.smartbuyer.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2016/9/13.
 * yangshuyu
 */
public abstract class BasePager extends Fragment {
    public Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return getPersistentView(inflater, container, savedInstanceState, initView());
    }

    public abstract View initView();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = view;
            initData();
        } else {
//            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }


    public void initData() {
        //加载数据
    }
}
