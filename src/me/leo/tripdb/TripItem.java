package me.leo.tripdb;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;



class TripItem {
	public TripItem (JSONObject o) {
		data = o;
		try {
			realdate = datefmt.parse(when());
		} catch (Exception e){}
	}	

	public TripItem () {
		data = new JSONObject();
		data.put("where", "");
		data.put("when", "");
	}
		
	public TripItem (TripItem base) {
		this();
		data.put("when", base.when());
		try {
			realdate = datefmt.parse(when());
		} catch (Exception e){}
	}

	public TripItem (String where, LocalDateTime when) {
		data.put("where", where);
		data.put("when", when);
	}

	public String where() { return data.get("where").toString(); }
	public String when() { return data.get("when").toString(); }
	public String relativeWhen() {
		long diff = realdate.getTime() - new Date().getTime();
		boolean past = diff < 0;
		diff = Math.abs(diff);
		long sec = diff / 1000;
		long min = sec / 60;
		sec = sec % 60;
		long hr = min / 60;
		min = min % 60;
		long d = hr / 24;
		hr = hr % 24;

		if(past)
			return String.format("%d dni %02d:%02d temu",
				d, hr, min
			);
		else
			return String.format("za %d dni %.0d:%02d",
				d, hr, min
			);
	}

	public View editor(Trip trip){
		int s = (This.width() - 20) / 2;
		TripItem item = this;
		return new DefaultLayout(LinearLayout.HORIZONTAL, 0)
			.with(new TextWidget()
				.withWidth(s).withHint("gdzie?")
				.withModel(data, "where").withFocus())
			.with(new Space(20, 0))
			.with(new DateWidget(realdate)
				.withHandler(new NewDateHandler() {
					public void newDate(Calendar c, DateWidget sender) {
						data.put("when", datefmt.format(c.getTime()));
						realdate = c.getTime();
						sender.clearFocus();
						trip.newItem(item);
					}
				})
				.withWidth(s)
				.withHint("kiedy?"));
	}

	public String json() {
		return data.toJSONString();
	}
	
	public JSONObject jsonObj() {
		return data;
	}
		
	@Override
	public String toString() {
		return String.format("%s @ %s", where(), when());
	}

	public static final SimpleDateFormat datefmt = new SimpleDateFormat("dd.MM HH:mm (yyyy)");
	Date realdate;
	JSONObject data;
}

class TextWidget extends EditText {
	public static int MULTILINE = 1;
	public static int ONELINE = 0;

	public TextWidget(int type) {
		super(This.get());

		setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
		));

		if (type != MULTILINE) setSingleLine(true);

		setTextAppearance(This.get(), android.R.attr.textAppearanceLarge);
	}

	public TextWidget() {
		this(ONELINE);
	}

	public TextWidget of (int inputType) {
		setInputType(inputType);
		return this;
	}

	public TextWidget withText(String s) {
		setText(s);
		return this;
	}

	public TextWidget withHint (String hint) {
		setHint(hint);
		return this;
	}

	public TextWidget withWidth(int pt) {
		setWidth (pt);
		return this;
	}

	public TextWidget withModel(JSONObject o, String key) {
		setText(o.get(key).toString());
		addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) {
				o.put(key, getText().toString());
			}
			public void onTextChanged(CharSequence c,int a,int b,int d) {}
			public void beforeTextChanged(CharSequence c,int a,int b,int d) {}
		});
		
		return this;
	}

	public TextWidget withFocus() {
		requestFocus();
		This.get().getWindow().setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
		);
		return this;
	}
}

interface NewDateHandler {
	public void newDate(Calendar c, DateWidget parent);
}

class DateWidget extends TextWidget {
	public DateWidget() {
		super();
		setInputType(InputType.TYPE_CLASS_DATETIME);
		setFocusable(false);

		value = Calendar.getInstance();
		setOnClickListener(new DateTimePicker(this, value));
	}

	public DateWidget(Date d) {
		this();
		if(d != null) {
			value = Calendar.getInstance();
			value.setTime(d);
			setText(TripItem.datefmt.format(d));
		}
	}

	public void newValue(Calendar c) {
		value = c;
		setText(TripItem.datefmt.format(c.getTime()));
		if (newDateHandler != null)
			newDateHandler.newDate(c, this);
	}

	public DateWidget withHandler(NewDateHandler h) {
		newDateHandler = h;
		return this;
	}

	NewDateHandler newDateHandler;
	Calendar value;
}

class DateTimePicker implements OnClickListener,
				DatePickerDialog.OnDateSetListener,
				TimePickerDialog.OnTimeSetListener{
	public DateTimePicker (DateWidget resultTaker, Calendar initial) {
		output = resultTaker;
		c = initial;
	}

	public void onClick (View sender) {
		new DatePickerDialog(This.get(), this, c.get(Calendar.YEAR),
			c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
		).show();
	}

	@Override
	public void onDateSet(DatePicker view, int yr, int mon, int day) {
		c.set(Calendar.YEAR, yr);
		c.set(Calendar.MONTH, mon);
		c.set(Calendar.DAY_OF_MONTH, day);

		new TimePickerDialog(This.get(), this,
			c.get(Calendar.HOUR_OF_DAY),
			c.get(Calendar.MINUTE), false).show();
	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		output.newValue(c);
	}

	Calendar c;
	DateWidget output;
}

class Space extends TextView {
	public Space(int w, int h) {
		super(This.get());
		setWidth(w);
		setHeight(h);
	}
}

