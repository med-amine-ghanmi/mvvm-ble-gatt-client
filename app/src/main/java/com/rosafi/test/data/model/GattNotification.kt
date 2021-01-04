package com.rosafi.test.data.model


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import com.google.gson.annotations.SerializedName

data class GattNotification(
        var bluetoothDevice: BluetoothDevice,
        var characteristic: BluetoothGattCharacteristic,
        var confirm: Boolean

){


}
