package com.dev.photogridtask

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.dev.photogridtask.databinding.CustomizeDialogBinding


class CustomDialog(context: Context) : Dialog(context) {
    lateinit var binding: CustomizeDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomizeDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPositive.setOnClickListener { dismiss() }
        binding.btnNegative.setOnClickListener { dismiss() }
    }

    fun setTitle(message: String?) {
        binding.tvDialogTitle.visibility = View.VISIBLE
        binding.tvDialogTitle.text = message
    }

    fun setMessage(message: String?) {
        binding.tvMessage.text = message
    }

    override fun setTitle(string: Int) {
        binding.tvDialogTitle.visibility = View.VISIBLE
        binding.tvDialogTitle.setText(string)
    }

    fun setMessage(string: Int) {
        binding.tvMessage.setText(string)
    }

    fun setPositiveButton(text: String?) {
        binding.btnPositive.text = text
        binding.btnPositive.visibility = View.VISIBLE
    }

    fun setPositiveButton(string: Int) {
        binding.btnPositive.setText(string)
        binding.btnPositive.visibility = View.VISIBLE
    }

    fun setNeutralButton(string: Int) {
        binding.btnNeutral.setText(string)
        binding.btnNeutral.visibility = View.VISIBLE
    }

    fun setNeutralButton(text: String, clickListener: View.OnClickListener) {
        binding.btnNeutral.text = text
        binding.btnNeutral.visibility = View.VISIBLE
        binding.btnNeutral.setOnClickListener(clickListener)
    }

    fun setPositiveButton(text: String, clickListener: View.OnClickListener) {
        binding.btnPositive.text = text
        binding.btnPositive.visibility = View.VISIBLE
        binding.btnPositive.setOnClickListener(clickListener)
    }

    fun setPositiveButton(string: Int, clickListener: View.OnClickListener) {
        binding.btnPositive.setText(string)
        binding.btnPositive.visibility = View.VISIBLE
        binding.btnPositive.setOnClickListener(clickListener)
    }

    fun setNegativeButton(text: String?) {
        binding.btnNegative.text = text
        binding.btnNegative.visibility = View.VISIBLE
    }

    fun setNegativeButton(text: Int) {
        binding.btnNegative.setText(text)
        binding.btnNegative.visibility = View.VISIBLE
    }

    fun setNegativeButton(text: String?, clickListener: View.OnClickListener) {
        binding.btnNegative.text = text
        binding.btnNegative.visibility = View.VISIBLE
        binding.btnNegative.setOnClickListener(clickListener)
    }

    fun setNegativeButton(string: Int, clickListener: View.OnClickListener) {
        binding.btnNegative.setText(string)
        binding.btnNegative.visibility = View.VISIBLE
        binding.btnNegative.setOnClickListener(clickListener)
    }
}