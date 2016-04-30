package com.hello_world.ronak.tradify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Query;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Activity_HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,Fragement_HomeScreen.OnListItemSelectedListener, Fragment_Filter.OnFilterAppliedListener {
    Fragment currFragment;
    Toolbar mtoolBar;
    Intent intent;
    ActionBar mActionbar;
    DrawerLayout mdrawerLayout;
    NavigationView mnavigationView;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    static boolean FILTER_CAT = false;
    static boolean FILTER_PRICE = false;
    static boolean FILTER_TIME = false;
    static boolean SORT_PRICE = false;
    static boolean SORT_TIME = false;
    Products product;
    Users user;
    View mLayout;
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public class DetailsTransition extends TransitionSet {

        public DetailsTransition(){
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform());
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment instanceof  Fragment_Filter || fragment instanceof Fragement_ProductDetailView) {
                    super.onBackPressed();
                    return;
                }
            }
        }

        if (doubleBackToExitPressedOnce) {
            Log.d("here","here");
            this.finishAffinity();
           // android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 5000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__home_screen);
        mtoolBar = (Toolbar) findViewById(R.id.hometoolbar);
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
        findViewById(R.id.fab).setVisibility(View.VISIBLE);
        mnavigationView = (NavigationView) findViewById(R.id.navigation_view);
        Bitmap b = StringToBitMap(UserContext.USERPROFILEURL);
        ((ImageView)mnavigationView.getHeaderView(0).findViewById(R.id.profile_image)).setImageBitmap(b);
        ((TextView)mnavigationView.getHeaderView(0).findViewById(R.id.nav_profile_name)).setText(UserContext.USERNAME);
        mnavigationView.setNavigationItemSelectedListener(this);

        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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

        Bundle bundleData = getIntent().getExtras();
        if(bundleData!=null) {
            boolean isUserFlow = (boolean) bundleData.getSerializable("userprofile");
            if(isUserFlow) {
                product = (Products) bundleData.getSerializable("product");
                user = (Users) bundleData.getSerializable("user");
                currFragment = Fragement_ProductDetailView.newInstance(product, user);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recyclerContainer, currFragment).addToBackStack(null).commit();
            }
        }
        else {
            currFragment = Fragement_HomeScreen.newInstance();
            mLayout = findViewById(R.id.lay_homescreen);
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
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                    }

                }
            });

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recyclerContainer, currFragment).commitAllowingStateLoss();
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        resetRadioButtons(view);
        switch(view.getId()) {
            case R.id.rb_sort_pricelh:
                SORT_PRICE = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.rb_sort_pricelh)).setChecked(true);
                break;
            case R.id.chk_ele:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_ele)).setChecked(true);
                break;
            case R.id.chk_cars:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_cars)).setChecked(true);
                break;
            case R.id.chk_sports:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_sports)).setChecked(true);
                break;
            case R.id.chk_home:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_home)).setChecked(true);
                break;
            case R.id.chk_movies:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_movies)).setChecked(true);
                break;
            case R.id.chk_fashion:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_fashion)).setChecked(true);
                break;
            case R.id.chk_baby:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_baby)).setChecked(true);
                break;
            case R.id.chk_others:
                FILTER_CAT = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_others)).setChecked(true);
                break;
            case R.id.chk_24hr:
                FILTER_TIME = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_24hr)).setChecked(true);
                break;
            case R.id.chk_7days:
                FILTER_TIME = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_7days)).setChecked(true);
                break;
            case R.id.chk_30days:
                FILTER_TIME = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_30days)).setChecked(true);
                break;
            case R.id.chk_sort_posted:
                SORT_TIME = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_sort_posted)).setChecked(true);
                break;
            case R.id.chk_price:
                FILTER_PRICE = true;
                if (checked)
                    ((RadioButton)findViewById(R.id.chk_price)).setChecked(true);
                break;
        }
    }

    public void resetRadioButtons(View view){
        ((RadioButton)findViewById(R.id.chk_ele)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_cars)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_sports)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_home)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_movies)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_fashion)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_baby)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_others)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_24hr)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_7days)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_30days)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_sort_posted)).setChecked(false);
        ((RadioButton)findViewById(R.id.rb_sort_pricelh)).setChecked(false);
        ((RadioButton)findViewById(R.id.chk_price)).setChecked(false);
        ((EditText)findViewById(R.id.edit_prc_min)).setText("");
        ((EditText)findViewById(R.id.edit_prc_max)).setText("");
        FILTER_CAT = false;
        FILTER_PRICE = false;
        FILTER_TIME = false;
        SORT_PRICE = false;
        SORT_TIME = false;

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
    public void onListItemSelected(Products product,Users user,View sharedElement) {
        currFragment =  Fragement_ProductDetailView.newInstance(product, user);
        currFragment.setSharedElementEnterTransition(new DetailsTransition().setDuration(500));
        currFragment.setEnterTransition(new Fade());
        currFragment.setExitTransition(new Fade());
        currFragment.setSharedElementReturnTransition(new DetailsTransition().setDuration(500));
        Log.d("SElement_homescree : ", sharedElement.getTransitionName());
        getSupportFragmentManager().beginTransaction().addSharedElement(sharedElement, sharedElement.getTransitionName())
                .replace(R.id.recyclerContainer, currFragment).commitAllowingStateLoss();

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).addToBackStack(null).commit();*/
    }

    @Override
    public void onMenuItemClicked() {
        currFragment =  Fragment_Filter.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).commitAllowingStateLoss();
    }

    @Override
    public void searchItemClicked(Query ref) {
        currFragment =  Fragement_HomeScreen.newInstance(ref);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).commitAllowingStateLoss();
    }

    @Override
    public void applyFilter(Query ref) {
        currFragment =  Fragement_HomeScreen.newInstance(ref);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).addToBackStack(null).commitAllowingStateLoss();
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
