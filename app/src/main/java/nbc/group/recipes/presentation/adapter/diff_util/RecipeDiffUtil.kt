package nbc.group.recipes.presentation.adapter.diff_util

import androidx.recyclerview.widget.DiffUtil
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.entity.RecipeEntity

class RecipeDiffUtil: DiffUtil.ItemCallback<RecipeEntity>() {
    override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem == newItem
    }
}