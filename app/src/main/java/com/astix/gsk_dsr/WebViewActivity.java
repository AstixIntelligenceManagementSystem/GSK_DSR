package com.astix.gsk_dsr;


import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import com.astix.gsk_dsr.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mozilla.gecko.GeckoView;

public class WebViewActivity extends Activity {

	String ImageUrl;
		
	
	String CourseMainID,CourseCatID,LoginId,CourseLanID,WebURLLinkToOpen,UserId;
	
	 ImageView btn_bck;
	 GskDBAdapter dbengine= new GskDBAdapter(this);
	 
	 
	 ProgressDialog progressDialog;
	 
	 public WebView webView;
	GeckoView gecko_view;
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
		
		  String UserNameandContact=dbengine.fnGetUserNameContact();
		   
		   String userName=UserNameandContact.split(Pattern.quote("^"))[0];
		   String ContactNo=UserNameandContact.split(Pattern.quote("^"))[1];
		   LoginId=UserNameandContact.split(Pattern.quote("^"))[2];
		   UserId=UserNameandContact.split(Pattern.quote("^"))[3];
		   
		   if(powerCheck==0)
		   {
		        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		        wl.acquire();
		   }
		   
		   Intent intentData=getIntent();
		   //CourseMainID,CourseCatID,LoginId,CourseLanID,WebURLLinkToOpen;
		   WebURLLinkToOpen=intentData.getStringExtra("WebURLToOpen");
		   CourseMainID=intentData.getStringExtra("ModuleID");
		   
		   CourseLanID=intentData.getStringExtra("LangugaeID");
		   
		   CourseCatID=intentData.getStringExtra("LPCourseCatID");
		}
		catch(Exception e)
		{
			
		}
		   
		   
			 try 
				{
				 progressDialog = new ProgressDialog(WebViewActivity.this);
			     progressDialog.setMessage("Please Wait..."); 
			     progressDialog.setCancelable(false);
			     
			    /* CourseMainID="32";
			     CourseCatID="43";*/
			     ImageUrl=WebURLLinkToOpen;//CommonInfo.WebPageUrl.trim();
				 ImageUrl=ImageUrl+"?CourseMainID="+CourseMainID+"&CourseCatID="+CourseCatID+"&LoginId="+LoginId+"&LangugageID="+CourseLanID;
				
					/*webView=(WebView) findViewById(R.id.webView);
					webView.setWebViewClient(new MyBrowser(progressDialog));
					webView.getSettings().setLoadsImagesAutomatically(true);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
					
					webView.loadUrl(ImageUrl);
					
					
					webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
				 */
				
							
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
				// TODO Auto-generated method stub
				// LoginId=UserNameandContact.split(Pattern.quote("^"))[2];
				 //  UserId=UserNameandContact.split(Pattern.quote("^"))[3];
				//CourseMainID
			/*	try
				{
					String Status=dbengine.fetchOnlineStatus(UserId,LoginId,CourseMainID);
					if(Status.equals("1") || Status.equals("2")  || Status.equals("3") || Status.equals("4"))
					{
							        dbengine.open();
									dbengine.savetblCheckOnlineModuleStatus(UserId,LoginId,CourseMainID,Status);
									dbengine.close();
							
					}
					else
					{
						dbengine.open();
						dbengine.savetblCheckOnlineModuleStatus(UserId,LoginId,CourseMainID,"0");
						dbengine.close();
					}
				}
				catch(Exception e)
				{
					
				}*/
				
				Intent intent=new Intent(WebViewActivity.this,AssesmentModuleActivity.class);
				intent.putExtra("ModuleId", CourseMainID);
				startActivity(intent);
				finish();
				   
				//finish();
				
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		
		boolean installed = appInstalledOrNot("org.mozilla.firefox");
	      if(installed)
	        {



				String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
	    	  webView=(WebView) findViewById(R.id.webView);
				//webView.setWebViewClient(new MyBrowser(progressDialog));
			//	webView.getSettings().setLoadsImagesAutomatically(true);
				webView.getSettings().setJavaScriptEnabled(true);
		/*		webView.getSettings().setUserAgentString(ua);

				webView.getSettings().setBuiltInZoomControls(true);
				webView.getSettings().setSupportZoom(true);
				webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setUseWideViewPort(true);

				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
				webView.getSettings().setPluginState(WebSettings.PluginState.ON);*/
			//	webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");


				webView.getSettings().setUserAgentString(ua);
				webView.getSettings().setUseWideViewPort(true);
				webView.getSettings().setLoadWithOverviewMode(true);




				webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
				webView.getSettings().setPluginState(WebSettings.PluginState.ON);
				webView.setWebViewClient(new WebViewClient(){
											 @Override
											 public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
												 Intent intent = new Intent(Intent.ACTION_MAIN, null);
												 intent.addCategory(Intent.CATEGORY_LAUNCHER);
												 intent.setComponent(new ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App"));
												 intent.setAction("org.mozilla.gecko.BOOKMARK");
												 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

												 intent.setData(Uri.parse(request.getUrl().toString()));
												 try {
													 // open Whatsapp listing in Play Store app
													 startActivity(intent);
												 } catch (ActivityNotFoundException ex) {
													 // open Whatsapp listing in browser
													 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
												 }
												 return true;
											 }

										 }



				);
				webView.setWebChromeClient(new WebChromeClient());

				webView.loadUrl(ImageUrl);
				//webView.loadUrl(ImageUrl);
				
				
				//

	    	 
	 		
	      }
	        else
	        {
	        	if(isOnline())
	        	{
	        		 alert("Alert", "Please Install Mozila Firefox to Proceed");
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
	private class MyWebViewClient extends WebViewClient {


		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(new ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App"));
			intent.setAction("org.mozilla.gecko.BOOKMARK");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			intent.setData(Uri.parse(request.getUrl().toString()));
			try {
				// open Whatsapp listing in Play Store app
				startActivity(intent);
			} catch (ActivityNotFoundException ex) {
				// open Whatsapp listing in browser
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
			}


			return true;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {


			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(new ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App"));
			intent.setAction("org.mozilla.gecko.BOOKMARK");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("args", "--url=" + url);
			intent.setData(Uri.parse(url));
			try {
				// open Whatsapp listing in Play Store app
				startActivity(intent);
			} catch (ActivityNotFoundException ex) {
				// open Whatsapp listing in browser
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
			}


			return true;
		}

	}
	 public void alert(String title,String message)
	 {
		 AlertDialog alertDialog = new AlertDialog.Builder(WebViewActivity.this).create();
         alertDialog.setTitle(title);
         alertDialog.setMessage(message);
         alertDialog.setIcon(R.drawable.error_ico);
         alertDialog.setButton("OK", new DialogInterface.OnClickListener()
         {
        public void onClick(DialogInterface dialog, int which)
        {
        
       	 if(isOnline())
       	     {
       		// org.mozilla.firefox
       		//https://play.google.com/store/apps/details?id=org.mozilla.firefox&hl=en
		        		 final String appPackageName ="org.mozilla.firefox&hl=en";//getPackageName(); // getPackageName() from Context or Activity object
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
