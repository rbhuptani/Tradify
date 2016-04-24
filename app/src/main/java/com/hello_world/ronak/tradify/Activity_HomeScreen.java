package com.hello_world.ronak.tradify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.firebase.client.Query;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;

public class Activity_HomeScreen extends AppCompatActivity implements Fragement_HomeScreen.OnListItemSelectedListener, Fragment_Filter.OnFilterAppliedListener {
    Fragment currFragment;
    Toolbar mtoolBar;
    ActionBar mActionbar;

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    View mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__home_screen);
        currFragment = Fragement_HomeScreen.newInstance();
        mLayout = findViewById(R.id.lay_homescreen);
        mtoolBar = (Toolbar) findViewById(R.id.hometoolbar);
        setSupportActionBar(mtoolBar);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mtoolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        findViewById(R.id.fab).setVisibility(View.VISIBLE);
        FloatingActionMenu fab = (FloatingActionMenu) findViewById(R.id.fab);
        FloatingActionButton camera = (FloatingActionButton) fab.getChildAt(0);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                CAMERA_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });

        FloatingActionButton gallery = (FloatingActionButton) fab.getChildAt(1);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                GALLERY_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                    }
                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                }

            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).commit();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rb_sort_pricelh:
                if (checked) {
                    ((RadioButton)findViewById(R.id.rb_sort_pricelh)).setChecked(true);
                    ((RadioButton)findViewById(R.id.rb_sort_pricehl)).setChecked(false);
                }
                break;
            case R.id.rb_sort_pricehl:
                if (checked) {
                    ((RadioButton)findViewById(R.id.rb_sort_pricelh)).setChecked(false);
                    ((RadioButton)findViewById(R.id.rb_sort_pricehl)).setChecked(true);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
            } else {
                Snackbar.make(mLayout, "Can't open gallery.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
            else {
                Snackbar.make(mLayout, "Can't add product.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Register activity", "Register activity");

        if (requestCode == CAMERA_REQUEST_CODE) {
            try{
                if (data != null) {
                    Bitmap prodImage = (Bitmap) data.getExtras().get("data");
                    Intent intent = new Intent(Activity_HomeScreen.this, RegisterNewProductActivity.class);
                    Bundle extra = new Bundle();
                    extra.putParcelable("prodImage", prodImage);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == GALLERY_REQUEST_CODE) {
            try {
                Bitmap prodImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                prodImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                prodImage = decodeSampledBitmapFromResource(byteArray, 200, 200);
                Intent intent = new Intent(Activity_HomeScreen.this, RegisterNewProductActivity.class);
                Bundle extra = new Bundle();
                extra.putParcelable("prodImage", prodImage);
                intent.putExtras(extra);
                startActivity(intent);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] byteArray,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    @Override
    public void onListItemSelected(Products product,Users user) {
        currFragment =  Fragement_ProductDetailView.newInstance(product,user);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).addToBackStack(null).commit();
    }

    @Override
    public void onMenuItemClicked() {
        currFragment =  Fragment_Filter.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).addToBackStack(null).commit();
    }

    @Override
    public void applyFilter(Query ref) {
        currFragment =  Fragement_HomeScreen.newInstance(ref);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).addToBackStack(null).commit();
    }
}
