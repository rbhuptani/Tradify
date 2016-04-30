package com.hello_world.ronak.tradify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by ronak_000 on 4/20/2016.
 */
public class Fragment_Filter  extends Fragment{
    static Firebase filter_ref = new Firebase("https://tradify.firebaseio.com/Products");
    final int DAY1 = 1;
    final int DAY7 = 2;
    final int DAY30 = 3;

    Button btn_reset,btn_apply;
    RadioButton chk_cat_ele,chk_cat_cars,chk_cat_sports,chk_cat_home,chk_cat_movies,chk_cat_fashion,chk_cat_baby,chk_cat_others;
    RadioButton chk_post_24hr,chk_post_7days,chk_post_30days;
    RadioButton chk_dist_nearby,chk_dist_area,chk_dist_city;
    EditText et_price_min,et_price_max;
    RadioButton chk_sort_dist,chk_sort_posted;
    RadioButton rb_sort_price_min,rb_sort_price_max;
    OnFilterAppliedListener filterListner;
    public interface OnFilterAppliedListener{
        public void applyFilter(Query ref);
    }
    public Fragment_Filter() {
    }
    public static Fragment_Filter newInstance() {
        Fragment_Filter fragment = new Fragment_Filter();
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
        View rootView = inflater.inflate(R.layout.fragment_filter,container,false);
        filterListner = (OnFilterAppliedListener) getActivity();
        btn_reset = (Button) rootView.findViewById(R.id.btn_reset);
        btn_apply = (Button) rootView.findViewById(R.id.btn_apply);

        chk_cat_ele = (RadioButton) rootView.findViewById(R.id.chk_ele);
        chk_cat_cars = (RadioButton) rootView.findViewById(R.id.chk_cars);
        chk_cat_sports = (RadioButton) rootView.findViewById(R.id.chk_sports);
        chk_cat_home = (RadioButton) rootView.findViewById(R.id.chk_home);
        chk_cat_movies = (RadioButton) rootView.findViewById(R.id.chk_movies);
        chk_cat_fashion = (RadioButton) rootView.findViewById(R.id.chk_fashion);
        chk_cat_baby = (RadioButton) rootView.findViewById(R.id.chk_baby);
        chk_cat_others = (RadioButton) rootView.findViewById(R.id.chk_others);


        chk_post_24hr = (RadioButton) rootView.findViewById(R.id.chk_24hr);
        chk_post_7days = (RadioButton) rootView.findViewById(R.id.chk_7days);
        chk_post_30days = (RadioButton) rootView.findViewById(R.id.chk_30days);

        et_price_min = (EditText) rootView.findViewById(R.id.edit_prc_min);
        et_price_max = (EditText) rootView.findViewById(R.id.edit_prc_max);

        chk_sort_posted= (RadioButton) rootView.findViewById(R.id.chk_sort_posted);

        rb_sort_price_min = (RadioButton) rootView.findViewById(R.id.rb_sort_pricelh);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query queryRef = filter_ref;
                if(Activity_HomeScreen.FILTER_PRICE)
                    queryRef = filtebyPrice();
                else if(Activity_HomeScreen.FILTER_TIME)
                    queryRef = filterbyDate(getPostedCode());
                else if(Activity_HomeScreen.FILTER_CAT)
                    queryRef = filterByCategory();
                else if (Activity_HomeScreen.SORT_PRICE)
                    queryRef = sortByPrice();
                else if (Activity_HomeScreen.SORT_TIME)
                    queryRef = sortByTime();
                filterListner.applyFilter(queryRef);
            }
        });
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        return rootView;
    }

    public Query sortByPrice(){
        Query queryRef;
        double fromPrice = Double.MAX_VALUE;
        double toPrice = 0;
        queryRef = filter_ref.orderByChild("Price").startAt(toPrice).endAt(fromPrice);
        return queryRef;

    }

    public Query sortByTime(){
        Query queryRef;
        Date currDate = new Date();
        long currTime = currDate.getTime();
        queryRef = filter_ref.orderByChild("PostedDate").startAt(0).endAt(currTime);
        return  queryRef;
    }

    public Query filterByCategory(){
        Query queryRef;
        String cat = "";
        if(chk_cat_ele.isChecked())
            cat = "Electronics";
        if(chk_cat_cars.isChecked())
            cat = "Cars and Motors";
        if(chk_cat_sports.isChecked())
            cat = "Sports and Games";
        if(chk_cat_home.isChecked())
            cat = "Electronics";
        if(chk_cat_movies.isChecked())
            cat = "Movies, Music and Books";
        if(chk_cat_fashion.isChecked())
            cat = "Fashion and Accessories";
        if(chk_cat_baby.isChecked())
            cat = "Baby and Child";
        if(chk_cat_others.isChecked())
            cat = "Others";
        Log.d("Category :",cat);
        queryRef = filter_ref.orderByChild("Category").equalTo(cat);
        return  queryRef;
    }

    public int getPostedCode(){
        if(chk_post_30days.isChecked())
            return DAY30;
        if(chk_post_7days.isChecked())
            return DAY7;
        if(chk_post_24hr.isChecked())
            return DAY1;
        return 0;

    }
    public Query filterbyDate(int code){
        Log.d("Return Code :",String.valueOf(code));
        Query queryRef;
        boolean flag = false;
        Calendar cal = Calendar.getInstance();
        int days=0;
        switch (code){
            case DAY1:
                days = -1;
                break;
            case DAY7:
                days = -7;
                break;
            case DAY30:
                days = -30;
                break;
            default:
                flag = true;
                break;
        }
        Log.d("Days : ",String.valueOf(days));
        cal.add(Calendar.DATE, days);
        Date currDate = new Date();
        long currTime = currDate.getTime();
        long yTime =  cal.getTime().getTime();
        Log.d("Start time:",String.valueOf(yTime));
        Log.d("End time:",String.valueOf(currTime));
        queryRef = filter_ref.orderByChild("PostedDate").startAt(yTime).endAt(currTime);
        return  queryRef;
    }

    public Query filtebyPrice(){
        Query queryRef;
        double fromPrice = Double.MAX_VALUE;
        double toPrice = 0;
        try
        {
            if(et_price_min.getText().toString() != "")
                toPrice = Double.parseDouble(et_price_min.getText().toString());
        }
        catch(NumberFormatException e){}
        try
        {
            if(et_price_max.getText().toString() != "")
                fromPrice = Double.parseDouble(et_price_max.getText().toString());
        }
        catch(NumberFormatException e){}
        queryRef = filter_ref.orderByChild("Price").startAt(toPrice).endAt(fromPrice);
        return queryRef;
    }

    public void reset(){
        chk_cat_ele.setChecked(false);
        chk_cat_cars.setChecked(false);
        chk_cat_sports.setChecked(false);
        chk_cat_home.setChecked(false);
        chk_cat_movies.setChecked(false);
        chk_cat_fashion.setChecked(false);
        chk_cat_baby.setChecked(false);
        chk_cat_others.setChecked(false);
        chk_post_24hr.setChecked(false);
        chk_post_7days.setChecked(false);
        chk_post_30days.setChecked(false);
        chk_dist_nearby.setChecked(false);
        chk_dist_area.setChecked(false);
        chk_dist_city.setChecked(false);
        chk_sort_dist.setChecked(false);
        chk_sort_posted.setChecked(false);
        rb_sort_price_min.setChecked(false);
        rb_sort_price_max.setChecked(false);

        et_price_min.setText("");
        et_price_max.setText("");
    }
}
