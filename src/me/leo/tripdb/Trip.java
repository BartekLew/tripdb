package me.leo.tripdb;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Trip {
	public Trip (JSONArray items) {
		data = new ArrayList<TripItem>();
		for(Object i : items) {
			data.add(new TripItem((JSONObject) i));
		}
	}

	public Trip (String json) throws ParseException {
		this((JSONArray) new JSONParser().parse(json));
	}

	public Trip() {
		data = new ArrayList<TripItem>();
	}

	TripItem item(int id) {return data.get(id);}

	public ViewGroup editor() {
		if(editor == null)
			editor = new DefaultLayout(
				LinearLayout.VERTICAL, 0
				).with(toolbar())
				.with(routeEd = new DefaultLayout(
					LinearLayout.VERTICAL, 0
					).with(childEditors()));
		return editor;
	}

	public ViewGroup routeEditor() {
		if(editor == null) editor();

		return routeEd;
	}

	private List<View> childEditors() {
		data.add(new TripItem());

		List<View> l = new ArrayList<View>();
		for(TripItem v: data)
			l.add(v.editor(this));

		return l;
	}

	private View toolbar() {
		return new DefaultLayout(
			LinearLayout.HORIZONTAL, DefaultLayout.horizontalFill
			).with(new Button("Mapa",
				new OnClickListener() {
					public void onClick(View v) {
						callMap();
					}
				}
			));
	}

	private void callMap() {
		This.get().startActivity(
			new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps"
					+ routeStr())));
	}

	private String routeStr() {
		return "?saddr=" + data.get(0).where()
				+ "&daddr=" + data.get(data.size()-2).where();
	}

	public void newItem() {
		TripItem i = new TripItem();
		data.add(i);
		routeEditor().addView(i.editor(this));
	}

	public void newItem(TripItem base) {
		if(base == data.get(data.size()-1)) {
			TripItem i = new TripItem(base);
			data.add(i);
			routeEditor().addView(i.editor(this));
		} else {
			int idx = data.indexOf(base) + 1;
			routeEditor().getChildAt(idx).requestFocus();
		}
	}

	public String json() {
		JSONArray a = new JSONArray();
		for(TripItem i: data)
			if(!i.where().isEmpty())
				a.add(i.jsonObj());

		return a.toJSONString();
	}

	@Override
	public String toString() {
		return String.format("%s - %s\n%s", item(0).where(), item(data.size()-1).where(), item(0).when());
	}

	ViewGroup editor;
	ViewGroup routeEd;
	ArrayList<TripItem> data;
}

