package nbc.group.recipes.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.R
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.databinding.RecipeItemViewBinding

class RecipeAdapter (
    private val onClick: (RecipeEntity) -> Unit,
): ListAdapter<RecipeEntity, RecipeAdapter.ViewHolder>(diff) {

    class ViewHolder(
        private val binding: RecipeItemViewBinding,
        private val onClick: (RecipeEntity) -> Unit,
    ): RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: RecipeEntity

        init {
            binding.root.setOnClickListener {
                onClick(item)
            }
        }

        fun bind(recipeInfo: RecipeEntity) {
            item = recipeInfo

            Glide.with(binding.root.context)
                .load(recipeInfo.recipeImg)
                .placeholder(R.drawable.ic_reload)
                .into(binding.ivRecipeImg)
            binding.tvRecipeName.text = recipeInfo.recipeName
            binding.tvRecipeDifficulty.text = recipeInfo.difficulty
            binding.tvRecipeTime.text = recipeInfo.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecipeItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<RecipeEntity>() {
            override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}