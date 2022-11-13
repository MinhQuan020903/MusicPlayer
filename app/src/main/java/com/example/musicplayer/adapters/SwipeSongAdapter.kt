package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.databinding.SwipeItemBinding
import com.google.android.exoplayer2.util.Log
import javax.inject.Inject

class SwipeSongAdapter @Inject constructor(
) : RecyclerView.Adapter<SwipeSongAdapter.SongViewHolder>() {

    lateinit var binding : SwipeItemBinding

    class SongViewHolder(binding : SwipeItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var songs : List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
       binding = SwipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return SongViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            val text = "${song.title} -  ${song.subtitle}"
            binding.tvPrimary.text = text
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }

    private var onItemClickListener : ((Song) -> Unit)? = null

    fun setItemClickListener(listener : (Song) -> Unit)  {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return songs.size
    }
}