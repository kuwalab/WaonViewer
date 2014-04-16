package net.kuwalab.android.waonviewer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.kazzz.felica.FeliCaException;
import net.kazzz.felica.FeliCaTag;
import net.kazzz.felica.command.ReadResponse;
import net.kazzz.felica.lib.FeliCaLib.ServiceCode;
import net.kuwalab.android.util.HexUtil;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView messageText = (TextView) findViewById(R.id.messageText);
		messageText.setText(R.string.waon_first_step);

		Intent intent = getIntent();
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			onNewIntent(intent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Parcelable nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		re(nfcTag);
	}

	public void re(Parcelable nfcTag) {
		List<Map<String, String>> list = null;
		try {
			FeliCaTag f = new FeliCaTag(nfcTag);
			list = read(f);
			viewList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void viewList(List<Map<String, String>> list) {
		String nowRestMoney = list.get(0).get("restMoney");

		TextView messageText = (TextView) findViewById(R.id.messageText);
		messageText.setText(R.string.waon_rest);

		LinearLayout firstStepLayout = (LinearLayout) findViewById(R.id.firstStepLayout);
		firstStepLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		messageText.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		ListView historyListView = (ListView) findViewById(R.id.listView);
		historyListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		SimpleAdapter adapter = new WaonAdapter(this, list, R.layout.list,
				new String[] { "date", "time", "chargeMoney", "useMaoney",
						"restMoney", "typeName", "point" }, new int[] {
						R.id.date, R.id.time, R.id.chargeMoney, R.id.useMoney,
						R.id.restMoney, R.id.typeName, R.id.point });
		historyListView.setAdapter(adapter);

		SharedPreferences pref = getSharedPreferences(
				WaonWidget.PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		DateFormat df = DateFormat.getDateInstance();
		edit.putString(WaonWidget.PREFERENCES_CONF_DATE, df.format(new Date()));
		edit.putString(WaonWidget.PREFERENCES_REST_MONEY,
				getString(R.string.waon_yen_sign) + nowRestMoney);
		edit.commit();

		refresh(getApplicationContext());
	}

	private void refresh(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				WaonWidget.PREFERENCES_NAME, Context.MODE_PRIVATE);

		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_main);
		remoteViews.setTextViewText(R.id.confDate, pref.getString(
				WaonWidget.PREFERENCES_CONF_DATE,
				getString(R.string.waon_widget_state_nothing)));
		remoteViews.setTextViewText(R.id.widetRest, pref.getString(
				WaonWidget.PREFERENCES_REST_MONEY,
				getString(R.string.waon_yen_sign)
						+ getString(R.string.waon_money_format)));
		awm.updateAppWidget(new ComponentName(context, WaonWidget.class),
				remoteViews);
	}

	private List<Map<String, String>> read(FeliCaTag f) throws FeliCaException {
		List<Map<String, String>> list = null;

		// polling は IDm、PMmを取得するのに必要
		f.polling(0xFE00);

		// サービスコード読み取り
		ServiceCode sc = new ServiceCode(0x680B);
		byte addr = 0;
		ReadResponse result = f.readWithoutEncryption(sc, addr);
		if (result == null) {
			Toast.makeText(getBaseContext(), getString(R.string.waon_not_waon),
					Toast.LENGTH_LONG).show();
			return null;
		}
		list = new ArrayList<Map<String, String>>();

		List<WaonHistory> waonHistoryList = new ArrayList<WaonHistory>();
		int renban = 0;
		while (result != null && result.getStatusFlag1() == 0) {
			if (addr % 2 == 0) {
				byte[] bytes = result.getBlockData();
				renban = HexUtil.toInt(Arrays.copyOfRange(bytes, 13, 15));
				Log.i("renban", "" + renban);
				addr++;
				result = f.readWithoutEncryption(sc, addr);
				continue;
			}

			waonHistoryList.add(new WaonHistory(renban, result.getBlockData()));

			addr++;
			result = f.readWithoutEncryption(sc, addr);
			if (addr == 6) {
				break;
			}
		}
		Collections.sort(waonHistoryList);

		for (WaonHistory waonHistory : waonHistoryList) {
			list.add(waonHistory.getMap());
		}
		return list;
	}
}
