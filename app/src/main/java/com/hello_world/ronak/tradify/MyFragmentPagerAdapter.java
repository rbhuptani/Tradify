package com.hello_world.ronak.tradify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sharm_000 on 2/11/2016.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    int count;
    String userId;
    ArrayList<String> tabs = new ArrayList<String>(Arrays.asList("Current Items","Sold","Notifications")) ;
   //MovieData movieData = new MovieData();

    @Override
    public Fragment getItem(int position) {
        if(position==2){
         return NotificationsFragment.newInstance();
        }else {
            return CurrentItemsFragment.newInstance(userId, position);
        }
        //UserProfileFragment.newInstance((HashMap<String,?>) movieData.getItem(position));
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Locale l = Locale.getDefault();
        String name = (String) tabs.get(position);
        return name.toUpperCase();
    }

    public MyFragmentPagerAdapter(FragmentManager fm, int size, String userId){
        super(fm);
        count = size;
        this.userId = userId;
    }
}
