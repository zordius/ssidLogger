package org.zordius.ssidlogger;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	public static final String ACTION_SCAN = "org.zordius.ssidlogger.action.SCAN";
	public static WifiManager wifi = null;
	public static AlarmManager alarm = null;
	public static PendingIntent pending = null;

	public static void toggleScan(Context context, boolean enable) {
		if (enable) {
			doScan(context);
			setAlarm(context);
		} else {
			receiveWifi(context, false);
			cancelAlarm(context);
		}
	}

	public static void setAlarm(Context context) {
		Log.d("alarm", "set");
		readyAlarm(context);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 30, pending);
	}

	public static void cancelAlarm(Context context) {
		Log.d("alarm", "cancel");
		readyAlarm(context);
		alarm.cancel(pending);
	}

	public static boolean isEnabled(Context context) {
		Log.d("wifi", String.valueOf(context.getPackageManager()
				.getComponentEnabledSetting(
						new ComponentName(context, WifiReceiver.class))));
		return context.getPackageManager().getComponentEnabledSetting(
				new ComponentName(context, WifiReceiver.class)) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
	}

	public static void receiveWifi(Context context, boolean enable) {
		context.getPackageManager().setComponentEnabledSetting(
				new ComponentName(context, WifiReceiver.class),
				enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
						: PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

	public static void doScan(Context context) {
		Log.d("wifi", "do scan");
		receiveWifi(context, true);
		readyWifi(context);
		if (wifi.isWifiEnabled()) {
			wifi.startScan();
		} else {
			wifi.setWifiEnabled(true);
		}
	}

	public static void readyAlarm(Context context) {
		if (alarm == null) {
			alarm = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			pending = PendingIntent.getBroadcast(context, 0, new Intent(
					context, WifiReceiver.class), 0);
		}
	}

	public static void readyWifi(Context context) {
		if (wifi == null) {
			wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		// Handle wifi scan result
		if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			readyWifi(context);
			List<ScanResult> results = wifi.getScanResults();
			for (ScanResult R : results) {
				Log.d("wifi", R.BSSID + ':' + R.level);
			}
			return;
		}

		// No action, it should be my alarm intent
		doScan(context);
	}
}
