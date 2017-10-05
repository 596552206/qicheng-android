package me.milechen.qicheng.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Fragments.MainFragment;

/**
 * Created by mile on 2017/4/30.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<MainFragment> fragmentList = new ArrayList<MainFragment>();

    public FragmentAdapter(FragmentManager fm, List<MainFragment> mFragmentList){
        super(fm);
        fragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
