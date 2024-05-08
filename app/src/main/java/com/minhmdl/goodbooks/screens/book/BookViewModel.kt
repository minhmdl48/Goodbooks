package com.minhmdl.goodbooks.screens.book

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.minhmdl.goodbooks.data.BookRepository
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.model.Shelf
import com.minhmdl.goodbooks.model.User
import com.minhmdl.goodbooks.utils.DataOrException

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
ViewModel class that provides data related to a specific book.
 * This class uses Hilt for dependency injection and relies on the [BookRepository] to provide book data.
 * @param repository the repository responsible for providing book data
 */
@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    private val _bookToMove = MutableLiveData<Book?>()
    private val bookToMove: MutableLiveData<Book?> = _bookToMove

    fun setBookToMove(book: Book) {
        _bookToMove.value = book
    }

    fun clearBookToMove() {
        _bookToMove.value = null
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Book, Boolean, Exception> {
        return repository.getBookInfo(bookId)
    }

    suspend fun moveBookToShelf(userId: String, shelfName: String, otherShelfName: String) {
        val db = FirebaseFirestore.getInstance().collection("users").document(userId)
        val documentSnapshot = db.get().await()
        if (documentSnapshot.exists()) {
            val shelves = documentSnapshot.toObject<User>()?.shelves as MutableList<Shelf>
            val otherShelf: Shelf? = shelves.find { it.name == otherShelfName }
            val currentShelf: Shelf? = shelves.find { it.name == shelfName }
            if (otherShelf != null && currentShelf != null) {
                val otherBooks: MutableList<Book> = otherShelf.books as MutableList<Book>
                otherBooks.removeIf { it.bookID == bookToMove.value?.bookID }
                otherShelf.books = otherBooks
                val index1 = shelves.indexOfFirst { it.name == otherShelfName }
                shelves[index1] = otherShelf

                val currentBooks: MutableList<Book> = currentShelf.books as MutableList<Book>
                if (!currentBooks.any { it.bookID == bookToMove.value?.bookID }) {
                    bookToMove.value?.let { currentBooks.add(it) }
                }
                currentShelf.books = currentBooks
                val index2 = shelves.indexOfFirst { it.name == shelfName }
                shelves[index2] = currentShelf
                db.update("shelves", shelves).await()
            }
        }
    }

    suspend fun addBookToShelf(
        userId: String?,
        shelfName: String,
        book: Book,
        context: Context,
        shelfExists: (String) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val shelves = documentSnapshot.toObject<User>()?.shelves as MutableList<Shelf>
                    val shelf: Shelf? = shelves.find { shelf ->
                        shelf.name == shelfName
                    }
                    if (shelf != null) {
                        val books: MutableList<Book> = shelf.books as MutableList<Book>
                        if (!books.any { it.bookID == book.bookID }) {
                            val otherShelf = shelves.find { otherShelf ->
                                otherShelf.name != shelfName && otherShelf.books.any { it.bookID == book.bookID }
                            }
                            if (otherShelf != null) {
                                shelfExists(otherShelf.name)
                            } else {
                                books.add(book)
                                shelf.books = books
                                val index = shelves.indexOfFirst { it.name == shelfName }
                                shelves[index] = shelf
                                db.update("shelves", shelves).await()
                                return@withContext true
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Book already in the shelf",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Log.d("AddBookToShelf", "Shelf not found: $shelfName")
                    }
                } else {
                    Log.d("AddBookToShelf", "Document snapshot does not exist for user: $userId")
                }
            }
        } else {
            Log.d("AddBookToShelf", "User ID is null")
        }
        return@withContext false
    }
}