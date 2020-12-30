package com.rosafi.test.ui.deliveries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosafi.test.R
import com.rosafi.test.data.model.Delivery
import com.rosafi.test.databinding.ItemDeliveryLayoutBinding
import com.rosafi.test.ui.deliveries.DeliveriesViewModel

class DeliveriesRecyclerViewAdapter(private val deliveriesList: ArrayList<Delivery>) :
    RecyclerView.Adapter<DeliveriesRecyclerViewAdapter.DeliveriesViewHolder>()  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeliveriesRecyclerViewAdapter.DeliveriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeliveryLayoutBinding.inflate(inflater, parent, false)
        return DeliveriesViewHolder(binding)
    }

    override fun getItemCount(): Int = deliveriesList.size

    override fun onBindViewHolder(holder: DeliveriesRecyclerViewAdapter.DeliveriesViewHolder, position: Int) {

        val item = deliveriesList[position]

        holder.binding.isDone = checkDeliveryStatus(item.status)
        holder.binding.backgroundColor = getDeliveryStatusColor(item.status)
        holder.binding.position = position
        holder.binding.delivery = item
    }

    private fun checkDeliveryStatus(deliveryStatus: String?) : Boolean{
        return deliveryStatus == "done"
    }

    private fun getDeliveryStatusColor(deliveryStatus: String?) : Int{
        return if (deliveryStatus == "done") R.color.doneBgColor else R.color.pendingBgColor
    }

    inner class DeliveriesViewHolder(val binding: ItemDeliveryLayoutBinding) : RecyclerView.ViewHolder(binding.root)


}