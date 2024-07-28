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

@Module // -> Hilt 에게 해당 클래스가 모듈임을 알려줌, 의존성 제공
@InstallIn(SingletonComponent::class) // -> 모듈이 설치될 컴포넌트 지정, SingletonComponent에 설치 (앱 전체에서 싱글톤 스코프 가짐)
object RetrofitModule {

    @Singleton // -> 의존성이 싱글톤 형식임 = 하나의 인스턴스만 생성됨
    @Provides // -> 의존성을 제공하는 메서드 표시
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("Specialty") // -> Hilt가 여러 구현체 중 어떤 것을 주입해야 하는지 지정할 때 사용
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
