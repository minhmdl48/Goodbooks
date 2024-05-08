package com.minhmdl.goodbooks.data.di

import com.minhmdl.goodbooks.data.BookRepository
import com.minhmdl.goodbooks.data.api.ApiConstants.BASE_ENDPOINT
import com.minhmdl.goodbooks.data.api.GoodbooksAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

        /**
        A Dagger provider method that provides a singleton instance of the BookRepository class.
         * @param api the BooksAPI dependency to be injected into the BookRepository constructor.
         * @return a singleton instance of the BookRepository class that depends on BooksAPI.
         */
        @Singleton
        @Provides
        fun provideBookRepository(api: GoodbooksAPI) = BookRepository(api)

        /**
        A Dagger provider method that provides a singleton instance of the BooksAPI interface.
         * @return a singleton instance of the BooksAPI interface created using a Retrofit builder.
         */
        @Singleton
        @Provides
        fun provideBookApi(): GoodbooksAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoodbooksAPI::class.java)
        }

}