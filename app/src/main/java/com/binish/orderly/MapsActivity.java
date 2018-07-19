package com.binish.orderly;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng companyLocation;
    Marker marker, kathmandu, previousLocation;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if ((getIntent().getDoubleExtra("locationLat", 0)) != 0
                && (getIntent().getDoubleExtra("locationLon", 0)) != 0
                && !((getIntent().getIntExtra("origin",0))==1)) {
            LatLng company = new LatLng((getIntent().getDoubleExtra("locationLat", 0)), (getIntent().getDoubleExtra("locationLon", 0)));
            mMap.addMarker(new MarkerOptions().position(company).title(getIntent().getStringExtra("companyname")));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(company, 16));
            check = 1;
        } else if ((getIntent().getIntExtra("origin",0))==1
                && (getIntent().getDoubleExtra("locationLat", 0)) != 0
                && (getIntent().getDoubleExtra("locationLon", 0)) != 0) {
            LatLng company = new LatLng((getIntent().getDoubleExtra("locationLat", 0)), (getIntent().getDoubleExtra("locationLon", 0)));
            previousLocation = mMap.addMarker(new MarkerOptions().position(company).title(getIntent().getStringExtra("companyname")));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(company, 16));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                return;
            }
            mMap.setMyLocationEnabled(true);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    previousLocation.remove();
                    if (marker != null) {
                        marker.setPosition(latLng);
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location").draggable(true));
                    }
                    companyLocation = latLng;
                }
            });
        } else {
            LatLng nepal = new LatLng(27.7172, 85.3240);
            kathmandu = mMap.addMarker(new MarkerOptions().position(nepal).title("Marker in Kathmandu"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nepal, 14));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                return;
            }
            mMap.setMyLocationEnabled(true);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    kathmandu.remove();
                    if (marker != null) {
                        marker.setPosition(latLng);
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location").draggable(true));
                    }
                    companyLocation = latLng;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (check == 1) {
            super.onBackPressed();
            check = 0;
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Confirm Location");
            alertbox.setMessage("Are you sure ?");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent returnIntent = new Intent(MapsActivity.this, RegisterCompanyActivity.class);
                    returnIntent.putExtra("latitude", String.valueOf(companyLocation.latitude));
                    returnIntent.putExtra("longitude", String.valueOf(companyLocation.longitude));
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    dialog.dismiss();
                }
            });
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent returnIntent = new Intent(MapsActivity.this, RegisterCompanyActivity.class);
                    setResult(201, returnIntent);
                    finish();
                    dialog.dismiss();
                }
            });
            alertbox.setNeutralButton("Remove Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent returnIntent = new Intent(MapsActivity.this, RegisterCompanyActivity.class);
                    setResult(RESULT_CANCELED, returnIntent);
                    finish();
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertbox.create();
            alertDialog.show();
        }
    }
}
