package com.WordZ4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;






public class Globals {
	

	//User this only for displaying the view file
	public static List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
	
	public static List<Map<String, String>> loadfilelist = new ArrayList<Map<String, String>>();
	
	//Store the current word array with the specific file
	public static List<Word> wordarray = new ArrayList<Word>();
	
	public static List<Integer> level1_list = new LinkedList<Integer>();
	public static List<Integer> level2_list = new LinkedList<Integer>();
	public static List<Integer> level3_list = new LinkedList<Integer>();
	public static List<Integer> level4_list = new LinkedList<Integer>();
	public static List<Integer> levelnone_list = new LinkedList<Integer>();
	
	public static int number_of_levelnone;
	public static int number_of_level1;
	public static int number_of_level2;
	public static int number_of_level3;
	public static int number_of_level4;
	
	

	public static boolean first = true;
	
	public static boolean exit_play_allfiles=false;
	
	public static boolean exit_play_singlefile=false;
	
	public static boolean in_view_words=false;
	public static boolean in_main_activity=false;
	
	
	public static boolean out_of_play=true;
	
	
	public static String file_name;
	
	//Initialize the file word list 

}
