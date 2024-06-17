package com.minhmdl.goodbooks.screens.stats

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.minhmdl.goodbooks.data.BookRepository
import com.minhmdl.goodbooks.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {
    suspend fun getReadCount(userId: String?): Int = withContext(Dispatchers.IO) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val progressReading = documentSnapshot.toObject<User>()?.progressReading
                    if (progressReading != null) {
                        return@withContext progressReading.size
                    }
                }
            }
        }
        return@withContext 0
    }

    suspend fun getNumberOfBooksReadEachMonth(userId: String?): Map<Int, Int> = withContext(Dispatchers.IO) {
        val booksReadEachMonth = mutableMapOf<Int, Int>()
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().await().let { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dateReading = documentSnapshot.toObject<User>()?.dates
                    if (dateReading != null) {
                        for (date in dateReading) {
                            val dateStr = if (date.date.isNullOrEmpty()) {
                                // Use the current date string when the date string is null or empty
                                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault()))
                            } else {
                                date.date
                            }
                            val dateMonth = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())).monthValue
                            booksReadEachMonth[dateMonth] = booksReadEachMonth.getOrDefault(dateMonth, 0) + 1
                        }
                    }
                }
            }
        }
        return@withContext booksReadEachMonth
    }
//suspend fun getNumberOfBooksReadEachMonth(userId: String?, year: Int): Map<Int, Int> = withContext(Dispatchers.IO) {
//    val booksReadEachMonth = mutableMapOf<Int, Int>()
//    if (userId != null) {
//        val db = FirebaseFirestore.getInstance().collection("users").document(userId)
//        db.get().await().let { documentSnapshot ->
//            if (documentSnapshot.exists()) {
//                val dateReading = documentSnapshot.toObject<User>()?.dates
//                if (dateReading != null) {
//                    for (date in dateReading) {
//                        val localDate = LocalDate.parse(date.date, DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault()))
//                        if (localDate.year == year) {
//                            val dateMonth = localDate.monthValue
//                            booksReadEachMonth[dateMonth] = booksReadEachMonth.getOrDefault(dateMonth, 0) + 1
//                        }
//                    }
//                }
//            }
//        }
//    }
//    return@withContext booksReadEachMonth
//}
}