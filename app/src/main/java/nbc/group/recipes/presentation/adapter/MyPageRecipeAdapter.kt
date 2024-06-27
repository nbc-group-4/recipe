package nbc.group.recipes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import nbc.group.recipes.GlideApp
import nbc.group.recipes.databinding.ItemMypageRecipeBinding
import nbc.group.recipes.presentation.adapter.diff_util.RecipeDiffUtil
import nbc.group.recipes.presentation.fragment.MypageFragment

class MyPageRecipeAdapter(
    private val fragment: Fragment,
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<String, MyPageRecipeAdapter.MyRecipeViewHolder>(RecipeDiffUtil()) {

    interface OnItemClickListener {
        fun onClick(recipeId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipeViewHolder {
        val binding =
            ItemMypageRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRecipeViewHolder, position: Int) {
        val current = getItem(position)
        GlideApp.with(fragment)
            .load(Firebase.storage.reference.child("recipeImage/$current/0.jpg"))
            .into(holder.binding.ivRecipe)

        holder.binding.ivRecipe.setOnClickListener {
            onItemClickListener.onClick(current)
        }
    }

    inner class MyRecipeViewHolder(
        val binding: ItemMypageRecipeBinding
    ) : ViewHolder(binding.root)
}