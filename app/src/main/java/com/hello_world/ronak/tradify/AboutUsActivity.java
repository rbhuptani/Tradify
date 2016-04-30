package com.hello_world.ronak.tradify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    Toolbar mtoolBar;
    Intent intent;
    ActionBar mActionbar;
    DrawerLayout mdrawerLayout;
    NavigationView mnavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mtoolBar = (Toolbar) findViewById(R.id.toolbarau);
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
        mnavigationView = (NavigationView) findViewById(R.id.navigation_view_au);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_mail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ronakbhuptani@hotmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Talk to us");
                intent.putExtra(Intent.EXTRA_TEXT, "Write something here");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
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
