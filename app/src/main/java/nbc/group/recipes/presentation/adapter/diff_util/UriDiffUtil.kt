package nbc.group.recipes.presentation.adapter.diff_util

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

class UriDiffUtil: DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }
}