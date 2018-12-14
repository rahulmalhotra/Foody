package io.github.rahulmalhotra.foody.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.rahulmalhotra.foody.R;

public class TabAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private final List<Fragment> fragmentList = new ArrayList<>();

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case 0:
                pageTitle = context.getResources().getString(R.string.nearbyTitle);
                break;
            case 1:
                pageTitle = context.getResources().getString(R.string.favoritesTitle);
            break;
        }
        return pageTitle;
    }
}
