package net.kuwalab.android.waonviewer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class WaonWidget extends AppWidgetProvider {
	public static final String ACTION = "net.kuwalab.android.waonviewer.UPDATE";
	protected static final String PREFERENCES_NAME = "WAON_DATA";
	protected static final String PREFERENCES_CONF_DATE = "conf_date";
	protected static final String PREFERENCES_REST_MONEY = "rest_money";

	public void onUpdate(Context context, AppWidgetManager awm, int[] ids) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_PRIVATE);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_main);
		remoteViews.setTextViewText(R.id.confDate,
				pref.getString(PREFERENCES_CONF_DATE, "未確認"));
		remoteViews.setTextViewText(R.id.widetRest,
				pref.getString(PREFERENCES_REST_MONEY, "￥--,---"));
		awm.updateAppWidget(new ComponentName(context, WaonWidget.class),
				remoteViews);
	}
}
