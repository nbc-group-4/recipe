package nbc.group.recipes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nbc.group.recipes.R
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.databinding.BookmarkItemBinding

class BookMarkAdapter(
    private val onClick : (Recipe, Int) -> Unit,
    private val onLongClick : (Recipe, Int) -> Unit
): ListAdapter<Recipe, BookMarkAdapter.BookMarkViewHolder>(BookMarkDiffUtil){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookMarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item, parent, false)
        return BookMarkViewHolder(BookmarkItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick(item, position)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(item, position)
            true
        }

    }

    class BookMarkViewHolder(private var binding : BookmarkItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe : Recipe){

//            Glide.with(itemView.context)
//                .load()
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(binding.recipeImage)

            binding.recipeName.text = recipe.recipeName
            binding.recipeDescription.text = recipe.summary
            binding.recipeLevel.text = recipe.levelName
            binding.recipeTime.text = recipe.cookingTime

        }
    }


    object BookMarkDiffUtil : DiffUtil.ItemCallback<Recipe>(){
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

    }

}