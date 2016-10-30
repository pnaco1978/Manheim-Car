package com.manheimthailand.pnaco.manheimcar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class detailActivity extends FragmentActivity implements OnMapReadyCallback {
    // Explicit
    private GoogleMap mMap;
    private String nameString, latString, lngString;
    private Double latADouble, lngADouble;
    private LatLng latLng;
    private Button backButton, qrCodeButton;
    private String modeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_detail);
        setContentView(R.layout.my_detail_layout);

        // Build widget
        backButton = (Button) findViewById(R.id.button4);
        qrCodeButton = (Button) findViewById(R.id.button5);

        // Back Controller
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); // OnClick backButton

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(detailActivity.this);
                builder.setCancelable(false);
                builder.setIcon(R.drawable.doremon48);
                builder.setTitle("Please select processing mode.");
                builder.setMessage("Bar Code or QR Code");
                builder.setNegativeButton("Bar Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        readCode("BAR_CODE_MODE");
                        dialogInterface.dismiss();

                    }
                });
                builder.setPositiveButton("QR Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        readCode("QR_CODE_MODE");
                    }
                });
                builder.show();

            }
        }); // OnClick qrCodeButton

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get Value from Intent
        nameString = getIntent().getStringExtra("Name");
        latString = getIntent().getStringExtra("Lat");
        lngString = getIntent().getStringExtra("Lng");

    }   // Main Method onCreate

    private void readCode(String modeString) {
        Log.d("30octV4", "Mode ==> " + modeString);

        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", modeString);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            Toast.makeText(detailActivity.this, "Please Install Barcode scanner", Toast.LENGTH_SHORT).show();

        }

    }   // Read Code

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            String scanResult = data.getStringExtra("SCAN_RESULT");
            Log.d("30octV4", "SCAN_RESULT ==> " + scanResult);
        }   // if

    }   //onActivityResult

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        latADouble = Double.parseDouble(latString);
        lngADouble = Double.parseDouble(lngString);
        latLng = new LatLng(latADouble, lngADouble);

        // Setup center MAP
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(nameString)
                .snippet("This is snippet."));

        LatLng connerLatLng = new LatLng(13.721540, 100.702816);
        mMap.addMarker(new MarkerOptions()
                .position(connerLatLng)
                .title("Junction")
                .snippet("Sirindhorn Hospital Junction")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.vn)));


        // mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in user location"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }   // onMapReady
}   // Main Class
