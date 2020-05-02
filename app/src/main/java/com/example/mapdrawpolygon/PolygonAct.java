package com.example.mapdrawpolygon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class PolygonAct extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
    GoogleMap gmap;
    Button btn_draw, btn_clear;
    CheckBox checkBox;
    Polygon polygon = null;
    SeekBar seek_red, seek_green, seek_blue;
    SupportMapFragment supportMapFragment;

    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    int red = 0, green = 0, blue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon);

        btn_draw = findViewById(R.id.btn_draw);
        btn_clear = findViewById(R.id.btn_clear);
        checkBox = findViewById(R.id.checkBox);
        seek_red = findViewById(R.id.seek_red);
        seek_green = findViewById(R.id.seek_green);
        seek_blue = findViewById(R.id.seek_blue);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(polygon == null) return;
                    polygon.setFillColor(Color.rgb(red, green, blue));
                }else{
                    polygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });

        btn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(polygon == null){
                    Toast.makeText(getApplicationContext(), "Gambarkan koordinat di maps", Toast.LENGTH_SHORT).show();
                }else if(polygon != null) polygon.remove();
                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).clickable(true);
                polygon = gmap.addPolygon(polygonOptions);
                polygon.setStrokeColor(Color.rgb(red, green, blue));
                if(checkBox.isChecked()) polygon.setFillColor(Color.rgb(red, green, blue));

            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(polygon != null) polygon.remove();
                for (Marker marker : markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                checkBox.setChecked(false);
                seek_red.setProgress(0);
                seek_green.setProgress(0);
                seek_blue.setProgress(0);
            }
        });

        seek_red.setOnSeekBarChangeListener(this);
        seek_green.setOnSeekBarChangeListener(this);
        seek_blue.setOnSeekBarChangeListener(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = gmap.addMarker(markerOptions);
                latLngList.add(latLng);
                markerList.add(marker);

            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seek_red:
                red = 1;
                break;
            case R.id.seek_green:
                green = 1;
                break;
            case R.id.seek_blue:
                blue = 1;
                break;
        }
        if(polygon != null) {
            polygon.setStrokeColor(Color.rgb(red, green, blue));
            if (checkBox.isChecked()) polygon.setFillColor(Color.rgb(red, green, blue));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
