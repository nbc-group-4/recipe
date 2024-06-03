package nbc.group.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.databinding.BottomsheetItemBinding

class BottomSheetAdapter(private val onClick : (TestData, Int) -> Unit) : ListAdapter<TestData, BottomSheetAdapter.BottomSheetViewHolder>(BottomSheetDiffUtil){

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

        private var currentItem : TestData?= null
        fun bind(item : TestData){
            currentItem = item

            Glide.with(itemView.context)
                .load(item.url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.recipeImage)

            binding.recipeName.text = item.title
            binding.recipeDescription.text = item.description
        }
    }

    object BottomSheetDiffUtil : DiffUtil.ItemCallback<TestData>(){
        override fun areItemsTheSame(oldItem: TestData, newItem: TestData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TestData, newItem: TestData): Boolean {
            return oldItem == newItem
        }
    }

}