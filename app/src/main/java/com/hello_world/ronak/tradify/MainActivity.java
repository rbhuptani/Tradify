package com.hello_world.ronak.tradify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    Button btn_lo;
    TextView txt_name,txt_id,txt_email;
    ImageView img_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_lo = (Button)findViewById(R.id.btn_logout);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_email = (TextView) findViewById(R.id.txt_email);
        img_profile = (ImageView) findViewById(R.id.img_Profile);
        txt_name.setText(UserContext.USERNAME);
        txt_id.setText(UserContext.USERID);
        txt_email.setText(UserContext.USEREMAIL);
        Picasso.with(getApplicationContext()).load(UserContext.USERPROFILEURL).into(img_profile);
        btn_lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.firebaseRef.unauth();
                intent = new Intent(getApplication(),LoginActivity.class);
                startActivity(intent);
            }
        });
        Log.d("", "");
    }
}
