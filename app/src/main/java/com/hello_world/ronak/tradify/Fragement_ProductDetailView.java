package com.hello_world.ronak.tradify;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ronak_000 on 4/18/2016.
 */
public class Fragement_ProductDetailView extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, YouTubePlayer.OnInitializedListener {
    static Firebase refNotif = new Firebase("https://tradify.firebaseio.com/Notifications");
    static Firebase refProducts = new Firebase("https://tradify.firebaseio.com/Products");
    private GoogleApiClient mGoogleApiClient;
    private int RECOVERY_DIALOG_REQUEST = 1;
    private SupportMapFragment mMapFragment;
    SmsManager sms;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location mLastLocation;
    public static String ARG_PRODUCT = "product_obj";
    public static String ARG_USER = "user_obj";
    public static Products product;
    public static Users user;
    YouTubePlayer mPlayer;
    String mVideoId;
    TextView txt_name, txt_details, txt_mode, txt_username, txt_loi,txt_price,txt_date,txt_cat;
    ImageView img_product;
    Button tradify;
    ImageButton left_ind,right_ind;
    String sender;
    String txtMessage;
    ViewFlipper viewFlipper;
    float lastX;
    FancyButton f_tradify;

    public static Fragement_ProductDetailView newInstance(Products product, Users user) {
        Fragement_ProductDetailView fragment = new Fragement_ProductDetailView();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragement_ProductDetailView() {

    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {
            buildGoogleApiClient();
        }
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_productdetailview, container, false);
        viewFlipper = (ViewFlipper)  rootView.findViewById(R.id.viewFlipper);
        txt_name = (TextView) rootView.findViewById(R.id.txt_dv_productName);
        txt_details = (TextView) rootView.findViewById(R.id.txt_dv_productDetails);
        txt_mode = (TextView) rootView.findViewById(R.id.txt_dv_transactionMode);
        txt_username = (TextView) rootView.findViewById(R.id.txt_dv_userName);
        txt_loi = (TextView) rootView.findViewById(R.id.txt_dv_LOI);
        img_product = (ImageView) rootView.findViewById(R.id.img_dv_product);
        //tradify = (Button) rootView.findViewById(R.id.btn_dv_tradify);
        f_tradify = (FancyButton) rootView.findViewById(R.id.btn_dv_tradify);
        txt_price = (TextView) rootView.findViewById(R.id.txt_dv_productPrice);
        txt_date = (TextView) rootView.findViewById(R.id.txt_dv_posted_date);
        txt_cat= (TextView) rootView.findViewById(R.id.txt_dv_cat);

        product = (Products) getArguments().getSerializable(ARG_PRODUCT);
        user = (Users) getArguments().getSerializable(ARG_USER);
        txt_name.setText(product.getProductName());
        txt_details.setText(product.getDescription());
        txt_mode.setText(product.getMode());
        txt_price.setText(String.valueOf(product.getPrice()));
        txt_cat.setText(product.getCategory());
        img_product.setTransitionName(product.getProductId());
        if (user.getUsername() == "") {
            //rootView.findViewById(R.id.lbl_dv_userName).setVisibility(View.GONE);
            txt_username.setVisibility(View.GONE);
        } else {
            //rootView.findViewById(R.id.lbl_dv_userName).setVisibility(View.VISIBLE);
            txt_username.setVisibility(View.VISIBLE);
            txt_username.setText(user.getUsername());
        }
        txt_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //someEventListener.someEvent((String) txt_username.getText());
                try {
                    Intent userProfileIntent = new Intent(getActivity(), UserProfileActivity.class);
                    userProfileIntent.putExtra("username", user.getUserId());
                    getActivity().startActivity(userProfileIntent);
                } catch (Exception ex) {
                    Log.d("Failure: ", ex.getMessage());
                }
            }
        });
        if(UserContext.USERID.equals(user.getUserId())){
            //tradify.setText("Mark as Sold");
            f_tradify.setText("Mark as Sold");
        }
        if(product!=null && product.getSold()!=null && product.getSold()){
            //tradify.setText("Sold");
            //tradify.setEnabled(false);
            f_tradify.setText("Sold");
            f_tradify.setBackgroundColor(getResources().getColor(R.color.divider));
            f_tradify.setEnabled(false);
        }
        //tradify.
        f_tradify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserContext.USERID.equals(user.getUserId())) {
                    refProducts.child(product.getProductId()).child("Sold").setValue(true);
                    f_tradify.setText("Sold");
                    f_tradify.setEnabled(false);
                } else {
                    sender = user.getContactNumber();//"+12017555242";
                    txtMessage = "Interested in your product.";
                    sendSMS(sender, txtMessage);
                    String notifMsg = UserContext.USERNAME + " is interested in product " + product.getProductName();
                    refNotif.child(user.getUserId()).child(product.getProductId()).setValue(notifMsg);
                }
                //refNotif.child(user.getUserId()).child("InterestProduct").setValue(product.getPrductName());
            }
        });
        long date = product.getPostedDate();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String postedDate = format.format(date);
        txt_date.setText(postedDate);

        txt_loi.setText(product.getListOfItems());
        img_product.setImageBitmap(StringToBitMap(product.getProductImage()));
        String Lat_Long = product.getLocation();
        String[] Lat_LongA = Lat_Long.split(",");
        double _lat = 0;
        double _long = 0;
        try{
            _lat = Double.parseDouble(Lat_LongA[0]);
            _long = Double.parseDouble(Lat_LongA[1]);
        }
        catch(Exception ex){

        }
        mLastLocation = new Location("0,0");
        mLastLocation.setLatitude(_lat);
        mLastLocation.setLongitude(_long);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googlemap);
        if(mMapFragment!=null){
            mMapFragment.getMapAsync(this);
        }
        mVideoId = product.getVideoId();
        YouTubePlayerSupportFragment playerFragment =
                (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.moviePlayer);
        playerFragment.initialize(getString(R.string.google_maps_key), this);
        left_ind = (ImageButton) rootView.findViewById(R.id.view_left);
        right_ind = (ImageButton) rootView.findViewById(R.id.view_right);

        left_ind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewFlipper.getDisplayedChild() == 1){


                    viewFlipper.setInAnimation(viewFlipper.getContext(), R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(viewFlipper.getContext(), R.anim.slide_out_to_right);
                    // Display previous screen.
                    viewFlipper.showPrevious();
                }

            }
        });

        right_ind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewFlipper.getDisplayedChild() == 0) {

                    viewFlipper.setInAnimation(viewFlipper.getContext(), R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(viewFlipper.getContext(), R.anim.slide_out_to_left);

                    // Display next screen.
                    viewFlipper.showNext();

                }
            }
        });


        return rootView;
    }



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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //sms.sendTextMessage(sender, null, txtMessage, null, null);
                Log.d("sms sent", "sent");
                Toast.makeText(getContext(),"Notified owner!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Grant Permission!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMS(String phoneNumber, String message)
    {
        //PendingIntent pi = PendingIntent.getActivity(this, 0,
        //      new Intent(this, SMS.class), 0);
        sms = SmsManager.getDefault();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                        1);
            } else {
                //sms.sendTextMessage(phoneNumber, null, message, null, null);
                Log.d("sms sent", "sent");
                Toast.makeText(getContext(),"Notified owner!",Toast.LENGTH_SHORT).show();
            }
        } else {
            //sms.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d("sms sent", "sent");
            Toast.makeText(getContext(),"Notified owner!",Toast.LENGTH_SHORT).show();
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = createLocationRequest();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    LatLng latLng_Prev = null;

    @Override
    public void onLocationChanged(Location location) {
        //move camera when location changed
        Log.d("Latitude",String.valueOf(mLastLocation.getLatitude()));
        Log.d("Longitude",String.valueOf(mLastLocation.getLongitude()));
        LatLng latLng_Now = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng_Now)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        LatLng ll = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        final Marker marker = mMap.addMarker(new MarkerOptions()
                .position(ll).visible(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
        );

        if (latLng_Prev == null) {
            latLng_Prev = latLng_Now;
        }
        //draw line between two locations:
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(latLng_Prev, latLng_Now)
                .width(5)
                .color(Color.RED));
        latLng_Prev = latLng_Now;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("onConnectionFailed ", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng lat) {
                Toast.makeText(getContext(), "Latitude: " + lat.latitude + "\nLongitude: " + lat.longitude, Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng lat) {
                final Marker marker = mMap.addMarker(new MarkerOptions()
                        .title("self defined marker")
                        .snippet("Hello!")
                        .position(lat).visible(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                );
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker){
                //Toast.makeText(getContext(), marker.getTitle().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer=youTubePlayer;
        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (mVideoId != null) {
            if (b) {
                mPlayer.play();
            } else {
                mPlayer.loadVideo(mVideoId);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(getActivity(), "onInitializationFailure", Toast.LENGTH_LONG).show();
        }
    }
}

