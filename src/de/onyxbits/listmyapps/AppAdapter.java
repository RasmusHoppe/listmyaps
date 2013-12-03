package de.onyxbits.listmyapps;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * For mapping a SortablePackageInfo into a view.
 * 
 * @author patrick
 * 
 */
public class AppAdapter extends ArrayAdapter<SortablePackageInfoInterface> {

	private int layout;

	public AppAdapter(Context context, int textViewResourceId,
			List<? extends SortablePackageInfoInterface> spi, int layout) {
		super(context, textViewResourceId,
				(List<SortablePackageInfoInterface>) spi);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View ret = convertView;
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (getItem(position).installed()) {
			ret = inflater.inflate(layout, null);
			SortablePackageInfo spi = (SortablePackageInfo) getItem(position);

			((TextView) ret.findViewById(R.id.appname))
					.setText(spi.displayName);
			((TextView) ret.findViewById(R.id.apppackage))
					.setText(spi.packageName);
			((ImageView) ret.findViewById(R.id.icon))
					.setImageDrawable(spi.icon);

			switch (layout) {
			case R.layout.app_item: {
				CheckBox sel = ((CheckBox) ret.findViewById(R.id.selected));
				sel.setChecked(spi.selected);
				sel.setOnClickListener(spi);
				break;
			}
			case R.layout.app_item_annotation: {
				((TextView) ret.findViewById(R.id.comments))
						.setText(MainActivity.noNull(spi.comment));
			}
			}
		} else {
			ret = inflater.inflate(R.layout.app_item_not_installed, null);
			SortablePackageInfoNotInstalled spi = (SortablePackageInfoNotInstalled) getItem(position);

			((TextView) ret.findViewById(R.id.apppackage))
					.setText(spi.packageName);
			((TextView) ret.findViewById(R.id.message)).setText(spi.comment);
			((ImageView) ret.findViewById(R.id.icon))
					.setImageResource(R.drawable.ic_action_add_to_queue);
			ret.setOnClickListener(spi);
		}

		return ret;
	}

}
