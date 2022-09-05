package com.pinslog.pairplay.base

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@SuppressLint("MissingPermission")
abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected lateinit var binding: VB
    protected lateinit var mContext: Context
    protected lateinit var inflateView: ViewGroup
    private var isAllowed = true
    protected lateinit var bluetoothAdapter: BluetoothAdapter
    protected lateinit var pairedDevices: Set<BluetoothDevice>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding(inflater, container)
        inflateView = binding.root as ViewGroup

        checkPermission()
        if (isAllowed) {
            settingBluetooth()
        }
        initSetting()
        initListener()
        return inflateView
    }


    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun initSetting() {}
    protected open fun initListener() {}


    private fun settingBluetooth() {
        val manager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = manager.adapter

        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(mContext, "블루투스가 비활성화 상태입니다", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkPermission() {
        val requiredPermissionS = arrayOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT)
        val requiredPermissionQ =
            arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
        val rejectPermissionList = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissionLauncher.launch(requiredPermissionS)

            for (permission in requiredPermissionS) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    isAllowed = false
                    rejectPermissionList.add(permission)
                }
            }

            if (rejectPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectPermissionList.size)
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    rejectPermissionList.toArray(array),
                    1000
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissionLauncher.launch(requiredPermissionQ)
            for (permission in requiredPermissionQ) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    isAllowed = false
                    rejectPermissionList.add(permission)
                }
            }

            if (rejectPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectPermissionList.size)
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    rejectPermissionList.toArray(array),
                    1000
                )
            }

        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            for (result in results.values) {
                isAllowed = result
            }
            if (isAllowed) {
                settingBluetooth()
            } else {
                showWarning()
            }
        }

    private fun showWarning() {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        alertDialogBuilder.setMessage("권한을 허용하지 않을 경우\n해당 앱을 사용할 수 없습니다.")
            .setPositiveButton("허용하러 가기") { dialog, id ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext.startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("허용 안함") { dialog, id ->
                requireActivity().finish()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



}