package hanbat.isl.baeminsu.firebasebasicchatapp.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> mainListFragments = new ArrayList<Fragment>();

    private String tabTitle[] = new String[]{"친구", "대화", "설정"};

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mainListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mainListFragments.size();
    }

    public void addFragment(Fragment fragment) {
        mainListFragments.add(fragment);
    }
}
