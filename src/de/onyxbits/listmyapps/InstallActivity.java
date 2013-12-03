package de.onyxbits.listmyapps;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class InstallActivity extends ListActivity implements
OnItemSelectedListener, OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = "InstallActivity";
	
	private ListView main_view;
	private ArrayList<SortablePackageInfo> mApps = new ArrayList<SortablePackageInfo>();
	private AppAdapter mAdapter;
	private static final int FILE_SELECTED = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chooseFile();
	}

	private void chooseFile() {
		Intent file_intent = new Intent(Intent.ACTION_GET_CONTENT);
		file_intent.setType("text/*");
		file_intent.addCategory(Intent.CATEGORY_OPENABLE);

		Intent final_intent = Intent.createChooser(file_intent,
				"Select an app list file");
		startActivityForResult(final_intent, FILE_SELECTED);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setListAdapter(new AppAdapter(this, R.layout.app_item,
				new ArrayList<SortablePackageInfoInterface>(), R.layout.app_item));
		new ListTask(this, R.layout.app_item).execute("");
		mAdapter = (AppAdapter) getListAdapter();
		mAdapter.addAll(mApps);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case FILE_SELECTED:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				try {
					Uri uri = data.getData();
					Log.d(TAG, "File Uri: " + uri.toString());
					// Get the path
					String path = getPath(this, uri);
					Log.d(TAG, "File Path: " + path);
					// Get the file instance
					mApps = makeListFromFile(new File(path));
				} catch (Exception e) {

				}
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * Parse a file to generate a list of apps
	 * @param file
	 * @return 
	 */
	public ArrayList<SortablePackageInfo> makeListFromFile(File file){
		ArrayList<SortablePackageInfo> apps = new ArrayList<SortablePackageInfo>();
		Scanner input;
		try {
			input = new Scanner(file);
		while (input.hasNext()) {
			String nextLine = input.nextLine();
			SortablePackageInfo spi = new SortablePackageInfo();
			spi.installer = nextLine;
			spi.packageName = nextLine.replace("market://details?id=", "");
			spi.displayName = spi.packageName;
			spi.icon = getResources().getDrawable(android.R.drawable.ic_input_get);
			
			apps.add(spi);
			
		}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return apps;
	}
	
	public static String getPath(Context context, Uri uri)
			throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
