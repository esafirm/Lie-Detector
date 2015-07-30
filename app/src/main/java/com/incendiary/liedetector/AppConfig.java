package com.incendiary.liedetector;

import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by esa on 29/07/15, with awesomeness
 */
public class AppConfig {

	/* -------- */
	/* General  */
	/* -------- */
	public static final boolean IS_DEBUG = true;

	/* -------- */
	/* Red Line */
	/* -------- */
	public static final int RED_LINE_SPEED = 1600;
	public static final TimeInterpolator RED_LINE_INTERPOLATOR = new
			AccelerateDecelerateInterpolator();

	/* -------- */
	/* Scanning */
	/* -------- */
	/* scanning delay is used when no voice recognition available */
	public static final int SCANNING_DELAY = 1000;
	public static final String OPENING_WORD = "Place your finger to start scanning";
	public static final String SCANNING_WORD = "Scanning ...";

	/* -------- */
	/* Vibrator */
	/* -------- */
	public static final boolean USE_VIBRATOR = true;
	public static final int VIBRATOR_DURATION = 500;
}
