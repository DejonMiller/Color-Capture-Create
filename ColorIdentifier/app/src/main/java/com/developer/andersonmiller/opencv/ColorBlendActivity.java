package com.developer.andersonmiller.opencv;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.andersonmiller.DatabaseHandler;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class ColorBlendActivity extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    private ArrayList<FavEntry> favEntries;
    private int[] Top = new int[3];
    private int[] Bottom = new int[3];
    private String[] Top_rgbArray = new String[3];
    private String[] Bottom_rgbArray = new String[3];
    private ArrayList<String> favArray = new ArrayList<>();
    private String[] AllColors;
    private TextView Shade_0;
    private TextView Shade_1;
    private TextView Shade_2;
    private TextView Shade_3;
    private TextView Shade_4;
    private TextView Red_View01;
    private TextView Green_View01;
    private TextView Blue_View01;
    private TextView Red_View02;
    private TextView Green_View02;
    private TextView Blue_View02;
    private SeekBar Top_1;
    private SeekBar Top_2;
    private SeekBar Top_3;
    private SeekBar Bottom_1;
    private SeekBar Bottom_2;
    private SeekBar Bottom_3;
    private Spinner format;
    private Spinner favChoices;
    private TextView Preview;
    private CheckBox checkBox;
    private ImageButton Favorite;
    private Button Palette;
    private CheckBox Blend_CheckBox;
    int RGB[] = new int[3];
    String stringRGB[] = new String[3];
    double LAB[] = new double[3];
    float HSV_Array[] = new float[3];
    private EditText Hex_Top;
    private EditText Hex_Bottom;
    ColorDatabase colorDatabase;
    GetColor getColor = new GetColor();
    GetColor.ColorComponet baseColor;
    List<GetColor.ColorComponet> shades = new ArrayList<>();
    String[] ColorArray;
    private Button SwitchFormat;
    private String HexFormat = "ENTER RGB";
    private String RGBFormat = "ENTER HEX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_color_blend);
        String name;
        Bundle bundle = this.getIntent().getExtras();
        ColorArray = bundle.getStringArray("AllColors");
        final Context context = getApplicationContext();

        Toast toast = Toast.makeText(this, "Switch between landscape and portrait to change modes", Toast.LENGTH_SHORT);
        toast.show();

        if (getResources().getConfiguration().orientation == 2) {

            Bundle bun = this.getIntent().getExtras();
            ColorArray = bundle.getStringArray("AllColors");

            Intent j = new Intent(context, MainActivity.class);
            Bundle b = new Bundle();
            b.putStringArray("TheColorArray", ColorArray);
            j.putExtras(b);
            startActivity(j);
            finish();
        }


        //String ColorString = dbHandler.dataBaseToString();
        // getColor.setColor(ColorString);
        //ColorArray = getColor.getColor();

        dbHandler = new DatabaseHandler(this, null, null, 1);
        favEntries = dbHandler.getFavorites();


        favArray.add("Select");
        for (int i = 0; i < favEntries.size(); i++) {

            name = favEntries.get(i).getColor();

            favArray.add(name);
        }
        //Used to populate the FavoriteSpinner which includes all the saved from the palette.
        ArrayAdapter<String> arrayAdapter_01 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favArray);
        arrayAdapter_01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        favChoices = (Spinner) findViewById(R.id.Favorite_Choices);
        favChoices.setOnItemSelectedListener(onItemSelectedListener);
        favChoices.setAdapter(arrayAdapter_01);

        //Used to populate choice spinner
        ArrayAdapter<String> arrayAdapter_02 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favArray);
        arrayAdapter_02.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        format = (Spinner) findViewById(R.id.Format);
        format.setOnItemSelectedListener(onItemSelectedListener2);
        format.setAdapter(arrayAdapter_02);


        Top_1 = (SeekBar) findViewById(R.id.seekBar1);
        Top_1.setMax(255);
        Top_1.setOnSeekBarChangeListener(onSeekBarChangeListener);

        Top_2 = (SeekBar) findViewById(R.id.seekBar2);
        Top_2.setMax(255);
        Top_2.setOnSeekBarChangeListener(onSeekBarChangeListener);


        Top_3 = (SeekBar) findViewById(R.id.seekBar3);
        Top_3.setMax(255);
        Top_3.setOnSeekBarChangeListener(onSeekBarChangeListener);


        Bottom_1 = (SeekBar) findViewById(R.id.seekBar4);
        Bottom_1.setMax(255);
        Bottom_1.setOnSeekBarChangeListener(onSeekBarChangeListener);

        Bottom_2 = (SeekBar) findViewById(R.id.seekBar5);
        Bottom_2.setMax(255);
        Bottom_2.setOnSeekBarChangeListener(onSeekBarChangeListener);

        Bottom_3 = (SeekBar) findViewById(R.id.seekBar6);
        Bottom_3.setMax(255);
        Bottom_3.setOnSeekBarChangeListener(onSeekBarChangeListener);


        Red_View01 = (TextView) findViewById(R.id.Red_01);
        Blue_View01 = (TextView) findViewById(R.id.Blue_01);
        Green_View01 = (TextView) findViewById(R.id.Green_01);

        Red_View02 = (TextView) findViewById(R.id.Red_02);
        Blue_View02 = (TextView) findViewById(R.id.Blue_02);
        Green_View02 = (TextView) findViewById(R.id.Green_02);

        Hex_Top = (EditText) findViewById(R.id.Top_Hex);
        Hex_Top.addTextChangedListener(new ColorTextWatcher(Hex_Top));

        Hex_Bottom = (EditText) findViewById(R.id.Bottom_Hex);
        Hex_Bottom.addTextChangedListener(new ColorTextWatcher(Hex_Bottom));

        Preview = (TextView) findViewById(R.id.Preview_View);
        Favorite = (ImageButton) findViewById(R.id.Favorite);
        SwitchFormat = (Button) findViewById(R.id.FormatSwitcher);

        SwitchFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SwitchFormat.getText().equals(HexFormat)) {
                    SwitchFormat.setText(RGBFormat);
                    Hex_Top.setHint("RRR,GGG,BBB");
                    Hex_Bottom.setHint("RRR,GGG,BBB");
                } else {

                    if (SwitchFormat.getText().equals(RGBFormat))
                        SwitchFormat.setText(HexFormat);
                    Hex_Top.setHint("#");
                    Hex_Bottom.setHint("#");

                }
            }
        });

        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopUp();
            }
        });

        Palette = (Button) findViewById(R.id.toPalette);

        Palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent myIntent = new Intent(context, RecordListActivity.class);

                startActivity(myIntent);
            }
        });


        Blend_CheckBox = (CheckBox) findViewById(R.id.Blend_CheckBox);
        Blend_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Blend_CheckBox.isChecked()) {

                    Bottom_1.setVisibility(View.VISIBLE);
                    Bottom_2.setVisibility(View.VISIBLE);
                    Bottom_3.setVisibility(View.VISIBLE);
                    Red_View02.setVisibility(View.VISIBLE);
                    Green_View02.setVisibility(View.VISIBLE);
                    Blue_View02.setVisibility(View.VISIBLE);
                    format.setVisibility(View.VISIBLE);
                    Hex_Bottom.setVisibility(View.VISIBLE);
                    blendColor();


                } else {

                    Bottom_1.setVisibility(View.INVISIBLE);
                    Bottom_2.setVisibility(View.INVISIBLE);
                    Bottom_3.setVisibility(View.INVISIBLE);

                    Red_View02.setVisibility(View.INVISIBLE);
                    Green_View02.setVisibility(View.INVISIBLE);
                    Blue_View02.setVisibility(View.INVISIBLE);

                    format.setVisibility(View.INVISIBLE);
                    Hex_Bottom.setVisibility(View.INVISIBLE);

                    singleColor();
                }
            }
        });

    }

    private PopupWindow pw;
    private PopupWindow mi;

    public void showPopUp() {
        final Button Ok;
        CheckBox Box;
        final String rgbValue, hex, hsv;
        String hexColor;
        int colors;

        try {
            // We need to get the instance of the LayoutInflater
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_color_info,
                    (ViewGroup) findViewById(R.id.PopUp));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            pw = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            //int backgroundColor = Color.rgb(baseColor.getRed(),baseColor.getGreen(),baseColor.getBlue());

            Ok = (Button) layout.findViewById(R.id.okButton);

            checkBox = (CheckBox) layout.findViewById(R.id.checkbox);

            final EditText Input = (EditText) layout.findViewById(R.id.ColorName);
            final TextView Hex = (TextView) layout.findViewById(R.id.Hex);
            final TextView RGB_Value = (TextView) layout.findViewById(R.id.RGB_Value);
            final TextView CMYK = (TextView) layout.findViewById(R.id.CMYK_Value);
            final TextView HSV = (TextView) layout.findViewById(R.id.HSV_Value);
            final TextView LAB_Value = (TextView) layout.findViewById(R.id.Lab_Value);
            RGB[0] = baseColor.getRed();
            RGB[1] = baseColor.getGreen();
            RGB[2] = baseColor.getBlue();

            Input.hasFocus();


            // Hex.setText(touch_color.getText().toString());
            RGB_Value.setText("RGB: " + baseColor.getRed() + "," + baseColor.getGreen() + "," + baseColor.getBlue());
            colors = ((ColorDrawable) Preview.getBackground()).getColor();
            hexColor = String.format("Hex: " + "#%06X", (0xFFFFFF & colors));
            Hex.setText(hexColor);
            rgbValue = RGB_Value.getText().toString();
            final String rgbHolder = rgbValue.substring(5, rgbValue.length()).trim();

            hex = (Hex.getText().toString().substring(5, Hex.getText().toString().length())).trim();
            Color.RGBToHSV(RGB[0], RGB[1], RGB[2], HSV_Array);
            hsv = (String.valueOf(HSV_Array[0]) + "," + String.valueOf(HSV_Array[1]) + "," + String.valueOf(HSV_Array[2]));
            HSV.setText("HSV: " + String.valueOf(HSV_Array[0]) + "," + String.valueOf(HSV_Array[1]) + "," + String.valueOf(HSV_Array[2]));

            final String cmyk = (getColor.getCMYK(String.valueOf(RGB[0]) + "," + String.valueOf(RGB[1]) + "," + String.valueOf(RGB[2])));
            ColorUtil.RGBToLAB(RGB[0], RGB[1], RGB[2], LAB);
            LAB[0] = ((Math.round(((LAB[0])) * 100) / 100));
            LAB[1] = ((Math.round(((LAB[1])) * 100) / 100));
            LAB[2] = ((Math.round(((LAB[2])) * 100) / 100));

            final String lab = String.valueOf(LAB[0]) + "," + String.valueOf(LAB[1]) + "," + String.valueOf(LAB[2]);

            CMYK.setText("CMYK: " + cmyk);
            LAB_Value.setText("Lab: " + lab);


            //baseColor = getColor.new ColorComponet(RGB[0],RGB[1],RGB[2]);
            int color = Color.rgb(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
            layout.setBackgroundColor(color);

            List<GetColor.ColorComponet> ColorShadesList = getColor.calculateShades(baseColor, 5);
            List<Integer> tints = new ArrayList<>(getColor.calculateTints(baseColor, 5));


            TextView Compliment = (TextView) layout.findViewById(R.id.Complimentary_View);
            int inverted = getColor.getComplementaryColor(Color.rgb(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue()));
            Compliment.setBackgroundColor(inverted);
            colors = ((ColorDrawable) Compliment.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Compliment.setText(hexColor);
            Compliment.setTextColor(inverted);


            TextView Shade_0 = (TextView) layout.findViewById(R.id.Shade_0);
            Shade_0.setBackgroundColor(Color.rgb(ColorShadesList.get(0).getRed(), ColorShadesList.get(0).getGreen(), ColorShadesList.get(0).getBlue()));
            colors = ((ColorDrawable) Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_0.setText(hexColor);
            Shade_0.setTextColor(inverted);

            TextView Shade_1 = (TextView) layout.findViewById(R.id.Shade_1);
            Shade_1.setBackgroundColor(Color.rgb(ColorShadesList.get(1).getRed(), ColorShadesList.get(1).getGreen(), ColorShadesList.get(1).getBlue()));
            colors = ((ColorDrawable) Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_1.setText(hexColor);
            Shade_1.setTextColor(inverted);

            TextView Shade_2 = (TextView) layout.findViewById(R.id.Shade_2);
            Shade_2.setBackgroundColor(Color.rgb(ColorShadesList.get(2).getRed(), ColorShadesList.get(2).getGreen(), ColorShadesList.get(2).getBlue()));
            colors = ((ColorDrawable) Shade_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_2.setText(hexColor);
            Shade_2.setTextColor(inverted);


            TextView Shade_3 = (TextView) layout.findViewById(R.id.Shade_3);
            Shade_3.setBackgroundColor(Color.rgb(ColorShadesList.get(3).getRed(), ColorShadesList.get(3).getGreen(), ColorShadesList.get(3).getBlue()));
            colors = ((ColorDrawable) Shade_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_3.setText(hexColor);
            Shade_3.setTextColor(inverted);

            TextView Shade_4 = (TextView) layout.findViewById(R.id.Shade_4);
            Shade_4.setBackgroundColor(Color.rgb(ColorShadesList.get(4).getRed(), ColorShadesList.get(4).getGreen(), ColorShadesList.get(4).getBlue()));
            colors = ((ColorDrawable) Shade_4.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_4.setText(hexColor);
            Shade_4.setTextColor(inverted);


            TextView Tint_0 = (TextView) layout.findViewById(R.id.Tint_0);
            Tint_0.setBackgroundColor((tints.get(0)));
            color = ((ColorDrawable) Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_0.setText(hexColor);
            Tint_0.setTextColor(inverted);

            TextView Tint_1 = (TextView) layout.findViewById(R.id.Tint_1);
            Tint_1.setBackgroundColor(tints.get(1));
            color = ((ColorDrawable) Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_1.setText(hexColor);
            Tint_1.setTextColor(inverted);

            TextView Tint_2 = (TextView) layout.findViewById(R.id.Tint_2);
            Tint_2.setBackgroundColor(tints.get(2));
            color = ((ColorDrawable) Tint_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_2.setText(hexColor);
            Tint_2.setTextColor(inverted);

            TextView Tint_3 = (TextView) layout.findViewById(R.id.Tint_3);
            Tint_3.setBackgroundColor(tints.get(3));
            color = ((ColorDrawable) Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_3.setText(hexColor);
            Tint_3.setTextColor(inverted);

            TextView Tint_4 = (TextView) layout.findViewById(R.id.Tint_4);
            Tint_4.setBackgroundColor(tints.get(4));
            color = ((ColorDrawable) Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_4.setText(hexColor);
            Tint_4.setTextColor(inverted);

            TextView Analog_0 = (TextView) layout.findViewById(R.id.Analog_0);
            Analog_0.setBackgroundColor(getColor.getAnalogousColors(baseColor).get(0));
            colors = ((ColorDrawable) Analog_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_0.setText(hexColor);
            Analog_0.setTextColor(inverted);


            TextView Analog_1 = (TextView) layout.findViewById(R.id.Analog_1);
            Analog_1.setBackgroundColor(getColor.getAnalogousColors(baseColor).get(1));
            colors = ((ColorDrawable) Analog_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_1.setText(hexColor);
            Analog_1.setTextColor(inverted);

            TextView Analog_2 = (TextView) layout.findViewById(R.id.Analog_2);
            Analog_2.setBackgroundColor(getColor.getAnalogousColors(baseColor).get(2));
            colors = ((ColorDrawable) Analog_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_2.setText(hexColor);
            Analog_2.setTextColor(inverted);

            TextView Tri_0 = (TextView) layout.findViewById(R.id.Tri_0);
            Tri_0.setBackgroundColor(getColor.getTriadicColors(baseColor).get(0));
            colors = ((ColorDrawable) Tri_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_0.setText(hexColor);
            Tri_0.setTextColor(inverted);

            TextView Tri_1 = (TextView) layout.findViewById(R.id.Tri_1);
            Tri_1.setBackgroundColor(getColor.getTriadicColors(baseColor).get(1));
            colors = ((ColorDrawable) Tri_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_1.setText(hexColor);
            Tri_1.setTextColor(inverted);

            TextView Tri_2 = (TextView) layout.findViewById(R.id.Tri_2);
            Tri_2.setBackgroundColor(getColor.getTriadicColors(baseColor).get(2));
            colors = ((ColorDrawable) Tri_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_2.setText(hexColor);
            Tri_2.setTextColor(inverted);

            RGB_Value.setTextColor(inverted);
            Hex.setTextColor(inverted);
            HSV.setTextColor(inverted);
            LAB_Value.setTextColor(inverted);
            Input.setTextColor(inverted);
            Input.setHintTextColor(inverted);
            CMYK.setTextColor(inverted);
            Ok.setTextColor(inverted);
            checkBox.setTextColor(inverted);

            //Adds colors to database and to palette

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (checkBox.isChecked()) {

                        Ok.setText("Add");
                    } else {
                        Ok.setText("Exit");
                    }
                }
            });

            Ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String color = Input.getText().toString();

                    if (Ok.getText().toString().equals("Add") && getColor.CheckColor(color)) {

                        colorDatabase = new ColorDatabase(color.replace(" ", ""), hex, rgbHolder, hsv, cmyk, lab);

                        dbHandler.addFavorite(colorDatabase);

                        favEntries = dbHandler.getFavorites();
                        String newColor = favEntries.get(favEntries.size() - 1).getColor();
                        favArray.add(newColor);

                        pw.dismiss();

                    } else {

                        if (Ok.getText().toString().equals("Add") && getColor.CheckColor(color) == false) {


                            Context context = getApplicationContext();
                            CharSequence text = "Invalid entry: The name must have at least 4 character";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            // pw.dismiss();
                        } else {
                            pw.dismiss();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopUp(GetColor.ColorComponet clickedBaseColor) {

        final Button Ok;
        CheckBox Box;
        final String rgbValue, hex, hsv;
        String hexColor;
        int colors;

        try {
            // We need to get the instance of the LayoutInflater
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_color_info,
                    (ViewGroup) findViewById(R.id.PopUp));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            pw = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            //int backgroundColor = Color.rgb(baseColor.getRed(),baseColor.getGreen(),baseColor.getBlue());

            Ok = (Button) layout.findViewById(R.id.okButton);

            checkBox = (CheckBox) layout.findViewById(R.id.checkbox);

            final EditText Input = (EditText) layout.findViewById(R.id.ColorName);
            final TextView Hex = (TextView) layout.findViewById(R.id.Hex);
            final TextView RGB_Value = (TextView) layout.findViewById(R.id.RGB_Value);
            final TextView CMYK = (TextView) layout.findViewById(R.id.CMYK_Value);
            final TextView HSV = (TextView) layout.findViewById(R.id.HSV_Value);
            final TextView LAB_Value = (TextView) layout.findViewById(R.id.Lab_Value);

            RGB[0] = clickedBaseColor.getRed();
            RGB[1] = clickedBaseColor.getGreen();
            RGB[2] = clickedBaseColor.getBlue();

            Input.hasFocus();

            RGB_Value.setText("RGB: " + RGB[0] + "," + RGB[1] + "," + RGB[2]);
            colors = Color.rgb(clickedBaseColor.getRed(), clickedBaseColor.getGreen(), clickedBaseColor.getBlue());
            hexColor = String.format("Hex: " + "#%06X", (0xFFFFFF & colors));
            Hex.setText(hexColor);
            rgbValue = RGB_Value.getText().toString();
            final String rgbHolder = rgbValue.substring(5, rgbValue.length()).trim();

            hex = (Hex.getText().toString().substring(5, Hex.getText().toString().length())).trim();
            Color.RGBToHSV(RGB[0], RGB[1], RGB[2], HSV_Array);
            hsv = (String.valueOf(Math.round((HSV_Array[0]))*100/100f) + "," +
                    String.valueOf(Math.round((HSV_Array[1]))*100/100f) + "," +
                    String.valueOf(Math.round((HSV_Array[2]))*100/100f));
            HSV.setText("HSV: " + String.valueOf(HSV_Array[0]) + "," + String.valueOf(HSV_Array[1]) + "," + String.valueOf(HSV_Array[2]));

            final String cmyk = (getColor.getCMYK(String.valueOf(RGB[0]) + "," + String.valueOf(RGB[1]) + "," + String.valueOf(RGB[2])));
            ColorUtil.RGBToLAB(RGB[0], RGB[1], RGB[2], LAB);
            LAB[0] = ((Math.round(((LAB[0])) * 100) / 100));
            LAB[1] = ((Math.round(((LAB[1])) * 100) / 100));
            LAB[2] = ((Math.round(((LAB[2])) * 100) / 100));

            final String lab = String.valueOf(LAB[0]) + "," + String.valueOf(LAB[1]) + "," + String.valueOf(LAB[2]);

            CMYK.setText("CMYK: " + cmyk);
            LAB_Value.setText("Lab: " + lab);


            //baseColor = getColor.new ColorComponet(RGB[0],RGB[1],RGB[2]);
            int color = Color.rgb(clickedBaseColor.getRed(), clickedBaseColor.getGreen(), clickedBaseColor.getBlue());
            layout.setBackgroundColor(color);

            List<GetColor.ColorComponet> ColorShadesList = getColor.calculateShades(clickedBaseColor, 5);
            List<Integer> tints = new ArrayList<>(getColor.calculateTints(clickedBaseColor, 5));

            TextView Compliment = (TextView) layout.findViewById(R.id.Complimentary_View);
            int inverted = getColor.getComplementaryColor(Color.rgb(clickedBaseColor.getRed(), clickedBaseColor.getGreen(),
                    clickedBaseColor.getBlue()));
            Compliment.setBackgroundColor(inverted);
            colors = ((ColorDrawable) Compliment.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Compliment.setText(hexColor);
            Compliment.setTextColor(inverted);


            TextView Shade_0 = (TextView) layout.findViewById(R.id.Shade_0);
            Shade_0.setBackgroundColor(Color.rgb(ColorShadesList.get(0).getRed(), ColorShadesList.get(0).getGreen(), ColorShadesList.get(0).getBlue()));
            colors = ((ColorDrawable) Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_0.setText(hexColor);
            Shade_0.setTextColor(inverted);

            TextView Shade_1 = (TextView) layout.findViewById(R.id.Shade_1);
            Shade_1.setBackgroundColor(Color.rgb(ColorShadesList.get(1).getRed(), ColorShadesList.get(1).getGreen(), ColorShadesList.get(1).getBlue()));
            colors = ((ColorDrawable) Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_1.setText(hexColor);
            Shade_1.setTextColor(inverted);

            TextView Shade_2 = (TextView) layout.findViewById(R.id.Shade_2);
            Shade_2.setBackgroundColor(Color.rgb(ColorShadesList.get(2).getRed(), ColorShadesList.get(2).getGreen(), ColorShadesList.get(2).getBlue()));
            colors = ((ColorDrawable) Shade_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_2.setText(hexColor);
            Shade_2.setTextColor(inverted);


            TextView Shade_3 = (TextView) layout.findViewById(R.id.Shade_3);
            Shade_3.setBackgroundColor(Color.rgb(ColorShadesList.get(3).getRed(), ColorShadesList.get(3).getGreen(), ColorShadesList.get(3).getBlue()));
            colors = ((ColorDrawable) Shade_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_3.setText(hexColor);
            Shade_3.setTextColor(inverted);

            TextView Shade_4 = (TextView) layout.findViewById(R.id.Shade_4);
            Shade_4.setBackgroundColor(Color.rgb(ColorShadesList.get(4).getRed(), ColorShadesList.get(4).getGreen(), ColorShadesList.get(4).getBlue()));
            colors = ((ColorDrawable) Shade_4.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_4.setText(hexColor);
            Shade_4.setTextColor(inverted);


            TextView Tint_0 = (TextView) layout.findViewById(R.id.Tint_0);
            Tint_0.setBackgroundColor((tints.get(0)));
            color = ((ColorDrawable) Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_0.setText(hexColor);
            Tint_0.setTextColor(inverted);

            TextView Tint_1 = (TextView) layout.findViewById(R.id.Tint_1);
            Tint_1.setBackgroundColor(tints.get(1));
            color = ((ColorDrawable) Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_1.setText(hexColor);
            Tint_1.setTextColor(inverted);

            TextView Tint_2 = (TextView) layout.findViewById(R.id.Tint_2);
            Tint_2.setBackgroundColor(tints.get(2));
            color = ((ColorDrawable) Tint_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_2.setText(hexColor);
            Tint_2.setTextColor(inverted);

            TextView Tint_3 = (TextView) layout.findViewById(R.id.Tint_3);
            Tint_3.setBackgroundColor(tints.get(3));
            color = ((ColorDrawable) Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_3.setText(hexColor);
            Tint_3.setTextColor(inverted);

            TextView Tint_4 = (TextView) layout.findViewById(R.id.Tint_4);
            Tint_4.setBackgroundColor(tints.get(4));
            color = ((ColorDrawable) Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_4.setText(hexColor);
            Tint_4.setTextColor(inverted);

            TextView Analog_0 = (TextView) layout.findViewById(R.id.Analog_0);
            Analog_0.setBackgroundColor(getColor.getAnalogousColors(clickedBaseColor).get(0));
            colors = ((ColorDrawable) Analog_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_0.setText(hexColor);
            Analog_0.setTextColor(inverted);


            TextView Analog_1 = (TextView) layout.findViewById(R.id.Analog_1);
            Analog_1.setBackgroundColor(getColor.getAnalogousColors(clickedBaseColor).get(1));
            colors = ((ColorDrawable) Analog_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_1.setText(hexColor);
            Analog_1.setTextColor(inverted);

            TextView Analog_2 = (TextView) layout.findViewById(R.id.Analog_2);
            Analog_2.setBackgroundColor(getColor.getAnalogousColors(clickedBaseColor).get(2));
            colors = ((ColorDrawable) Analog_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_2.setText(hexColor);
            Analog_2.setTextColor(inverted);

            TextView Tri_0 = (TextView) layout.findViewById(R.id.Tri_0);
            Tri_0.setBackgroundColor(getColor.getTriadicColors(clickedBaseColor).get(0));
            colors = ((ColorDrawable) Tri_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_0.setText(hexColor);
            Tri_0.setTextColor(inverted);

            TextView Tri_1 = (TextView) layout.findViewById(R.id.Tri_1);
            Tri_1.setBackgroundColor(getColor.getTriadicColors(clickedBaseColor).get(1));
            colors = ((ColorDrawable) Tri_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_1.setText(hexColor);
            Tri_1.setTextColor(inverted);

            TextView Tri_2 = (TextView) layout.findViewById(R.id.Tri_2);
            Tri_2.setBackgroundColor(getColor.getTriadicColors(clickedBaseColor).get(2));
            colors = ((ColorDrawable) Tri_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_2.setText(hexColor);
            Tri_2.setTextColor(inverted);

            RGB_Value.setTextColor(inverted);
            Hex.setTextColor(inverted);
            HSV.setTextColor(inverted);
            LAB_Value.setTextColor(inverted);
            Input.setTextColor(inverted);
            Input.setHintTextColor(inverted);
            CMYK.setTextColor(inverted);
            Ok.setTextColor(inverted);
            checkBox.setTextColor(inverted);

            //Adds colors to database and to palette

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (checkBox.isChecked()) {

                        Ok.setText("Add");
                    } else {
                        Ok.setText("Exit");
                    }
                }
            });

            Ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String color = Input.getText().toString();

                    if (Ok.getText().toString().equals("Add") && getColor.CheckColor(color)) {

                        colorDatabase = new ColorDatabase(color.replace(" ", ""), hex, rgbHolder, hsv, cmyk, lab);

                        dbHandler.addFavorite(colorDatabase);

                        favEntries = dbHandler.getFavorites();
                        String newColor = favEntries.get(favEntries.size() - 1).getColor();
                        favArray.add(newColor);

                        pw.dismiss();

                    } else {

                        if (Ok.getText().toString().equals("Add") && getColor.CheckColor(color) == false) {


                            Context context = getApplicationContext();
                            CharSequence text = "Invalid entry: The name must have at least 4 character";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            // pw.dismiss();
                        } else {
                            pw.dismiss();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position > 0) {

                int choice = ((int) parent.getItemIdAtPosition(position));

                Top_rgbArray = favEntries.get(choice - 1).getRgb().split(",");

                Red_View01.setText(Top_rgbArray[0]);
                Top_1.setProgress(Integer.valueOf(Top_rgbArray[0]));
                Green_View01.setText(Top_rgbArray[1]);
                Top_2.setProgress(Integer.valueOf(Top_rgbArray[1]));
                Blue_View01.setText(Top_rgbArray[2]);
                Top_3.setProgress(Integer.valueOf(Top_rgbArray[2]));

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Spinner.OnItemSelectedListener onItemSelectedListener2 = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position > 0) {

                int choice = ((int) parent.getItemIdAtPosition(position));

                Bottom_rgbArray = favEntries.get(choice - 1).getRgb().split(",");

                Red_View02.setText(Bottom_rgbArray[0]);
                Bottom_1.setProgress(Integer.valueOf(Bottom_rgbArray[0]));
                Green_View02.setText(Bottom_rgbArray[1]);
                Bottom_2.setProgress(Integer.valueOf(Bottom_rgbArray[1]));
                Blue_View02.setText(Bottom_rgbArray[2]);
                Bottom_3.setProgress(Integer.valueOf(Bottom_rgbArray[2]));

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (seekBar.getId()) {

                case R.id.seekBar1:

                    Red_View01.setText(String.valueOf(progress));

                    if (Blend_CheckBox.isChecked()) {

                        blendColor();
                    } else {

                        singleColor();
                    }
                    break;

                case R.id.seekBar2:

                    Green_View01.setText(String.valueOf(progress));

                    if (Blend_CheckBox.isChecked()) {

                        blendColor();
                    } else {

                        singleColor();
                    }
                    break;

                case R.id.seekBar3:

                    Blue_View01.setText(String.valueOf(progress));
                    if (Blend_CheckBox.isChecked()) {

                        blendColor();
                    } else {

                        singleColor();
                    }
                    break;

                case R.id.seekBar4:

                    Red_View02.setText(String.valueOf(progress));
                    blendColor();

                    break;

                case R.id.seekBar5:

                    Green_View02.setText(String.valueOf(progress));
                    blendColor();

                    break;

                case R.id.seekBar6:

                    Blue_View02.setText(String.valueOf(progress));
                    blendColor();

                    break;

            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    };

    Button.OnClickListener onClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            int color = (int) v.getTag(v.getId());
            String hexColor = String.format("#%06X", (0xFFFFFF & color));
            //  Log.i("BUTTON TAG", hexColor);
            GetColor.ColorComponet clickedBaseColor = getColor.new ColorComponet(Color.red(color), Color.green(color), Color.blue(color));

            showPopUp(clickedBaseColor);

        }
    };

    private void singleColor() {
        int Red1, Green1, Blue1;
        String Red1_Value = Red_View01.getText().toString();
        String Green1_Value = Green_View01.getText().toString();
        String Blue1_Value = Blue_View01.getText().toString();


        if (Red1_Value == "") {
            Red1 = 0;
        } else {
            Red1 = Integer.valueOf(Red1_Value);
        }
        if (Green1_Value == "") {
            Green1 = 0;
        } else {

            Green1 = Integer.valueOf(Green1_Value);
        }
        if (Blue1_Value == "") {
            Blue1 = 0;
        } else {
            Blue1 = Integer.valueOf(Blue1_Value);
        }


        Top[0] = Red1;
        Top[1] = Green1;
        Top[2] = Blue1;
        int color1 = Color.rgb(Red1, Green1, Blue1);


        baseColor = getColor.new ColorComponet(Red1, Green1, Blue1);

        Preview.setBackgroundColor(Color.rgb(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue()));
        ColorUtil.colorToLAB(color1, LAB);
        Top_rgbArray[0] = Red1_Value;
        Top_rgbArray[1] = Green1_Value;
        Top_rgbArray[2] = Blue1_Value;
        Preview.setText(getColor.mostSimilar(LAB[0], LAB[1], LAB[2], ColorArray, Top));

        shades = getColor.calculateShades(baseColor, 5);
        List<Integer> tints = new ArrayList<>(getColor.calculateTints(baseColor, 5));

        int colors;
        String hexColor;
        int toInvert = Color.rgb(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        int inverted = getColor.getComplementaryColor(toInvert);

        Button Shade_0 = (Button) findViewById(R.id.Shade_0);
        Shade_0.setOnClickListener(onClickListener);
        Shade_0.setBackgroundColor(Color.rgb(shades.get(0).getRed(), shades.get(0).getGreen(), shades.get(0).getBlue()));
        colors = ((ColorDrawable) Shade_0.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_0.setText(hexColor);
        Shade_0.setTextColor(inverted);
        Shade_0.setTag(Shade_0.getId(), colors);

        Button Shade_1 = (Button) findViewById(R.id.Shade_1);
        Shade_1.setOnClickListener(onClickListener);
        Shade_1.setBackgroundColor(Color.rgb(shades.get(1).getRed(), shades.get(1).getGreen(), shades.get(1).getBlue()));
        colors = ((ColorDrawable) Shade_1.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_1.setText(hexColor);
        Shade_1.setTextColor(inverted);
        Shade_1.setTag(Shade_1.getId(), colors);


        Button Shade_2 = (Button) findViewById(R.id.Shade_2);
        Shade_2.setBackgroundColor(Color.rgb(shades.get(2).getRed(), shades.get(2).getGreen(), shades.get(2).getBlue()));
        colors = ((ColorDrawable) Shade_2.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_2.setText(hexColor);
        Shade_2.setTextColor(inverted);
        Shade_2.setOnClickListener(onClickListener);
        Shade_2.setTag(Shade_2.getId(), colors);


        Button Shade_3 = (Button) findViewById(R.id.Shade_3);
        Shade_3.setBackgroundColor(Color.rgb(shades.get(3).getRed(), shades.get(3).getGreen(), shades.get(3).getBlue()));
        colors = ((ColorDrawable) Shade_3.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_3.setText(hexColor);
        Shade_3.setTextColor(inverted);
        Shade_3.setOnClickListener(onClickListener);
        Shade_3.setTag(Shade_3.getId(), colors);


        Button Shade_4 = (Button) findViewById(R.id.Shade_4);
        Shade_4.setBackgroundColor(Color.rgb(shades.get(4).getRed(), shades.get(4).getGreen(), shades.get(4).getBlue()));
        colors = ((ColorDrawable) Shade_4.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_4.setText(hexColor);
        Shade_4.setTextColor(inverted);
        Shade_4.setOnClickListener(onClickListener);
        Shade_4.setTag(Shade_4.getId(), colors);


        Button Tint_0 = (Button) findViewById(R.id.Tint_0);
        Tint_0.setBackgroundColor((tints.get(0)));
        colors = ((ColorDrawable) Shade_0.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_0.setText(hexColor);
        Tint_0.setTextColor(inverted);
        Tint_0.setOnClickListener(onClickListener);
        Tint_0.setTag(Tint_0.getId(), colors);


        Button Tint_1 = (Button) findViewById(R.id.Tint_1);
        Tint_1.setBackgroundColor(tints.get(1));
        colors = ((ColorDrawable) Shade_1.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_1.setText(hexColor);
        Tint_1.setTextColor(inverted);
        Tint_1.setOnClickListener(onClickListener);
        Tint_1.setTag(Tint_1.getId(), colors);


        Button Tint_2 = (Button) findViewById(R.id.Tint_2);
        Tint_2.setBackgroundColor(tints.get(2));
        colors = ((ColorDrawable) Tint_2.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_2.setText(hexColor);
        Tint_2.setTextColor(inverted);
        Tint_2.setOnClickListener(onClickListener);
        Tint_2.setTag(Tint_2.getId(), colors);


        Button Tint_3 = (Button) findViewById(R.id.Tint_3);
        Tint_3.setBackgroundColor(tints.get(3));
        colors = ((ColorDrawable) Tint_3.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_3.setText(hexColor);
        Tint_3.setTextColor(inverted);
        Tint_3.setOnClickListener(onClickListener);
        Tint_3.setTag(Tint_3.getId(), colors);


        Button Tint_4 = (Button) findViewById(R.id.Tint_4);
        Tint_4.setBackgroundColor(tints.get(4));
        colors = ((ColorDrawable) Tint_3.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_4.setText(hexColor);
        Tint_4.setTextColor(inverted);
        Tint_4.setOnClickListener(onClickListener);
        Tint_4.setTag(Tint_4.getId(), colors);

    }

    private void blendColor() {

        int Red1, Green1, Blue1, Red2, Green2, Blue2;
        String Red1_Value = Red_View01.getText().toString();
        String Green1_Value = Green_View01.getText().toString();
        String Blue1_Value = Blue_View01.getText().toString();
        String Red2_Value = Red_View02.getText().toString();
        String Green2_Value = Green_View02.getText().toString();
        String Blue2_Value = Blue_View02.getText().toString();

        if (Red1_Value == "") {
            Red1 = 0;
        } else {
            Red1 = Integer.valueOf(Red1_Value);
        }
        if (Green1_Value == "") {
            Green1 = 0;
        } else {

            Green1 = Integer.valueOf(Green1_Value);
        }
        if (Blue1_Value == "") {
            Blue1 = 0;
        } else {
            Blue1 = Integer.valueOf(Blue1_Value);
        }
        if (Red2_Value == "") {
            Red2 = 0;
        } else {
            Red2 = Integer.valueOf(Red2_Value);
        }
        if (Green2_Value == "") {
            Green2 = 0;
        } else {
            Green2 = Integer.valueOf(Green2_Value);
        }
        if (Blue2_Value == "") {
            Blue2 = 0;
        } else {
            Blue2 = Integer.valueOf(Blue2_Value);
        }

        Top[0] = Red1;
        Top[1] = Green1;
        Top[2] = Blue1;

        Bottom[0] = Red2;
        Bottom[1] = Green2;
        Bottom[2] = Blue2;

        int color1 = Color.rgb(Red1, Green1, Blue1);
        int color2 = Color.rgb(Red2, Green2, Blue2);


        int blendedColor = ColorUtil.blendARGB(color1, color2, .5f);
        int blendedRed = Color.red(blendedColor);
        int blendedGreen = Color.green(blendedColor);
        int blendedBlue = Color.blue(blendedColor);

        int[] blendedColorArray = {blendedRed, blendedGreen, blendedBlue};
        baseColor = getColor.new ColorComponet(blendedRed, blendedGreen, blendedBlue);
        ColorUtil.colorToLAB(blendedColor, LAB);
        Preview.setText(getColor.mostSimilar(LAB[0], LAB[1], LAB[2], ColorArray, blendedColorArray));
        Preview.setBackgroundColor(ColorUtil.blendARGB(color1, color2, .5f));

        shades = getColor.calculateShades(baseColor, 5);
        List<Integer> tints = new ArrayList<>(getColor.calculateTints(baseColor, 5));

        int colors;
        String hexColor;
        int toInvert = Color.rgb(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        int inverted = getColor.getComplementaryColor(toInvert);

        Button Shade_0 = (Button) findViewById(R.id.Shade_0);
        Shade_0.setOnClickListener(onClickListener);
        Shade_0.setBackgroundColor(Color.rgb(shades.get(0).getRed(), shades.get(0).getGreen(), shades.get(0).getBlue()));
        colors = ((ColorDrawable) Shade_0.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_0.setText(hexColor);
        Shade_0.setTextColor(inverted);
        Shade_0.setTag(Shade_0.getId(), colors);

        Button Shade_1 = (Button) findViewById(R.id.Shade_1);
        Shade_1.setOnClickListener(onClickListener);
        Shade_1.setBackgroundColor(Color.rgb(shades.get(1).getRed(), shades.get(1).getGreen(), shades.get(1).getBlue()));
        colors = ((ColorDrawable) Shade_1.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_1.setText(hexColor);
        Shade_1.setTextColor(inverted);
        Shade_1.setTag(Shade_1.getId(), colors);


        Button Shade_2 = (Button) findViewById(R.id.Shade_2);
        Shade_2.setBackgroundColor(Color.rgb(shades.get(2).getRed(), shades.get(2).getGreen(), shades.get(2).getBlue()));
        colors = ((ColorDrawable) Shade_2.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_2.setText(hexColor);
        Shade_2.setTextColor(inverted);
        Shade_2.setOnClickListener(onClickListener);
        Shade_2.setTag(Shade_2.getId(), colors);


        Button Shade_3 = (Button) findViewById(R.id.Shade_3);
        Shade_3.setBackgroundColor(Color.rgb(shades.get(3).getRed(), shades.get(3).getGreen(), shades.get(3).getBlue()));
        colors = ((ColorDrawable) Shade_3.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_3.setText(hexColor);
        Shade_3.setTextColor(inverted);
        Shade_3.setOnClickListener(onClickListener);
        Shade_3.setTag(Shade_3.getId(), colors);


        Button Shade_4 = (Button) findViewById(R.id.Shade_4);
        Shade_4.setBackgroundColor(Color.rgb(shades.get(4).getRed(), shades.get(4).getGreen(), shades.get(4).getBlue()));
        colors = ((ColorDrawable) Shade_4.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Shade_4.setText(hexColor);
        Shade_4.setTextColor(inverted);
        Shade_4.setOnClickListener(onClickListener);
        Shade_4.setTag(Shade_4.getId(), colors);


        Button Tint_0 = (Button) findViewById(R.id.Tint_0);
        Tint_0.setBackgroundColor((tints.get(0)));
        colors = ((ColorDrawable) Shade_0.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_0.setText(hexColor);
        Tint_0.setTextColor(inverted);
        Tint_0.setOnClickListener(onClickListener);
        Tint_0.setTag(Tint_0.getId(), colors);


        Button Tint_1 = (Button) findViewById(R.id.Tint_1);
        Tint_1.setBackgroundColor(tints.get(1));
        colors = ((ColorDrawable) Shade_1.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_1.setText(hexColor);
        Tint_1.setTextColor(inverted);
        Tint_1.setOnClickListener(onClickListener);
        Tint_1.setTag(Tint_1.getId(), colors);


        Button Tint_2 = (Button) findViewById(R.id.Tint_2);
        Tint_2.setBackgroundColor(tints.get(2));
        colors = ((ColorDrawable) Tint_2.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_2.setText(hexColor);
        Tint_2.setTextColor(inverted);
        Tint_2.setOnClickListener(onClickListener);
        Tint_2.setTag(Tint_2.getId(), colors);


        Button Tint_3 = (Button) findViewById(R.id.Tint_3);
        Tint_3.setBackgroundColor(tints.get(3));
        colors = ((ColorDrawable) Tint_3.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_3.setText(hexColor);
        Tint_3.setTextColor(inverted);
        Tint_3.setOnClickListener(onClickListener);
        Tint_3.setTag(Tint_3.getId(), colors);


        Button Tint_4 = (Button) findViewById(R.id.Tint_4);
        Tint_4.setBackgroundColor(tints.get(4));
        colors = ((ColorDrawable) Tint_3.getBackground()).getColor();
        hexColor = String.format("#%06X", (0xFFFFFF & colors));
        Tint_4.setText(hexColor);
        Tint_4.setTextColor(inverted);
        Tint_4.setOnClickListener(onClickListener);
        Tint_4.setTag(Tint_4.getId(), colors);
    }

    class ColorTextWatcher implements TextWatcher {

        View view;

        private ColorTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = s.toString();
            int red, green, blue, convertedColor;

            boolean result = getColor.CheckFormat(text);
            boolean rgbResult = getColor.CheckRGBFormat(text);
            int [] rgbInputArray = new int [3];
            String []rgbStringArray = new String [3];
            if (text.length() == 7 && !result && text.charAt(0) == '#' && SwitchFormat.getText().equals(HexFormat)) {

                switch (view.getId()) {

                    case R.id.Top_Hex:
                        Log.i("EDITOR", text);

                        convertedColor = Color.parseColor(text);
                        red = Color.red(convertedColor);
                        green = Color.green(convertedColor);
                        blue = Color.blue(convertedColor);

                        Top_1.setProgress(red);
                        Top_2.setProgress(green);
                        Top_3.setProgress(blue);

                        break;

                    case R.id.Bottom_Hex:

                        convertedColor = Color.parseColor(text);

                        red = Color.red(convertedColor);
                        green = Color.green(convertedColor);
                        blue = Color.blue(convertedColor);

                        Bottom_1.setProgress(red);
                        Bottom_2.setProgress(green);
                        Bottom_3.setProgress(blue);

                        break;
                }
            }
                else{

                    if (text.length() == 11 && SwitchFormat.getText().equals(RGBFormat) &&
                            !rgbResult) {
                        switch (view.getId()) {

                            case R.id.Top_Hex:
                                Log.i("RGB EDITOR", text);

                         /*   convertedColor = Color.parseColor(text);
                            red = Color.red(convertedColor);
                            green = Color.green(convertedColor);
                            blue = Color.blue(convertedColor);*/

                                rgbStringArray = text.split(",");

                                Top_1.setProgress(Integer.valueOf(rgbStringArray[0]));
                                Top_2.setProgress(Integer.valueOf(rgbStringArray[1]));
                                Top_3.setProgress(Integer.valueOf(rgbStringArray[2]));


                                break;

                            case R.id.Bottom_Hex:

                                rgbStringArray = text.split(",");

                                Bottom_1.setProgress(Integer.valueOf(rgbStringArray[0]));
                                Bottom_2.setProgress(Integer.valueOf(rgbStringArray[1]));
                                Bottom_3.setProgress(Integer.valueOf(rgbStringArray[2]));

                                break;
                        }
                    }
                }
            }

        }
    }








