package com.developer.andersonmiller.opencv;
/*
Using SQLite, if color is not in database, compare the color to the color closest in value and ask
to add color to database. If yes then add to database in order.
 */
/*
Palette feature
color mix + creator

 */
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.developer.andersonmiller.DatabaseHandler;
import com.developer.andersonmiller.Palette;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {


    private CameraBridgeViewBase mOpenCvCameraView;
    private JavaCameraView jCamera;
    private Mat mRgba;
    private Scalar mBlobColorHsv;
    private Scalar mBlobColorRgba;
    private Boolean lightSwitch = false;
    public static Camera camera;
    private boolean hasFlash;
    TextView touch_color;
    TextView touch_TrueColor;
    TextView Similar_Color;
    ImageButton Add;
    Button View_Favorite;
    TextView rgb;
    Switch Light;
    Boolean isChecked = false;
    SeekBar mSeekBar;
    double x = -1;
    double y = -1;
    DatabaseHandler dbHandler;
    GetColor getColor = new GetColor();
    ColorDatabase colorDatabase;
    private CameraManager cameraManager;
    private CameraCharacteristics cameraCharacteristics;
    private CameraDevice mCameraDevice;
    private Camera mCamera;
    private CameraCaptureSession mSession;
    private CaptureRequest.Builder mBuilder;
     CheckBox checkBox;
    int RGB[]= new int[3];
    String stringRGB[] = new String[3];
    double LAB[] = new double [3];
    float HSV_Array[] = new float[3];
    GetColor.ColorComponet baseColor;
    TextView ColorPreview;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();
        dbHandler = new DatabaseHandler(this, null, null, 1);


        if(getResources().getConfiguration().orientation==1) {
           // dbHandler = new DatabaseHandler(this, null, null, 1);
            Intent i = new Intent(context, ColorBlendActivity.class);
            string();
            Bundle b = new Bundle();
            String[]ColorArray;
            ColorArray = getColor.getColor();
            b.putStringArray("AllColors",ColorArray);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
            //Toast toast = Toast.makeText(context, "For Color Creation mode hold device in portrait position.", Toast.LENGTH_SHORT);
            //toast.show();
        //    setContentView(R.layout.landscape_activity_main);
        setContentView(R.layout.landscape_activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.action_bar_main);
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = this.getIntent().getExtras();
        String[] ColorArray = bundle.getStringArray("AllColors");


        //touch_coordinates = (TextView) findViewById(R.id.touch_coordinates);
        ColorPreview = (TextView)findViewById(R.id.ColorPreview);
        touch_color = (TextView) findViewById(R.id.touch_color);
        touch_TrueColor = (TextView) findViewById(R.id.touch_TrueColor);
       // Similar_Color = (TextView) findViewById(R.id.Similar_Color);
       // addPalette = (CheckBox)findViewById(R.id.AddToPallete);
        Add = (ImageButton) findViewById(R.id.Add);
        rgb = (TextView) findViewById(R.id.RGB);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_tutorial_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);


        View_Favorite = (Button)findViewById(R.id.View_Palette_Button);
        View_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent myIntent = new Intent(context, RecordListActivity.class);

                startActivity(myIntent);

            }
        });

        string();
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touch_TrueColor.getText().toString()!=(" Color: ")) {
                    // Add.setEnabled(true);
                    showPopUp();
                }
            }
        });

    }

    private PopupWindow pw;

