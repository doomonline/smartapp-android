package cn.wxcs.smartapp;

import org.apache.cordova.CordovaActivity;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends CordovaActivity  {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        super.loadUrl("file:///android_asset/www/index.html");
        super.appView.getSettings().setUserAgentString(appView.getSettings().getUserAgentString() + " | SmartAPP 0.1 pre alpha");
    }
}
