package com.developer.andersonmiller;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.developer.andersonmiller.opencv.ColorActivity;
import com.developer.andersonmiller.opencv.FavEntry;
import com.developer.andersonmiller.opencv.Favorite;
import com.developer.andersonmiller.opencv.FavoriteLab;
import com.developer.andersonmiller.opencv.R;
import com.developer.andersonmiller.opencv.RecordListActivity;

import java.util.ArrayList;

public class RecordListFragment extends ListFragment {

    private static final String TAG = "RecordListFragment";
    private ArrayList<FavEntry> mFavEntry;
    TextView titleTextView;
    TextView hexTextView;
    Bundle savedInstanceState;
    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Captured Palettes");
       mFavEntry = FavoriteLab.get(getActivity()).getRecords();
        //Context context = getActivity().getApplicationContext();
      //  databaseHandler = new DatabaseHandler(context,null,null,1);
        //ArrayAdapter<FavEntry> adapter = new ArrayAdapter<FavEntry>(getActivity(),android.R.layout.simple_list_item_1,mFavEntry);
        //setListAdapter(adapter);
        RecordAdapter adapter = new RecordAdapter(mFavEntry);
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_record_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState){
        View v = super.onCreateView(inflater,parent,savedInstanceState);
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                RecordAdapter adapter = (RecordAdapter) getListAdapter();

                for (int i = adapter.getCount() - 1; i >= 0; i--) {
                    if (getListView().isItemChecked(i)) {
                       // favoriteLab.deleteFavorite(adapter.getItem(i));
                     //   getListView().findViewById(i).setBackgroundColor(Color.DKGRAY);



                    }
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.favorite_list_delete,menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_favorite:
                        RecordAdapter adapter = (RecordAdapter) getListAdapter();
                        FavoriteLab favoriteLab = FavoriteLab.get(getActivity());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (getListView().isItemChecked(i)) {

                                favoriteLab.deleteFavorite(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                        default:
                    return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                Context context = getActivity().getApplicationContext();
                Intent myIntent = new Intent(context, RecordListActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

        getActivity().getMenuInflater().inflate(R.menu.fragment_record_list, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        RecordAdapter adapter = (RecordAdapter)getListAdapter();
        FavEntry favEntry = adapter.getItem(position);
        switch (item.getItemId()) {

            case R.id.menu_item_delete_favorite:
                FavoriteLab.get(getActivity()).deleteFavorite(favEntry);
                mFavEntry= FavoriteLab.get(getActivity()).refresh();
                adapter.notifyDataSetChanged();

                return true;
        }

        return super.onContextItemSelected(item);
        }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FavEntry c = ((RecordAdapter) getListAdapter()).getItem(position);
        Log.d(TAG, c.getColor() + "was clicked");
        Intent i = new Intent(getActivity(), ColorActivity.class);
          i.putExtra("Key", position);
           startActivity(i);
    }

    private class RecordAdapter extends ArrayAdapter<FavEntry> {

        public RecordAdapter(ArrayList<FavEntry> mFavEntry) {
            super(getActivity(), 0 , mFavEntry);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_record, null);
            }

                FavEntry c = getItem(position);
                titleTextView = (TextView) convertView.findViewById(R.id.record_list_item_titleTextView);
                titleTextView.setText(c.getColor());

            hexTextView =(TextView)convertView.findViewById(R.id.record_list_item_dateTextView);
            hexTextView.setText(c.getHexValue());

            return convertView;
        }
    }


}
