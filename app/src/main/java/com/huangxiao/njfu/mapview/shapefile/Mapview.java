package com.huangxiao.njfu.mapview.shapefile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Toast;

import com.huangxiao.njfu.mapview.R;

/**
 * Created by hasee on 2018/1/21.
 */

public class Mapview extends View {
    private boolean PointFlag=true;//单指为true，双指为false
    private Polygone polygone=null;
    private Spot sp=new Spot(0,0);
    private float disX;
    private float midX;
    private float midY;
    private Context context;
    private Bitmap viewBitmap;
    private Box externalBox;
    private Boolean reDrawMap=true;
    private Matrix matrix=new Matrix();
    private float porprotionX=1;
    public Mapview(Context context){
        super(context);
        this.context=context;
    }
    public Mapview(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context=context;
    }
    public void addPolygone(Polygone polygone){
        this.polygone=polygone;
        externalBox=new Box(this.polygone.integarBox.getxMin(),this.polygone.integarBox.getyMin(),
                this.polygone.integarBox.getxMax(),this.polygone.integarBox.getyMax());
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    private void drawMap(){
        viewBitmap=Bitmap.createBitmap(this.getMeasuredWidth(),this.getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas1=new Canvas(viewBitmap);
        matrix=new Matrix();//重置矩阵
        if(polygone!=null){
            Paint paint = new Paint();
            Path path = new Path();
            paint.setAntiAlias(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            for(int id=0;id<polygone.num;id++) {
                Spot[] spots = new Spot[polygone.polygoneParts[id].getNumPoints()];
                float width = (float) getMeasuredWidth() / (externalBox.getxMax() - externalBox.getxMin());
                float height = (float) getMeasuredHeight() / (externalBox.getyMax() - externalBox.getyMin());
                for (int i = 0; i < spots.length; i = i + 1) {
                    float x = (polygone.polygoneParts[id].getSpots()[i].x - externalBox.getxMin()) * width;
                    float y = (float) getMeasuredHeight() - (polygone.polygoneParts[id].getSpots()[i].y - externalBox.getyMin()) * height;
                    spots[i] = new Spot(x, y);
                }
                path.moveTo(spots[0].x, spots[0].y);
                float forX = spots[0].x;
                float forY = spots[0].y;
                for (int i = 1; i < spots.length; i = i + 1) {
                    if (forX == spots[i].x && forY == spots[i].y) {
                    } else {
                        path.lineTo(spots[i].x, spots[i].y);
                    }
                    forX = spots[i].x;
                    forY = spots[i].y;
                }
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(255, 102, 204, 255));
            canvas1.drawPath(path, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(2);
            canvas1.drawPath(path, paint);
            reDrawMap=false;
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(reDrawMap){ //判断是否需要刷新位图
            drawMap();
        }
        canvas.drawBitmap(viewBitmap,matrix,null);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            PointFlag=true;
            sp.x=event.getX();
            sp.y=event.getY();
        }else if(event.getAction()==MotionEvent.ACTION_POINTER_2_DOWN){
            PointFlag=false;
            disX = (float) Math.sqrt(Math.pow(event.getX(0) - event.getX(1),2)+Math.pow(event.getY(0)-event.getY(1),2));
            midX=(event.getX(0)+event.getX(1))/2;
            midY=(event.getY(0)+event.getY(1))/2;
        }else if(event.getAction()==MotionEvent.ACTION_MOVE) {
            if (PointFlag && event.getPointerCount() == 1) {
                float x=event.getX();
                float y=event.getY();
                float disX=x-sp.x;
                float disY=y-sp.y;
                sp.x=x;
                sp.y=y;
                float trueX=disX*((externalBox.getxMax()-externalBox.getxMin())/(float)getMeasuredWidth());
                float trueY=disY*((externalBox.getyMax()-externalBox.getyMin())/(float)getMeasuredHeight());
                externalBox.xMin-=trueX;externalBox.xMax-=trueX;
                externalBox.yMin+=trueY;externalBox.yMax+=trueY;
                matrix.postTranslate(disX, disY);//将位图通过matrix进行平移
                invalidate();
            } else if (!PointFlag && event.getPointerCount() == 2) {
                float X1 = midX * ((externalBox.getxMax() - externalBox.getxMin()) / (float) getMeasuredWidth());
                float X2 = externalBox.getxMax() - externalBox.getxMin() - X1;
                float Y2 = midY * ((externalBox.getyMax() - externalBox.getyMin()) / (float) getMeasuredHeight());
                float Y1 = externalBox.getyMax() - externalBox.getyMin() - Y2;
                float nodisX = (float) Math.sqrt(Math.pow(event.getX(0) - event.getX(1), 2) + Math.pow(event.getY(0) - event.getY(1), 2));
                porprotionX = nodisX / disX;
                disX = nodisX;
                externalBox.xMin = externalBox.xMin + X1 - X1 / porprotionX;
                externalBox.xMax = externalBox.xMax - X2 + X2 / porprotionX;
                externalBox.yMin = externalBox.yMin + Y1 - Y1 / porprotionX;
                externalBox.yMax = externalBox.yMax - Y2 + Y2 / porprotionX;
                matrix.postScale(porprotionX,porprotionX,midX,midY);//将位图通过matrix进行放大
                invalidate();
            }
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            reDrawMap=true;//抬手时刷新一次位图
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
