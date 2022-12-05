package com.phonegap.sfa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Utility {
 
	public static final String LocationUpdateUrl="http://134.0.204.102:62811/sfa/som/api/api/ws/routetrack";  

	//public static final String LocationUpdateUrl="http://10.10.10.52:62811/sfa/som/api/api/ws/routetrack";  
	
	public interface getGpsStatus{
		public void IsAvailable();
	}
	public static void scheduleLTService(Context c) {
		Intent mIntent = new Intent(c, LocationTrackService.class);
		PendingIntent mPendingIntent = PendingIntent.getService(c, LocationTrackService.HBSERVICE_ALARMID, mIntent, 0);
		AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), LocationTrackService.INTERVAL,
				mPendingIntent);
	}

	public static void cancelLTServiceSchedule(Context c) {
		Intent mIntent = new Intent(c, LocationTrackService.class);
		PendingIntent mPendingIntent = PendingIntent.getService(c, LocationTrackService.HBSERVICE_ALARMID, mIntent, 0);
		AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(mPendingIntent);
	}

	
	
}
