package com.developer.andersonmiller.opencv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.developer.andersonmiller.Palette;

import java.util.ArrayList;
import java.util.UUID;

public class RecordPagerActivity extends FragmentActivity {
/*
    private ViewPager mViewPager;
    private ArrayList<FavEntry> mFavEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mFavEntry = FavoriteLab.get(this).getRecords();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return mFavEntry.size();
            }

            @Override
            public Fragment getItem(int pos) {
                FavEntry Record = mFavEntry.get(pos);
                return Favorite.newInstance(Record.get_id());
            }
        });

        UUID RecordId=(UUID)getIntent().getSerializableExtra(Palette.EXTRA_RECORD_ID);
        for(int i = 0; i < mFavEntry.size(); i++){
            if(mFavEntry.get(i).get_id().equals(RecordId)){
                mViewPager.setCurrentItem(i);
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state){}

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }

            public void onPageSelected(int pos) {
                FavEntry Record = mFavEntry.get(pos);
                if (Record.getTitle() != null) {
                    setTitle(Record.getTitle());
                }
            }
        });
    }
    */
}

