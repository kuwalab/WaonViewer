package net.kuwalab.android.waonviewer;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ConfigurationActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(l);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(l);

	}

	OnSharedPreferenceChangeListener l = new OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (key.equals("enableAutoLaunch")) {
				Boolean check = sharedPreferences.getBoolean(key, false);
				PackageManager pm = getPackageManager();
				Class<WaonViewerActivity> waonClass = WaonViewerActivity.class;

				if (check) {
					pm.setComponentEnabledSetting(
							new ComponentName(waonClass.getPackage().getName(),
									waonClass.getCanonicalName()),
							PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
							PackageManager.DONT_KILL_APP);
				} else {
					pm.setComponentEnabledSetting(
							new ComponentName(waonClass.getPackage().getName(),
									waonClass.getCanonicalName()),
							PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
							PackageManager.DONT_KILL_APP);
				}
			}
		}
	};
}
