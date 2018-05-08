package com.astix.gsk_dsr;

import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyBrowser extends WebViewClient 
{
	 private ProgressDialog progressBar;
	 
	 public MyBrowser(ProgressDialog progressBar)
	 {
		 this.progressBar=progressBar;
		
		 progressBar.show();
	 }

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			// TODO Auto-generated method stub
			  view.loadUrl(url);
		      return true;
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			
			
			super.onPageFinished(view, url);
			progressBar.dismiss();
			
			
		}
}
