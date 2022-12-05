package com.phonegap.sfa;
import org.apache.cordova.DroidGap;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
@SuppressLint("NewApi")
public class App extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setIntegerProperty("loadUrlTimeoutValue", 25000);
        super.setIntegerProperty("splashscreen", R.drawable.splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        decorView.setSystemUiVisibility(uiOptions);
         getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
       

         //Then call this function
    
        //super.clearCache();
       
        super.loadUrl("file:///android_asset/www/index.html",3000);
        //        hideSystemUI();
      
    }
    

    
    
    private void hideSystemUI() {
    	View mDecorView = getWindow().getDecorView();
    	int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
    	mDecorView.setSystemUiVisibility(uiOptions);
    }
}