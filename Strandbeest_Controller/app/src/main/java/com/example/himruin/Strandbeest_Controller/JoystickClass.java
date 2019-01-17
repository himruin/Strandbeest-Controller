package com.example.himruin.Strandbeest_Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.lang.Math;
import java.io.IOException;
import static java.lang.Math.round;
import static java.lang.Thread.sleep;


public class JoystickClass {
    public static final int stick_none = 0;
    public static final int stick_up = 1;
    public static final int stick_upright = 2;
    public static final int stick_right = 3;
    public static final int stick_downright = 4;
    public static final int stick_down = 5;
    public static final int stick_downleft = 6;
    public static final int stick_left = 7;
    public static final int stick_upleft = 8;


    private int stick_alpha = 200;
    private int layout_alpha = 200;
    private int OFFSET = 0;

    private Context mContext;
    private ViewGroup mLayout;
    private ViewGroup.LayoutParams params;
    private int stick_width, stick_height;

    private int position_x = 0;
    private int position_y = 0;
    private static int min_distance = 0;
    private static int max_distance = 420;

    public static float distance = 0;
    public static float angle = 0;

    private DrawCanvas draw;
    private Paint paint;
    private Bitmap stick;

    private static boolean touch_state = false;

    public JoystickClass (Context context, ViewGroup layout, int stick_res_id) {
        mContext = context;

        stick = BitmapFactory.decodeResource(mContext.getResources(), stick_res_id);

        stick_width = stick.getWidth();
        stick_height = stick.getHeight();

        draw = new DrawCanvas(mContext);
        paint = new Paint();
        mLayout = layout;
        params = mLayout.getLayoutParams();

    }


    public void drawStick(MotionEvent arg1) {
        position_x = (int) (arg1.getX() - (params.width / 2));
        position_y = (int) (arg1.getY() - (params.height / 2));
        distance = (float) Math.sqrt(Math.pow(position_x, 2) + Math.pow(position_y, 2));
        angle = (float) cal_angle(position_x, position_y);

        if(arg1.getAction() == MotionEvent.ACTION_DOWN) {
            if(distance <= (params.width / 2) - OFFSET) {
                draw.position(arg1.getX(), arg1.getY());
                draw();
                touch_state = true;
            }
        } else if (arg1.getAction() == MotionEvent.ACTION_MOVE && touch_state) {
            if(distance <= (params.width / 2) - OFFSET) {
                draw.position(arg1.getX(), arg1.getY());
                draw();
            } else if(distance > (params.width / 2) - OFFSET) {
                float x = (float) (Math.cos(Math.toRadians(cal_angle(position_x, position_y))) * ((params.width / 2) - OFFSET));
                float y = (float) (Math.sin(Math.toRadians(cal_angle(position_x, position_y))) * ((params.height / 2) - OFFSET));
                x += (params.width / 2);
                y += (params.height / 2);
                draw.position(x, y);
                draw();
            } else {
                mLayout.removeView(draw);
            }
        } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
            mLayout.removeView(draw);
            touch_state = false;
        }
    }

    public int[] getPosition() {
        if(distance > min_distance && touch_state) {
            return new int[] {position_x, position_y};
        }
        return new int[]{0, 0};
    }

    public int getX() {
        if(distance > min_distance && distance < max_distance && touch_state) {
            return position_x;
        }
        return 0;
    }

    public int getY() {
        if(distance > min_distance && distance < max_distance && touch_state) {
            return position_y;
        }
        return 0;
    }

    public static float getAngle() {
        if(distance > min_distance && distance < max_distance && touch_state) {
            return angle;
        }
        return 0;
    }

    public static float getDistance() {
        if(distance > min_distance && distance < max_distance && touch_state) {
            return distance;
        }
        return 0;
    }

    public void setMinimumDistance(int minDistance) {
        min_distance = minDistance;
    }


    public String data2Send(float distance, float angle){
        distance = round(getDistance());
        angle = round(getAngle());
        String data = String.valueOf(distance) + "," + String.valueOf(angle) + "#";
        return data;
    }

    public void dataTransfer(){
        try {
            MainConnection.btSocket.getOutputStream().write(data2Send(distance,angle).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void noData(){
        try {
            MainConnection.btSocket.getOutputStream().write(data2Send(0,0).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public int get8Direction() {
//        if(distance > min_distance && distance < max_distance && touch_state) {
//            dataTransfer();
//            try {
//                sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(angle >= 247.5 && angle < 292.5 ) {
//                return stick_up;
//            } else if(angle >= 292.5 && angle < 337.5 ) {
//                return stick_upright;
//            } else if(angle >= 337.5 || angle < 22.5 ) {
//                return stick_right;
//            } else if(angle >= 22.5 && angle < 67.5 ) {
//                return stick_downright;
//            } else if(angle >= 67.5 && angle < 112.5 ) {
//                return stick_down;
//            } else if(angle >= 112.5 && angle < 157.5 ) {
//                return stick_downleft;
//            } else if(angle >= 157.5 && angle < 202.5 ) {
//                return stick_left;
//            } else if(angle >= 202.5 && angle < 247.5 ) {
//                return stick_upleft;
//            }
//        } else if(distance <= min_distance && touch_state) {
//            //noData();
//            return stick_none;
//        }
//        return 0;
//    }

    public int get4Direction() {
        if(distance > min_distance && distance < max_distance && touch_state) {
            dataTransfer();
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(angle >= 225 && angle < 315 ) {
                return stick_up;
            } else if(angle >= 315 || angle < 45 ) {
                return stick_right;
            } else if(angle >= 45 && angle < 135 ) {
                return stick_down;
            } else if(angle >= 135 && angle < 225 ) {
                return stick_left;
            }
        } else if(distance <= min_distance && touch_state) {
//            noData();
//            try {
//                sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return stick_none;
        }
        return 0;
    }


    public void setOFFSET(int offset) {
        OFFSET = offset;
    }

    public void setStickAlpha(int alpha) {
        stick_alpha = alpha;
        paint.setAlpha(alpha);
    }

    public void setLayoutAlpha(int alpha) {
        layout_alpha = alpha;
        mLayout.getBackground().setAlpha(alpha);
    }

    //stick
    public void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stick_width = stick.getWidth();
        stick_height = stick.getHeight();
    }


    public void setLayoutSize(int width, int height) {
        params.width = width;
        params.height = height;
    }


    private double cal_angle(float x, float y) {
        if(x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }

    private void draw() {
        try {
            mLayout.removeView(draw);
        } catch (Exception e) { }
        mLayout.addView(draw);
    }

    private class DrawCanvas extends View {
        float x, y;

        private DrawCanvas(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(stick, x, y, paint);
        }

        private void position(float pos_x, float pos_y) {
            x = pos_x - (stick_width / 2);
            y = pos_y - (stick_height / 2);
        }
    }
}

