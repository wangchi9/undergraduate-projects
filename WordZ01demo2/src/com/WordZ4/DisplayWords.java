package com.WordZ4;

import com.WordZ4.Globals;
import com.WordZ4.ShowDef;
import com.example.WordZ.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DisplayWords extends Activity implements OnClickListener {

	public static int currentIndex;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_def_button:
			Intent i = new Intent(this, ShowDef.class);
			startActivity(i);

			break;

		// More buttons go here (if any) ...
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(this,MainActivity.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

	/**
	 * This initial function get all the index of words in Globals and put the
	 * in the corresponding global level list.
	 */
	public void iniGenInd() {

		Log.i("debug", "in iniGenInd");

		int i = 0;
		for (i = 0; i < Globals.wordarray.size(); i++) {
			String color_background;
			try {
				color_background = Integer.toString(Globals.wordarray.get(i)
						.getBackgroundRes());
			} catch (Exception e) {
				Log.i("debug", "no color in index" + i);
				color_background = Integer.toString(R.color.btn_holo_grey);
			}

			if (color_background.equals(Integer.toString(R.color.btn_red))) {
				Globals.level1_list.add(i);
			} else if (color_background.equals(Integer
					.toString(R.color.btn_yellow))) {
				Globals.level2_list.add(i);
			} else if (color_background.equals(Integer
					.toString(R.color.btn_green))) {
				Globals.level3_list.add(i);
			} else if (color_background.equals(Integer
					.toString(R.color.btn_blue))) {
				Globals.level4_list.add(i);
			} else {
				Globals.levelnone_list.add(i);
			}

		}

	}

	/**
	 * This function returns the index we needs for this turn and update the
	 * current index
	 ** 
	 **/
	private int getRandom(int min, int max) {
		int Randnumber;
		int Min = min;
		int Max = max;
		Randnumber = Min + (int) (Math.random() * ((Max - Min) + 1));
		return Randnumber;

	}

	public int genInd() {

		// Only run this function when it's getting into this method
		// from out side of the play functionality(display_Words and show_def)
		if (Globals.out_of_play) {

			Log.i("debug", "In display genInd function star initail");
			iniGenInd();
			// turn off the out_of_play
			Globals.out_of_play = false;
		}
		int index = 0;

		int min_level1 = getRandom(3, 8);
		int min_level2 = getRandom(8, 15);
		int min_level3 = getRandom(12, 20);

		if (Globals.level1_list.size() > min_level1) {
			// get the fist one and remove
			Log.i("genInd", "1");
			index = Globals.level1_list.get(0);
			Globals.level1_list.remove(0);

		} else if (Globals.level2_list.size() > min_level2) {
			Log.i("genInd", "2");
			// get the fist on and remove
			index = Globals.level2_list.get(0);
			Globals.level2_list.remove(0);

		} else if (Globals.level3_list.size() > min_level3) {
			Log.i("genInd", "3");
			// get the fist on and remove
			index = Globals.level3_list.get(0);
			Globals.level3_list.remove(0);

		} else {
			// get word from any list randomly

			int Randnumber = getRandom(0, 110);
			if (Randnumber < 0 || Randnumber >110) {
				Log.i("debug", "!!!ERROR check genInd in display");

			}
			Log.i("Randnumber", "Randnumber is" + Integer.toString(Randnumber));
			if ((Randnumber >= 0) && (Randnumber < 70)) {
				if (Globals.levelnone_list.size() > 0) {

					// get the first one on level none and remove it
					index = Globals.levelnone_list.get(0);
					Globals.levelnone_list.remove(0);

				} else if (Globals.level2_list.size() > 0) {
					index = Globals.level2_list.get(0);
					Globals.level2_list.remove(0);

				} else if (Globals.level1_list.size() > 0) {
					index = Globals.level1_list.get(0);
					Globals.level1_list.remove(0);
				} else if (Globals.level3_list.size() > 0) {
					index = Globals.level3_list.get(0);
					Globals.level3_list.remove(0);

				} else if (Globals.level4_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level4_list.get(0);
					Globals.level4_list.remove(0);
				}

			} else if ((Randnumber >= 70) && (Randnumber < 90)) {
				// get the first one on level none and remove it
				if (Globals.level1_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level1_list.get(0);
					Globals.level1_list.remove(0);

				} else if (Globals.level2_list.size() > 0) {
					index = Globals.level2_list.get(0);
					Globals.level2_list.remove(0);
				} else if (Globals.level3_list.size() > 0) {
					index = Globals.level3_list.get(0);
					Globals.level3_list.remove(0);

				} else if (Globals.levelnone_list.size() > 0) {
					index = Globals.levelnone_list.get(0);
					Globals.levelnone_list.remove(0);

				} else if (Globals.level4_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level4_list.get(0);
					Globals.level4_list.remove(0);
				}

			} else if ((Randnumber >= 90) && (Randnumber < 100)) {

				if (Globals.level2_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level2_list.get(0);
					Globals.level2_list.remove(0);

				} else if (Globals.level1_list.size() > 0) {
					index = Globals.level1_list.get(0);
					Globals.level1_list.remove(0);

				} else if (Globals.level3_list.size() > 0) {
					index = Globals.level3_list.get(0);
					Globals.level3_list.remove(0);

				} else if (Globals.levelnone_list.size() > 0) {
					index = Globals.levelnone_list.get(0);
					Globals.levelnone_list.remove(0);

				} else if (Globals.level4_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level4_list.get(0);
					Globals.level4_list.remove(0);
				}

			} else if ((Randnumber >= 100) && (Randnumber < 105)) {
				// get the first one on level none and remove it
				if (Globals.level3_list.size() > 0) {

					// get the first one on level none and remove it
					index = Globals.level3_list.get(0);
					Globals.level3_list.remove(0);

				} else if (Globals.level1_list.size() > 0) {
					index = Globals.level1_list.get(0);
					Globals.level1_list.remove(0);

				} else if (Globals.level2_list.size() > 0) {
					index = Globals.level2_list.get(0);
					Globals.level2_list.remove(0);

				} else if (Globals.levelnone_list.size() > 0) {
					index = Globals.levelnone_list.get(0);
					Globals.levelnone_list.remove(0);

				} else if (Globals.level4_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level4_list.get(0);
					Globals.level4_list.remove(0);
				}

			} else if ((Randnumber >= 105) && (Randnumber < 110)) {
				// get the first one on level none and remove it

				if (Globals.level4_list.size() > 0) {
					// get the first one on level none and remove it
					index = Globals.level4_list.get(0);
					Globals.level4_list.remove(0);
				} else if (Globals.level1_list.size() > 0) {
					index = Globals.level1_list.get(0);
					Globals.level1_list.remove(0);

				} else if (Globals.level2_list.size() > 0) {
					index = Globals.level2_list.get(0);
					Globals.level2_list.remove(0);

				} else if (Globals.level3_list.size() > 0) {
					index = Globals.level3_list.get(0);
					Globals.level3_list.remove(0);

				} else if (Globals.levelnone_list.size() > 0) {
					index = Globals.levelnone_list.get(0);
					Globals.levelnone_list.remove(0);

				}
			}

		}

		currentIndex = index;
		return index;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// First check if display is accessed through view file
		// If so, when exit to display load globals to a single file
		if (Globals.in_view_words) {
			Globals.exit_play_singlefile = true;
			Globals.in_view_words = false;
		}

		if (Globals.in_main_activity) {
			Globals.exit_play_allfiles = true;
			Globals.in_main_activity = false;
		}

		// **Important** the index in globals are the key,
		// we used in as a bridge
		int index;
		index = genInd();
		// currentIndex=0;
		// index=0;
		Log.i("debug", "index is" + Integer.toString(index));

		// By knowing the index, we know which word it is and we choose the
		// display
		String color_background;
		try {
			color_background = Integer.toString(Globals.wordarray.get(index)
					.getBackgroundRes());
		} catch (Exception e) {
			Log.i("debug", "no color in index" + index);
			color_background = Integer.toString(R.color.btn_holo_grey);
		}

		if (color_background.equals(Integer.toString(R.color.btn_green))) {
			setContentView(R.layout.display_words_green);
		} else if (color_background
				.equals(Integer.toString(R.color.btn_yellow))) {
			setContentView(R.layout.display_words_yellow);
		} else if (color_background.equals(Integer.toString(R.color.btn_red))) {
			setContentView(R.layout.display_words_red);
		} else if (color_background.equals(Integer.toString(R.color.btn_blue))) {
			setContentView(R.layout.display_words_blue);
		} else {
			setContentView(R.layout.display_words_original);
		}

		// Use the index the access the word's content
		String s = Globals.wordarray.get(index).getContent();
		// debug sentence
		Log.i("Display", "in display string s is" + s);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(s);

		// Set up click listeners for all the buttons
		View continueButton = findViewById(R.id.show_def_button);
		continueButton.setOnClickListener(this);

	}

	public void setActivityBackgroundColor(int color) {
		View view = this.getWindow().getDecorView();
		view.setBackgroundColor(color);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}