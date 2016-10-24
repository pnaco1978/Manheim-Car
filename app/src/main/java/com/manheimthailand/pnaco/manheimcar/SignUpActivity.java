package com.manheimthailand.pnaco.manheimcar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    // Explicit
    private EditText nameEdiText, userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString, userString, passwordString, imageString, imagePathString, imageNameString;
    private Uri uri;
    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Bind Widget
        nameEdiText = (EditText) findViewById(R.id.editText);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText2);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);

        // Button Controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Value From Edit Text
                nameString = nameEdiText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                // Check Space
                if (nameString.equals("") || userString.equals("") || passwordString.equals("")) {
                    // Have Space
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.bird48, "don't space", "please verifry value");
                    myAlert.myDialog();
                } else if (aBoolean) {
                    // Non Choose Image
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.doremon48, "Non Choose Image", "please Choose Image");
                    myAlert.myDialog();
                } else {
                    // Choose Image finish
                    upLoadImageToServer();

                    // Update String to Server
                    AddUser addUser = new AddUser(SignUpActivity.this);
                    MyContant myContant = new MyContant();
                    addUser.execute(myContant.getUrlAddUserString());

                }



            }   // onClick
        });

        // Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please select your picture."), 1);

            }   // onClick
        });

    }   // Main Method

    // Create Inner Class
    private class AddUser extends AsyncTask<String, Void, String> {

        // Explicit
        private Context context;

        public AddUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                MyContant myContant = new MyContant();
                imageString = myContant.getUrlImageString() + imageNameString;
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Name", nameString)
                        .add("User", userString)
                        .add("Password", passwordString)
                        .add("Image", imageString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("23octV2", "e doInBackground ==> " + e.toString());
                return null;
            }

        }   // doInBackground

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("23octV2", "Result ==> " + s);
            String result = null;
            if (Boolean.parseBoolean(s)) {
                result = "Upload Value Finish";
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                finish();

            } else {
                result = "Can't Upload";
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }

        }   // onPostExecute
    }   // AddUser Class


    private void upLoadImageToServer() {

        // Create Policy
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {
            MyContant myContant = new MyContant();
            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect(myContant.getHostString(), myContant.getPortAnInt(), myContant.getUserFTPString(), myContant.getPassFTPwordString());
            simpleFTP.bin();
            simpleFTP.cwd("images");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

            Toast.makeText(SignUpActivity.this, "Upload " + imageNameString + " finished.'", Toast.LENGTH_SHORT).show();
            Log.d("23octV1", "Upload " + imageNameString + " finished.'");

        } catch (Exception e) {
            // e.printStackTrace();
            Log.d("23octV1", "e simpleFTP ==> " + e.toString());
        }
    }   // upLoadImageToServer

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Log.d("23octV1", "Result OF");

            aBoolean = false;

            // setupImage
            uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }   // tryCatch

            // Find path and Name Image
            imagePathString = myFindPath(uri);
            Log.d("23octV1", "imagePathString ==> " + imagePathString);
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("23octV1", "imageNameString ==> " + imageNameString);
        }   // if

    }   // onActivityResult

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(i);
        } else {
            result = uri.getPath();
        }
        return result;
    }
}   // Main Class
