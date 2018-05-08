package com.astix.gsk_dsr;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
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
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ModuleQuestionAnswerActivity extends Activity 
{
	
	public String newfullFileName;
	DatabaseAssistant DA = new DatabaseAssistant(this);
	public int CorrectAnsCounter=0;
	
	ImageView img_back,img_successfail;
	String ModuleId="0";
	public GskDBAdapter helperDb;
	public LinearLayout ll_dynamicForm;
	public String AnsControlType="2";
	
	public String TestID="0";
	public String TestDescr="NA";
	public String TestInstanceID="0";
	public String TestInsUserMapID="0";
	
	public String TotalQstn="0";
	public String PassingQstn="0";
	
	public String flgAssessmentAvailable="0";
	
	
	
	
	
	
		
	
	public LinkedHashMap<String, String> hmapQuestionIDAndQuestionTxt=new LinkedHashMap<String, String>();
	
	public LinkedHashMap<String, String> hmapQstnStatIDAndStatementDescr=new LinkedHashMap<String, String>();
	
	
	public LinkedHashMap<String, String> hmapQstnStatIDAndCorrectAns=new LinkedHashMap<String, String>();
	
	public LinkedHashMap<String, ArrayList<String>> hmapOptionValues=new LinkedHashMap<String, ArrayList<String>>();
	
	
	
	public int questionCounting=1;
	
	public String QuestionID="0";
	public String QuestionTxt="NA";
	
	public String AnswerDescr="0";
	
	public Button btn_next;
	public Button btn_submit;
	
	public TextView txt_CorrectQuestion;
	
	
	public String UserFullName="NA";
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_module_question_answer);
		
		Intent intentData=getIntent();
		ModuleId=intentData.getStringExtra("ModuleId");
		
		helperDb=new GskDBAdapter(ModuleQuestionAnswerActivity.this);
		ll_dynamicForm=(LinearLayout) findViewById(R.id.ll_dynamicForm);
		
		UserFullName=helperDb.fnfetchUserFullNameFromtblUserLoginMstr();
		
        TextView txtVw_testName=(TextView)findViewById(R.id.txtVw_testName);
        TextView txt_TotalQuestion=(TextView)findViewById(R.id.txt_TotalQuestion);
        
        
        txt_CorrectQuestion=(TextView)findViewById(R.id.txt_CorrectQuestion);
         
        
        
        
        
 		String TestIDAndDescr=helperDb.fnfetchTestIDAndDescrBasedOnCourseMainID(Integer.parseInt(ModuleId));
 		TestID=TestIDAndDescr.split(Pattern.quote("^"))[0];
 		TestDescr=TestIDAndDescr.split(Pattern.quote("^"))[1];
 		TestInstanceID=TestIDAndDescr.split(Pattern.quote("^"))[2];
 		TestInsUserMapID=TestIDAndDescr.split(Pattern.quote("^"))[3];
 		TotalQstn=TestIDAndDescr.split(Pattern.quote("^"))[4];
 		PassingQstn=TestIDAndDescr.split(Pattern.quote("^"))[5];
 		flgAssessmentAvailable=TestIDAndDescr.split(Pattern.quote("^"))[6];
 		
 		
 		if(Integer.parseInt(flgAssessmentAvailable)==0)
 		{
 			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		     
		        
		        alertDialog.setTitle("Information");
		        alertDialog.setIcon(R.drawable.error_info_ico);
		        alertDialog.setCancelable(false);
		      
		        alertDialog.setMessage("There is no Test for this Module.");
		 
		        // On pressing Settings button
		        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
		        {
		            public void onClick(DialogInterface dialog,int which)
		            {
		            	Intent intent=new Intent(ModuleQuestionAnswerActivity.this,AssesmentModuleActivity.class);
						intent.putExtra("ModuleId", ModuleId);
						startActivity(intent);
						finish();
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
 		}
 		else
 		{
 		
 		String totalresult="This test contain " + TotalQstn +" question(s), to clear the test you need to answer minimum "+ PassingQstn+" question(s) correctly.";
 		
 		String correctresult="Answered "+ CorrectAnsCounter +" question(s) correctly out of total "+questionCounting+" question(s).";
 		
 		
 		txtVw_testName.setText(TestDescr);
 		txt_TotalQuestion.setText(totalresult);
 		
 		txt_CorrectQuestion.setText(correctresult);
 		
 		
 		
 		// helperDb.getTotalNoOfQuesution(TestID); 
		
 		
		hmapQuestionIDAndQuestionTxt=helperDb.fetchQstIDAndQstpartTxtbasedbasedTestID(Integer.parseInt(TestID));
		initializtion();
		createQuestion();
 		}
	}
	
	public void initializtion()
	{
		 img_back=(ImageView) findViewById(R.id.img_back);
		 img_successfail=(ImageView) findViewById(R.id.img_successfail);
		 
		 
         img_back.setOnClickListener(new OnClickListener()
         {
			
			@Override
			public void onClick(View v) {
				
				

				 AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModuleQuestionAnswerActivity.this);
				 alertDialog.setTitle("Information");
			        alertDialog.setIcon(R.drawable.info_ico);
			        alertDialog.setCancelable(false);
			     alertDialog.setMessage("Do you want to exit the test ? If yes, you need to start the test again. ");
			     alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            	dialog.dismiss();
			            	Intent intent=new Intent(ModuleQuestionAnswerActivity.this,AssesmentModuleActivity.class);
							intent.putExtra("ModuleId", ModuleId);
							startActivity(intent);
							finish();
			           
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
         
		
	    btn_next=(Button)findViewById(R.id.btn_next);
	    btn_next.setVisibility(View.GONE);
	    btn_next.setEnabled(false);
 		btn_next.setOnClickListener(new OnClickListener()
 		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				img_successfail.setVisibility(View.INVISIBLE);
				btn_submit.setEnabled(true);
				btn_next.setEnabled(false);
				AnswerDescr="0";
				ll_dynamicForm.removeAllViews();
				questionCounting=questionCounting+1;
				createQuestion();
				
			}
		});
		
		btn_submit=(Button)findViewById(R.id.btn_submit);
		btn_submit.setEnabled(true);
 		btn_submit.setOnClickListener(new OnClickListener()
 		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
				
				AnswerDescr="0";
				//img_successfail.setVisibility(View.VISIBLE);
				
				btn_next.setEnabled(true);
			//	btn_submit.setEnabled(false);
				StringBuilder answer=new StringBuilder();
				LinearLayout ll_checkBoxVal=(LinearLayout)ll_dynamicForm.getChildAt(0);
				boolean isSingleSelected=false;
				boolean noValSelected=true;
				
				 Boolean answerRightOrWrong=false;
				for(int i=0;i<ll_checkBoxVal.getChildCount();i++)
				{
					
					 if(AnsControlType.equals("1"))
					 {
							CheckBox checkBoxVal=(CheckBox) ll_checkBoxVal.getChildAt(i);
							//checkBoxVal.setEnabled(false);
							if(checkBoxVal.isChecked())
							{
								img_successfail.setVisibility(View.VISIBLE);
								isSingleSelected=true;
								String tagValueOfCheckBox=checkBoxVal.getTag().toString();
								String QuestionID=tagValueOfCheckBox.split(Pattern.quote("^"))[0];
								String checkBoxID=tagValueOfCheckBox.split(Pattern.quote("^"))[1];
								
								AnswerDescr=checkBoxID;
								int CorrectAns=helperDb.fnfetchCorrectAnsBasedQstIDQstnStatID(Integer.parseInt(QuestionID),Integer.parseInt(checkBoxID));
									if(CorrectAns==1)
									{
										CorrectAnsCounter=CorrectAnsCounter+1;
										img_successfail.setImageResource(R.drawable.right_icon);
										
										
									}
									else
									{
										img_successfail.setImageResource(R.drawable.wrong_icon);
									}
									
									
							}
							
					 }
					 else if(AnsControlType.equals("2"))
					 {
						 hmapQstnStatIDAndCorrectAns=helperDb.fetchQstnStatIDAndCorrectAnsbasedbasedTestIDQstID(Integer.parseInt(TestID),Integer.parseInt(QuestionID));
					   
						 
						 CheckBox checkBoxVal=(CheckBox) ll_checkBoxVal.getChildAt(i);
						// checkBoxVal.setEnabled(false);
							if(checkBoxVal.isChecked())
							{
								img_successfail.setVisibility(View.VISIBLE);
								isSingleSelected=true;
								String tagValueOfCheckBox=checkBoxVal.getTag().toString();
								String QuestionID=tagValueOfCheckBox.split(Pattern.quote("^"))[0];
								String checkBoxID=tagValueOfCheckBox.split(Pattern.quote("^"))[1];
								
								if(hmapQstnStatIDAndCorrectAns.containsKey(checkBoxID))
								{
									answerRightOrWrong=true;
								}
								else
								{
									answerRightOrWrong=false;
								}
								
								if(i==0)
								{
									AnswerDescr=checkBoxID;
								}
								else
								{
									AnswerDescr=AnswerDescr+"^"+checkBoxID;
								}
								
								
							}
					 }
					
				}
				
				
				
				
				if(isSingleSelected)
				{
					if(answerRightOrWrong==true && AnsControlType.equals("2"))
					{
						CorrectAnsCounter=CorrectAnsCounter+1;
						img_successfail.setImageResource(R.drawable.right_icon);	
					}
					else if(answerRightOrWrong==false && AnsControlType.equals("2"))
					{
						img_successfail.setImageResource(R.drawable.wrong_icon);
					}
					String correctresult="Answered "+ CorrectAnsCounter +" question(s) correctly out of total "+questionCounting+" question(s).";
			 		
			 		
			 		txt_CorrectQuestion.setText(correctresult);
			 		
			 		 Calendar cal = Calendar.getInstance();
						
					String ModuleCompleteDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(cal.getTime());
				       
					
					helperDb.open();
					helperDb.savetblTestsAnswerForModule(Integer.parseInt(ModuleId),Integer.parseInt(TestID),Integer.parseInt(TestInstanceID),Integer.parseInt(TestInsUserMapID),Integer.parseInt(QuestionID),AnswerDescr,Integer.parseInt(AnsControlType),ModuleCompleteDate,3);
					helperDb.close();
				
					
					
					img_successfail.setVisibility(View.INVISIBLE);
					btn_submit.setEnabled(true);
					btn_next.setEnabled(false);
					AnswerDescr="0";
					ll_dynamicForm.removeAllViews();
					questionCounting=questionCounting+1;
					createQuestion();
				}
				
				else
				{
					Toast.makeText(ModuleQuestionAnswerActivity.this, "Please select a value to proceed", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			
			
		});
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
	
	
	public void createQuestion()
	{

		
		if(questionCounting==1)
 		{
 			txt_CorrectQuestion.setVisibility(View.INVISIBLE);
 		}
		else
		{
		txt_CorrectQuestion.setVisibility(View.VISIBLE);
		}
	
		if(questionCounting==(Integer.parseInt(TotalQstn)+1))
		{
			
			String abcd="NA";
			
			if(CorrectAnsCounter>=Integer.parseInt(PassingQstn))
			{
				abcd="You have successfully cleared the test. You have answered "+ CorrectAnsCounter +" question(s) correctly out of total "+ TotalQstn +" questions. \nClick below to get the certificate. \n                    All the best.";	
			}
			else
			{
				abcd="You have answered "+ CorrectAnsCounter +" question(s) correctly out of total "+ TotalQstn +" questions. \nTo pass the test minimum "+ PassingQstn +" question should be correct. \nPlease reappear after reading corresponding Training material. \n               All the best";
				
			}
			
			
			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			 
			 final LayoutInflater inflater = this.getLayoutInflater();
			    final View dialogView = inflater.inflate(R.layout.custom_alert_for_passfail, null);
		     
		        // Setting Dialog Title
		        alertDialog.setTitle("Info");
		        alertDialog.setIcon(R.drawable.info_ico);
		        alertDialog.setCancelable(false);
		        // Setting Dialog Message
		       // alertDialog.setMessage(abcd);
		        
		        alertDialog.setView(dialogView);
		        
		        
		        final TextView testNameVal = (TextView) dialogView.findViewById(R.id.testNameVal);
		        testNameVal.setText(TestDescr);
		        
		        final TextView passMsg = (TextView) dialogView.findViewById(R.id.passMsg);
		        passMsg.setText(abcd);
		        
		        final TextView printCertificate = (TextView) dialogView.findViewById(R.id.printCertificate);
		        if(CorrectAnsCounter>=Integer.parseInt(PassingQstn))
    			{
		        	printCertificate.setVisibility(View.VISIBLE);
		        SpannableString content = new SpannableString("Certificate");
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
				printCertificate.setText(content);
    			}
		        else
		        {
		        	printCertificate.setVisibility(View.INVISIBLE);
		        }
				printCertificate.setOnClickListener(new OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub


			 			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModuleQuestionAnswerActivity.this);
			 			    final View dialogView = inflater.inflate(R.layout.custom_alert_certificate, null);
					     
					      alertDialog.setTitle("Info");
					        alertDialog.setIcon(R.drawable.info_ico);
					        alertDialog.setCancelable(false);
					      
					       // alertDialog.setMessage("You already pass the test.");
					        alertDialog.setView(dialogView);
					        
					        final TextView txt_userName = (TextView) dialogView.findViewById(R.id.txt_userName);
					        txt_userName.setText(UserFullName);
					        
					        
					      long  syncTIMESTAMP = System.currentTimeMillis();
							Date dateobj = new Date(syncTIMESTAMP);
							SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
							String fullFileName1 = df.format(dateobj);
					        
					        String moduleName=helperDb.fnfetchModuleContentNameBasedmoduleID(Integer.parseInt(ModuleId));
					        
					        String abcd="has successfully completed the "+moduleName +" module on "+fullFileName1;
					        
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
    			
		        
		        
		        // On pressing Settings button
		        alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() 
		        {
		            public void onClick(DialogInterface dialog,int which) 
		            {
		            	
		            	if(CorrectAnsCounter>=Integer.parseInt(PassingQstn))
		    			{
		            		
		            		helperDb.UpdateAssessmentCompleteBasedOnModuleID(ModuleId);
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
				   					
				   					
				   				helperDb.open();
				   				helperDb.updateXMLRecordsSyncd();		// update syncd' records
				   				helperDb.close();
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
		        			
		        				Intent intent = new Intent(ModuleQuestionAnswerActivity.this, UploadXMLService.class);
		        				intent.putExtra("foo", "bar");
		        				startService(intent);
		        			 }
		            		
		            		
		            		questionCounting=1;
			            	Intent intent=new Intent(ModuleQuestionAnswerActivity.this,AssesmentModuleActivity.class);
							intent.putExtra("ModuleId", ModuleId);
							startActivity(intent);
							finish();	
		    			}
		    			else
		    			{
		    				
		    				helperDb.fnDeletefailsRecords(Integer.parseInt(ModuleId));
		    				questionCounting=1;
			            	Intent intent=new Intent(ModuleQuestionAnswerActivity.this,AssesmentModuleActivity.class);
							intent.putExtra("ModuleId", ModuleId);
							startActivity(intent);
							finish();
		    			}
		    			
		            	
		            	
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
			
		}
		else
		{
		
		
		TextView questino_txt=(TextView)findViewById(R.id.questino_txt);
		String QuestionIDAndQuestionTxt =hmapQuestionIDAndQuestionTxt.get(""+questionCounting);
		
		QuestionID=QuestionIDAndQuestionTxt.split(Pattern.quote("^"))[0];
		QuestionTxt=QuestionIDAndQuestionTxt.split(Pattern.quote("^"))[1];
		
		questino_txt.setText(""+questionCounting+".  "+QuestionTxt);
		
		
		AnsControlType=helperDb.fnfetchQstnTypeIDBasedQstID(Integer.parseInt(QuestionID));
		
		
		hmapQstnStatIDAndStatementDescr=helperDb.fetchQstnStatIDAndStatementDescrbasedbasedTestIDQstID(Integer.parseInt(TestID),Integer.parseInt(QuestionID));
		
		View answerInputTypeView = null;
		
		for(Entry<String, String> entry:hmapQstnStatIDAndStatementDescr.entrySet())
		{
			 if(AnsControlType.equals("1"))
				{
				    hmapOptionValues.put(entry.getKey(), helperDb.fnGetOptionMstr(Integer.parseInt(QuestionID)));
					ArrayList<String> checkBoxSinglSel=hmapOptionValues.get(entry.getKey());
					
					answerInputTypeView=getCheckBoxView(checkBoxSinglSel, true);
				}
				else if(AnsControlType.equals("2"))
				{
					hmapOptionValues.put(entry.getKey(), helperDb.fnGetOptionMstr(Integer.parseInt(QuestionID)));
					ArrayList<String> checkBoxMultiSel=hmapOptionValues.get(entry.getKey());
					answerInputTypeView=getCheckBoxView(checkBoxMultiSel, false);
				}
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			params.setMargins(0, 10, 0, 0);
		ll_dynamicForm.addView(answerInputTypeView,params);
		
	}
	
	}
	
	public LinearLayout getCheckBoxView(final ArrayList<String> chckBoxVal,boolean isSingleChecked)
	{
		final LinearLayout lay = new LinearLayout(this);
		//lay.setTag(tagVal);
		lay.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		ArrayList<String> arrayCheckedView=new ArrayList<String>();
		lay.setOrientation(LinearLayout.VERTICAL);
		lay.removeAllViews();
		 /* if(hmapViewAddedOutlet!=null)
			{
				if(hmapViewAddedOutlet.containsKey(tagVal))
				{
					if(hmapViewAddedOutlet.get(tagVal).contains("^"))
					{
						String[] spliitedCheckedVal=hmapViewAddedOutlet.get(tagVal).toString().split(Pattern.quote("^"));
						for(String element:spliitedCheckedVal)
						{
							arrayCheckedView.add(element);
						}
					}
					else 
					{
						arrayCheckedView.add(hmapViewAddedOutlet.get(tagVal).toString());
						
					}
				}
				
			}*/
		for(int index=0;index <chckBoxVal.size();index++)
		{
			CheckBox chBox=new CheckBox(this);
			chBox.setEnabled(true);
			//String quesIdForkey=tagVal.split(Pattern.quote("^"))[0];
			String checkBoxID=chckBoxVal.get(index).split(Pattern.quote("^"))[0].toString().trim();
			String checkBoxVal=chckBoxVal.get(index).split(Pattern.quote("^"))[1];
			
			//String checkBoxValNoneOptionNo=chckBoxVal.get(index).split(Pattern.quote("^"))[2];
			chBox.setTextSize(12);
						chBox.setTag(QuestionID+"^"+checkBoxID);
			//System.out.println("CheckBox Tag = "+checkBoxVal+" : "+checkBoxValNoneOptionNo);
			chBox.setText(checkBoxVal);
			//hmapOptionId.put(quesIdForkey+"^"+checkBoxVal, chckBoxVal.get(index).split(Pattern.quote("^"))[0]);
			//chBox.setTag(String.valueOf(index));
			 RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams
		        		((int)LayoutParams.WRAP_CONTENT, (int) LayoutParams.WRAP_CONTENT);
		        params2.leftMargin = 16;
		        params2.topMargin = 0;
		        chBox.setLayoutParams(params2);
		        lay.addView(chBox);
		      if(arrayCheckedView.contains(checkBoxID))
		      {
		    	  chBox.setChecked(true); 
		      }

		        if(isSingleChecked )
		        {
		        	chBox.setOnClickListener(new OnClickListener()
		        	{
						
						@Override
						public void onClick(View v) {

							((CheckBox)v).setChecked(true);
							
						}
					});
		            chBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		            {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
						{
							
							for(int i=0;i<lay.getChildCount();i++)
							{
							
								CheckBox unChckdOtherCheckBox=(CheckBox) lay.getChildAt(i);
								if(unChckdOtherCheckBox!=buttonView)
								{
									if(unChckdOtherCheckBox.isChecked())
									{
										unChckdOtherCheckBox.setChecked(false);
									}
								
								
								}
								
								
							}
							
						}
					});
		        }
		        
		        
		        else 
		        {
		        	chBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		        	{
		            
		            @Override
		            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		            {
		             System.out.println("CheckBox Tag = "+buttonView.getText().toString()+" : "+buttonView.getTag().toString());
		             if(buttonView.getTag().toString().equals("0"))
		             {
		              
		             
		              for(int i=0;i<lay.getChildCount();i++)
		              {
		               
		               CheckBox unChckdOtherCheckBox=(CheckBox) lay.getChildAt(i);
		               if(unChckdOtherCheckBox!=buttonView)
		               {
		                if(buttonView.isChecked())
		                {
		                 unChckdOtherCheckBox.setChecked(false);
		                }
		               
		               
		               }
		               
		               
		              } 
		             }
		             
		             else
		             {

		              for(int i=0;i<lay.getChildCount();i++)
		              {
		              
		               CheckBox unChckdOtherCheckBox=(CheckBox) lay.getChildAt(i);
		               if(unChckdOtherCheckBox.getTag().toString().equals("0"))
		               {
		                
		                 if(buttonView.isChecked())
		                 {
		                  unChckdOtherCheckBox.setChecked(false);
		                 }
		                
		                
		                
		                
		               
		               
		               
		               }
		               
		               
		              } 
		             
		              
		             }
		             
		             
		            }
		           });}
		        
		        
		
		}
		
	        return lay;
	}

}
