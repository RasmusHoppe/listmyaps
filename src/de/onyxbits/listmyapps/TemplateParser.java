package de.onyxbits.listmyapps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

public class TemplateParser {

	private List<SortablePackageInfoNotInstalled> spiList =  new ArrayList<SortablePackageInfoNotInstalled>();
	private String fileStr;

	public List<SortablePackageInfoNotInstalled> setFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			String google_search = "https://www.google.com/search?q=";
			String google_play = "https://play.google.com/store/apps/details?id=";
			String google_market = "market://details?id=";
			String fdroid = "https://f-droid.org/repository/browse/?fdid=";
			String amazon = "http://www.amazon.com/gp/mas/dl/android?p=";
            while (s.hasNext()) {
            	String current_string = s.next();
            	if(current_string.indexOf(google_search) >= 0)
            	{
            		int stringCnt = current_string.indexOf(google_search);
            		stringCnt += google_search.length();
            		spiList.add(parseSpiFromString(current_string.substring(stringCnt)));
            	} 
            	else if(current_string.indexOf(google_play) >= 0)
            	{
            		int stringCnt = current_string.indexOf(google_play);
            		stringCnt += google_play.length();
            		spiList.add(parseSpiFromString(current_string.substring(stringCnt)));
            	}
            	else if(current_string.indexOf(google_market) >= 0)
            	{
            		int stringCnt = current_string.indexOf(google_market);
            		stringCnt += google_market.length();
            		spiList.add(parseSpiFromString(current_string.substring(stringCnt)));
            	}
            	else if(current_string.indexOf(fdroid) >= 0)
            	{
            		int stringCnt = current_string.indexOf(fdroid);
            		stringCnt += fdroid.length();
            		spiList.add(parseSpiFromString(current_string.substring(stringCnt)));
            	}
            	else if(current_string.indexOf(amazon) >= 0)
            	{
            		int stringCnt = current_string.indexOf(amazon);
            		stringCnt += amazon.length();
            		spiList.add(parseSpiFromString(current_string.substring(stringCnt)));
            	}
            }
            return spiList;
	}
	
	private SortablePackageInfoNotInstalled parseSpiFromString(String search_string){
		String empty_string = search_string.replaceFirst("[[\\w\\.-]+\\.[\\w\\.-]+]+", "");
		String package_name = search_string.substring(0, search_string.length() - empty_string.length());
		return new SortablePackageInfoNotInstalled(package_name);
	}
}
