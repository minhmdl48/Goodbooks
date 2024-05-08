package com.minhmdl.goodbooks.data

import android.util.Log
import com.minhmdl.goodbooks.data.api.GoodbooksAPI
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.model.ResourceConverter
import com.minhmdl.goodbooks.utils.DataOrException
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: GoodbooksAPI) {

    private val dataOrException = DataOrException<List<Book>, Boolean, Exception>()

    suspend fun getBooks(searchQuery: String): DataOrException<List<Book>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            val bookResource = api.getAllBooks(searchQuery)
            dataOrException.data = ResourceConverter().toBookList(bookResource)
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    private val bookInfoDataOrException = DataOrException<Book, Boolean, Exception>()

    suspend fun getBookInfo(bookId: String): DataOrException<Book, Boolean, Exception> {
        val response = try {
            bookInfoDataOrException.loading = true

            val book = api.getBookInfo(bookId)
            bookInfoDataOrException.data = ResourceConverter().toBook(book)
            if (bookInfoDataOrException.data.toString()
                    .isNotEmpty()
            ) bookInfoDataOrException.loading = false
            else {
            }
        } catch (e: Exception) {
            bookInfoDataOrException.e = e
            Log.e("BookRepository", "Error calling api: $e")
        }
        return bookInfoDataOrException
    }
}