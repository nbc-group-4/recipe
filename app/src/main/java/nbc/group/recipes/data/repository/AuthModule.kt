package nbc.group.recipes.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository = authRepositoryImpl

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage

    @Singleton
    @Provides
    fun provideFirebaseRepository(
        firebaseRepositoryImpl: FirebaseRepositoryImpl
    ): FirebaseRepository = firebaseRepositoryImpl

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository = recipeRepositoryImpl

    @Singleton
    @Provides
    fun provideBanRepository(
        banRepositoryImpl: BanRepositoryImpl
    ): BanRepository = banRepositoryImpl
}