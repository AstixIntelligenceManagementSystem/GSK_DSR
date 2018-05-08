package com.astix.gsk_dsr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CheckInstallAppBroadCastReciever extends BroadcastReceiver 
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		// TODO Auto-generated method stub
		Intent intent1 = new Intent(context, SplashScreen.class);
		intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent1);
	}

}
