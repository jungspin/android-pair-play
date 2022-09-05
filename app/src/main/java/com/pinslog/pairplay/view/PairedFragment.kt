package com.pinslog.pairplay.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pinslog.pairplay.adapter.DeviceAdapter
import com.pinslog.pairplay.adapter.TYPE_PAIRED
import com.pinslog.pairplay.base.BaseFragment
import com.pinslog.pairplay.databinding.FragmentPairedBinding

@SuppressLint("MissingPermission")
class PairedFragment : BaseFragment<FragmentPairedBinding>() {

    private lateinit var deviceAdapter: DeviceAdapter

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPairedBinding {
        binding = FragmentPairedBinding.inflate(inflater, container, false)
        return binding
    }

    private fun getPairedDevices(){
        pairedDevices = bluetoothAdapter.bondedDevices
        deviceAdapter.addItems(pairedDevices as MutableSet<BluetoothDevice>)
    }


    override fun initSetting() {
        super.initSetting()
        deviceAdapter = DeviceAdapter(TYPE_PAIRED)
        binding.pairedListRv.adapter = deviceAdapter
        getPairedDevices()
    }

    override fun initListener() {
        binding.pairedSwipeLy.setOnRefreshListener {
            deviceAdapter.clearAll()
            getPairedDevices()
            binding.pairedSwipeLy.isRefreshing = false
        }
    }


}