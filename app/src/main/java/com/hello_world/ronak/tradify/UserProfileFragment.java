package com.hello_world.ronak.tradify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;




public class UserProfileFragment extends Fragment {
    String userId="";
    final Firebase ref = new Firebase("https://tradify.firebaseio.com/Users");
    TextView name, address, emailId, contactNum;
    ImageView profilePic;
    ViewPager myViewPager;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    View view;



    public UserProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        name = (TextView) view.findViewById(R.id.userName);
        address = (TextView) view.findViewById(R.id.address);
        emailId = (TextView) view.findViewById(R.id.emailId);
        contactNum = (TextView) view.findViewById(R.id.contactNumber);
        profilePic = (ImageView) view.findViewById(R.id.profilePic);

        userId= getArguments().getString("username");


       ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        name.setText((String) users.child("Username").getValue());
                        address.setText((String) users.child("Address").getValue());
                        emailId.setText((String) users.child("Email").getValue());
                        contactNum.setText(users.child("ContactNumber").getValue().toString());
                        if(users.child("UserImage").getValue()!=null) {
                            byte[] imageAsBytes = Base64.decode(users.child("UserImage").getValue().toString().getBytes(), 0);
                            Bitmap bp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                            profilePic.setImageBitmap(bp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //getActivity().setContentView(R.layout.fragment_user_profile);
        myViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),3, userId);
        myViewPager.setAdapter(myFragmentPagerAdapter);
        myViewPager.setPageTransformer(true, new AccordionTransformer() );
        myViewPager.setCurrentItem(3);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(myViewPager);
        return view;
    }


}
