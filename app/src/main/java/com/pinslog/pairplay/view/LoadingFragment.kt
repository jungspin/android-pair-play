package com.pinslog.pairplay.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.pinslog.pairplay.R
import com.pinslog.pairplay.databinding.LayoutLoadingBinding

object LoadingFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LayoutLoadingBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        return binding.root
    }

    fun showLoading(mActivity : FragmentActivity){
        show(mActivity.supportFragmentManager, "")
    }

    fun hideLoading(){
        dismiss()
    }


}