package com.hello_world.ronak.tradify;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationsAdapter recyclerViewAdapter;
    LinearLayoutManager layoutManager;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.cardListNotif);
        //layoutManager = new LinearLayoutManager(getActivity());
        layoutManager = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //List<String> notifications = new ArrayList<String>(Arrays.asList("1","2"));
        if(NotificationsLocal.notification!=null && NotificationsLocal.notification.size()>0) {
            recyclerViewAdapter = new NotificationsAdapter(NotificationsLocal.notification);
        }else {
            NotificationsLocal.notification.clear();
            NotificationsLocal.notification.add("No data found. Refresh again.");
            recyclerViewAdapter = new NotificationsAdapter(NotificationsLocal.notification);
        }
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

