package com.rosafi.test.ui.deliveries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosafi.test.R
import com.rosafi.test.data.model.Delivery
import com.rosafi.test.data.model.DeliveryStatus
import com.rosafi.test.databinding.ItemDeliveryLayoutBinding
import com.rosafi.test.ui.deliveries.DeliveriesViewModel
import com.rosafi.test.utils.Util
import java.util.*
import kotlin.collections.ArrayList

class DeliveriesRecyclerViewAdapter(private val deliveriesList: ArrayList<Delivery>, private val viewModel: DeliveriesViewModel) :
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
        holder.binding.statusToShow = Util.getFormattedDeliveryStatus(item.status)
        holder.binding.viewModel = this@DeliveriesRecyclerViewAdapter.viewModel
        holder.binding.delivery = item
    }

    private fun checkDeliveryStatus(deliveryStatus: String?): Boolean {
        return deliveryStatus?.toUpperCase(Locale.getDefault()) != Delivery.DeliveryStatuses.DONE_BY_SENDER.toString()
    }

    private fun getDeliveryStatusColor(deliveryStatus: String?) : Int{
        return if (deliveryStatus?.toUpperCase(Locale.getDefault()) == Delivery.DeliveryStatuses.PENDING.toString()) R.color.pendingBgColor else R.color.doneBgColor
    }

    fun updateDeliveryStatus(position: Int){
        deliveriesList[position].status = Delivery.DeliveryStatuses.DONE_BY_SENDER.toString()
        notifyDataSetChanged()
    }

    fun updateList(newDeliveriesList: ArrayList<Delivery>){
        deliveriesList.clear()
        deliveriesList.addAll(newDeliveriesList)
        notifyDataSetChanged()
    }

    inner class DeliveriesViewHolder(val binding: ItemDeliveryLayoutBinding) : RecyclerView.ViewHolder(binding.root)


}