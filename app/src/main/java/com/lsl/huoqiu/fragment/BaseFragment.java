package com.lsl.huoqiu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsl.huoqiu.AppContext;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Forrest on 16/9/9.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        //增加OOM监听
        RefWatcher refWatcher= AppContext.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
