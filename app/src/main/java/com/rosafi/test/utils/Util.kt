package com.rosafi.test.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import android.widget.Toast
import com.rosafi.test.data.model.Delivery
import java.util.*

class Util {

    companion object {

        fun getFormattedDeliveryStatus(deliveryStatus: String?) : String? {

            return when {
                deliveryStatus?.toUpperCase(Locale.getDefault()) == Delivery.DeliveryStatuses.DONE_BY_SENDER.toString() -> Delivery.DeliveryStatusesToShow.DONE_BY_CARRIER.toString()
                deliveryStatus?.toUpperCase(Locale.getDefault()) == Delivery.DeliveryStatuses.DONE_BY_RECEIVER.toString() -> Delivery.DeliveryStatusesToShow.DONE_BY_CLIENT.toString()
                else -> deliveryStatus
            }

        }

        fun toastSuccess(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}