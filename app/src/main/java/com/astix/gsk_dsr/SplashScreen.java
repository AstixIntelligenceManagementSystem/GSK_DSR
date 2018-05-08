package com.astix.gsk_dsr;

import java.io.File;
import java.lang.reflect.Method;

import com.astix.gsk_dsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

public class SplashScreen extends Activity {

	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "GSKELearning" ;
	 private static int SPLASH_TIME_OUT = 3000;
	
	 File dirORIGiOSPlayer;
	 public String imei="";
	 
	// CheckInstallAppBroadCastReciever broadCastReceiver = new CheckInstallAppBroadCastReciever();
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		if (Build.VERSION.SDK_INT >= 23) {

			if(checkingPermission()){
				onCreateFunctionality();
			}
			else{
				ActivityCompat.requestPermissions(SplashScreen.this,
						new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
			}}
		else{
			onCreateFunctionality();

		}
	}

	public void onCreateFunctionality()
	{
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			// Activity was brought to front and not created,
			// Thus finishing this will get us to the last viewed activity
			finish();
			return;
		}
		//BugSenseHandler.setup(this, "13ae41b3");

		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = tManager.getDeviceId();
		//imei="357327070587767";

		if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
		{
			imei = tManager.getDeviceId();
			CommonInfo.imei=imei;
		}
		else
		{
			imei=CommonInfo.imei.trim();
		}
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		goForward();

	}

	public  boolean checkingPermission(){
		if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) )
		{
			return false;
			//onCreateFunctionality();
		}
		else if((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
			return false;
		}
		else if((checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)){
			return false;
		}


		else{
			return true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if((grantResults[0]== PackageManager.PERMISSION_GRANTED) && (grantResults[1]== PackageManager.PERMISSION_GRANTED) && (grantResults[2]== PackageManager.PERMISSION_GRANTED) )
		{
			onCreateFunctionality();
		}
		else
		{
			finish();

		}
	}

	public void goForward() {
		boolean installed = appInstalledOrNot("air.com.articulate.articulatemobileplayer");
		if (installed)
		{
			new Handler().postDelayed(new Runnable()
			{
	 	            /*
	 	             * Showing splash screen with a timer. This will be useful when you
	 	             * want to show case your app logo / company
	 	             */

				@Override
				public void run()
				{
					if (sharedpreferences.contains("FirstRun")) {
						boolean isFirstRun = sharedpreferences.getBoolean("FirstRun", true);
						if (isFirstRun) {
							flyOut("displayLoginPage");
						} else {
							flyOut("displayStoreListPage");
						}
					} else {
						flyOut("displayLoginPage");
					}

				}
			}, SPLASH_TIME_OUT);
		} else {
			if (isOnline()) {
				alert("Alert", "Please Install Articulate Player to Proceed");
			} else {
				alert("Alert", "Please Connect to Internet to Proceed");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void flyOut(final String method_name)
	{
		/*if (true)
		{*/
			callMethod(method_name);
		//}
	}

	private void callMethod(String method_name) 
	{
		if (method_name.equals("finish"))
		{
			//overridePendingTransition(0, 0);
			finish();
		} 
		else 
		{
			try
			{
				Method method = getClass().getDeclaredMethod(method_name);
				method.invoke(this, new Object[] {});
			} 
			catch (Exception e) 
			{
				
			}
		}
	}

	public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) 
		{
			return true;
		}
		return false;
	}

	@Override
	protected void onStop() 
	{
		//overridePendingTransition(0, 0);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//this.unregisterReceiver(broadCastReceiver);
	}

	private void displayStoreListPage()
	{
		//Intent intent = new Intent(SplashScreen.this,HomeActivity.class);
	   Intent intent = new Intent(SplashScreen.this,AllButtonActivity.class);
		startActivity(intent);
		finish();
	}

	private void displayLoginPage()
	{
		Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
		intent.putExtra("View", "week");
		startActivity(intent);
		finish();
	}
	
	private boolean appInstalledOrNot(String uri)
	{
		PackageManager pm = getPackageManager();
		boolean app_installed;
		try
		{
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			app_installed = false;
		}
		return app_installed;
	}

	public void alert(String title,String message)
	{
		 AlertDialog alertDialog = new AlertDialog.Builder(SplashScreen.this).create();
		 alertDialog.setTitle(title);
		 alertDialog.setMessage(message);
 		 alertDialog.setIcon(R.drawable.error_ico);

 		 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which)
		 {
			 if(isOnline())
			 {
				 final String appPackageName ="air.com.articulate.articulatemobileplayer&hl=en";//getPackageName(); // getPackageName() from Context or Activity object
				 try
				 {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				 }
				 catch (android.content.ActivityNotFoundException anfe)
				 {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				 }
       		}
       		else {
				 finish();}
         }
 		});

 		alertDialog.show();
	 }
}
