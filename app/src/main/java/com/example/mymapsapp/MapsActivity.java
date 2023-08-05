package com.example.mymapsapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mymapsapp.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LatLng latLng;
    String place;
    private static final int LOCATION_PERMISSION_CODE=101;

    @Override
    public void onBackPressed() {
        Intent i= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i=getIntent();
        String loc=i.getStringExtra("location");

        if (!isLocationEnabled()) {
            requestLocationAccess();// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        }

        Geocoder geocoder= new Geocoder(this, Locale.getDefault());
        try {
            assert loc != null;
            List<Address> listAddress=geocoder.getFromLocationName(loc,1);
            assert listAddress != null;
            if (listAddress.size()>0){
                Address address=listAddress.get(0);
                place=address.getLocality()+", "+address.getAdminArea()+", "+address.getCountryName();
                latLng =new LatLng(address.getLatitude(),address.getLongitude());

                //Storing in Sql
                DbHelper dbHelper=new DbHelper(MapsActivity.this);
                dbHelper.insert(place,address.getLatitude(),address.getLongitude());

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync(this);
            }
            else{
                Toast.makeText(MapsActivity.this,"No places found for this search",Toast.LENGTH_SHORT).show();

            }
        }catch (IOException e){
            e.printStackTrace();
        }



    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(latLng).title(place));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12) );
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMapClickListener(latLng1 -> {
            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng1.latitude,latLng1.longitude,1);
                Address address11=addresses.get(0);
                String location=address11.getLocality()+", "+address11.getAdminArea()+", "+address11.getCountryName();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("LOCATION")
                        .setMessage("Do you want to save the location?\nName : "+location+"\nLatitude : "+latLng1.latitude+"\nLongitude : "+latLng1.longitude)
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            DbHelper dbHelper=new DbHelper(MapsActivity.this);
                            dbHelper.insert(location,latLng1.latitude,latLng1.longitude);
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private boolean isLocationEnabled(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationAccess(){
        ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng1) {
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng1.latitude,latLng1.longitude,1);
            Address address11=addresses.get(0);
            String location=address11.getLocality()+", "+address11.getAdminArea()+", "+address11.getCountryName();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("LOCATION")
                    .setMessage("Do you want to save the location?\nName : "+location+"\nLatitude : "+latLng1.latitude+"\nLongitude : "+latLng1.longitude)
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DbHelper dbHelper=new DbHelper(MapsActivity.this);
                        dbHelper.insert(location,latLng1.latitude,latLng1.longitude);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}