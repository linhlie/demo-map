package com.example.app.kidstracking;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MapDTO> mapDTOS;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/maps");

    DatabaseReference check = database.getReference("server/saving-data/check");

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (Button)findViewById(R.id.nextSOS);

        check.child(String.valueOf("check")).setValue(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SOSActivity.class);
                view.getContext().startActivity(intent);}
        });
//
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
                      .title("Điểm kết thúc!")
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

              mMap.addMarker(new MarkerOptions().position(start).title("Điểm bắt đầu!"));
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