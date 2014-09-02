package org.zordius.ssidlogger;

import android.app.Activity;
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
		Log.d("wifi service", WifiReceiver.isEnabled(this) ? "on" : "of");
	}

	public void onClickLog(View v) {
		WifiReceiver.toggleScan(this, ((ToggleButton) v).isChecked());
	}
	
	public void onClickScan(View v) {
		WifiReceiver.doScan(this);
	}
}
