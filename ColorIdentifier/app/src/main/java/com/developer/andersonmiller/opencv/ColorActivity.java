package com.developer.andersonmiller.opencv;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.andersonmiller.Palette;

import java.util.UUID;


public class ColorActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
       UUID recordId = (UUID)getIntent().getSerializableExtra(Favorite.EXTRA_RECORD_ID);
        Intent intent = getIntent();
        int record = intent.getIntExtra("Key",0);
        //int recordId = getIntent().getExtras("RecordListFragment");
        return Favorite.newInstance(record);

        }
    }

