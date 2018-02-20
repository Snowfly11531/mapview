package com.huangxiao.njfu.mapview.shapefile;

import java.io.FileInputStream;

/**
 * Created by hasee on 2018/1/19.
 */

public class Point extends ShapefileReader implements shapefile {
    private FileInputStream pointStream;
    private int index=0;
    private Box integarBox;
    private Spot[] spots;
    public Point(String url){
        super(url);
        this.pointStream=super.shpStream;
        this.integarBox=super.integarBox;

    }

    public void draw(){

    }
}
