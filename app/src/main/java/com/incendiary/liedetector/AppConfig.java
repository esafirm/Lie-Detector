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
	public static final boolean RED_LINE_STARTUP_SCANNING = false;
	public static final int MIN_TOUCH_DURATION = 1500;
	public static final String HELP_MESSAGE = "1. Ask the person you want to test a question " +
			"you want\n\n2. Ask the person you want to test to place his finger on the finger " +
			"print spot\n\n3. Lift the finger up to see the answer";

	/* -------- */
	/* Red Line */
	/* -------- */
	public static final int RED_LINE_SPEED = 1000;
	public static final TimeInterpolator RED_LINE_INTERPOLATOR = new
			AccelerateDecelerateInterpolator();

	/* -------- */
	/* Scanning */
	/* -------- */
	/* scanning delay is used when no voice recognition available */
	public static final int SCANNING_DELAY = 2500;
	public static final String OPENING_WORD = "Place your finger to start";
	public static final String SCANNING_WORD = "Scanning ...";
	public static final String SCANNING_TO_SHORT = "Scan time to short, please try again";

	/* -------- */
	/* Vibrator */
	/* -------- */
	public static final boolean USE_VIBRATOR = true;
	public static final int VIBRATOR_DURATION = 500;

	/* -------- */
	/* Message  */
	/* -------- */
	public static final String MESSAGE_LIE = "You Lied";
	public static final String MESSAGE_NOT_LIE = "You tell the truth";
}

