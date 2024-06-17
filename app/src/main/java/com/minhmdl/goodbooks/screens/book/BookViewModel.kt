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
import com.minhmdl.goodbooks.model.Review
import com.minhmdl.goodbooks.model.Shelf
import com.minhmdl.goodbooks.model.User
import com.minhmdl.goodbooks.model.dateTime
import com.minhmdl.goodbooks.model.progressReading
import com.minhmdl.goodbooks.utils.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {
    suspend fun getBookInfo(bookId: String): DataOrException<Book, Boolean, Exception> {
        return repository.getBookInfo(bookId)
    }

    suspend fun addBook(
        userID: String,
        shelfName: String,
        book: Book,
        context: Context,
        shelfExists: (String) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {

        val db = FirebaseFirestore.getInstance().collection("users").document(userID)
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
                Log.d("AddBookToShelf", "Document snapshot does not exist for user: $userID")
            }
        }

        return@withContext false
    }

    suspend fun deleteABookInShelf(userId: String?, book: Book, shelfName: String?): Boolean =
        withContext(Dispatchers.IO) {
            if (userId != null) {
                val db = FirebaseFirestore.getInstance().collection("users").document(userId)
                db.get().await().let { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userShelves =
                            documentSnapshot.toObject<User>()?.shelves as MutableList<Shelf>
                        val shelf = userShelves.find { it.name == shelfName }
                        if (shelf != null) {
                            val books: MutableList<Book> = shelf.books as MutableList<Book>
                            books.removeIf { it.bookID == book.bookID }
                            shelf.books = books
                            // Update shelves
                            val index = userShelves.indexOfFirst { it.name == shelfName }
                            userShelves[index] = shelf
                            db.update("shelves", userShelves).await()
                            return@withContext true
                        }
                    }
                }
            }
            return@withContext false
        }

    suspend fun getShelfName(userID: String?, book: Book): String = withContext(Dispatchers.IO) {
        if (userID != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userID)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userShelves = documentSnapshot.toObject<User>()?.shelves
                    if (userShelves != null) {
                        for (shelf in userShelves) {
                            if (shelf.books.any { it.bookID == book.bookID }) {
                                return@withContext shelf.name
                            }
                        }
                    }
                }
            }
        }
        return@withContext ""
    }

    suspend fun getProgressReading(userId: String?, bookId: String): Int = withContext(Dispatchers.IO) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val progressReading = documentSnapshot.toObject<User>()?.progressReading
                    if (progressReading != null) {
                        for (progress in progressReading) {
                            if (progress.bookId == bookId) {
                                return@withContext progress.progress
                            }
                        }
                    }
                }
            }
        }
        return@withContext 0
    }

    suspend fun setProgressReading(userId: String?, bookId: String, progress: Int) = withContext(Dispatchers.IO) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject<User>()
                    val progressReadingList = user?.progressReading?.toMutableList()
                    val existingProgress = progressReadingList?.find { it.bookId == bookId }

                    if (existingProgress != null) {
                        // Update the existing progress
                        existingProgress.progress = progress
                    } else {
                        // Add new progressReading if it doesn't exist
                        progressReadingList?.add(progressReading(bookId, progress))
                    }

                    // Update the progressReading in Firestore
                    db.update("progressReading", progressReadingList).await()
                }
            }
        }
    }

    suspend fun setReviewReading(userId: String?, bookId: String, reviewText:String) = withContext(Dispatchers.IO){
        if(userId !=null){
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject<User>()
                    val reviewReadingList = user?.reviews?.toMutableList()
                    val existingReview = reviewReadingList?.find { it.bookId == bookId }

                    if (existingReview != null) {
                        // Update the existing progress
                        existingReview.reviewText = reviewText
                    } else {
                        // Add new progressReading if it doesn't exist
                        reviewReadingList?.add(Review(bookId, reviewText))
                    }

                    // Update the progressReading in Firestore
                    db.update("reviews", reviewReadingList).await()
                }
            }
        }
    }

    suspend fun getReview(userId:String?, bookId: String):String = withContext(Dispatchers.IO){
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val reviewReading = documentSnapshot.toObject<User>()?.reviews
                    if (reviewReading != null) {
                        for (review in reviewReading) {
                            if (review.bookId == bookId) {
                                return@withContext review.reviewText
                            }
                        }
                    }
                }
            }
        }
        return@withContext ""
    }


    suspend fun setDate(userId: String?, bookId: String, date: String) = withContext(Dispatchers.IO) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject<User>()
                    val dateReadingList = user?.dates?.toMutableList()
                    val existingDate = dateReadingList?.find { it.bookId == bookId }

                    if (existingDate != null) {
                        // Update the existing progress
                        if (date != null) {
                            existingDate.date = date
                        }
                    } else {
                        // Add new progressReading if it doesn't exist
                        dateReadingList?.add(dateTime(bookId, date))
                    }

                    // Update the progressReading in Firestore
                    db.update("dates", dateReadingList).await()
                }
            }
        }
    }

    suspend fun getDate(userId: String?, bookId: String): String = withContext(Dispatchers.IO) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dateReading = documentSnapshot.toObject<User>()?.dates
                    if (dateReading != null) {
                        for (date in dateReading) {
                            if (date.bookId == bookId) {
                                return@withContext date.date
                            }
                        }
                    }
                }
            }
        }
        return@withContext ""
    }



}