package com.huangxiao.njfu.mapview.shapefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by hasee on 2018/1/19.
 */

public class ShapefileReader {
    public FileInputStream shpStream;
    public Box integarBox;
    public int shpType;
    public static int bytesToIntLittle(byte[] src) {
        int value;
        value = (int) ((src[0] & 0xFF)
                | ((src[1] & 0xFF)<<8)
                | ((src[2] & 0xFF)<<16)
                | ((src[3] & 0xFF)<<24));
        return value;
    }
    public static int bytesToIntBig(byte[] src) {
        int value;
        value = (int) ( ((src[0] & 0xFF)<<24)
                |((src[1] & 0xFF)<<16)
                |((src[2] & 0xFF)<<8)
                |(src[3] & 0xFF));
        return value;
    }
    public static double bytesToDouble(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }
    public byte[] read4(FileInputStream input){
        byte[] bytes=new byte[4];
        for(int i=0;i<4;i++){
            try {
                bytes[i] = (byte) input.read();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return bytes;
    }
    public int read4big(FileInputStream input){
        return bytesToIntBig(read4(input));
    }
    public int read4little(FileInputStream input){
        return bytesToIntLittle(read4(input));
    }
    public double read8Double(FileInputStream input){
        byte[] bytes=new byte[8];
        for(int i=0;i<8;i++){
            try{
                bytes[i]=(byte)input.read();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return bytesToDouble(bytes);
    }

    public void read() throws IOException{
        int index=0;
        while (this.shpStream.available()>0){
            this.shpStream.read();
            index++;
            if(index==32){
                break;
            }
        }
        shpType=read4little(shpStream);
        integarBox=new Box((float) read8Double(shpStream),(float) read8Double(shpStream),(float) read8Double(shpStream),(float) read8Double(shpStream));
        index+=36;
        while (this.shpStream.available()>0){
            this.shpStream.read();
            index++;
            if(index==100)break;
        }
    }

    public ShapefileReader(String url){
        File file=new File(url);
        FileInputStream shpStream=null;
        try {
            shpStream = new FileInputStream(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        this.shpStream=shpStream;
        try {
            read();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
