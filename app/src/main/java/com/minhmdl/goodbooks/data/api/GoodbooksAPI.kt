package com.minhmdl.goodbooks.data.api

import com.minhmdl.goodbooks.model.BooksResource
import com.minhmdl.goodbooks.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

/**
An interface that represents an API for retrieving books.
This interface uses Retrofit to interact with a remote API that provides information on books.
This interface is annotated with the @Singleton annotation to indicate that a single instance of the object will be used across the entire application.
 */
@Singleton
interface GoodbooksAPI {

    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): BooksResource

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): Item
}