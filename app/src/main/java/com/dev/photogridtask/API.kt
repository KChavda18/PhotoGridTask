package com.dev.photogridtask

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by arthtilva on 07-Nov-16.
 */


interface API {

    @GET("photos")
    fun getPhotos(): Call<ResponseBody>
}