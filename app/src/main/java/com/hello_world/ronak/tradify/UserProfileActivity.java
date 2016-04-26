package com.hello_world.ronak.tradify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class UserProfileActivity extends AppCompatActivity {
    Fragment userProfileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle extra = getIntent().getExtras();
        /*String username = "";
        if(extra!=null){
            username = extra.getString("username");
        }*/
        userProfileFragment = UserProfileFragment.newInstance();
        userProfileFragment.setArguments(extra);
        getSupportFragmentManager().beginTransaction().replace(R.id.userProfileContainer,userProfileFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_prod_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.editProfile:
                Intent intent = new Intent(UserProfileActivity.this,AccountSettingsActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }
}

