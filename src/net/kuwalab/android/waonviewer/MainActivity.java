package net.kuwalab.android.waonviewer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private NfcHelper nfcHelper;
	private ComponentName waon;
	private ComponentHelper componentHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		nfcHelper = new NfcHelper(this);

		Class<WaonViewerActivity> waonClass = WaonViewerActivity.class;
		waon = new ComponentName(waonClass.getPackage().getName(),
				waonClass.getCanonicalName());
		componentHelper = new ComponentHelper(getApplicationContext(),
				getPackageManager(), waon);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			settings();
			return true;
		}
		return false;
	}

	private void settings() {
		startActivity(new Intent(this, ConfigurationActivity.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		nfcHelper.enableForegroundDispatch();
	}

	@Override
	public void onPause() {
		super.onPause();
		nfcHelper.disableForegroundDispatch();
		componentHelper.disabledComponentStatus();
	}

	public void onNewIntent(Intent intent) {
		componentHelper.enableComponentStatus();

		// Class<WaonViewerActivity> waonClass = WaonViewerActivity.class;
		Intent launchIntent = new Intent();
		launchIntent.setComponent(waon);
		launchIntent.putExtras(intent);
		startActivity(launchIntent);
	}
}
