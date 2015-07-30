package com.incendiary.liedetector.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontsFactory {

	private static Typeface mRobotoMedium;
	private static Typeface mRobotoRegular;
	private static Typeface mRobotoLight;

	public static Typeface digits(Context context) {
		if (mRobotoMedium == null)
			mRobotoMedium = Typeface.createFromAsset(context.getAssets(), "digitalis.ttf");
		return mRobotoMedium;
	}
}
