package com.forhope.sas;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.github.ybq.android.spinkit.SpinKitView;

import at.markushi.ui.CircleButton;


public class Tab1Fragment extends Fragment implements LocationListener {
    public static final String TAG = "Tab1Fragment";
    private FrameLayout frameLayout;
    private CircleButton circleButton;
    private CircleButton safeButton;
    private TextView textView;
    // public   static  boolean isSafe=true;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private SpinKitView redCircle;
    private SpinKitView greenCircle;
    private Vibrator vibe;
    LocationManager locationManager;
    String provider;
    static String googlemapslink;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        // final View viewList = inflater.inflate(R.layout.costume_list,container,false);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
        circleButton = (CircleButton) view.findViewById(R.id.circleButton);
        safeButton = (CircleButton) view.findViewById(R.id.safeButton);
        textView = (TextView) view.findViewById(R.id.textView);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        redCircle = (SpinKitView) view.findViewById(R.id.redCircle);
        greenCircle = (SpinKitView) view.findViewById(R.id.spin_kit);
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           return view;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.i("Location Info", "LOCATION ACHIEVED");
        } else {
            Log.i("Location Info", "NO LOCATION");
            googlemapslink = "Location Services is Off But the Sender is in Trouble";
        }

        if (SafetyMode.isSafe) {
            safeStatue(view);
            // SafetyMode.isSafe=true;
            Log.d("TAG", "SAFE");

        } else {
            unSaveStatue(view);
            SafetyMode.isSafe = false;
            //SafetyMode.unSafeMode(getContext());
            Log.d("TAG", "not SAFE");
        }
        view.findViewById(R.id.circleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyMode.isSafe = true;
                safetyStart();
                safeStatue(view);
                vibe.vibrate(200);


            }
        });

        view.findViewById(R.id.safeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Confirmation");
                dialog.setContentView(R.layout.dialog_confirmation);
                dialog.show();
                final TextView label = (TextView) dialog.findViewById(R.id.Labeltext);


                //CountDown

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        label.setText("Seconds remaining! : " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        if (dialog.isShowing()) {
                            label.setText("done!");
                            unSaveStatue(view);
                            SafetyMode.isSafe = false;
                            safetyStart();
                            vibe.vibrate(300);
                            dialog.cancel();
                        }
                    }
                }.start();


                TextView cancelButton = (TextView) dialog.findViewById(R.id.cancelButton);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //user cancels
                        dialog.cancel();
                    }
                });

                TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(getActivity(), "Button OK", Toast.LENGTH_LONG).show();
                        // SafetyMode.isSafe=false;
                        //SafetyMode.unSafeMode(getContext());
                        unSaveStatue(view);
                        SafetyMode.isSafe = false;
                        safetyStart();
                        vibe.vibrate(300);
                        dialog.cancel();

                        //code when the user accepts / change to unSave


                    }
                });


            }
        });
        return view;


    }

    private void unSaveStatue(View view) {

        frameLayout.setBackgroundColor(getResources().getColor(R.color.NotSafe));
        safeButton.setVisibility(View.GONE);
        circleButton.setVisibility(View.VISIBLE);
        textView.setBackgroundColor(getResources().getColor(R.color.BtnNotSafe));
        textView.setText("YOUR STATUE IS :UNSAFE!");
        getActivity().setTitle("WARNING: UNSAFE STATUE");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryN));
        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryN));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryN));
        tabLayout.setTabTextColors(Color.WHITE, getResources().getColor(R.color.colorAccentN));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccentN));
        greenCircle.setVisibility(View.GONE);
        redCircle.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkN));
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDarkN));

        }

    }

    private void safeStatue(View view) {
        frameLayout.setBackgroundColor(Color.parseColor("#9ad100"));
        safeButton.setVisibility(View.VISIBLE);
        circleButton.setVisibility(View.GONE);
        textView.setText("YOUR STATUE IS :SAFE!");
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(Color.WHITE, getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        greenCircle.setVisibility(View.VISIBLE);
        redCircle.setVisibility(View.GONE);

        getActivity().setTitle("SAFE");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        }
    }

    private void safetyStart() {
        if (!SafetyMode.isSafe) {
            Intent intent = new Intent(getContext(), SafetyMode.class);
            getActivity().startService(intent);

        } else {
            Intent intent = new Intent(getContext(), SafetyMode.class);
            getActivity().stopService(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Log.i("Latitude",Double.toString(lat));
        Log.i("Longitude",Double.toString(lon));
        googlemapslink = "www.google.dz/maps/search/"+lat+","+lon;
        Log.i("GOOGLE",googlemapslink);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
