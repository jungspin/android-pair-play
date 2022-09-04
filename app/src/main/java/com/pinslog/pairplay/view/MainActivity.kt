package com.pinslog.pairplay.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.pinslog.pairplay.adapter.ViewPagerAdapter
import com.pinslog.pairplay.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val mContext: Context = this
    private lateinit var binding: ActivityMainBinding
    private val tabTitleArray = arrayOf("페어링 가능 목록", "페어링 된 기기")



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.mainViewPager
        val tabLayout = binding.mainTabLayout
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager,  lifecycle)

        // tabLayout 과 viewPager2 연동을 도와주는 객체
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

    }



}