package com.WordZ4;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.example.WordZ.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PieChart extends Activity {

	private static int[] COLORS = new int[] { Color.RED, Color.YELLOW,
			Color.GREEN, Color.BLUE,Color.GRAY };

	private static double[] VALUES = new double[] { 10, 11, 12, 13,40 };

	private static String[] NAME_LIST = new String[] { "level1", "level2", "level3", "level4" ,"unchecked"};

	private CategorySeries mSeries = new CategorySeries("");

	private DefaultRenderer mRenderer = new DefaultRenderer();

	private GraphicalView mChartView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.piechart_layout);
		
		
		PieChart.VALUES[4]=Globals.number_of_levelnone;
		PieChart.VALUES[0]=Globals.number_of_level1;
		PieChart.VALUES[1]=Globals.number_of_level2;
		PieChart.VALUES[2]=Globals.number_of_level3;
		PieChart.VALUES[3]=Globals.number_of_level4;
		

		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(90);

		for (int i = 0; i < VALUES.length; i++) {
			mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
					% COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);
		}

		if (mChartView != null) {
			mChartView.repaint();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);

			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();

					if (seriesSelection == null) {
//						Toast.makeText(PieChart.this,
//								"No chart element was clicked",
//								Toast.LENGTH_SHORT).show();
					} else {
						
						if(seriesSelection.getPointIndex()==0){
							Toast.makeText(
									PieChart.this,
									"The number of words in level1 is"
											+ seriesSelection.getValue(),
									Toast.LENGTH_SHORT).show();
							
						}else if(seriesSelection.getPointIndex()==1){
							Toast.makeText(
									PieChart.this,
									"The number of words in level2 is"
											+ seriesSelection.getValue(),
									Toast.LENGTH_SHORT).show();
							
						}
						else if(seriesSelection.getPointIndex()==2){
							Toast.makeText(
									PieChart.this,
									"The number of words in level3 is"
											+ seriesSelection.getValue(),
									Toast.LENGTH_SHORT).show();
													
												}
						else if(seriesSelection.getPointIndex()==3){
							Toast.makeText(
									PieChart.this,
									"The number of words in level4 is"
											+ seriesSelection.getValue(),
									Toast.LENGTH_SHORT).show();
							
						}
						else{
							Toast.makeText(
									PieChart.this,
									"The number of unchecked words is"
											+ seriesSelection.getValue(),
									Toast.LENGTH_SHORT).show();
							
						}
						
					}
				}
			});

			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(PieChart.this,
								"No chart element was long pressed",
								Toast.LENGTH_SHORT);
						return false;
					} else {
						Toast.makeText(PieChart.this,
								"Chart element data point index "
										+ seriesSelection.getPointIndex()
										+ " was long pressed",
								Toast.LENGTH_SHORT);
						return true;
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
		}
	}
}