package com.hello_world.ronak.tradify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterNewProductActivity extends AppCompatActivity {
    ImageView image;
    TextView prodName;
    TextView prodDesc;
    Spinner dropDown1;
    TextView listOfItems;
    ActionBar recyclerActionBar;
    Button tradify;
    String mode = "Sell";
    Products newProduct = new Products();
    Firebase ref = new Firebase("https://tradify.firebaseio.com/Products");
    String imageFile = "";
    String Latitude="NaN",Longitude="NaN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerActionBar = getSupportActionBar();
        recyclerActionBar.setDisplayHomeAsUpEnabled(true);
        recyclerActionBar.setHomeButtonEnabled(true);
        setContentView(R.layout.activity_register_new_product);
        final Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Sell", "Rent", "Trade", "Any"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        image = (ImageView) findViewById(R.id.newProdImage);
        Bundle bundleData = getIntent().getExtras();
        final Bitmap prodImage = (Bitmap) bundleData.getParcelable("prodImage");

        //conversion from image to string
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        prodImage.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
        //prodImage.recycle();
        byte[] byteArray = bYtE.toByteArray();
        imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        image.setImageBitmap(prodImage);
        dropDown1 = (Spinner) findViewById(R.id.spinner);
        dropDown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tradify = (Button) findViewById(R.id.tradifyButton);
        tradify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productID;

                prodName = (TextView) findViewById(R.id.itemName);
                prodDesc = (TextView) findViewById(R.id.itemDetails);
                listOfItems = (TextView) findViewById(R.id.listOfItems);
                //set values into object
                newProduct.setProductName(prodName.getEditableText().toString());
                newProduct.setDescription(prodDesc.getEditableText().toString());
                newProduct.setListOfItems(listOfItems.getEditableText().toString());
                Log.d("latitude", Latitude);
                Log.d("Longitude",Longitude);
                newProduct.setLocation(Latitude + "," + Longitude);
                newProduct.setMode(mode);

                Date currDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String postedDate = format.format(currDate);
                newProduct.setPostedDate(postedDate);


                newProduct.setProductImage(imageFile);
                newProduct.setSold("false");
                newProduct.setUserID(UserContext.USERID);
                productID = UserContext.USEREMAIL + prodName.getEditableText().toString() + prodDesc.getEditableText().toString() + currDate.toString() ;
                productID = String.valueOf(productID.hashCode());
                newProduct.setProductId("PID_" + productID);
                ref.child("PID_" + productID).setValue(newProduct, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.d("Data not saved. ", firebaseError.getMessage());
                        } else {
                            Log.d("Data saved.", "Data saved.");
                            Intent intent = new Intent(RegisterNewProductActivity.this, Activity_HomeScreen.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

}
