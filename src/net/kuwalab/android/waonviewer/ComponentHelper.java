package net.kuwalab.android.waonviewer;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class ComponentHelper {
	private Context context;
	private PackageManager pm;
	private ComponentName waon;

	public ComponentHelper(Context context, PackageManager pm,
			ComponentName waon) {
		this.context = context;
		this.pm = pm;
		this.waon = waon;
	}

	public void enableComponentStatus() {
		if (pm.getComponentEnabledSetting(waon) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
			pm.setComponentEnabledSetting(waon,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		}
	}

	public void disabledComponentStatus() {
		if (pm.getComponentEnabledSetting(waon) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			boolean enableAutoLaunch = prefs.getBoolean("enableAutoLaunch",
					true);
			if (!enableAutoLaunch) {
				pm.setComponentEnabledSetting(waon,
						PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
						PackageManager.DONT_KILL_APP);
			}
		}

	}
}
