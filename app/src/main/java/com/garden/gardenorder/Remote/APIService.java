package com.garden.gardenorder.Remote;


import com.garden.gardenorder.Model.MyResponse;
import com.garden.gardenorder.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by Faggot on 11/26/2017.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAIdOQoq8:APA91bHL72UNi6BQJ2ZU_Wcj3pQDVct_bV5cEGMBktkqkGAD-ZHB7D5-DX6E-_YESoHcCLlir_0j4ipTi5basY2NqnkN-HZGwXb1vNLqBGlI6sMDP-S9pyTCGxG__YUg-37ZBEKgsSne"
            }
    )
    @POST("fcm/send")

    Call<MyResponse> sendNotification(@Body Sender body);
}
