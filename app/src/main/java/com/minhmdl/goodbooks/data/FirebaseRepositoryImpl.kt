package com.minhmdl.goodbooks.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseRepository{
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val collection get() = firestore.collection(USER_COLLECTION)
        .whereEqualTo(USER_ID_FIELD, currentUserId)

    override suspend fun getBooks(shelfName: String): Flow<List<Book>> = callbackFlow {

    }


    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COMPLETED_FIELD = "completed"
        private const val PRIORITY_FIELD = "priority"
        private const val FLAG_FIELD = "flag"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val USER_COLLECTION = "users"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val UPDATE_TASK_TRACE = "updateTask"
    }
}