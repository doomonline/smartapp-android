package cn.wxcs.smartapp;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

public class DemoPlugin extends CordovaPlugin {
	private static final String TAG = DemoPlugin.class.getSimpleName();
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	Log.d(TAG, "Action: " + action);
    	Log.d(TAG, "Arguments: " + args.toString());
    	this.echo(args.toString(), callbackContext);
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
        	Log.d(TAG, "begin vibrate...");
        	Vibrator vibrator = (Vibrator) this.cordova.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            callbackContext.success(message);
        } else {
        	Log.d(TAG, "No Messages,return error");
            callbackContext.error("出错啦！！");
        }
    }
}
