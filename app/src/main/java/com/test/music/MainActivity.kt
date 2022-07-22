package com.test.music

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.test.music.dataModel.LiveMusic
import com.test.music.viewModel.MusicViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LiveMusicAdapter.LiveMusicAdapterListener {
    private val TAG: String = "MainActivity"

    private val mMusicViewModel: MusicViewModel by viewModels()
    private val myHandler: Handler = Handler()
    private var startTime: Int? = null
    val selectedMusicList: MutableList<LiveMusic> = java.util.ArrayList()
    var audioManager: AudioManager? = null
    private var mLiveMusicAdapter: LiveMusicAdapter? = null

    lateinit var selectMusicrecyleview: RecyclerView
    lateinit var selectedMusicRecyclerView: RecyclerView
    lateinit var selectedMusic: TextView
    lateinit var musicView: RelativeLayout
    lateinit var progressbar: SeekBar
    lateinit var mediaPlayer: MediaPlayer
    lateinit var fabview: View
    lateinit var stopMusic: TextView
    private var isgranted: Boolean = false

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        config()
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)


    }


    private fun init() {
        fabview = findViewById(R.id.fab)
        selectedMusicRecyclerView = findViewById(R.id.selected_recycler_view)
        musicView = findViewById(R.id.playview)
        progressbar = findViewById(R.id.sound_seekBar)
        stopMusic = findViewById(R.id.stop_music)
    }

    private fun config() {
        audioManager = AudioManager()
        fabview.setOnClickListener {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)){
                openBottomSheetDialog()
            }else{
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
            }
        }
        stopMusic.setOnClickListener {
            musicView.visibility = View.GONE
            fabview.visibility = View.VISIBLE
            audioManager?.stopMusic()
            myHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
    add bottom sheet dialog to select music file from the device storage.
     */
    private fun openBottomSheetDialog() {
        fetchLiveMusicData()
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.select_music_dialog, null)
        selectMusicrecyleview = view.findViewById<RecyclerView>(R.id.music_recycler_view)
        selectedMusic = view.findViewById(R.id.music_number_text_view)
        mLiveMusicAdapter = LiveMusicAdapter(this, true)
        selectMusicrecyleview.adapter = mLiveMusicAdapter
        selectedMusic.setOnClickListener {
            dialog.dismiss()
            mLiveMusicAdapter = LiveMusicAdapter(this, false)
            selectedMusicRecyclerView.adapter = mLiveMusicAdapter
            mLiveMusicAdapter!!.setData(selectedMusicList as ArrayList)

        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun fetchLiveMusicData() {
        lifecycleScope.launch {
            mMusicViewModel?.getLocalDeviceMusic(application)?.let { liveMusicList ->
                Log.d(TAG, "fetchLiveMusicData: liveMusicList $liveMusicList")
                mLiveMusicAdapter!!.setData(liveMusicList as ArrayList)
            }
        }
    }

    override fun getLifecycle(): Lifecycle {
        return super.getLifecycle()
    }

    private val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition
            progressbar.progress = startTime!!.toInt()
            myHandler.postDelayed(this, 100)
        }
    }

    override fun onClickMusic(liveMusic: LiveMusic, position: Int) {
        audioManager?.startMusic(applicationContext, liveMusic, object : MusicInterval {
            override fun getTimer(finalTime: Int, startTimer: Int, sMediaPlayer: MediaPlayer) {
                mediaPlayer = sMediaPlayer
                musicView.visibility = View.VISIBLE
                fabview.visibility = View.GONE
                progressbar.max = finalTime
                progressbar.progress = startTimer as Int
                myHandler.postDelayed(UpdateSongTime, 100);
            }

            override fun musicfinishcallback() {
                musicView.visibility = View.GONE
                fabview.visibility = View.VISIBLE

            }

        })
    }

    override fun onClickAddMusic(liveMusic: LiveMusic, position: Int) {
        Log.d(TAG, "onClickAddMusic")
        selectedMusicList.add(liveMusic)
        if(selectedMusicList.size>0){
            selectedMusic.text = selectedMusicList.size.toString().plus("/click hear to Add")
        }

    }

    override fun onClickDeleteMusic(liveMusic: LiveMusic) {
        Log.d(TAG, "onClickDeleteMusic")

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        audioManager?.stopMusic()
    }

    /**
     * require permission
     */
    private fun checkPermission(permission: String, requestCode: Int): Boolean {

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            isgranted = true
        }
        return isgranted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    interface MusicInterval {
        fun getTimer(finalTime: Int, startTime: Int, mediaPlayer: MediaPlayer)
        fun musicfinishcallback()
    }
}