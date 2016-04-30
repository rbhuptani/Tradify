package com.hello_world.ronak.tradify;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sharm_000 on 4/27/2016.
 */
public class NotificationsLocal {
    static List<String> notification = new ArrayList<String>();
    //static List<String> notificationVisitor = new ArrayList<String>(Arrays.asList("Can't show other user's notifications."));
    static String createNotification(DataSnapshot ds) {
        //Iterator it = dataSnapShot.entrySet().iterator();
       /* String product = "";
        String user = "";
        *//*while (it.hasNext()) {
            Map.Entry ds = (Map.Entry) it.next();
            if (ds.getKey() == "InterestProduct")
                product = ds.getValue().toString();
            if (ds.getKey() == "InterestUser")
                user = ds.getValue().toString();
        }*//*
        for(DataSnapshot snapshot : ds) {
            if (snapshot.getKey() == "InterestProduct")
                product = snapshot.getValue().toString();
            if (snapshot.getKey() == "InterestUser")
                user = snapshot.getValue().toString();
        }*/
        return ds.getValue().toString();
    }
}
