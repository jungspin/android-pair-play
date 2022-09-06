package com.pinslog.pairplay.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pinslog.pairplay.databinding.ItemDeviceBinding
import com.pinslog.pairplay.util.DiffUtilCallback

const val TYPE_NON_PAIRED = 0
const val TYPE_PAIRED = 1

class DeviceAdapter(private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var deviceSet = mutableSetOf<BluetoothDevice>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDeviceBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).onBind(type, deviceSet.elementAt(position))
    }

    override fun getItemCount(): Int {
        return deviceSet.size
    }


    fun addItem(device: BluetoothDevice) {
        deviceSet.add(device)
        notifyItemInserted(deviceSet.size)
    }

    fun addItems(devices: MutableSet<BluetoothDevice>) {
        devices.let {
            val diffCallback = DiffUtilCallback(devices, deviceSet)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            deviceSet.run {
                clear()
                addAll(devices)
                diffResult.dispatchUpdatesTo(this@DeviceAdapter)
            }
        }
    }

    fun clearItem(device: BluetoothDevice){
        if (deviceSet.contains(device)){
            val index = deviceSet.indexOf(device)
            deviceSet.remove(device)
            notifyItemRemoved(index)
        }
    }

    fun clearAll() {
        if (deviceSet.isNotEmpty()){
            deviceSet.clear()
            notifyItemRangeRemoved(0, deviceSet.size)
        }
    }


    @SuppressLint("MissingPermission")
    inner class ItemViewHolder(private val binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var device: BluetoothDevice


        init {
            binding.itemConnectBtn.setOnClickListener {
                try {
                    device.createBond()
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }


        @SuppressLint("MissingPermission")
        fun onBind(type: Int, device: BluetoothDevice) {
            this.device = device
            binding.itemAddressTv.text = device.address
            binding.itemNameTv.text = device.name
            when (type) {
                TYPE_NON_PAIRED -> {
                    binding.itemConnectBtn.visibility = View.VISIBLE
                }
                TYPE_PAIRED -> {
                    binding.itemConnectBtn.visibility = View.INVISIBLE
                    binding.itemConnectBtn.visibility = View.GONE
                }
            }
        }
    }
}