package com.test.music

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.music.dataModel.LiveMusic
import de.hdodenhof.circleimageview.CircleImageView


class LiveMusicViewHolder(
    itemView: View,
    private val liveMusicAdapterListener: LiveMusicAdapter.LiveMusicAdapterListener,
    private val isSelection: Boolean
) : RecyclerView.ViewHolder(itemView) {
    private lateinit var liveMusic: LiveMusic
    val musicName: TextView = itemView.findViewById(R.id.music_name_text_view)
    val addToPlayListImageView: ImageView = itemView.findViewById(R.id.add_to_play_list_image_view)
    val albumImageView: CircleImageView = itemView.findViewById(R.id.album_image_view)
    fun bindData(liveMusic: LiveMusic) {
        if (liveMusic != null) {
            this.liveMusic = liveMusic
            musicName.text = liveMusic.name

            if (isSelection) {
                addToPlayListImageView.visibility = VISIBLE
            } else {
                addToPlayListImageView.visibility = GONE
            }


            addToPlayListImageView.setOnClickListener {
                if (liveMusicAdapterListener != null) {
                    Log.d(TAG, "bindData position: $adapterPosition")
                    liveMusicAdapterListener.onClickAddMusic(liveMusic, adapterPosition)
                }

            }
            Glide
                .with(itemView)
                .load("uri")
                .centerCrop()
                .placeholder(R.drawable.ic_default_music)
                .error(R.drawable.ic_default_music)
                .into(albumImageView);

            itemView.setOnClickListener {
                if (liveMusicAdapterListener != null) {
                    if (!isSelection) {
                        liveMusicAdapterListener.onClickMusic(liveMusic, adapterPosition)
                    }
                }
            }
        }
    }


    companion object {
        private const val TAG = "LiveMusicViewHolder"
    }


}