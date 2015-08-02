package com.incendiary.liedetector.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.incendiary.liedetector.AppConfig;
import com.incendiary.liedetector.R;
import com.incendiary.liedetector.utils.CommonUtils;
import com.incendiary.liedetector.utils.FontsFactory;
import com.incendiary.liedetector.utils.Screens;
import com.incendiary.liedetector.utils.SoundManager;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import at.markushi.ui.RevealColorView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	@Bind(R.id.txtTitle) TextView mTxtTitle;
	@Bind(R.id.imgContent) ImageView mImgContent;
	@Bind(R.id.lineRed) View mRedLine;
	@Bind(R.id.pbLoad) ProgressBar mProgressBar;

	/* Result Layout */
	@Bind(R.id.reveal) RevealColorView mRevealColorView;
	@Bind(R.id.lyOverlay) View mOverlay;
	@Bind(R.id.txtResult) TextView mTxtResult;

	private Handler mHandler = new Handler(Looper.getMainLooper());

	private boolean isMustLie = true;

	private long mTouchTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		mTxtResult.setTypeface(FontsFactory.digits(this));
		mTxtTitle.setTypeface(FontsFactory.digits(this));

		initViews();

		SoundManager.getInstance(this);

		mImgContent.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

					mTouchTime = System.currentTimeMillis();
					startScanningLine();
					SoundManager.getInstance(MainActivity.this).play(R.raw.beep);

				} else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent
						.getAction() == MotionEvent.ACTION_CANCEL) {

					if (System.currentTimeMillis() - mTouchTime < AppConfig.MIN_TOUCH_DURATION) {
						CommonUtils.toast(MainActivity.this, AppConfig.SCANNING_TO_SHORT);
						stopScanningLine();
						return false;
					}

					stopScanningLine();
					mTxtTitle.setText(AppConfig.SCANNING_WORD);

					mProgressBar.setAlpha(0f);
					mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.animate().alpha(1f);

					mImgContent.animate().alpha(0f);
					mRedLine.animate().alpha(0f);

					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mProgressBar.animate().alpha(0f);
							doEndScanning();
						}
					}, AppConfig.SCANNING_DELAY);
				}
				return true;
			}
		});
	}

	private void initViews() {
		mImgContent.setAlpha(1f);
		mRedLine.setAlpha(1f);

		AnimatorSet set = new AnimatorSet();
		set.playTogether(
				Glider.glide(Skill.ExpoEaseIn, 600, ObjectAnimator.ofFloat(mImgContent, "scaleY", 0, 1f)),
				Glider.glide(Skill.ExpoEaseIn, 600, ObjectAnimator.ofFloat(mImgContent, "scaleX", 0f, 1f))
		);

		mTxtTitle.setText(AppConfig.OPENING_WORD);

		set.setDuration(600);
		set.start();
	}

	@OnClick(R.id.btnHelp)
	public void onHelpClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(AppConfig.HELP_MESSAGE);
		builder.setPositiveButton("GOT IT", null);
		try {
			builder.create().show();
		} catch (Exception ignored) {
		}
	}

	@OnClick(R.id.btnRetry)
	public void onRetryClick() {
		initViews();
		mOverlay.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mOverlay.animate().setListener(null);
				mOverlay.setVisibility(View.GONE);
				mOverlay.setAlpha(1f);
				super.onAnimationEnd(animation);
			}
		});
		mRevealColorView.hide(0, 0, Color.TRANSPARENT, null);
	}

	private void doEndScanning() {

		mOverlay.setAlpha(0f);
		mOverlay.setVisibility(View.VISIBLE);
		mOverlay.animate().alpha(1f);

		int centerX = Screens.getDisplaySize(this).x / 2;
		int centerY = Screens.getDisplaySize(this).y / 2;
		int color = getResources().getColor(isMustLie ? R.color.accent : R.color.material_green);
		mRevealColorView.reveal(centerX, centerY, color, null);

		if (AppConfig.USE_VIBRATOR)
			if (isMustLie) {
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(AppConfig.VIBRATOR_DURATION);
			}

		mTxtResult.setText(isMustLie ? AppConfig.MESSAGE_LIE : AppConfig.MESSAGE_NOT_LIE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (AppConfig.RED_LINE_STARTUP_SCANNING)
			startScanningLine();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopScanningLine();
	}

	private void stopScanningLine() {
		mHandler.removeCallbacks(redLineAnimateTask);
	}

	private void startScanningLine() {
		mHandler.postDelayed(redLineAnimateTask, AppConfig.RED_LINE_SPEED);
	}

	Runnable redLineAnimateTask = new Runnable() {
		@Override
		public void run() {
			int height = mImgContent.getHeight() + mRedLine.getHeight();
			int translationY = mRedLine.getTranslationY() > 0 ? 0 : height;
			mRedLine.animate().setDuration(AppConfig.RED_LINE_SPEED * 9 / 10).translationY
					(translationY).setInterpolator(AppConfig.RED_LINE_INTERPOLATOR);
			startScanningLine();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			isMustLie = false;

			if (AppConfig.IS_DEBUG)
				CommonUtils.toast(this, "Not lying mode activated");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
