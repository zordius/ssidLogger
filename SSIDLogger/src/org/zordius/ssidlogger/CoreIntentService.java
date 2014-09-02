package org.zordius.ssidlogger;

import java.io.FileWriter;
import java.util.List;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CoreIntentService extends IntentService {
	private static final String ACTION_START = "org.zordius.ssidlogger.action.STARTLOG";
	private static final String ACTION_STOP = "org.zordius.ssidlogger.action.STOPLOG";
	private static final String ACTION_EDIT = "org.zordius.ssidlogger.action.EDITCFG";

	private static final String EXTRA_CFGNAME = "org.zordius.ssidlogger.extra.CFGNAME";
	private static final String EXTRA_CFGVAL = "org.zordius.ssidlogger.extra.CFGVAL";

	private static final String CFG_LOGFILE = "logFile";

	private BroadcastReceiver wifiReceiver = null;
	private String strLogFile = null;
	private SharedPreferences settings = null;

	/**
	 * Starts this service to perform action START or STOP.
	 * 
	 * @see IntentService
	 */
	public static void startActionToggle(Context context, boolean enable) {
		if (enable) {
			CoreIntentService.startActionStart(context);
		} else {
			CoreIntentService.startActionStop(context);
		}
	}

	/**
	 * Starts this service to perform action START.
	 * 
	 * @see IntentService
	 */
	public static void startActionStart(Context context) {
		Intent intent = new Intent(context, CoreIntentService.class);
		intent.setAction(ACTION_START);
		context.startService(intent);
	}

	/**
	 * Starts this service to perform action STOP.
	 * 
	 * @see IntentService
	 */
	public static void startActionStop(Context context) {
		Intent intent = new Intent(context, CoreIntentService.class);
		intent.setAction(ACTION_STOP);
		context.startService(intent);
	}

	public void writeLog(String str) {
		try {
			FileWriter logFile = new FileWriter(strLogFile, true);
			logFile.write(str);
			logFile.close();
		} catch (Exception e) {
			// TODO: error handling
		}
	}

	/**
	 * Starts this service to perform action SETTING.
	 * 
	 * @see IntentService
	 */
	public static void startActionEdit(Context context, String name,
			String value) {
		Intent intent = new Intent(context, CoreIntentService.class);
		intent.setAction(ACTION_EDIT);
		intent.putExtra(EXTRA_CFGNAME, name);
		intent.putExtra(EXTRA_CFGVAL, value);
		context.startService(intent);
	}

	public CoreIntentService() {
		super("CoreIntentService");
		Log.d("CORE", "service init....");

	}

	@Override
	public void onCreate() {
		super.onCreate();
		resolveLogFileName();
		Log.d("CORE", "service created....");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("CORE", "intent received....");
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_START.equals(action)) {
				Log.d("CORE", "log start....");
				handleActionStart();
			} else if (ACTION_STOP.equals(action)) {
				Log.d("CORE", "log stop....");
				handleActionStop();
			} else if (ACTION_EDIT.equals(action)) {
				final String name = intent.getStringExtra(EXTRA_CFGNAME);
				final String value = intent.getStringExtra(EXTRA_CFGVAL);
				handleActionEdit(name, value);
			}
		}
	}

	private void resolveLogFileName() {
		if (strLogFile == null) {
			settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			strLogFile = settings.getString(CFG_LOGFILE, null);
		}
	}

	private void handleActionStart() {
		Log.d("CORE", "start logging...");
	}

	private void handleActionStop() {
		Log.d("CORE", "stop logging...");
	}

	private void handleActionEdit(String name, String value) {
		// TODO: Handle action STOP
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
