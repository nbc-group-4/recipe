package nbc.group.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.databinding.BottomsheetItemBinding

class BottomSheetAdapter(var recipeData : ArrayList<TestData>, private val onClick : (TestData, Int) -> Unit) : RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_item, parent, false)
        return BottomSheetViewHolder(BottomsheetItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(recipeData[position])

        holder.itemView.setOnClickListener {
            onClick(recipeData[position], position)
        }

    }

    override fun getItemCount(): Int = recipeData.size

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

    fun updateData(testData: List<TestData>) {
        recipeData.clear()
        recipeData.addAll(testData)
        notifyDataSetChanged()
    }

}