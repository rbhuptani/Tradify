package com.hello_world.ronak.tradify;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_HomeScreen extends AppCompatActivity {
    Fragment currFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__home_screen);
        currFragment = Fragement_HomeScreen.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerContainer, currFragment).commit();
    }
}
