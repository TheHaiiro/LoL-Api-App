package com.example.pracazaliczeniowa.ui.champions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pracazaliczeniowa.data.ChampionEntity
import com.example.pracazaliczeniowa.databinding.ChampionListItemBinding

/**
 * Adapter for the champions RecyclerView.
 * Uses ListAdapter for efficient list updates.
 */
class ChampionsAdapter(private val onClick: (ChampionEntity) -> Unit) :
    ListAdapter<ChampionEntity, ChampionsAdapter.ChampionViewHolder>(ChampionDiffCallback) {

    /**
     * ViewHolder for a single champion item in the list.
     */
    class ChampionViewHolder(private val binding: ChampionListItemBinding, val onClick: (ChampionEntity) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentChampion: ChampionEntity? = null

        init {
            itemView.setOnClickListener {
                currentChampion?.let {
                    onClick(it)
                }
            }
        }

        fun bind(champion: ChampionEntity) {
            currentChampion = champion
            binding.championName.text = champion.name
            Glide.with(binding.root.context)
                .load(champion.imageUrl)
                .circleCrop()
                .into(binding.championImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionViewHolder {
        val binding = ChampionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChampionViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ChampionViewHolder, position: Int) {
        val champion = getItem(position)
        holder.bind(champion)
    }
}

/**
 * DiffUtil.ItemCallback implementation for the ChampionsAdapter.
 * It helps the ListAdapter determine which items in the list have changed.
 */
object ChampionDiffCallback : DiffUtil.ItemCallback<ChampionEntity>() {
    override fun areItemsTheSame(oldItem: ChampionEntity, newItem: ChampionEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChampionEntity, newItem: ChampionEntity): Boolean {
        return oldItem == newItem
    }
}
