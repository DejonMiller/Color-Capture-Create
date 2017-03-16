package com.developer.andersonmiller.opencv;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.andersonmiller.DatabaseHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Favorite extends Fragment {

        //RECORD FRAGMENT
        private static final String TAG = "Palette";
        public static final String EXTRA_RECORD_ID = "com.anddev.opencv.records_id";
        private FavEntry mFavEntry;
        Context context;
        GetColor.ColorComponet colorShades;
        GetColor getColor = new GetColor();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            DatabaseHandler databaseHandler = new DatabaseHandler(context,null,null,1);
            int recordId = (int) getArguments().getSerializable(EXTRA_RECORD_ID);

            mFavEntry = FavoriteLab.get(getActivity()).getRecord(recordId);

            setHasOptionsMenu(true);
        }

        @TargetApi(11)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_palette, parent, false);
            int color;
            String hexColor;
            String [] rgbArray = mFavEntry.getRgb().split(",");
            colorShades = new GetColor().new ColorComponet(Integer.valueOf(rgbArray[0]),Integer.valueOf(rgbArray[1]),Integer.valueOf(rgbArray[2]));
            double [] xyzArray = new double[3];
            float []hslArray = new float[3];
            List<GetColor.ColorComponet> ColorShadesList = getColor.calculateShades(colorShades, 5);
            List<Integer>tints = new ArrayList<>(getColor.calculateTints(colorShades,5));


            TextView Title = (TextView)v.findViewById(R.id.Title);
            Title.setText("Color: "+mFavEntry.getColor());

            TextView Hex = (TextView)v.findViewById(R.id.Hex_View);
            Hex.setText("Hex: " + mFavEntry.getHexValue());


            TextView RGB = (TextView)v.findViewById(R.id.RGB_View);
            RGB.setText("RGB: (" + mFavEntry.getRgb() + ")");

            TextView HSL = (TextView)v.findViewById(R.id.HSL_View);
            ColorUtil.RGBToHSL(colorShades.getRed(), colorShades.getGreen(), colorShades.getGreen(), hslArray);
            HSL.setText("HSL: " + Math.round((hslArray[0]))*100 /100 + "," + Math.round((hslArray[1]))*100/100f
                    + "," + Math.round((hslArray[2])*100/100f));

            TextView HSV = (TextView)v.findViewById(R.id.HSV_View);
            HSV.setText("HSV: " + mFavEntry.getHSV());

            TextView CMYK = (TextView)v.findViewById(R.id.CMYK_View);
            CMYK.setText("CMYK: "+mFavEntry.getCMYK());

            TextView LAB = (TextView)v.findViewById(R.id.LAB_View);
            LAB.setText("LAB: " + mFavEntry.getLab());

            TextView XYZ = (TextView)v.findViewById(R.id.XYZ_View);
            ColorUtil.RGBToXYZ(colorShades.getRed(),colorShades.getGreen(),colorShades.getBlue(),xyzArray);
            XYZ.setText("XYZ: "+Math.round((xyzArray[0]))*1000/1000+","+Math.round((xyzArray[1]))*1000/1000+","+Math.round((xyzArray[2]))*1000/1000);


            TextView Compliment = (TextView)v.findViewById(R.id.Complimentary_View);
            int inverted = getColor.getComplementaryColor(Color.rgb(Integer.valueOf(rgbArray[0]), Integer.valueOf(rgbArray[1]), Integer.valueOf(rgbArray[2])));
            Compliment.setBackgroundColor(inverted);
            color = ((ColorDrawable)Compliment.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Compliment.setText(hexColor);

            v.setBackgroundColor(Color.rgb(colorShades.getRed(),colorShades.getGreen(),colorShades.getBlue()));


            TextView Shade_0 = (TextView)v.findViewById(R.id.Shade_0);
            Shade_0.setBackgroundColor(Color.rgb(ColorShadesList.get(0).getRed(),ColorShadesList.get(0).getGreen(),ColorShadesList.get(0).getBlue()));
            color = ((ColorDrawable)Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Shade_0.setText(hexColor);
            Shade_0.setTextColor(inverted);

            TextView Shade_1 = (TextView)v.findViewById(R.id.Shade_1);
            Shade_1.setBackgroundColor(Color.rgb(ColorShadesList.get(1).getRed(),ColorShadesList.get(1).getGreen(),ColorShadesList.get(1).getBlue()));
            color = ((ColorDrawable)Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Shade_1.setText(hexColor);
            Shade_1.setTextColor(inverted);


            TextView Shade_2 = (TextView)v.findViewById(R.id.Shade_2);
            Shade_2.setBackgroundColor(Color.rgb(ColorShadesList.get(2).getRed(),ColorShadesList.get(2).getGreen(),ColorShadesList.get(2).getBlue()));
            color = ((ColorDrawable)Shade_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Shade_2.setText(hexColor);
            Shade_2.setTextColor(inverted);


            TextView Shade_3 = (TextView)v.findViewById(R.id.Shade_3);
            Shade_3.setBackgroundColor(Color.rgb(ColorShadesList.get(3).getRed(),ColorShadesList.get(3).getGreen(),ColorShadesList.get(3).getBlue()));
            color = ((ColorDrawable)Shade_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Shade_3.setText(hexColor);
            Shade_3.setTextColor(inverted);


            TextView Shade_4 = (TextView)v.findViewById(R.id.Shade_4);
            Shade_4.setBackgroundColor(Color.rgb(ColorShadesList.get(4).getRed(), ColorShadesList.get(4).getGreen(), ColorShadesList.get(4).getBlue()));
            color = ((ColorDrawable)Shade_4.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Shade_4.setText(hexColor);
            Shade_4.setTextColor(inverted);




            TextView Tint_0 = (TextView)v.findViewById(R.id.Tint_0);
            Tint_0.setBackgroundColor((tints.get(0)));
            color = ((ColorDrawable)Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_0.setText(hexColor);
            Tint_0.setTextColor(inverted);

            TextView Tint_1 = (TextView)v.findViewById(R.id.Tint_1);
            Tint_1.setBackgroundColor(tints.get(1));
            color = ((ColorDrawable)Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_1.setText(hexColor);
            Tint_1.setTextColor(inverted);

            TextView Tint_2 = (TextView)v.findViewById(R.id.Tint_2);
            Tint_2.setBackgroundColor(tints.get(2));
            color = ((ColorDrawable)Tint_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_2.setText(hexColor);
            Tint_2.setTextColor(inverted);

            TextView Tint_3 = (TextView)v.findViewById(R.id.Tint_3);
            Tint_3.setBackgroundColor(tints.get(3));
            color = ((ColorDrawable)Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_3.setText(hexColor);
            Tint_3.setTextColor(inverted);

            TextView Tint_4 = (TextView)v.findViewById(R.id.Tint_4);
            Tint_4.setBackgroundColor(tints.get(4));
            color = ((ColorDrawable)Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_4.setText(hexColor);
            Tint_4.setTextColor(inverted);

            TextView Analog_0 = (TextView)v.findViewById(R.id.Analog_0);
            Analog_0.setBackgroundColor(getColor.getAnalogousColors(mFavEntry).get(0));
            color = ((ColorDrawable)Analog_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Analog_0.setText(hexColor);
            Analog_0.setTextColor(inverted);


            TextView Analog_1 = (TextView)v.findViewById(R.id.Analog_1);
            Analog_1.setBackgroundColor(getColor.getAnalogousColors(mFavEntry).get(1));
            color = ((ColorDrawable)Analog_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Analog_1.setText(hexColor);
            Analog_1.setTextColor(inverted);


            TextView Analog_2 = (TextView)v.findViewById(R.id.Analog_2);
            Analog_2.setBackgroundColor(getColor.getAnalogousColors(mFavEntry).get(2));
            color = ((ColorDrawable)Analog_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Analog_2.setText(hexColor);
            Analog_2.setTextColor(inverted);


            TextView Tri_0 = (TextView)v.findViewById(R.id.Tri_0);
            Tri_0.setBackgroundColor(getColor.getTriadicColors(mFavEntry).get(0));
            color = ((ColorDrawable)Tri_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tri_0.setText(hexColor);
            Tri_0.setTextColor(inverted);

            TextView Tri_1 = (TextView)v.findViewById(R.id.Tri_1);
            Tri_1.setBackgroundColor(getColor.getTriadicColors(mFavEntry).get(1));
            color = ((ColorDrawable)Tri_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tri_1.setText(hexColor);
            Tri_1.setTextColor(inverted);

            TextView Tri_2 = (TextView)v.findViewById(R.id.Tri_2);
            Tri_2.setBackgroundColor(getColor.getTriadicColors(mFavEntry).get(2));
            color = ((ColorDrawable)Tri_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tri_2.setText(hexColor);
            Tri_2.setTextColor(inverted);

            TextView TintsTitle = (TextView)v.findViewById(R.id.Tints);
            TintsTitle.setTextColor(inverted);
            TextView ComplimentTitle = (TextView)v.findViewById(R.id.Compliment_Title);
            ComplimentTitle.setTextColor(inverted);
            TextView AnalogTitle = (TextView)v.findViewById(R.id.Analog_Title);
            AnalogTitle.setTextColor(inverted);
            TextView ShadesTitle = (TextView)v.findViewById(R.id.Shade_Names);
            ShadesTitle.setTextColor(inverted);

            TextView Tri = (TextView)v.findViewById(R.id.Triadic_Title);
            Tri.setTextColor(inverted);
            Hex.setTextColor(inverted);
            RGB.setTextColor(inverted);
            CMYK.setTextColor(inverted);
            HSL.setTextColor(inverted);
            HSV.setTextColor(inverted);
            LAB.setTextColor(inverted);
            XYZ.setTextColor(inverted);
            Title.setTextColor(inverted);


            return v;
        }

        public static Favorite newInstance(int recordId) {
            Bundle args = new Bundle();
            args.putSerializable(EXTRA_RECORD_ID, recordId);

            Favorite fragment = new Favorite();
            fragment.setArguments((args));

            return fragment;
        }

        @Override
        public void onPause() {
            super.onPause();
            FavoriteLab.get(getActivity()).saveRecords();
        }

    }

