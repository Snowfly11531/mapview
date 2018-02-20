package com.huangxiao.njfu.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangxiao.njfu.mapview.shapefile.Mapview;
import com.huangxiao.njfu.mapview.shapefile.Polygone;
import com.huangxiao.njfu.mapview.shapefile.ShapefileReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            Polygone polygone=new Polygone(Environment.getExternalStorageDirectory()+"/datas/bou2_4p.shp");
            int parts=polygone.polygoneParts[0].getNumParts();
            int points=polygone.polygoneParts[0].getNumPoints();
            Toast.makeText(this,Double.toString(polygone.polygoneParts[600].getPolygonePartsBox().getxMax()),Toast.LENGTH_LONG).show();
            Mapview mapview=(Mapview)findViewById(R.id.mapview);
            mapview.addPolygone(polygone);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                }
        }
    }
}
