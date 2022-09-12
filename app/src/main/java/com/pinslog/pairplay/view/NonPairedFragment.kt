package com.pinslog.pairplay.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pinslog.pairplay.adapter.DeviceAdapter
import com.pinslog.pairplay.adapter.TYPE_NON_PAIRED
import com.pinslog.pairplay.base.BaseFragment
import com.pinslog.pairplay.databinding.FragmentNonPairedBinding

@SuppressLint("MissingPermission")
class NonPairedFragment : BaseFragment<FragmentNonPairedBinding>() {

    private lateinit var deviceAdapter: DeviceAdapter

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNonPairedBinding {
        binding = FragmentNonPairedBinding.inflate(inflater, container, false)
        return binding
    }


    override fun initSetting() {
        super.initSetting()
        registerBluetoothReceiver()
        deviceAdapter = DeviceAdapter(TYPE_NON_PAIRED)
        binding.nonPairedListRv.adapter = deviceAdapter
    }

    override fun initListener() {
        binding.nonPairedFindDeviceBtn.setOnClickListener {
            val text = binding.nonPairedFindDeviceBtn.text
            if (text.equals("기기 검색")){
                deviceAdapter.clearAll()

                if (bluetoothAdapter.isDiscovering) {
                    bluetoothAdapter.cancelDiscovery()
                }
                bluetoothAdapter.startDiscovery()
                binding.nonPairedFindDeviceBtn.text = "검색 중지"
            } else if (text.equals("검색 중지")) {
                if (bluetoothAdapter.isDiscovering) {
                    bluetoothAdapter.cancelDiscovery()
                }
                binding.nonPairedFindDeviceBtn.text = "기기 검색"
            }

        }
    }


    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1?.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // 기기 찾으면
                    val device: BluetoothDevice? =
                        p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    pairedDevices = bluetoothAdapter.bondedDevices
                    if (!pairedDevices.contains(device) && device != null) {
                        if (device.name != null) {
                            deviceAdapter.addItem(device)
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    // 검색중입니다..
                    binding.nonPairedProgress.visibility = View.VISIBLE
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    // 검색이 종료되었습니다
                    binding.nonPairedProgress.visibility = View.INVISIBLE
                    binding.nonPairedFindDeviceBtn.text = "기기 검색"
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {

                    val device: BluetoothDevice? =
                        p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val bondState = p1.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    if (bondState == BluetoothDevice.BOND_BONDED){
                        LoadingFragment.hideLoading()
                        if (device != null) {
                            deviceAdapter.clearItem(device)
                            Toast.makeText(mContext, "페어링 되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                    } else if (bondState == BluetoothDevice.BOND_BONDING){
                        LoadingFragment.showLoading(requireActivity())
                    } else if (bondState == BluetoothDevice.BOND_NONE){
                        LoadingFragment.hideLoading()
                    }

                }
            }
        }
    }

    private fun registerBluetoothReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        mContext.registerReceiver(bluetoothReceiver, intentFilter)
    }



}