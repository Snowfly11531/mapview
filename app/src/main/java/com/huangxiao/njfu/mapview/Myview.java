package com.huangxiao.njfu.mapview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by hasee on 2018/1/15.
 */

public class Myview extends View implements GestureDetector.OnGestureListener{
    private String TAG = "Paint";
    private Context context;
    private GestureDetector detector;
    public Myview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        this.detector=new GestureDetector(context,this);
        // TODO Auto-generated constructor stub
    }

    public Myview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.detector=new GestureDetector(context,this);
    }

    public Myview(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.detector=new GestureDetector(context,this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        Toast.makeText(context,"haha",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(context,"thids",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
}
