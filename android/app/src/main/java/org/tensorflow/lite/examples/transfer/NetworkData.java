//package org.tensorflow.lite.examples.transfer;
//
//
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//
//import java.lang.Object;
//import java.util.*;
//import java.io.*;
//import java.lang.*;
//import java.io.Console;
//import android.net.wifi.WifiInfo;
//
//
//public class NetworkData {
//
//    private static  final String LOG_TAG = "LogActivity";
//
//    public static String getMacAddress(Context context)
//    {
//        Log.d(LOG_TAG, "hello");
//        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        String isWifiEnable;
//        if (!wifiManager.isWifiEnabled()) {
//            isWifiEnable = "false";
//        } else
//            isWifiEnable = "true";
//        WifiInfo info = wifi.getConnectionInfo();
//        String mac = info.getMacAddress();
//        //LogUtils.i(TAG, " MAC：" + mac);
//        return mac;
//    }
//
//}
