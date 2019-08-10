package me.leo.tripdb;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Trip {
	public Trip (JSONArray items) {
		data = new ArrayList<TripItem>();
		for(Object i : items)
			data.add(new TripItem((JSONObject) i));
	}

	public Trip() {
		data = new ArrayList<TripItem>();
	}

	TripItem item(int id) {return data.get(id);}

	public ViewGroup editor() {
		if(editor == null)
			editor = new DefaultLayout(
				LinearLayout.VERTICAL, DefaultLayout.fillBoth
			).with(childEditors());
		return editor;
	}

	private List<View> childEditors() {
		data.add(new TripItem());

		List<View> l = new ArrayList<View>();
		for(TripItem v: data)
			l.add(v.editor(this));

		return l;
	}

	public void newItem() {
		TripItem i = new TripItem();
		data.add(i);
		editor().addView(i.editor(this));
	}

	@Override
	public String toString() {
		return String.format("%s - %s\n%s", item(0).where(), item(data.size()-1).where(), item(0).when());
	}

	ViewGroup editor;
	ArrayList<TripItem> data;
}

