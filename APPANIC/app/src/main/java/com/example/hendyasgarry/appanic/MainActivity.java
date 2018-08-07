package com.example.hendyasgarry.appanic;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends /*AppCompatActivity*/ FragmentActivity implements OnMapReadyCallback, LocationListener {

    FirebaseAuth auth;
    private static final int PERMISSIONS_REQUEST_SMS = 0;
    Button sendBtn;
    String phone;
    String message;

    private static final int LOCATION_REQUEST_PERMISSION_CODE = 99;
    GoogleMap mMap;
    Location location;
    LocationManager locationManager;
    LatLng latLng;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = (Button) findViewById(R.id.btnSendSMS);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        //initializing firebase authentication object
        auth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(auth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginAct.class));
        }
    }

    protected void sendSMS() {
        phone = "085732147662";
        message = "kerabat anda sedang dalam keadaan darurat, lokasi terakhir kerabat anda : \n\n"
                + "google.com/maps/@" + latitude + "," + longitude;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone,null,message,null,null);
        Intent b = new Intent(Intent.ACTION_MAIN);
        b.setType("vnd.android-dir/mms-sms");
        startActivity(b);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_CODE);
            showSettingsAlert();
            return;
        }
        mMap.setMyLocationEnabled(true);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            //GPS aktif
            setLocation();
            onLocationChanged(location);
        } else {
            locationManager.requestLocationUpdates(bestProvider, 1000, 1, this);
        }
    }

    /**Ngubah posisi marker dan
     * zoom ke posisi saat ini*/
    private void setLocation(){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng =  new LatLng(latitude,longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**Menyuruh membuka settings
     * apabila GPS belum menyala*/
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("Enable GPS Settings first. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void logout(View view) {
        //logging out the user
        auth.signOut();
        //closing activity
        finish();
        //starting login activity
        startActivity(new Intent(this, LoginAct.class));
    }
}

