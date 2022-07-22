package com.test.music

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.test.music.dataModel.LiveMusic

class AudioManager {

    private val TAG = AudioManager::class.java.simpleName
    private var mediaPlayer: MediaPlayer? = null
    private val context: Context? = null
    private var currentMusic: LiveMusic? = null
    private var startTime = 0
    private var finalTime = 0
    fun stopMusic() {
        Log.d(TAG, "stopRingTone notificationTone:")
        try {
            if (mediaPlayer != null) {
                Log.d(TAG, "play is active:")
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.message.toString())
        } catch (e: SecurityException) {
            Log.e(TAG, e.message.toString())
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message.toString())
        }
    }

    @SuppressLint("WrongConstant")
    fun startMusic(context: Context, liveMusic: LiveMusic, param: MainActivity.MusicInterval) {
        Log.d(TAG, "startCallerTone: ")
        if (currentMusic != null) {
            mediaPlayer?.release()
        }
        if (mediaPlayer != null) {
            mediaPlayer?.release()
        }
        currentMusic = liveMusic
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioManager.STREAM_MUSIC)
                .build()
        )
        Log.d(TAG, "startCallerTone:*** ${liveMusic.path} ")
        try {
            mediaPlayer?.setDataSource(liveMusic.path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            finalTime = mediaPlayer?.duration!!.toLong().toInt()
            startTime = mediaPlayer?.currentPosition!!.toLong().toInt()
            Log.d(TAG, "startCallerTone finalTime: $finalTime ,startTime:$startTime")
            param.getTimer(finalTime, startTime, mediaPlayer!!)
            mediaPlayer?.setOnCompletionListener {
                currentMusic = null
                param.musicfinishcallback()
            }
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, e.toString())
        }
    }


}