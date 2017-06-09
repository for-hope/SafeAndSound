package com.forhope.sas;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class SafetyMode extends Service {
    public  static  boolean isSafe;
    private static String costumeSMS;
    private static int smsPeriod;
   static MySQLiteHelper db;

    public static void unSafeMode(Context context) {
        Toast.makeText(context,"SafetyCheck",Toast.LENGTH_LONG).show();
        if(!isSafe) {
            SharedPreferences sharedPref = context.getSharedPreferences("userPref",Context.MODE_APPEND);
        costumeSMS = sharedPref.getString("costumeSMS","");
        smsPeriod = sharedPref.getInt("timePeriod",0);
       db = new MySQLiteHelper(context);
        try {
          for(int i=0;i<db.getAllContacts().size();i++) {
             if (costumeSMS.equals("")) {
                 costumeSMS = "Im in trouble i need help here is my location!";
             }
              SmsManager.getDefault().sendTextMessage(db.getAllContacts().get(i).getContactNumber(), null,
                      costumeSMS, null, null);
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

        try{
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
                    1000*60*60*smsPeriod,
                    pendingIntent);


        }catch(Exception e){
            e.printStackTrace();
        }


    }

   @Override
   public void onCreate() {
       super.onCreate();
       isSafe = false;
       unSafeMode(getApplicationContext());

   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(SafetyMode.this,"Service Started",Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(SafetyMode.this,"Service Stopped",Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
