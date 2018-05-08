package com.astix.gsk_dsr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.astix.gsk_dsr.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreBoardActivity extends Activity {
	
	LinkedHashMap<String, String> hmapTestResult=new LinkedHashMap<String, String>();
	TextView txt_spirit,txt_rcmndd_actn,txt_profile,txt_dscrptn,txt_total_Score;
	int totalScore=0;
	GskDBAdapter gskDatabase;
	LinearLayout ll_Ok;
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
		
		setContentView(R.layout.activity_score_board);
		gskDatabase=new GskDBAdapter(ScoreBoardActivity.this);
		Intent intentData=getIntent();
		totalScore=intentData.getIntExtra("total_score", 0);
		txt_spirit=(TextView) findViewById(R.id.txt_spirit);
		txt_rcmndd_actn=(TextView) findViewById(R.id.txt_rcmndd_actn);
		txt_profile=(TextView) findViewById(R.id.txt_profile);
		txt_dscrptn=(TextView) findViewById(R.id.txt_dscrptn);
		txt_total_Score=(TextView) findViewById(R.id.txt_total_Score);
		txt_total_Score.setText(String.valueOf(totalScore));
		ll_Ok=(LinearLayout) findViewById(R.id.ll_Ok);
		ll_Ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(ScoreBoardActivity.this,AllButtonActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		setResultGrade();
		int colorIndex=0;
		for(Map.Entry<String,String> entry:hmapTestResult.entrySet())
		{
			int minScore=Integer.parseInt(entry.getKey().split(Pattern.quote("-"))[0]);
			int maxScore=Integer.parseInt(entry.getKey().split(Pattern.quote("-"))[1]);
		
		if(totalScore>(minScore-1) && totalScore<(maxScore+1))
		{
			txt_dscrptn.setText((entry.getValue()).split(Pattern.quote("~"))[0].toString());
			txt_profile.setText((entry.getValue()).split(Pattern.quote("~"))[1].toString());
			txt_rcmndd_actn.setText((entry.getValue()).split(Pattern.quote("~"))[2].toString());
			if(colorIndex==0)
			{
				txt_profile.setBackgroundColor(getResources().getColor(R.color.profile_red));
				txt_profile.setTextColor(getResources().getColor(R.color.profile_text_yellow));
				
				txt_rcmndd_actn.setBackgroundColor(getResources().getColor(R.color.profile_red));
				txt_rcmndd_actn.setTextColor(getResources().getColor(R.color.profile_text_yellow));
			}
			else if(colorIndex==1)
			{
				txt_profile.setBackgroundColor(getResources().getColor(R.color.profile_yellow));
				txt_profile.setTextColor(getResources().getColor(R.color.profile_text_black));
				txt_rcmndd_actn.setBackgroundColor(getResources().getColor(R.color.profile_yellow));
				txt_rcmndd_actn.setTextColor(getResources().getColor(R.color.profile_text_black));
			}
			else if(colorIndex==2)
			{
				txt_profile.setBackgroundColor(getResources().getColor(R.color.profile_blue));
				txt_profile.setTextColor(getResources().getColor(R.color.profile_text_black));
				txt_rcmndd_actn.setBackgroundColor(getResources().getColor(R.color.profile_blue));
				txt_rcmndd_actn.setTextColor(getResources().getColor(R.color.profile_text_black));
			}
			else
			{
				txt_profile.setBackgroundColor(getResources().getColor(R.color.profile_green));
				txt_profile.setTextColor(getResources().getColor(R.color.profile_text_white));
				txt_rcmndd_actn.setBackgroundColor(getResources().getColor(R.color.profile_green));
				txt_rcmndd_actn.setTextColor(getResources().getColor(R.color.profile_text_white));
			}
			
			txt_spirit.setText((entry.getValue()).split(Pattern.quote("~"))[3].toString());
			break;
		
		}
		colorIndex++;
		
	}
	}
	private void setResultGrade() {
		
		hmapTestResult=gskDatabase.fetchProfileLevel();
	}

}
