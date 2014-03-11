package com.WordZ4;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.WordZ.R;
 
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
 
public class LineChart extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linechart_layout);
        
		String s = Integer.toString(Globals.number_of_level1);	
		// debug sentence
		Log.i("Display", "in display string s is" + s);
		TextView tv1 = (TextView) findViewById(R.id.textViewlvl1data);
		tv1.setText(s);
		
		 s = Integer.toString(Globals.number_of_level2);	
		// debug sentence
		Log.i("Display", "in display string s is" + s);
		TextView tv2 = (TextView) findViewById(R.id.textViewlvl2data);
		tv2.setText(s);
		
		 s = Integer.toString(Globals.number_of_level3);		
		// debug sentence
		Log.i("Display", "in display string s is" + s);
		TextView tv3 = (TextView) findViewById(R.id.textViewlvl3data);
		tv3.setText(s);
		
		 s = Integer.toString(Globals.number_of_level4);	
		// debug sentence
		Log.i("Display", "in display string s is" + s);
		TextView tv4 = (TextView) findViewById(R.id.textViewlvl4data);
		tv4.setText(s);
		
		 s = Integer.toString(Globals.number_of_levelnone);	
			// debug sentence
		Log.i("Display", "in display string s is" + s);
		TextView tvnone = (TextView) findViewById(R.id.textViewnotcheckeddata);
		tvnone.setText(s);
 
       }
    }
 
  