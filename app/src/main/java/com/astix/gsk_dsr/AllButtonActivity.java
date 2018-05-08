package com.astix.gsk_dsr;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.astix.gsk_dsr.R;



import android.app.Activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class AllButtonActivity extends Activity
{
	
	
	ImageView img_side_popUp;
	Dialog dialog;
	public ProgressDialog pDialogGetStores;
	public String newfullFileName;
	
	ImageView img_logout;
	
	DatabaseAssistant DA = new DatabaseAssistant(this);
	GskDBAdapter dbengine = new GskDBAdapter(this);
	
	//public TextView txtVw_name;
	//public TextView txtVw_contact;
	
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
	
	public void checkNonSubmitData()
	{
		 File GSKXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
			
		 if (!GSKXMLFolder.exists()) 
			{
			 GSKXMLFolder.mkdirs();
			} 
   	 
   	 File del = new File(Environment.getExternalStorageDirectory(), "GSKXML");
   	
   	// check number of files in folder
   	 String [] AllFilesName= checkNumberOfFiles(del);
   	 
   	 if(AllFilesName.length>0)
   	 {
   		 long  syncTIMESTAMP = System.currentTimeMillis();
				   Date dateobj = new Date(syncTIMESTAMP);
				   SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				   String StampEndsTime = df.format(dateobj);
				
			       SimpleDateFormat df1 = new SimpleDateFormat(CommonInfo.imei+ ".dd.MM.yyyy.HH.mm.ss",Locale.ENGLISH);
			
			       newfullFileName=df1.format(dateobj);
			
			
				try 
				{
					DA.open();
					DA.export(CommonInfo.DATABASE_NAME, newfullFileName);
					DA.close();
					
					
				dbengine.open();
				dbengine.updateXMLRecordsSyncd();		// update syncd' records
				dbengine.close();
				} catch (Exception e)
				{
					
				}
			
			
			 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
			 Editor editor = pref.edit();
			 editor.putString(newfullFileName, ""+3);  // Saving string
			    
			    // Save the changes in SharedPreferences
			  editor.commit(); // commit changes
			
 		
 		
 		 if(isOnline())
			 {
			
				Intent intent = new Intent(AllButtonActivity.this, UploadXMLService.class);
				intent.putExtra("foo", "bar");
				startService(intent);
			 }
   	 }
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_button);
		
		 if(isOnline())
		 {
			 checkNonSubmitData(); 
		 }
		
//		String UserNameandContact=dbengine.fnGetUserNameContact();
		/*txtVw_name=(TextView) findViewById(R.id.txtVw_name);
		txtVw_name.setText(UserNameandContact.split(Pattern.quote("^"))[0]);
		txtVw_contact=(TextView) findViewById(R.id.txtVw_contact);
		txtVw_contact.setText(UserNameandContact.split(Pattern.quote("^"))[1]);*/
		
		img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
		img_side_popUp.setOnClickListener(new OnClickListener() 
		  {
				@Override
				public void onClick(View v)
				{
					 open_pop_up();
				}
		  });
		
		
		img_logout=(ImageView) findViewById(R.id.img_logout);
		img_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);
				 alertDialog.setTitle("Information");
			        alertDialog.setIcon(R.drawable.info_ico);
			        alertDialog.setCancelable(false);
			     alertDialog.setMessage("Do you really want to logout ? ");
			     alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            	dialog.dismiss();
			            finishAffinity();
			            }
			        });
			     alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            	dialog.dismiss();
			            }
			        });
			 
			        // Showing Alert Message
			        alertDialog.show();
				 
				
			}
		});
      /* img_logout.setOnClickListener(new OnClickListener() 
       {
			
			@Override
			public void onClick(View v) 
			{*/
				/*// TODO Auto-generated method stub
				
				if(dbengine.validateToLogout())
			    {
			     AlertDialog alertDialog = new AlertDialog.Builder(AllButtonActivity.this).create();

			   
			   alertDialog.setTitle("Alert");

			   alertDialog.setIcon(R.drawable.error_info_ico);
			   alertDialog.setMessage("Please Upload Data Before Logging Out.");


			   
			   alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int which) 
			           {
			            dialog.dismiss();
			           }
			   });

			   
			   alertDialog.show();
			    }
			    else
			    {
			     AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);

			     // Setting Dialog Title
			     alertDialog.setTitle("Confirm Logout");

			     // Setting Dialog Message
			     alertDialog.setMessage("Do you really want to logout!");

			     

			     // Setting Positive "Yes" Button
			     alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() 
			     {
			      public void onClick(DialogInterface dialog,int which)
			      {
			    	  
			    	  
			    	  File MRDisplayXMLFolder = new File(Environment.getExternalStorageDirectory(), "MRDisplayXML");
	      				
	        			 if (!MRDisplayXMLFolder.exists()) 
	        				{
	        				 MRDisplayXMLFolder.mkdirs();
	        					 
	        				} 
	                	 
	                	 File del = new File(Environment.getExternalStorageDirectory(), "MRDisplayXML");
	                     
	                     deleteNon_EmptyDir(del);
	                     
	                     File MRDisplayImagesFolder = new File(Environment.getExternalStorageDirectory(), "MRDisplayImages");
	      				
	        			 if (!MRDisplayImagesFolder.exists()) 
	        				{
	        				 MRDisplayImagesFolder.mkdirs();
	        					 
	        				} 
	                	 
	                	 File del1 = new File(Environment.getExternalStorageDirectory(), "MRDisplayImages");
	                     
	                     deleteNon_EmptyDir(del1);
			    	  

			       SharedPreferences pref = getApplicationContext().getSharedPreferences("MRDisplay", MODE_PRIVATE); 
			                Editor editor = pref.edit();
			                editor.clear();
			                editor.commit();
			                finishAffinity();
			     }
			     });

			     // Setting Negative "NO" Button
			     alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			      // Write your code here to invoke NO event
			      
			      dialog.cancel();
			      }
			     });

			     // Showing Alert Message
			     alertDialog.show();
			     
			    }
				
			}
	   });*/

		
		
		
		final Button btn_eLearning=(Button)findViewById(R.id.btn_eLearning);
		btn_eLearning.setOnClickListener(new OnClickListener()
		{
            @Override
			public void onClick(View v) 
			{
            	
            	final float growTo = 1.05f;
        		final long duration = 800;

        		ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo, 
        		                                         Animation.RELATIVE_TO_SELF, 0.5f,
        		                                         Animation.RELATIVE_TO_SELF, 0.5f);
        		grow.setDuration(duration / 2);
        		ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
        		                                           Animation.RELATIVE_TO_SELF, 0.5f,
        		                                           Animation.RELATIVE_TO_SELF, 0.5f);
        		shrink.setDuration(duration / 2);
        		shrink.setStartOffset(duration / 2);
        		AnimationSet growAndShrink = new AnimationSet(true);
        		growAndShrink.setInterpolator(new LinearInterpolator());
        		growAndShrink.addAnimation(grow);
        		growAndShrink.addAnimation(shrink);
        		btn_eLearning.startAnimation(growAndShrink);
				// TODO Auto-generated method stub
				Intent intent=new Intent(AllButtonActivity.this,ModulesActivity.class);
				//intent.putExtra("btnId", "2");
				startActivity(intent);
				finish();
				
			}
		});
		
		
		final Button button3=(Button)findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener()
		{
            @Override
			public void onClick(View v) 
			{
            	
            	final float growTo = 1.05f;
        		final long duration = 800;

        		ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo, 
        		                                         Animation.RELATIVE_TO_SELF, 0.5f,
        		                                         Animation.RELATIVE_TO_SELF, 0.5f);
        		grow.setDuration(duration / 2);
        		ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
        		                                           Animation.RELATIVE_TO_SELF, 0.5f,
        		                                           Animation.RELATIVE_TO_SELF, 0.5f);
        		shrink.setDuration(duration / 2);
        		shrink.setStartOffset(duration / 2);
        		AnimationSet growAndShrink = new AnimationSet(true);
        		growAndShrink.setInterpolator(new LinearInterpolator());
        		growAndShrink.addAnimation(grow);
        		growAndShrink.addAnimation(shrink);
        		button3.startAnimation(growAndShrink);
				// TODO Auto-generated method stub
				
        		CopyReadAssets();
        
				
			}
		});
		
		final Button button4=(Button)findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener()
		{
            @Override
			public void onClick(View v) 
			{
            	
            	final float growTo = 1.05f;
        		final long duration = 800;

        		ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo, 
        		                                         Animation.RELATIVE_TO_SELF, 0.5f,
        		                                         Animation.RELATIVE_TO_SELF, 0.5f);
        		grow.setDuration(duration / 2);
        		ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
        		                                           Animation.RELATIVE_TO_SELF, 0.5f,
        		                                           Animation.RELATIVE_TO_SELF, 0.5f);
        		shrink.setDuration(duration / 2);
        		shrink.setStartOffset(duration / 2);
        		AnimationSet growAndShrink = new AnimationSet(true);
        		growAndShrink.setInterpolator(new LinearInterpolator());
        		growAndShrink.addAnimation(grow);
        		growAndShrink.addAnimation(shrink);
        		button4.startAnimation(growAndShrink);
				
        		
        		 Intent intent=new Intent(AllButtonActivity.this,DistributorAssessmentMain.class);
        		    startActivity(intent);
        		    finish();
        		
				
			}
		});
		
	
		
		
	}
	
	  public static boolean deleteNon_EmptyDir(File dir) 
	  {
	        if (dir.isDirectory()) 
	        {
	            String[] children = dir.list();
	            for (int i=0; i<children.length; i++) 
	            {
	                boolean success = deleteNon_EmptyDir(new File(dir, children[i]));
	                if (!success) {
	                    return false;
	                }
	            }
	        }
	        return dir.delete();
	    }
	
	 public void showNoConnAlert() 
	 {
			AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);
			alertDialogNoConn.setTitle("No Data Connection!");
			alertDialogNoConn.setMessage("Your device has no Data Connection! Please ensure Internet is accessible to Continue.");
			alertDialogNoConn.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) 
						{
                        dialog.dismiss();
                        }
					});
			alertDialogNoConn.setIcon(R.drawable.error_ico);
			AlertDialog alert = alertDialogNoConn.create();
			alert.show();
			
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
	
	
	 protected void open_pop_up() 
	 {
	        dialog = new Dialog(AllButtonActivity.this);
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.selection_header_custom);
	        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	        dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
	        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
	        parms.gravity = Gravity.TOP | Gravity.LEFT;
	        parms.height=parms.MATCH_PARENT;
	        parms.dimAmount = (float) 0.5;
	        

	      
	        final Button btnVersion = (Button) dialog.findViewById(R.id.btnVersion);
	        btnVersion.setOnClickListener(new OnClickListener()
	        {
				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					
					 btnVersion.setBackgroundColor(Color.GREEN);
					 dialog.dismiss();
				}
			});
	        
	        
			 String ApplicationVersion=CommonInfo.AppVersionID;
			
			 btnVersion.setText("Version No-V"+ApplicationVersion);
			 
			// Version No-V12
	        
