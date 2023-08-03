package com.moko.bxp.sethala.clients;

import android.content.Context;



public class KhiyaClient {
    private static final int Timeout = 100000;


    private static String getAbsoluteUrl() {
        return Configuration.KhiyaUrl + "/realms/" + Configuration.KhiyaRealm + "/protocol/openid-connect/token";
    }



    public static void CheckforInternetConnection(Context context){

    }


}
