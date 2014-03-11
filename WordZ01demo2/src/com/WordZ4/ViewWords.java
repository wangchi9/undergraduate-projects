package com.WordZ4;

import java.util.ArrayList;

import com.example.WordZ.R;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class ViewWords extends ExpandableListActivity implements OnClickListener {
	
	
	private ArrayList<Word> parentItems = new ArrayList<Word>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(this,LoadFile.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Globals.in_view_words=true;	
		Globals.in_main_activity=false;
		super.onCreate(savedInstanceState);
		Globals.out_of_play=true;
     	// this is not really  necessary as ExpandableListActivity contains an ExpandableList
		setContentView(R.layout.article_screen);
		View playButton = findViewById(R.id.viewwords_play_button);
		playButton.setOnClickListener(this);

		//Put the file name all the top of the lay out 
				String s = Globals.file_name;
				// debug sentence
				TextView tv = (TextView) findViewById(R.id.textView1);
				tv.setText(s);

		ExpandableListView expandableList = getExpandableListView(); // you can use (ExpandableListView) findViewById(R.id.list)

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		setGroupParents();
		setChildData();

		MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);

		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(this);
		
		
	}

	public void setGroupParents() {
		
		//Get word list through file name from Globals
		int i;
		for(i=0; i < Globals.wordarray.size(); i++){
			parentItems.add(Globals.wordarray.get(i));

		}
	}

	public void setChildData() {

		//Get definition list through file name from Globals	
		// Android
		ArrayList<String> child = new ArrayList<String>();
		int i;
		
		for(i=0; i <Globals.wordarray.size(); i++){
			child = new ArrayList<String>();
			child.add(Globals.wordarray.get(i).getDefine());
			childItems.add(child);
		}	
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.viewwords_play_button:
			
			Globals.in_view_words=true;	
			Log.i("debug", "In view words, and clicked the in view words " +
					"play button"+Globals.in_view_words);
			Intent playIntent = new Intent(this, DisplayWords.class);
			startActivity(playIntent);	
		break;		
	     }
	}
	


}