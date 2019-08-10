package me.leo.tripdb;

import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;

public class TripEditor extends This {
	public void onCreate(Bundle state) {
		super.onCreate(state);

		trip = new Trip();
		setContentView(rootView = new DefaultLayout(
			LinearLayout.VERTICAL, DefaultLayout.fillBoth
		).with(trip.editor())
			.with(new Button("Zatwierdź", new OnClickListener() {
				public void onClick(View button) {
					respond(trip.json());
				}
			})));
	}

	Trip trip;
}
