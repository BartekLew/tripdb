package me.leo.tripdb;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TripList {
	public TripList (Activity parent, ListView v) {
		lv = v;

		refresh();
	}

	List<Trip> load() {
		try {
			if(data == null) {
				data = new ArrayList<Trip>();

				for (Object obj: (JSONArray) new JSONParser().parse(
					"[[{\"where\": \"Warszawa\", \"when\": \"12.06 12:00 (2019)\"}, {\"where\": \"Gdańsk\", \"when\": \"12.06 14:00 (2019)\"}]]"
				)){
					data.add(new Trip((JSONArray) obj));
				}
			}

			return data;
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("tripdb", e.toString());
			return new JSONArray();
		}
	}

	public Trip at(int index) {
		return data.get(index);
	}

	public void add(String json) {
		try {
			Trip t = new Trip(json);
			data.add(t);

			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(int idx, String json) {
		try {
			Trip t = new Trip(json);
			data.set(idx, t);

			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refresh() {
		lv.setAdapter(new ArrayAdapter<Object> (
			This.get(), R.layout.list_items, load().toArray()
		));
	}

	ListView lv;
	ArrayList<Trip> data;
}
