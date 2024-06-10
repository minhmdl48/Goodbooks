package com.minhmdl.goodbooks.screens.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.model.User


class HomeViewModel : ViewModel() {
    var books: MutableState<MutableList<Book>> = mutableStateOf(mutableListOf())

    fun getBooksInReadingList(
        userId: String?,
        context: Context,
        onDone: () -> Unit
    ): MutableList<Book> {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance().collection("users").document(userId)
            db.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val shelves = documentSnapshot.toObject<User>()?.shelves
                    if (shelves != null) {
                        val shelf = shelves.find { it.name == "Currently Reading" }
                        if (shelf != null) {
                            books.value = shelf.books as MutableList<Book>
                        }
                    }
                }
                onDone()
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "$it",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return books.value
    }
}