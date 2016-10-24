package com.manheimthailand.pnaco.manheimcar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Explicit
    private Button signInButton, signUpButton;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        signInButton = (Button) findViewById(R.id.button);
        signUpButton = (Button) findViewById(R.id.button2);
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);

        // SignUp Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        // SignIn Controller
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Value
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                // Check space
                if (userString.equals("") || passwordString.equals("")) {
                    MyAlert myAlert = new MyAlert(MainActivity.this, R.drawable.doremon48,
                            "Have space", "Please fill all every blank");
                    myAlert.myDialog();
                } else {
                    // No space
                    MyContant myContant = new MyContant();
                    SynData synData = new SynData(MainActivity.this);
                    synData.execute(myContant.getUrlJSONString(),
                            myContant.getTestTitleString(),
                            myContant.getTestMessageString());

                }
            }
        });

    }   // Main Method

    private class SynData extends AsyncTask<String, Void, String> {

        // Explicit
        private Context context;
        private String titleString, messageString, truePasswordString;
        private String[] nameStrings, imageStrings, latStrings, lngStrings;
        private boolean aBoolean = true;

        public SynData(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                titleString = params[1];
                messageString = params[2];

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("24octV1", "e doInBackground ==> " + e.toString());
                return null;
            }

        }   // doInBackground

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("24octV1", "JSON ==> " + s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                nameStrings = new String[jsonArray.length()];
                imageStrings = new String[jsonArray.length()];
                latStrings = new String[jsonArray.length()];
                lngStrings = new String[jsonArray.length()];


                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);



                    // Check users
                    if (userString.contentEquals(jsonObject.getString("User"))) {
                        aBoolean = false;
                        truePasswordString = jsonObject.getString("Password");
                    }   // if

                    // Setup Array
                    nameStrings[i] = jsonObject.getString("Name");
                    imageStrings[i] = jsonObject.getString("Image");
                    latStrings[i] = jsonObject.getString("Lat");
                    lngStrings[i] = jsonObject.getString("Lng");

                    // Check
                    Log.d("24octV4", "Name (" + i + ") ==> " + nameStrings[i]);
                    Log.d("24octV4", "Image (" + i + ") ==> " + imageStrings[i]);
                    Log.d("24octV4", "Lat (" + i + ") ==> " + latStrings[i]);
                    Log.d("24octV4", "Lng (" + i + ") ==> " + lngStrings[i]);

                }   // for loop

                if (aBoolean) {
                    MyAlert myAlert = new MyAlert(context, R.drawable.kon48, titleString, messageString);
                    myAlert.myDialog();

                } else if (passwordString.equals(truePasswordString)) {
                    // Password True
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();

                    // Check Array
                    Log.d("24octV5", "Total records ==> " + nameStrings.length);

                    for (int i=0; i<nameStrings.length; i++) {
                        Log.d("24octV5", "Name (" + i + ") ==> " + nameStrings[i]);
                        Log.d("24octV5", "Image (" + i + ") ==> " + imageStrings[i]);
                        Log.d("24octV5", "Lat (" + i + ") ==> " + latStrings[i]);
                        Log.d("24octV5", "Lng (" + i + ") ==> " + lngStrings[i]);

                    }


                    Intent intent = new Intent(MainActivity.this, ListService.class);

                    // Put data to listService
                    intent.putExtra("Name", nameStrings);
                    intent.putExtra("Image", imageStrings);
                    intent.putExtra("Lat", latStrings);
                    intent.putExtra("Lng", lngStrings);

                    startActivity(intent);
                    finish();

                } else {
                    // Password false
                    MyAlert myAlert = new MyAlert(context, R.drawable.doremon48, "Password flase", "Please try again password false");
                    myAlert.myDialog();
                }

            } catch (Exception e) {
                Log.d("24octV2", "e onPost ==> " + e.toString());
            }
        }
    }   // SynData

}   // Main Class
