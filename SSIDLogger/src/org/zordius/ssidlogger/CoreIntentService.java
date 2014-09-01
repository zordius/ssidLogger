package org.zordius.ssidlogger;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CoreIntentService extends IntentService {
	// TODO: Rename actions, choose action names that describe tasks that this
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_START = "org.zordius.ssidlogger.action.STARTLOG";
	private static final String ACTION_STOP = "org.zordius.ssidlogger.action.STOPLOG";
	private static final String ACTION_EDIT = "org.zordius.ssidlogger.action.EDITCFG";

	// TODO: Rename parameters
	private static final String EXTRA_CFGNAME = "org.zordius.ssidlogger.extra.CFGNAME";
	private static final String EXTRA_CFGVAL = "org.zordius.ssidlogger.extra.CFGVAL";

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

	/**
	 * Starts this service to perform action SETTING.
	 * 
	 * @see IntentService
	 */
	public static void startActionEdit(Context context, String name, String value) {
		Intent intent = new Intent(context, CoreIntentService.class);
		intent.setAction(ACTION_EDIT);
		intent.putExtra(EXTRA_CFGNAME, name);
		intent.putExtra(EXTRA_CFGVAL, value);
		context.startService(intent);
	}

	public CoreIntentService() {
		super("CoreIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_START.equals(action)) {
				handleActionStart();
			} else if (ACTION_STOP.equals(action)) {
				handleActionStop();
			} else if (ACTION_EDIT.equals(action)) {
				final String name = intent.getStringExtra(EXTRA_CFGNAME);
				final String value = intent.getStringExtra(EXTRA_CFGVAL);
				handleActionEdit(name, value);
			}
		}
	}

	/**
	 * Handle action START
	 */
	private void handleActionStart() {
		// TODO: Handle action START
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Handle action STOP
	 */
	private void handleActionStop() {
		// TODO: Handle action STOP
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Handle action EDIT
	 */
	private void handleActionEdit(String name, String value) {
		// TODO: Handle action STOP
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
