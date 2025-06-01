package com.example.wannaeat.di

import android.content.Context
import android.content.SharedPreferences
import com.example.wannaeat.data.remote.OpenAiApi
import com.example.wannaeat.data.repository.RecipeRepositoryImpl
import com.example.wannaeat.domain.RecipeRepository
import com.example.wanneat.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val API_KEY = BuildConfig.OPENAI_API_KEY

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideOpenApi(
        @ApplicationContext context: Context
    ): OpenAiApi {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $API_KEY")
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(api: OpenAiApi, sharedPreferences: SharedPreferences): RecipeRepository = RecipeRepositoryImpl(
        api,
        sharedPreferences
    )

}