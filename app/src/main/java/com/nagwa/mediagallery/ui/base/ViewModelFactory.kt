package com.nagwa.mediagallery.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nagwa.mediagallery.data.repo.local.MainLocalRepo
import com.nagwa.mediagallery.data.repo.local.MediaRepoHelper
import com.nagwa.mediagallery.ui.main.viewmodel.MainViewModel

class ViewModelFactory (private val apiHelper: MediaRepoHelper, val context: Context,
                        val filePath: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainLocalRepo(apiHelper, context, filePath)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}