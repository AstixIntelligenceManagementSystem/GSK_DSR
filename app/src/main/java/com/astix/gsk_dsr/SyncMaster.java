package com.astix.gsk_dsr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.astix.gsk_dsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;





public class SyncMaster extends Activity 
{
	//ImageView progress_image;
	private String FilePathStrings;
	File fileintial;
	//Timer timer,timer2;
	String progressMsg;
	// MyTimerTask myTimerTask;
	// MyTimerTask2 mytimerTask2;
	 SyncPROdata task;
	 SyncImgTasker task1;
	 ProgressDialog pDialogGetStores;
	
	 private File[] listFile;
	public String[] xmlForWeb = new String[1];
	
	//ProgressDialog dialog = null;
	
	//public TextView chkString;
	HttpEntity resEntity;
	private SyncMaster _activity;
	
	//private static final String DATASUBDIRECTORY = "NMPphotos";
	
	
	public int syncFLAG = 0;
	public int res_code;
	public String zipFileName;
	//ProgressDialog PDpicTasker;
	public String whereTo;
	public int IMGsyOK = 0;
	ProgressDialog pDialog2;
	InputStream inputStream;
	
	 int serverResponseCode = 0;
	 
	 public String fnameIMG;
	 
	 public String UploadingImageName;
	
	
	 GskDBAdapter db = new GskDBAdapter(this);
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event)  // Control the PDA Native Button Handling
		{
			  // TODO Auto-generated method stub
			  if(keyCode==KeyEvent.KEYCODE_BACK)
			  {
			   return true;
			  }
			  if(keyCode==KeyEvent.KEYCODE_HOME)
			  {
				 // finish();
				  return true;
			  }
			  if(keyCode==KeyEvent.KEYCODE_MENU)
			  {
				  return true;
			  }
			  if(keyCode==KeyEvent.KEYCODE_SEARCH)
			  {
				  return true;
			  }

			  return super.onKeyDown(keyCode, event);
		}
	
	public void showSyncError() 
	{
		AlertDialog.Builder alertDialogSyncError = new AlertDialog.Builder(
				SyncMaster.this);
		alertDialogSyncError.setTitle("Sync Error!");
		alertDialogSyncError.setCancelable(false);
		/*alertDialogSyncError
				.setMessage("Sync was not successful! Please try again.");*/
		if(whereTo.contentEquals("11"))
		{
			alertDialogSyncError.setMessage(getText(R.string.syncAlertErrMsgDayEndOrChangeRoute));
		}
		else
		{
		alertDialogSyncError.setMessage(getText(R.string.syncAlertErrMsg));
		}
		alertDialogSyncError.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						
						dialog.dismiss();
						// finishing activity & stepping back
						
						Intent submitStoreIntent = new Intent(SyncMaster.this, MainActivity.class);
						startActivity(submitStoreIntent);
						finish();
						//SyncMaster.this.finish();
					}
				});
		alertDialogSyncError.setIcon(R.drawable.sync_error_ico);
		
		AlertDialog alert = alertDialogSyncError.create();
		alert.show();
		// alertDialogLowbatt.show();
	}
	public void showSyncErrorStart() {
		AlertDialog.Builder alertDialogSyncError = new AlertDialog.Builder(SyncMaster.this);
		alertDialogSyncError.setTitle("Sync Error!");
		alertDialogSyncError.setCancelable(false);
		//alertDialogSyncError.setMessage("Sync Error! \n\n Please check your Internet Connectivity & Try Again!");
		alertDialogSyncError.setMessage(getText(R.string.syncAlertErrMsg));
		alertDialogSyncError.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{

						
						dialog.dismiss();
						
						Intent submitStoreIntent = new Intent(SyncMaster.this, MainActivity.class);
						startActivity(submitStoreIntent);
						finish();
					}
				});
		alertDialogSyncError.setIcon(R.drawable.sync_error_ico);
		
		AlertDialog alert = alertDialogSyncError.create();
		alert.show();
		// alertDialogLowbatt.show();
	}
	public void showSyncSuccessStart() 
	{
		AlertDialog.Builder alertDialogSyncOK = new AlertDialog.Builder(SyncMaster.this);
		alertDialogSyncOK.setTitle("Information");
		alertDialogSyncOK.setCancelable(false);
		alertDialogSyncOK.setMessage(getText(R.string.syncAlertOKMsg));
		alertDialogSyncOK.setNeutralButton("OK",
				new DialogInterface.OnClickListener() 
		         {
					public void onClick(DialogInterface dialog, int which) 
					{
					
					dialog.dismiss();
					
					
					
					Intent submitStoreIntent = new Intent(SyncMaster.this, MainActivity.class);
					startActivity(submitStoreIntent);
					finish();		
					/*destroyNcleanup(1);
					imgs = null;
					uComments.clear();*/
					
				//	finish();
					
					
					}
				});
		alertDialogSyncOK.setIcon(R.drawable.info_ico);
		
		AlertDialog alert = alertDialogSyncOK.create();
		alert.show();
		
	}
	
	private class SyncImgTasker extends AsyncTask<Void, Void, Void>
{
		
