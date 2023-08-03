package com.moko.bxp.sethala.helpers;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    public void Log(Context context, String tag , String activity, String description)
    {
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        Log.i(tag,"Device Log Created");
    }
}
