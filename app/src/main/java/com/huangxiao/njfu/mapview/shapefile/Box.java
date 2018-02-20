package com.huangxiao.njfu.mapview.shapefile;

/**
 * Created by hasee on 2018/1/19.
 */

public class Box {
    public float xMin;
    public float yMin;
    public float xMax;
    public float yMax;
    public Box(float xMin,float yMin,float xMax,float yMax){
        this.xMax=xMax;
        this.xMin=xMin;
        this.yMax=yMax;
        this.yMin=yMin;
    }
    public float getxMin(){
        return this.xMin;
    }
    public float getxMax(){
        return this.xMax;
    }
    public float getyMin(){
        return this.yMin;
    }
    public float getyMax(){
        return this.yMax;
    }
}
