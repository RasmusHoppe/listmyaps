package de.onyxbits.listmyapps;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;

/**
 * Data container for holding all the relevant information on a package.
 * @author patrick
 * 
 */
class SortablePackageInfo implements SortablePackageInfoInterface, Comparable<SortablePackageInfoInterface>,
		View.OnClickListener {

	/**
	 * Maximum items per category (for sorting)
	 */
	
	public String packageName;
	public String displayName;
	public String installer;
	public Drawable icon;
	public boolean selected;
	public int versionCode;
	public String version;
	public long firstInstalled;
	public long lastUpdated;
	public int uid;
	public int rating;
	public String dataDir;
	public String comment;
	public int category;

	public SortablePackageInfo(){}

	@Override
	public int compareTo(SortablePackageInfoInterface another) {
		return MAXCATEGORY*category+ displayName.compareTo(another.getDisplayName());
	}

	@Override
	public void onClick(View v) {
		selected = ((CheckBox) v).isChecked();
	}

	@Override
	public boolean installed() {
		return true;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean equals(Object o) {
		if(((SortablePackageInfoInterface) o).installed()){
			SortablePackageInfo _spi =	(SortablePackageInfo)o;
			if(this.packageName.equals( _spi.packageName))
				return true;
		} else {
			SortablePackageInfoNotInstalled _spi = (SortablePackageInfoNotInstalled)o;
			if (this.packageName.equals(  _spi.packageName)) {
				return true;
			}
		}
		return false;
	}

}
