package me.leo.tripdb;

import java.util.List;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.view.View;
import android.view.ViewGroup;

public class DefaultLayout extends LinearLayout {
	public static int horizontalFill = 1;
	public static int verticalFill = 2;
	public static int fillBoth = 3;

	public DefaultLayout (int orientation, int fillType) {
		super(This.get());
		setOrientation(orientation);
		setLayoutParams(new LayoutParams(
			((fillType & horizontalFill) != 0)? LayoutParams.FILL_PARENT : LayoutParams.WRAP_CONTENT,
			((fillType & verticalFill) != 0)? LayoutParams.FILL_PARENT : LayoutParams.WRAP_CONTENT
		));
		setPadding(10, 10, 10, 10);
	}

	public DefaultLayout withPadding(int l, int t, int r, int b) {
		setPadding(l, t, r, b);
		return this;
	}

	public DefaultLayout with(View v) {
		addView(v);
		return this;
	}

	public DefaultLayout with(List<View> l) {
		for(View v : l)
			addView(v);

		return this;
	}
}

