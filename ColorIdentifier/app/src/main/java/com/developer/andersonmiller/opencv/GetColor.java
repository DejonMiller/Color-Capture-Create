package com.developer.andersonmiller.opencv;


import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import com.developer.andersonmiller.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetColor {

    double R,G,B;
    protected String[] colorArray;
    protected String []seperateValues;
    protected String ColorList;
    protected String CMYK;

    DatabaseHandler db;

    public String[] getColor() {

        colorArray = ColorList.split(System.getProperty("line.separator"));

        return colorArray;
    }

    public void setColor(String ColorList) {
        this.ColorList = ColorList;
      //  System.out.println(ColorList);
    }

    public int[] HexToRGB(String hex) {
        int []rgb = new int[3];
        String hexColor = hex.substring(1, hex.length()).trim();
        String red = hexColor.substring(0, 2).trim();
        String green = hexColor.substring(2, 4).trim();
        String blue = hexColor.substring(4, 6).trim();

        String Red = Integer.toBinaryString(Integer.parseInt(red, 16)).toString();
        String Green = Integer.toBinaryString(Integer.parseInt(green, 16)).toString();
        String Blue = Integer.toBinaryString(Integer.parseInt(blue, 16)).toString();

        int R = Integer.parseInt(Red, 2);
        int G = Integer.parseInt(Green, 2);
        int B= Integer.parseInt(Blue, 2);

        //String RGB = Red + "," + Green + "," + Blue;
        rgb[0]=R;
        rgb[1]=G;
        rgb[2]=B;

        return rgb;
    }

    public String HexToRBG(String hex) {
        String hexColor = hex.substring(1, hex.length()).trim();
        String red = hexColor.substring(0, 2).trim();
        String green = hexColor.substring(2, 4).trim();
        String blue = hexColor.substring(4, 6).trim();

        String Red = Integer.toBinaryString(Integer.parseInt(red, 16)).toString();
        String Green = Integer.toBinaryString(Integer.parseInt(green, 16)).toString();
        String Blue = Integer.toBinaryString(Integer.parseInt(blue, 16)).toString();

        Red = String.valueOf(Integer.parseInt(Red, 2));
        Green = String.valueOf(Integer.parseInt(Green, 2));
        Blue = String.valueOf(Integer.parseInt(Blue, 2));

        String RGB = Red + "," + Green + "," + Blue;


        return RGB;
    }

    //sqrt (l^2 + a^2 + b^2
    /* Finds the most similar color by comparing lab values a

    * deltaE = sqrt(deltaL^2 + deltaA^2 + deltaB^2)
    * */

    public String mostSimilar(double L, double A, double B , String[]input, int[]rgb){

        String [] labRow = new String[3];
        String [] row = new String[3];
        String []rgbRow = new String[3];
        double rowL, rowA, rowB;
        int rowR, rowG, rowBl;
        double count =0;
        double temp =0;
        String MostSimilar="";


    for (int i = 0; i < input.length; i++) {

        row = input[i].split(" ");
        labRow = row[5].split(",");
        rgbRow = row[2].split(",");

        rowR = Integer.valueOf(rgbRow[0]);
        rowG = Integer.valueOf(rgbRow[1]);
        rowBl = Integer.valueOf(rgbRow[2]);

        rowL = Double.valueOf(labRow[0]);
        rowA = Double.valueOf(labRow[1]);
        rowB = Double.valueOf(labRow[2]);

        double delta = Math.sqrt(((L - rowL) * (L - rowL)) + ((A - rowA) * (A - rowA)) + (B - rowB) * (B - rowB));

        if (count == 0) {
            temp = delta;
            MostSimilar = "Black";
            count++;
        } else {

            if (rgb[0] == rowR && rgb[1] == rowG && rgb[2] == rowBl) {

                return "Color: " + row[0];

            } else {
                if (delta < temp) {
                    temp = delta;
                    MostSimilar = row[0];

                }
            }
        }
    }

        return "Similar to: "+MostSimilar;
    }

    public String getCMYK(String inputRGB){

       // inputRGB = inputRGB.substring(5,inputRGB.length()).trim();
         String[] rgbArray = inputRGB.split(",");
        int red = Integer.valueOf(rgbArray[0]);
        int green = Integer.valueOf(rgbArray[1]);
        int blue = Integer.valueOf(rgbArray[2]);

        float r = red/255;
        float g =  green/255;
        float b = blue/255;

        float bl = Math.min(Math.min(1 - r, 1 - g), 1 - b);
        float black = Math.min(Math.min(255 - red, 255 - green), 255 - blue);



        //  black = Math.round(black)*1000/1000;

        if (black!=255) {
            float cyan    = ((255-red-black)/(255-black));
        //    cyan = Math.round(cyan)*1000/1000;
            float magenta = (255-green-black)/(255-black);
          //  magenta = Math.round(magenta)*1000/1000;
            float yellow  = (255-blue-black)/(255-black);
         //   yellow = Math.round(yellow)*1000/1000;
            return String.valueOf(cyan)+","+String.valueOf(magenta)+","+String.valueOf(yellow)+","+String.valueOf(bl);

        } else {

            float cyan = 255 - red;
            float magenta = 255 - green;
            float yellow = 255 - blue;
           // return cyan+", "+magenta+", "+yellow+", "+black;*/
            return String.valueOf(cyan)+","+String.valueOf(magenta)+","+String.valueOf(yellow)+","+String.valueOf(bl);

        }
    }

    //Same as HSB
    public String getHSV(String inputRGB){

        inputRGB = inputRGB.substring(5,inputRGB.length()).trim();
        String[] rgbArray = inputRGB.split(",");
        int red = Integer.valueOf(rgbArray[0]);
        int green = Integer.valueOf(rgbArray[1]);
        int blue = Integer.valueOf(rgbArray[2]);

        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);

        String r = String.valueOf(hsv[0]);
        String g= String.valueOf(hsv[1]*100);
        String b = String.valueOf(hsv[2] * 100);

        return r+","+g+","+b;
//hsv contains the desired values

    }

    public  int getComplementaryColor(int colorToInvert) {
        Log.i("String HERE", String.valueOf(colorToInvert));

        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(colorToInvert), Color.green(colorToInvert),
                Color.blue(colorToInvert), hsv);
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }

    //Found by taking the hsl and +/- 30%. If n + 30% > 360 || n-30% < 0 then wraparound
    public List<Integer> getAnalogousColors(FavEntry mFavEntry){
        float [] hslArray = new float[3];
        List<Integer> colors = new ArrayList<Integer>();
        String []rgbArray = mFavEntry.getRgb().split(",");


        ColorUtil.RGBToHSL(Integer.valueOf(rgbArray[0]), Integer.valueOf(rgbArray[1]), Integer.valueOf(rgbArray[2]), hslArray);

        double hue = hslArray[0];
        double maxHue = 360;
        double lowerHue = hue -30;
        double highHue = 30 + hue;

        colors.add(ColorUtil.HSLToColor(hslArray));

        if (lowerHue <= 0){

            lowerHue = lowerHue + maxHue;
        }
       hslArray[0]=(float)lowerHue;
        colors.add(ColorUtil.HSLToColor(hslArray));
        if (highHue >= 360){

            highHue = highHue - maxHue;
        }
        hslArray[0]=(float)highHue;
        colors.add(ColorUtil.HSLToColor(hslArray));

        return colors;

    }


    public List<Integer>getTriadicColors(FavEntry mFavEntry){

        float [] hslArray = new float[3];
        List<Integer> colors = new ArrayList<Integer>();
        String []rgbArray = mFavEntry.getRgb().split(",");


        ColorUtil.RGBToHSL(Integer.valueOf(rgbArray[0]), Integer.valueOf(rgbArray[1]), Integer.valueOf(rgbArray[2]), hslArray);

        double hue = hslArray[0];
        double maxHue = 360;
        double lowerHue = hue -120;
        double highHue = 120 + hue;

        colors.add(ColorUtil.HSLToColor(hslArray));

        if (lowerHue <= 0){

            lowerHue = lowerHue + maxHue;
        }
        hslArray[0]=(float)lowerHue;
        colors.add(ColorUtil.HSLToColor(hslArray));
        if (highHue >= 360){

            highHue = highHue - maxHue;
        }
        hslArray[0]=(float)highHue;
        colors.add(ColorUtil.HSLToColor(hslArray));

        return colors;

    }

   public List<ColorComponet> calculateShades(ColorComponet baseColor, int numberShades)
    {
        //decompose color into RGB
        int redMax  = baseColor.getRed();
        int greenMax  = baseColor.getGreen();
        int blueMax = baseColor.getBlue();


        //Max color component in RGB
        final int MAX_COMPONENT = 255;

        //bin sizes for each color component
        int redDelta = (MAX_COMPONENT - redMax) / numberShades;
        int greenDelta = (MAX_COMPONENT - greenMax) / numberShades;
        int blueDelta = (MAX_COMPONENT - blueMax) / numberShades;

        List<ColorComponet> colors = new ArrayList<ColorComponet>();

        int redCurrent = redMax;
        int greenCurrent = greenMax;
        int blueCurrent = blueMax;

        //now step through each shade, and decrease darkness by adding color to it
        for(int i = 0; i < numberShades; i++)
        {

            //step up by the bin size, but stop at the max color component (255)
            redCurrent = (redCurrent+redDelta) < MAX_COMPONENT ? (redCurrent + redDelta ) : MAX_COMPONENT;
            greenCurrent = (greenCurrent+greenDelta) < MAX_COMPONENT ? (greenCurrent + greenDelta ) : MAX_COMPONENT;
            blueCurrent = (blueCurrent+blueDelta) < MAX_COMPONENT ? (blueCurrent + blueDelta ) : MAX_COMPONENT;

            ColorComponet nextShade = new ColorComponet(redCurrent, greenCurrent, blueCurrent);
            colors.add(nextShade);
        }

        return colors;
    }


    public List<Integer> calculateTints(ColorComponet baseColor, int numberTints) {


        List<Integer> colors = new ArrayList<Integer>();

        float []hslArray = new float[3];

       int convertedColor = Color.rgb(baseColor.getRed(),baseColor.getGreen(),baseColor.getBlue());

        ColorUtils.colorToHSL(convertedColor,hslArray);

        float tint = hslArray[2]* .15f;
        float decreasingTint;


        for ( int i =0; i < numberTints; i++ ){

           decreasingTint = hslArray[2] - tint;
            hslArray[2] = decreasingTint;

            if(hslArray[2] < 0){

                hslArray[2] =0;
            }

            //ColorUtil.
            colors.add(ColorUtil.HSLToColor(hslArray));

        }

        return colors;
    }

    public List<Integer> getAnalogousColors(ColorComponet baseColor){
        float [] hslArray = new float[3];
        List<Integer> colors = new ArrayList<Integer>();

        ColorUtil.RGBToHSL(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hslArray);

        double hue = hslArray[0];
        double maxHue = 360;
        double lowerHue = hue -30;
        double highHue = 30 + hue;

        colors.add(ColorUtil.HSLToColor(hslArray));

        if (lowerHue <= 0){

            lowerHue = lowerHue + maxHue;
        }
        hslArray[0]=(float)lowerHue;
        colors.add(ColorUtil.HSLToColor(hslArray));
        if (highHue >= 360){

            highHue = highHue - maxHue;
        }
        hslArray[0]=(float)highHue;
        colors.add(ColorUtil.HSLToColor(hslArray));

        return colors;

    }


    public List<Integer>getTriadicColors(ColorComponet baseColor){

        float [] hslArray = new float[3];
        List<Integer> colors = new ArrayList<Integer>();

        ColorUtil.RGBToHSL(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hslArray);

        double hue = hslArray[0];
        double maxHue = 360;
        double lowerHue = hue -120;
        double highHue = 120 + hue;

        colors.add(ColorUtil.HSLToColor(hslArray));

        if (lowerHue <= 0){

            lowerHue = lowerHue + maxHue;
        }
        hslArray[0]=(float)lowerHue;
        colors.add(ColorUtil.HSLToColor(hslArray));
        if (highHue >= 360){

            highHue = highHue - maxHue;
        }
        hslArray[0]=(float)highHue;
        colors.add(ColorUtil.HSLToColor(hslArray));

        return colors;

    }

    /* Checks the color name submitted by the user to see if there are no whitespaces, the input name will be added to the
     * database */
    public boolean CheckColor(String input){


        return input.replace(" ", "").length() >= 2;
    }

    public boolean CheckFormat(String input){

      //  if((input.length() ==7 && input.charAt(0)=='#')) {

//            input = input.substring(1, input.length());
            input = input.toUpperCase().trim();
            Log.i("CheckFormat TAG",input);
            for (int i = 0; i < input.length(); i++) {

                char position = input.charAt(i);
                if (position != 'A' || position != 'B' || position != 'C' || position != 'D' || position !=
                        'E' || position != 'F' || position != '1' || position != '2' || position != '3' ||
                        position != '4' || position != '5' || position != '6' || position != '7' || position != '8' || position != '9' || position != '0') {
                    return false;
        //        }
            }

        }

        return true;
    }

    public boolean CheckRGBFormat(String input){
        int count=0;
        Log.i("RGB CHECK FORMAT",input);
        for(int i =0; i < input.length();i++){
            char comma = input.charAt(i);
            if(comma == ','){
                count++;
            }
        }
        if(count!=2){
            return false;
        }

        for(int i = 0; i < input.length(); i++){
            char position = input.charAt(i);
           if( position!=','||position != '1' || position != '2' || position != '3' ||
                    position != '4' || position != '5' || position != '6'
                   || position != '7' || position != '8' || position != '9' || position != '0') {
               return false;
           }
           }

        String [] inputArray = input.split(",");

        for(int i =0; i < inputArray.length; i++){

          if(Integer.valueOf(inputArray[i]) > 255 || Integer.valueOf(inputArray[i])<0){
              return false;
          }

        }
        return true;
    }
    public class ColorComponet{

        private int red,green,blue;

        public ColorComponet(int red, int green, int blue){

            this.red =red;
            this.green = green;
            this.blue = blue;

        }

        public int getRed(){
            return red;
        }
        public int getBlue() {
            return blue;
        }

        public int getGreen(){
            return green;
        }
    }


}



