package nbc.group.recipes.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import nbc.group.recipes.databinding.ItemAddImageBinding
import nbc.group.recipes.presentation.adapter.diff_util.UriDiffUtil

class MakeRecipeImageAdapter(
    private val removeButtonClickListener: (Int) -> Unit,
) :ListAdapter<Uri, MakeRecipeImageAdapter.RecipeImageViewHolder>(UriDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeImageViewHolder {
        val binding = ItemAddImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeImageViewHolder, position: Int) {
        val current = getItem(position)
        with(holder.binding) {
            iv.setImageURI(current)
            btRemove.setOnClickListener {
                removeButtonClickListener(position)
            }
        }
    }

    inner class RecipeImageViewHolder(
        val binding: ItemAddImageBinding,
    ) : ViewHolder(binding.root)
}