	 String[] fp2s; // = "/mnt/sdcard/NMPphotos/1539_24-05-2013_1.jpg";
	 String[] NoOfOutletID;
	
	public SyncImgTasker(SyncMaster activity) 
	{
		pDialogGetStores = new ProgressDialog(activity);
		
	}
	
	@Override
	 protected void onPreExecute() 
	  {
		 super.onPreExecute();
          
		 
		    pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
		    pDialogGetStores.setMessage("Uploading Data...");
			pDialogGetStores.setIndeterminate(false);
			pDialogGetStores.setCancelable(false);
			pDialogGetStores.setCanceledOnTouchOutside(false);
			pDialogGetStores.show();
			
			
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				
			}
			else 
			{
				// Locate the image folder in your SD Card
				fileintial = new File(Environment.getExternalStorageDirectory()+ File.separator + "MRDisplayImages");
				// Create a new folder if no folder named SDImageTutorial exist
				fileintial.mkdirs();
			}
			

			if (fileintial.isDirectory())
			   {
				   listFile = fileintial.listFiles();
			   }
				
			        
					try 
					{
						db.open();
						NoOfOutletID = db.getStoreIDTblSelectedStoreIDinChangeRouteCase();
						db.close();
						
						
					} catch (Exception e) 
					{
						// TODO Auto-generated catch block
						db.close();
						e.printStackTrace();
					}
					
		
            
        }

		@Override
		protected Void doInBackground(Void... params)
		{
			
			
			  try
		        {
				  
				  System.out.println("Checking H1");
			for(int chkCountstore=0; chkCountstore < NoOfOutletID.length;chkCountstore++)
			{
				//db.open();
				 System.out.println("Checking H2");
				 System.out.println("Checking H2 value :"+NoOfOutletID[chkCountstore].toString().trim());
				int NoOfImages = db.getExistingPicNos(NoOfOutletID[chkCountstore].toString().trim());
				
				 System.out.println("Checking H2 one ");
				 System.out.println("Checking H2 one "+NoOfImages);
				 String[] NoOfImgsPath = db.getImgsPath(NoOfOutletID[chkCountstore].toString().trim());
				 System.out.println("Checking H2 two");
				 System.out.println("Checking H2 two "+NoOfImgsPath);
				//db.close();
				
				 System.out.println("Checking H3");
				
				fp2s = new String[2];
				
				for(int syCOUNT = 0; syCOUNT < NoOfImages; syCOUNT++)
				{
					fp2s[0] = NoOfImgsPath[syCOUNT];
					fp2s[1] = NoOfOutletID[chkCountstore];
					
					 System.out.println("Checking H4");
					
					fnameIMG = fp2s[0];
					UploadingImageName=fp2s[0];
					
					
					String stID = fp2s[1];
					String currentImageFileName=fnameIMG;
					
					 System.out.println("Checking H5");
					boolean isImageExist=false;
					for (int i = 0; i < listFile.length; i++)
					{
						FilePathStrings = listFile[i].getAbsolutePath();
						if(listFile[i].getName().equals(fnameIMG))
						{
							fnameIMG=listFile[i].getAbsolutePath();
							
							isImageExist=true;
							break;
						}
					}
					if(isImageExist)
					{
						 System.out.println("Checking H6");
						 Bitmap bmp = BitmapFactory.decodeFile(fnameIMG);
			             ByteArrayOutputStream stream = new ByteArrayOutputStream();
			             System.out.println("Checking H7");
			             
			             String image_str=  BitMapToString(bmp);
			             ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

			             System.out.println("Checking H8");
			        
			        try 
			        {
						stream.flush();
					}
			        catch (IOException e1)
			        {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        try
			        {
						stream.close();
					}
			        catch (IOException e1) 
			        {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    
			        
			        nameValuePairs.add(new BasicNameValuePair("image",image_str));
			    
					
					nameValuePairs.add(new BasicNameValuePair("FileName", currentImageFileName));
			   
			        nameValuePairs.add(new BasicNameValuePair("storeID", stID));
			    
			        try
			        {
			        	
			        	  HttpParams httpParams = new BasicHttpParams();
			           
			              HttpConnectionParams.setSoTimeout(httpParams, 0);
			        	
			        	
			            HttpClient httpclient = new DefaultHttpClient(httpParams);
			          
			           HttpPost httppost = new HttpPost(CommonInfo.ImageSyncPath.trim());
			           
			         
			            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			            HttpResponse response = httpclient.execute(httppost);
			            String the_string_response = convertResponseToString(response);
			            //Toast.makeText(MainActivity.this, "Response " + the_string_response, Toast.LENGTH_LONG).show();
			            System.out.println("Sunil Doing Testing Response after sending Image" + the_string_response);
			          
			          //  if(serverResponseCode == 200)
			            if(the_string_response.equals("Abhinav"))
			               {
			            	   
			            	 System.out.println("Sunil Doing Testing Response after sending Image inside if" + the_string_response);
			            	 db.updateImageRecordsSyncd(UploadingImageName.toString().trim());	
			            	/* runOnUiThread(new Runnable()
			                   {
			                        public void run() 
			                        {
			                        
			                        	IMGsyOK = 0;
			                        	//db.open();
			        		    		db.updateImageRecordsSyncd(UploadingImageName.toString().trim());		
			        		    		//db.close();
		                        		
			                        }
			                    });  */              
			               }    
			            
			        }catch(Exception e)
			        {
			              //Toast.makeText(MainActivity.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
			        	System.out.println("Anajli checking First time exception");
			        	IMGsyOK = 1;
			        
			        }
					}
				
				
				}
				
			
			}
		 }
			catch(Exception e)
	        {
	              //Toast.makeText(MainActivity.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
				System.out.println("Anajli checking Second time exception :"+e);
	        	IMGsyOK = 1;
	        
	        }
			
	
			
			return null;
		}

		@Override
		protected void onCancelled() {
			Log.i("SvcMgr", "Service Execution Cancelled");
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			 if(pDialogGetStores.isShowing()) 
		      {
		    	   pDialogGetStores.dismiss();
			  }
			 
			 if(IMGsyOK == 1)
				{
					IMGsyOK = 0;
					
					showSyncErrorStart();
				}
				else
				{
					db.open();
		    		//db.updateImageRecordsSyncd();		// update syncd' records
		    		db.updateRecordsSyncd();
		    		db.close();
					
					showSyncSuccess();
			
					//showSyncSuccessStart();
				
				}
	
		}
	}
	
	public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException
	{
		 
        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
        //System.out.println("contentLength : " + contentLength);
        //Toast.makeText(MainActivity.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
        if (contentLength < 0)
        {
        }
        else
        {
               byte[] data = new byte[512];
               int len = 0;
               try
               {
                   while (-1 != (len = inputStream.read(data)) )
                   {
                       buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                   }
               }
               catch (IOException e)
               {
                   e.printStackTrace();
               }
               try
               {
                   inputStream.close(); // closing the stream…..
               }
               catch (IOException e)
               {
                   e.printStackTrace();
               }
               res = buffer.toString();     // converting stringbuffer to string…..

               //System.out.println("Result : " + res);
               //Toast.makeText(MainActivity.this, "Result : " + res, Toast.LENGTH_LONG).show();
               ////System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
   }
	
	
	
	 public static boolean deleteFolderFiles(File path)
	 {

	/*  // Check if file is directory/folder
	  if(file.isDirectory())
	  {
	  // Get all files in the folder
	  File[] files=file.listFiles();

	   for(int i=0;i<files.length;i++)
	   {

	   // Delete each file in the folder
	 //  `	(files[i]);
		   file.delete();
	   }

	  // Delete the folder
	

	  }*/
		 
		 if( path.exists() ) 
		 {
	            File[] files = path.listFiles();
	            for(int i=0; i<files.length; i++)
	            {
	                if(files[i].isDirectory()) 
	                {
	                	deleteFolderFiles(files[i]);
	                }
	                else 
	                {
	                    files[i].delete();
	                }
	            }
	      }
	        return(path.delete());
	 
	 }
	public void showSyncSuccess() 
	{
		AlertDialog.Builder alertDialogSyncOK = new AlertDialog.Builder(SyncMaster.this);
		alertDialogSyncOK.setTitle("Information");
		alertDialogSyncOK.setCancelable(false);
		
		alertDialogSyncOK.setMessage(getText(R.string.syncAlertOKMsg));
		alertDialogSyncOK.setNeutralButton("OK",new DialogInterface.OnClickListener() 
		    {
					public void onClick(DialogInterface dialog, int which) 
					{

						
						dialog.dismiss();
						
						int flag=0;
						String[] imageToBeDeletedFromSdCard=db.deletFromSDcCardPhotoValidationBasedSstat("4");
			    		if(!imageToBeDeletedFromSdCard[0].equals("No Data"))
			    		{
				    			for(int i=0;i<imageToBeDeletedFromSdCard.length;i++)
				    		     {
				    				flag=1;
				    				
				    				String file_dj_path = Environment.getExternalStorageDirectory() + "/MRDisplayImages/"+imageToBeDeletedFromSdCard[i].toString().trim();
							        File fdelete = new File(file_dj_path);
							        if (fdelete.exists())
							        {
							            if (fdelete.delete())
							            {
							                Log.e("-->", "file Deleted :" + file_dj_path);
							                callBroadCast();
							            }
							            else 
							            {
							                Log.e("-->", "file not Deleted :" + file_dj_path);
							            }
							        }
				    			}
			    		}
						
						//db.deleteAllSubmitDataToServer(4);
						
						if(whereTo.contentEquals("11"))
						{
							
						}
						
						else
						{
							
							 SharedPreferences pref = getApplicationContext().getSharedPreferences("SCJohnson", MODE_PRIVATE); 
				                Editor editor = pref.edit();
				                editor.clear();
				                editor.commit();
							
						Intent submitStoreIntent = new Intent(SyncMaster.this, SplashScreen.class);
						//Intent submitStoreIntent = new Intent(SyncMaster.this, LoginActivity.class);
						startActivity(submitStoreIntent);
						finish();
						}
						//finish();
						//SyncMaster.this.finish();
					}
				});
		alertDialogSyncOK.setIcon(R.drawable.info_ico);
		
		AlertDialog alert = alertDialogSyncOK.create();
		alert.show();
		// alertDialogLowbatt.show();
	}
	
	 public void callBroadCast() 
	 {
	        if (Build.VERSION.SDK_INT >= 14)
	        {
	            Log.e("-->", " >= 14");
	            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener()
	            {
	              
	                public void onScanCompleted(String path, Uri uri)
	                {
	                
	                }
	            });
	        }
	        else 
	        {
	            Log.e("-->", " < 14");
	            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://" + Environment.getExternalStorageDirectory())));
	        }
	    }
	
	
	//
	public void delXML(String delPath)
	{
		//System.out.println("Deleting..: " + delPath);
		File file = new File(delPath);
	    file.delete();
	    File file1 = new File(delPath.toString().replace(".xml", ".zip"));
	     file1.delete();
	}
	//
	//
	public static void zip(String[] files, String zipFile) throws IOException
	{
	    BufferedInputStream origin = null;
	    final int BUFFER_SIZE = 2048;

	    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
	    try
	    { 
	        byte data[] = new byte[BUFFER_SIZE];

	        for (int i = 0; i < files.length; i++) 
	          {
	            FileInputStream fi = new FileInputStream(files[i]);    
	            origin = new BufferedInputStream(fi, BUFFER_SIZE);
	            try 
	              {
		                ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
		                out.putNextEntry(entry);
		                int count;
		                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) 
		                {
		                    out.write(data, 0, count);
		                }
	              }
	            finally 
	            {
	                origin.close();
	            }
	        }
	    }
	    finally 
	    {
	        out.close();
	    }
	}
	
	
	private class SyncPROdata extends AsyncTask<Void, Void, Void> 
	{

	
		
		public SyncPROdata(SyncMaster activity) 
		{
			pDialogGetStores = new ProgressDialog(activity);
		}
		
		@Override
		 protected void onPreExecute() 
		  {
			 super.onPreExecute();
	          
			 
			    pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
			   
			    pDialogGetStores.setMessage("Uploading Data...");
				pDialogGetStores.setIndeterminate(false);
				pDialogGetStores.setCancelable(false);
				pDialogGetStores.setCanceledOnTouchOutside(false);
				pDialogGetStores.show();
	            
	        }
		 
		 @Override
	        protected Void doInBackground(Void... params)
		    {
			 
			 // New Way for Sending XML File
			 
             String newzipfile = Environment.getExternalStorageDirectory() + "/GSKXML/" + zipFileName + ".zip";
			 
			 try 
			 {
				zip(xmlForWeb,newzipfile);
			 }
			 catch (Exception e1)
			 {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			 }
			 
			  HttpURLConnection conn = null;
	          DataOutputStream dos = null;  
	          String lineEnd = "\r\n";
	          String twoHyphens = "--";
	          String boundary = "*****";
	          int bytesRead, bytesAvailable, bufferSize;
	          byte[] buffer;
	          int maxBufferSize = 1 * 1024 * 1024; 
			 
			  File file2send = new File(newzipfile);
		       
		        // It is for Testing Purpose
			   //String urlString = "http://115.124.126.184/ReadXML_PragaSFAForTest/default.aspx?CLIENTFILENAME=" + zipFileName;
			  
			  // It is for Live Purpose
		     // String urlString = "http://115.124.126.184/ReadXML_PragaSFA/default.aspx?CLIENTFILENAME=" + zipFileName;
		        
			   
			   
			   String urlString = CommonInfo.OrderSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;
				  
			   
			   
		        try 
		        { 
		        	   
	            	 // open a URL connection to the Servlet
	               FileInputStream fileInputStream = new FileInputStream(file2send);
	               URL url = new URL(urlString);
	               
	               // Open a HTTP  connection to  the URL
	               conn = (HttpURLConnection) url.openConnection(); 
	               conn.setDoInput(true); // Allow Inputs
	               conn.setDoOutput(true); // Allow Outputs
	               conn.setUseCaches(false); // Don't use a Cached Copy
	               conn.setRequestMethod("POST");
	               conn.setRequestProperty("Connection", "Keep-Alive");
	               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	               conn.setRequestProperty("zipFileName", zipFileName); 
	               
	               dos = new DataOutputStream(conn.getOutputStream());
	     
	               dos.writeBytes(twoHyphens + boundary + lineEnd); 
	               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	            		                     + zipFileName + "\"" + lineEnd);
	               
	               dos.writeBytes(lineEnd);
	     
	               // create a buffer of  maximum size
	               bytesAvailable = fileInputStream.available(); 
	     
	               bufferSize = Math.min(bytesAvailable, maxBufferSize);
	               buffer = new byte[bufferSize];
	     
	               // read file and write it into form...
	               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                 
	               while (bytesRead > 0) 
	               {
	            	   
	                 dos.write(buffer, 0, bufferSize);
	                 bytesAvailable = fileInputStream.available();
	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	                 
	                }
	     
	               // send multipart form data necesssary after file data...
	               dos.writeBytes(lineEnd);
	               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	     
	               // Responses from the server (code and message)
	               serverResponseCode = conn.getResponseCode();
	               String serverResponseMessage = conn.getResponseMessage();
	                
	               Log.i("uploadFile", "HTTP Response is : " 
	            		   + serverResponseMessage + ": " + serverResponseCode);
	               
	               if(serverResponseCode == 200)
	               {
	            	   
	                   runOnUiThread(new Runnable()
	                   {
	                        public void run() 
	                        {
	                        	
	                        	syncFLAG = 1;
                        		//db.open();
                        		//db.updateRecordsSyncd();		// update syncd' records
                        		//db.updateRecordsSyncStoreProductReturn();
                        		//db.close();
                        		
                        		//delete recently synced xml (not zip)
                        		//db.deleteUnRequiredRecordsFromTablesAfterCase();
                        		delXML(xmlForWeb[0].toString());
                        		
	                        }
	                    });                
	               }    
	               
	               //close the streams //
	               fileInputStream.close();
	               dos.flush();
	               dos.close();
	                
	          } 
		        catch (MalformedURLException ex)
	             {
	        	  
	        	  pDialogGetStores.dismiss();  
	              ex.printStackTrace();
	             } 
		        catch (Exception e) 
	             {
	        	  
	        	  pDialogGetStores.dismiss();  
	              e.printStackTrace();
	              
	             }
		      
		        pDialogGetStores.dismiss(); 
		        
		      return null;
			 }

		 @Override
	        protected void onCancelled() 
		    {
	            Log.i("SyncMaster", "Sync Cancelled");
	        }
	        
		 @Override
	        protected void onPostExecute(Void result) 
		     {
			 super.onPostExecute(result);    
			 if(!isFinishing())
			    {
					
			        	 if(pDialogGetStores.isShowing()) 
					      {
					    	   pDialogGetStores.dismiss();
						  }
					
			        	
			        	if(syncFLAG == 0)
			        	{
			        		if(whereTo.contentEquals("11"))
								{
			            			
									showSyncError();
									
								}
			            		else
			            		{
			            			/*Intent submitStoreIntent = new Intent(SyncMaster.this, MainActivity.class);
									startActivity(submitStoreIntent);
									finish();*/
									
									showSyncErrorStart();
			            		}
			        		
			        	}
			        	else
			        	{
			        		
			        		
			        		try 
			        		{
			        			
			        			db.open();
					    		db.updateXMLRecordsSyncd();		// update syncd' records
					    		db.close();
			        			
			        			/*task1 = new SyncImgTasker(SyncMaster.this);
			        			task1.execute();*/
			        			
			        			
			        			showSyncSuccess();
			        			
			        		}
			        		catch (Exception e)
			        		{
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
			        	
			        		
				        		
			            		
		            	}
		            		
		            	
		            		
			        		
			        	}
			 }


	       

	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_master);
		
	  
		_activity = this;
		
		Intent syncIntent = getIntent();
		xmlForWeb[0] = syncIntent.getStringExtra("xmlPathForSync");
		zipFileName = syncIntent.getStringExtra("OrigZipFileName");
		whereTo = syncIntent.getStringExtra("whereTo");
		
		
		
		try 
		{
			
		     task = new SyncPROdata(SyncMaster.this);
			 task.execute();
			 
				
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	class MyTimerTask extends TimerTask {

	    @Override
	    public void run() {
	    
	     runOnUiThread(new Runnable(){

	    	
	      @Override
	      public void run() {
	    	  
	    	  if(task.getStatus()==AsyncTask.Status.RUNNING)
	    	  {
	    		//last...........
	    		  
	    		  task.cancel(true);
	    		  System.out.println("cancleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
	    		  pDialogGetStores.dismiss();
	    		
	    		  //  Intent submitStoreIntent = new Intent(SyncMaster.this, LauncherActivity.class);
				//	startActivity(submitStoreIntent);
				//	finish();
	    		  
	    		  
	    		//  pDialogGetStores.dismiss();
	    		//  progressMsg="Internet is slow,submitting again";
	    		// new SyncPROdata(SyncMaster.this).execute();
	    		  AlertDialog.Builder alertDialogSyncOKk = new AlertDialog.Builder(SyncMaster.this);
		    		 alertDialogSyncOKk.setTitle("Internet issue");
		    		 alertDialogSyncOKk.setCancelable(false);
		    		 alertDialogSyncOKk.setMessage(getText(R.string.syncAlertErrMsggg));
		    		 alertDialogSyncOKk.setNeutralButton("OK",
		    					new DialogInterface.OnClickListener() {
		    						public void onClick(DialogInterface dialog, int which) {
		    						
		    					dialog.dismiss();
		    					// progressMsg="Internet is slow,submitting again";
		    			    	//	new SyncPROdata(SyncMaster.this).execute();
		    					
	    						Intent submitStoreIntent = new Intent(SyncMaster.this, MainActivity.class);
	    						startActivity(submitStoreIntent);
	    						finish();
		    				
		    						
		    						}
		    				});
		    		 alertDialogSyncOKk.setIcon(R.drawable.error_info_ico);
		    			
		    			AlertDialog alert = alertDialogSyncOKk.create();
		    			alert.show();
	    	  }
	    
	      }});
	    }
	    
	   }
	class MyTimerTask2 extends TimerTask {

	    @Override
	    public void run() {
	    
	     runOnUiThread(new Runnable(){

	    	
	      @Override
	      public void run() {
	    	  
	    	  if(task.getStatus()==AsyncTask.Status.RUNNING)
	    		  
	    	  {
	    		  
	    		 task.cancel(true);
	    		 pDialogGetStores.dismiss();
	    		  //  Intent submitStoreIntent = new Intent(SyncMaster.this, LauncherActivity.class);
				//		startActivity(submitStoreIntent);
				//		finish();
	    		
	    		
	    		 
	    		 AlertDialog.Builder alertDialogSyncOKk = new AlertDialog.Builder(SyncMaster.this);
	    		 alertDialogSyncOKk.setTitle("Information");
	    		 alertDialogSyncOKk.setCancelable(false);
	    		 alertDialogSyncOKk.setMessage(getText(R.string.syncAlertErrMsggg));
	    		 alertDialogSyncOKk.setNeutralButton("OK",
	    					new DialogInterface.OnClickListener() {
	    						public void onClick(DialogInterface dialog, int which) {
	    						
	    						dialog.dismiss();
	    						
	    					//	db.open();
	    						//System.out.println("Indubati flgChangeRouteOrDayEnd :"+StoreSelection_Old.flgChangeRouteOrDayEnd);
	    						/*if(StoreSelection.flgChangeRouteOrDayEnd==1 || StoreSelection.flgChangeRouteOrDayEnd==2)
	    						{
	    							db.reTruncateRouteTbl();
	    						}*/
	    						
	    						
	    					//	db.reCreateDB();
	    					//	db.close();
	    						
	    						Intent submitStoreIntent = new Intent(SyncMaster.this, MainActivity.class);
	    						startActivity(submitStoreIntent);
	    						finish();		
	    						/*destroyNcleanup(1);
	    						imgs = null;
	    						uComments.clear();*/
	    						
	    					//	finish();
	    						
	    						
	    						}
	    					});
	    		alertDialogSyncOKk.setIcon(R.drawable.info_ico);
	    			
	    			AlertDialog alert = alertDialogSyncOKk.create();
	    			alert.show();
	    		 
	    		//  progressMsg="Internet is slow,submitting againsssssssssss";
	    		//  new SyncPROdata(SyncMaster.this).execute();
	    	  }
	    
	      }});
	    }
	    
	   }
	
	public String BitMapToString(Bitmap bitmap){
		int h1=bitmap.getHeight();
		int w1=bitmap.getWidth();
		h1=h1/8;
		w1=w1/8;
		bitmap=Bitmap.createScaledBitmap(bitmap, w1, h1, true);
		
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] arr=baos.toByteArray();
        String result=Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
  }
}

