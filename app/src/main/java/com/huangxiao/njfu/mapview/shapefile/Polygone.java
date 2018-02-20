package com.huangxiao.njfu.mapview.shapefile;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by hasee on 2018/1/20.
 */

public class Polygone extends ShapefileReader implements shapefile {
    private FileInputStream polygoneStream;
    private int index=0;
    public Box integarBox;
    public double test=0;
    private Spot[] spots;
    public int num=0;
    public PolygonePart[] polygoneParts=new PolygonePart[2000];
    public class PolygonePart{
        private int id;
        private Box PolygonePartsBox;
        private int length;
        private int NumParts;
        private int NumPoints;
        private int shape;
        private int[] Parts;
        private Spot[] spots;
        public int getLength(){
            return length;
        }
        public Box getPolygonePartsBox(){
            return PolygonePartsBox;
        }
        public int getShape(){
            return shape;
        }
        public int getNumParts(){
            return NumParts;
        }
        public int getNumPoints(){
            return NumPoints;
        }
        public int[] getParts(){
            return Parts;
        }
        public Spot[] getSpots(){
            return spots;
        }
        public int getId(){
            return id;
        }
    }
    public Polygone(String url){
        super(url);
        this.polygoneStream=super.shpStream;
        this.integarBox=super.integarBox;
        try {
            while (this.polygoneStream.available() > 0) {
                readPolygone();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void readPolygone(){
        polygoneParts[num]=new PolygonePart();
        polygoneParts[num].id=super.read4big(this.polygoneStream);
        polygoneParts[num].length=super.read4big(this.polygoneStream);
        polygoneParts[num].shape=super.read4little(this.polygoneStream);
        polygoneParts[num].PolygonePartsBox=new Box((float) super.read8Double(this.polygoneStream),(float) super.read8Double(this.polygoneStream),
                (float) super.read8Double(this.polygoneStream),(float) super.read8Double(this.polygoneStream));
        polygoneParts[num].NumParts=super.read4little(this.polygoneStream);
        polygoneParts[num].NumPoints=super.read4little(this.polygoneStream);
        polygoneParts[num].Parts=new int[polygoneParts[num].NumParts];
        polygoneParts[num].spots=new Spot[polygoneParts[num].NumPoints];
        for(int i=0;i<polygoneParts[num].NumParts;i++){
            polygoneParts[num].Parts[i]=super.read4little(this.polygoneStream);
        }
        for(int i=0;i<polygoneParts[num].NumPoints;i++){
            float x=(float) super.read8Double(this.polygoneStream);
            float y=(float) super.read8Double(this.polygoneStream);
            polygoneParts[num].spots[i]=new Spot(x,y);
        }
        num++;
    }
    public void draw(){

    }
}
