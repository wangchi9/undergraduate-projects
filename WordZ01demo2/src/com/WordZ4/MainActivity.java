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
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

import android.R.integer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final String DEBUG_TAG = "HttpExample";
	Button btnStartProgress;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	private long fileSize = 0;

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.play_button:
			loadFileToGlobal();
			if ((Globals.filelist.size() == 0)
					|| (Globals.wordarray.size() == 0)) {
				// When there is not date in globals
				// To do: give an alert dialog
				Toast.makeText(
						getBaseContext(),
						"No Data Found:fist time load please connect to internet",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent playIntent = new Intent(this, DisplayWords.class);
				startActivity(playIntent);
			}

			break;

		case R.id.res_button:
			if ((Globals.filelist.size() == 0)
					|| (Globals.wordarray.size() == 0)) {
				// When there is not date in globals
				// To do: give an alert dialog
				Toast.makeText(
						getBaseContext(),
						"No Data Found:fist time load please connect to internet",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent viewIntent = new Intent(this, ShowStatusGraph.class);
				startActivity(viewIntent);
			}
			// to do

			break;

		case R.id.records_button:
			if ((Globals.filelist.size() == 0)
					|| (Globals.wordarray.size() == 0)) {
				// When there is not date in globals
				// To do: give an alert dialog
				Toast.makeText(
						getBaseContext(),
						"No Data Found:fist time load please connect to internet",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent loadIntent = new Intent(this, LoadFile.class);
				startActivity(loadIntent);
			}
			break;

		case R.id.info_button:

			Intent loadIntent = new Intent(this, Info.class);
			startActivity(loadIntent);

			break;
		// More buttons go here (if any) ...
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		View playButton = findViewById(R.id.play_button);
		playButton.setOnClickListener(this);

		View viewButton = findViewById(R.id.res_button);
		viewButton.setOnClickListener(this);

		View loadButton = findViewById(R.id.records_button);
		loadButton.setOnClickListener(this);

		Globals.in_main_activity = true;
		Globals.in_view_words = false;
		Globals.out_of_play = true;

		/*
		 * Initialize this is the data feedback
		 */
		// If Got out from show_def or display words we load data
		// from Global to file;
		if (Globals.exit_play_singlefile) {

			Globals.level1_list.clear();
			Globals.level2_list.clear();
			Globals.level3_list.clear();
			Globals.level4_list.clear();
			Globals.levelnone_list.clear();

			loadGlobalToSingleFile(Globals.file_name);

			// Globals===>numberFile
			globalsToRecordsfile();

			Globals.exit_play_singlefile = false;
			//Log.i("debug", "in main in exit_play_singlefile");

		}

		if (Globals.exit_play_allfiles) {

			Globals.level1_list.clear();
			Globals.level2_list.clear();
			Globals.level3_list.clear();
			Globals.level4_list.clear();
			Globals.levelnone_list.clear();

			loadGlobalToAllFiles();

			// Globals===>numberFile
			globalsToRecordsfile();

			Globals.exit_play_allfiles = false;
			//Log.i("debug", "in main in exit_play_allfiles");

		}

		// First run only create the fileinfo.txt
		// if file exist from file to globals

		// check if there file fileinfo.txt exist
		File file2 = getBaseContext().getFileStreamPath("fileinfo.txt");
		if (file2.exists()) {
			// File===>globals;
			loadFileToGlobalFileList();
			 int total_words = loadFileToGlobal();
			Log.i("debug",
					"fist in main fileinfo.txt exist and total_number_of_words is "
							+ total_words);
			// numberFile====>globals
			RecordsfileToGlobals();

		} else {
			// If there is net work, load every thing from the DB
			// This only runs for very first time you open this program
			if (isNetworkConnected()) {
				Log.i("debug", "fist in main fileinfo.txt don't exist");
				// Initialize every thing.

				progressBar();
				// DB====>fileinfo.txt==>global.filelist
				// DB====>allwordsfiles.
				LoadFromDB();
				

			} else {
				progressBar();

			}

		}

	}

	/**
	 * Load info in fileinfo.txt in global file_list ***important*** unfixed
	 * potential bug: Why this can be done in loadFileToGlobalFileList
	 ***/
	public void loadFileToGlobalFileList() {
		//Log.i("debug", "I am in main load file to global filelist ");
		List<Map<String, String>> file_list = new ArrayList<Map<String, String>>();

		try {
			FileInputStream testfIn;
			testfIn = openFileInput("fileinfo.txt");

			DataInputStream testin = new DataInputStream(testfIn);
			BufferedReader testbr = new BufferedReader(new InputStreamReader(
					testin));

			// This is hard coded should be changed later
			// The assumption was use only has less than 150 file.

			// Put the file name list to an array
			String readString = testbr.readLine();
			while (readString != null) {

				file_list.add(createWord("planet", readString));
				//Log.i("check file", "In check file the line is+++" + readString);
				readString = testbr.readLine();
			}
			// Log.i("check file", "In check file+++"+readString);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Globals.filelist = file_list;
	}


	public synchronized void initRecordsfile(int total_number) {
		try {
			FileOutputStream fOut = openFileOutput("recordsfile.txt",
					MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			String separator = System.getProperty("line.separator");

			int number = 0;
			for (int i = 0; i < 5; i++) {

				if (i == 0) {
				//	Log.i("debug", "i=0-----" + i + "|" + total_number);
					osw.write(i + "|" + total_number);
					osw.append(separator);
				} else {
				//	Log.i("debug", "i!=0-----" + i + "|" + number);
					number = 0;
					osw.write(i + "|" + number);
					osw.append(separator);
				}
			}

			osw.flush();
			osw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public void globalsToRecordsfile() {

		try {
			FileOutputStream fOut = openFileOutput("recordsfile.txt",
					MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			String separator = System.getProperty("line.separator");

			int number = 0;
			for (int i = 0; i < 5; i++) {

				if (i == 0) {

					number = Globals.number_of_levelnone;

				//	Log.i("debug", "To recondsfile number_of_levelnone "
				//			+ number);
					osw.write(i + "|" + number);
					osw.append(separator);
				} else if (i == 1) {
					number = Globals.number_of_level1;

				//	Log.i("debug", "To recondsfile number_of_level1 " + number);
					osw.write(i + "|" + number);
					osw.append(separator);
				} else if (i == 2) {
					number = Globals.number_of_level2;

				//	Log.i("debug", "To recondsfile number_of_level2 " + number);
					osw.write(i + "|" + number);
					osw.append(separator);
				} else if (i == 3) {
					number = Globals.number_of_level3;

				//	Log.i("debug", "To recondsfile number_of_level3 " + number);
					osw.write(i + "|" + number);
					osw.append(separator);
				} else {
					number = Globals.number_of_level4;

				//	Log.i("debug", "To recondsfile number_of_level4 " + number);
					osw.write(i + "|" + number);
					osw.append(separator);
				}
			}

			osw.flush();
			osw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public synchronized void RecordsfileToGlobals() {

		try {
			FileInputStream wfIn = openFileInput("recordsfile.txt");
			DataInputStream win = new DataInputStream(wfIn);
			BufferedReader wbr = new BufferedReader(new InputStreamReader(win));
			String inReadString;
			inReadString = wbr.readLine();
			while (inReadString != null) {
				String index = "";
				String number = "";
				int j4;

			//	Log.i("debug", "------------" + inReadString);

				for (j4 = 0; j4 < inReadString.length(); j4++) {
					if (inReadString.substring(j4, j4 + 1).equals("|")) {
						index = inReadString.substring(0, j4);
						number = inReadString.substring(j4 + 1,
								inReadString.length());
						break;
					}
				}
				if (index.equals("0")) {

				//	Log.i("debug", "To Globals number_of_levelnone " + number);
					Globals.number_of_levelnone = Integer.parseInt(number);

				} else if (index.equals("1")) {
				//	Log.i("debug", "To Globals number_of_level1 " + number);
					Globals.number_of_level1 = Integer.parseInt(number);

				} else if (index.equals("2")) {
				//	Log.i("debug", "To Globals number_of_level2 " + number);
					Globals.number_of_level2 = Integer.parseInt(number);

				} else if (index.equals("3")) {
				//	Log.i("debug", "To Globals number_of_level3 " + number);
					Globals.number_of_level3 = Integer.parseInt(number);

				} else {
				//	Log.i("debug", "To Globals number_of_level4 " + number);
					Globals.number_of_level4 = Integer.parseInt(number);

				}
				inReadString = wbr.readLine();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	// **important**Right now the assumption is no file can be renamed and
	// merged file can be down load.
	// If it changed this function needs to be modified
	public int loadFileToGlobal() {

		List<Word> word_array = new ArrayList<Word>();
		try {

			// Use the clicked file name to get the file content
			FileInputStream fIn = openFileInput("fileinfo.txt");
			DataInputStream in = new DataInputStream(fIn);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// Parse the format update the global word array

			String readString = br.readLine();
			// This while loop go through all the lines in file info and get
			// file name
			while (readString != null) {
			//	Log.i("merge", readString);
				// Use the file name to open file and get the words from the
				// inside.
				FileInputStream wfIn = openFileInput(readString);
				DataInputStream win = new DataInputStream(wfIn);
				BufferedReader wbr = new BufferedReader(new InputStreamReader(
						win));
				String inReadString;
				inReadString = wbr.readLine();
				while (inReadString != null) {
					String wordID = "";
					String word = "";
					String background_color = "";
					String definition = "";
					String fileID = "";
					String file = "";
					int j1, j2, j3, j4, j5;
					for (j1 = 0; j1 < inReadString.length(); j1++) {
						if (inReadString.substring(j1, j1 + 1).equals("|")) {
							wordID = inReadString.substring(0, j1);
							inReadString = inReadString.substring(j1 + 1,
									inReadString.length());
							for (j2 = 0; j2 < inReadString.length(); j2++) {
								if (inReadString.substring(j2, j2 + 1).equals(
										"|")) {
									word = inReadString.substring(0, j2);
									inReadString = inReadString.substring(
											j2 + 1, inReadString.length());
									for (j5 = 0; j5 < inReadString.length(); j5++) {
										if (inReadString.substring(j5, j5 + 1)
												.equals("|")) {
											background_color = inReadString
													.substring(0, j5);
											inReadString = inReadString
													.substring(j5 + 1,
															inReadString
																	.length());
											for (j3 = 0; j3 < inReadString
													.length(); j3++) {
												if (inReadString.substring(j3,
														j3 + 1).equals("|")) {
													fileID = inReadString
															.substring(0, j3);
													inReadString = inReadString
															.substring(
																	j3 + 1,
																	inReadString
																			.length());
													for (j4 = 0; j4 < inReadString
															.length(); j4++) {
														if (inReadString
																.substring(j4,
																		j4 + 1)
																.equals("|")) {
															file = inReadString
																	.substring(
																			0,
																			j4);
															definition = inReadString
																	.substring(
																			j4 + 1,
																			inReadString
																					.length());
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

					Word w = new Word(wordID, word, background_color,
							definition, file, fileID, 20);

					word_array.add(w);
			//		Log.i("merge", "word array is " + word);
					inReadString = wbr.readLine();
				}
				readString = br.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Globals.wordarray = word_array;

		return Globals.wordarray.size();

	}

	public int loadFileToGlobalsingle(String fileName) {

		List<Word> word_array = new ArrayList<Word>();
		
	//	Log.i("debug","Loading loadFileToGlobalsinge input fileName is"+fileName);

		// Use the file name to open file and get the words from the
		// inside.
		try {
			FileInputStream wfIn = openFileInput(fileName);
			DataInputStream win = new DataInputStream(wfIn);
			BufferedReader wbr = new BufferedReader(new InputStreamReader(win));
			String inReadString;

			inReadString = wbr.readLine();

			while (inReadString != null) {
				String wordID = "";
				String word = "";
				String background_color = "";
				String definition = "";
				String fileID = "";
				String file = "";
				int j1, j2, j3, j4, j5;
				//Parse the single line
				for (j1 = 0; j1 < inReadString.length(); j1++) {
					if (inReadString.substring(j1, j1 + 1).equals("|")) {
						wordID = inReadString.substring(0, j1);
						inReadString = inReadString.substring(j1 + 1,
								inReadString.length());
						for (j2 = 0; j2 < inReadString.length(); j2++) {
							if (inReadString.substring(j2, j2 + 1).equals("|")) {
								word = inReadString.substring(0, j2);
								inReadString = inReadString.substring(j2 + 1,
										inReadString.length());
								for (j5 = 0; j5 < inReadString.length(); j5++) {
									if (inReadString.substring(j5, j5 + 1)
											.equals("|")) {
										background_color = inReadString
												.substring(0, j5);
										inReadString = inReadString.substring(
												j5 + 1, inReadString.length());
										for (j3 = 0; j3 < inReadString.length(); j3++) {
											if (inReadString.substring(j3,
													j3 + 1).equals("|")) {
												fileID = inReadString
														.substring(0, j3);
												inReadString = inReadString
														.substring(
																j3 + 1,
																inReadString
																		.length());
												for (j4 = 0; j4 < inReadString
														.length(); j4++) {
													if (inReadString.substring(
															j4, j4 + 1).equals(
															"|")) {
														file = inReadString
																.substring(0,
																		j4);
														definition = inReadString
																.substring(
																		j4 + 1,
																		inReadString
																				.length());
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

				Word w = new Word(wordID, word, background_color, definition,
						file, fileID, 20);
				word_array.add(w);
			//	Log.i("loadfiletosingle", "word array is " + word);
				inReadString = wbr.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Globals.wordarray = word_array;

		return Globals.wordarray.size();

	}

	// **important** NEED TO modify :right now only from GRE
	// To Do
	public void loadGlobalToAllFiles() {
		String separator = System.getProperty("line.separator");
		List<Word> word_array = new ArrayList<Word>();
		word_array = Globals.wordarray;

		// Get all the file list;
		if (Globals.filelist.size() == 0) {
			Log.i("debug", "EEErorEEEro check loadGlobalToAllFile in main");
		} else {

			for (int j = 0; j < Globals.filelist.size(); j++) {
				String file_name = Globals.filelist.get(j).get("planet");
			//	Log.i("debug", "+++++in loadGlobalToAllFiles file_name is "
			//			+ file_name);

				try {
					FileOutputStream fOut = openFileOutput(file_name,
							MODE_WORLD_READABLE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);
					for (int i = 0; i < word_array.size(); i++) {

						String file = word_array.get(i).getfile();
						// if it belongs to the same file
						if (file.equals(file_name)) {
							String word = word_array.get(i).getContent();
							String wordID = word_array.get(i).wordID;
							String definition = word_array.get(i).getDefine();
							String fileID = word_array.get(i).fileID;
							int backgroudcolor = word_array.get(i)
									.getBackgroundRes();

							osw.write(wordID + "|" + word + "|"
									+ Integer.toString(backgroudcolor) + "|"
									+ fileID + "|" + file + "|" + definition);

							osw.append(separator);

						}

						// Log.i("HAHA",Integer.toString(backgroudcolor));

						// First write to a file---will be delete later

					}
					osw.flush();
					osw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

			}

		}

	}

	/***
	 * To show the down load progress
	 */
	// file download simulator... a really simple
	public int doSomeTasks() {

		while (fileSize <= 1) {

			fileSize++;

			if (fileSize == 1) {
				return 10;
			}
			// ...add your own

		}

		return 100;

	}

	public void progressBar() {
		// prepare for a progress bar dialog
		progressBar = new ProgressDialog(MainActivity.this);
		progressBar.setCancelable(true);
		progressBar
				.setMessage("Please connect you internet when first loading this page ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		// reset progress bar status
		progressBarStatus = 0;

		// reset filesize
		fileSize = 0;

		new Thread(new Runnable() {
			public void run() {
				while (progressBarStatus < 100) {

					// process some tasks
					progressBarStatus = doSomeTasks();

					// your computer is too fast, sleep 1 second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Update the progress bar
					progressBarHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressBarStatus);
						}
					});
				}

				// ok, file is downloaded,
				if (progressBarStatus >= 100) {
				//	Log.i("bar", "I in bar 1 spleep");

					// sleep 2 seconds, so that you can see the 100%
					// try {
					// Thread.sleep(2000);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// }

					// close the progress bar dialog
					progressBar.dismiss();
				}
			}
		}).start();
	}

	/**
	 * This class is 
	 * 1.Load data from DB and write to fileinfo.txt
	 * 2.getting the all the available file name from fileinfo.txt and update global file list
	 * 3.Then use global file list to write to word files
	 */
	private class DownloadToFileinfo extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		private HashMap<String, String> createWord(String key, String name) {
			HashMap<String, String> planet = new HashMap<String, String>();
			planet.put(key, name);
			return planet;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			JSONArray jArray = null;
			List<Map<String, String>> file_list = new ArrayList<Map<String, String>>();
			String separator = System.getProperty("line.separator");

			try {
				jArray = new JSONArray(result);
				JSONObject json_data = null;

				// ***Start write data from data base data to fileinfo.txt
				try {
					FileOutputStream fOut = openFileOutput("fileinfo.txt",
							MODE_WORLD_READABLE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);

					// User for loop to write data base data to load_file_list
					for (int i = 0; i < jArray.length(); i++) {
						json_data = jArray.getJSONObject(i);
						String filename = json_data.getString("fileName");
					//	Log.i("debug", "Loading to file info file name is"+filename);
						file_list.add(createWord("planet", filename));
						osw.append(filename);
						osw.append(separator);

						// here "Name" is the column name in database
					}
				//	Log.i("debug","Loading to file info file_list size is"+ Integer.toString(file_list.size()));
					
					//Update the global filelist
					Globals.filelist = file_list;
					Log.i("debug", "Loading to file info Globals.file_list size is"+ Integer.toString(Globals.filelist.size()));

					osw.flush();
					osw.close();

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			// After load the filelist, use it to load the file words list
		//	Log.i("debug", "Loading to file info source is"+result);
			int number_of_file = Globals.filelist.size();
		//	Log.i("debug",
		//			"Loading to file info number_of_file is"
		//					+ number_of_file);

			// DB====>allwordsfiles.
			for (int i = 0; i < Globals.filelist.size(); i++) {
				Log.i("debug", "Loading to file info DB to all words files start loading file Name"
						+ Globals.filelist.get(i).get("planet") + "to files");
				DBToWordsFile(Globals.filelist.get(i).get("planet"));
			}

		}
	}
	
	public static int total_number_of_words=0;

	public void LoadFromDB() {

		String stringUrl = "http://derekswebsites.com/WordZ/get_files.php";
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadToFileinfo().execute(stringUrl);
		} else {
			// textView.setText("No network connection available.");
			Log.i("debug", "No network connection available.");
		}
	}



	/**
	 * This class will
	 * 1 Load from DB to all word files
	 * 2 Update the Global
	 **/

	private class DownloadToWordsFile extends AsyncTask<String, Void, String> {
		public String DownloadToWordsFile_fileName;
		
		public DownloadToWordsFile(String fileName) {
			// TODO Auto-generated constructor stub
			this.DownloadToWordsFile_fileName=fileName;
		}

	

		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			JSONArray jArray = null;
			String separator = System.getProperty("line.separator");
			
		//	Log.i("debug","loading to individual words file the DownloadToWordsFile_fileName is"+this.DownloadToWordsFile_fileName);

			try {
				jArray = new JSONArray(result);
				JSONObject json_data = null;

				// ***Start write data from data base data to words file
				try {
					int background = R.color.btn_holo_grey;
					FileOutputStream fOut = openFileOutput(
							this.DownloadToWordsFile_fileName, MODE_WORLD_READABLE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);
					// User for loop to write data base data to the file DownloadToWordsFile_filename
					int numberOfWords = jArray.length();
					for (int i = 0; i < numberOfWords; i++) {

						json_data = jArray.getJSONObject(i);
						String word = json_data.getString("word");
						String wordID = json_data.getString("wordID");
						String definition = json_data.getString("definition");
						String fileID = json_data.getString("fileID");
						String file = json_data.getString("file");

						// First write to a file---will be delete later
						

						Log.i("debug","+++"+wordID + "|" + word + "|"
							+ Integer.toString(background) + "|" + fileID
								+ "|" + file + "|" + definition);
						
						osw.write(wordID + "|" + word + "|"
								+ Integer.toString(background) + "|" + fileID
								+ "|" + file + "|" + definition);

						osw.append(separator);
					}
					osw.flush();
					osw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
			
			//Load the file to Global.
			//loadFileToGlobalsingle here is only for incrementing the total number.
			//the global word array it updated will be updated later at on click
			total_number_of_words=total_number_of_words+loadFileToGlobalsingle(this.DownloadToWordsFile_fileName);
		

			initRecordsfile(total_number_of_words);
			
			RecordsfileToGlobals();

		}
	}

	
    /*
     * write to words files
     */
	public synchronized void DBToWordsFile(String fileName) {
		String stringUrl = "http://derekswebsites.com/WordZ/get_words.php";
		stringUrl = stringUrl + "?file=" + fileName;

		Log.i("debug","loading in DBToWordsFile1 the fileName passed in is"+fileName);
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadToWordsFile(fileName).execute(stringUrl);
		} else {
			// textView.setText("No network connection available.");
			Log.i("debug", "No network connection available.");
		}

	}

	
	// **important**Right now the assumption is no file can be renamed and
	// merged file can be down load.
	// If it changed this function needs to be modified
	public void loadGlobalToSingleFile(String fileName) {

		String separator = System.getProperty("line.separator");
		List<Word> word_array = new ArrayList<Word>();
		word_array = Globals.wordarray;

		try {
			FileOutputStream fOut = openFileOutput(fileName,
					MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			for (int i = 0; i < word_array.size(); i++) {
				String word = word_array.get(i).getContent();
				String wordID = word_array.get(i).wordID;
				String definition = word_array.get(i).getDefine();
				String fileID = word_array.get(i).fileID;
				String file = word_array.get(i).getfile();
				int backgroudcolor = word_array.get(i).getBackgroundRes();
			//	Log.i("HAHA", Integer.toString(backgroudcolor));

				// First write to a file---will be delete later

				osw.write(wordID + "|" + word + "|"
						+ Integer.toString(backgroudcolor) + "|" + fileID + "|"
						+ file + "|" + definition);

				osw.append(separator);
			}
			osw.flush();
			osw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		  
		    
			String result = null;
			StringBuilder sb = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
	

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d(DEBUG_TAG, "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
//			String contentAsString = readIt(is, len);
//			return contentAsString;
			
			
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
	  		return result;
			
			

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private HashMap<String, String> createWord(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
