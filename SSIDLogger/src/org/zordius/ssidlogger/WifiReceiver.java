package org.zordius.ssidlogger;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	public static WifiManager wifi = null;
	public static WifiLock lock = null;

	public static void startScan(Context context) {
		readyWifi(context);
		if (wifi.isWifiEnabled()) {
			wifi.startScan();
		} else {
			wifi.setWifiEnabled(true);
		}
		lock.acquire();
	}

	public static void stopScan(Context context) {
		Log.d("wifi", "stop scan");
		readyWifi(context);
		lock.release();
	}

	public static void setScan(Context context, boolean enabled) {
		if (enabled) {
			startScan(context);
		} else {
			stopScan(context);
		}
	}

	public static void readyWifi(Context context) {
		Log.d("wifi", "check ready");
		if (wifi == null) {
			Log.d("wifi", "not ready");
			wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			lock = wifi.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY,
					"LockTag");
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("wifi", "intent~~");

		String action = intent.getAction();
		if (!action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
			return;
		}

		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> results = wifi.getScanResults();
		for (ScanResult R : results) {
			Log.d("wifi", R.BSSID + ':' + R.level);
		}
	}
}
