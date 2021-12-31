package com.nagwa.mediagallery.data.repo.local

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nagwa.mediagallery.data.model.Media

class MainLocalRepo(
    private val apiHelper: MediaRepoHelper,
    val context: Context,
    val filePath: String
) {

    fun getMediaList(): List<Media> {
        val mediaListJson: String? = apiHelper.getJsonDataFromAsset(context, filePath)
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Media>>() {}.type

        var mlist: List<Media> = gson.fromJson(mediaListJson, listPersonType)
        mlist.forEachIndexed { idx, media -> Log.i("data", "> Item $idx:\n$media") }
        return mlist
    }
}