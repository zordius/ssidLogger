package org.zordius.ssidlogger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		syncStatus();
	}

	public void syncStatus() {
		Log.d("wifi service",
				(getPackageManager().getComponentEnabledSetting(
						new ComponentName(this, WifiReceiver.class)) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) ? "off"
						: "on");
	}

	public void onClickLog(View v) {
		boolean logOn = ((ToggleButton) v).isChecked();
		getPackageManager().setComponentEnabledSetting(
				new ComponentName(this, WifiReceiver.class),
				logOn ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
						: PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
		WifiReceiver.setScan(this, logOn);
	}
}
