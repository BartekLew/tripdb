package me.leo.tripdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.util.DisplayMetrics;

public class This extends Activity {
	public void onCreate(Bundle state) {
		super.onCreate(state);

		single = this;

		metrics = new DisplayMetrics();
		getWindowManager()
			.getDefaultDisplay()
			.getMetrics(metrics);
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

	public static int width() { return single.metrics.widthPixels; }
	public static int height() { return single.metrics.heightPixels; }
	DisplayMetrics metrics;

	public static Activity get() { return single; }
	public static ViewGroup root() {return rootView;}

	static This single;
	static ViewGroup rootView;
}
