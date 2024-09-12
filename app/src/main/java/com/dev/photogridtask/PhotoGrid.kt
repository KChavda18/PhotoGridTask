package com.dev.photogridtask

import java.io.Serializable

data class PhotoGrid(
    val albumId: Int = 0,
    val id: Int = 0,
    val title: String = "",
    val url: String? = "",
    val thumbnailUrl: String? = ""
) : Serializable
