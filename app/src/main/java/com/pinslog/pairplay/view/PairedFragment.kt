package com.pinslog.pairplay.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    /**
     * 페어링된 블루투스 디바이스 목록을 가져옵니다
     */
    private fun getPairedDevices(){
        pairedDevices = bluetoothAdapter.bondedDevices
        deviceAdapter.addItems(pairedDevices as MutableSet<BluetoothDevice>)
    }

    /**
     * 블루투스 리시버를 등록합니다.
     */
    private fun registerBluetoothReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        mContext.registerReceiver(bluetoothReceiver, intentFilter)
    }

    /**
     * 블루투스 리시버
     */
    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1?.action
            when (action) {
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {

                    val device: BluetoothDevice? =
                        p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val bondState = p1.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    if (bondState == BluetoothDevice.BOND_NONE) {
                        if (device != null) {
                            val content = "${device.name} 등록 해제"
                            setBluetoothAfter(device, deviceAdapter, content)
                        }
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerBluetoothReceiver()
    }

    override fun onPause() {
        super.onPause()
        mContext.unregisterReceiver(bluetoothReceiver)
    }


}