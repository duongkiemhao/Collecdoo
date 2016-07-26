package com.collecdoo.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.collecdoo.R;
import com.collecdoo.dto.CountryInfo;

import java.util.List;


public class MySimpleSpinnerAdapter extends ArrayAdapter<CountryInfo> {
	private Context context;
	public List<CountryInfo> list;


	public MySimpleSpinnerAdapter(Context context, int textViewResourceId,
								  List<CountryInfo> list) {
		super(context, textViewResourceId, list);
		this.context=context;
		this.list = list;
	}

	public int findPosition(String name){
		for (int i=0;i<list.size();i++){
			if(list.get(i).value.equals(name))
				return i;
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.simple_spinner_view, null);
		((TextView) v.findViewById(R.id.textView)).setText("");
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.simple_spinner_view, null);
		((TextView) v.findViewById(R.id.textView)).setText(list
				.get(position).value);
		return v;
	}

}
