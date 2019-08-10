package me.leo.tripdb;

import me.leo.tripdb.R;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		rootView = findViewById(R.id.main);
		single = this;

		handler = new AddButtonAction(this, findViewById(R.id.add));
		trips = new TripList(this, findViewById(R.id.trips));
	}

	public static Activity get() { return single; }
	public static ViewGroup root() {return rootView;}

	static Activity single;
	static ViewGroup rootView;
	TripList trips;

	class AddButtonAction implements OnClickListener {
		public AddButtonAction (Activity activity, View button) {
			a = activity;
			button.setOnClickListener(this);
		}

		public void onClick (View button) {
			setContentView(rootView = new DefaultLayout(
				LinearLayout.VERTICAL, DefaultLayout.fillBoth
			).with(new TripItem().editor(a, root().getWidth())));
		}

		Activity a;
	}
	AddButtonAction handler;
}

