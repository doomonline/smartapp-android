package cn.wxcs.smartapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class Device extends CordovaPlugin {
    public static final String TAG = "Device";

    public static String cordovaVersion = "dev";              // Cordova version
    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    BroadcastReceiver telephonyReceiver = null;

    /**
     * Constructor.
     */
    public Device() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Device.uuid = getUuid();
        this.initTelephonyReceiver();
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into JavaScript.
     * @return True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getDeviceInfo")) {
            JSONObject r = new JSONObject();
            r.put("uuid", Device.uuid);
            r.put("version", this.getOSVersion());
            r.put("cordova", Device.cordovaVersion);
            r.put("model", this.getModel());
            callbackContext.success(r);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Unregister receiver.
     */
    public void onDestroy() {
        this.cordova.getActivity().unregisterReceiver(this.telephonyReceiver);
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

    /**
     * Listen for telephony events: RINGING, OFFHOOK and IDLE
     * Send these events to all plugins using
     * CordovaActivity.onMessage("telephone", "ringing" | "offhook" | "idle")
     */
    private void initTelephonyReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        //final CordovaInterface mycordova = this.cordova;
        this.telephonyReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                // If state has changed
                if ((intent != null) && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                    if (intent.hasExtra(TelephonyManager.EXTRA_STATE)) {
                        String extraData = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                        if (extraData.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            LOG.i(TAG, "Telephone RINGING");
                            webView.postMessage("telephone", "ringing");
                        } else if (extraData.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                            LOG.i(TAG, "Telephone OFFHOOK");
                            webView.postMessage("telephone", "offhook");
                        } else if (extraData.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                            LOG.i(TAG, "Telephone IDLE");
                            webView.postMessage("telephone", "idle");
                        }
                    }
                }
            }
        };

        // Register the receiver
        this.cordova.getActivity().registerReceiver(this.telephonyReceiver, intentFilter);
    }

    /**
     * Get the device's Universally Unique Identifier (UUID).
     *
     * @return
     */
    public String getUuid() {
        String uuid = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }

    /**
     * Get the Cordova version.
     *
     * @return
     */
    public String getCordovaVersion() {
        return Device.cordovaVersion;
    }

    public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }

    /**
     * Get the OS version.
     *
     * @return
     */
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }

    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }

    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }

}
