package com.test.music.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.test.music.dataModel.LiveMusic
import com.test.music.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicViewModel() : ViewModel() {

    private lateinit var mMusicRepository: MusicRepository
    suspend fun getLocalDeviceMusic(application: Application): List<LiveMusic> =
        withContext(Dispatchers.IO) {
            mMusicRepository = MusicRepository(application)
            return@withContext mMusicRepository.getLocalDeviceMusic()
        }

    companion object {
        private const val TAG = "MusicViewModel"
    }

}