package com.astix.gsk_dsr;

import com.astix.gsk_dsr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DistributorAssessmentMain  extends Activity{
	LinearLayout btnDistributionAssesmentNext;
	EditText edt_DistributorName;
	EditText edt_AssessorName1;
	EditText edt_AssessorName2;
	EditText edt_Zone;
	EditText edt_Territory;
	EditText edt_Town;
	ImageView img_home;
	int cRed = Color.RED;
	
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distributorassessment);

		
		img_home=(ImageView) findViewById(R.id.img_home);
		edt_DistributorName=(EditText)findViewById(R.id.edt_DistributorName);
		edt_AssessorName1=(EditText)findViewById(R.id.edt_AssessorName1);
		edt_AssessorName2=(EditText)findViewById(R.id.edt_AssessorName2);
		edt_Zone=(EditText)findViewById(R.id.edt_Zone);
		edt_Territory=(EditText)findViewById(R.id.edt_Territory);
		edt_Town=(EditText)findViewById(R.id.edt_Town);
		
		btnDistributionAssesmentNext=(LinearLayout) findViewById(R.id.ll_next);
		img_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				confirmExitAlert();
			
			
			}
		});
		btnDistributionAssesmentNext.setOnClickListener(new OnClickListener() 
		  {
				@Override
				public void onClick(View v)
				{
					Boolean boolvalidateDataEntry= fnvalidateDataEntry();
					if(boolvalidateDataEntry)
					{
						String assessorName2="";
						if(TextUtils.isEmpty(edt_Zone.getText().toString().trim()))
						{
							assessorName2="";	
						}
						else
						{
							assessorName2=edt_Zone.getText().toString().trim();
						}
						Intent intent=new Intent(DistributorAssessmentMain.this,GroupQuesActivity.class);
						intent.putExtra("dstrbtr_name", edt_DistributorName.getText().toString().trim());
						intent.putExtra("assessorName1", edt_AssessorName1.getText().toString().trim());
						intent.putExtra("assessorName2", assessorName2);//edt_AssessorName2.getText().toString().trim());
						intent.putExtra("zone", edt_Zone.getText().toString().trim());
						intent.putExtra("territory", edt_Territory.getText().toString().trim());
						intent.putExtra("town", edt_Town.getText().toString().trim());
						startActivity(intent);
						finish();
					}
				}
		  });
		
	}
	public Boolean fnvalidateDataEntry() {
		
		if(TextUtils.isEmpty(edt_DistributorName.getText().toString().trim()))
		{
	
			edt_DistributorName.setHint("Distributor Name");
			//edt_DistributorName.setHintTextColor(cRed);
			edt_DistributorName.setFocusable(true);
			return false;
		}
		if(TextUtils.isEmpty(edt_AssessorName1.getText().toString().trim()))
		{
			edt_AssessorName1.setHint("AssessorName1");
			return false;
		}
		/*if(TextUtils.isEmpty(edt_AssessorName2.getText().toString().trim()))
		{
			edt_AssessorName2.setHint("AssessorName2");
			return false;
		}*/
		if(TextUtils.isEmpty(edt_Zone.getText().toString().trim()))
		{
			edt_Zone.setHint("Zone");
			return false;
		}
		if(TextUtils.isEmpty(edt_Territory.getText().toString().trim()))
		{
			edt_Territory.setHint("Territory");
			return false;
		}
		if(TextUtils.isEmpty(edt_Town.getText().toString().trim()))
		{
			edt_Town.setHint("Town");
			return false;
		}
		//Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
		return true;
	}
	
 public void confirmExitAlert()
	 
	 {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistributorAssessmentMain.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Exit");

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want to exit Assessment?");

		
			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					
					Intent intent=new Intent(DistributorAssessmentMain.this,AllButtonActivity.class);
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
