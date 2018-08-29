package com.blogspot.chunkingz.eat_it2.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blogspot.chunkingz.eat_it2.Model.User;

public class Common {
    public static User currentUser;

    public static final String USER_KEY = "User";
    public static final String PASS_KEY = "Password";

    // for the mysql db
//    public static final String URL_REQUEST = "http://firebase.kropmann.com/request.php";

    // check if Device is connected to internet or not.
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null){

                for (NetworkInfo anInfo : info) {

                    if (anInfo.isConnectedOrConnecting()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
