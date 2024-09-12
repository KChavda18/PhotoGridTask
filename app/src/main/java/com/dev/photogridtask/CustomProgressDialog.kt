package com.dev.photogridtask

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.dev.photogridtask.databinding.CustomProgressDialogBinding

class CustomProgressDialog(context: Context) : Dialog(context) {
    lateinit var binding: CustomProgressDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomProgressDialogBinding.inflate(layoutInflater)
        setContentView(R.layout.custom_progress_dialog)
    }

    fun setTitle(message: String?) {
        binding.tvProgressTitle.visibility = View.VISIBLE
        binding.tvProgressTitle.text = message
    }

    override fun setTitle(string: Int) {
        binding.tvProgressTitle.visibility = View.VISIBLE
        binding.tvProgressTitle.setText(string)
    }
}