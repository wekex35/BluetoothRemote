package com.wekex.apps.bluetoothremote.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wekex.apps.bluetoothremote.R;
import com.wekex.apps.bluetoothremote.constants.utils;

import static com.wekex.apps.bluetoothremote.constants.Constants.TAG;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.CURRENT_LAYOUT_NAME;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.ID;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.getScreenHeight;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.getScreenWidth;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.valueTOpercentage;
import static com.wekex.apps.bluetoothremote.constants.utils.pxtoDp;
import static com.wekex.apps.bluetoothremote.helper.dbhelper.getDB;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.view_json_info;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.view_json_row_info;

public class move_zoom_rotate implements View.OnTouchListener {
    final Handler longPress = new Handler();
    final Handler singleClick = new Handler();
    Boolean isSingleTap = true;
    Boolean islongTap = true;
    int clickCount = 0;
    //variable for storing the time of first click
    long startTime;
    //variable for calculating the total time
    long duration;
    //constant for defining the time duration between the click that can be considered as double-tap
    static final int MAX_DURATION = 150;


    float scalediff;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    Activity context;
    int i;

    //For move
    private int lastAction;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    RelativeLayout.LayoutParams  params;
    public move_zoom_rotate(Activity activity) {
        context = activity;
    }

  @Override
    public boolean onTouch(View view, MotionEvent event) {

        //View width and height
        int width= getScreenWidth();
        int height= getScreenHeight();

        params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        float angle = 0;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_DOWN:
                longPress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            Log.d(TAG, "onTouch: click3");
                            isSingleTap = false;
                        }
                    }
                }, 500);




                startTime = System.currentTimeMillis();
                Log.d(TAG, "onTouch: start "+startTime);
                clickCount++;

                //remember the initial position.
                initialX = params.leftMargin;
                initialY = params.topMargin;

                //get the touch location
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();

                //Initial mode
                mode = DRAG;

                lastAction = event.getAction();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    mode = ZOOM;
                }
               // Log.d(TAG, "onTouch: "+mode);
                d = rotation(event);
                break;

            case MotionEvent.ACTION_UP:


                //myLayout2.setVisibility(View.GONE);
                if (lastAction == MotionEvent.ACTION_MOVE) {
                    String jstring = sharedprefs.read(context, CURRENT_LAYOUT_NAME);
                    String ForSaving = String.valueOf(view_json_row_info(
                            view_json_info(
                                    pxtoDp(params.leftMargin, context),
                                    pxtoDp(params.topMargin, context),//120,120,
                                    pxtoDp(view.getHeight(), context),
                                    pxtoDp(view.getWidth(), context),
                                    '0'),//Creating JSon Object
                            jstring, Integer.parseInt(view.getTag().toString())));
                    Log.d(TAG, ForSaving);
                   // ForSaving = ForSaving.replaceAll("null,", "");
                    getDB(context).updateLayout(ID, ForSaving);
                    sharedprefs.save(context, CURRENT_LAYOUT_NAME, ForSaving);
                }

                if (lastAction == MotionEvent.ACTION_DOWN) {
            ////////////////////##TOUCH DETECT##/////////////////////

                    //Log.d(TAG, "onTouch: click");



                       // ShowDailog();
                   /* long time = System.currentTimeMillis() - startTime;
                    duration=  duration + time;
                    Log.d(TAG, "onTouch: "+duration);
                    if(clickCount == 2)
                    {
                        if(duration<= MAX_DURATION)
                        {
                            Log.d(TAG, "onTouch: click2");
                            longPress.removeCallbacksAndMessages(null);
                            isSingleTap = false;
                        }
                        clickCount = 0;
                        duration = 0;
                        break;
                    }*/
                    singleClick.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isSingleTap) {
                                longPress.removeCallbacksAndMessages(null);
                                Log.d(TAG, "onTouch: click1");

                            }else {
                                //  isSingleTap = true;
                            }

                        }
                    }, 150);
                    lastAction = event.getAction();

                }



                    //Open the chat conversation click.
                    //close the service and remove the chat heads
                    //stopSelf();


                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;

                break;
            case MotionEvent.ACTION_MOVE:
               // Log.d(TAG, "onTouch: "+mode);
                if (mode == DRAG) {

                    params.leftMargin = initialX + (int) (event.getRawX() - initialTouchX);
                    params.topMargin = initialY + (int) (event.getRawY() - initialTouchY);

                    //Update the layout with new X & Y coordinate

                    view.setLayoutParams(params);
                    lastAction = event.getAction();

                } else if (mode == ZOOM) {

                    if (event.getPointerCount() == 2) {
                    newRot = rotation(event);
                    float r = newRot - d;
                    angle = r;


                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        float scale = newDist / oldDist * view.getScaleX();
                        if (scale > 0.6) {
                            scalediff = scale;
                            view.setScaleX(scale);
                            view.setScaleY(scale);

                        }
                    }
                    view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();
                    params.leftMargin = (int) (initialX + (int) (event.getRawX() - initialTouchX) + scalediff);
                    params.topMargin = (int) (initialY + (int) (event.getRawY() - initialTouchY) + scalediff);

                    //Update the layout with new X & Y coordinate

                    view.setLayoutParams(params);
                    lastAction = event.getAction();

                }
        }
                //Calculate the X and Y coordinates of the view.
              break;

        }
        return true;
    }

