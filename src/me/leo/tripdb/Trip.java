package me.leo.tripdb;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class Trip {
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

