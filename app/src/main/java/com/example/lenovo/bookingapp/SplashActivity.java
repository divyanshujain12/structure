package com.example.lenovo.bookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.lenovo.bookingapp.Utils.ConnectionDetector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity
{
    public static  boolean gpscheck;
    LinearLayout l_backGround;
    int delayMillis;
    Boolean isInternetPresent = false;
    SharedPreferences preferences;
    ConnectionDetector cdr;
    String logflag;


    //file system ;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.elgroup.bookingapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        setContentView(R.layout.activity_splash);
        cdr = new ConnectionDetector(this);
        isInternetPresent = cdr.isConnectingToInternet();
        preferences = MyApplication.preference;
         logflag = preferences.getString("loggedIn", "");
        System.out.print("flag is " +logflag);

        if(isInternetPresent) {
            Thread timer= new Thread()
            {
                public void run()
                {
                    try
                    {
                        //Display for 3 seconds
                        sleep(3000);

                    }
                    catch (InterruptedException e)
                    {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    finally
                    {
                        //Goes to Activity  StartingPoint.java(STARTINGPOINT)

                        if(logflag.equals("true")){
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        }
                        else {
                            Intent i = new Intent(SplashActivity.this, StartActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }

                    }
                }
            };
            timer.start();

        }
        else
        {
            displayAlert();

        }


    }

    public  void displayAlert()
    {
        new AlertDialog.Builder(this).setMessage("Please Enable your Internet access")
                .setTitle("Booking Office")
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){
                                finish();
                                SplashActivity.this.finish();

                            }
                        })
                .show();
    }

}
