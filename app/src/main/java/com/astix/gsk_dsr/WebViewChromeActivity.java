package com.astix.gsk_dsr;


import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import com.astix.gsk_dsr.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewChromeActivity extends Activity {

	String ImageUrl;
		
	
	//String CourseMainID,CourseCatID,LoginId,CourseLanID,WebURLLinkToOpen,UserId;
	
	 ImageView btn_bck;
	 GskDBAdapter dbengine= new GskDBAdapter(this);
	 
	 
	 ProgressDialog progressDialog;
	 
	 public WebView webView;
	 public int powerCheck=0;
	 public  PowerManager.WakeLock wl;
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			  // TODO Auto-generated method stub
			  if(keyCode==KeyEvent.KEYCODE_BACK){
			   return true;
			  }
			  if(keyCode==KeyEvent.KEYCODE_HOME){
			   return true;
			  }
			  if(keyCode==KeyEvent.KEYCODE_MENU){
				  return true;
			  }
			  if(keyCode==KeyEvent.KEYCODE_SEARCH){
				  return true;
			  }

			  return super.onKeyDown(keyCode, event);
		}
	 @Override
	  public void onDestroy() 
	  {
	   super.onDestroy();
	   wl.release();
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		try
		{
		
		 
		   if(powerCheck==0)
		   {
		        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		        wl.acquire();
		   }
		   
		  
		}
		catch(Exception e)
		{
			
		}
		   
		   
			 try 
				{
				 progressDialog = new ProgressDialog(WebViewChromeActivity.this);
			     progressDialog.setMessage("Please Wait..."); 
			     progressDialog.setCancelable(false);
			     
			   
			   //  ImageUrl=WebURLLinkToOpen;
				// ImageUrl=ImageUrl+"?CourseMainID="+CourseMainID+"&CourseCatID="+CourseCatID+"&LoginId="+LoginId+"&LangugageID="+CourseLanID;
				
				
				
							
				} 
			 catch (Exception e) 
			    {
					e.printStackTrace();
				}
		 btn_bck=(ImageView) findViewById(R.id.btn_bck);
		 btn_bck.setOnClickListener(new OnClickListener() 
		 {
			
			@Override
			public void onClick(View v) 
			{
				
				
				//Intent intent=new Intent(WebViewActivity.this,AssesmentModuleActivity.class);
				//intent.putExtra("ModuleId", CourseMainID);
				//startActivity(intent);
				//finish();
				   
				
				
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		
		boolean installed = appInstalledOrNot("com.android.chrome");  
	      if(installed) 
	        {
	    	  
	    	  
	    	   // webView=(WebView) findViewById(R.id.webView);
				webView.setWebViewClient(new MyBrowser(progressDialog));
				webView.getSettings().setLoadsImagesAutomatically(true);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				
				webView.loadUrl(ImageUrl);
				// String ua = "Mozilla/50.0.2 (X11; Linux i686) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/55.0.2883.75(Windows NT 10.0; Win64; x64) Safari/537.31";

				webView.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 Chrome/10.0.648.204");
				//String newUA = "Mozilla/5.0 (Android; Tablet; rv:20.0) Gecko/20.0 Firefox/20.0";
				//String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
				//webView.getSettings().setUserAgentString(newUA);
				//webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
			 
	    	 
	 		
	      }
	        else
	        {
	        	if(isOnline())
	        	{
	        		 alert("Alert", "Please Install Google Chrome to Proceed");
	        	}
	        	else
	        	{
	        		alert("Alert", "Please Connect to Internet to Proceed");
	        	}
	        
	        }
		
		
		
	
	}
	
	private boolean appInstalledOrNot(String uri)
	{
	    PackageManager pm = getPackageManager();
	    try {
	        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (PackageManager.NameNotFoundException e) {
	    }

	    return false;
	}
	
	 public void alert(String title,String message)
	 {
		 AlertDialog alertDialog = new AlertDialog.Builder(WebViewChromeActivity.this).create();
         alertDialog.setTitle(title);
         alertDialog.setMessage(message);
         alertDialog.setIcon(R.drawable.error_ico);
         alertDialog.setButton("OK", new DialogInterface.OnClickListener()
         {
        public void onClick(DialogInterface dialog, int which)
        {
        
       	 if(isOnline())
       	     {
       		 
       		//https://play.google.com/store/apps/details?id=com.android.chrome&hl=en
       		// org.mozilla.firefox
       		//https://play.google.com/store/apps/details?id=org.mozilla.firefox&hl=en
		        		 final String appPackageName ="com.android.chrome&hl=en";//getPackageName(); // getPackageName() from Context or Activity object
					try 
					{
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					} 
					catch (android.content.ActivityNotFoundException anfe)
					{
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
					}

            }
	      else
	       	 {
	       		 finish();
	       	 }
        }
});

// Showing Alert Message
alertDialog.show();
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
}
