package com.developer.andersonmiller.opencv;

import android.content.Context;
import android.util.Log;

import com.developer.andersonmiller.DatabaseHandler;

import java.util.ArrayList;
import java.util.UUID;

//Record Lab
public class FavoriteLab {

    private String title;
    private static FavoriteLab sPaletteLab;
    private Context mAppContext;
    private static final String FILENAME = "records.json";
    private static final String TAG = "FavoriteLab";
    private ArrayList<Entry> mEntry;
    private ArrayList<FavEntry>mFavEntry;
    private RecordIntentJSONSerializer mSerializer;
    Context context;
   DatabaseHandler databaseHandler;

    public FavoriteLab(Context appContext) {
        mAppContext = appContext;
        //mEntry = new ArrayList<Entry>();
//        mRecords = new ArrayList<Record>();
        mSerializer = new RecordIntentJSONSerializer(mAppContext, FILENAME);

        try {
            DatabaseHandler databaseHandler = new DatabaseHandler(appContext, null, null, 1);

            mFavEntry = databaseHandler.getFavorites();
        }catch(Exception e){
            mEntry = new ArrayList<Entry>();
            Log.e(TAG, "Error loading records: ",e);
        }
    }

    public ArrayList<FavEntry> refresh (){

        DatabaseHandler databaseHandler = new DatabaseHandler(mAppContext,null,null,1);

        return databaseHandler.getFavorites();
    }

    public static FavoriteLab get(Context c){
        if(sPaletteLab==null){
            sPaletteLab= new FavoriteLab(c.getApplicationContext());
        }
        return sPaletteLab;
    }

    public void addRecord(FavEntry c){
        mFavEntry.add(c);
    }

    public void deleteFavorite(FavEntry c){

        DatabaseHandler databaseHandler = new DatabaseHandler(mAppContext, null, null, 1);
        databaseHandler.deleteColor(c);
        //c.setColor("Deleted");
        mFavEntry.remove(c);
      //  c.setColor("Deleted");

    }
    public boolean saveRecords(){
        try{
           // mSerializer.saveRecords(mEntry);
            Log.d(TAG, "Records saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "Error saving records: ", e);
            return false;
        }
    }

    public ArrayList<FavEntry>getRecords(){
        return refresh();
    }

   /* public FavEntry getRecord(){
        int count = 0;
       for(FavEntry c: mFavEntry){
           count++;
         //   if(c.get_id().equals(count))
                if(c.get_id() == count)
             c = mFavEntry.get(count);
                return c;
        }
        return null;
    }*/

    public FavEntry getRecord(int record){

        DatabaseHandler databaseHandler = new DatabaseHandler(mAppContext,null,null,1);
        for(FavEntry c: databaseHandler.getFavorites()){

            if(c.get_id() == record)
            return c;
        }
        return null;
    }

}

