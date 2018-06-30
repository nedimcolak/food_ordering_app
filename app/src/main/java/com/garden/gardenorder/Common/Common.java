package com.garden.gardenorder.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.garden.gardenorder.Model.User;
import com.garden.gardenorder.Remote.APIService;
import com.garden.gardenorder.Remote.RetrofitClient;

/**
 * Created by Nedim on 10/27/2017.
 */

public class Common {
    public static User currentUser;

    private static final String BASE_URL = "https://fcm.googleapis.com";
    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Poslana narudzba";
        else if (status.equals("1"))
            return "Narudzba prihvaćena";
        else return "Isporučeno";
    }

    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i<info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }

        } return false;

    }

    public static final String DELETE = "Izbriši";
    public static final String PHONE_KEY = "Phone";
}
