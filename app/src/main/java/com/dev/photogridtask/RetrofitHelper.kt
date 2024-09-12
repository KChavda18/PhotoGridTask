package com.dev.photogridtask

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ParseException
import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
Created by Arth on 13-Jul-17.
 */

class RetrofitHelper {

    private var gsonAPI: API
    private var connectionCallBack: ConnectionCallBack? = null

    constructor(context: Context) {
        val httpClient = UnsafeOkHttpClient.unsafeOkHttpClient.newBuilder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().header(
                "Accept", "application/json"
            )
                .method(original.method(), original.body())

            chain.proceed(request.build())
        }

        val TIMEOUT = 2 * 60 * 1000
        val gsonretrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(
                httpClient.connectTimeout(
                    TIMEOUT.toLong(),
                    TimeUnit.SECONDS
                ).readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS).writeTimeout(
                    TIMEOUT.toLong(),
                    TimeUnit.SECONDS
                ).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gsonAPI = gsonretrofit.create(API::class.java)


    }

    fun api(): API {
        return gsonAPI
    }


    fun callApi(
        activity: Context,
        call: Call<ResponseBody>,
        callBack: ConnectionCallBack,
    ) {
        if (!Utils.isOnline(activity)) {
            Utils.internetAlert(activity)
            return
        }
        connectionCallBack = callBack
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i("TAG", "onResponse URL: ${call.request().url()}")
                Log.i("TAG", "onResponse CODE: ${response.code()}")
                if (response.code() == 200 || response.code() == 201) {
                    if (connectionCallBack != null)
                        connectionCallBack!!.onSuccess(response)
                } else {
                    val res = response.errorBody()!!.string()

                    Log.d("TAG", "errorBody res  " + res)
                    Log.i("TAG", "onResponse: ${response.code()}")
//              todo error format {"status":false,"data":[],"error":{"device_token":["The device token field is required."]}} 422
                    if (response.code() == 401 || response.code() == 400) {
                        val alert = CustomDialog(activity)
                        alert.setCancelable(false)
                        alert.show()
                        alert.setMessage(
                            JSONObject(
                                res
                            ).getString("message")
                        )
                        alert.setPositiveButton("ok") {
                            alert.dismiss()
                        }


                    } else {
                        try {
                            if (connectionCallBack != null)
                                connectionCallBack!!.onError(response.code(), res.toString())
                        } catch (e: IOException) {
                            Log.i("TAG", "onResponse: " + call.request().url())
                            e.printStackTrace()
                            if (connectionCallBack != null)
                                connectionCallBack!!.onError(response.code(), e.message)
                        } catch (e: NullPointerException) {
                            Log.i("TAG", "onResponse: " + call.request().url())
                            e.printStackTrace()
                            if (connectionCallBack != null)
                                connectionCallBack!!.onError(response.code(), e.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
                var message: String? = null
                if (error is NetworkErrorException) {
                    message = "Please check your internet connection"
                } else if (error is ParseException) {
                    message = "Parsing error! Please try again after some time!!"
                } else if (error is TimeoutException) {
                    message = "Connection TimeOut! Please check your internet connection."
                } else if (error is UnknownHostException) {
                    message = "Please check your internet connection and try later"
                } else if (error is Exception) {
                    message = error.message
                }
                if (connectionCallBack != null)
                    connectionCallBack!!.onError(-1, "{\"message\":\"$message\"}")
            }
        })
    }

    interface ConnectionCallBack {
        fun onSuccess(body: Response<ResponseBody>)

        fun onError(code: Int, error: String?)
    }

    companion object {

        fun createPartFromString(descriptionString: String): RequestBody {
            return RequestBody.create(
                MultipartBody.FORM, descriptionString
            )
        }

        fun createJsonFromString(descriptionString: JSONObject): RequestBody {
            return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                descriptionString.toString()
            )
        }

        fun prepareFilePart(partName: String, filePath: String): MultipartBody.Part {
            val requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                File(filePath)
            )
            return MultipartBody.Part.createFormData(partName, File(filePath).name, requestFile)
        }
    }
}

/******************USAGE**********************
 *
private fun getSectionList() {
showProgress()
val retrofitHelper = RetrofitHelper()
var call: Call<ResponseBody> =
retrofitHelper.api().get_section_by_class(
storeUserData.getString(Constants.USER_LANGUAGE_ID),
storeUserData.getString(Constants.USER_SCHOOL_ID),
intent.getStringExtra("classId")
)
retrofitHelper.callApi(activity, call, object : RetrofitHelper.ConnectionCallBack {
override fun onSuccess(body: Response<ResponseBody>) {
dismissProgress()
if (body.code() != 200) {
return
}
val responseString = body.body()!!.string()
Log.i("TAG", responseString)
if (JSONObject(responseString).getString("code") == "LOGOUT") {
startActivity(
Intent(activity, LoginActivity::class.java)
.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
)
return
}
var dashboard = Gson().fromJson(responseString, Dashboard::class.java)
if (dashboard.success == 1 && !dashboard.data.all_section_list.isNullOrEmpty()) {
var pojo = ClassPojo()
pojo.section_id = "0"
pojo.class_id = "0"
pojo.name = "ALL"
dashboard.data.all_section_list.add(0, pojo)
searchList.addAll(dashboard.data.all_section_list)
allClass.addAll(dashboard.data.all_section_list)
adapter.notifyDataSetChanged()
} else {
showAlert(dashboard.message)
}
}

override fun onError(code: Int, error: String?) {
dismissProgress()
Log.i("error", error)
}
})
}
 */