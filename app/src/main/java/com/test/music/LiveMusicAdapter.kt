package com.test.music

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.test.music.dataModel.LiveMusic
import java.util.*


class LiveMusicAdapter(
    private val liveMusicAdapterListener: LiveMusicAdapterListener,
    private val isSelection: Boolean
) : RecyclerView.Adapter<LiveMusicViewHolder>() {
    private var liveMusicList = ArrayList<LiveMusic>()

    val initialLiveMusicList = ArrayList<LiveMusic>().apply {
        addAll(liveMusicList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveMusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.live_music_item, parent, false)

        return LiveMusicViewHolder(view, liveMusicAdapterListener, isSelection)
    }

    override fun onBindViewHolder(holder: LiveMusicViewHolder, position: Int) {
        holder.bindData(liveMusicList[position])
    }

    override fun getItemCount(): Int = liveMusicList.size

    fun setData(liveMusicList: ArrayList<LiveMusic>) {
        Log.d(TAG, "setData: liveMusicList $liveMusicList")
        if (liveMusicList != null) {
            this.liveMusicList = liveMusicList
            initialLiveMusicList.addAll(liveMusicList)
            notifyDataSetChanged()
        }
    }


    companion object {
        private const val TAG = "LiveMusicAdapter"
    }

    private val musicFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<LiveMusic> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialLiveMusicList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialLiveMusicList.forEach {
                    if (it.name.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                liveMusicList = results.values as ArrayList<LiveMusic>
                notifyDataSetChanged()
            }
        }
    }




    interface LiveMusicAdapterListener {
        fun onClickMusic(liveMusic: LiveMusic, position: Int)
        fun onClickAddMusic(liveMusic: LiveMusic, position: Int)
        fun onClickDeleteMusic(liveMusic: LiveMusic)
    }


}