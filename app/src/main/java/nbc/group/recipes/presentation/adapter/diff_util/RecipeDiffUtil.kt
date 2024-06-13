package nbc.group.recipes.presentation.adapter.diff_util

import androidx.recyclerview.widget.DiffUtil
import nbc.group.recipes.data.model.dto.Recipe

class RecipeDiffUtil: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}