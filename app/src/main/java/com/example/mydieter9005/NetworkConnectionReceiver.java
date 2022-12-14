package com.example.mydieter9005;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class NetworkConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(!isOnline(context))
                noInternetAccess(context);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void noInternetAccess(Context context){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(context);
        adb.setTitle("Internet connection not found!");
        adb.setMessage("Connect to the internet and try again.");
        adb.setIcon(R.drawable.ic_network_not_found);
        adb.setCancelable(false);

        adb.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!isOnline(context))
                    noInternetAccess(context);
                else
                    Toast.makeText(context, "Network connection available.", Toast.LENGTH_SHORT).show();
            }
        });

        ad = adb.create();
        ad.show();
    }
}