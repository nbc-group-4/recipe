package nbc.group.recipes.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nbc.group.recipes.data.network.SPECIALTY_API_BASE
import nbc.group.recipes.data.network.RECIPE_API_BASE
import nbc.group.recipes.data.network.SpecialtyService
import nbc.group.recipes.data.network.RecipeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("Specialty")
    fun provideRetrofitAgriculture(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(SPECIALTY_API_BASE)
            .build()
    }

    @Singleton
    @Provides
    @Named("Recipe")
    fun provideRetrofitRecipe(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(RECIPE_API_BASE)
            .build()
    }

    @Singleton
    @Provides
    @Named("SpecialtyService")
    fun provideApiServiceAgriculture(
        @Named("Specialty") retrofit: Retrofit
    ): SpecialtyService {
        return retrofit.create(SpecialtyService::class.java)
    }

    @Singleton
    @Provides
    @Named("RecipeService")
    fun provideApiServiceRecipe(
        @Named("Recipe") retrofit: Retrofit
    ): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }
}
