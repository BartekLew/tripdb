package me.leo.tripdb;

import me.leo.tripdb.R;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.view.View.OnClickListener;

public class TripsUI extends This {
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.main);

		rootView = findViewById(R.id.main);

		((View)findViewById(R.id.add)).setOnClickListener(
			new OnClickListener() {
				public void onClick (View button) {
					startActivityForResult(
						new Intent(get(), TripEditor.class),
						NEW_REQ_CODE
					);
				}
			}
		);

		ListView tripsView = findViewById(R.id.trips);
		trips = new TripList(this, tripsView);

		tripsView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> lv, View v, int position, long id) {
				Intent i = new Intent(get(), TripEditor.class);
				i.putExtra("Trip", trips.at(position).json());
				i.putExtra("pos", position);
				startActivityForResult(i, ADD_REQ_CODE);
			}
		});
	}

	public void onActivityResult(int reqCode, int resultCode, Intent i) {
		try {
			if(reqCode == NEW_REQ_CODE)
				trips.add(i.getStringExtra("Trip"));
			else
				trips.update(i.getIntExtra("pos", -1),
						i.getStringExtra("Trip")
				);
		} catch (Exception e) {} // case of cancelling add/new activity
	}

	private static int NEW_REQ_CODE = 0x0fe01;
	private static int ADD_REQ_CODE = 0x0fe02;
	TripList trips;
}

