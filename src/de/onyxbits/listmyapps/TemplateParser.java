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

	private Context ctx;
	private TemplateSource tmpSource;
	private String fileStr;
	private TemplateData tmpData;

	public TemplateParser(Context ctx) {
		this.ctx = ctx;
		tmpSource = new TemplateSource(ctx);
		tmpSource.open();
	}

	public TemplateData getTemplateData(File f) {
		List<TemplateData> tmpList = tmpSource.list();
		Scanner s;
		tmpData = null;

		try {
			s = new Scanner(f);
			fileStr = "";
			while (s.hasNext()) {
				fileStr += s.nextLine();
			}
			for (TemplateData td : tmpList) {
				String pattern = td.item.replace("*", ".")
						.replace("\"", "\\\"").replace("\n", "")
						.replace("\t", "").replace("${comment}", ")(.*)(")
						.replace("${packagename}", ")(.*)(")
						.replace("${displayname}", ")(.*)(")
						.replace("${source}", ")(.*)(")
						.replace("${versioncode}", ")(.*)(")
						.replace("${version}", ")(.*)(")
						.replace("${rating}", ")(.*)(")
						.replace("${uid}", ")(.*)(")
						.replace("${firstinstalled}", ")(.*)(")
						.replace("${lastupdated}", ")(.*)(")
						.replace("${datadir}", ")(.*)(")
						.replace("${marketid}", ")(.*)(");

				pattern = "(" + pattern + ")";
				pattern = pattern.replace("()", "");

				Pattern p = Pattern.compile(pattern);

				Log.d("TEMPLATE Input", td.item);
				Log.d("TEMPLATE PARSER", p.pattern());
				if (p.matcher(" ").find()) {
					tmpData = td;
				} else if (p.matcher(fileStr).find()) {
					Log.d("TEMPLATE PARSER", "WIN");
					tmpData = td;
					return td;
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmpData;
	}

	public TemplateData getTemplateData() {
		return tmpData;
	}

	public List<SortablePackageInfoNotInstalled> getList() {
		List<SortablePackageInfoNotInstalled> spiList = new ArrayList<SortablePackageInfoNotInstalled>();
		return spiList;
	}
}
