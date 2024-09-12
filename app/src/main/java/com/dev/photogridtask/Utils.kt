package com.dev.photogridtask

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import java.io.Serializable


object Utils {

    fun <T : Serializable?> getSerializable(intent: Intent, name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getSerializableExtra(
            name, clazz
        )!!
        else intent.getSerializableExtra(name) as T
    }

    fun internetAlert(activity: Context) {
        AlertDialog.Builder(activity)
            .setMessage("Please check internet connection.")
            .setPositiveButton("ok") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    fun isOnline(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices)
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        return false
    }

    fun showError(activity: Context, error: String) {
        try {
            val errorMessage = JSONObject(error)

            // Check for the known keys and display the appropriate message
            when {
                errorMessage.has("message") -> {
                    showAlert(activity, errorMessage.getString("message"))

                }

                errorMessage.has("error") -> {
                    val errorObject = errorMessage.get("error")
                    if (errorObject is JSONObject) {
                        val keys = errorObject.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            val jsonArray = errorObject.getJSONArray(key)
                            if (jsonArray.length() > 0) {
                                showAlert(activity, jsonArray.getString(0))
                                return
                            }
                        }
                    } else {
                        showAlert(activity, errorMessage.getString("error"))
                    }
                }

                errorMessage.has("errors") -> {
                    showAlert(activity, errorMessage.getString("errors"))
                }

                else -> {
                    val keys = errorMessage.keys()
                    while (keys.hasNext()) {
                        val dynamicKey = keys.next().toString()
                        try {
                            val jsonArray = errorMessage.getJSONArray(dynamicKey)
                            if (jsonArray.length() > 0) {
                                showAlert(activity, jsonArray.getString(0))
                                return
                            }
                        } catch (e: Exception) {
                            showAlert(activity, errorMessage.toString())
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showAlert(activity, error)
        }
    }


    fun showAlert(activity: Context, message: String) {
        val alert = CustomDialog(activity)
        alert.setCancelable(true)
        alert.show()
        alert.setMessage(message)
        alert.setPositiveButton("ok") {
            alert.dismiss()
        }
    }
}