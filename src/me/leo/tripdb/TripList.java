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
		v.setAdapter(new ArrayAdapter<Object> (
			parent, R.layout.list_items, load().toArray()
		));
		lv = v;
	}

	List<Trip> load() {
		try {
			if(data == null) {
				data = new ArrayList<Trip>();

				for (Object obj: (JSONArray) new JSONParser().parse(
					"[[{\"where\": \"Warszawa\", \"when\": \"12.06.2019 12:00\"}, {\"where\": \"Gdańsk\", \"when\": \"12.06.2019 14:00\"}]]"
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

	void add(String json) {
		try {
			Trip t = new Trip((JSONArray) new JSONParser().parse(json));
			data.add(t);

			lv.setAdapter(new ArrayAdapter<Object> (
				This.get(), R.layout.list_items, load().toArray()
			));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ListView lv;
	ArrayList<Trip> data;
}
