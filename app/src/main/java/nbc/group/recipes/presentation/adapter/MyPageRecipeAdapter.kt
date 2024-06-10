package nbc.group.recipes.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import nbc.group.recipes.GlideApp
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.databinding.ItemMypageRecipeBinding
import nbc.group.recipes.presentation.adapter.diff_util.RecipeDiffUtil

class MyPageRecipeAdapter(
    private val fragment: Fragment
): ListAdapter<Recipe, MyPageRecipeAdapter.MyRecipeViewHolder>(RecipeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipeViewHolder {
        val binding = ItemMypageRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRecipeViewHolder, position: Int) {
        val current = getItem(position)
        with(holder.binding) {
            tvRecipe.text = current.recipeName

            // todo: Recipe Wrapper를 이용하여 Recipe 이미지 경로 가져오기
            GlideApp.with(fragment)
                .load(Firebase.storage.reference.child("userProfile/jun/profile.jpg"))
                .into(ivRecipe)
        }
    }

    inner class MyRecipeViewHolder(
        val binding: ItemMypageRecipeBinding
    ): ViewHolder(binding.root)
}
