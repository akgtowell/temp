package com.phonegap.sfa;

import android.content.Context;



import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;

import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.AidcManager.CreatedCallback;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;

public class HoneywellScannerPlugin extends Plugin implements BarcodeReader.BarcodeListener {
    private static final String TAG = "HoneywellScanner";

    private static BarcodeReader barcodeReader;
    private AidcManager manager;
    private static String callbackContext = "callbackContext";
   // private CallbackContext callbackContext;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
     //   super.initialize(cordova, webView);

        Context context = cordova.getActivity().getApplicationContext();
        AidcManager.create(context, new CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                barcodeReader = manager.createBarcodeReader();
                if(barcodeReader != null){
                    barcodeReader.addBarcodeListener(HoneywellScannerPlugin.this);
                    try {
                        barcodeReader.claim();
                    } catch (ScannerUnavailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /*@Override
    public boolean execute(String action, final JSONArray args, String callbackContext) throws JSONException {
        if(action.equals("listenForScans")){
            this.callbackContext = callbackContext;
            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            //this.callbackContext.sendPluginResult(result);
        }
        return true;
    }*/
	@Override
	public PluginResult execute(String action, JSONArray arg1, String callbackContext) {
		// TODO Auto-generated method stub
		 //this.callbackContext = callbackContext;

		try {
       // if(action.equals("listenForScans")){
            this.callbackContext = callbackContext;
            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            //return new PluginResult(PluginResult.Status.INVALID_ACTION);
     //   }
		} catch (Exception e) {

			e.printStackTrace();
		}
		
        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        return r;
	}
    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        if(this.callbackContext!=null)
        {
            PluginResult result = new PluginResult(PluginResult.Status.OK, barcodeReadEvent.getBarcodeData());
            result.setKeepCallback(true);
          //  this.callbackContext.sendPluginResult(result);
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        NotifyError("Scan failed");
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                NotifyError("Scanner unavailable");
            }
        }
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        if (barcodeReader != null) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }

    private void NotifyError(String error){
        if(this.callbackContext!=null)
        {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, error);
            result.setKeepCallback(true);
           // this.callbackContext.sendPluginResult(result);
        }
    }


}