package com.example.app.kidstracking;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.kidstracking.model.MapDTO;
import com.example.app.kidstracking.service.RestartServiceBroadcastReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MapDTO> mapDTOS;
    private Button btn;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/maps");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btn = findViewById(R.id.btn_next);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        DatabaseReference postsRef = ref.child("maps");

//        DatabaseReference newPostRef = postsRef.push();
        ref.child("id").setValue(new MapDTO(20.983650, 105.792398));
        ref.child("id1").setValue(new MapDTO(20.985580, 105.795075));
        ref.child("id2").setValue(new MapDTO(20.985800, 105.794903));

//        startService(new Intent(this, ServiceSchedule.class));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapDTOS = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
//              mapDTOS.clear();
              for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                  MapDTO map = snapshot.getValue(MapDTO.class);
//                  ((ArrayList) mapDTOS ).add(map);
                  mapDTOS.add(map);
              }
              // Add a marker in Sydney and move the camera
              LatLng start = new LatLng(mapDTOS.get(0).getLatitude(), mapDTOS.get(0).getLongitude());
              LatLng end = new LatLng(mapDTOS.get(mapDTOS.size()-1).getLatitude(), mapDTOS.get(mapDTOS.size()-1).getLongitude());
              //tao 1 marker
              mMap.addMarker(new MarkerOptions()
                      .position(end)
                      .title("Bưu điện - Hà Đông")
                      .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
              );
              for (int i = 0; i < mapDTOS.size() - 1; i++) {
                  MapDTO src = mapDTOS.get(i);
                  MapDTO dest = mapDTOS.get(i + 1);

                  // mMap is the Map Object
                  Polyline line = mMap.addPolyline(
                          new PolylineOptions().add(
                                  new LatLng(src.getLatitude(), src.getLongitude()),
                                  new LatLng(dest.getLatitude(),dest.getLongitude())
                          ).width(10).color(Color.BLUE).geodesic(true)
                  );
              }

              mMap.addMarker(new MarkerOptions().position(start).title("Marker in KMA"));
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(end,18));

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
              System.out.println("The read failed: " + databaseError.getCode());
          }
      });
        mMap = googleMap;
    }
}