package com.pinslog.pairplay.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pinslog.pairplay.databinding.LayoutLoadingBinding

object LoadingFragment : DialogFragment() {

    private var alertDialog: AlertDialog? = null

    /**
     * 로딩 프래그먼트를 띄웁니다
     */
    fun showLoading(mContext: Context) {
        hideLoading()

        val layoutInflater = LayoutInflater.from(mContext)
        val binding = LayoutLoadingBinding.inflate(layoutInflater)
        val alertDialogBuilder = mContext.let { AlertDialog.Builder(it) }
        alertDialogBuilder.setView(binding.root)

        alertDialog = alertDialogBuilder.create()
        alertDialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            it.show()
        }
    }

    /**
     * 로딩 프래그먼트를 닫습니다.
     */
    fun hideLoading() {
        alertDialog?.dismiss()
        alertDialog = null
    }


}