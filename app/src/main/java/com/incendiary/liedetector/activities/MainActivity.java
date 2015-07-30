package com.incendiary.liedetector.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.incendiary.liedetector.AppConfig;
import com.incendiary.liedetector.R;
import com.incendiary.liedetector.speech.MyRecognitionListener;
import com.incendiary.liedetector.utils.CommonUtils;
import com.incendiary.liedetector.utils.FontsFactory;
import com.incendiary.liedetector.utils.Screens;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import at.markushi.ui.RevealColorView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	@Bind(R.id.reveal) RevealColorView mRevealColorView;
	@Bind(R.id.txtTitle) TextView mTxtTitle;
	@Bind(R.id.imgContent) ImageView mImgContent;
	@Bind(R.id.lineRed) View mRedLine;

	private Handler mHandler = new Handler(Looper.getMainLooper());

	private boolean isMustLie = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		AnimatorSet set = new AnimatorSet();
		set.playTogether(
				Glider.glide(Skill.ExpoEaseIn, 600, ObjectAnimator.ofFloat(mImgContent, "scaleY", 0, 1f)),
				Glider.glide(Skill.ExpoEaseIn, 600, ObjectAnimator.ofFloat(mImgContent, "scaleX", 0f, 1f))
		);

		mTxtTitle.setTypeface(FontsFactory.digits(this));
		mTxtTitle.setText(AppConfig.OPENING_WORD);

		set.setDuration(600);
		set.start();
	}

	@OnClick(R.id.imgContent)
	public void onScanFinger() {
		stopScanningLine();
		mTxtTitle.setText(AppConfig.SCANNING_WORD);

		if (SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
			SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
			speechRecognizer.setRecognitionListener(new MyRecognitionListener() {
				@Override
				public void onEndOfSpeech() {
					doEndScanning();
				}
			});
			speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(getApplicationContext()));
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					doEndScanning();
				}
			}, AppConfig.SCANNING_DELAY);
		}
	}

	private void doEndScanning() {
		int centerX = Screens.getDisplaySize(this).x / 2;
		int centerY = Screens.getDisplaySize(this).y / 2;
		int color = getResources().getColor(isMustLie ? R.color.material_red : R.color.material_green);
		mRevealColorView.reveal(centerX, centerY, color, null);

		if (AppConfig.USE_VIBRATOR)
			if (isMustLie) {
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(AppConfig.VIBRATOR_DURATION);
			}
	}

	@Override
	protected void onResume() {
		super.onResume();
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
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
