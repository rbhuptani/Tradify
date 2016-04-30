package com.hello_world.ronak.tradify;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class AccountSettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    static Firebase ref = new Firebase("https://tradify.firebaseio.com");
    static Firebase userRef = new Firebase("https://tradify.firebaseio.com/Users");
    Button saveBtn;
    EditText userName, oldPass, newPass, confirmPass, contactNo, address;
    ImageView profileImage;
    private static int GALLERY_REQUEST_CODE = 2;
    Toolbar mtoolBar;
    Intent intent;
    ActionBar mActionbar;
    DrawerLayout mdrawerLayout;
    NavigationView mnavigationView;
    boolean error = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String userId = UserContext.USERID;
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number of children",String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    Object o = users.child("UserId").getValue();
                    String userFromDb = "NA";
                    if(o != null)
                        userFromDb = o.toString();
                    Log.d("userFromDb",userFromDb);
                    if (userFromDb.equals(userId)) {
                        UserContext.USERNAME = users.child("Username").getValue().toString();
                        UserContext.USERADRESS = users.child("Address").getValue().toString();
                        //UserContext.USEREMAIL = users.child("Email").getValue().toString();
                        UserContext.USERCONTACTNUMBER = users.child("ContactNumber").getValue().toString();/*
                        if(users.child("UserImage").getValue()!=null) {
                            byte[] imageAsBytes = Base64.decode(users.child("UserImage").getValue().toString().getBytes(), 0);
                            Bitmap bp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                            profilePic.setImageBitmap(bp);
                        }*/
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.d("usercontext ACCT SET: ", UserContext.USERNAME + "," + UserContext.USERID + "," + UserContext.USEREMAIL + "," + UserContext.USERCONTACTNUMBER + "," + UserContext.USERADRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        mtoolBar = (Toolbar) findViewById(R.id.toolbar_as);
        mtoolBar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolBar);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mtoolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        mnavigationView = (NavigationView) findViewById(R.id.navigation_view_as);
        Bitmap b = StringToBitMap(UserContext.USERPROFILEURL);
        ((ImageView)mnavigationView.getHeaderView(0).findViewById(R.id.profile_image)).setImageBitmap(b);
        ((TextView)mnavigationView.getHeaderView(0).findViewById(R.id.nav_profile_name)).setText(UserContext.USERNAME);
        mnavigationView.setNavigationItemSelectedListener(this);

        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_as);
        ActionBarDrawerToggle actionBarDrawerToggle =new ActionBarDrawerToggle(this,mdrawerLayout,mtoolBar,R.string.abt_od, R.string.abt_cd) {
            @Override
            public  void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public  void onDrawerOpened(View v){
                super.onDrawerOpened(v);
            }

        };

        mdrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        userName= (EditText) findViewById(R.id.profNameEditText);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        userName.setText(UserContext.USERNAME);
        profileImage.setImageBitmap(StringToBitMap(UserContext.USERPROFILEURL));
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                GALLERY_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                }
            }
        });
        contactNo = (EditText) findViewById(R.id.contactNoEditText);
        contactNo.setText(UserContext.USERCONTACTNUMBER);
        address= (EditText) findViewById(R.id.addressEditText);
        address.setText(UserContext.USERADRESS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_settings_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.saveSettings:
                error= false;
                userName= (EditText) findViewById(R.id.profNameEditText);
                String userProfName= userName.getText().toString();
                if(!userProfName.isEmpty()){
                    userRef.child(UserContext.USERID).child("Username").setValue(userProfName);
                }
                confirmPass = (EditText) findViewById(R.id.confirmPassEditText);
                //saveBtn = (Button) findViewById(R.id.save);
                oldPass = (EditText) findViewById(R.id.oldPassEditText);
                newPass = (EditText) findViewById(R.id.newPassEditText);
                String oldPassword = oldPass.getText().toString();
                String newPassword = newPass.getText().toString();
                if (!oldPassword.isEmpty()) {
                    if (!newPassword.isEmpty() && newPassword.equals(confirmPass.getText().toString())) {
                        ref.changePassword("testuser@gmail.com", oldPassword, newPass.getText().toString(), new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(AccountSettingsActivity.this, "Woila! Password change success!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Toast.makeText(AccountSettingsActivity.this, "Arrgh! Password change failed!", Toast.LENGTH_SHORT).show();
                                error = true;
                            }
                        });
                    } else {
                        Toast.makeText(AccountSettingsActivity.this, "New and confirm password do not match.", Toast.LENGTH_SHORT).show();
                        error = true;
                    }
                }
                contactNo = (EditText) findViewById(R.id.contactNoEditText);
                String contactNumber = contactNo.getText().toString();
                if(!contactNumber.isEmpty()){
                    userRef.child(UserContext.USERID).child("ContactNumber").setValue(contactNumber);
                }else{
                    Toast.makeText(AccountSettingsActivity.this, "Sorry! You must have a contact number.", Toast.LENGTH_SHORT).show();
                    error = true;
                }
                address= (EditText) findViewById(R.id.addressEditText);
                String addressStr= address.getText().toString();
                if(!addressStr.isEmpty()){
                    userRef.child(UserContext.USERID).child("Address").setValue(addressStr);
                }else{
                    Toast.makeText(AccountSettingsActivity.this, "Sorry! You must have an address.", Toast.LENGTH_SHORT).show();
                    error = true;
                }
                if(!error){
                    Toast.makeText(AccountSettingsActivity.this, "Settings saved.", Toast.LENGTH_SHORT).show();
                }
        }
        return true;
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
                Toast.makeText(AccountSettingsActivity.this, "Can't open gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            try {
                Bitmap prodImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                prodImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                prodImage = decodeSampledBitmapFromResource(byteArray, 200, 200);
                String bitmapStr = BitMapToString(prodImage);
                userRef.child(UserContext.USERID).child("UserImage").setValue(bitmapStr);
                UserContext.USERPROFILEURL =  bitmapStr;
                /*Intent intent = getIntent();
                Bundle extra = new Bundle();
                extra.putParcelable("prodImage", prodImage);
                intent.putExtras(extra);*/
                finish();
                startActivity(getIntent());
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

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.drw_home:
                intent = new Intent(this,Activity_HomeScreen.class);
                startActivity(intent);
                break;
            case R.id.drw_settings:
                intent = new Intent(this,AccountSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.drw_my_profile:
                //Toast.makeText(getApplicationContext(),"My profile clicked",Toast.LENGTH_SHORT).show();
                intent = new Intent(this,UserProfileActivity.class);
                intent.putExtra("username", UserContext.USERID);
                startActivity(intent);
                break;
            case R.id.drw_abtus:
                intent = new Intent(this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.drw_logoff:
                LoginActivity.firebaseRef.unauth();
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
