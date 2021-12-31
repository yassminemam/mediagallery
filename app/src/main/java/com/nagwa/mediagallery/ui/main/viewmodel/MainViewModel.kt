package com.nagwa.mediagallery.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nagwa.mediagallery.data.model.Media
import com.nagwa.mediagallery.data.repo.local.MainLocalRepo
import com.nagwa.mediagallery.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MainViewModel(private val mainRepository: MainLocalRepo) : ViewModel() {

    private val mediaList = MutableLiveData<Resource<List<Media>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchMediaList()
    }

    fun fetchMediaList() {
        mediaList.postValue(Resource.loading(null))
        try {
            val list = mainRepository.getMediaList()
            mediaList.postValue(Resource.success(list))
        }
        catch (e:Exception)
        {
            mediaList.postValue(Resource.error("Something Went Wrong", null))
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
    fun getList(): LiveData<Resource<List<Media>>> {
        return mediaList
    }
}
