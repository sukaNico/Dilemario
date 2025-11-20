package com.example.dilemario.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dilemario.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val prefs: UserPreferences) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country

    init {
        viewModelScope.launch {
            prefs.userName.collect { _name.value = it ?: "" }
        }
        viewModelScope.launch {
            prefs.userEmail.collect { _email.value = it ?: "" }
        }
        viewModelScope.launch {
            prefs.userAge.collect { _age.value = it ?: "" }
        }
        viewModelScope.launch {
            prefs.userCountry.collect { _country.value = it ?: "" }
        }
    }
}

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val prefs = UserPreferences(context)
        return ProfileViewModel(prefs) as T
    }
}
