package com.incendiary.liedetector.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

import com.incendiary.liedetector.R;


/**
 * Created by esa on 23/10/14 with awesomeness.
 */
public class SoundManager {

	private static SoundManager mSoundManager;

	private Context mContext;
	private SoundPool mSoundPool;

	private SparseIntArray mSoundsMap = new SparseIntArray();

	private float mVolume;

	public static SoundManager getInstance(Context context) {
		if (mSoundManager == null)
			mSoundManager = new SoundManager(context);
		return mSoundManager;
	}

	public SoundManager(Context context) {

		mContext = context;

		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		mVolume = actualVolume / maxVolume;

		mSoundPool = new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 0);
		mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleID, int status) {
				Logger.log(Log.DEBUG, "SampleID:" + sampleID + "\nStatus:" + status);
			}
		});

		loadSound(R.raw.beep);
	}

	private void loadSound(int resID) {
		mSoundsMap.put(resID, mSoundPool.load(mContext, resID, 1));
	}

	public void play(int resId) {
		mSoundPool.play(mSoundsMap.get(resId), mVolume, mVolume, 1, 0, 1f);
	}
}

