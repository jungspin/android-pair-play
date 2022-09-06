package com.pinslog.pairplay.util

import android.bluetooth.BluetoothDevice
import androidx.recyclerview.widget.DiffUtil


class DiffUtilCallback(private val oldList: MutableSet<BluetoothDevice>, private val newList: MutableSet<BluetoothDevice>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.elementAt(oldItemPosition)
        val newItem = newList.elementAt(newItemPosition)

        return if (oldItem is BluetoothDevice && newItem is BluetoothDevice){
            oldItem.address == newItem.address
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.elementAt(oldItemPosition) == newList.elementAt(newItemPosition)
    }
}