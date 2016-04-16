package com.hello_world.ronak.tradify;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by ronak_000 on 4/15/2016.
 */
public class ProductsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }

}
