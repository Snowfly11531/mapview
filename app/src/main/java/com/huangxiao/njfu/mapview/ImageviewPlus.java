package com.huangxiao.njfu.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by hasee on 2018/1/15.
 */

public class ImageviewPlus extends View {
    private Bitmap bitmap;
    private Matrix matrix=new Matrix();
    private boolean PointFlag=true;//单指为true，双指为false
    private boolean IsFrist=true;//图片第一次设置的标志
    private PointF pf=new PointF();
    private float dis=0f;
    private float midX=0f;
    private float midY=0f;
    private float maxScale=5f;
    private float minScale=1f;
    private float Scale;
    private float[] arr=new float[9];
    private Context context;
    public ImageviewPlus(Context context, AttributeSet attrs){
        super(context,attrs);
        //TODO Auto-generated constructor stub
        this.context=context;
    }
    public void setMaxScale(float maxScale){
        if(maxScale>5||maxScale<0.5){
            return;
        }
        this.maxScale=maxScale;
    }
    public void setMinScale(float minScale){
        if(minScale>5||minScale<0.5){
            return;
        }
        this.minScale=minScale;
    }
    public void setBitmap(Bitmap bitmap){
        IsFrist=true;
        this.bitmap=bitmap;
        //Toast.makeText(context,"haha",Toast.LENGTH_LONG).show();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(IsFrist&bitmap!=null){
            //Toast.makeText(context,"haha1",Toast.LENGTH_LONG).show();
            float xScale=(float) getMeasuredWidth()/(float) bitmap.getWidth();
            float yScale=(float)getMeasuredHeight()/(float)bitmap.getHeight();
            Scale=Math.min(xScale,yScale);
            maxScale*=Scale;
            minScale*=Scale;
            matrix.postScale(Scale,Scale);
            float disX=(getMeasuredWidth()-bitmap.getWidth()*Scale)/2;
            float disY=(getMeasuredHeight()-bitmap.getHeight()*Scale)/2;
            matrix.postTranslate(disX,disY);
            Toast.makeText(context,"haha",Toast.LENGTH_LONG).show();
        }
        IsFrist=false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO Auto-generated method stub
        super.onDraw(canvas);
        if(bitmap!=null){
            //Toast.makeText(context,"haha1",Toast.LENGTH_LONG).show();
            canvas.drawBitmap(bitmap,matrix,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            PointFlag=true;
            pf.x=event.getX();
            pf.y=event.getY();
        }else if(event.getAction()==MotionEvent.ACTION_POINTER_2_DOWN) {
            PointFlag = false;
            dis = (float) Math.sqrt(Math.pow(event.getX(0) - event.getX(1),2)+Math.pow(event.getY(0)-event.getY(1),2));
            midX=(event.getX(0)+event.getX(1))/2;
            midY=(event.getY(0)+event.getY(1))/2;
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            if(PointFlag&event.getPointerCount()==1){
                float x=event.getX();
                float y=event.getY();
                float disX=x-pf.x;
                float disY=y-pf.y;
                pf.x=x;
                pf.y=y;
                matrix.getValues(arr);
                if(!(disY>0&&(arr[5]+disY)>0)&&!(disY<0&&(arr[5]+disY<getMeasuredHeight()-arr[4]*bitmap.getHeight()))&&arr[0]>Scale&&!(disX>0&&(arr[2]+disX)>0)&&!(disX<0&&(arr[2]+disX)<(getMeasuredWidth()-arr[0]*bitmap.getWidth()))){
                    if(arr[0]*bitmap.getWidth()>getMeasuredWidth()&&arr[4]*bitmap.getHeight()>getMeasuredHeight()) {
                        matrix.postTranslate(disX, disY);
                    }else if(arr[0]*bitmap.getWidth()>getMeasuredWidth()){
                        matrix.postTranslate(disX,0);
                    }else if(arr[4]*bitmap.getHeight()>getMeasuredHeight()){
                        matrix.postTranslate(0,disY);
                    }
                }
            }else if(!PointFlag&event.getPointerCount()==2){
                float nowdis=(float) Math.sqrt(Math.pow(event.getX(0) - event.getX(1),2)+Math.pow(event.getY(0)-event.getY(1),2));
                float scale=nowdis/dis;
                matrix.getValues(arr);
                dis=nowdis;
                if(arr[0]>=maxScale&&scale>=1){
                    return true;
                }
                if(arr[0]<=minScale&&scale<=1){
                    return true;
                }
                matrix.postScale(scale,scale,midX,midY);
                matrix.getValues(arr);
                if(arr[0]<=minScale){
                    matrix.setScale(Scale,Scale);
                    float disX=(getMeasuredWidth()-bitmap.getWidth()*Scale)/2;
                    float disY=(getMeasuredHeight()-bitmap.getHeight()*Scale)/2;
                    matrix.postTranslate(disX,disY);
                }
            }
            invalidate();
        }
        return true;
    }
}
