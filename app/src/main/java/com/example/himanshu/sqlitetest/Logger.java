package com.example.himanshu.sqlitetest;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by himanshu on 02/04/16.
 */
public class Logger {

    public static void message(Context context,String data){
        Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
    }
}
