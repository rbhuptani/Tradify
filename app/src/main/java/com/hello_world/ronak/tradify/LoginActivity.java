package com.hello_world.ronak.tradify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class LoginActivity extends FirebaseLoginBaseActivity{

    static Firebase firebaseRef;
    EditText userNameET;
    EditText passwordET;
    String mName;

    /* String Constants */
    private static final String FIREBASEREF = "https://tradify.firebaseio.com/";
    private static final String FIREBASE_ERROR = "Firebase Error";
    private static final String USER_ERROR = "User Error";
    private static final String LOGIN_SUCCESS = "Login Success";
    private static final String LOGOUT_SUCCESS = "Logout Success";
    private static final String USER_CREATION_SUCCESS =  "Successfully created user";
    private static final String USER_CREATION_ERROR =  "User creation error";
    private static final String EMAIL_INVALID =  "email is invalid :";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this.getApplicationContext());
        firebaseRef = new Firebase(FIREBASEREF);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameET = (EditText)findViewById(R.id.edit_text_email);
        passwordET = (EditText)findViewById(R.id.edit_text_password);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showFirebaseLoginPrompt();
            }
        });

        Button createButton = (Button) findViewById(R.id.button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {
        Snackbar snackbar = Snackbar.
                make(userNameET, FIREBASE_ERROR + firebaseLoginError.message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        resetFirebaseLoginPrompt();
    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {
        Snackbar snackbar = Snackbar
                .make(userNameET, USER_ERROR + firebaseLoginError.message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        resetFirebaseLoginPrompt();
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid()); System.exit(1);
    }


    @Override
    public Firebase getFirebaseRef() {
        return firebaseRef;
    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        String userID="";
        switch (authData.getProvider()) {
            case "password":
                mName = (String) authData.getProviderData().get("email");
                String user_id = String.valueOf(mName.hashCode());
                break;
            default:
                mName = (String) authData.getProviderData().get("displayName");
                UserContext.USERNAME = authData.getProviderData().get("displayName").toString();
                userID = authData.getProviderData().get("id").toString();
                if(authData.getProviderData().get("email") == null)
                    UserContext.USEREMAIL = "";
                else
                    UserContext.USEREMAIL =authData.getProviderData().get("email").toString();
                UserContext.USERPROFILEURL = authData.getProviderData().get("profileImageURL").toString();
                addUsertoDB(userID,UserContext.USERNAME,"NaN",UserContext.USEREMAIL,UserContext.USERPROFILEURL);
                break;
        }
        //Toast.makeText(getApplicationContext(), LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
        setUserDetails(userID);
        //Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        Intent myIntent = new Intent(LoginActivity.this, Activity_HomeScreen.class);
        LoginActivity.this.startActivity(myIntent);
    }

    public void setUserDetails(String userID){
        final Firebase uref = new Firebase("https://tradify.firebaseio.com/Users");
        uref.child(userID).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                for(MutableData md :mutableData.getChildren() ){
                    if(md.getKey() == "Username")
                        UserContext.USERNAME = md.getValue().toString();
                    if(md.getKey() == "UserId")
                        UserContext.USERID = md.getValue().toString();
                    if(md.getKey() == "Email")
                        UserContext.USEREMAIL = md.getValue().toString();
                    if(md.getKey() == "UserImage")
                        UserContext.USERPROFILEURL = md.getValue().toString();
                    if(md.getKey() == "Address")
                        UserContext.USERADRESS = md.getValue().toString();
                    if(md.getKey() == "ContactNumber")
                        UserContext.USERCONTACTNUMBER = md.getValue().toString();
                }
                Log.d("USer Context : ",UserContext.USERNAME + "," + UserContext.USERID + "," + UserContext.USEREMAIL + ","+UserContext.USERADRESS  + ","+UserContext.USERCONTACTNUMBER );
                return Transaction.abort();
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                if (b) {
                    //Log.d("Check","user added");
                } else {
                   // Log.d("Check","user exist");
                }
            }
        });

    }

    public void addUsertoDB(final String uid,String uname,String uPassword,String uemail, final String upic){
        final Firebase uref = new Firebase("https://tradify.firebaseio.com/Users");
        final Users user = new Users();
        Log.d("Email", uemail);
        if(uemail == "")
            uemail = "Not Available";

        user.setUserId(uid);
        user.setEmail(uemail);
        user.setUsername(uname);
        user.setPassword(uPassword);
        user.setAddress("");
        user.setContactNumber("");
        uref.child(uid).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                String bimage = "";
                if(upic != "")
                    bimage = BitMapToString(getBitmapFromURL(upic));
                user.setUserImage(bimage);
                if (mutableData.getValue() == null) {
                    mutableData.setValue(user);
                    return Transaction.success(mutableData);
                }
                return Transaction.abort();
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                if (b) {
                    Log.d("Check","user added");
                } else {
                    Log.d("Check","user exist");
                }
            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
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
    public void onFirebaseLoggedOut() {
        Toast.makeText(getApplicationContext(), LOGOUT_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
        setEnabledAuthProvider(AuthProviderType.GOOGLE);
        setEnabledAuthProvider(AuthProviderType.FACEBOOK);
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }

    // create a new user in Firebase
    public void createUser() {
        if(userNameET.getText() == null ||  !isEmailValid(userNameET.getText().toString())) {
            return;
        }
        firebaseRef.createUser(userNameET.getText().toString(), passwordET.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String user_id = String.valueOf(userNameET.getText().toString().hashCode());
                addUsertoDB(user_id,userNameET.getText().toString(),passwordET.getText().toString(),userNameET.getText().toString(),"");
                Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_SUCCESS, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_ERROR, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
}
