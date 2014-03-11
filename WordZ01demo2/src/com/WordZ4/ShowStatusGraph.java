package com.WordZ4;

import com.example.WordZ.R;

import android.os.Bundle;
import android.app.Activity;

import android.view.KeyEvent;
import android.view.Menu;


import android.app.TabActivity;
import android.content.Intent;

import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
public class ShowStatusGraph extends TabActivity {
    /** Called when the activity is first created. */
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
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
         
        TabHost tabHost = getTabHost();
         

         
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("pie records");       
        songspec.setIndicator("graph status", getResources().getDrawable(R.drawable.icon_line_tab));
        Intent songsIntent = new Intent(this, PieChart.class);
        songspec.setContent(songsIntent);
         
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("status");
        videospec.setIndicator("text status", getResources().getDrawable(R.drawable.icon_pie_tab));
        Intent videosIntent = new Intent(this, LineChart.class);
        videospec.setContent(videosIntent);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
    }
}