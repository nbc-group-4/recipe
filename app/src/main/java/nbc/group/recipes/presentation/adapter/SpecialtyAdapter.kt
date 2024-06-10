package nbc.group.recipes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nbc.group.recipes.KindItem
import nbc.group.recipes.databinding.ItemSpecialtyBinding

val diffCallbackSpecialty = object : DiffUtil.ItemCallback<KindItem>() {
    override fun areItemsTheSame(oldItem: KindItem, newItem: KindItem): Boolean {
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(oldItem: KindItem, newItem: KindItem): Boolean {
        return oldItem == newItem
    }
}

class SpecialtyAdapter :
    ListAdapter<KindItem, SpecialtyAdapter.ItemViewHolder>(diffCallbackSpecialty) {

    class ItemViewHolder(
        private val binding: ItemSpecialtyBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: KindItem) {
            binding.apply {
                tvSpecialty.text = items.item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemSpecialtyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}