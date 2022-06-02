package com.zaniva.storyappv2.story.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaniva.storyappv2.connection.SessionManager

class HomeVMFactory(private val pref: SessionManager): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown VM Class: " + modelClass.name)
    }
}