package com.hello_world.ronak.tradify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class AccountSettingsActivity extends AppCompatActivity {
    static Firebase ref = new Firebase("https://tradify.firebaseio.com");
    static Firebase userRef = new Firebase("https://tradify.firebaseio.com/Users");
    Button saveBtn;
    EditText userName, oldPass, newPass, confirmPass;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        userName= (EditText) findViewById(R.id.profNameEditText);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        userName.setText(UserContext.USERNAME);
        profileImage.setImageBitmap(StringToBitMap(UserContext.USERPROFILEURL));
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
                            }
                        });
                    } else {
                        Toast.makeText(AccountSettingsActivity.this, "New and confirm password do not match.", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        return true;
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

}
