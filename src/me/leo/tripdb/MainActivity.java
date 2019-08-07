package me.leo.tripdb;

import me.leo.tripdb.R;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

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
	public static View root() {return rootView;}

	static Activity single;
	static View rootView;
	TripList trips;

	class AddButtonAction implements OnClickListener {
		public AddButtonAction (Activity activity, View button) {
			a = activity;
			button.setOnClickListener(this);
		}

		public void onClick (View button) {
			setContentView(rootView = new DefaultLayout(
				LinearLayout.HORIZONTAL, DefaultLayout.fillBoth
			).with(new TripItem().editor(a, root())));
;
		}

		Activity a;
	}
	AddButtonAction handler;
}

class DefaultWidget<T extends View> {
	public DefaultWidget(T base) {
		control = base;
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		base.setLayoutParams(lparams);
	}

	public T commit() {return control;}

	T control;
}

class DefaultLayout extends LinearLayout {
	public static int horizontalFill = 1;
	public static int verticalFill = 2;
	public static int fillBoth = 3;

	public DefaultLayout (int orientation, int fillType) {
		super(MainActivity.get());
		setOrientation(orientation);
		setLayoutParams(new LayoutParams(
			((fillType & horizontalFill) != 0)? LayoutParams.FILL_PARENT : LayoutParams.WRAP_CONTENT,
			((fillType & verticalFill) != 0)? LayoutParams.FILL_PARENT : LayoutParams.WRAP_CONTENT
		));
		setPadding(10, 10, 10, 10);
	}

	public DefaultLayout with(View v) {
		addView(v);
		return this;
	}
}


class TextWidget extends EditText {
	public TextWidget() {
		super(MainActivity.get());

		setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
		));

		setTextAppearance(MainActivity.get(), android.R.attr.textAppearanceLarge);
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
}

class Space extends TextView {
	public Space(int w, int h) {
		super(MainActivity.get());
		setWidth(w);
		setHeight(h);
	}
}

class TripItem {
	public TripItem (JSONObject o) {
		data = o;
	}	

	public TripItem () {
		data = new JSONObject();
		data.put("where", "");
		data.put("when", "");
	}
		
	public TripItem (String where, LocalDateTime when) {
		data.put("where", where);
		data.put("when", when);
	}

	public String where() { return data.get("where").toString(); }
	public String when() { return data.get("when").toString(); }

	public View editor(Activity a, View parent) {
		int size = parent.getWidth() / 2 - 30;
		return new DefaultLayout(LinearLayout.HORIZONTAL, DefaultLayout.horizontalFill)
			.with(new TextWidget().withWidth(size).withHint("gdzie?"))
			.with(new Space(20, 0))
			.with(new TextWidget().withWidth(size).withHint("kiedy?"));
	}
		
	@Override
	public String toString() {
		return String.format("%s @ %s", where(), when());
	}

	JSONObject data;
}

class Trip {
	public Trip (JSONArray items) {
		data = new ArrayList<TripItem>();
		for(Object i : items)
			data.add(new TripItem((JSONObject) i));
	}

	TripItem item(int id) {return data.get(id);}

	@Override
	public String toString() {
		return String.format("%s - %s\n%s", item(0).where(), item(data.size()-1).where(), item(0).when());
	}

	ArrayList<TripItem> data;
}

class TripList {
	public TripList (Activity parent, ListView v) {
		v.setAdapter(new ArrayAdapter<Object> (
			parent, R.layout.list_items, load().toArray()
		));
	}

	List<Trip> load() {
		try {
			if(data == null) {
				data = new ArrayList<Trip>();

				for (Object obj: (JSONArray) new JSONParser().parse(
					"[[{\"where\": \"Warszawa\", \"when\": \"12.06.2019 12:00\"}, {\"where\": \"Gdańsk\", \"when\": \"12.06.2019 14:00\"}]]"
				)){
					data.add(new Trip((JSONArray) obj));
				}
			}

			return data;
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("tripdb", e.toString());
			return new JSONArray();
		}
	}

	ArrayList<Trip> data;
}
