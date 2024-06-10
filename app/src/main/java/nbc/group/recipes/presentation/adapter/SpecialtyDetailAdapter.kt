package nbc.group.recipes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

class SpecialtyDetailAdapter :
    ListAdapter<Item, SpecialtyDetailAdapter.ItemViewHolder>(diffCallbackSpecialtyDetail) {

    class ItemViewHolder(
        private val binding: ItemSpecialtyDetailBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Item) {
            binding.apply {
                Glide.with(root.context)
                    .load(items.imgUrl)
                    .into(ivSpecialty)

                binding.tvSpecialtyName.text = items.cntntsSj
                binding.tvSpecialtyRegion.text = items.areaName
                // 검색된 특산품 이미지, 명칭, 지역
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemSpecialtyDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}