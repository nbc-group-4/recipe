package nbc.group.recipes.presentation.fragment.specialty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.R
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.databinding.ItemSpecialtyBinding

val diffCallbackSpecialty = object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.cntntsSj == newItem.cntntsSj
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

class SpecialtyAdapter :
    ListAdapter<Item, SpecialtyAdapter.ItemViewHolder>(diffCallbackSpecialty) {

    class ItemViewHolder(
        private val binding: ItemSpecialtyBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Item) {
            binding.apply {
                Glide.with(root.context)
                    .load(items.imgUrl)
                    .into(ivSpecialty)

                tvSpecialty.text = items.cntntsSj
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