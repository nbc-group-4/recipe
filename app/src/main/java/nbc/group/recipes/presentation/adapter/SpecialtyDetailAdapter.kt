package nbc.group.recipes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.KindItem
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.databinding.ItemSpecialtyDetailBinding

val diffCallbackSpecialtyDetail = object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.cntntsSj == newItem.cntntsSj
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

class SpecialtyDetailAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<Item, SpecialtyDetailAdapter.ItemViewHolder>(diffCallbackSpecialtyDetail) {

    class ItemViewHolder(
        private val binding: ItemSpecialtyDetailBinding,
        private val onItemClick: (Item) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.apply {
                Glide.with(root.context)
                    .load(item.imgUrl)
                    .into(ivSpecialty)
                tvSpecialtyName.text = item.cntntsSj
                tvSpecialtyRegion.text = item.areaName
                // 검색된 특산품 이미지, 명칭, 지역

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemSpecialtyDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
