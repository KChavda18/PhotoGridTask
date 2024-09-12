package com.dev.photogridtask

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import org.json.JSONObject


open class BaseActivity : AppCompatActivity() {
    lateinit var activity: AppCompatActivity
    lateinit var progressDialog: CustomProgressDialog
    private lateinit var alert: CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val config: Configuration = this.resources.configuration
        val fontWeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment
        } else {

        }
        Log.i("TAG", "fontWeight: $fontWeight")

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            e.printStackTrace()
        }
    }


    fun showAlert(message: String) {
        if (::alert.isInitialized && this::alert.isInitialized) {
            alert.dismiss()
        }
        alert = CustomDialog(activity)
        alert.setCancelable(true)
        alert.show()
        alert.setMessage(message)
        alert.setPositiveButton("ok") {
            alert.dismiss()
        }
    }

    fun showAlert(message: String, finish: Boolean) {
        if (::alert.isInitialized && this::alert.isInitialized) {
            alert.dismiss()
        }
        alert = CustomDialog(activity)
        if (finish)
            alert.setCancelable(false)
        else
            alert.setCancelable(true)
        alert.show()
        alert.setMessage(message)
        alert.setPositiveButton("ok") {
            alert.dismiss()
            if (finish) {
                finish()
            }
        }
    }

    fun showAlert(message: String, finish: Boolean, java: Class<*>) {
        if (::alert.isInitialized && this::alert.isInitialized) {
            alert.dismiss()
        }
        alert = CustomDialog(activity)
        alert.setCancelable(false)
        alert.show()
        alert.setMessage(message)
        alert.setPositiveButton("ok") {
            alert.dismiss()
            if (finish) {
                startActivity(Intent(activity, java))
            }
        }
    }


    fun showAlert(title: String, message: String) {
        if (::alert.isInitialized && this::alert.isInitialized) {
            alert.dismiss()
        }
        alert = CustomDialog(activity)
        alert.setCancelable(true)
        alert.show()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("ok") {
            alert.dismiss()
        }
    }

    fun showAlert(message: String, positiveCallback: View.OnClickListener) {
        if (::alert.isInitialized && this::alert.isInitialized) {
            alert.dismiss()
        }
        alert = CustomDialog(activity)
        alert.setCancelable(true)
        alert.show()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("ok") {
            alert.dismiss()
            positiveCallback.onClick(it)
        }
    }

    fun showProgress() {
        progressDialog = CustomProgressDialog(activity)
        progressDialog.setCancelable(false)
        if (!activity.isFinishing) {
            progressDialog.show()
        }
    }

    fun dismissProgress() {
        try {
            if (progressDialog != null && this::progressDialog.isInitialized) {
                if (progressDialog.isShowing) {

                    val context = (progressDialog.context as ContextWrapper).baseContext

                    if (context is Activity) {
                        if (!context.isFinishing && !context.isDestroyed) progressDialog.dismiss()
                    } else
                        progressDialog.dismiss()
                }
            }
        } catch (e: java.lang.Exception) {
            Log.i("TAG", "dismissProgress: ${e.message}")
        }
    }

    fun showProgress(message: String) {
        progressDialog = CustomProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setTitle(message)
        progressDialog.show()
    }

    open fun fromHtml(source: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
    }

    fun showError(error: String) {
        try {
            showAlert(JSONObject(error).getString("message"))
        } catch (e: Exception) {
            showAlert(error)
        }
    }
}