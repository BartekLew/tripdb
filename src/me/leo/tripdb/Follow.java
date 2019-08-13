package me.leo.tripdb;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.IBinder;
import android.os.Handler;

public class Follow extends Service {
	@Override
	public void onCreate() {
		super.onCreate();

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		msg = new TextView(this);
		msg.setText("Hello world!");
		msg.setTextSize(15);
		msg.setPadding(10,10,10,10);

		WindowManager.LayoutParams msgPos = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_TOAST,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.RGB_888);
		msgPos.gravity = Gravity.TOP | Gravity.LEFT;
		msgPos.x = 0;
		msgPos.y = 100;

		msg.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				switch(e.getAction()) {
					case MotionEvent.ACTION_MOVE:
						msgPos.x += e.getRawX() - x;
						msgPos.y += e.getRawY() - y;
						wm.updateViewLayout(msg,msgPos);

					case MotionEvent.ACTION_DOWN:
						x = e.getRawX();
						y = e.getRawY();
						return true;
				}
				return false;
			}

			float x, y;
		});

		wm.addView(msg, msgPos);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String json = intent.getStringExtra("Trip");

		try {
			trip = new Trip(json);

			Handler handler = new Handler();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// This is must because Views can be edited only
					// by threads they are created with.
					handler.post(new Runnable() {
						@Override
						public void run() {
							msg.setText(trip.relativeTiming());
						}
					});
				}
			}, 0, 60000);

			return START_NOT_STICKY;
		} catch (Exception e) {
			System.err.println("tripdb/Follow/init caught" + e.toString());
			e.printStackTrace();
			return -1;
		}	
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if(msg != null) wm.removeView(msg);
	}

	@Override
	public IBinder onBind(Intent par) { return null; }

	Timer timer;
	Trip trip;
	WindowManager wm;
	TextView msg;
}
