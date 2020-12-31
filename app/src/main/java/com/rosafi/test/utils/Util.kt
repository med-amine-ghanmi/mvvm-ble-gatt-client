package com.rosafi.test.utils

import com.rosafi.test.data.model.Delivery
import java.util.*

class Util {

    companion object {

        fun getFormattedDeliveryStatus(deliveryStatus: String?) : String? {

            return if (deliveryStatus?.toLowerCase(Locale.getDefault()) == Delivery.DeliveryStatuses.DONE_BY_SENDER.toString())
                Delivery.DeliveryStatusesToShow.DONE_BY_CARRIER.toString()
            else if (deliveryStatus == Delivery.DeliveryStatuses.DONE_BY_RECEIVER.toString())
                Delivery.DeliveryStatusesToShow.DONE_BY_CLIENT.toString()
            else deliveryStatus

        }


    }

}