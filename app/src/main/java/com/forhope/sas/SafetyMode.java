package com.forhope.sas;


import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class SafetyMode extends Service {
    public static boolean isSafe = true;
    private static int smsPeriod;
    LocationManager locationManager;
    String provider;

    public static void unSafeMode(Context context,String maps) {
        //  Toast.makeText(context,"SafetyCheck",Toast.LENGTH_LONG).show();
        if (!isSafe) {
            SharedPreferences sharedPref = context.getSharedPreferences("userPref", Context.MODE_APPEND);
            String costumeSMS = sharedPref.getString("costumeSMS", "");
            smsPeriod = sharedPref.getInt("timePeriod", 0);
            MySQLiteHelper db = new MySQLiteHelper(context);
            try {
                for (int i = 0; i < db.getAllContacts().size(); i++) {
                    if (costumeSMS.equals("")) {
                        costumeSMS = "Im in trouble i need help here is my location!";
                    }
                    SmsManager.getDefault().sendTextMessage(db.getAllContacts().get(i).getContactNumber(), null,
                            costumeSMS + " " + maps, null, null);
                }
                Alarm(context);


            } catch (Exception e) {
                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(context);
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.setMessage(e.getMessage());
                dialog.show();
            }
        }
    }

    private static void Alarm(Context context) {
        Date when = new Date(System.currentTimeMillis());

        try {
            Intent someIntent = new Intent(context, MyReceiver.class); // intent to be launched
            // note this could be getActivity if you want to launch an activity
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0, // id, optional
                    someIntent, // intent to launch
                    PendingIntent.FLAG_CANCEL_CURRENT); // PendintIntent flag

            AlarmManager alarms = (AlarmManager) context.getSystemService(
                    Context.ALARM_SERVICE);

            alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                    when.getTime(),
                    1000 * 60 * 60 * smsPeriod,
                    pendingIntent);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        isSafe = true;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
              //public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                         int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!= null) {
            Log.i("Location Info","LOCATION ACHIEVED");
        } else {
            Log.i("Location Info","NO LOCATION");
        }
   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_LONG).show();
        isSafe=false;
        Alarm(getApplicationContext());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(SafetyMode.this,"Service Stopped",Toast.LENGTH_LONG).show();
        isSafe = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
