package com.zaryab.omovie;


import android.util.Log;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter {


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    LollyWood lollyWood;
    BollyWood bollyWood;
    HollyWood hollyWood;
    AnimatedMovies animatedMovies;
    TrailersFrg trailersFrg;

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TrailersFrg trailersFrg = new TrailersFrg();
                return trailersFrg;

            case 1:
                HollyWood hollyWood = new HollyWood();
                return hollyWood;

            case 2:
                AnimatedMovies animatedMovies = new AnimatedMovies();
                return animatedMovies;

            case 3:
                LollyWood lollyWood = new LollyWood();
                return lollyWood;

            case 4:
                BollyWood bollyWood = new BollyWood();
                return bollyWood;

            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {

            case 0:
                trailersFrg = (TrailersFrg) createdFragment;
                break;
            case 1:
                hollyWood = (HollyWood) createdFragment;
                break;
            case 2:
                animatedMovies = (AnimatedMovies) createdFragment;
                break;
            case 3:
                lollyWood = (LollyWood) createdFragment;
                break;
            case 4:
                bollyWood = (BollyWood) createdFragment;
                break;

        }
        return createdFragment;
    }

    public void showSearchBar(int position){
        Log.d( "search postion" , String.valueOf( position ) );
        if (position==3&&lollyWood!=null){
            Log.d( "search postion" , String.valueOf( position ) );

            ((SecActivity.onSearchSelectedInterface)lollyWood).onSearchSelected();
        }
        if (position==4&&bollyWood!=null){
            Log.d( "search postion" , String.valueOf( position ) );
            ((SecActivity.onSearchSelectedInterface)bollyWood).onSearchSelected();
        }
        if (position==1&&hollyWood!=null){
            Log.d( "search postion holly" , String.valueOf( position ) );
            ((SecActivity.onSearchSelectedInterface)hollyWood).onSearchSelected();
        }
        if (position==2&&animatedMovies!=null){
            Log.d( "search postion holly" , String.valueOf( position ) );
            ((SecActivity.onSearchSelectedInterface)animatedMovies).onSearchSelected();
        }
        if (position==0&&trailersFrg!=null){
            Log.d( "search postion holly" , String.valueOf( position ) );
            ((SecActivity.onSearchSelectedInterface)trailersFrg).onSearchSelected();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "Trailers";
            case 1:
                return "HollyWood";
            case 2:
                return "Animated Movies";
            case 3:
                return "LollyWood";
            case 4:
                return "BollyWood";
            default:
                return null;
        }

    }
}
