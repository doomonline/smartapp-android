package cn.wxcs.smartapp;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;
public class MyActivity extends CordovaActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
        super.appView.getSettings().setUserAgentString("smart@pp");
    }
}