/*


   @Override
   public boolean onTouch(View view, MotionEvent event) {
          //final ImageView view = (ImageView) v;
          int width= getScreenWidth();
          int height= getScreenHeight();
          Log.d(TAG, "onTouch: "+view.getTag());
          if (view.getTag() == null) {

          }
          RelativeLayout.LayoutParams  parms = (RelativeLayout.LayoutParams) view.getLayoutParams();

          int startwidth;
          int startheight;
          float dx = 0, dy = 0, x = 0, y = 0;
          float angle = 0;


          //  ((BitmapDrawable) view.getDrawable()).setAntiAlias(true);
          switch (event.getAction() & MotionEvent.ACTION_MASK) {
              case MotionEvent.ACTION_DOWN:

                  parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                  startwidth = parms.width;
                  startheight = parms.height;
                  dx = event.getRawX();
                  dy = event.getRawY();
                  mode = DRAG;
                  break;

              case MotionEvent.ACTION_POINTER_DOWN:
                  oldDist = spacing(event);
                  if (oldDist > 10f) {
                      mode = ZOOM;
                  }
                  d = rotation(event);
                  break;
              case MotionEvent.ACTION_UP:


                  Log.d("params", parms.leftMargin+" onTouch: "+parms.topMargin);
                  //String params = "{\"rightMargin\":"+parms.leftMargin+",\"bottomMargin\":"+parms.topMargin+"}";
                  Log.d(TAG, "onTouch: "+view.getTag());

                   String jstring = sharedprefs.read(context,CURRENT_LAYOUT_NAME);
                  Log.d("params5", String.valueOf(view_json_row_info(view_json_info(parms.leftMargin,parms.topMargin, view.getHeight(), view.getWidth(), '0'),jstring,Integer.parseInt(view.getTag().toString()))));
                  sharedprefs.save(context,CURRENT_LAYOUT_NAME, String.valueOf(view_json_row_info(view_json_info(
                          valueTOpercentage(parms.leftMargin,width),
                          valueTOpercentage(parms.topMargin,height),
                          view.getHeight(),
                          view.getWidth(), '0'),//Creating JSon Object
                          jstring,Integer.parseInt(view.getTag().toString()))));
                  break;

              case MotionEvent.ACTION_POINTER_UP:
                  mode = NONE;


                  break;
              case MotionEvent.ACTION_MOVE:
                  if (mode == DRAG) {

                      x = event.getRawX();
                      y = event.getRawY();
                      if (width-view.getWidth() > x && height-view.getHeight() > y){
                          Log.d(TAG, y +" "+height+ " onTouch: " + x+" "+width);
                      parms.leftMargin = (int) (x - dx);
                      parms.topMargin = (int) (y - dy);
                      parms.rightMargin = 0;
                      parms.bottomMargin = 0;


                    //  parms.rightMargin = parms.leftMargin + (5 * parms.width);
                   //   parms.bottomMargin = parms.topMargin + (10 * parms.height);

                      view.setLayoutParams(parms);
                  }

                  } else if (mode == ZOOM) {

                      if (event.getPointerCount() == 2) {

                          newRot = rotation(event);
                          float r = newRot - d;
                          angle = r;

                          x = event.getRawX();
                          y = event.getRawY();

                          float newDist = spacing(event);
                          if (newDist > 10f) {
                              float scale = newDist / oldDist * view.getScaleX();
                              if (scale > 0.6) {
                                  scalediff = scale;
                                  view.setScaleX(scale);
                                  view.setScaleY(scale);

                              }
                          }

                          view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                          x = event.getRawX();
                          y = event.getRawY();

                          parms.leftMargin = (int) ((x - dx) + scalediff);
                          parms.topMargin = (int) ((y - dy) + scalediff);

                          parms.rightMargin = 0;
                          parms.bottomMargin = 0;
                          parms.rightMargin = parms.leftMargin + (5 * parms.width);
                          parms.bottomMargin = parms.topMargin + (10 * parms.height);

                          view.setLayoutParams(parms);


                      }
                  }
                  break;
          }

          return true;

      }
      */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void buttonInfo(View view){
        TextView buttonId = view.findViewById(R.id.button_id);

        EditText heightED = view.findViewById(R.id.heightED);
        SeekBar heightSB = view.findViewById(R.id.heightSB);
        heightSB.setOnSeekBarChangeListener(new myseekbar(heightED));
        heightED.addTextChangedListener(new myTextChangelistener(heightSB));

        EditText WidthtED = view.findViewById(R.id.WidthtED);
        SeekBar WidthSB = view.findViewById(R.id.WidthSB);
        WidthSB.setOnSeekBarChangeListener(new myseekbar(WidthtED));
        WidthtED.addTextChangedListener(new myTextChangelistener(WidthSB));

        EditText RotationED = view.findViewById(R.id.RotationED);
        SeekBar RotationSB = view.findViewById(R.id.RotationSB);
        RotationSB.setOnSeekBarChangeListener(new myseekbar(RotationED));
        RotationED.addTextChangedListener(new myTextChangelistener(RotationSB));

        TextView selecBackground = view.findViewById(R.id.selecBackground);

        RadioGroup ButtonType = view.findViewById(R.id.ButtonType);
        ButtonType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String type = "press";
                switch (checkedId){
                    case R.id.press:type = "press";break;
                    case R.id.longpress:type = "longpress";break;
                }
            }
        });
    }

    private class myseekbar implements SeekBar.OnSeekBarChangeListener {
        EditText rotationED;
        public myseekbar(EditText rotationED) {
            this.rotationED = rotationED;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            rotationED.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class myTextChangelistener implements TextWatcher {
        SeekBar seekBar;
        public myTextChangelistener(SeekBar seekBar) {
            this.seekBar = seekBar;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    }
}
