package com.developer.andersonmiller.opencv;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.developer.andersonmiller.DatabaseHandler;

import java.io.IOException;
import java.io.InputStream;


public class SplashScreen extends AppCompatActivity {

    DatabaseHandler dbHandler;
    GetColor allColors = new GetColor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.splash);
    int red, blue, green=0;
        dbHandler = new DatabaseHandler(this, null, null, 1);

        if (dbHandler.checkEmpty()) {
            ColorDatabase colorDatabase;
            String color = "";
            String hex = "";
            String rgb = "";
            String cmyk = "";
            String hsv = "";
            String lab ="";
            double []labArray = new double[3];

            String full;
            char temp;
            //String [] row = new String[5];

            InputStream stream;
            try {
                AssetManager assetManager = getAssets();

                stream = assetManager.open("ColorList.txt");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                String text = new String(buffer);

                allColors.setColor(text);

            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] colors = allColors.getColor();

//////////////////////////////For adding to database
            /*
            Name HEX RGB HSV CMYK
            */

            for (int i = 0; i < colors.length; i++) {

      /*
      The colors array is created from the getColor.getColor() method. Each line in the orginal text file is made
      into an element in the array.
      The row array splits each iteration of the "full" string and seperates the string into array elements where white spaces
      are found.

       */
                full = colors[i];
                String[] row = full.split(" ");

                color = row[0];
                hex = row[1];
                rgb = row[2];
                hsv = row[3];
                cmyk = row[4];

                String[]rgbArray = rgb.split(",");

                ColorUtil.RGBToLAB(Integer.valueOf(rgbArray[0]), Integer.valueOf(rgbArray[1]), Integer.valueOf(rgbArray[2]), labArray);

                lab = (String.valueOf(labArray[0]))+","+(String.valueOf(labArray[1]))+","+(String.valueOf(labArray[2]));

                Log.i("LAB TAG", lab);

                colorDatabase = new ColorDatabase(color, hex, rgb, hsv, cmyk, lab);
                dbHandler.addColor(colorDatabase);

            }
            System.out.println("Newly Created!!!");
            System.out.println("****************************");
            string();

        } else {

            string();

        }
//Sends the array of color data the ColorCreator class

    Intent i = new Intent(this, ColorBlendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("AllColors", allColors.getColor());
        i.putExtras(bundle);
        startActivity(i);


        finish();
}

    //Only to be used to create database...actually a redundant method
    public void string() {

        String string="";
        string = dbHandler.dataBaseToString();
        allColors.setColor(string);
        System.out.print(string);
    }
}
