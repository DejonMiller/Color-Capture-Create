package com.developer.andersonmiller.opencv;



import android.support.v4.app.Fragment;

import com.developer.andersonmiller.RecordListFragment;


public class RecordListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return new RecordListFragment();
    }
}
