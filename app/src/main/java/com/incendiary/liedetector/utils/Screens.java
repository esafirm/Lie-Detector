package com.incendiary.liedetector.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by esa on 28/04/15, with awesomeness
 */
public class Screens {

	public static Point getDisplaySize(Context context) {
		Point point = new Point();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getSize(point);
		return point;
	}

	public static int getOrientation(Activity activity) {
		return activity.getWindowManager().getDefaultDisplay().getRotation();
	}
}
