package com.manheimthailand.pnaco.manheimcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.DetectedActivity;

public class ListService extends AppCompatActivity {

    // Explicit
    private ListView listView;
    private String[] nameStrings, latStrings, lngStrings, imageStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);

        // Bind Widget
        listView = (ListView) findViewById(R.id.livOfficer);

        // Get Value form Intent
        nameStrings = getIntent().getStringArrayExtra("Name");
        latStrings = getIntent().getStringArrayExtra("Lat");
        lngStrings = getIntent().getStringArrayExtra("Lng");
        imageStrings = getIntent().getStringArrayExtra("Image");

        // Check Array
        Log.d("24octV3", "Total records ==> " + nameStrings.length);

        for (int i=0; i<nameStrings.length; i++) {
            Log.d("24octV3", "Name (" + i + ") ==> " + nameStrings[i]);
            Log.d("24octV3", "Image (" + i + ") ==> " + imageStrings[i]);
            Log.d("24octV3", "Lat (" + i + ") ==> " + latStrings[i]);
            Log.d("24octV3", "Lng (" + i + ") ==> " + lngStrings[i]);

        }

        // Create ListView
        OfficerAdapter officerAdapter = new OfficerAdapter(ListService.this, nameStrings, latStrings, lngStrings, imageStrings);
        listView.setAdapter(officerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListService.this, detailActivity.class);
                startActivity(intent);

            }
        });

    }
}
