package de.onyxbits.listmyapps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.support.v4.app.ListFragment;
import android.app.ListActivity;

/**
 * Query the packagemanager for a list of all installed apps that are not system
 * apps. Populate a listview with the result.
 * 
 * @author patrick
 * 
 */
public class ListTask extends
		AsyncTask<Object, Object, ArrayList<? extends SortablePackageInfoInterface>> {

	private ListActivity listActivity;
	private int layout;

	/**
	 * New task
	 * 
	 * @param listActivity
	 *          the activity to report back to
	 * @param layout
	 *          layout id to pass to the AppAdaptier
	 */
	public ListTask(ListActivity listActivity, int layout) {
		this.listActivity = listActivity;
		this.layout = layout;
	}

	@Override
	protected ArrayList<SortablePackageInfoInterface> doInBackground(Object... params) {
		SharedPreferences prefs = listActivity.getSharedPreferences(
				MainActivity.PREFSFILE, 0);
		ArrayList<SortablePackageInfoInterface> ret = new ArrayList<SortablePackageInfoInterface>();
		PackageManager pm = listActivity.getPackageManager();
		List<PackageInfo> list = pm.getInstalledPackages(0);
		SortablePackageInfo spitmp[] = new SortablePackageInfo[list.size()];
		Iterator<PackageInfo> it = list.iterator();
		AnnotationsSource aSource = new AnnotationsSource(listActivity);
		aSource.open();
		int idx = 0;
		while (it.hasNext()) {
			PackageInfo info = it.next();
			try {
				ApplicationInfo ai = pm.getApplicationInfo(info.packageName, 0);
				if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM
						&& (ai.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) {
					spitmp[idx] = new SortablePackageInfo();
					spitmp[idx].packageName = info.packageName;
					spitmp[idx].displayName = pm
							.getApplicationLabel(info.applicationInfo).toString();
					spitmp[idx].installer = pm.getInstallerPackageName(info.packageName);
					spitmp[idx].icon = ai.loadIcon(pm);
					spitmp[idx].versionCode = info.versionCode;
					spitmp[idx].version = info.versionName;
					spitmp[idx].firstInstalled = info.firstInstallTime;
					spitmp[idx].lastUpdated = info.lastUpdateTime;
					spitmp[idx].uid = info.applicationInfo.uid;
					spitmp[idx].dataDir = info.applicationInfo.dataDir;
					spitmp[idx].comment = aSource.getComment(info.packageName);
					idx++;
				}
			}
			catch (NameNotFoundException exp) {
			}
		}
		// Reminder: the copying is necessary because we are filtering away the
		// system apps.
		SortablePackageInfo spi[] = new SortablePackageInfo[idx];
		System.arraycopy(spitmp, 0, spi, 0, idx);
		Arrays.sort(spi);
		for (int i = 0; i < spi.length; i++) {
			spi[i].selected = prefs.getBoolean(MainActivity.SELECTED + "."
					+ spi[i].packageName, false);
			ret.add(spi[i]);
		}
		String filePath = prefs.getString(MainActivity.INSTALL_FILE, "");
		if(filePath != ""){
			File file = new File(filePath);
			Scanner input;
			try {
				input = new Scanner(file);
			while (input.hasNext()) {
				String nextLine = input.nextLine();
				SortablePackageInfoNotInstalled spiNotInst = new SortablePackageInfoNotInstalled();
				spiNotInst.installer = nextLine;
				spiNotInst.packageName = nextLine.replace("market://details?id=", "");
				spiNotInst.icon = listActivity.getResources().getDrawable(android.R.drawable.ic_input_get);
				
				if(!ret.contains(spiNotInst)){
					spiNotInst.comment = listActivity.getString(R.string.not_installed);
					ret.add(0, spiNotInst);
				}
			}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	@Override
	protected void onPreExecute() {
		listActivity.setProgressBarIndeterminate(true);
		listActivity.setProgressBarVisibility(true);
	}

	@Override
	protected void onPostExecute(ArrayList<? extends SortablePackageInfoInterface> result) {
		super.onPostExecute(result);
		listActivity.setListAdapter(new AppAdapter(listActivity, layout, result,
				layout));
		listActivity.setProgressBarIndeterminate(false);
		listActivity.setProgressBarVisibility(false);
	}

}
