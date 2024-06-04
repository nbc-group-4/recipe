package nbc.group.recipes.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nbc.group.recipes.RecipeItem
import nbc.group.recipes.databinding.ItemHomeQuizBinding

// RecipeItem 더미 데이터 테스트
val diffCallbackHomeQuiz = object : DiffUtil.ItemCallback<RecipeItem>() {
    override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean {
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean {
        return oldItem == newItem
    }
}

class HomeQuizAdapter :
    ListAdapter<RecipeItem, HomeQuizAdapter.ItemViewHolder>(diffCallbackHomeQuiz) {

    // 이미지 데이터 소스?
    // 퀴즈 로직?

    class ItemViewHolder(
        private val binding: ItemHomeQuizBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: RecipeItem) {
            binding.apply {
                Glide.with(root.context)
                    .load(items.imageResourceId)
                    .into(ivQuiz)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemHomeQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}