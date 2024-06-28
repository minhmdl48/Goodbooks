package com.minhmdl.goodbooks.data


import com.minhmdl.goodbooks.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface FirebaseRepository {

    suspend fun getBooks(shelfName: String): Flow<List<Book>>


}