//When pressing the add button, the color name, hex, and rgb values are added to the database
    public void showPopUp(){
        final Button Ok;
        CheckBox Box;
        final String rgbValue, hex, hsv;
        String hexColor;
        int colors;

        try {
            // We need to get the instance of the LayoutInflater
             final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.activity_color_info,
                        (ViewGroup) findViewById(R.id.PopUp));
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                pw = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT , true);
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            //int backgroundColor = Color.rgb(baseColor.getRed(),baseColor.getGreen(),baseColor.getBlue());

            Ok = (Button) layout.findViewById(R.id.okButton);

            checkBox = (CheckBox)layout.findViewById(R.id.checkbox);

            final EditText Input = (EditText)layout.findViewById(R.id.ColorName);
            final TextView Hex = (TextView)layout.findViewById(R.id.Hex);
            final TextView RGB_Value = (TextView)layout.findViewById(R.id.RGB_Value);
            final TextView CMYK =(TextView)layout.findViewById(R.id.CMYK_Value);
            final  TextView HSV = (TextView)layout.findViewById(R.id.HSV_Value);
            final  TextView LAB_Value = (TextView)layout.findViewById(R.id.Lab_Value);

            Input.hasFocus();


            Hex.setText(touch_color.getText().toString());
            RGB_Value.setText(rgb.getText().toString());

            rgbValue = RGB_Value.getText().toString();
            final String rgbHolder = rgbValue.substring(5,rgbValue.length()).trim();

            hex = (Hex.getText().toString().substring(5, Hex.getText().toString().length())).trim();
            Color.RGBToHSV(RGB[0], RGB[1], RGB[2], HSV_Array);
            hsv = (String.valueOf(HSV_Array[0]) + "," + String.valueOf(HSV_Array[1]) + "," + String.valueOf(HSV_Array[2]));
            HSV.setText("HSV: " + String.valueOf(HSV_Array[0]) + "," + String.valueOf(HSV_Array[1]) + "," + String.valueOf(HSV_Array[2]));

            final String cmyk = (getColor.getCMYK(stringRGB[0]+","+stringRGB[1]+","+stringRGB[2]));
            ColorUtil.RGBToLAB(RGB[0], RGB[1], RGB[2], LAB);
            LAB[0]=((Math.round(((LAB[0])) * 100) / 100f));
            LAB[1]=((Math.round(((LAB[1])) * 100) / 100f));
            LAB[2]=((Math.round(((LAB[2])) * 100) / 100f));

            final String lab =String.valueOf(LAB[0])+","+String.valueOf(LAB[1])+","+String.valueOf(LAB[2]);

            CMYK.setText("CMYK: "+cmyk);
            LAB_Value.setText("Lab: "+lab);


            baseColor = getColor.new ColorComponet(RGB[0],RGB[1],RGB[2]);
            int color = Color.rgb(baseColor.getRed(),baseColor.getGreen(),baseColor.getBlue());
            layout.setBackgroundColor(color);

            List <GetColor.ColorComponet> ColorShadesList = getColor.calculateShades(baseColor, 5);


            TextView Compliment = (TextView)layout.findViewById(R.id.Complimentary_View);
            int inverted = getColor.getComplementaryColor(Color.rgb(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue()));
            Compliment.setBackgroundColor(inverted);
            colors = ((ColorDrawable)Compliment.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Compliment.setText(hexColor);
            Compliment.setTextColor(inverted);


            TextView Shade_0 = (TextView)layout.findViewById(R.id.Shade_0);
            Shade_0.setBackgroundColor(Color.rgb(ColorShadesList.get(0).getRed(),ColorShadesList.get(0).getGreen(),ColorShadesList.get(0).getBlue()));
            colors = ((ColorDrawable)Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_0.setText(hexColor);
            Shade_0.setTextColor(inverted);

            TextView Shade_1 = (TextView)layout.findViewById(R.id.Shade_1);
            Shade_1.setBackgroundColor(Color.rgb(ColorShadesList.get(1).getRed(),ColorShadesList.get(1).getGreen(),ColorShadesList.get(1).getBlue()));
            colors = ((ColorDrawable)Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_1.setText(hexColor);
            Shade_1.setTextColor(inverted);

            TextView Shade_2 = (TextView)layout.findViewById(R.id.Shade_2);
            Shade_2.setBackgroundColor(Color.rgb(ColorShadesList.get(2).getRed(),ColorShadesList.get(2).getGreen(),ColorShadesList.get(2).getBlue()));
            colors = ((ColorDrawable)Shade_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_2.setText(hexColor);
            Shade_2.setTextColor(inverted);


            TextView Shade_3 = (TextView)layout.findViewById(R.id.Shade_3);
            Shade_3.setBackgroundColor(Color.rgb(ColorShadesList.get(3).getRed(),ColorShadesList.get(3).getGreen(),ColorShadesList.get(3).getBlue()));
            colors = ((ColorDrawable)Shade_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_3.setText(hexColor);
            Shade_3.setTextColor(inverted);

            TextView Shade_4 = (TextView)layout.findViewById(R.id.Shade_4);
            Shade_4.setBackgroundColor(Color.rgb(ColorShadesList.get(4).getRed(), ColorShadesList.get(4).getGreen(), ColorShadesList.get(4).getBlue()));
            colors = ((ColorDrawable)Shade_4.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Shade_4.setText(hexColor);
            Shade_4.setTextColor(inverted);

            List<Integer>tints = new ArrayList<>(getColor.calculateTints(baseColor,5));

            TextView Tint_0 = (TextView)layout.findViewById(R.id.Tint_0);
            Tint_0.setBackgroundColor((tints.get(0)));
            color = ((ColorDrawable)Shade_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_0.setText(hexColor);
            Tint_0.setTextColor(inverted);

            TextView Tint_1 = (TextView)layout.findViewById(R.id.Tint_1);
            Tint_1.setBackgroundColor(tints.get(1));
            color = ((ColorDrawable)Shade_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_1.setText(hexColor);
            Tint_1.setTextColor(inverted);

            TextView Tint_2 = (TextView)layout.findViewById(R.id.Tint_2);
            Tint_2.setBackgroundColor(tints.get(2));
            color = ((ColorDrawable)Tint_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_2.setText(hexColor);
            Tint_2.setTextColor(inverted);

            TextView Tint_3 = (TextView)layout.findViewById(R.id.Tint_3);
            Tint_3.setBackgroundColor(tints.get(3));
            color = ((ColorDrawable)Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_3.setText(hexColor);
            Tint_3.setTextColor(inverted);

            TextView Tint_4 = (TextView)layout.findViewById(R.id.Tint_4);
            Tint_4.setBackgroundColor(tints.get(4));
            color = ((ColorDrawable)Tint_3.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            Tint_4.setText(hexColor);
            Tint_4.setTextColor(inverted);



            TextView Analog_0 = (TextView)layout.findViewById(R.id.Analog_0);
            Analog_0.setBackgroundColor(getColor.getAnalogousColors(baseColor).get(0));
            colors = ((ColorDrawable)Analog_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_0.setText(hexColor);
            Analog_0.setTextColor(inverted);



            TextView Analog_1 = (TextView)layout.findViewById(R.id.Analog_1);
            Analog_1.setBackgroundColor(getColor.getAnalogousColors(baseColor).get(1));
            colors = ((ColorDrawable)Analog_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_1.setText(hexColor);
            Analog_1.setTextColor(inverted);

            TextView Analog_2 = (TextView)layout.findViewById(R.id.Analog_2);
            Analog_2.setBackgroundColor(getColor.getAnalogousColors(baseColor).get(2));
            colors = ((ColorDrawable)Analog_2.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Analog_2.setText(hexColor);
            Analog_2.setTextColor(inverted);

            TextView Tri_0 = (TextView)layout.findViewById(R.id.Tri_0);
            Tri_0.setBackgroundColor(getColor.getTriadicColors(baseColor).get(0));
            colors = ((ColorDrawable)Tri_0.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_0.setText(hexColor);
            Tri_0.setTextColor(inverted);

            TextView Tri_1 = (TextView)layout.findViewById(R.id.Tri_1);
            Tri_1.setBackgroundColor(getColor.getTriadicColors(baseColor).get(1));
            colors = ((ColorDrawable)Tri_1.getBackground()).getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colors));
            Tri_1.setText(hexColor);
            Tri_1.setTextColor(inverted);

            TextView Tri_2 = (TextView)layout.findViewById(R.id.Tri_2);
            Tri_2.setBackgroundColor(getColor.getTriadicColors(baseColor).get(2));
            colors = ((ColorDrawable)Tri_2.getBackground()).getColor();
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

                    if(checkBox.isChecked()){

                        Ok.setText("Add");
                    }
                    else{
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

                            string();
                            pw.dismiss();

                        } else {

                            if (Ok.getText().toString().equals("Add") && getColor.CheckColor(color) == false) {

                                Context context = getApplicationContext();
                                CharSequence text = "Invalid entry: The name must have at least 4 character";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }  else {

                            pw.dismiss();
                        }
                    }
                }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//Only to be used to create database...actually a redundant method
    public void string() {

        String string="";
        string = dbHandler.dataBaseToString();
        getColor.setColor(string);
        System.out.print(string);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }



    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        return mRgba;
    }

    //When user touches the screen
    @Override
    public boolean onTouch(View v, MotionEvent event) throws IndexOutOfBoundsException {
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        double yLow = (double) mOpenCvCameraView.getHeight() * 0.2401961;
        double yHigh = (double) mOpenCvCameraView.getHeight() * 0.7696078;

        double xScale = (double) cols / (double) mOpenCvCameraView.getWidth();
        double yScale = (double) rows / (yHigh - yLow);
      /* colorArray is from getColor.  getColor should be set first.... */
        String[] colorArray = getColor.getColor();
        String colorHolder = "";
        String color;

        x = event.getX();
        y = event.getY();
        y = y - yLow;
        x = x * xScale;
        y = y * yScale;

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

      //  touch_coordinates.setText("X: " + Double.valueOf(x) + ", Y: " + Double.valueOf(y));

        Rect touchedRect = new Rect();

        touchedRect.x = (int) x;
        touchedRect.y = (int) y;

        touchedRect.width = 8;
        touchedRect.height = 8;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width * touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv);

        touch_color.setText("Hex: #" + String.format("%02X", (int) mBlobColorRgba.val[0]) + String.format("%02X", (int) mBlobColorRgba.val[1])
                + String.format("%02X", (int) mBlobColorRgba.val[2]));

        color = "#" + String.format("%02X", (int) mBlobColorRgba.val[0]) + String.format("%02X", (int) mBlobColorRgba.val[1])
                + String.format("%02X", (int) mBlobColorRgba.val[2]);

        for (int i = 0; i < colorArray.length; i++) {

            if (colorArray[i].contains(color.toLowerCase())) {
                colorHolder = colorArray[i];

                for (int j = 0; j < colorHolder.length(); j++) {

                    if (colorHolder.charAt(j) == ' ') {

                        colorHolder = colorHolder.substring(0, colorHolder.length());

                    }

                }
            }
        }
                touch_TrueColor.setText(" Color: " + colorHolder);
                rgb.setText(" RGB: " + getColor.HexToRBG(color));
        touch_TrueColor.setTextColor((Color.rgb((int) mBlobColorRgba.val[0], (int) mBlobColorRgba.val[1], (int) mBlobColorRgba.val[2])));

        rgb.setTextColor((Color.rgb((int) mBlobColorRgba.val[0], (int) mBlobColorRgba.val[1], (int) mBlobColorRgba.val[2])));
        touch_color.setTextColor(Color.rgb((int) mBlobColorRgba.val[0], (int) mBlobColorRgba.val[1], (int) mBlobColorRgba.val[2]));
        ColorPreview.setBackgroundColor(Color.rgb((int) mBlobColorRgba.val[0], (int) mBlobColorRgba.val[1], (int) mBlobColorRgba.val[2]));

        stringRGB = rgb.getText().toString().substring(6,rgb.getText().toString().length()).replace(" ","").split(",");
        //stringRGB = rgb.getText().toString().substring(6,rgb.getText().toString().length()).split(",");
        RGB[0]=Integer.valueOf(stringRGB[0]);
        RGB[1]=Integer.valueOf(stringRGB[1]);
        RGB[2]=Integer.valueOf(stringRGB[2]);

        ColorUtil.RGBToLAB(RGB[0],RGB[1],RGB[2],LAB);
        LAB[0]=((Math.round(((LAB[0])) * 100) / 100f));
        LAB[1]=((Math.round(((LAB[1])) * 100) / 100f));
        LAB[2]=((Math.round(((LAB[2])) * 100) / 100f));

        //  Similar_Color.setText("Similar to: " + getColor.nearestColor(rgb.getText().toString()));
        touch_TrueColor.setText(getColor.mostSimilar(LAB[0], LAB[1], LAB[2], colorArray, RGB));

        int x = ColorUtil.LABToColor(LAB[0],LAB[1],LAB[2]);
        float[]hsvArry =  new float[3];
       Color.colorToHSV(x,hsvArry);

     //   touch_TrueColor.setText(String.valueOf(hsvArry[0]+" , "+String.valueOf(hsvArry[1])+" , "+String.valueOf(hsvArry[2])));
      int inverted  = getColor.getComplementaryColor(x);
      //  ColorPreview.setText("Preview");
      //  ColorPreview.setTextColor(inverted);


        //If color is not found in the database.
      /*  if(touch_TrueColor.getText().toString()!=(" Color: ")){
            Add.setEnabled(true);
        }
        else{
            Add.setEnabled(false);
        }*/


                return false;
            }



    private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }


    class MyCameraDeviceStateCallback extends CameraDevice.StateCallback
    {

        @Override
        public void onOpened(CameraDevice camera)
        {
            mCameraDevice = camera;
            // get builder
            try
            {
                mBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                List<Surface> list = new ArrayList<Surface>();
                SurfaceTexture mSurfaceTexture = new SurfaceTexture(1);
                Size size = getSmallestSize(mCameraDevice.getId());
                mSurfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
                Surface mSurface = new Surface(mSurfaceTexture);
                list.add(mSurface);
                mBuilder.addTarget(mSurface);
                camera.createCaptureSession(list, new MyCameraCaptureSessionStateCallback(), null);
            }
            catch (CameraAccessException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera)
        {

        }

        @Override
        public void onError(CameraDevice camera, int error)
        {

        }
    }

    private Size getSmallestSize(String cameraId) throws CameraAccessException
    {
        Size[] outputSizes = cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(SurfaceTexture.class);
        if (outputSizes == null || outputSizes.length == 0)
        {
            throw new IllegalStateException("Camera " + cameraId + "doesn't support any outputSize.");
        }
        Size chosen = outputSizes[0];
        for (Size s : outputSizes)
        {
            if (chosen.getWidth() >= s.getWidth() && chosen.getHeight() >= s.getHeight())
            {
                chosen = s;
            }
        }
        return chosen;
    }

    class MyCameraCaptureSessionStateCallback extends CameraCaptureSession.StateCallback
    {
        @Override
        public void onConfigured(CameraCaptureSession session)
        {
            mSession = session;
            try
            {
                mSession.setRepeatingRequest(mBuilder.build(), null, null);
            }
            catch (CameraAccessException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session)
        {

        }
    }

    public void turnOnFlashLight() {
        try
        {
            mBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            mSession.setRepeatingRequest(mBuilder.build(), null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight()
    {
        try
        {
            mBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
            mSession.setRepeatingRequest(mBuilder.build(), null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void close()
    {
        if (mCameraDevice == null || mSession == null)
        {
            return;
        }
        mSession.close();
        mCameraDevice.close();
        mCameraDevice = null;
        mSession = null;
    }
}



