package me.leo.tripdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

public class This extends Activity {
	public void onCreate(Bundle state) {
		super.onCreate(state);

		single = this;
	}

	public static void refresh() {
		single.setContentView(rootView);
	}

	void respond(String s) {
		Intent i = getIntent();
		i.putExtra("Trip", s);
		setResult(RESULT_OK, i);
		finish();
	}

	public static Activity get() { return single; }
	public static ViewGroup root() {return rootView;}

	static Activity single;
	static ViewGroup rootView;
}
