package com.nagwa.mediagallery.data.model

import com.google.gson.annotations.SerializedName

data class Media(
@SerializedName("id")
val id: Int,
@SerializedName("type")
val type: String = "",
@SerializedName("url")
val url: String = "",
@SerializedName("name")
val name: String = ""
)