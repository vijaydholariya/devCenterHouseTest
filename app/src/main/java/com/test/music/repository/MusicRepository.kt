package com.test.music.repository

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.test.music.dataModel.LiveMusic


class MusicRepository(private val application: Application) {



    suspend fun getLocalDeviceMusic(): List<LiveMusic> {
        Log.d(TAG, "getLocalDeviceMusic: ")
        val liveMusicList: MutableList<LiveMusic> = ArrayList()

        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor: Cursor? = application.contentResolver.query(
            uri,
            projection,
            MediaStore.Audio.Media.IS_MUSIC + " = 1",
            null,
            null
        )

        if (cursor != null) {
            Log.d(TAG, "getLocalDeviceMusic: cursor $cursor")
            while (cursor.moveToNext()) {
                val path = cursor.getString(0)
                val id = cursor.getInt(3)
                val name = path.substring(path.lastIndexOf("/") + 1)

                val liveMusic = LiveMusic(
                    id = id,
                    path = path,
                    name = name,
                ).apply {
                }

                Log.d(TAG, "id :$id")
                liveMusicList.add(liveMusic)
            }
            cursor.close()
        } else {
            Log.d(TAG, "getLocalDeviceMusic: ")
        }

        Log.d(TAG, "getLocalDeviceMusic: liveMusicList $liveMusicList")
        return liveMusicList
    }


    companion object {
        private const val TAG = "MusicRepository"
    }
}