package me.leo.tripdb;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.util.DisplayMetrics;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

	static void ensurePerm(String permid) {
		if (ContextCompat.checkSelfPermission(single, permid)
        		!= PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(single,permid)) {
				ensurePerm(permid);
			} else {
        			ActivityCompat.requestPermissions(single,
                			new String[]{permid},
                			MY_PERMISSIONS_REQUEST_READ_CONTACTS);
			}
		}
	}

	public static int width() { return single.metrics.widthPixels; }
	public static int height() { return single.metrics.heightPixels; }
	DisplayMetrics metrics;

	public static This get() { return single; }
	public static ViewGroup root() {return rootView;}

	static This single;
	static ViewGroup rootView;

	private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
}
