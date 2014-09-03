package org.zordius.ssidlogger;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	public static final String PREFERENCES = "org.zordius.ssidlogger.preference";
	public static final String LOGFILE = "ssidLogger.log";
	public static final String PREF_LOGFILE = "logFile";
	public static final String ACTION_UPDATE = "org.zordius.ssidlogger.action_update";

	public static WifiManager wifi = null;
	public static AlarmManager alarm = null;
	public static PendingIntent pending = null;
	public static SharedPreferences pref = null;
	public static String logFile = null;

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
		readyAlarm(context);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 30, pending);
	}

	public static void cancelAlarm(Context context) {
		readyAlarm(context);
		alarm.cancel(pending);
	}

	public static boolean isEnabled(Context context) {
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
		writeLog(context, "SCAN");
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

	public static void readyPref(Context context) {
		if (pref == null) {
			pref = context.getSharedPreferences(PREFERENCES,
					Context.MODE_PRIVATE);
		}
	}

	public static boolean setLogFile(Context context, String name) {
		logFile = name;
		if (writeLog(context, "SETFILE")) {
			return setPref(context, PREF_LOGFILE, name);
		}
		logFile = null;
		return false;
	}

	public static boolean setPref(Context context, String name, String value) {
		readyPref(context);
		return pref.edit().putString(name, value).commit();
	}

	public static void readyLog(Context context) {
		if (logFile == null) {
			readyPref(context);
			logFile = pref.getString(PREF_LOGFILE, null);
		}
		if (logFile == null) {
			logFile = Environment.getExternalStorageDirectory().toString()
					+ File.separator + LOGFILE;
		}
	}

	public static String getLogFileName(Context context) {
		readyLog(context);
		Log.d("pref", logFile);
		return logFile;
	}

	@SuppressLint("SimpleDateFormat")
	public static boolean writeLog(Context context, String text) {
		// skip empty log text
		if ((text == null) || (text.length() == 0)) {
			return false;
		}

		readyLog(context);
		try {
			FileWriter log = new FileWriter(logFile, true);
			log.write(String.valueOf(System.currentTimeMillis())
					+ " "
					+ new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z")
							.format(new Date()) + " " + text);
			log.close();
			return true;
		} catch (Exception e) {
			Log.d("logerr", text);
		}

		return false;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		// Handle wifi scan result
		if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			readyWifi(context);
			List<ScanResult> results = wifi.getScanResults();
			for (ScanResult R : results) {
				writeLog(context, "WIFI " + R.BSSID + " " + R.level + " "
						+ R.SSID);
			}
			context.sendBroadcast(new Intent(ACTION_UPDATE));
			return;
		}

		// No action, it should be my alarm intent
		doScan(context);
	}
}
