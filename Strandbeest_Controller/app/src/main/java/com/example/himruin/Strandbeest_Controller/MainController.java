package com.example.himruin.Strandbeest_Controller;

import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainController extends Activity {
    RelativeLayout layout_joystick;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    JoystickClass js;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);

        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoystickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(200, 200);
        js.setLayoutSize(1000, 1000);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOFFSET(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));
                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));

//                    int direction = js.get8Direction();
//                    if(direction == JoystickClass.stick_up) {
//                        textView5.setText("Direction : Up");
//                    } else if(direction == JoystickClass.stick_upright) {
//                        textView5.setText("Direction : Up Right");
//                    } else if(direction == JoystickClass.stick_right) {
//                        textView5.setText("Direction : Right");
//                    } else if(direction == JoystickClass.stick_downright) {
//                        textView5.setText("Direction : Down Right");
//                    } else if(direction == JoystickClass.stick_down) {
//                        textView5.setText("Direction : Down");
//                    } else if(direction == JoystickClass.stick_downleft) {
//                        textView5.setText("Direction : Down Left");
//                    } else if(direction == JoystickClass.stick_left) {
//                        textView5.setText("Direction : Left");
//                    } else if(direction == JoystickClass.stick_upleft) {
//                        textView5.setText("Direction : Up Left");
//                    } else if(direction == JoystickClass.stick_none) {
//                        textView5.setText("Direction : -");
//                    }

                    int direction = js.get4Direction();
                    if(direction == JoystickClass.stick_up) {
                        textView5.setText("Direction : Up");
                    } else if(direction == JoystickClass.stick_right) {
                        textView5.setText("Direction : Right");
                    } else if(direction == JoystickClass.stick_down) {
                        textView5.setText("Direction : Down");
                    } else if(direction == JoystickClass.stick_left) {
                        textView5.setText("Direction : Left");
                    } else if(direction == JoystickClass.stick_none) {
                        textView5.setText("Direction : -");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");
                    textView5.setText("Direction :");
                }
                return true;
            }
        });
    }
}