/*	        final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
	        btnSubmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					dialog.dismiss();
					
					if(isOnline())
					{
						 // Changes By Sunil 
						   AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(AllButtonActivity.this);
							alertDialogSubmitConfirm.setTitle("Information");
							alertDialogSubmitConfirm.setMessage(getText(R.string.submitConfirmAlert));
							alertDialogSubmitConfirm.setCancelable(false);
							
							alertDialogSubmitConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which)
										{
											
												
												 try
												    {
													 dbengine.open();
													 dbengine.fnUpdatetblUserLoginMstr(3);
													 dbengine.close();
													FullSyncDataNow task = new FullSyncDataNow(AllButtonActivity.this);
													 task.execute();
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
			});*/
			
	      

	        dialog.setCanceledOnTouchOutside(true);
	        dialog.show();
	    }

	 
	 
	 private class FullSyncDataNow extends AsyncTask<Void, Void, Void> 
		{
			

			
			public FullSyncDataNow(AllButtonActivity activity) 
			{
				pDialogGetStores = new ProgressDialog(activity);
			}
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				
				 File MRDisplayXMLFolder = new File(Environment.getExternalStorageDirectory(), "MRDisplayXML");
					
				 if (!MRDisplayXMLFolder.exists()) 
					{
					 MRDisplayXMLFolder.mkdirs();
						 
					} 
				
				
				pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
				pDialogGetStores.setMessage("Submitting Stores Details...");
				pDialogGetStores.setIndeterminate(false);
				pDialogGetStores.setCancelable(false);
				pDialogGetStores.setCanceledOnTouchOutside(false);
				pDialogGetStores.show();
			   
			  
			}

			@Override
			
			protected Void doInBackground(Void... params) 
			{

				
				
				    long  syncTIMESTAMP = System.currentTimeMillis();
					Date dateobj = new Date(syncTIMESTAMP);
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					String StampEndsTime = df.format(dateobj);
					
				SimpleDateFormat df1 = new SimpleDateFormat(CommonInfo.imei+ "dd.MM.yyyy.HH.mm.ss",Locale.ENGLISH);
				
				newfullFileName=df1.format(dateobj);
				
				
				 
				
				try 
				{
					
				
					DA.open();
					DA.export(CommonInfo.DATABASE_NAME, newfullFileName);
					DA.close();
					
					

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
				
				try {
					
					Intent syncIntent = new Intent(AllButtonActivity.this, SyncMaster.class);
					syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/MRDisplayXML/" + newfullFileName + ".xml");
					syncIntent.putExtra("OrigZipFileName", newfullFileName);
					syncIntent.putExtra("whereTo", "12");
					startActivity(syncIntent);
					finish();
					

				} catch (Exception e) {
				
					e.printStackTrace();
				}
				
				
			}
		}
	
	
	 private void CopyReadAssets()
     {
         AssetManager assetManager = getAssets();

         InputStream in = null;
         OutputStream out = null;
         File file = new File(getFilesDir(), "two_wheeler.pdf");
         try
         {
             in = assetManager.open("two_wheeler.pdf");
             out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

             copyFile(in, out);
             in.close();
             in = null;
             out.flush();
             out.close();
             out = null;
         } catch (Exception e)
         {
             
         }

         Intent intent = new Intent(Intent.ACTION_VIEW);
         intent.setDataAndType(
                 Uri.parse("file://" + getFilesDir() + "/two_wheeler.pdf"),
                 "application/pdf");

         startActivity(intent);
     }

     private void copyFile(InputStream in, OutputStream out) throws IOException
     {
         byte[] buffer = new byte[1024];
         int read;
         while ((read = in.read(buffer)) != -1)
         {
             out.write(buffer, 0, read);
         }
     }

 
	
}
