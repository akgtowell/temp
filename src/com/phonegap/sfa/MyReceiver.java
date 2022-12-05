package com.phonegap.sfa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import com.honeywell.scanintent.ScanIntent;


public class MyReceiver extends BroadcastReceiver{
    private static final String ACTION_BARCODE_DATA = "com.honeywell.sample.action.BARCODE_DATA";
    private static final String ACTION_RESULT="com.honeywell.intent.action.SCAN_RESULT";
	private static final String TAG = "BARCOE SUJEEEEEEEEEEEEEE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
    	Log.d("BARCODE DATA","SUJEE_________________");
    	
		  String data = intent.getStringExtra(ScanIntent.EXTRA_RESULT_BARCODE_DATA);
		  String data1 = intent.getStringExtra("com.honeywell.sample.action.BARCODE_DATA");
		  String data2 = intent.getStringExtra("com.honeywell.intent.action.SCAN_RESULT");
		  Bundle properties = new Bundle();
	        properties.putBoolean("DPR_DATA_INTENT", true);
	        properties.putString("DPR_DATA_INTENT_ACTION", ACTION_BARCODE_DATA);
	    	Log.d("BARCODE DATA","AFTER SCANNINN     SUJEE_________________");
	    	Log.d("BARCODE DATA RESUlt ",data +"=="+data1+"==" +data2);
	    	
	    	 String keys = Arrays.toString(intent.getExtras().keySet().toArray());
	         // [codeId, dataBytes, data, timestamp, aimId, version, charset, scanner]
	         String[] mKeys=keys.split(" ");
	         for (String s:mKeys) {
	             Log.d(TAG, "key: "+s);
	         }
	         
	         String data3 = intent.getStringExtra("com.honeywell.intent.action.BARCODE_DATA)");
	         Log.d(TAG, "codeId: \t" + intent.getStringExtra("codeId"));
	         Log.d(TAG, "dataBytes\t" + Arrays.toString(intent.getByteArrayExtra("dataBytes")));
	         Log.d(TAG, "data: \t" + intent.getStringExtra("data"));
	         Log.d(TAG, "timestamp: \t" + intent.getStringExtra("timestamp"));
	         Log.d(TAG, "aimId: \t" + intent.getStringExtra("aimId"));
	         Log.d(TAG, "version: \t" + intent.getIntExtra("version", -1));
	         Log.d(TAG, "charset: \t" + intent.getStringExtra("charset"));
	         Log.d(TAG, "scanner: \t" + intent.getStringExtra("scanner"));
		    	Log.d("BARCODE DATA RESUlt ",data +"=="+data1+"==" +data2+"==" +data3);
	         
	}

}
