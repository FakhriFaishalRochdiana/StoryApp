package com.zaniva.storyappv2.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaniva.storyappv2.connection.SessionManager

class LoginVMFactory(private val pref: SessionManager): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVm::class.java)){
            return LoginVm(pref) as T
        }
        throw IllegalArgumentException("Unknown VM Class: " + modelClass.name)
    }
}