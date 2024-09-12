package com.dev.photogridtask

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.photogridtask.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val photoList = ArrayList<PhotoGrid>()
    private val photoListAdapter: PhotoListAdapter by lazy {
        PhotoListAdapter(photoList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = this

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupGridList()
    }

    private fun setupGridList() {
        binding.photoGrid.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = photoListAdapter
        }

        getAPIData()
    }

    private fun getAPIData() {
        val retrofitHelper = RetrofitHelper(activity)
        val call: Call<ResponseBody> =
            retrofitHelper.api().getPhotos()
        retrofitHelper.callApi(activity, call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody>) {
                dismissProgress()
                if (body.code() != 200) {
                    return
                }
                val responseString = body.body()!!.string()
                Log.i("TAG", responseString)

                val listData: ArrayList<PhotoGrid> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ArrayList<PhotoGrid>>() {}.type
                )

                if (listData.isEmpty()) {
                    showAlert("No Data Found.")
                } else {
                    photoList.clear()
                    photoList.addAll(listData)

                    photoListAdapter.notifyItemRangeInserted(0, listData.size)
                }
            }

            override fun onError(code: Int, error: String?) {
                dismissProgress()
                if (error != null) {
                    Log.i("error", error)
                }
            }
        })
    }
}