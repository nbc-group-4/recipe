package nbc.group.recipes.data.network.di

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nbc.group.recipes.data.network.NaverSearchService
import nbc.group.recipes.data.network.SPECIALTY_API_BASE
import nbc.group.recipes.data.network.RECIPE_API_BASE
import nbc.group.recipes.data.network.NAVER_API_BASE
import nbc.group.recipes.data.network.SpecialtyService
import nbc.group.recipes.data.network.RecipeService
import nbc.group.recipes.data.network.SEARCH_API_BASE
import nbc.group.recipes.data.network.SearchService
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
    fun provideRetrofitSpecialty(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                TikXmlConverterFactory
                    .create(TikXml.Builder().exceptionOnUnreadXml(false).build())
            )
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
    @Named("SearchRegion")
    fun provideRetrofitSearchRegion(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(SEARCH_API_BASE)
            .build()
    }


    @Singleton
    @Provides
    @Named("SpecialtyService")
    fun provideApiServiceSpecialty(
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

    @Singleton
    @Provides
    @Named("SearchService")
    fun provideApiServiceSearch(
        @Named("SearchRegion") retrofit: Retrofit
    ): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Singleton
    @Provides
    @Named("NaverSearch")
    fun provideRetrofitNaverSearch(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(NAVER_API_BASE)
            .build()
    }

    @Singleton
    @Provides
    @Named("NaverSearchService")
    fun provideApiServiceNaverSearch(
        @Named("NaverSearch") retrofit: Retrofit
    ): NaverSearchService {
        return retrofit.create(NaverSearchService::class.java)
    }
}
