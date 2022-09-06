package com.pinslog.pairplay.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.pinslog.pairplay.databinding.LayoutLoadingBinding

object LoadingFragmentClass{
    private val loadingFragment = LoadingFragment()

    fun showLoading(mActivity : FragmentActivity){
        loadingFragment.show(mActivity.supportFragmentManager, "")
    }

    fun hideLoading(){
        loadingFragment.dismiss()
    }
}
class LoadingFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LayoutLoadingBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        return binding.root
    }



}