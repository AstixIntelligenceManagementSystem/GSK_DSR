package com.astix.gsk_dsr;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;

import com.astix.gsk_dsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GroupQuesActivity extends Activity {
	
	String tempId;
	 public  String VisitStartTS="NA";
	LinkedHashMap<String, String> hmapQuestDetail=new LinkedHashMap<String, String>();
	String[] arrayQuestGroupId;
	LinkedHashMap<String, ArrayList<String>> hmapQuestGrpIdMappedWdQuestId=new LinkedHashMap<String, ArrayList<String>>();
	LinkedHashMap<String, ArrayList<String>> hmapQuestIdMappedWdOptId=new LinkedHashMap<String, ArrayList<String>>();
	LinkedHashMap<String, String> hmapQuestOptionScore=new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> hmapQuestAnswer=new LinkedHashMap<String, String>(); //key =GrpQuesId^QuesId^OptId , value = score*weightage
	LinkedHashMap<String, String> hmapStnOfEx=new LinkedHashMap<String, String>();
	TextView txt_next;
	String questGrpId,questId,OptId;
	ImageView imgVw_alert;
	int indexQuestGrpId=0;
	LinearLayout ll_radioGroup;
	int totalScore=0;
	String  _distributor, _assessorName1, _assessorName2, _zone, _territory, _town;
	int indexQuestId=0;
	String questGrpDesc,questDescr,weightage,optionDesc;
	int sizeOfQuestIdMpdWdGrp=0;;
	ArrayList<String> listquestIdMpdWdGrp=new ArrayList<String>();
	ArrayList<String> listOptIdMpdWdQuestId=new ArrayList<String>();
	int questIdSizeMappdWdGrp=0;
	GskDBAdapter gskDatabase;
	TextView txt_ques,txtVw_name;
	RadioGroup neweRadioGroup;
	ImageView img_home;
	RadioButton selectedRadioButton;
	
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_ques);
		getQuestFromDatabase();
		txt_next=(TextView) findViewById(R.id.txt_next);
		img_home=(ImageView) findViewById(R.id.img_home);
		getIntentDataAssesment();
		tempId=genOutID();
		
		intializeField();
		img_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				confirmExitAlert();
			
			
			}
		});
		txt_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selectedId = neweRadioGroup.getCheckedRadioButtonId();
	            // find the radiobutton by returned id
				if(((TextView)v).getText().toString().trim().equals("Next"))
				{
					if(selectedId!=-1)
					{
						selectedRadioButton = (RadioButton) findViewById(selectedId);
						int score =Integer.valueOf((hmapQuestOptionScore.get(selectedRadioButton.getTag().toString())).split(Pattern.quote("^"))[1].toString());
						System.out.println("weightage = "+weightage);
						if(TextUtils.isEmpty(weightage) ||weightage.equals("") )
						{
							weightage="1";
						}
						totalScore=totalScore+(score*Integer.valueOf(weightage));
						hmapQuestAnswer.put(selectedRadioButton.getTag().toString(), String.valueOf(score*Integer.valueOf(weightage)+"^"+weightage));
			        /*    Toast.makeText(GroupQuesActivity.this,
			            		selectedRadioButton.getTag().toString()+" = "+String.valueOf(totalScore) , Toast.LENGTH_SHORT).show();
			        	*/
						if(sizeOfQuestIdMpdWdGrp>indexQuestId)
						{
							
							intializeField();
						}
						else
						{
							indexQuestGrpId++;
							if(indexQuestGrpId<arrayQuestGroupId.length)
							{
								indexQuestId=0;
								intializeField();
								
							}
							else
							{
								TextView txt_next=(TextView) findViewById(R.id.txt_next);
								txt_next.setText("Submit");
							}
						}
					}
					else
					{
						 			 Toast.makeText(GroupQuesActivity.this,"Please Select Answer to proceed" , Toast.LENGTH_SHORT).show();
					}
					
				}
				else if(((TextView)v).getText().toString().trim().equals("Submit"))
				{
					submitAlert();
					
				}
			
				
				
			
				
				
			}
		});
		
		
	}

	
	private void getIntentDataAssesment() {
		// _distributor, _assessorName1, _assessorName2, _zone, _territory, _town;
		Intent intentData=getIntent();
		
		_distributor=intentData.getStringExtra("dstrbtr_name");
		_assessorName1=intentData.getStringExtra("assessorName1");
		_assessorName2=intentData.getStringExtra("assessorName2");
		_zone=intentData.getStringExtra("zone");
		_territory=intentData.getStringExtra("territory");
		_town=intentData.getStringExtra("town");
		
	}


	private void intializeField() {
		
		txtVw_name=(TextView) findViewById(R.id.txtVw_name);
		
		imgVw_alert=(ImageView) findViewById(R.id.imgVw_alert);
		txt_ques=(TextView) findViewById(R.id.txt_ques);
		
		
		ll_radioGroup=(LinearLayout) findViewById(R.id.ll_radioGroup);
		ll_radioGroup.removeAllViews();
		if(arrayQuestGroupId!=null && arrayQuestGroupId.length>0)
		{
			questGrpId=arrayQuestGroupId[indexQuestGrpId];
			
			if(hmapQuestGrpIdMappedWdQuestId!=null && hmapQuestGrpIdMappedWdQuestId.size()>0)
			{
				if(hmapQuestGrpIdMappedWdQuestId.containsKey(questGrpId))
				{
					listquestIdMpdWdGrp=hmapQuestGrpIdMappedWdQuestId.get(questGrpId);
					sizeOfQuestIdMpdWdGrp=listquestIdMpdWdGrp.size();
					questId=listquestIdMpdWdGrp.get(indexQuestId);
					
					if(hmapStnOfEx.containsKey(questGrpId+"^"+questId))
				    {
				     imgVw_alert.setVisibility(View.VISIBLE);
				     imgVw_alert.setOnClickListener(new OnClickListener() {
				      
				      @Override
				      public void onClick(View v) {
				       
				       alert("Standards of Excellence", hmapStnOfEx.get(questGrpId+"^"+questId));
				      }
				     });
				    }
				    else
				    {
				     imgVw_alert.setVisibility(View.INVISIBLE);
				    }
					
						indexQuestId++;
					
					
					if(hmapQuestIdMappedWdOptId!=null && hmapQuestIdMappedWdOptId.size()>0)
					{
						if(hmapQuestIdMappedWdOptId.containsKey(questId))
						{
							listOptIdMpdWdQuestId=hmapQuestIdMappedWdOptId.get(questId);
							//qstnGrpID+"^"+qstnID, qstnGrpDescr+"^"+qstnDescr+"^"+weightage);
							questGrpDesc=(hmapQuestDetail.get(questGrpId+"~"+questId).toString()).split(Pattern.quote("~"))[0];
							questDescr=(hmapQuestDetail.get(questGrpId+"~"+questId).toString()).split(Pattern.quote("~"))[1];
							weightage=(hmapQuestDetail.get(questGrpId+"~"+questId).toString()).split(Pattern.quote("~"))[2];
							txtVw_name.setText(questGrpDesc);
							txt_ques.setText(Html.fromHtml(questDescr));
							if(listOptIdMpdWdQuestId!=null && listOptIdMpdWdQuestId.size()>0)
							{
								RadioGroup answerRadiGroup=new RadioGroup(GroupQuesActivity.this);
								neweRadioGroup=answerRadiGroup;
								for(int i=0;i<listOptIdMpdWdQuestId.size();i++)
								{
									optionDesc=(hmapQuestOptionScore.get(questGrpId+"^"+questId+"^"+listOptIdMpdWdQuestId.get(i))).split(Pattern.quote("^"))[0];
									if(optionDesc!=null)
									{
										RadioButton radioButton = new RadioButton(this);
										radioButton.setText(optionDesc);
										radioButton.setTag(questGrpId+"^"+questId+"^"+listOptIdMpdWdQuestId.get(i));
										radioButton.setId(Integer.valueOf(listOptIdMpdWdQuestId.get(i)));
								        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
								                RadioGroup.LayoutParams.WRAP_CONTENT,
								                RadioGroup.LayoutParams.WRAP_CONTENT);
								        answerRadiGroup.addView(radioButton, 0, layoutParams);
									}
									
								}
								
								ll_radioGroup.addView(answerRadiGroup);
							}
							
						}
					}
				}
			}
			
			
		}
			
	}


	private void getQuestFromDatabase() {
		gskDatabase=new GskDBAdapter(GroupQuesActivity.this);
		arrayQuestGroupId=gskDatabase.getQuestGrpIds();
		hmapQuestGrpIdMappedWdQuestId=gskDatabase.fetchQuestGrpIdQuestId();
		hmapQuestIdMappedWdOptId=gskDatabase.fetchQuestIdOptnId();
		hmapQuestDetail=gskDatabase.fetchQuestInfo();
		hmapQuestOptionScore=gskDatabase.fetchQuestOptionInfo();
		hmapStnOfEx=gskDatabase.getStnOfEx();
	}

	 public String genOutID()
		{
			//store ID generation <x>
			long syncTIMESTAMP = System.currentTimeMillis();
			Date dateobj = new Date(syncTIMESTAMP);
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			VisitStartTS = df.format(dateobj);
			String cxz;
					cxz = UUID.randomUUID().toString();
					/*cxz.split("^([^-]*,[^-]*,[^-]*,[^-]*),(.*)$");*/
					//System.out.println("cxz (BEFORE split): "+cxz);
					StringTokenizer tokens = new StringTokenizer(String.valueOf(cxz), "-");
					
					String val1 = tokens.nextToken().trim();
					String val2 = tokens.nextToken().trim();
					String val3 = tokens.nextToken().trim();
					String val4 = tokens.nextToken().trim();
					cxz = tokens.nextToken().trim();
					
					//System.out.println("cxz (AFTER split): "+cxz);
					
					/*TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String IMEIid = tManager.getDeviceId();*/
					String IMEIid =  CommonInfo.imei.substring(9);
					//cxz = IMEIid +"-"+cxz;
					cxz = IMEIid +"-"+cxz+"-"+VisitStartTS.replace(" ", "").replace(":", "").trim();
					//System.out.println("cxz: "+cxz);
					
					return cxz;
					//-_
		}
	 

	 public void alert(String title,String message)
	  {
	   AlertDialog alertDialog = new AlertDialog.Builder(
	     GroupQuesActivity.this).create();

	 // Setting Dialog Title
	 alertDialog.setTitle(title);

	 // Setting Dialog Message
	 alertDialog.setMessage(message);

	 // Setting OK Button
	 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	   public void onClick(DialogInterface dialog, int which) {
	   // Write your code here to execute after dialog closed
	   dialog.dismiss();
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
	 
	 
	 
	 public void submitAlert()
	 
	 {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupQuesActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Submit");

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want submit this?");

		
			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) 
				{
					
					
					
					gskDatabase.insertDistributor(tempId, _distributor, _assessorName1, _assessorName2, _zone, _territory, _town,VisitStartTS);
					gskDatabase.inserDstrbtrQuesAnsVal(tempId,hmapQuestAnswer);
					
					
					
					 File GSKXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
	 					
    				 if (!GSKXMLFolder.exists()) 
    					{
    					 GSKXMLFolder.mkdirs();
    						 
    					} 
    				 
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
		   					
		   					
		   					gskDatabase.open();
		   					gskDatabase.updateXMLRecordsSyncd();		// update syncd' records
		   					gskDatabase.close();
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
        			
        				Intent intent = new Intent(GroupQuesActivity.this, UploadXMLService.class);
        				intent.putExtra("foo", "bar");
        				startService(intent);
        			 }
					
					Intent intent=new Intent(GroupQuesActivity.this,ScoreBoardActivity.class);
					intent.putExtra("total_score", totalScore);
					startActivity(intent);
					finish();
			
				}
			});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int which) {
				// Write your code here to invoke NO event
			
				dialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();
	 }
	 
	 	public void confirmExitAlert()
	 
	 {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupQuesActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Exit");

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want to exit Assessment?");

		
			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					
					Intent intent=new Intent(GroupQuesActivity.this,AllButtonActivity.class);
					startActivity(intent);
					finish();
				}
			});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int which) {
				// Write your code here to invoke NO event
			
				dialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();
	 }
}
