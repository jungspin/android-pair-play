package com.pinslog.pairplay.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pinslog.pairplay.R
import com.pinslog.pairplay.adapter.DeviceAdapter
import com.pinslog.pairplay.adapter.TYPE_NON_PAIRED
import com.pinslog.pairplay.base.BaseFragment
import com.pinslog.pairplay.databinding.FragmentNonPairedBinding

const val BLUETOOTH_SCAN_TEXT = "기기 검색"
const val BLUETOOTH_SCAN_STOP_TEXT = "검색 중지"

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

        deviceAdapter = DeviceAdapter(TYPE_NON_PAIRED)
        binding.nonPairedListRv.adapter = deviceAdapter
    }

    override fun initListener() {
        binding.nonPairedFindDeviceBtn.setOnClickListener(findDeviceListener)
    }

    /**
     * 기기검색버튼 클릭리스너
     */
    private val findDeviceListener = View.OnClickListener {
        if (!bluetoothAdapter.isEnabled) {
            val alertDialogBuilder = AlertDialog.Builder(mContext)
            alertDialogBuilder.setMessage(getText(R.string.non_bluetooth_disable_text))
                .setPositiveButton(getText(R.string.non_bluetooth_make_enable)) { dialog, id ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_BLUETOOTH_SETTINGS
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    mContext.startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton(getText(R.string.non_finish_app)) { dialog, id ->
                    requireActivity().finish()
                }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            val text = binding.nonPairedFindDeviceBtn.text
            if (text.equals(BLUETOOTH_SCAN_TEXT)) {
                deviceAdapter.clearAll()

                if (bluetoothAdapter.isDiscovering) {
                    bluetoothAdapter.cancelDiscovery()
                }
                if (bluetoothAdapter.startDiscovery()) {
                    binding.nonPairedFindDeviceBtn.text = BLUETOOTH_SCAN_STOP_TEXT
                } else {
                    Toast.makeText(mContext, getText(R.string.non_bluetooth_cant_scan), Toast.LENGTH_SHORT).show()
                }


            } else if (text.equals(BLUETOOTH_SCAN_STOP_TEXT)) {
                if (bluetoothAdapter.isDiscovering) {
                    bluetoothAdapter.cancelDiscovery()
                }
                binding.nonPairedFindDeviceBtn.text = BLUETOOTH_SCAN_TEXT
            }

        }
    }


    /**
     * 블루투스 리시버를 등록합니다.
     */
    private fun registerBluetoothReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        mContext.registerReceiver(bluetoothReceiver, intentFilter)
    }

    /**
     * 블루투스 리시버
     */
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
                    binding.nonPairedFindDeviceBtn.text = BLUETOOTH_SCAN_TEXT
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {

                    val device: BluetoothDevice? =
                        p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val bondState = p1.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    if (bondState == BluetoothDevice.BOND_BONDED) {
                        LoadingFragment.hideLoading()
                        if (device != null) {
                            deviceAdapter.clearItem(device)
                            Toast.makeText(mContext, getString(R.string.non_bluetooth_paired), Toast.LENGTH_SHORT).show()
                        }
                    } else if (bondState == BluetoothDevice.BOND_BONDING) {
                        LoadingFragment.showLoading(requireActivity())
                    } else if (bondState == BluetoothDevice.BOND_NONE) {
                        LoadingFragment.hideLoading()
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