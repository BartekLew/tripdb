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
						ADD_REQ_CODE
					);
				}
			}
		);
		trips = new TripList(this, findViewById(R.id.trips));
	}

	private static int ADD_REQ_CODE = 0x0fe01;
	TripList trips;
}

