package com.developer.andersonmiller;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.andersonmiller.opencv.Entry;
import com.developer.andersonmiller.opencv.FavoriteLab;
import com.developer.andersonmiller.opencv.R;

import java.util.UUID;

public class Palette extends Fragment {

    //RECORD FRAGMENT
    private static final String TAG = "Palette";
    public static final String EXTRA_RECORD_ID= "com.anddev.opencv.records_id";
    private Entry mEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recordId =(UUID)getArguments().getSerializable(EXTRA_RECORD_ID);
      //  mEntry= FavoriteLab.get(getActivity()).getRecord();
        setHasOptionsMenu(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_palette, parent, false);
        return v;
    }

    public static Palette newInstance(UUID recordId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_RECORD_ID, recordId);

        Palette fragment = new Palette();
        fragment.setArguments((args));

        return fragment;
    }

    @Override
    public void onPause(){
        super.onPause();
        FavoriteLab.get(getActivity()).saveRecords();
    }
}