package com.incendiary.liedetector.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by esa on 29/07/15, with awesomeness
 */
public class CommonUtils {

	public static void toast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
