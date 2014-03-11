package com.WordZ4;

import com.WordZ4.DisplayWords;
import com.WordZ4.Globals;
import com.example.WordZ.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowDef extends Activity implements OnClickListener {
	
	public static int currentRecordsIndex;
	
	
	@Override
	//This function set up all the page view
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Globals.out_of_play = false;
		Globals.in_view_words = false;
		Globals.in_main_activity = false;
			
		int index=DisplayWords.currentIndex;
		String color_background;
		try{
			color_background = Integer.toString(Globals.wordarray.get(index)
				.getBackgroundRes());
		}
		catch(Exception e){
			color_background = Integer.toString(R.color.btn_holo_grey);
		}
		if (color_background.equals(Integer.toString(R.color.btn_green))) {
			ShowDef.currentRecordsIndex=3;
			setContentView(R.layout.show_def_green);
		} else if (color_background
				.equals(Integer.toString(R.color.btn_yellow))) {
			ShowDef.currentRecordsIndex=2;
			setContentView(R.layout.show_def_yellow);
		} else if (color_background.equals(Integer.toString(R.color.btn_red))) {
			ShowDef.currentRecordsIndex=1;
			setContentView(R.layout.show_def_red);
		} else if (color_background.equals(Integer.toString(R.color.btn_blue))){
			ShowDef.currentRecordsIndex=4;
			setContentView(R.layout.show_def_blue);
		}else{
			ShowDef.currentRecordsIndex=0;
			setContentView(R.layout.show_def);
		}

		
		//index=genInd();	
		//String s = Integer.toString(index);	
		String s1 = Globals.wordarray.get(index).getContent();	
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText(s1);
		
		String s2 = Globals.wordarray.get(index).getDefine();	
		TextView tv2 = (TextView) findViewById(R.id.textView2);
		tv2.setText(s2);
		
		//Set all the button levels
		View button_level1 = findViewById(R.id.button_level1);
		button_level1.setOnClickListener(this);
		View button_level2 = findViewById(R.id.button_level2);
		button_level2.setOnClickListener(this);
		View button_level3 = findViewById(R.id.button_level3);
		button_level3.setOnClickListener(this);
		View button_level4 = findViewById(R.id.button_level4);
		button_level4.setOnClickListener(this);
		
		View exitButton = findViewById(R.id.show_def_exit_button);
		exitButton.setOnClickListener(this);
	}

	private int getRandom(int min, int max) {
		int Randnumber;
		int Min = min;
		int Max = max;
		Randnumber = Min + (int) (Math.random() * ((Max - Min) + 1));
		return Randnumber;

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent displayIntent = new Intent(this, DisplayWords.class);
		Intent exitIntent = new Intent(this, MainActivity.class);
		//Below goes the button choice of different level
		//Once the button get clicked it
		//1.Base on the level set the priority of word object with the currentIndex
		//   set in DisplayWords.
		//2.Current priority=priority+1 (For all the word object.)
		//3.It leads to the DisplayWords page
		getRandom(0,1);
		int rand=getRandom(0,1);
		
		int currentPrio;
		int index;
		switch (v.getId()) {
		case R.id.button_level1:
			
			
			//Get the current color use that to decrease records
			if(ShowDef.currentRecordsIndex==0){
				Globals.number_of_levelnone--;
				
			}else if(ShowDef.currentRecordsIndex==1){
				Globals.number_of_level1--;
				
			}else if(ShowDef.currentRecordsIndex==2){
				Globals.number_of_level2--;
				
			}else if(ShowDef.currentRecordsIndex==3){
				Globals.number_of_level3--;
				
			}else{
				Globals.number_of_level4--;
				
			}
			
			Globals.number_of_level1++;
			
			index = DisplayWords.currentIndex;
		
			//Update the list
			Globals.level1_list.add(index);
			
			//Update prio
			
			//Update the color to red
			Globals.wordarray.get(index).setBackgroundRes(R.color.btn_red);
			
			startActivity(displayIntent);
			
			if(rand==0){
			    overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
			}
			else{
				overridePendingTransition(R.anim.side2,R.anim.side1);
			}
		

			break;

		case R.id.button_level2:
		
     //Get the current color use that to decrease records
			if(ShowDef.currentRecordsIndex==0){
				Globals.number_of_levelnone--;
				
			}else if(ShowDef.currentRecordsIndex==1){
				Globals.number_of_level1--;
				
			}else if(ShowDef.currentRecordsIndex==2){
				Globals.number_of_level2--;
				
			}else if(ShowDef.currentRecordsIndex==3){
				Globals.number_of_level3--;
				
			}else{
				Globals.number_of_level4--;
				
			}
			
			Globals.number_of_level2++;
			
			
			
			index = DisplayWords.currentIndex;
			Globals.level2_list.add(index);
			
			Globals.wordarray.get(index).setBackgroundRes(R.color.btn_yellow);

			startActivity(displayIntent);

			if(rand==0){
			    overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
			}
			else{
				overridePendingTransition(R.anim.side2,R.anim.side1);
			}
			
			break;
			
		case R.id.button_level3:
			
			//Get the current color use that to decrease records
			if(ShowDef.currentRecordsIndex==0){
				Globals.number_of_levelnone--;
				
			}else if(ShowDef.currentRecordsIndex==1){
				Globals.number_of_level1--;
				
			}else if(ShowDef.currentRecordsIndex==2){
				Globals.number_of_level2--;
				
			}else if(ShowDef.currentRecordsIndex==3){
				Globals.number_of_level3--;
				
			}else{
				Globals.number_of_level4--;
				
			}
			
			Globals.number_of_level3++;
			
			index = DisplayWords.currentIndex;
			Globals.level3_list.add(index);
			
			Globals.wordarray.get(index).setBackgroundRes(R.color.btn_green);
			
			
	
			startActivity(displayIntent);
		
			if(rand==0){
			    overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
			}
			else{
				overridePendingTransition(R.anim.side2,R.anim.side1);
			}

			break;
			
			
			
		case R.id.button_level4:
			
			//Get the current color use that to decrease records
			if(ShowDef.currentRecordsIndex==0){
				Globals.number_of_levelnone--;
				
			}else if(ShowDef.currentRecordsIndex==1){
				Globals.number_of_level1--;
				
			}else if(ShowDef.currentRecordsIndex==2){
				Globals.number_of_level2--;
				
			}else if(ShowDef.currentRecordsIndex==3){
				Globals.number_of_level3--;
				
			}else{
				Globals.number_of_level4--;
				
			}
			
			Globals.number_of_level4++;
			
			
			
			index = DisplayWords.currentIndex;
			Globals.level4_list.add(index);
		
			Globals.wordarray.get(index).setBackgroundRes(R.color.btn_blue);
			
			startActivity(displayIntent);
			
			if(rand==0){
			    overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
			}
			else{
				overridePendingTransition(R.anim.side2,R.anim.side1);
			}
	

			break;
			
		case R.id.show_def_exit_button:
			Globals.out_of_play = true;

			startActivity(exitIntent);
		

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
	
}