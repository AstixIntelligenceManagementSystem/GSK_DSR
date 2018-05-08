package com.astix.gsk_dsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.astix.gsk_dsr.R;






import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AssesmentModuleActivity extends Activity 
{
	public String flgTestMap="0";
	public String AssessmentStatus="0";
	public static String LPEndDate="0";
	public String NoTestMsg="No Test Associated";
	public static String ModulesStatus="0";
	ServiceWorker service_worker;
	ProgressDialog prgDialog;
	public int chkFlgForErrorToCloseApp=0;
	
	String LoginId="0";
	String UserId="0";
	File dir12345,file12345,fileIos;
	String ModuleId;
	GskDBAdapter dbengine;
    String	CourseCatID="0";
	boolean isModulePPtCompleted=false;
	public String strModuleCurentSelecteLangID="0";
	
	LinkedHashMap<String, String> hmapLanguageIDColumnNameMap=new LinkedHashMap<String, String>();
	
	
	LinkedHashMap<String, String> hmapModulesLanguageData=new LinkedHashMap<String, String>();
	
	LinkedHashMap<String, String> hmapModulesIDOnlineOffLine=new LinkedHashMap<String, String>();
	
	LinkedHashMap<String, String> hmapLanguageData=new LinkedHashMap<String, String>();
	
	LinkedHashMap<String, String> hmapModulesData=new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> hmapProjectName_ID=new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> hmapSlideInfoCurrent=new LinkedHashMap<String, String>();
	
	LinkedHashMap<String, String> hmapAllModuleIdName=new LinkedHashMap<String, String>();
	
	LinkedHashMap<String, String> hmapSaveDataViewModule=new LinkedHashMap<String, String>();
	String imeiNum;
	
	TextView txt_Assmnt;
	
	TextView txt_comp_incomp,txt_status;
	String lpCourseNameCurrent;
	String moduleHeading,moduleSubHeading,moduleDesc,filePathStrings,flagNewOrOld;
	ImageView img_Module,img_back,img_start_Module;
	LinearLayout ll_module_Desc;
	String slideName,slideNo,courseName;
	String prvs_ProjectRunning,prvs_ModuleId,prvs_LangID;
	String modulFolderName;
	SharedPreferences sharedPref;
	String lastSlideName="",prvousLastSlideName="";
	SharedPreferences.Editor editor;
	public static final String MyPREFERENCES = "GSKELearning" ;
	TextView txt_sub_heading,txtVw_ModuleHeading,txt_Status;
	
	
	TextView txt_print_certificate;
	 boolean existArticulate=true;
	 TextView txt_short_descrp;
	 private ActivityManager am ;
	
	AlertDialog alertDialog1;
    String[] values; //= {" English "," Hindi "," Punjabi "};
    
    
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assesment_module);
		
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		dbengine=new GskDBAdapter(AssesmentModuleActivity.this);
		Intent intentData=getIntent();
		ModuleId=intentData.getStringExtra("ModuleId");
		
		TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		imeiNum= mngr.getDeviceId().toString();
		 
		 prgDialog=new ProgressDialog(AssesmentModuleActivity.this);
		 
		if(ModuleId!=null && !TextUtils.isEmpty(ModuleId))
		{
			getAllModulesFromDatabase(ModuleId);
			sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			if(sharedPref.contains("PrvsLanguageIdRunning"))
			  {
				getAllModuleDataAgainstLanguageFromDatabase(ModuleId, sharedPref.getString("PrvsLanguageIdRunning", "0"));
			  }
		}
		
		intiAlizeFields();
				  
		sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		 
		 if(sharedPref.contains("PrvsProjectIdRunning") && sharedPref.contains("PrvsModuleIdRunning"))
		  {
			  prvs_ProjectRunning=sharedPref.getString("PrvsProjectIdRunning", "");
			  prvs_ModuleId=sharedPref.getString("PrvsModuleIdRunning", "");
			  prvs_LangID=sharedPref.getString("PrvsLanguageIdRunning", "0");
					
			  prvousLastSlideName=dbengine.getLastNameSlide(prvs_ModuleId,prvs_LangID);
			 
			  if(hmapModulesIDOnlineOffLine.containsKey(prvs_ModuleId))
				 {
					if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(prvs_ModuleId))==0) 
					{
						//isProjectDatFileExist(lpCourseNameCurrent, hmapSlideInfoCurrent,ModuleId,prvs_LangID);
						 isProjectDatFileExist(prvs_ProjectRunning, dbengine.fetchSlideInf(prvs_ModuleId,prvs_LangID),prvs_ModuleId,prvs_LangID);
					}
				
				 }
			 
			  
		  }

			
		
		/*  dir12345=   Environment.getExternalStorageDirectory();
         file12345=new File(dir12345,"/iOSPlayer/localsettings.dat");
 

          fileIos=new File("/sdcard/iOSPlayer/localsettings.dat");
       

         String linenew = "";

         String line="";




         if (fileIos.exists()) {
             StringBuilder text = new StringBuilder();
             try {
                 BufferedReader br = new BufferedReader(new FileReader(fileIos));

                 while ((line = br.readLine()) != null) {

                     text.append(line);
                     text.append('\n');
                     Toast.makeText(AssesmentModuleActivity.this, text.toString(), Toast.LENGTH_LONG).show();

                 }
                 linenew=text.toString();
                 
                 hmapProjectName_ID=dbengine.fetchAllProjectName_ID();
                 if(hmapProjectName_ID!=null && hmapProjectName_ID.size()>0)
                 {int index=0;
                	 for(Map.Entry<String, String> entry:hmapProjectName_ID.entrySet()){
                		 if(linenew.indexOf(entry.getKey().toString().trim())!=-1){
                			 
                			if(isProjectDatFileExist(entry.getKey().toString().trim(), dbengine.fetchSlideInf(entry.getValue().toString())))
                			{
                				 linenew= linenew.replace(entry.getKey().toString().trim(),lpCourseNameCurrent );
                				 Toast.makeText(AssesmentModuleActivity.this, "AfterChange ="+linenew.toString(), Toast.LENGTH_LONG).show();
                			}
                			
                		 }
                		 
                	 }
                	 
                	 
                 }
                 
                 
                 
                   //
             } catch (Exception e) {
                 // TODO: handle exception
             }
             // Toast.makeText(this, text, Toast.LENGTH_LONG).show();


             try {
                 String BlankMe="";
                 FileOutputStream fOut1 = new FileOutputStream (new File(file12345.toString()),true);//,MODE_APPEND);//, MODE_WORLD_READABLE);
                 fOut1.write(BlankMe.getBytes());
                 fOut1.close();
                 Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
             } catch (Exception e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
             
             try {
                 FileOutputStream fOut =new FileOutputStream (new File(file12345.toString()),true);// openFileOutput(file123, MODE_APPEND);
                 fOut.write(linenew.getBytes());
                 fOut.close();
                 Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
             } catch (Exception e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
         }
        

		*/
		
		
		
		
	}
	private void getAllModuleDataAgainstLanguageFromDatabase(String moduleId,String LanguageID) 
	{
		hmapSlideInfoCurrent=dbengine.fetchSlideInf(moduleId,LanguageID);
		lpCourseNameCurrent=dbengine.fetchModuleNameId(moduleId,LanguageID);
		lastSlideName=dbengine.getLastNameSlide(moduleId,LanguageID);
		
	}
	private void getAllModulesFromDatabase(String moduleId) 
	{
		
		hmapLanguageIDColumnNameMap.put("1", "PDAEnglishCoursePath");
		hmapLanguageIDColumnNameMap.put("2", "PDAHIndiCoursePath");
		hmapLanguageIDColumnNameMap.put("3", "PDATeleguCoursePath");
		hmapLanguageIDColumnNameMap.put("4", "PDATamilCoursePath");
		hmapLanguageIDColumnNameMap.put("5", "PDAMalyalamCoursePath");
		hmapLanguageIDColumnNameMap.put("6", "PDAKannadaCoursePath");
		hmapLanguageIDColumnNameMap.put("7", "PDAMarathiCoursePath");
		hmapLanguageIDColumnNameMap.put("8", "PDAGujratiCoursePath");
		hmapLanguageIDColumnNameMap.put("9", "PDABangaliCoursePath");
		hmapLanguageIDColumnNameMap.put("10", "PDAAssamiesCoursePath");
		hmapLanguageIDColumnNameMap.put("11", "PDAOdiyaCoursePath");
		
		
		
		hmapModulesData=dbengine.getSingleModulesData(moduleId);
		
		hmapAllModuleIdName=dbengine.getAllModuleIDName();
		
		hmapModulesLanguageData=dbengine.fetchtblModuleLanguageMstr(moduleId);
		
		hmapModulesIDOnlineOffLine=dbengine.fetchtblModuleOnlineOfflineMstr(moduleId);
		
		hmapLanguageData=dbengine.fetchtblLanguageMstr();
		values=new String[hmapModulesLanguageData.size()];
	
		
		/*hmapModulesData=dbengine.getSingleModulesData(moduleId);
		hmapSlideInfoCurrent=dbengine.fetchSlideInf(moduleId);
		lpCourseNameCurrent=dbengine.fetchModuleNameId(moduleId);
		lastSlideName=dbengine.getLastNameSlide(moduleId);
		hmapAllModuleIdName=dbengine.getAllModuleIDName();
		
		hmapModulesLanguageData=dbengine.fetchtblModuleLanguageMstr(moduleId);
		hmapLanguageData=dbengine.fetchtblLanguageMstr();
		values=new String[hmapModulesLanguageData.size()];*/
				
		
		/*for(int i=0;i<hmapModulesLanguageData.size();i++)
		{
			values[i]=hmapLanguageData.get(hmapModulesLanguageData.get);
		}*/
		
		if(hmapModulesLanguageData!=null)
		{ 
			int i=0;
			for(Map.Entry<String, String> entry:hmapModulesLanguageData.entrySet())
			{
				values[i]=hmapLanguageData.get(entry.getKey());
				i++;
			}
		}
		
		
	}
	
	private void intiAlizeFields() 
	{
		
		txt_status=(TextView) findViewById(R.id.txt_status);
		txt_sub_heading=(TextView) findViewById(R.id.txt_sub_heading);
		txtVw_ModuleHeading=(TextView) findViewById(R.id.txtVw_ModuleHeading);
		txt_short_descrp=(TextView) findViewById(R.id.txt_short_descrp);
		img_Module=(ImageView) findViewById(R.id.img_Module);
		
		img_back=(ImageView) findViewById(R.id.img_back);
		ll_module_Desc=(LinearLayout) findViewById(R.id.ll_module_Desc);
		txt_comp_incomp=(TextView) findViewById(R.id.txt_comp_incomp);
		txt_Assmnt=(TextView) findViewById(R.id.txt_Assmnt);
		
		
		/*txt_Status=(TextView) findViewById(R.id.txt_Status);
		txt_Status.setTag(ModuleId);
		txt_Status.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(AssesmentModuleActivity.this,ModuleQuestionAnswerActivity.class);
				intent.putExtra("ModuleId", ModuleId);
				startActivity(intent);
				finish();	
				
				
			}
		});*/
		
		txt_short_descrp.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) {
				//Fetch LanguageID and pass It As Parameter
				if(values.length>1)
				{
					
					CreateAlertDialogWithRadioButtonGroup(ModuleId);

					
				}
				else
				{
				//String LanguageID=hmapModulesLanguageData.get(ModuleId);
				 String LanguageID=	getSelectedKey(hmapModulesLanguageData,ModuleId);
					 if(hmapModulesIDOnlineOffLine.containsKey(ModuleId))
					 {
						if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(ModuleId))==1) 
						{
								String strOnlineUrlToOpen=dbengine.fngetUrlLinkToOpen(ModuleId,LanguageID);
								String LPCourseCatID=dbengine.fnGetLpCourseCatID(ModuleId);
								if(!TextUtils.isEmpty(strOnlineUrlToOpen) && !strOnlineUrlToOpen.equals("error"))
								{
									//Check Internet and set alert msg accordingly and call web view
									if(isOnline()==true)
									{
										//Open Web View
										Intent intent=new Intent(AssesmentModuleActivity.this,WebViewActivity.class);
										intent.putExtra("WebURLToOpen", strOnlineUrlToOpen);
										intent.putExtra("ModuleID", ModuleId);
										intent.putExtra("LangugaeID", LanguageID);
										intent.putExtra("LPCourseCatID", LPCourseCatID);
										startActivity(intent);
									}
									else
									{
										alert("Alert", "No internet connectivity found!");
									}
								}
								else
								{
									alert("Alert", "Not able to open URL!");
								}
						}
						else
						{
							modulFolderName=dbengine.fetchModuleDownloaded(ModuleId,LanguageID);
							getAllModuleDataAgainstLanguageFromDatabase(ModuleId,LanguageID);
							strModuleCurentSelecteLangID=LanguageID;
							prvs_LangID=LanguageID;
							strModuleCurentSelecteLangID=LanguageID;
							if(!TextUtils.isEmpty(modulFolderName) && !modulFolderName.equals("error"))
							{
								try {
									deleteViewdXml("iOSPlayer/"+"viewed.xml");
									copyFile("/sdcard/iOSPlayer/"+modulFolderName.split(Pattern.quote("."))[0]+"/viewed.xml","/sdcard/iOSPlayer/"+"viewed.xml");
									String flag=dbengine.fngetTableStartEndTime(ModuleId,LanguageID);
									
									
									if(flag.equals("0")){
										Calendar c = Calendar.getInstance();
										SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
										String strDate = sdf.format(c.getTime());
										
										 Calendar calendar = Calendar.getInstance();
										 calendar.setTimeInMillis(System.currentTimeMillis()+5*60*1000);
										 SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
											String endDate = sdf2.format(calendar.getTime());
										
										dbengine.open();
										dbengine.saveStartTime(ModuleId, strDate,endDate,LanguageID);
										dbengine.close();
								}} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							/*	final Intent intentDeviceTest = new Intent("android.intent.action.MAIN");                
								intentDeviceTest.setComponent(new  ComponentName("air.com.articulate.articulatemobileplayer","com.adobe.air.AndroidActivityWrapper"));
								startActivity(intentDeviceTest);*/
								killPackageProcesses();
								
								
							}
							else
							{
								alert("Alert", "There is no Project for this module!");
							}
							
						}
					 }
					 else
						{
							alert("Alert", "There is no Project for this module!");
						}
				 
				 
				
				}
			
				
			}
		});
		img_Module.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				if(values.length>1)
				{
					
					CreateAlertDialogWithRadioButtonGroup(ModuleId);

					
				}
				else
				{
					//String ModuleLangID=hmapModulesLanguageData.get(ModuleId);
					 String ModuleLangID=	getSelectedKey(hmapModulesLanguageData,ModuleId);	
					 
					 
					 if(hmapModulesIDOnlineOffLine.containsKey(ModuleId))
					 {
						if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(ModuleId))==1) 
						{
								String strOnlineUrlToOpen=dbengine.fngetUrlLinkToOpen(ModuleId,ModuleLangID);
								String LPCourseCatID=dbengine.fnGetLpCourseCatID(ModuleId);
								if(!TextUtils.isEmpty(strOnlineUrlToOpen) && !strOnlineUrlToOpen.equals("error"))
								{
									//Check Internet and set alert msg accordingly and call Web View
									if(isOnline()==true)
									{
										//Open Web View
										Intent intent=new Intent(AssesmentModuleActivity.this,WebViewActivity.class);
										intent.putExtra("WebURLToOpen", strOnlineUrlToOpen);
										intent.putExtra("ModuleID", ModuleId);
										intent.putExtra("LangugaeID", ModuleLangID);
										intent.putExtra("LPCourseCatID", LPCourseCatID);
										startActivity(intent);
									}
									else
									{
										alert("Alert", "No internet connectivity found!");
									}
								}
								else
								{
									alert("Alert", "Not able to open URL!");
								}
						}
						else
						{
							modulFolderName=dbengine.fetchModuleDownloaded(ModuleId,ModuleLangID);
							getAllModuleDataAgainstLanguageFromDatabase(ModuleId,ModuleLangID);
							 prvs_LangID=ModuleLangID;
							 strModuleCurentSelecteLangID=ModuleLangID;
							if(!TextUtils.isEmpty(modulFolderName) && !modulFolderName.equals("error"))
							{
								try {
									deleteViewdXml("iOSPlayer/"+"viewed.xml");
									copyFile("/sdcard/iOSPlayer/"+modulFolderName.split(Pattern.quote("."))[0]+"/viewed.xml","/sdcard/iOSPlayer/"+"viewed.xml");
									
									String flag=		dbengine.fngetTableStartEndTime(ModuleId,ModuleLangID);
									
							
							if(flag.equals("0")){
								Calendar c = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
								String strDate = sdf.format(c.getTime());
								
								 Calendar calendar = Calendar.getInstance();
								 calendar.setTimeInMillis(System.currentTimeMillis()+5*60*1000);
								 SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
									String endDate = sdf2.format(calendar.getTime());
								
								dbengine.open();
								dbengine.saveStartTime(ModuleId, strDate,endDate,ModuleLangID);
								dbengine.close();
								
								
							}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							/*	final Intent intentDeviceTest = new Intent("android.intent.action.MAIN");                
								intentDeviceTest.setComponent(new  ComponentName("air.com.articulate.articulatemobileplayer","com.adobe.air.AndroidActivityWrapper"));
								startActivity(intentDeviceTest);*/
								killPackageProcesses();
							}
							else
							{
								alert("Alert", "There is no Project for this module!");
							}
						}
					 }
					 else
					 {
						 alert("Alert", "There is no Project for this module!"); 
					 }
				}
			}
		});
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(AssesmentModuleActivity.this,ModulesActivity.class);
				startActivity(intent);
				finish();
				
				
			}
		});
		CourseCatID=hmapModulesData.get(ModuleId).toString().split(Pattern.quote("^"))[0];
		moduleHeading=hmapModulesData.get(ModuleId).toString().split(Pattern.quote("^"))[3];
		moduleSubHeading=hmapModulesData.get(ModuleId).toString().split(Pattern.quote("^"))[1];
		moduleDesc=hmapModulesData.get(ModuleId).toString().split(Pattern.quote("^"))[4];
		txtVw_ModuleHeading.setText(moduleHeading);
		txt_sub_heading.setText(moduleSubHeading);
		txt_short_descrp.setText(moduleDesc);
		filePathStrings =hmapModulesData.get(ModuleId).toString().split(Pattern.quote("^"))[10]; ;
		Bitmap bmp = BitmapFactory.decodeFile(filePathStrings);
		img_Module.setImageBitmap(bmp);
	}
	
	 public void CreateAlertDialogWithRadioButtonGroup(final String curntModuleID){


	        AlertDialog.Builder builder = new AlertDialog.Builder(AssesmentModuleActivity.this);

	        builder.setTitle("Select Your Language");

	        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int item) 
	            {

	            	
	               if(item!=-1)
	               {
	            	   String curntLang=values[item];
                   //	String langPrefer=hmapLanguageData.get(curntLang);
	            	   String langPrefer=	getSelectedKey(hmapLanguageData,curntLang);
                       //Toast.makeText(AssesmentModuleActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                      
                       moduleLanguageBased(curntModuleID,langPrefer);
	               }
	                  
	            	   	
	                    
	                
	                alertDialog1.dismiss();
	            }
	        });
	        alertDialog1 = builder.create();
	        alertDialog1.show();

	    }
	 
	 
	 public void moduleLanguageBased(String curntModuleID,String langPrefer)
	 {
		 strModuleCurentSelecteLangID=langPrefer;
		 if(hmapModulesIDOnlineOffLine.containsKey(curntModuleID))
		 {
			if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(curntModuleID))==1) 
			{
				String strOnlineUrlToOpen=dbengine.fngetUrlLinkToOpen(curntModuleID,langPrefer);
				String LPCourseCatID=dbengine.fnGetLpCourseCatID(curntModuleID);
				if(!TextUtils.isEmpty(strOnlineUrlToOpen) && !strOnlineUrlToOpen.equals("error"))
				{
					// cechk for internet availabilty and show proper msg and the call Cal web View Here
					if(isOnline()==true)
					{
						//Open Web View
						
						Intent intent=new Intent(AssesmentModuleActivity.this,WebViewActivity.class);
						intent.putExtra("WebURLToOpen", strOnlineUrlToOpen);
						intent.putExtra("ModuleID", curntModuleID);
						intent.putExtra("LangugaeID", langPrefer);
						intent.putExtra("LPCourseCatID", LPCourseCatID);
						startActivity(intent);
						
					}
					else
					{
						alert("Alert", "No internet connectivity found!");
					}
				}
			}
			else
			{
				 modulFolderName=dbengine.fetchModuleDownloaded(curntModuleID,langPrefer);
				 getAllModuleDataAgainstLanguageFromDatabase(curntModuleID,langPrefer);
				 strModuleCurentSelecteLangID=langPrefer;
					if(!TextUtils.isEmpty(modulFolderName) && !modulFolderName.equals("error"))
					{
						try {
							deleteViewdXml("iOSPlayer/"+"viewed.xml");
							copyFile("/sdcard/iOSPlayer/"+modulFolderName.split(Pattern.quote("."))[0]+"/viewed.xml","/sdcard/iOSPlayer/"+"viewed.xml");
							
							String flag=		dbengine.fngetTableStartEndTime(curntModuleID,langPrefer);
							
					
					if(flag.equals("0")){
						Calendar c = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						String strDate = sdf.format(c.getTime());
						
						 Calendar calendar = Calendar.getInstance();
						 calendar.setTimeInMillis(System.currentTimeMillis()+5*60*1000);
						 SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							String endDate = sdf2.format(calendar.getTime());
						
						dbengine.open();
						dbengine.saveStartTime(curntModuleID, strDate,endDate,langPrefer);
						dbengine.close();
						
						
					}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					/*	final Intent intentDeviceTest = new Intent("android.intent.action.MAIN");                
						intentDeviceTest.setComponent(new  ComponentName("air.com.articulate.articulatemobileplayer","com.adobe.air.AndroidActivityWrapper"));
						startActivity(intentDeviceTest);*/
						killPackageProcesses();
					}
					else
					{
						alert("Alert", "There is no Project for this module!");
					}
			}
		 }
		 else
			{
				alert("Alert", "There is no Project for this module!");
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

	
	 public void alert(String title,String message)
	 {
		 AlertDialog alertDialog = new AlertDialog.Builder(AssesmentModuleActivity.this).create();

         // Setting Dialog Title
         alertDialog.setTitle(title);

       // Setting Dialog Message
        alertDialog.setMessage(message);

       // Setting Icon to Dialog
       alertDialog.setIcon(R.drawable.error_ico);

      // Setting OK Button
      alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
      {
         public void onClick(DialogInterface dialog, int which) 
         {
         
        	 existArticulate=true;
         }
 });

 // Showing Alert Message
 alertDialog.show();
	 }
	 
	 
	 public void copyFile(String src,String dest) throws IOException 
	 {
		 InputStream in = new FileInputStream(new File(src));
		  OutputStream out = new FileOutputStream(new File(dest));

		    // Transfer bytes from in to out
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		    
		   // deleteViewdXml("/sdcard/iOSPlayer/"+modulFolderName.split(Pattern.quote("."))[0]+"/viewed.xml");
		}
	 
	 public void deleteViewdXml(String file_dj_path)
	 {
		 File dir=   Environment.getExternalStorageDirectory();
	     File fdelete=new File(dir,file_dj_path);
		// File fdelete = new File(file_dj_path);
	        if (fdelete.exists()) {
	            if (fdelete.delete()) {
	                Log.e("-->", "file Deleted :" + file_dj_path);
	                callBroadCast();
	            } else {
	                Log.e("-->", "file not Deleted :" + file_dj_path);
	            }
	        }  
	        
	        
	 }
	 
	 public void callBroadCast() 
	 {
	        if (Build.VERSION.SDK_INT >= 14)
	        {
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
	 
	 
	 private void getDataFromBackened()
	 {
		 
		   try
		   {
		
			new AsyncTask<Void, Void, String>()
			{
				
				protected void onPreExecute()
				{
					  prgDialog.setCanceledOnTouchOutside(false);
					  prgDialog.setMessage("Checking Status...");
				      prgDialog.show();
				};
				
				@Override
				protected String doInBackground(Void... params) 
				{
		
					
					try
					{
						 service_worker=service_worker.getGSKModuleStatusOnline(AssesmentModuleActivity.this,UserId,LoginId,ModuleId);
						if(!service_worker.director.trim().equals("1"))
						{
							 
					        if(chkFlgForErrorToCloseApp==0)
					          {
					            chkFlgForErrorToCloseApp=1;
					          }
					    }
						
					} 
				catch (Exception e) 
				   {
						 service_worker.director="100";
					}
					return null;
				}
				
				protected void onPostExecute(String result)
				{
					prgDialog.dismiss();
					
					if(chkFlgForErrorToCloseApp==1)   // if Webservice showing exception or not excute complete properly
					{
						
						
							chkFlgForErrorToCloseApp=0;
						
							/* if(service_worker.director.toString().trim().equals("100"))
							{
								 alertDialogSingle(AssesmentModuleActivity.this,"Connection Alert","Your device has no Data Connection. \n Please ensure Internet is accessible to Continue!");

							}
							else if(service_worker.director.toString().trim().equals("200"))
							{
								alertDialogSingle(AssesmentModuleActivity.this,"Alert","Error while retrieving data from server!\n Please try again");
								
							}
							
							else
							{
								alertDialogSingle(AssesmentModuleActivity.this,"Alert","Error while Retrieving Data!\n Please try again");
								
							}*/
			               
					}
					else
					{
						
						    //  LpStatus values 
						    //  1=Started but not completed, 
						    //  2=Completed and done,
						    //  3=Completed but repeat required, 
						    //  4=Expired
							//  and default is 1.
							//  else if no record return then it is 
							//   0, =Not Started
						
						// service_worker=service_worker.getGSKModuleStatusOnline(AssesmentModuleActivity.this,
						//UserId,LoginId,ModuleId);
						//dbengine.updateStatusIntblCheckOnlineModuleStatus(UserId,LoginId,ModuleId,ModulesStatus);
						
						dbengine.savetblCheckOnlineModuleStatus(UserId,LoginId,ModuleId,ModulesStatus.trim(),LPEndDate);
						
						
						if(ModulesStatus.equals("2"))
						{
					        txt_print_certificate.setVisibility(View.VISIBLE);
						}
						else
						{
							 txt_print_certificate.setVisibility(View.GONE);
						}
						
						
						 try
						  {
							  flgTestMap=dbengine.fetchflgTestMapFromtblCheckOnlineAssessmentStatus(UserId,LoginId,ModuleId);
							  AssessmentStatus=dbengine.fetchStatusFromtblCheckOnlineAssessmentStatus(UserId,LoginId,ModuleId);
						  }
						  catch(Exception e)
						  {
							  
						  }
						if(ModulesStatus.equals("1"))
						{
							txt_status.setText("Status : Started but not completed");
							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
							if(flgTestMap.equals("0"))
							{
								txt_comp_incomp.setText(NoTestMsg);
							}
							else if(flgTestMap.equals("1"))
							{
								if(AssessmentStatus.equals("0"))
								{
									txt_comp_incomp.setText("Not Started");
								}
								else if(AssessmentStatus.equals("1"))
								{
									txt_comp_incomp.setText("Incomplete");
								}
								else if(AssessmentStatus.equals("2"))
								{
									txt_comp_incomp.setText("Complete");
									txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
								}
							}
							
							
						}
						
						else if(ModulesStatus.equals("2"))
						{
							txt_status.setText("Status : Completed and done");
							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
							
							if(flgTestMap.equals("0"))
							{
								txt_comp_incomp.setText(NoTestMsg);
							}
							else if(flgTestMap.equals("1"))
							{
								if(AssessmentStatus.equals("0"))
								{
									txt_comp_incomp.setText("Not Started");
									
		    						txt_Assmnt.setEnabled(true);
								}
								else if(AssessmentStatus.equals("1"))
								{
									txt_comp_incomp.setText("Incomplete");
									
									
		    						txt_Assmnt.setEnabled(true);
								}
								else if(AssessmentStatus.equals("2"))
								{
									txt_comp_incomp.setText("Complete");
									txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
									txt_Assmnt.setEnabled(false);
								}
							}
							
						}
						else if(ModulesStatus.equals("3"))
						{
							txt_status.setText("Status : Completed but repeat required");
							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
							if(flgTestMap.equals("0"))
							{
								txt_comp_incomp.setText(NoTestMsg);
							}
							else if(flgTestMap.equals("1"))
							{
								if(AssessmentStatus.equals("0"))
								{
									txt_comp_incomp.setText("Not Started");
									
		    						txt_Assmnt.setEnabled(true);
								}
								else if(AssessmentStatus.equals("1"))
								{
									txt_comp_incomp.setText("Incomplete");
									
		    						txt_Assmnt.setEnabled(true);
								}
								else if(AssessmentStatus.equals("2"))
								{
									txt_comp_incomp.setText("Complete");
									txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
									txt_Assmnt.setEnabled(false);
								}
							}
							
						}
						else if(ModulesStatus.equals("4"))
						{
							txt_status.setText("Status : Expired");
							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
							if(flgTestMap.equals("0"))
							{
								txt_comp_incomp.setText(NoTestMsg);
							}
							else if(flgTestMap.equals("1"))
							{
								if(AssessmentStatus.equals("0"))
								{
									txt_comp_incomp.setText("Not Started");
								}
								else if(AssessmentStatus.equals("1"))
								{
									txt_comp_incomp.setText("Incomplete");
								}
								else if(AssessmentStatus.equals("2"))
								{
									txt_comp_incomp.setText("Complete");
									txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
								
								}
							}
							
						}
						else if(ModulesStatus.equals("0"))
						{
							txt_status.setText("Status : Not Started");
							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
							 txt_comp_incomp.setText("Not Started");
							
						}
						
					}
				};
			}.execute();
			
	 }
	   catch(Exception e)
	   {
		   
	   }
		}
	 
		public static void alertDialogSingle(final Activity activity,String title,String msg)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

			
			alertDialog.setTitle(title);

			// Setting Dialog Message
			alertDialog.setMessage(msg);

		
			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			{
		         public void onClick(DialogInterface dialog, int which) 
		         {
				
					dialog.dismiss();
				 }
	         });

	// Showing Alert Message
	alertDialog.show();
		}
	 
	 @Override
	protected void onResume() 
	 {
		// TODO Auto-generated method stub
		super.onResume();
		service_worker=new ServiceWorker();
		
	
		
		txt_print_certificate=(TextView)findViewById(R.id.txt_print_certificate);
		
		
		String TestIDAndDescr=dbengine.fnfetchTestIDAndDescrBasedOnCourseMainID(Integer.parseInt(ModuleId));
 		String TestID=TestIDAndDescr.split(Pattern.quote("^"))[0];
 		
 		int TotalNoOfQuestionFrombackend=dbengine.getTotalNoOfQstID(Integer.parseInt(TestID));
 		
 		int TotalNoOfQuestionuserAttand=dbengine.getTotalNoOfQstIDUserAttend(Integer.parseInt(TestID));
 		
 		if(TotalNoOfQuestionFrombackend!=0)
 		{
 		
	 		if(TotalNoOfQuestionFrombackend==TotalNoOfQuestionuserAttand)
	 		{
	 			txt_print_certificate.setVisibility(View.VISIBLE);
	 		}
	 		else
	 		{
	 			txt_print_certificate.setVisibility(View.GONE);
	 			
	 		}
 		}
 		else
 		{
 			txt_comp_incomp.setText(NoTestMsg);
 			txt_print_certificate.setVisibility(View.GONE);
 		}

		
		txt_print_certificate.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				
				
				 final LayoutInflater inflater = AssesmentModuleActivity.this.getLayoutInflater();
	 			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(AssesmentModuleActivity.this);
	 			    final View dialogView = inflater.inflate(R.layout.custom_alert_certificate, null);
			     
			      alertDialog.setTitle("Info");
			        alertDialog.setIcon(R.drawable.info_ico);
			        alertDialog.setCancelable(false);
			      
			       // alertDialog.setMessage("You already pass the test.");
			        alertDialog.setView(dialogView);
			        
			        
			        final TextView txt_userName = (TextView) dialogView.findViewById(R.id.txt_userName);
			        
			        String UserFullName=dbengine.fnfetchUserFullNameFromtblUserLoginMstr();
				    txt_userName.setText(UserFullName);
			        
			        
			    /*    long  syncTIMESTAMP = System.currentTimeMillis();
					Date dateobj = new Date(syncTIMESTAMP);
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
					String fullFileName1 = df.format(dateobj);
			        */
				    String CompleteDate="0";
				    String moduleName="0";
				    if(ModulesStatus.equals("2"))
				    {
				    	    moduleName=dbengine.fnfetchModuleNametblUserModuleMstr(Integer.parseInt(ModuleId));
				    	 
				    	    /*long  syncTIMESTAMP = System.currentTimeMillis();
							Date dateobj = new Date(syncTIMESTAMP);
							SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
							CompleteDate = df.format(dateobj);*/
				    	    CompleteDate = LPEndDate;
				    }
				    else
				    {
				    	 CompleteDate =dbengine.fnfetchModuleCompleteDate(Integer.parseInt(ModuleId));
					     moduleName=dbengine.fnfetchModuleContentNameBasedmoduleID(Integer.parseInt(ModuleId));
					        
				    }
			        
			        
			        String abcd="has successfully completed the "+moduleName +" module on "+CompleteDate;
			        
			        final TextView txt_moduleAndDateInfo = (TextView) dialogView.findViewById(R.id.txt_moduleAndDateInfo);
			        txt_moduleAndDateInfo.setText(abcd);
			        
			 
			        // On pressing Settings button
			        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
			        {
			            public void onClick(DialogInterface dialog,int which)
			            {
			            	dialog.dismiss();
			            }
			        });
			 
			        // Showing Alert Message
			        alertDialog.show();
	 		
	 		
				
			
			}
		});
		
		isModulePPtCompleted=dbengine.isModuleCompleted(ModuleId,strModuleCurentSelecteLangID);
		if(sharedPref.contains("PrvsModuleIdRunning"))
		{
			

			 
			if(!sharedPref.getString("PrvsModuleIdRunning","").equals(ModuleId) )
			{
				
				if(hmapModulesIDOnlineOffLine.containsKey(ModuleId))
				 {
					if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(ModuleId))==0) 
					{
						deleteViewdXml("iOSPlayer/"+"viewed.xml");
					}
				
				 }
				
			}
		}
	
		
		if(!TextUtils.isEmpty(lpCourseNameCurrent) && !lpCourseNameCurrent.equals("error"))
		{
			
			if(hmapModulesIDOnlineOffLine.containsKey(ModuleId))
			 {
				if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(ModuleId))==0) 
				{
					isProjectDatFileExist(lpCourseNameCurrent, hmapSlideInfoCurrent,ModuleId,prvs_LangID);
				}
			
			 }
			
			
		}
		
		try
		{
			if(ModuleId!=null && !TextUtils.isEmpty(ModuleId))
			{
				String CheckModuleOnline=dbengine.fetchModuleDownloaded(ModuleId);
			if(CheckModuleOnline.equals("1"))
				{
					
				 String UserNameandContact=dbengine.fnGetUserNameContact();
				   
				 String userName=UserNameandContact.split(Pattern.quote("^"))[0];
				 String ContactNo=UserNameandContact.split(Pattern.quote("^"))[1];
				 LoginId=UserNameandContact.split(Pattern.quote("^"))[2];
				 UserId=UserNameandContact.split(Pattern.quote("^"))[3];
				   
				String Status=dbengine.fetchOnlineStatus(UserId,LoginId,ModuleId);
				LPEndDate=dbengine.fetchOnlineLPEndDate(UserId,LoginId,ModuleId);
				if(Status.equals("0") || Status.equals("") || Status.equals(" "))
						{
					       if(ModulesStatus.equals("0"))
					          {
						         txt_status.setText("Status : Not Started");
						         txt_status.setTextColor(getResources().getColor(R.color.status_complete));
						         txt_comp_incomp.setText("Not Started");
						      }
						}
			
				if(Status.equals("1") || Status.equals("2")  || Status.equals("3") || Status.equals("4"))
				{

					
				    //  LpStatus values 
				    //  1=Started but not completed, 
				    //  2=Completed and done,
				    //  3=Completed but repeat required, 
				    //  4=Expired
					//  and default is 1.
					//  else if no record return then it is 
					//   0, =Not Started
					
					    //dbengine.open();
						dbengine.savetblCheckOnlineModuleStatus(UserId,LoginId,ModuleId,Status,LPEndDate);
						//dbengine.close();
					
				
				  try
				  {
					  flgTestMap=dbengine.fetchflgTestMapFromtblCheckOnlineAssessmentStatus(UserId,LoginId,ModuleId);
					  AssessmentStatus=dbengine.fetchStatusFromtblCheckOnlineAssessmentStatus(UserId,LoginId,ModuleId);
				  }
				  catch(Exception e)
				  {
					  
				  }
				
				if(ModulesStatus.equals("1"))
				{
					txt_status.setText("Status : Started but not completed");
					txt_status.setTextColor(getResources().getColor(R.color.status_complete));
					if(flgTestMap.equals("0"))
					{
						txt_comp_incomp.setText(NoTestMsg);
					}
					else if(flgTestMap.equals("1"))
					{
						if(AssessmentStatus.equals("0"))
						{
							txt_comp_incomp.setText("Not Started");
						}
						else if(AssessmentStatus.equals("1"))
						{
							txt_comp_incomp.setText("Incomplete");
						}
						else if(AssessmentStatus.equals("2"))
						{
							txt_comp_incomp.setText("Complete");
							txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
						}
					}
					
					
				}
				else if(ModulesStatus.equals("2"))
				{
					txt_status.setText("Status : Completed and done");
					txt_status.setTextColor(getResources().getColor(R.color.status_complete));
					
					//txt_comp_incomp.setText("Completed and done");
					//txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
					if(flgTestMap.equals("0"))
					{
						txt_comp_incomp.setText(NoTestMsg);
					}
					else if(flgTestMap.equals("1"))
					{
						if(AssessmentStatus.equals("0"))
						{
							txt_comp_incomp.setText("Not Started");
							
    						txt_Assmnt.setEnabled(true);
						}
						else if(AssessmentStatus.equals("1"))
						{
							txt_comp_incomp.setText("Incomplete");
							
    						txt_Assmnt.setEnabled(true);
						}
						else if(AssessmentStatus.equals("2"))
						{
							txt_comp_incomp.setText("Complete");
							txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
							txt_Assmnt.setEnabled(false);
						}
					}
					
				}
				else if(ModulesStatus.equals("3"))
				{
					txt_status.setText("Status : Completed but repeat required");
					txt_status.setTextColor(getResources().getColor(R.color.status_complete));
					
					if(flgTestMap.equals("0"))
					{
						txt_comp_incomp.setText(NoTestMsg);
					}
					else if(flgTestMap.equals("1"))
					{
						if(AssessmentStatus.equals("0"))
						{
							txt_comp_incomp.setText("Not Started");
							
    						txt_Assmnt.setEnabled(true);
						}
						else if(AssessmentStatus.equals("1"))
						{
							txt_comp_incomp.setText("Incomplete");
							
    						txt_Assmnt.setEnabled(true);
						}
						else if(AssessmentStatus.equals("2"))
						{
							txt_comp_incomp.setText("Complete");
							txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
							txt_Assmnt.setEnabled(false);
						}
					}
					
				}
				else if(ModulesStatus.equals("4"))
				{
					txt_status.setText("Status : Expired");
					txt_status.setTextColor(getResources().getColor(R.color.status_complete));
					
					if(flgTestMap.equals("0"))
					{
						txt_comp_incomp.setText(NoTestMsg);
					}
					else if(flgTestMap.equals("1"))
					{
						if(AssessmentStatus.equals("0"))
						{
							txt_comp_incomp.setText("Not Started");
						}
						else if(AssessmentStatus.equals("1"))
						{
							txt_comp_incomp.setText("Incomplete");
						}
						else if(AssessmentStatus.equals("2"))
						{
							txt_comp_incomp.setText("Complete");
							txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
						}
					}
					
				}
				
				
					
					if(isOnline()==true)
					{
						getDataFromBackened();	
					}
					
				
				}
				else
				{
					if(isOnline()==true)
					{
					  getDataFromBackened();
					}
					
				}
				if(ModulesStatus.equals("2"))
				{
			        txt_print_certificate.setVisibility(View.VISIBLE);
				}
				else
				{
					 txt_print_certificate.setVisibility(View.GONE);
				}
			}
			}
		}
		catch(Exception e)
		{
			
		}
		if(ModulesStatus.equals("2"))
		{
			if(flgTestMap.equals("1"))
			{
				if(AssessmentStatus.equals("0"))
				{
					txt_comp_incomp.setText("Not Started");
					
					txt_Assmnt.setEnabled(true);
				}
				else if(AssessmentStatus.equals("1"))
				{
					txt_comp_incomp.setText("Incomplete");
					
					txt_Assmnt.setEnabled(true);
				}
				else if(AssessmentStatus.equals("2"))
				{
					txt_Assmnt.setEnabled(false);
					txt_comp_incomp.setText("Complete");
					txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
					
				}
			}
			
		}
		else
		{
			txt_Assmnt.setEnabled(false);
			if(!TextUtils.isEmpty(lpCourseNameCurrent) && !lpCourseNameCurrent.equals("error"))
			{
				
				if(hmapModulesIDOnlineOffLine.containsKey(ModuleId))
				 {
					if(Integer.parseInt(hmapModulesIDOnlineOffLine.get(ModuleId))==0) 
					{
						isProjectDatFileExist(lpCourseNameCurrent, hmapSlideInfoCurrent,ModuleId,prvs_LangID);
					}
				
				 }
				
				
			}
			
		}
		
	   	txt_Assmnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				dbengine=new GskDBAdapter(AssesmentModuleActivity.this);
				String TestIDAndDescr=dbengine.fnfetchTestIDAndDescrBasedOnCourseMainID(Integer.parseInt(ModuleId));
		 		String TestID=TestIDAndDescr.split(Pattern.quote("^"))[0];
		 		
		 		int TotalNoOfQuestionFrombackend=dbengine.getTotalNoOfQstID(Integer.parseInt(TestID));
		 		
		 		int TotalNoOfQuestionuserAttand=dbengine.getTotalNoOfQstIDUserAttend(Integer.parseInt(TestID));
		 		if(TotalNoOfQuestionFrombackend!=0)
		 		{
		 		
				 		if(TotalNoOfQuestionFrombackend==TotalNoOfQuestionuserAttand)
				 		{
		
				 			    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AssesmentModuleActivity.this);
						        alertDialog.setTitle("Info");
						        alertDialog.setIcon(R.drawable.info_ico);
						        alertDialog.setCancelable(false);
						        alertDialog.setMessage("You have already completed the test.");
						        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
						        {
						            public void onClick(DialogInterface dialog,int which)
						            {
						            	dialog.dismiss();
						            }
						        });
						        alertDialog.show();
				 		
				 		}
				 		else
				 		{
					 		Intent intent=new Intent(AssesmentModuleActivity.this,ModuleQuestionAnswerActivity.class);
							intent.putExtra("ModuleId", ModuleId);
							startActivity(intent);
							finish();
				 		}
			     }
		 		else
		 		{
		 			AlertDialog.Builder alertDialog = new AlertDialog.Builder(AssesmentModuleActivity.this);
			        alertDialog.setTitle("Info");
			        alertDialog.setIcon(R.drawable.info_ico);
			        alertDialog.setCancelable(false);
			        alertDialog.setMessage("There is no question for this Assessment.");
			        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
			        {
			            public void onClick(DialogInterface dialog,int which)
			            {
			            	dialog.dismiss();
			            }
			        });
			        alertDialog.show();
		 		}
				
			}
		});
		
		
		
	}
	 @Override
	protected void onRestart() 
	 {
		// TODO Auto-generated method stub
		super.onRestart();
		
	}
	 public boolean isProjectDatFileExist(String lpCourseName,LinkedHashMap<String, String> hmapSlideInfo,String modulIDOfCalc, String langPrefernew){

	
			String filename="iOSPlayer/"+lpCourseName+".dat";
			
			 File dir=   Environment.getExternalStorageDirectory();
		     File file=new File(dir,filename);
		     if(file.exists()){
		    	 StringBuilder text=new StringBuilder();
		    	 try {
					BufferedReader br=new BufferedReader(new FileReader(file));
					String line;
					while((line=br.readLine())!=null){
						text.append(line);
						text.append('\n');
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	 
		    	
		    	if(hmapSlideInfo!=null && hmapSlideInfo.size()>0)
		    	{
		    		for(Map.Entry<String, String> entry:hmapSlideInfo.entrySet())
		    		{
		    			slideName=entry.getKey();
		    			if(text.indexOf(slideName)!=-1)
		    			{
		    				img_start_Module=(ImageView)findViewById(R.id.img_start_Module);
		    				slideNo=entry.getValue().toString();
		    				if(lastSlideName.equals(entry.getKey().toString().trim()) ||prvousLastSlideName.equals(entry.getKey().toString().trim()))
		    				{
		    					if(ModuleId.equals(modulIDOfCalc))
		    					{
		    						Calendar c = Calendar.getInstance();
		    						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		    						String strDate = sdf.format(c.getTime());
		    						dbengine.open();
		    						dbengine.saveEndTime(ModuleId, strDate,langPrefernew);
		    						dbengine.close();
		    						img_start_Module.setVisibility(View.GONE);
		    						txt_comp_incomp=(TextView) findViewById(R.id.txt_comp_incomp);
		    						
		    						txt_Assmnt.setEnabled(true);
		    						
		    							txt_status.setText("Status : Complete");
		    							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
		    							
		    							dbengine=new GskDBAdapter(AssesmentModuleActivity.this);
										String TestIDAndDescr=dbengine.fnfetchTestIDAndDescrBasedOnCourseMainID(Integer.parseInt(ModuleId));
								 		String TestID=TestIDAndDescr.split(Pattern.quote("^"))[0];
								 		
								 		int TotalNoOfQuestionFrombackend=dbengine.getTotalNoOfQstID(Integer.parseInt(TestID));
								 		
								 		int TotalNoOfQuestionuserAttand=dbengine.getTotalNoOfQstIDUserAttend(Integer.parseInt(TestID));
								 		
								 		
								 		if(TotalNoOfQuestionFrombackend==TotalNoOfQuestionuserAttand && isModulePPtCompleted)
								 		{

		    							txt_comp_incomp.setText("Complete");
		    							txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
			    						
								 		}
								 		else
								 		{
								 			txt_comp_incomp.setText("Incomplete");
								 		}
			    					
		    						
		    						
		    						
		    						
		    						
		    					}
		    			//		Toast.makeText(this, "SlideName = "+slideName+" Slide No = "+slideNo+"Last Slide", Toast.LENGTH_LONG).show();
		    					////tblModuleSaved(moduleID text null,ModuleContentName text null,moduleSlideSequenceNo text null,isLastSlide text null,imeiNum text null);";
		    					hmapSaveDataViewModule.put(modulIDOfCalc, hmapAllModuleIdName.get(modulIDOfCalc)+"^"+slideNo+"^"+"1"+"^"+imeiNum+"^"+langPrefernew);
		    				}
		    				else
		    				{
		    					if(ModuleId.equals(modulIDOfCalc))
		    					{				
		    					if(!isModulePPtCompleted)
	    						{
		    						
		    						
		    						
		    						
	    							txt_Assmnt.setEnabled(false);
	    							
	    							img_start_Module.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending_module_btn));
		    						
				    				
	    						}
	    						else
	    						{
	    							txt_comp_incomp=(TextView) findViewById(R.id.txt_comp_incomp);
		    						txt_Assmnt=(TextView) findViewById(R.id.txt_Assmnt);
		    						img_start_Module.setVisibility(View.GONE);
	    							txt_Assmnt.setEnabled(true);
	    							txt_status.setText("Status : Complete");
	    							txt_status.setTextColor(getResources().getColor(R.color.status_complete));
	    							
	    							dbengine=new GskDBAdapter(AssesmentModuleActivity.this);
									String TestIDAndDescr=dbengine.fnfetchTestIDAndDescrBasedOnCourseMainID(Integer.parseInt(ModuleId));
							 		String TestID=TestIDAndDescr.split(Pattern.quote("^"))[0];
							 		
							 		int TotalNoOfQuestionFrombackend=dbengine.getTotalNoOfQstID(Integer.parseInt(TestID));
							 		
							 		int TotalNoOfQuestionuserAttand=dbengine.getTotalNoOfQstIDUserAttend(Integer.parseInt(TestID));
							 		
							 		
							 		if(TotalNoOfQuestionFrombackend==TotalNoOfQuestionuserAttand)
							 		{

	    							txt_comp_incomp.setText("Complete");
	    							txt_comp_incomp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_icon, 0, 0, 0);
		    						
							 		}
							 		else
							 		{
							 			txt_comp_incomp.setText("Incomplete");
							 		}
		    					
	    							
	    						}
		    				}
		    					hmapSaveDataViewModule.put(modulIDOfCalc, hmapAllModuleIdName.get(modulIDOfCalc)+"^"+slideNo+"^"+"0"+"^"+imeiNum+"^"+langPrefernew);
		    					//Toast.makeText(this, "SlideName = "+slideName+" Slide No = "+slideNo, Toast.LENGTH_LONG).show();
			    				break;
		    				}
		    				
		    			}
		    		}
		    	}
		    	
		 
		    
		     return true;
		     }
		     else {
		    	 
		    	// Toast.makeText(this, "no file found", Toast.LENGTH_LONG).show();
		    	 return false;
			
				
			}
		
	 }
	 
	 @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		String changeLangID="0";
		if(hmapSaveDataViewModule!=null)
		{
			for(Map.Entry<String, String> entry:hmapSaveDataViewModule.entrySet())
			{
				//moduleID text null,ModuleContentName text null,moduleSlideSequenceNo text null,isLastSlide text null,imeiNum text null);";
				String moduleIdToSave=entry.getKey();
				String moduleContentName=entry.getValue().split(Pattern.quote("^"))[0];
				String moduleSlideSequenceNo=entry.getValue().split(Pattern.quote("^"))[1];
				String isLastSlide=entry.getValue().split(Pattern.quote("^"))[2];
				String imeiNum=entry.getValue().split(Pattern.quote("^"))[3];
				changeLangID=entry.getValue().split(Pattern.quote("^"))[4];
		String flagg=	dbengine.fngetTableStartEndTime(ModuleId,changeLangID);
		String StartTime="0";
		String LpEndDate="0";
		if(!flagg.equals("0")){
			
			  StartTime=flagg.split(Pattern.quote("^"))[0];
			 LpEndDate=flagg.split(Pattern.quote("^"))[1];
			
		}
				dbengine.saveOrUpdateModuleData(moduleIdToSave, moduleContentName, moduleSlideSequenceNo, isLastSlide, imeiNum,StartTime,LpEndDate,String.valueOf(CommonInfo.loginId),CourseCatID,changeLangID );
				
				
			}
		}
	}
	 public void killPackageProcesses() {
		    
		    ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		    List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
		             int processid = 0;
		             
		             
		             new AsyncTask<Void, Void, List<ProcessManager.Process>>() {

		                 long startTime;

		                 @Override
		                 protected List<ProcessManager.Process> doInBackground(Void... params) {
		                     startTime = System.currentTimeMillis();
		                     return ProcessManager.getRunningApps();
		                 }

		                 @Override
		                 protected void onPostExecute(List<ProcessManager.Process> processes) {
		                     StringBuilder sb = new StringBuilder();
		                     sb.append("Execution time: ").append(System.currentTimeMillis() - startTime).append("ms\n");
		                     sb.append("Running apps:\n");
		                     for (ProcessManager.Process process : processes) {
		                         sb.append('\n').append(process.name);
		                         if(process.name.contains("air.com.articulate.articulatemobileplayer"))
		                         {
		                        	 existArticulate=false;
		                        	 
		                        	 if(sharedPref.contains("PrvsModuleIdRunning"))
		       		                   {
				       		            	  if(!sharedPref.getString("PrvsModuleIdRunning","").equals(ModuleId) || !sharedPref.getString("PrvsLanguageIdRunning","").equals(strModuleCurentSelecteLangID))
				       		            	  {
				       		            		  alert("Alert", "Another module is running in background, Please Close Articulate  From Background To Run current module");
				       		            	  }
		       		                   }
			       		              else
			       		              {
			       		            	  alert("Alert", "Another module is running in background, Please Close Articulate  From Background To Run current module"); 
			       		              }
		                        	
		                         	break;
		                         }
		                        
		                     }
		                     
		                     if(existArticulate)
		                     {
		                    	 deleteViewdXml("iOSPlayer/"+"localsettings.dat");

		 						editor = sharedPref.edit();
		 						  editor.putString("PrvsProjectIdRunning", lpCourseNameCurrent);
		 						   editor.putString("PrvsModuleIdRunning", ModuleId);
		 						  editor.putString("PrvsLanguageIdRunning", strModuleCurentSelecteLangID);
		 						   editor.commit();
		 						Intent intent = getPackageManager().getLaunchIntentForPackage("air.com.articulate.articulatemobileplayer");
		 						
		 						
		 					    startActivity(intent);	
		 					
		                     }
		                     else
		                     {

		     					if(sharedPref.contains("PrvsModuleIdRunning"))
		     					{
		     							if(sharedPref.getString("PrvsModuleIdRunning","").equals(ModuleId) && sharedPref.getString("PrvsLanguageIdRunning","").equals(strModuleCurentSelecteLangID))
		     							{
		     								Intent intent = getPackageManager().getLaunchIntentForPackage("air.com.articulate.articulatemobileplayer");
		     								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		     								
		     							   startActivity(intent);	
		     							}
		     						}
		                     }
		                    // new AlertDialog.Builder(AssesmentModuleActivity.this).setMessage(sb.toString()).show();
		                 }
		             }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		             
		             
		             
		      /* for(int i = 0; i < pids.size(); i++)
		       {
		           ActivityManager.RunningAppProcessInfo info = pids.get(i);
		           if(info.processName.equalsIgnoreCase("air.com.articulate.articulatemobileplayer")){
		              processid = info.pid;
		              existArticulate=false;
		              android.os.Process.killProcess(processid);
		              if(sharedPref.contains("PrvsModuleIdRunning"))
		              {
		            	  if(!sharedPref.getString("PrvsModuleIdRunning","").equals(ModuleId))
		            	  {
		            		  alert("Alert", "Another module is running in background, Please Close Articulate  From Background To Run current module");
		            	  }
		              }
		              else
		              {
		            	  alert("Alert", "Another module is running in background, Please Close Articulate  From Background To Run current module"); 
		              }
		             
		              break;
		           } 
		       }*/
		       
		   
		}
	 
	 public String getSelectedKey(LinkedHashMap<String, String> hmapList,String selectedValue)
	 {
		 String selectdKey="0";
		 for(Map.Entry<String, String> entry:hmapList.entrySet())
		 {
			 if(entry.getValue().equals(selectedValue))
			 {
				 selectdKey=entry.getKey();
				 break;
			 }
		 }
		 return selectdKey;
	 }
	 
}
