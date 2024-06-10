package nbc.group.recipes.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.KindItem
import nbc.group.recipes.databinding.ItemHomeKindBinding

val diffCallbackHomeKind = object : DiffUtil.ItemCallback<KindItem>() {
    override fun areItemsTheSame(oldItem: KindItem, newItem: KindItem): Boolean {
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(oldItem: KindItem, newItem: KindItem): Boolean {
        return oldItem == newItem
    }
}

class HomeKindAdapter(
    private val itemClickListener: OnItemClickListener
): ListAdapter<KindItem, HomeKindAdapter.ItemViewHolder>(diffCallbackHomeKind) {

    interface OnItemClickListener {
        fun onClick(data: KindItem)
    }

    class ItemViewHolder(
        private val binding: ItemHomeKindBinding,
        private val itemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: KindItem) {
            binding.apply {
                Glide.with(root.context)
                    .load(items.imageResourceId)
                    .into(ivKind)

                tvKind.text = items.item

                root.setOnClickListener {
                    itemClickListener?.onClick(items)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemHomeKindBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}