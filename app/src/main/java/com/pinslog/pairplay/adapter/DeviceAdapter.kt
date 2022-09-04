package com.pinslog.pairplay.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinslog.pairplay.databinding.ItemDeviceBinding

const val TYPE_NON_PAIRED = 0
const val TYPE_PAIRED = 1

@SuppressLint("NotifyDataSetChanged")
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
        notifyDataSetChanged()
    }

    fun addItems(devices: MutableSet<BluetoothDevice>) {
        deviceSet.addAll(devices)
        notifyDataSetChanged()
    }

    fun clearItem(device: BluetoothDevice){
        if (deviceSet.contains(device)){
            deviceSet.remove(device)
            notifyDataSetChanged()
        }
    }

    fun clearAll() {
        if (deviceSet.isNotEmpty()){
            deviceSet.clear()
            notifyDataSetChanged()
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