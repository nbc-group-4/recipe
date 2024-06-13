package nbc.group.recipes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.R
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.databinding.BottomsheetItemBinding

class BottomSheetAdapter(
    private val onClick : (Item, Int) -> Unit
) : ListAdapter<Item, BottomSheetAdapter.BottomSheetViewHolder>(BottomSheetDiffUtil){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_item, parent, false)
        return BottomSheetViewHolder(BottomsheetItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick(item, position)
        }
    }


    class BottomSheetViewHolder(private var binding : BottomsheetItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Item){

            Glide.with(itemView.context)
                .load(item.imgUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.recipeImage)

            binding.recipeName.text = item.cntntsSj
        }
    }

    object BottomSheetDiffUtil : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.areaName == newItem.areaName
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

}