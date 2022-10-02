package com.pinslog.pairplay.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.pinslog.pairplay.R
import com.pinslog.pairplay.databinding.LayoutLoadingBinding

object LoadingFragment : DialogFragment() {

    private lateinit var alertDialog: AlertDialog

    /**
     * 로딩 프래그먼트를 띄웁니다
     */
    fun showLoading(mContext: Context) {

        val layoutInflater = LayoutInflater.from(mContext)
        val binding = LayoutLoadingBinding.inflate(layoutInflater)
        val alertDialogBuilder = mContext.let { AlertDialog.Builder(it) }
        alertDialogBuilder.setView(binding.root)

        alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        alertDialog.show()
    }

    /**
     * 로딩 프래그먼트를 닫습니다.
     */
    fun hideLoading() {
        if (alertDialog != null){
            alertDialog.dismiss()
        }
    }


}