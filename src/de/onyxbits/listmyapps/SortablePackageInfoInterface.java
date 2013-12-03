package de.onyxbits.listmyapps;


/**
 * Interface for the data container for holding all the relevant information on a package.
 * @author Rasmus
 * 
 */
interface SortablePackageInfoInterface {
	/**
	 * Maximum items per category (for sorting)
	 */
	public static final int MAXCATEGORY=10000;

	public String getDisplayName();
	
	@Override
	public boolean equals(Object o);
	
	/**
	 * Weather an item is installed or not
	 * @return
	 */
	public boolean installed();
}
