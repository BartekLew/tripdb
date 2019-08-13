package me.leo.tripdb;

import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

public class Button extends TextView {
	public Button (String caption, OnClickListener handler) {
		super(This.get());

		setText(caption);
		setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
		));
		setTextSize(15);
		setPadding(10,5,5,5);

		setOnClickListener(handler);
	}
}
