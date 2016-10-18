package com.lsl.huoqiu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.lsl.huoqiu.bean.StructBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/9/26.
 */
public class StructFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    private final FragmentManager fm;
    private List<StructBean> beans;

    public StructFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.fm=fm;
    }

    public StructFragmentAdapter(FragmentManager fm,  ArrayList<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    public StructFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<StructBean> beans) {
        super(fm);
        this.fragments = fragments;
        this.fm = fm;
        this.beans = beans;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.size():0;
    }


//    public void appendList(ArrayList<Fragment> fragment) {
//        fragments.clear();
//        if (!fragments.containsAll(fragment) && fragment.size() > 0) {
//            fragments.addAll(fragment);
//        }
//        notifyDataSetChanged();
//    }
//    public void setFragments(ArrayList<Fragment> fragments) {
//        if (this.fragments != null) {
//            FragmentTransaction ft = fm.beginTransaction();
//            for (Fragment f : this.fragments) {
//                ft.remove(f);
//            }
//            ft.commit();
//            ft = null;
//            fm.executePendingTransactions();
//        }
//        this.fragments = fragments;
//        notifyDataSetChanged();
//    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return beans!=null?beans.get(position).getName():"Title";
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (fragments.size() <= position) {
            position = position % fragments.size();
        }
        Object obj = super.instantiateItem(container, position);
        return obj;
    }
}
