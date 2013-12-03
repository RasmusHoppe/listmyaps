package de.onyxbits.listmyapps;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

/**
 * Data container for holding all the relevant information on a package.
 * @author patrick
 * 
 */
class SortablePackageInfoNotInstalled implements SortablePackageInfoInterface, Comparable<SortablePackageInfoInterface>,
		View.OnClickListener {

	/**
	 * Maximum items per category (for sorting)
	 */
	
	public String packageName;
	public String installer;
	public String comment;

	public SortablePackageInfoNotInstalled(String packageName){
		this.packageName = packageName;
		this.installer = "market://details?id=" + packageName;
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

	@Override
	public int compareTo(SortablePackageInfoInterface another) {
		return MAXCATEGORY*MAXCATEGORY+ packageName.compareTo(another.getDisplayName());
	}

	@Override
	public void onClick(View v) {
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setData(Uri.parse(installer));
		v.getContext().startActivity(installIntent);
	}

	@Override
	public boolean installed() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return packageName;
	}

}
