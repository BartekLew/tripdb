package me.leo.tripdb;

import me.leo.tripdb.R;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class MainActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		trips = new TripList(this, findViewById(R.id.trips));
	}

	TripList trips;
}

class TripItem {
	public TripItem (JSONObject o) {
		data = o;
	}	
		
	public TripItem (String where, LocalDateTime when) {
		data.put("where", where);
		data.put("when", when);
	}

	public String where() { return data.get("where").toString(); }
	public String when() { return data.get("when").toString(); }
		
	@Override
	public String toString() {
		return String.format("%s @ %s", where(), when());
	}

	JSONObject data;
}

class Trip {
	public Trip (JSONArray items) {
		data = new ArrayList<TripItem>();
		for(Object i : items)
			data.add(new TripItem((JSONObject) i));
	}

	TripItem item(int id) {return data.get(id);}

	@Override
	public String toString() {
		return String.format("%s - %s\n%s", item(0).where(), item(data.size()-1).where(), item(0).when());
	}

	ArrayList<TripItem> data;
}

class TripList {
	public TripList (Activity parent, ListView v) {
		v.setAdapter(new ArrayAdapter<Object> (
			parent, R.layout.list_items, load().toArray()
		));
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

	ArrayList<Trip> data;
}
