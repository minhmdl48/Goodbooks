package com.minhmdl.goodbooks.screens.login

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
import com.google.firebase.ktx.Firebase
import com.minhmdl.goodbooks.data.StoreUserName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application
) : ViewModel()  {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val context = application.applicationContext
    private val dataStore = StoreUserName(context)
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

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onLoginSuccess()
                        }
                        _loading.value = false
                    }
                    .addOnFailureListener { exception ->
                        when (exception) {
                            is FirebaseAuthException -> {
                                when (exception.errorCode) {
                                    "ERROR_USER_NOT_FOUND" -> {
                                        onError("Email does not exist")
                                    }
                                    "ERROR_WRONG_PASSWORD" -> {
                                        onError("Incorrect email or password")
                                    }
                                    else -> {
                                        onError("Unknown error: ${exception.localizedMessage}")
                                    }
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
                Log.e("TAG", "signInWithEmailAndPassword: ${e.message}")
            }
        }
    }
}