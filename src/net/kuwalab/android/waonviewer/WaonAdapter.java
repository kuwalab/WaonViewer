package net.kuwalab.android.waonviewer;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WaonAdapter extends SimpleAdapter {
	private LayoutInflater layoutInflater;

	public WaonAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		layoutInflater = LayoutInflater.from(parent.getContext());

		convertView = layoutInflater.inflate(R.layout.list, parent, false);
		ListView listView = (ListView) parent;

		Map<String, Object> data = (Map<String, Object>) listView
				.getItemAtPosition(position);

		TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
		dateTextView.setText((String) data.get("date"));

		TextView timeTextView = (TextView) convertView.findViewById(R.id.time);
		timeTextView.setText((String) data.get("time"));

		TextView chargeTextView = (TextView) convertView
				.findViewById(R.id.chargeMoney);
		chargeTextView.setText((String) data.get("chargeMoney"));

		TextView useTextView = (TextView) convertView
				.findViewById(R.id.useMoney);
		useTextView.setText((String) data.get("useMoney"));

		TextView restTextView = (TextView) convertView
				.findViewById(R.id.restMoney);
		restTextView.setText((String) data.get("restMoney"));

		TextView typeNameTextView = (TextView) convertView
				.findViewById(R.id.typeName);
		typeNameTextView.setText((String) data.get("typeName"));

		TextView pointTextView = (TextView) convertView
				.findViewById(R.id.point);
		pointTextView.setText((String) data.get("point"));

		return convertView;
	}
}
