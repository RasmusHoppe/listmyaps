package de.onyxbits.listmyapps;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnnotationsActivity extends ListActivity implements
		DialogInterface.OnClickListener, OnItemLongClickListener {

	private EditText comment;
	private SortablePackageInfo spi;
	private AnnotationsSource annotationsSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_annotations);
		setProgressBarIndeterminate(true);
		setProgressBarVisibility(true);
		getListView().setOnItemLongClickListener(this);
		annotationsSource = new AnnotationsSource(this);
		annotationsSource.open();
		setListAdapter(new AppAdapter(this, R.layout.app_item_annotation,
				new ArrayList<SortablePackageInfo>(),
				R.layout.app_item_annotation));
		new ListTask(this, R.layout.app_item_annotation).execute("");
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		AppAdapter aa = (AppAdapter) getListAdapter();
		if (aa.getItem(pos).installed()) {
			spi = (SortablePackageInfo) aa.getItem(pos);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			comment = new EditText(this);
			comment.setHint(R.string.hint_my_notes);
			comment.setText(MainActivity.noNull(spi.comment));
			builder.setTitle(spi.displayName).setView(comment)
					.setIcon(spi.icon)
					.setPositiveButton(R.string.btn_save, this)
					.setNegativeButton(R.string.btn_cancel, this).show();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			spi.comment = comment.getText().toString();
			annotationsSource.putComment(spi.packageName, spi.comment);
			((AppAdapter) getListAdapter()).notifyDataSetChanged();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		AppAdapter aa = (AppAdapter) getListAdapter();
		if (aa.getItem(position).installed()) {
			SortablePackageInfo spi = (SortablePackageInfo) aa
					.getItem(position);
			View content = getLayoutInflater().inflate(R.layout.details, null);
			ScrollView scrollView = new ScrollView(this);
			scrollView.addView(content);

			DateFormat df = DateFormat.getDateTimeInstance();
			((TextView) content.findViewById(R.id.lbl_val_version))
					.setText(spi.version);
			((TextView) content.findViewById(R.id.lbl_val_versioncode))
					.setText("" + spi.versionCode);
			((TextView) content.findViewById(R.id.lbl_val_installed))
					.setText(df.format(new Date(spi.firstInstalled)));
			((TextView) content.findViewById(R.id.lbl_val_updated)).setText(df
					.format(new Date(spi.lastUpdated)));
			((TextView) content.findViewById(R.id.lbl_val_uid)).setText(""
					+ spi.uid);
			((TextView) content.findViewById(R.id.lbl_val_datadir))
					.setText(spi.dataDir);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(spi.displayName).setIcon(spi.icon)
					.setView(scrollView).setNegativeButton(null, null)
					.setPositiveButton(null, null).setNeutralButton(null, null)
					.show();
		}
		return true;
	}
}
