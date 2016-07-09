package com.example.android.spiderintask3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Srikanth on 7/10/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter{

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:{
                return new FavouriteMovies();
            }
            case 1:{
                return new FavouriteSeries();
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
