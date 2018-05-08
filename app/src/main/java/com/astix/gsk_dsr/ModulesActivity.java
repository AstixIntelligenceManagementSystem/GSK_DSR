package com.astix.gsk_dsr;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModulesActivity extends Activity {
	
	Dialog dialog;
	LinearLayout ll_modules;
	String moduleId,moduleHeading,moduleSubHeading,moduleDesc,filePathStrings,flagNewOrOld;
	SpannableStringBuilder txt_flagNew;
	GskDBAdapter dbengine;
	ImageView img_side_popUp;
private File[] listFile;
	
	File fileintial;
	LinkedHashMap<String, String> modulesData=new LinkedHashMap<String, String>();
	

	public ProgressDialog pDialogGetStores;
	public String newfullFileName;
	DatabaseAssistant DA = new DatabaseAssistant(this);
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
			
				Intent intent = new Intent(ModulesActivity.this, UploadXMLService.class);
				intent.putExtra("foo", "bar");
				startService(intent);
			 }
  	 }
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modules);
		
		 if(isOnline())
		 {
			 checkNonSubmitData(); 
		 }
		
		ll_modules=(LinearLayout) findViewById(R.id.ll_modules);
		img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
		img_side_popUp.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				try
				{
				open_pop_up();
				}
				catch(Exception e)
				{
					
				}
			}
		});
		dbengine=new GskDBAdapter(ModulesActivity.this);
		getAllModulesFromDatabase();
		crateView();
		
		
	}

	private void getAllModulesFromDatabase() {
		
		modulesData=dbengine.getAllModules();
		
	}
	

	private void crateView()
	{
		
		if(modulesData!=null && modulesData.size()>0)
		{
			
			LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				
			} 
			else 
			{
				// Locate the image folder in your SD Card
				fileintial = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "Parag SKU");
				// Create a new folder if no folder named SDImageTutorial exist
				fileintial.mkdirs();
			}
	 
			if (fileintial.isDirectory()) {
				listFile = fileintial.listFiles();
				
				
				
			}
			
			for(Map.Entry<String,String> entry:modulesData.entrySet() )
			{
				final View view=inflater.inflate(R.layout.single_module_design,null);
			//LPCourseGroupMainID , LPCourseGroupName , LPCourseID , LPCourseName , LPCourseDescription ,ModuleImgName , ModuleImgUrl , Status ,DayVal , flgNewOld	
				moduleId=entry.getKey();
				moduleHeading=entry.getValue().toString().split(Pattern.quote("^"))[3];
				moduleSubHeading=entry.getValue().toString().split(Pattern.quote("^"))[1];
				String descrpt=entry.getValue().toString().split(Pattern.quote("^"))[4];
				moduleDesc=descrpt.substring(0, 6)+"...";
				flagNewOrOld=entry.getValue().toString().split(Pattern.quote("^"))[9];
				LinearLayout ll_singleModule=(LinearLayout) view.findViewById(R.id.ll_singleModule);
				ll_singleModule.setTag(moduleId.toString());
				ImageView img_smallIcon=(ImageView) view.findViewById(R.id.img_smallIcon);
				TextView txt_heading=(TextView) view.findViewById(R.id.txt_heading);
				txt_heading.setText(moduleHeading);
				TextView txt_New=(TextView) view.findViewById(R.id.txt_New);
				if(flagNewOrOld.equals("1"))
				{
					if(txt_New.getVisibility()==View.GONE)
					{
						txt_New.setVisibility(View.VISIBLE);
					}
					
					
				}
				else
				{
					if(txt_New.getVisibility()==View.VISIBLE)
					{
						txt_New.setVisibility(View.GONE);
					}
					
				}
				
				TextView txt_sub_heading=(TextView) view.findViewById(R.id.txt_sub_heading);
				txt_sub_heading.setText(moduleSubHeading);
				TextView txt_short_descrp=(TextView) view.findViewById(R.id.txt_short_descrp);
				txt_short_descrp.setText(descrpt);
				
				filePathStrings =entry.getValue().toString().split(Pattern.quote("^"))[10]; ;
				Bitmap bmp = BitmapFactory.decodeFile(filePathStrings);
				img_smallIcon.setImageBitmap(bmp);
				ll_singleModule.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(ModulesActivity.this,AssesmentModuleActivity.class);
						intent.putExtra("ModuleId", v.getTag().toString());
						startActivity(intent);
						finish();
						
					}
				});
				ll_modules.addView(view);
			}
				
		}
		
	}
	
	 public void showNoConnAlert() 
	 {
			AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(ModulesActivity.this);
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
		
	        dialog = new Dialog(ModulesActivity.this);
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.selection_header_custom);
	        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	        dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
	        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
	        parms.gravity = Gravity.TOP | Gravity.LEFT;
	        parms.height=parms.MATCH_PARENT;
	        parms.dimAmount = (float) 0.5;
	        

	        
	        Button butn_eLearning=(Button) dialog.findViewById(R.id.butn_eLearning);
	       butn_eLearning.setBackgroundResource(R.drawable.alert_text_background_selected);
	       
	       final   Button img_Dashboard = (Button) dialog.findViewById(R.id.img_Dashboard);
	       img_Dashboard.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent=new Intent(ModulesActivity.this,AllButtonActivity.class);
					startActivity(intent);
					finish();
					
				}
			});
	        
	        
	        final   Button btn_newLink = (Button) dialog.findViewById(R.id.btn_newLink);
	       
	        btn_newLink.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					
					/*String url = "http://103.20.212.194/gsk_test/Modules/Workshop%203/crocin_english/ContentPage.aspx?CourseMainID=32&CourseCatID=43&LoginId=33612";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);*/
					
					Intent intent=new Intent(ModulesActivity.this,WebViewActivity.class);
					startActivity(intent);
					//finish();
					
				}
			});
	        
	        
	        
	        
	        
	        final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
	        btnSubmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					dialog.dismiss();
					
					if(isOnline())
					{
						 // Changes By Sunil 
						   AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(ModulesActivity.this);
							alertDialogSubmitConfirm.setTitle("Information");
							alertDialogSubmitConfirm.setMessage(getText(R.string.submitConfirmAlert));
							alertDialogSubmitConfirm.setCancelable(false);
							
							alertDialogSubmitConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which)
										{
											
												
												 try
												    {
													/* dbengine.open();
													 dbengine.fnUpdatetblUserLoginMstr(3);
													 dbengine.close();*/
													//FullSyncDataNow task = new FullSyncDataNow(ModulesActivity.this);
													// task.execute();
													 
													 File GSKXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
									 					
								    				 if (!GSKXMLFolder.exists()) 
								    					{
								    					 GSKXMLFolder.mkdirs();
								    						 
								    					} 
								    				 
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
								        			
								        				Intent intent = new Intent(ModulesActivity.this, UploadXMLService.class);
								        				intent.putExtra("foo", "bar");
								        				startService(intent);
								        			 }
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
			});
			
	      


	     dialog.setCanceledOnTouchOutside(true);
	     dialog.show();
	    }
	
	
	
	 private class FullSyncDataNow extends AsyncTask<Void, Void, Void> 
		{
			

			
			public FullSyncDataNow(ModulesActivity activity) 
			{
				pDialogGetStores = new ProgressDialog(activity);
			}
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				
				 File MRDisplayXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
					
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
					
					Intent syncIntent = new Intent(ModulesActivity.this, SyncMaster.class);
					syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/GSKXML/" + newfullFileName + ".xml");
					syncIntent.putExtra("OrigZipFileName", newfullFileName);
					syncIntent.putExtra("whereTo", "12");
					startActivity(syncIntent);
					finish();
					

				} catch (Exception e) {
				
					e.printStackTrace();
				}
				
				
			}
		}
	
	 public SpannableStringBuilder textWithMandatory(String text_Value)
		
		{
			String simple = text_Value;
			String colored = "New";
			SpannableStringBuilder builder = new SpannableStringBuilder();

			builder.append(simple);
			int start = builder.length();
			builder.append(colored);
			int end = builder.length();

			builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, 
			                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			//text.setText(builder);
			
			return builder;

		}
}
