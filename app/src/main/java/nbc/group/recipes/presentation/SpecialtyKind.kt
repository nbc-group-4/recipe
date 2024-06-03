package nbc.group.recipes.presentation

import nbc.group.recipes.R

val specialtyKind = listOf(
    KindItem(
        item = "곡물류",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties1
    ),
    KindItem(
        item = "채소류",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties2
    ),
    KindItem(
        item = "과일류",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties3
    ),
    KindItem(
        item = "어류",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties4
    ),
    KindItem(
        item = "해조류",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties5
    ),
    KindItem(
        item = "고기류",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties6
    ),
)

// 더보기 아이템
val specialtyKindMore = listOf(
    KindItem(
        item = "쌀",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties7
    ),
    KindItem(
        item = "기타",
        imageResourceId = R.drawable.img_specialty_sample7
        // img_specialties8
    )
)

data class KindItem(
    val item: String,
    val imageResourceId: Int,
    val isMore: Boolean = false // 더보기 여부를 나타내는 속성
)


// 홈퀴즈어댑터 테스트
val dummyRecipe = listOf(
    RecipeItem(
        item = "1",
        imageResourceId = R.drawable.img_recipe_sample
    ),
    RecipeItem(
        item = "2",
        imageResourceId = R.drawable.img_recipe_sample
    ),
    RecipeItem(
        item = "3",
        imageResourceId = R.drawable.img_recipe_sample
    ),
    RecipeItem(
        item = "4",
        imageResourceId = R.drawable.img_recipe_sample
    ),
)

data class RecipeItem(
    val item: String,
    val imageResourceId: Int,
)