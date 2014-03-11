package com.WordZ4;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.WordZ.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoadFile extends Activity {
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_file);
		
		//initialize it to be false
		Globals.in_view_words=false;
		Globals.in_main_activity=false;
		Globals.out_of_play=true;
		Log.i("debug", "I am in LoadFile and in_view_words is"+Globals.in_view_words);
		Log.i("debug", "I am in LoadFile and in_main_activity is"+Globals.in_main_activity);
	
		@SuppressWarnings("unused")
		String separator = System.getProperty("line.separator");
	
		// We get the ListView component from the layout

		ListView lv = (ListView) findViewById(R.id.listView);
		SimpleAdapter simpleAdpt = new SimpleAdapter(this,
				Globals.filelist, android.R.layout.simple_list_item_1,
				new String[] { "planet" }, new int[] { android.R.id.text1 });

		lv.setAdapter(simpleAdpt);

		// React to user clicks on item
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				TextView clickedView = (TextView) view;
				//get the clicked file name
				Globals.file_name = (String) clickedView.getText();
				Log.i("debug", "I am load file on click");
				
			
				/**Use file Name, file ===>globals word files ***/
				int number_of_words=loadFileToWordArray(Globals.file_name);
				if(number_of_words==0)
				{
					Log.i("debug", "in LoadFile load file to globals the file"+Globals.file_name +" is empty");
					Toast.makeText(getBaseContext(), "No Data Found", Toast.LENGTH_SHORT)
					.show();
					
				}else{
					Log.i("debug","In laod file in on click"+ Globals.file_name +" is not empty");
					Intent wordsIntent = new Intent(view.getContext(),
							ViewWords.class);
							startActivityForResult(wordsIntent, 0);
					} 
				
			}
		});

	}
	
	
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

	private HashMap<String, String> createWord(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}
	
	/**Check file we out put all the info 
	 * in file line by line in Tag check 
	 * file log
	 ***/
	public void checkFile(String fileName){
			Log.i("check file", "I am in checkFile function");
			List<Map<String, String>>file_list = new ArrayList<Map<String, String>>();
			
		try {
			FileInputStream testfIn;
			testfIn = openFileInput(fileName);

			DataInputStream testin = new DataInputStream(testfIn);
			BufferedReader testbr = new BufferedReader(
					new InputStreamReader(testin));

			// This is hard coded should be changed later
			// The assumption was use only has less than 150 file.

			// Put the file name list to an array
			String readString = testbr.readLine();
			while (readString != null) {

				file_list.add(createWord("planet", readString));
				Log.i("check file", "In check file the line is+++"+readString);
				readString = testbr.readLine();
			}
		//	Log.i("check file", "In check file+++"+readString);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Globals.filelist=file_list;
	}
	
	
	
	
	/**
	 * This function will get the all the available 
	 * file name from database and put it to the 
	 * global variable load_file_list to this load 
	 * file page to display 
	 */
	public void loadDBToLoadfilelist() {
		List<Map<String, String>> load_file_list = new ArrayList<Map<String, String>>();
		//****This part will get all the data of file names in data base 
		//*****and put it to load_file_list for this page to ONLY to display
		JSONArray jArray = null;
		String result = null;
		StringBuilder sb = null;
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://derekswebsites.com/WordZ/get_files.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			jArray = new JSONArray(result);
			JSONObject json_data = null;

			// ***Start write data from data base data to load_file_list

			// User for loop to write data base data to load_file_list
			for (int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				String filename = json_data.getString("fileName");
				load_file_list.add(createWord("planet", filename));
				
				// here "Name" is the column name in database
			}

			// **End write data from data base data to load_file_list

		} catch (JSONException e1) {
			Toast.makeText(getBaseContext(), "No Data Found", Toast.LENGTH_LONG)
					.show();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Globals.loadfilelist = load_file_list;
		

	}
	
	
	
	
	
	/**
	 * This function load filename string to fileinfo.txt
	 * this happened after click and will put the down loaded
	 * file name to fileinfo.txt  
	 **/
	public int loadToFileinfo(String globalFileName){
		// TO do
		
		int downloaded=0;
		
		String separator = System.getProperty("line.separator");
		try {
			// First read from fileinfo.txt
			
			//check if there file fileinfo.txt exist
			File file = getBaseContext().getFileStreamPath("fileinfo.txt");  
			if(file.exists()){

				FileInputStream fIn = openFileInput("fileinfo.txt");
				DataInputStream in = new DataInputStream(fIn);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));

				// This is hard coded should be changed later
				// The assumption was use only has less than 150 file.
				String[] st = new String[150];
				// Put the file name list to an array
				String readString = br.readLine();
				int i = 0;
				while (readString != null) {
					st[i] = readString;
					Log.i("Word is", st[i] + "this is in fileinfo.txt");
					readString = br.readLine();
					i++;
				}
				

				// Then check if the file has been down loaded yet
				i = 0;
				int j = 0;
				while (st[i] != null) {
					if (st[i].equals(globalFileName)) {
						j = 1;
						Log.i("Word is", "file has been down load in whiel");
						break;
					}
					i++;
				}

				// Means didn't find the file with the same name.
				// then write to a file fileinfo.txt with no duplicate
				if (j == 0) {
					Log.i("Word is", "no duplicate");

					FileOutputStream fOut = openFileOutput("fileinfo.txt",
							MODE_APPEND);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);
					osw.append(globalFileName);
					osw.append(separator);
					osw.flush();
					osw.close();
				} else {
					downloaded=1;
				}
				
			}
			else{
				
				FileOutputStream fOut = openFileOutput("fileinfo.txt",
						MODE_APPEND);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				osw.append(globalFileName);
				osw.append(separator);
				osw.flush();
				osw.close();
				
			}
			
			
			

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return downloaded;
	}
	
	
	
    /**
     * This function will load the file "fileName" in 
     * data base and create a file "fileName" and wirte
     * data in it  
     **/
	public void loadDBToWordsFile(String fileName) {
	
        int numberOfWords=0;
		JSONArray jArray = null;
		String result = null;
		StringBuilder sb = null;
		InputStream is = null;
		String separator = System.getProperty("line.separator");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		String link="http://derekswebsites.com/WordZ/get_words.php";
		link=link+"?file="+fileName;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			// Why to use 10.0.2.2
			HttpPost httppost = new HttpPost(
					link);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		
	
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			jArray = new JSONArray(result);
			JSONObject json_data = null;

			// ***Start write data from data base data to load_file_list
			try {
				int background = android.R.color.holo_purple;
				FileOutputStream fOut = openFileOutput(fileName,
						MODE_WORLD_READABLE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				// User for loop to write data base data to load_file_list
				numberOfWords=jArray.length();
				for (int i = 0; i < numberOfWords; i++) {
				
					
					json_data = jArray.getJSONObject(i);
					String word = json_data.getString("word");
					String wordID = json_data.getString("wordID");
					String definition = json_data.getString("definition");
					String fileID = json_data.getString("fileID");
					String file = json_data.getString("file");

					// First write to a file---will be delete later

					osw.write(wordID + "|" + word + "|" +Integer.toString(background) + "|"+ fileID +
							"|" + file+"|" + definition);

					osw.append(separator);
				}
				osw.flush();
				osw.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			// here "Name" is the column name in database

			// **End write data from data base data to load_file_list

		} catch (JSONException e1) {
			Toast.makeText(getBaseContext(), "No Data Found", Toast.LENGTH_LONG)
					.show();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		

	}
	
//Has not been tested yet	
	public int getAllFileLength(){
		int length=0;
		try{
			FileInputStream fIn = openFileInput("fileinfo.txt");
			DataInputStream in = new DataInputStream(fIn);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in));
			String readString = br.readLine();
			while (readString != null) {
				Log.i("Word is","getAllFileLength is"+readString);			
				readString = br.readLine();		
			}
	
			br.close();
		
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}	
		return length;
		
	}
	
	/** 
	 * Load info in fileinfo.txt in global file_list
	 * ***important*** unfixed potential bug: Why this can be done in loadFileToGlobalFileList
	 ***/
	public void loadFileToGlobalFileList(){
			Log.i("check file", "I am in checkFile function");
			List<Map<String, String>>file_list = new ArrayList<Map<String, String>>();
			
		try {
			FileInputStream testfIn;
			testfIn = openFileInput("fileinfo.txt");

			DataInputStream testin = new DataInputStream(testfIn);
			BufferedReader testbr = new BufferedReader(
					new InputStreamReader(testin));

			// This is hard coded should be changed later
			// The assumption was use only has less than 150 file.

			// Put the file name list to an array
			String readString = testbr.readLine();
			while (readString != null) {

				file_list.add(createWord("planet", readString));
				Log.i("check file", "In check file the line is+++"+readString);
				readString = testbr.readLine();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Globals.filelist=file_list;
	}
	
	
	/**
	 * load the specific file to global word array
	 * Return 0 means file is empty
	 */
	public int loadFileToWordArray(String fileName){
		 List<Word> word_array = new ArrayList<Word>();
		 int i=0;
		try{
			//Use the clicked file name to get the file content
			FileInputStream fIn = openFileInput(fileName);
			DataInputStream in = new DataInputStream(fIn);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in));
			
		
			String inReadString = br.readLine();
			//This while loop go through all the lines in file info and get file name
			while(inReadString!= null)
			{	
				String wordID="";
				String word="";
				String definition="";
				String fileID="";
				String file="";
				String background_color="";
				int j1,j2,j3,j4,j5;					
				for(j1 = 0; j1 < inReadString.length(); j1++){
					if(inReadString.substring(j1,j1+1).equals("|")){
						wordID =  inReadString.substring(0, j1);
						inReadString=inReadString.substring(j1+1, inReadString.length());
						for(j2=0;j2<inReadString.length();j2++){
							if(inReadString.substring(j2,j2+1).equals("|")){
								word =  inReadString.substring(0, j2);
								inReadString=inReadString.substring(j2+1, inReadString.length());
								for(j5=0;j5<inReadString.length();j5++)
								{
									if(inReadString.substring(j5,j5+1).equals("|")){
										background_color = inReadString.substring(0, j5);
										inReadString=inReadString.substring(j5+1, inReadString.length());
										for(j3=0;j3<inReadString.length();j3++){
											if(inReadString.substring(j3,j3+1).equals("|")){
												fileID = inReadString.substring(0, j3);
												inReadString=inReadString.substring(j3+1, inReadString.length());
												for(j4=0;j4<inReadString.length();j4++){
													if(inReadString.substring(j4,j4+1).equals("|")){
														file =  inReadString.substring(0, j4);
														definition=inReadString.substring(j4+1, inReadString.length());
														break;												
													  }
												  }
												break;
											  }
										  }
							     	 break;
							        }
						         }
					            break;
					        }
				          }
			           break;
			         }
		          }	

				Word w=new Word(wordID,word,background_color,definition,file,fileID,20);
				
				word_array.add(w);
		
				inReadString = br.readLine();
				i++;
			}
			
			Globals.wordarray=word_array;
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		if (i==0)
		{
			return 0;
			}
		else{
			return 1;
		}
		
	}
	
	
	/**
	 * important**Right now the assumption is no file can be renamed and merged file can be down load.
	 * If it changed this function needs to be modified 
	 * If isEmpty is 0 means is empty
	 **/
	public int loadFileToGlobal(){
		int isEmpty=0;
		
	    List<Word> word_array = new ArrayList<Word>();
		try{
	
			//Use the clicked file name to get the file content
			FileInputStream fIn = openFileInput("fileinfo.txt");
			DataInputStream in = new DataInputStream(fIn);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in));

			//Parse the format update the global word array
			String readString = br.readLine();
			//This while loop go through all the lines in file info and get file name
			int i=0;
			while (readString != null) {
				Log.i("debug", "In loadfile in function loadFileToGlobal file Name"+readString);
				
				//Use the file name to open file and get the words from the inside.
				FileInputStream wfIn = openFileInput(readString);
				DataInputStream win = new DataInputStream(wfIn);
				BufferedReader wbr = new BufferedReader(
						new InputStreamReader(win));	
				String inReadString;
				inReadString = wbr.readLine();
				while(inReadString!= null)
				{	
					isEmpty++;
					String wordID="";
					String word="";
					String background_color="";
					String definition="";
					String fileID="";
					String file="";
					int j1,j2,j3,j4,j5;	
					for(j1 = 0; j1 < inReadString.length(); j1++){
						if(inReadString.substring(j1,j1+1).equals("|")){
							wordID =  inReadString.substring(0, j1);
							inReadString=inReadString.substring(j1+1, inReadString.length());
							for(j2=0;j2<inReadString.length();j2++){
								if(inReadString.substring(j2,j2+1).equals("|")){
									word =  inReadString.substring(0, j2);
									inReadString=inReadString.substring(j2+1, inReadString.length());
									for(j5=0;j5<inReadString.length();j5++)
									{
										if(inReadString.substring(j5,j5+1).equals("|")){
											background_color = inReadString.substring(0, j5);
											inReadString=inReadString.substring(j5+1, inReadString.length());
											for(j3=0;j3<inReadString.length();j3++){
												if(inReadString.substring(j3,j3+1).equals("|")){
													fileID = inReadString.substring(0, j3);
													inReadString=inReadString.substring(j3+1, inReadString.length());
													for(j4=0;j4<inReadString.length();j4++){
														if(inReadString.substring(j4,j4+1).equals("|")){
															file =  inReadString.substring(0, j4);
															definition=inReadString.substring(j4+1, inReadString.length());
															break;												
														  }
													  }
													break;
												  }
											  }
								     	 break;
								        }
							         }
						            break;
						        }
					          }
				           break;
				         }
			          }
					
					
					Word w=new Word(wordID,word,background_color,definition,file,fileID,20);
					word_array.add(w);
		
					inReadString = wbr.readLine();
					i++;
				}		
				Log.i("Word is", "Global file list is"+readString);
				
				
				readString = br.readLine();		
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}	
		Globals.wordarray=word_array;
		return isEmpty;
	
	
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}