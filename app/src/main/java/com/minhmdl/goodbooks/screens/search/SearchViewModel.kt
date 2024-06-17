package com.minhmdl.goodbooks.screens.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhmdl.goodbooks.data.BookRepository
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.utils.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {
    var resultsState: MutableState<DataOrException<List<Book>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), false, Exception("")))

    var listOfBooks: MutableState<List<Book>> = mutableStateOf(listOf())

    var loading: MutableState<Boolean> = mutableStateOf(false)

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                return@launch
            }
            // Clear items from the previous search
            listOfBooks.value = listOf()
            resultsState.value = repository.getBooks(query)

            if (resultsState.value.data != null) {
                listOfBooks.value = resultsState.value.data!!
            }
        }
    }
}