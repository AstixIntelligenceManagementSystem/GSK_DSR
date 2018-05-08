package com.astix.gsk_dsr;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.astix.gsk_dsr.R;





import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity
{
	
	public int syncFLAG = 0;
	
	 public String[] xmlForWeb = new String[1];
	 int serverResponseCode = 0;
	 public ProgressDialog pDialogGetStores;
	 
	private File[] listFile;
	private ProgressDialog pDialog;
	public static final int progress_bar_icon = 0; 
	public static final int progress_bar_content = 1; 
	
	String onlyDate;
	//public static int flgTSIApproved=0;
	public static int flgUserAlreadyExists=0;
	public static int flgTSIApproved=0;
	
	public static String msg="";
	public static final String DATASUBDIRECTORY = "GSKModulesSmallImage";
	public static final String DATAMODULECONTENT = "GSKModulesContent";
	
	 public static final String DATA_ARTICULATE = "iOSPlayer";
public String ErrorName="0";
	boolean isErrorAlert=false;
	public static int PersonNodeID=0;
	public static int PersonNodeType=16;
	ArrayList<String> arrayImageName=new ArrayList<String>();
	int countImgeDownloaded =0;
	int countContentDownloaded =0;
	//ViewFlipper view_flipper;
	public String imei="";
	Button btn_login,btn_reset;
	ServiceWorker newservice;
	ArrayList<String> arraysmallIconToBDwnld=new ArrayList<String>();
	ArrayList<String> arrayContentDownload=new ArrayList<String>();
	LinkedHashMap<String, String> hmapSmallIconImageName = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> hmapContentModule = new LinkedHashMap<String, String>();
	SharedPreferences.Editor editor;
	EditText txt_userName,txt_Password;
	File dirORIGimg,dirModuleContent,dirArticuate;
	public ProgressDialog pDialogVersionCheck;
	Context context;
	 public static final String MyPREFERENCES = "GSKLogin" ;
	SharedPreferences sharedpreferences;
	String fileuri;
	public int chkFlgForErrorToCloseApp=0;
	GskDBAdapter dbengine = new GskDBAdapter(this); 
	
	String VisitStartTS;
	
	public int checkClick=0;

	
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
	
	public void showSettingsAlert()
	{
		  AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		     
		        // Setting Dialog Title
		        alertDialog.setTitle("Information");
		        alertDialog.setIcon(R.drawable.error_info_ico);
		        alertDialog.setCancelable(false);
		        // Setting Dialog Message
		        alertDialog.setMessage("GPS is not enabled. \nPlease select all settings on the next page!");
		 
		        // On pressing Settings button
		        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		             Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		             startActivity(intent);
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
		 }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	 {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_login_activty);
		   
		    
		    pDialogVersionCheck = new ProgressDialog(LoginActivity.this);
		    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		    editor = sharedpreferences.edit();
		    newservice = new ServiceWorker();
		    context=this;
		    
		    if(!sharedpreferences.contains("GSkUserName"))
		    {
		    	deleteDirectory(new File(Environment.getExternalStorageDirectory(),DATASUBDIRECTORY));
		    	deleteDirectory(new File(Environment.getExternalStorageDirectory(),DATAMODULECONTENT));
		    	deleteDirectory(new File(Environment.getExternalStorageDirectory(),DATA_ARTICULATE));
		    }
		    
		    dirORIGimg = new File(Environment.getExternalStorageDirectory(),DATASUBDIRECTORY);
		    dirModuleContent = new File(Environment.getExternalStorageDirectory(),DATAMODULECONTENT);
		    dirArticuate = new File(Environment.getExternalStorageDirectory(),DATA_ARTICULATE);
		    if (!dirArticuate.exists()) 
			{
		    	dirArticuate.mkdirs();
			}
		
		    if (!dirModuleContent.exists()) 
			{
		    	dirModuleContent.mkdirs();
			}
		    
			if (!dirORIGimg.exists()) 
			{
				dirORIGimg.mkdirs();
			}
			
			
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
			
			//imei="351971070001539";
			//Link for dual sim
			//http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim#
			
			final TextView textView_forgotpassword=(TextView) findViewById(R.id.textView_forgotpassword);
			
			SpannableString content = new SpannableString("GET PASSWORD");
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			textView_forgotpassword.setText(content);
			
			textView_forgotpassword.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					textView_forgotpassword.animate();
					checkClick=1;

					if(TextUtils.isEmpty(txt_userName.getText().toString().trim()))
					{
						txt_userName.requestFocus();
						txt_userName.setText("");
						txt_userName.setFocusable(true);
						btn_login.setEnabled(true);
						txt_userName.requestFocus();
					
						String estring = "Please enter the user name to proceed";
						ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
						SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
						ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
						txt_userName.setError(ssbuilder);
					}
					
					else if(sharedpreferences.contains("GSkUserName"))
					{
						if(sharedpreferences.contains("GSkUserName"))
						{
							String userName=sharedpreferences.getString("GSkUserName", "");
							String password=sharedpreferences.getString("GSkPassword", "");
							if(userName.equals(txt_userName.getText().toString()))
							{
								alert("Username : "+userName, "Hi Your Password is : "+password);
							}
							else
							{
								alert("Alert", "Invalid Username");
							}
						}
					}
					else
					{
					 	if(isOnline())
					   {
						   GetForgotPasswordCheck task = new GetForgotPasswordCheck(LoginActivity.this);
						   task.execute();
					   }
					   else
					   {
						   showNoConnAlert();
					   }
					}
				}
			});
			
			txt_userName=(EditText) findViewById(R.id.txt_userName);
			txt_userName.requestFocus(); 
			//txt_userName.requestFocusFromTouch();
			
			txt_userName.setText("DSR2147F");

			txt_userName.setOnFocusChangeListener(new OnFocusChangeListener() 
			{
				@Override
				public void onFocusChange(View v, boolean hasFocus)
				{

					if(txt_userName.hasFocus())
					{
						if(TextUtils.isEmpty(txt_userName.getText().toString().trim()))
						{
							txt_userName.setHint("");
						}
					}
					else
					{
						if(TextUtils.isEmpty(txt_userName.getText().toString().trim()))
						{
							txt_userName.setHint("User Name");
						}
					}
				 
				}
			});
			
			txt_Password=(EditText) findViewById(R.id.txt_password);
			txt_Password.setText("BAEF99");
			
			txt_Password.setOnFocusChangeListener(new OnFocusChangeListener() 
			{
				@Override
				public void onFocusChange(View v, boolean hasFocus)
				{
					if(txt_Password.hasFocus())
					{
						if(TextUtils.isEmpty(txt_Password.getText().toString().trim()))
						{
							txt_Password.setHint("");
						}
					}
					else
					{
						if(TextUtils.isEmpty(txt_Password.getText().toString().trim()))
						{
							txt_Password.setHint("Password");
						}
					}
				}
			});
			
			txt_Password.setOnEditorActionListener(new TextView.OnEditorActionListener() 
			{
			    @Override
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			    {
			        if (actionId == EditorInfo.IME_ACTION_DONE) 
			        {
						checkClick=1;
						if(TextUtils.isEmpty(txt_userName.getText().toString().trim()))
						{
							txt_userName.requestFocus();
							txt_userName.setText("");
							txt_userName.setFocusable(true);
							btn_login.setEnabled(true);
							txt_userName.requestFocus();
						
							String estring = "Please enter the user name to proceed";
							ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
							SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
							ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
							txt_userName.setError(ssbuilder);
						}
						else
						{}
			        }
			        return false;
			    }
			});

			btn_login=(Button) findViewById(R.id.btn_login);
			btn_login.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					checkClick=1;
					if(TextUtils.isEmpty(txt_userName.getText().toString().trim()))
					{
						txt_userName.requestFocus();
						txt_userName.setText("");
						txt_userName.setFocusable(true);
						btn_login.setEnabled(true);
						txt_userName.requestFocus();
					
						String estring = "Please enter the user name to proceed";
						ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
						SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
						ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
						txt_userName.setError(ssbuilder);
					}
					else if(TextUtils.isEmpty(txt_Password.getText().toString().trim()))
					{
						txt_Password.requestFocus();
						txt_Password.setText("");
						txt_Password.setFocusable(true);
						btn_login.setEnabled(true);
						txt_Password.requestFocus();
					
						String estring = "Please enter the password to proceed";
						ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
						SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
						ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
						txt_Password.setError(ssbuilder);
					}
					else 
					{
						if(isOnline())
						{
							if(!sharedpreferences.contains("GSkUserName"))
							{
								dbengine.open();
								LinkedHashMap<String, String> hmapLocalSet=new LinkedHashMap<>();
								hmapLocalSet.put("localsettings.zip", "0");
								dbengine.savetblModuleContent("id123",hmapLocalSet,"0");
								dbengine.close();
								String localsettingPath=CommonInfo.LocalSettingPath.trim();
								arrayContentDownload.add("localsettings.zip^"+localsettingPath+"/LocalSetting/localsettings.zip^0");
								
							}
							new Authenticate().execute();	
						}
						else
						{
							if(sharedpreferences.contains("GSkUserName"))
							{
								String userName=sharedpreferences.getString("GSkUserName", "");
								String password=sharedpreferences.getString("GSkPassword", "");
								if(userName.equals(txt_userName.getText().toString()) && password.equals(txt_Password.getText().toString()))
								{
									Intent intent=new Intent(LoginActivity.this,AllButtonActivity.class);
									startActivity(intent);
									finish();
								}
								else
								{
									alert("Alert", "Invalid Username or Password!");
								}
							}
							else
							{
								alert("Alert", "Please Connect to Internet");
							}
						}
					}
				}
			});
			
			btn_reset=(Button) findViewById(R.id.btn_reset);
			btn_reset.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					txt_userName.setText("");
					txt_userName.setHint("User Name");
					txt_Password.setText("");
					txt_Password.setHint("Password");
					
					/*Intent intent=new Intent(LoginActivity.this,WebViewActivity.class);
					startActivity(intent);*/
					
				}
			});
	}
	
	
	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	
	private class Authenticate extends AsyncTask<Void,Void,Void>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			if(pDialogVersionCheck == null)
			{
				pDialogVersionCheck=new ProgressDialog(LoginActivity.this);
			}
			pDialogVersionCheck.setTitle(getText(R.string.genTermPleaseWaitNew));
			pDialogVersionCheck.setMessage("Authenticating...");
			pDialogVersionCheck.setIndeterminate(false);
			pDialogVersionCheck.setCancelable(false);
			pDialogVersionCheck.setCanceledOnTouchOutside(false);
			pDialogVersionCheck.show();
		}

		@Override
		protected Void doInBackground(Void... args) 
		{
			try
			{
				for(int i=0;i<4;i++)
				{
					if(i==1)
					{
						 // Device model
					    String PhoneModel="NA";
					    // Android version
					    String AndroidVersion ="NA"; 
						try
						{
							PhoneModel = android.os.Build.MODEL;
							AndroidVersion = android.os.Build.VERSION.RELEASE;
						}
						catch(Exception e)
						{}
						newservice = newservice.getAuthenticate(getApplicationContext(),imei,txt_userName.getText().toString().trim(),txt_Password.getText().toString().trim(),PhoneModel,AndroidVersion);
						 if(!newservice.director.toString().trim().equals("1"))
						   {
								if(chkFlgForErrorToCloseApp==0)
								{
									chkFlgForErrorToCloseApp=1;
								}
						   }
					}
					int chkLatestVersion=dbengine.FetchVersionDownloadStatus();
					if(chkLatestVersion==1)
					{
						
					}
					else
					{
						
						//pDialogVersionCheck.setMessage("Fetching Data");
						//publishProgress("Fetching Data");
					if(i==2)
					{
						if(CommonInfo.userId!=0)
						{
							 newservice = newservice.fetchUserModuleQuestionAndAnswer(getApplicationContext(),imei,CommonInfo.userId);
							 if(!newservice.director.toString().trim().equals("1"))
					           {	
									if(chkFlgForErrorToCloseApp==0)
									{
										chkFlgForErrorToCloseApp=1;
			                        }
							   } 
					    }
					}
					if(i==3)
				     {
						if(CommonInfo.userId!=0)
						{
				      		newservice = newservice.fnCallAllQuestionWebservice(getApplicationContext());
				       		if(!newservice.director.toString().trim().equals("1"))
					   		{
								if(chkFlgForErrorToCloseApp==0)
								{
							 		chkFlgForErrorToCloseApp=1;
								}
							}
						}
				     }
					}
				}
				
			}
			catch (Exception e) 
		    {
				 newservice.director="100";
				//showNoConnAlert();
				Log.i("SvcMgr", "Service Execution Failed!", e);
            }

			finally
			 {
                Log.i("SvcMgr", "Service Execution Completed...");
             }
			
			return null;
		}
		
		private void publishProgress(String string) {

		}
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			if(pDialogVersionCheck.isShowing()) 
	        {
	           pDialogVersionCheck.dismiss();
	           btn_login.setEnabled(true);
	        }
			
			
			if(chkFlgForErrorToCloseApp==1)   // if Webservice showing exception or not excute complete properly
			{
				
					btn_login.setEnabled(true); 
					chkFlgForErrorToCloseApp=0;
				
					 if(newservice.director.toString().trim().equals("100"))
					{
						showNoConnAlert();
					}
					else if(newservice.director.toString().trim().equals("200"))
					{
						showAlertForEveryOne("Error while Retrieving Data!\n Please try again");
					}
					else if(newservice.director.toString().trim().equals("10"))
					{
						//showAlertForEveryOne("This phone is not registered, please contact administrator");
						// Text Message change by Avinash Sir on 16 feb 2017
						showAlertForEveryOne("Device not Registered, please contact administrator");
					}
					else if(newservice.director.toString().trim().equals("0"))
					{
						showAlertForEveryOne("Incorrect User Name or Password");
					}
					else
					{
						showAlertForEveryOne("Error while Retrieving Data!\n Please try again");
					}
			}
			else
			{
				
				int chkLatestVersion=dbengine.FetchVersionDownloadStatus();
				if(chkLatestVersion==1)
				{
					File GSKXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
					
					 if (!GSKXMLFolder.exists()) 
						{
						 GSKXMLFolder.mkdirs();
						} 
			    	 
			    	 File del = new File(Environment.getExternalStorageDirectory(), "GSKXML");
			    	
			    	// check number of files in folder
			    	 String [] AllFilesNameNotSync= checkNumberOfFiles(del);
			    	 if(AllFilesNameNotSync.length>0)
			    	 {
			    		 if(isOnline())
							{
								 // Changes By Sunil 
								   AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(LoginActivity.this);
									alertDialogSubmitConfirm.setTitle("Information");
									alertDialogSubmitConfirm.setMessage(getText(R.string.submitConfirmAlertPendingData));
									alertDialogSubmitConfirm.setCancelable(false);
									
									alertDialogSubmitConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which)
												{
													
														
														 try
														    {
															 

															 FullSyncDataNow task = new FullSyncDataNow(LoginActivity.this);
															 task.execute();
																// TODO Auto-generated method stub
																
																
															
														    }
															catch (Exception e)
															{
																// TODO Autouuid-generated catch block
																e.printStackTrace();
														    }
														
												}
											});
									
									alertDialogSubmitConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									});
									
									alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
									
									AlertDialog alert = alertDialogSubmitConfirm.create();
									
									alert.show();
									

								    
							}
							 else
							 {
								showNoConnAlert();
							 }
							 
			    	 }
			    	 else
			    	 {
					showNewVersionAvailableAlert();
			    	 }
				}
				else
				{
				editor.clear();
				editor.commit();
			
				editor.putString("GSkUserName", txt_userName.getText().toString().trim());
				editor.putString("GSkPassword", txt_Password.getText().toString().trim());
				
				editor.commit();
				new GetModuleData().execute();
				}
			}
		
		}
	}
	
	public  int upLoad2Server(String sourceFileUri,String fileUri) 
	{
		 
		fileUri=fileUri.replace(".xml", "");
		
		     String fileName = fileUri;
		     String zipFileName=fileUri;
		     
		     String newzipfile = Environment.getExternalStorageDirectory() + "/GSKXML/" + fileName + ".zip";
				///storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.zip
		     
		     sourceFileUri=newzipfile;
		    	
		     xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/GSKXML/" + fileName + ".xml";
		     //[/storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.xml]
			 	
			   try 
				 {
					zip(xmlForWeb,newzipfile);
				 }
				 catch (Exception e1) 
				 {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//java.io.FileNotFoundException: /359648069495987.2.21.04.2016.12.44.02: open failed: EROFS (Read-only file system)
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
			        
			        String urlString = CommonInfo.OrderSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;
			        
			        try { 
			        	   
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
		                
		               Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
		               
		               if(serverResponseCode == 200)
		               {
		            	   syncFLAG = 1;
		            	   
		            	   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
		            	   Editor editor = pref.edit();
		            	  // editor.remove(xmlForWeb[0]);
		            	   editor.putString(fileUri, ""+4); 
		            	   editor.commit();
		            	   
		            	   String FileSyncFlag=pref.getString(fileUri, ""+1); 
		            	   
						   		delXML(xmlForWeb[0].toString());
						   		/*dbengine.open();
					            dbengine.deleteXMLFileRow(fileUri);
					            dbengine.close();*/
					           
					   } 
		               else
		               {
		            	   syncFLAG = 0;  
		               }
		               
		               //close the streams //
		               fileInputStream.close();
		               dos.flush();
		               dos.close();
		                
		          } catch (MalformedURLException ex) 
		          {
		        	  ex.printStackTrace();
		          } catch (Exception e)
		          {
		        	 e.printStackTrace();
		           }
						
			   
		
		 
			  return serverResponseCode;  

			 }
	
	
	 private class FullSyncDataNow extends AsyncTask<Void, Void, Void> 
		{
			

			
			public FullSyncDataNow(LoginActivity activity) 
			{
				pDialogGetStores = new ProgressDialog(activity);
			}
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				
				 File GSKXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
					
				 if (!GSKXMLFolder.exists()) 
					{
					 GSKXMLFolder.mkdirs();
					} 
		    	 
				
				pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
				pDialogGetStores.setMessage("Submitting Pending Data...");
				pDialogGetStores.setIndeterminate(false);
				pDialogGetStores.setCancelable(false);
				pDialogGetStores.setCanceledOnTouchOutside(false);
				pDialogGetStores.show();
			   
			  
			}

			@Override
			
			protected Void doInBackground(Void... params) 
			{

				
				try 
				{
					
				
					
			    	 File del = new File(Environment.getExternalStorageDirectory(), "GSKXML");
			    	
			    	// check number of files in folder
			    	 String [] AllFilesName= checkNumberOfFiles(del);
			    	 
			    	 
			    	 if(AllFilesName.length>0)
			    	 {
			    		 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
			    		
			    		 
			    		 for(int vdo=0;vdo<AllFilesName.length;vdo++)
							{
								String fileUri=  AllFilesName[vdo];
								

								System.out.println("Sunil Again each file Name :" +fileUri);
								
								if(fileUri.contains(".zip"))
								{
									File file = new File(fileUri);
					    		    file.delete();
								}
								else
								{
								String f1=Environment.getExternalStorageDirectory().getPath()+"/GSKXML/"+fileUri;
								System.out.println("Sunil Again each file full path"+f1);
								try
								{
								upLoad2Server(f1,fileUri);
								}
								catch (Exception e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
							}
			    		
			    	 }
			    	 else
			    	 {
			    		 
			    	 }
					
					
					
					
					
					

				} catch (Exception e)
				{
				
					e.printStackTrace();
					if(pDialogGetStores.isShowing()) 
				      {
				    	   pDialogGetStores.dismiss();
					  }
				}
				return null;
			}

			@Override
			protected void onCancelled() {
				
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(pDialogGetStores.isShowing()) 
			      {
			    	   pDialogGetStores.dismiss();
				  }
				
				if(syncFLAG == 0)
	        	{

					 // Changes By Sunil 
					   AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(LoginActivity.this);
						alertDialogSubmitConfirm.setTitle("Information");
						alertDialogSubmitConfirm.setMessage("Error while Uploading Data!\n Please try again");
						alertDialogSubmitConfirm.setCancelable(false);
						
						alertDialogSubmitConfirm.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										dialog.dismiss();
									}
								});
						
						
						alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
						
						AlertDialog alert = alertDialogSubmitConfirm.create();
						
						alert.show();
						

					    
				
	        	}
				else
				{
					showNewVersionAvailableAlert();
				}
				
				
			}
		}
	
	public void delXML(String delPath)
	{
		File file = new File(delPath);
	    file.delete();
	    File file1 = new File(delPath.toString().replace(".xml", ".zip"));
	    file1.delete();
	}

	
	public static void zip(String[] files, String zipFile) throws IOException
	{
	    BufferedInputStream origin = null;
	    final int BUFFER_SIZE = 2048;

	    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
	    try { 
	        byte data[] = new byte[BUFFER_SIZE];

	        for (int i = 0; i < files.length; i++) {
	            FileInputStream fi = new FileInputStream(files[i]);    
	            origin = new BufferedInputStream(fi, BUFFER_SIZE);
	            try {
	                ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
	                out.putNextEntry(entry);
	                int count;
	                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
	                    out.write(data, 0, count);
	                }
	            }
	            finally {
	                origin.close();
	            }
	        }
	    }
	    
	    finally {
	        out.close();
	    }
	}
	
	
	 public static String[] checkNumberOfFiles(File dir) 
	  {
		 int NoOfFiles=0;
		 String [] Totalfiles = null;
		
	        if (dir.isDirectory()) 
	        {
	            String[] children = dir.list();
	            NoOfFiles=children.length;
	            Totalfiles=new String[children.length];
	            
	            for (int i=0; i<children.length; i++) 
	            {
	            	Totalfiles[i]=children[i];
	            }
	        }
	        return Totalfiles;
	    }
	
	public String genOutID()
	{
		
		long syncTIMESTAMP = System.currentTimeMillis();
		Date dateobj = new Date(syncTIMESTAMP);
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		VisitStartTS = df.format(dateobj);
		
		        //store ID generation
		        String cxz;
				cxz = UUID.randomUUID().toString();
				StringTokenizer tokens = new StringTokenizer(String.valueOf(cxz), "-");
				
				String val1 = tokens.nextToken().trim();
				String val2 = tokens.nextToken().trim();
				String val3 = tokens.nextToken().trim();
				String val4 = tokens.nextToken().trim();
				cxz = tokens.nextToken().trim();
				
				String IMEIid = imei.substring(9);
				cxz = IMEIid +"-"+cxz+"-"+VisitStartTS.replace(" ", "").replace(":", "").trim();
				
				
				return cxz;
				
	}
	
	public void showNewVersionAvailableAlert()
    {
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
		AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(LoginActivity.this);
		alertDialogNoConn.setTitle(R.string.genTermInformation);
		alertDialogNoConn.setCancelable(false);
		alertDialogNoConn.setMessage(getText(R.string.newVersionAlertOnLauncher));
		alertDialogNoConn.setNeutralButton(R.string.txtOk,new DialogInterface.OnClickListener()
		        {
					public void onClick(DialogInterface dialog, int which)
					 {
						GetUpdateInfo task = new GetUpdateInfo(LoginActivity.this);
						task.execute();
						dialog.dismiss();
					 }
				});
	
		alertDialogNoConn.setIcon(R.drawable.info_ico);
		AlertDialog alert = alertDialogNoConn.create();
		alert.show();
	
}
	
	private class GetUpdateInfo extends AsyncTask<Void, Void, Void> 
	{
		
		public GetUpdateInfo(LoginActivity activity) 
		{
		
		}
		
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			pDialogVersionCheck.setTitle(getText(R.string.genTermPleaseWaitNew));
			pDialogVersionCheck.setMessage(getText(R.string.genTermDownloadData));
			pDialogVersionCheck.setIndeterminate(false);
			pDialogVersionCheck.setCancelable(false);
			pDialogVersionCheck.setCanceledOnTouchOutside(false);
			pDialogVersionCheck.show();
			
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			
	        try 
	 	      {
	        	downloadapk();
		      }
	 	   catch(Exception e) 
	 	      {}

	 	   finally 
	 	      {}
	  
			return null;
		}

		
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			if(pDialogVersionCheck.isShowing()) 
		      {
				pDialogVersionCheck.dismiss();
			  }
			sharedpreferences.edit().clear().commit();
			
			try {
				/*deleteDirectory(new File(Environment.getExternalStorageDirectory(),DATASUBDIRECTORY));
				deleteDirectory(new File(Environment.getExternalStorageDirectory(),DATAMODULECONTENT));
				deleteDirectory(new File(Environment.getExternalStorageDirectory(),DATA_ARTICULATE));*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			installApk();
		}
	}
	
private void downloadapk()
	{
	    try {
	    	
	      URL url = new URL(CommonInfo.VersionDownloadPath.trim()+CommonInfo.VersionDownloadAPKName);
	        URLConnection connection = url.openConnection();
	        HttpURLConnection urlConnection = (HttpURLConnection) connection;
	       urlConnection.setRequestMethod("GET");
	        urlConnection.setDoInput(true);
	         urlConnection.connect();
	       	 File sdcard = Environment.getExternalStorageDirectory();
	 	       String PATH = Environment.getExternalStorageDirectory() + "/download/";
	 	       File file2 = new File(PATH+CommonInfo.VersionDownloadAPKName);
	 	      if(file2.exists())
	          {
	 	    	 file2.delete();
	          }
	 	       
	             File file1 = new File(PATH);
	             file1.mkdirs();
	             File file = new File(file1, CommonInfo.VersionDownloadAPKName);
	            int size = connection.getContentLength();
	            FileOutputStream fileOutput = new FileOutputStream(file);
	 	        InputStream inputStream = urlConnection.getInputStream();
	 	        byte[] buffer = new byte[10240];
	 	        int bufferLength = 0;
	 	       int current = 0;
	 	        while ( (bufferLength = inputStream.read(buffer)) != -1 ) {
	 	            fileOutput.write(buffer, 0, bufferLength);
	 	        }
	 	        fileOutput.close();
	 	       
	    } catch (MalformedURLException e) 
	    {
	          //  e.printStackTrace();
	         //   //System.out.println("sunil downloadapk failed ");
	    } catch (IOException e) {
	         //   e.printStackTrace();
	         //   //System.out.println("sunil downloadapk failed ");
	            
	    }
	    }

	 private void installApk()
	 {
		    this.deleteDatabase(CommonInfo.DATABASE_NAME);
		    Intent intent = new Intent(Intent.ACTION_VIEW);
	       // Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "ParagIndirect.apk"));
	        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + CommonInfo.VersionDownloadAPKName));
	       
	        intent.setDataAndType(uri, "application/vnd.android.package-archive");
	        startActivity(intent);
	        finish();
	        
	        
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
	public void showNoConnAlert() 
	 {
			AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(LoginActivity.this);
			alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
			alertDialogNoConn.setMessage(R.string.genTermNoDataConnectionFullMsg);
			alertDialogNoConn.setNeutralButton(R.string.txtOk,
					new DialogInterface.OnClickListener() 
			          {
							public void onClick(DialogInterface dialog, int which) 
							{
		                        dialog.dismiss();
		                       // finish();
		                    }
					  });
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();
			
		}
	 public void onLoginSuccess() 
	 {
		 btn_login.setEnabled(true);
	        finish();
	    }

	    public void onLoginFailed() {
	       Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

	        btn_login.setEnabled(true);
	    }
	    public boolean validate() {
	        boolean valid = true;

	        return valid;
	    }
	public void login() {
       // Log.d(TAG, "Login");
        
       
    }

	    public void showAlertForEveryOne(String msg) 
	    {
	     AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(LoginActivity.this);
	     alertDialogNoConn.setTitle("Information");
	     alertDialogNoConn.setMessage(msg);
	     alertDialogNoConn.setCancelable(false);
	     alertDialogNoConn.setNeutralButton(R.string.txtOk,new DialogInterface.OnClickListener() 
	          {
	               public void onClick(DialogInterface dialog, int which) 
	                   {
	                          dialog.dismiss();
	                          //finish();
	                   }
	          });
	     alertDialogNoConn.setIcon(R.drawable.info_ico);
	     AlertDialog alert = alertDialogNoConn.create();
	     alert.show();
	   }
	    
	   
		private class GetModuleData extends AsyncTask<Void,Void,Void>
		{
			
			
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				pDialogVersionCheck.setTitle(getText(R.string.genTermPleaseWaitNew));
				pDialogVersionCheck.setMessage("Fetching Data");
				pDialogVersionCheck.setIndeterminate(false);
				pDialogVersionCheck.setCancelable(false);
				pDialogVersionCheck.setCanceledOnTouchOutside(false);
				pDialogVersionCheck.show();
			}
			@Override
			protected Void doInBackground(Void... args) 
			{
				
				
				try
				{
					
					String userID=dbengine.fnfetchUserIdFromtblUserLoginMstr();
					newservice = newservice.fetchModuleData(getApplicationContext(),imei,Integer.parseInt(userID));
					 if(!newservice.director.toString().trim().equals("1"))
			           {	
							if(chkFlgForErrorToCloseApp==0)
							{
								chkFlgForErrorToCloseApp=1;
	                      }
						
					  } 
				}
				catch (Exception e) 
			    {
					 newservice.director="100";
					//showNoConnAlert();
				
	            }

				finally
				 {
	               
	             }
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);
				
				if(pDialogVersionCheck.isShowing()) 
		        {
		           pDialogVersionCheck.dismiss();
		           btn_login.setEnabled(true);
		        }
				
				
				if(chkFlgForErrorToCloseApp==1)   // if Webservice showing exception or not excute complete properly
				{
					
						btn_login.setEnabled(true); 
						chkFlgForErrorToCloseApp=0;
					
						 if(newservice.director.toString().trim().equals("100"))
						{
							showNoConnAlert();
						}
						else if(newservice.director.toString().trim().equals("200"))
						{
							showAlertForEveryOne("Error while Retrieving Data!\n Please try again");
						}
						else if(newservice.director.toString().trim().equals("0"))
						{
							showAlertForEveryOne("Incorrect User Name or Password");
						}
						else
						{
							
							showAlertForEveryOne("Error while Retrieving Data!\n Please try again");
							
						}
		               
				}
				else
				{
					if(dirORIGimg.exists())
					{
						listFile = dirORIGimg.listFiles();
						hmapSmallIconImageName=dbengine.returnImageName();
					}
					else
					{
						dirORIGimg.mkdirs();
						listFile = dirORIGimg.listFiles();
						hmapSmallIconImageName=dbengine.returnImageName();
					}
					boolean containsListFile=false;
					if(listFile.length>0)
					{
						containsListFile=true;
						for(int i=0;i<listFile.length;i++)
						{
							arrayImageName.add(listFile[i].getName().toString().trim());
						}
					}
						if(hmapSmallIconImageName!=null)
						{
							String smallIconName;
							String moduleId;
							int index=0;
							for(Map.Entry<String, String> entry:hmapSmallIconImageName.entrySet())
							{
								smallIconName=entry.getKey().toString().trim();
								if(containsListFile)
								{
									
										if(!arrayImageName.contains(smallIconName))
										{
											arraysmallIconToBDwnld.add(entry.getValue().toString().trim());
											String file_dj_path=dbengine.getimagePath(smallIconName);
											 //String file_dj_path = Environment.getExternalStorageDirectory() + "/GSKModulesSmallImage/"+arrayImageName.get(index).toString();
										      if(file_dj_path!=null)
										      {
										    		File fdelete = new File(file_dj_path);
											        if (fdelete.exists()) {
											            if (fdelete.delete()) {
											                Log.e("-->", "file Deleted :" + file_dj_path);
											                callBroadCast();
											            } else {
											                Log.e("-->", "file not Deleted :" + file_dj_path);
											            }
											        }  
										      }
										
										        
										}		
									
									else
									{
										
									}
									
								}
								else
								{
									arraysmallIconToBDwnld.add(entry.getValue().toString().trim());
								}
								
								
								index++;
							}
						}
						if(arraysmallIconToBDwnld.size()>0)
						{
							new Download_SmallIcon().execute(arraysmallIconToBDwnld);
						}
						else
						{

							 if(dirModuleContent.exists())
							 {
								 hmapContentModule=dbengine.returnModulContent();//("2", "17","22"); 
							 }
								else
								{
									dirModuleContent.mkdirs();
									 hmapContentModule=dbengine.returnModulContent();//("2", "17","22");
								}
							
							 if(hmapContentModule!=null)
								{
									
									for(Map.Entry<String, String> entry:hmapContentModule.entrySet())
									{
										
									
										
										arrayContentDownload.add(entry.getValue().toString().trim()+"^"+entry.getKey().split(Pattern.quote("^"))[1]);
										
										
										System.out.println("Nitish Content ="+entry.getValue().toString().trim());
										
									}
								}
								if(arrayContentDownload.size()>0)
								{
									
									/*arrayContentDownload.add("localSetting"+"^"+"http://103.16.141.16/GSKPDAModuleforDownloads/localsettings.zip");
									dbengine.open();
									dbengine.savetblModuleContent("abx", "localSetting");
									dbengine.close();*/
									
									new  Download_ModuleContent().execute(arrayContentDownload);
								}
								
								else
								{
									 Intent intent=new Intent(LoginActivity.this,AllButtonActivity.class);
										startActivity(intent);
										finish();
								
								}
							
							
						 
						}
						
						
						
					
					}
			
			}
		}
		
		 @Override
		    protected Dialog onCreateDialog(int id) {
		        switch (id) {
		        case progress_bar_icon: // we set this to 0
		            pDialog = new ProgressDialog(this);
		            pDialog.setMessage("Downloading Images. Please wait...");
		            pDialog.setIndeterminate(false);
		            pDialog.setMax(100);
		            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		            pDialog.setCancelable(true);
		            pDialog.show();
		            return pDialog;
		            
		        case progress_bar_content: // we set this to 0
		            pDialog = new ProgressDialog(this);
		            pDialog.setMessage("Downloading Content. Please wait...");
		            pDialog.setIndeterminate(false);
		            pDialog.setMax(100);
		            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		            pDialog.setCancelable(true);
		            pDialog.show();
		            return pDialog;
		        default:
		            return null;
		        }
		    }
		 
		
		public class Download_SmallIcon extends AsyncTask<ArrayList<String>, String, ArrayList<String>>
		{
			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();
				 showDialog(progress_bar_icon);
		
				 pDialog.setCanceledOnTouchOutside(false);
				 	
			}
			
			 protected void onProgressUpdate(String... progress) {
		            // setting progress percentage
				
		            pDialog.setProgress(Integer.parseInt(progress[0]));
		            pDialog.setMessage("Downloading Images "+String.valueOf(countImgeDownloaded)+"/"+arraysmallIconToBDwnld.size());
		            if(progress[0].equals("100"))
		            {
		            	countImgeDownloaded++;
		            	pDialog.setMessage("Downloading Images "+String.valueOf(countImgeDownloaded)+"/"+arraysmallIconToBDwnld.size());
		            	
		            }
		            
		       }
		 
			@Override
			protected ArrayList<String> doInBackground(ArrayList<String>... params) 
			{
				int count;
				 try {
					
				
					 ArrayList<String> passed = params[0]; 
					 System.out.println("ArrayList async =  "+params[0]);
					
					// dbengine.open();
					 for(int i=0;i<passed.size();i++)
					 {
						
						   fileuri="/sdcard/GSKModulesSmallImage/"+passed.get(i).toString().split(Pattern.quote("^"))[0];
						   
						  
						 URL u=new URL(passed.get(i).toString().split(Pattern.quote("^"))[1]);
						 URLConnection conn = u.openConnection();
					        int contentLength = conn.getContentLength();

					        DataInputStream stream = new DataInputStream(u.openStream());

					          byte[] buffer = new byte[contentLength];
					        
					          DataOutputStream fos = new DataOutputStream(new FileOutputStream(new File(fileuri)));
					          String url=passed.get(i).toString();
					   //       dbengine.updateURI(fileuri, url);
							   long total = 0;
							   while ((count = stream.read(buffer)) != -1) {
					                total += count;
					                // publishing the progress....
					                // After this onProgressUpdate will be called
					              publishProgress(""+(int)((total*100)/contentLength));
					 
					                // writing data to file
					                fos.write(buffer,0,count);

					            }
							   stream.close();
					          fos.flush();
					          fos.close();
					        
					         
					          dbengine.updatePathsmallIcon(fileuri, passed.get(i).toString().split(Pattern.quote("^"))[0]);
					 }
					// dbengine.close();
				
	        
				
				 }
				 catch(Exception e)
				 {
					 isErrorAlert=true;
					 ErrorName=e.toString().trim();
					
				 }
				
				return null;
				
				
			}
			@Override
			protected void onPostExecute(ArrayList<String>
			result) {
				
				super.onPostExecute(result);
				//mProgressDialog.dismiss();
				 dismissDialog(progress_bar_icon);
				 if(isErrorAlert)
				 {
					 System.out.println("Error Name :"+ErrorName);
					 if(ErrorName.equals("java.lang.NegativeArraySizeException: -1"))
					 {
						    isErrorAlert=false;
							
							arraysmallIconToBDwnld=new ArrayList<String>();
							alert("Error", "Application Security does not allow to download the content!");
					 }
					 else
					 {
						    isErrorAlert=false;
							
							arraysmallIconToBDwnld=new ArrayList<String>();
							alert("Error", "Error While Downloading Images!");
					 }
					
				 }
				 else
				 {
					 if(dirModuleContent.exists())
					 {
						 hmapContentModule=dbengine.returnModulContent();//("2", "17","22"); 
					 }
						else
						{
							dirModuleContent.mkdirs();
							 hmapContentModule=dbengine.returnModulContent();//("2", "17","22");
						}
					
					 if(hmapContentModule!=null)
						{
							
							for(Map.Entry<String, String> entry:hmapContentModule.entrySet())
							{
								
							
								
								arrayContentDownload.add(entry.getValue().toString().trim()+"^"+entry.getKey().split(Pattern.quote("^"))[1]);
								
								
								System.out.println("Nitish Content ="+entry.getValue().toString().trim());
								
							}
						}
						if(arrayContentDownload.size()>0)
						{
							
							/*arrayContentDownload.add("localSetting"+"^"+"http://103.16.141.16/GSKPDAModuleforDownloads/localsettings.zip");
							dbengine.open();
							dbengine.savetblModuleContent("abx", "localSetting");
							dbengine.close();*/
							
							new  Download_ModuleContent().execute(arrayContentDownload);
						}
						
						else
						{
							 Intent intent=new Intent(LoginActivity.this,AllButtonActivity.class);
								startActivity(intent);
								finish();
						
						}
					
					
				 }
			
				
				
				
			}
		}
		
		 public void callBroadCast() {
		        if (Build.VERSION.SDK_INT >= 14) {
		            Log.e("-->", " >= 14");
		            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
		              
		                public void onScanCompleted(String path, Uri uri) {
		                
		                }
		            });
		        } else {
		            Log.e("-->", " < 14");
		            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		        }
		        
		        
		       
		    }
		 
		 public void alert(String title,String message)
		 {
			 AlertDialog alertDialog = new AlertDialog.Builder(
                     LoginActivity.this).create();

     // Setting Dialog Title
     alertDialog.setTitle(title);

     // Setting Dialog Message
     alertDialog.setMessage(message);

     // Setting Icon to Dialog
     alertDialog.setIcon(R.drawable.info_ico);

     // Setting OK Button
     alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
             
            
             }
     });

     // Showing Alert Message
     alertDialog.show();
		 }
		 
			public class Download_ModuleContent extends AsyncTask<ArrayList<String>, String, ArrayList<String>>
			{
				@Override
				protected void onPreExecute() {
					
					super.onPreExecute();
					 showDialog(progress_bar_content);
				
					 pDialog.setCanceledOnTouchOutside(false);
					 	
				}
				
				 protected void onProgressUpdate(String... progress) {
			            // setting progress percentage
					
			            pDialog.setProgress(Integer.parseInt(progress[0]));
			            pDialog.setMessage("Downloading Contents "+String.valueOf(countContentDownloaded)+"/"+arrayContentDownload.size());
			            if(progress[0].equals("100"))
			            {
			            	countContentDownloaded++;
			            	pDialog.setMessage("Downloading Contents "+String.valueOf(countContentDownloaded)+"/"+arrayContentDownload.size());
			            	
			            }
			            
			       }
			 
				@Override
				protected ArrayList<String> doInBackground(ArrayList<String>... params) 
				{
					int count;
					 try {
						
					
						 ArrayList<String> passed = params[0]; 
						 System.out.println("Nitish Contents async =  "+params[0]);
						
						// dbengine.open();
						 for(int i=0;i<passed.size();i++)
						 {
							
							   fileuri="/sdcard/GSKModulesContent/"+passed.get(i).toString().split(Pattern.quote("^"))[0];
							   
							  
							 URL u=new URL(passed.get(i).toString().split(Pattern.quote("^"))[1]);
							 URLConnection conn = u.openConnection();
						        int contentLength = conn.getContentLength();

						        DataInputStream stream = new DataInputStream(u.openStream());

						          byte[] buffer = new byte[contentLength];
						        
						          DataOutputStream fos = new DataOutputStream(new FileOutputStream(new File(fileuri)));
						          String url=passed.get(i).toString();
						   //       dbengine.updateURI(fileuri, url);
								   long total = 0;
								   while ((count = stream.read(buffer)) != -1) {
						                total += count;
						                // publishing the progress....
						                // After this onProgressUpdate will be called
						              publishProgress(""+(int)((total*100)/contentLength));
						 
						                // writing data to file
						                fos.write(buffer,0,count);

						            }
								   stream.close();
						          fos.flush();
						          fos.close();
						        
						         
						          dbengine.updateModuleContent(fileuri, passed.get(i).toString().split(Pattern.quote("^"))[0],passed.get(i).toString().split(Pattern.quote("^"))[2]);
						 }
						// dbengine.close();
					
		        
					
					 }
					 catch(Exception e)
					 {
						 isErrorAlert=true;
						// Log.e("theple", "" + e);
					 }
					
					return null;
					
					
				}
				@Override
				protected void onPostExecute(ArrayList<String>
				result) {
					
					super.onPostExecute(result);
					//mProgressDialog.dismiss();
					 dismissDialog(progress_bar_content);
					 if(isErrorAlert)
					 {
						 isErrorAlert=false;
						 alert("Error", "Error While Downloading Modules Content!");
					 }
					 else
					 {
						 new Unzip().execute(); 
					 }
					
					
				}
			}
			
			public void unzip(String _zipFile,String LanguageID) { 
			    try  { 
		
			   FileInputStream fin = new FileInputStream(_zipFile); 
			      ZipInputStream zin = new ZipInputStream(fin); 
			      ZipEntry ze = null; 
			      while ((ze = zin.getNextEntry()) != null) { 
			     
			 
			        if(ze.isDirectory()) { 
			         _dirChecker(ze.getName(),Environment.getExternalStorageDirectory() + "/iOSPlayer/"); 
			        } else { 
			        	
			          FileOutputStream fout = new FileOutputStream("/sdcard/iOSPlayer/"+ze.getName());
			        	//FileOutputStream fout = new FileOutputStream(unzipDestination);
			          byte b[] = new byte[1024];
			          int n;
			          while ((n = zin.read(b,0,1024)) >= 0) {
			        	  fout.write(b,0,n);
			          }
			          /*for (int c = zin.read(); c != -1; c = zin.read()) { 
			            fout.write(c); 
			          } */
			 
			          zin.closeEntry(); 
			          fout.close(); 
			        } 
			         
			      } 
			      zin.close(); 
			      dbengine.updateModuleContentDownloaded(_zipFile,LanguageID);
			    } catch(Exception e) { 
			    	isErrorAlert=true;
			      
			    } 
			}
			 
			private void _dirChecker(String dir,String _location) { 
			    File f = new File(_location + dir); 
			 
			    if(!f.isDirectory()) { 
			      f.mkdirs(); 
			    } 
			  } 
			 
			
			private class Unzip extends AsyncTask<Void,Void,Void>
			{
				
				
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					pDialogVersionCheck.setTitle(getText(R.string.genTermPleaseWaitNew));
					pDialogVersionCheck.setMessage("Unziping File...");
					pDialogVersionCheck.setIndeterminate(false);
					pDialogVersionCheck.setCancelable(false);
					pDialogVersionCheck.setCanceledOnTouchOutside(false);
					pDialogVersionCheck.show();
				}
				@Override
				protected Void doInBackground(Void... args) 
				{
					
					
					try
					{
						ArrayList<String> arrayZipData=new ArrayList<String>();
						arrayZipData=dbengine.returnZipFileDetails();
						if(arrayZipData!=null)
						{
							if(arrayZipData.size()>0)
							{
								for(int i=0;i<arrayZipData.size();i++)
								{
									String fileUrl=arrayZipData.get(i).toString().split(Pattern.quote("^"))[1];
									String filename=arrayZipData.get(i).toString().split(Pattern.quote("^"))[0];
									String LanguageID=arrayZipData.get(i).toString().split(Pattern.quote("^"))[2];
									unzip(fileUrl,LanguageID);
								}
							}
						
						}
						
						
					}
					catch (Exception e) 
				    {
						isErrorAlert=true;
						 newservice.director="100";
						//showNoConnAlert();
						Log.i("SvcMgr", "Service Execution Failed!", e);
		            }

					finally
					 {
		                Log.i("SvcMgr", "Service Execution Completed...");
		             }
					
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result)
				{
					super.onPostExecute(result);
					
					if(pDialogVersionCheck.isShowing()) 
			        {
			           pDialogVersionCheck.dismiss();
			          
			        }
					if(isErrorAlert)
					{
						isErrorAlert=false;
						alert("Error", "Error While Unzipping File!");
					}
					else
					{
						 Intent intent=new Intent(LoginActivity.this,AllButtonActivity.class);
							startActivity(intent);
							finish();
					}
					
					
				}
			}
			
			 private class GetForgotPasswordCheck extends AsyncTask<Void,Void,Void>
				{
					ServiceWorker newservice = new ServiceWorker();
					public GetForgotPasswordCheck(LoginActivity activity) 
					{
						pDialogVersionCheck = new ProgressDialog(activity);
					}
					@Override
					protected void onPreExecute()
					{
						super.onPreExecute();
						pDialogVersionCheck.setTitle(getText(R.string.genTermPleaseWaitNew));
						pDialogVersionCheck.setMessage("fetching the password.");
						pDialogVersionCheck.setIndeterminate(false);
						pDialogVersionCheck.setCancelable(false);
						pDialogVersionCheck.setCanceledOnTouchOutside(false);
						pDialogVersionCheck.show();
					}
					@Override
					protected Void doInBackground(Void... args) 
					{
						// TODO Auto-generated method stub
						int DatabaseVersionID=CommonInfo.DATABASE_VERSION;
				        int ApplicationID=CommonInfo.Application_TypeID;
						try
						{
							String contactNo="0";
							String Password="0";
							if(TextUtils.isEmpty(txt_userName.getText().toString()))
								{
								   contactNo="0000000000";
								}
								else
								{
									contactNo=txt_userName.getText().toString().trim();
								}
							
							
							newservice = newservice.sendPasswordToRegisteredUser(getApplicationContext(),imei,DatabaseVersionID,ApplicationID,contactNo);
							 if(!newservice.director.toString().trim().equals("1"))
					           {	
									if(chkFlgForErrorToCloseApp==0)
									{
										chkFlgForErrorToCloseApp=1;
			                      }
								
							  } 
						}
						catch (Exception e) 
					    {
							 newservice.director="100";
							//showNoConnAlert();
							Log.i("SvcMgr", "Service Execution Failed!", e);
			            }

						finally
						 {
			                Log.i("SvcMgr", "Service Execution Completed...");
			             }
						
						return null;
					}
					
					@Override
					protected void onPostExecute(Void result)
					{
						super.onPostExecute(result);
						
						if(pDialogVersionCheck.isShowing()) 
				        {
				           pDialogVersionCheck.dismiss();
				           btn_login.setEnabled(true);
				        }
						
						
						if(chkFlgForErrorToCloseApp==1)   // if Webservice showing exception or not excute complete properly
						{
							
								//btn_login.setEnabled(true); 
								chkFlgForErrorToCloseApp=0;
							
								 if(newservice.director.toString().trim().equals("100"))
								{
									showNoConnAlert();
								}
								else if(newservice.director.toString().trim().equals("200"))
								{
									showAlertForEveryOne("Error while Retrieving Data!\n Please try again");
								}
								
								else
								{
									
									showAlertForEveryOne("Error while Retrieving Data!\n Please try again");
									
								}
				               
						}
						else
						{
							
							
							if(!msg.equals(""))
							{
								// showAlertForEveryOne(msg);	
								// msg="";
								 
								 String userName="NA";
								 if(TextUtils.isEmpty(txt_userName.getText().toString().trim()))
									{
									 
									}
								 else
								 {
									 userName=txt_userName.getText().toString().trim();
								 }
								 
								 alert("Username : "+userName, "Hi Your Password is : "+msg);
								 msg="";
							}
							
						
							
							
							
						}
					
					}
				}
}
