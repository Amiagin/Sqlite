package com.demo.goof;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GoofListViewAdapter extends BaseAdapter {
	private Context context = null;
	private List<String>items;
	
	public GoofListViewAdapter(List<String> items,Context context) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.items, null);
		if (v != null) {
			TextView textView = (TextView)v.findViewById(R.id.textview);
			if (textView != null) {
				textView.setText(this.items.get(position));
			}
		}
		return v;
	}
}
