package me.leo.tripdb;

import android.widget.LinearLayout;
import android.os.Bundle;

public class TripEditor extends This {
	public void onCreate(Bundle state) {
		super.onCreate(state);

		trip = new Trip();
		setContentView(rootView = new DefaultLayout(
			LinearLayout.VERTICAL, DefaultLayout.fillBoth
		).with(trip.editor()));
	}

	Trip trip;
}
