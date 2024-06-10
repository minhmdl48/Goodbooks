package com.minhmdl.goodbooks.screens.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.model.Review
import com.minhmdl.goodbooks.model.Shelf
import com.minhmdl.goodbooks.model.User
import com.minhmdl.goodbooks.model.progressReading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val application: Application
) : ViewModel()  {

    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    val loading: LiveData<Boolean> = _loading
    private val context = application.applicationContext
    val dataStore: StoreUserName = StoreUserName(context)
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    init {
        viewModelScope.launch {
            dataStore.getName
                .onEach { value: String? ->
                    if (value != null) {
                        _name.value = value
                    }
                }
                .launchIn(this)
        }
    }

    /**
    Creates a new user with the provided email and password.
     */
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onRegisterSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val displayName = name.value
                            createUser(displayName)
                            onRegisterSuccess()
                        }
                        _loading.value = false
                    }
                    .addOnFailureListener { exception ->
                        when (exception) {
                            is FirebaseAuthException -> {
                                if (exception.message?.contains("email address is already in use") == true) {
                                    onError("Email already exists")
                                } else {
                                    onError("Unknown error: ${exception.localizedMessage}")
                                }
                            }
                            is FirebaseNetworkException -> {
                                onError("Network error: ${exception.localizedMessage}")
                            }
                            else -> {
                                onError("Unknown error: ${exception.localizedMessage}")
                            }
                        }
                        _loading.value = false
                    }
            } catch (e: Exception) {
                Log.e("TAG", "createUserWithEmailAndPassword: ${e.message}")
            }
        }
    }
    /**
    createUser is a private function that creates a new user in Firebase Firestore.
     * @param displayName a string representing the display name of the user.
    The function first gets the userId of the current user from the auth object.
    Then, it creates a new MyUser object.
    Finally, it adds the user object to the "users" collection in Firebase Firestore.
     */
    private fun createUser(displayName: String) {
        val userId = Firebase.auth.currentUser?.uid
        val bookList: MutableList<Book> = mutableListOf()
        val shelves: MutableList<Shelf> = mutableListOf(
            Shelf("Currently Reading", mutableListOf()),
            Shelf("Want to Read", mutableListOf()),
            Shelf("Read", mutableListOf())
        )
        val searchHistory: MutableList<String> = mutableListOf()
        val reviews: MutableList<Review> = mutableListOf()
        val favourites: MutableList<Book> = mutableListOf()
        val progressReading: MutableList<progressReading> = mutableListOf()

        if (userId != null) {
            val user = User(
                userID = userId.toString(),
                displayName = displayName,
                avatar = "Image not set",
                shelves = shelves,
                searchHistory = searchHistory,
                reviews = reviews,
                favourites = favourites,
                progressReading = progressReading
            )
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            Log.e("RegisterViewModel","path to doc:  ${userRef.path}")
            userRef.set(user)
        }
    }

}