package com.rosafi.test.utils

class Util {

    companion object {

        fun getFormattedDeliveryStatus(deliveryStatus: String?) : String? {

            return if(deliveryStatus == "done_by_sender") "Done by carrier" else if (deliveryStatus == "done_by_receiver") "Done by client" else deliveryStatus


        }


    }

}