package net.kuwalab.android.waonviewer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;

public class NfcHelper {
	private Activity activity;
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private IntentFilter ndef;
	private IntentFilter[] intentFilters;
	private String[][] techLists;

	public NfcHelper(Activity activity) {
		this.activity = activity;
		setupNfcAdapter();
	}

	public void enableForegroundDispatch() {
		nfcAdapter.enableForegroundDispatch(activity, pendingIntent,
				intentFilters, techLists);
	}

	public void disableForegroundDispatch() {
		nfcAdapter.disableForegroundDispatch(activity);
	}

	private void setupNfcAdapter() {
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity
				.getApplicationContext());
		if (nfcAdapter == null) {
			return;
		}

		pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(
				activity, activity.getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Unable to speciy */* Mime Type", e);
		}
		intentFilters = new IntentFilter[] { ndef };

		techLists = new String[][] { new String[] { NfcF.class.getName() } };
	}
}
