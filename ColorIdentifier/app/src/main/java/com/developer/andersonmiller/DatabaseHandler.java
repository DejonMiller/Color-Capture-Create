package com.developer.andersonmiller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developer.andersonmiller.opencv.ColorDatabase;
import com.developer.andersonmiller.opencv.FavEntry;

import java.sql.SQLData;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String Color_DataBase ="Color_DB";
    private static final int DATABASE_VERSION = 1;
    //private static final String TABLE_NAME ="Color_Table";
    private static final String table_name = "Color_Table";
    private static final String fav_table = "Favorite_Table";
    private static final String COLUMN_Color ="Color_Name";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_Hex="Color_Hex";
    private static final String COLUMN_RGB="Color_RGB";
    private static final String COLUMN_HSV="Color_HSV";
    private static final String COLUMN_CMYK="Color_CMYK";
    private static final String COLUMN_LAB = "Color_LAB";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Color_DataBase, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //COLUMN_ID increased id by 1 for each item in database.  have 3 columns: id, color, hex.
      /*  String query = "CREATE TABLE "+ table_name + "(" + COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Color+" TEXT, "+ COLUMN_Hex+" TEXT );";
        CREATE TABLE IF NOT EXISTS*/

        String query = "CREATE TABLE IF NOT EXISTS "+ table_name + "(" + COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Color+" TEXT, "+ COLUMN_Hex+" TEXT, "+ COLUMN_RGB+" TEXT , "+ COLUMN_HSV+" TEXT , " + COLUMN_CMYK+" TEXT , " + COLUMN_LAB+" TEXT);";
        //Creates the table above.

        String query2 = "CREATE TABLE IF NOT EXISTS "+ fav_table + "(" + COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Color+" TEXT, "+ COLUMN_Hex+" TEXT, "+ COLUMN_RGB+" TEXT , "+ COLUMN_HSV+" TEXT , " + COLUMN_CMYK+" TEXT , " + COLUMN_LAB+" TEXT);";
        db.execSQL(query);


        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db = getWritableDatabase();
        //Needed to upgrade database. If user decides to add color to database.
     //   db.execSQL("DROP TABLE IF EXISTS");
        //db.execSQL("delete from "+ TABLE_NAME);
        db.execSQL("ALTER TABLE Color_Table ADD COLUMN COLUMN_RGB INTEGER DEFAULT 0");

        onCreate(db);
    }

    public SQLiteDatabase tempBase(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        return db;
    }


    public boolean checkEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        String query = ("SELECT * FROM " + table_name);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c != null) {
            if (c.getCount() > 0) {
                return false;
            }
            c.close();
        }
            return true;
    }

    //Add new row to database
    public void addColor(ColorDatabase color){

        ContentValues values = new ContentValues();
        values.put(COLUMN_Color, color.getColor());
        values.put(COLUMN_Hex, color.getHexValue());
        values.put(COLUMN_RGB, color.getRgb());
        values.put(COLUMN_HSV, color.getHSV());
        values.put(COLUMN_CMYK, color.getCMYK());
        values.put(COLUMN_LAB, color.getLab());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(table_name, null, values);
       // db.insert(fav_table, null, values);
        db.close();
    }

    public void addFavorite(ColorDatabase color){
        ContentValues values = new ContentValues();
        values.put(COLUMN_Color, color.getColor());
        values.put(COLUMN_Hex, color.getHexValue());
        values.put(COLUMN_RGB, color.getRgb());
        values.put(COLUMN_HSV, color.getHSV());
        values.put(COLUMN_CMYK, color.getCMYK());
        values.put(COLUMN_LAB, color.getLab());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(table_name, null, values);
         db.insert(fav_table, null, values);
        db.close();
    }

    public void addRGB(ColorDatabase color, ContentValues v){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RGB, color.getRgb());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(table_name, null, values);
        db.close();
    }

    public void deleteColor(FavEntry favEntry) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + fav_table + " WHERE " + COLUMN_Color + "=\"" + favEntry.getColor() + "\";");
        db.execSQL("DELETE FROM " + table_name + " WHERE " + COLUMN_Color + "=\"" + favEntry.getColor() + "\";");

        //db.execSQL("DELETE * FROM " + TABLE_NAME);
//        db.delete(fav_table,"COLUMN_Color = ?", new String[]{name});
        db.close();
    }

    public void deleteTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public String dataBaseToString(){
        String dbString ="";
        SQLiteDatabase db = getWritableDatabase();
        String query = ("SELECT * FROM "+ table_name + " WHERE 1");
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("Color_Name"))!=null){

              /* dbString += c.getString(c.getColumnIndex("Color_Name") )+" "+
                       c.getString(c.getColumnIndex("Color_Hex"))+" ";*/

                dbString += c.getString(c.getColumnIndex("Color_Name") )+" "+
                        c.getString(c.getColumnIndex("Color_Hex"))+" " +
                        c.getString(c.getColumnIndex("Color_RGB"))+" "+ c.getString(c.getColumnIndex("Color_HSV"))+" "+
                        c.getString(c.getColumnIndex("Color_CMYK"))+" "+ c.getString(c.getColumnIndex("Color_LAB"));

                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();

        return dbString;
    }


    public String favoriteBaseToString(){
        String dbString ="";
        SQLiteDatabase db = getWritableDatabase();
        String query = ("SELECT * FROM "+ fav_table + " WHERE 1");
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("Color_Name"))!=null){

              /* dbString += c.getString(c.getColumnIndex("Color_Name") )+" "+
                       c.getString(c.getColumnIndex("Color_Hex"))+" ";*/

                dbString += c.getString(c.getColumnIndex("Color_Name") )+" "+
                        c.getString(c.getColumnIndex("Color_Hex"))+" " +
                        c.getString(c.getColumnIndex("Color_RGB"))+" "+ c.getString(c.getColumnIndex("Color_HSV"))+" "+
                        c.getString(c.getColumnIndex("Color_CMYK"))+" "+ c.getString(c.getColumnIndex("Color_LAB"));

                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();

        return dbString;
    }

    public ArrayList<FavEntry> getFavorites(){
        ArrayList<FavEntry>mFavEntries = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = ("SELECT * FROM "+ fav_table + " WHERE 1");
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("Color_Name"))!=null){

                FavEntry mEntry = new FavEntry(c.getPosition(),c.getString(c.getColumnIndex("Color_Name")),c.getString(c.getColumnIndex("Color_Hex")),c.getString(c.getColumnIndex("Color_RGB"))
                ,c.getString(c.getColumnIndex("Color_HSV")),c.getString(c.getColumnIndex("Color_CMYK")),c.getString(c.getColumnIndex("Color_LAB")));

                mFavEntries.add(mEntry);
            }
            c.moveToNext();
        }
        c.close();
        db.close();

        return mFavEntries;
    }


}
