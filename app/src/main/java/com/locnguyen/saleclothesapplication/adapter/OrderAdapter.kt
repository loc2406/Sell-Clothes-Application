package com.locnguyen.saleclothesapplication.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.ItemOrderBinding
import com.locnguyen.saleclothesapplication.model.Order
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(private var list: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderVH>(){

    class OrderVH(val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderVH {
        return OrderVH(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderVH, position: Int) {
        val data = list[position]
        val context = holder.binding.root.context

        holder.binding.orderId.text = context.getString(R.string.Order_id_regex, data.id)

        val builder = SpannableStringBuilder()
        val boldSpan = StyleSpan(Typeface.BOLD)

        builder.append("Ngày đặt: ")
        var endTitle = builder.length
        builder.append(data.date)
        builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.orderDate.text = builder
        builder.clear()

        builder.append("Số lượng đặt: ")
        endTitle = builder.length
        builder.append(data.listClothes.size.toString())
        builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.orderQuantity.text = builder
        builder.clear()

        builder.append("Người nhận: ")
        endTitle = builder.length
        builder.append(data.receiver)
        builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.orderReceiver.text = builder
        builder.clear()

        builder.append("Điện thoại: ")
        endTitle = builder.length
        builder.append(data.phone.toString())
        builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.orderPhone.text = builder
        builder.clear()

        builder.append("Địa chỉ: ")
        endTitle = builder.length
        builder.append(data.address)
        builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.orderAddress.text = builder
        builder.clear()

        builder.append("Phương thức thanh toán: ")
        endTitle = builder.length
        builder.append(data.paymentMethod)
        builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.binding.orderPaymentMethod.text = builder
        builder.clear()

        holder.binding.orderValue.text = context.getString(R.string.Price_regex, NumberFormat.getNumberInstance(Locale.GERMANY).format(data.price))

       when(data.state){
           "Chờ xử lí" -> {
               timeLineSelected(holder.binding.waitHandle, null, context)
           }

           "Đã xuất kho" -> {
               timeLineSelected(holder.binding.waitHandle, null, context)
               timeLineSelected(holder.binding.exitStorage, holder.binding.waitHandleExitStorage, context)
           }

           "Đang vận chuyển" -> {
               timeLineSelected(holder.binding.waitHandle, null, context)
               timeLineSelected(holder.binding.exitStorage, holder.binding.waitHandleExitStorage, context)
               timeLineSelected(holder.binding.shipping, holder.binding.exitStorageShipping, context)
           }

           "Đã tới nơi" -> {
               timeLineSelected(holder.binding.waitHandle, null, context)
               timeLineSelected(holder.binding.exitStorage, holder.binding.waitHandleExitStorage, context)
               timeLineSelected(holder.binding.shipping, holder.binding.exitStorageShipping, context)
               timeLineSelected(holder.binding.comeAddress, holder.binding.shippingComeAddress, context)
           }

           "Đã thanh toán" -> {
               timeLineSelected(holder.binding.waitHandle, null, context)
               timeLineSelected(holder.binding.exitStorage, holder.binding.waitHandleExitStorage, context)
               timeLineSelected(holder.binding.shipping, holder.binding.exitStorageShipping, context)
               timeLineSelected(holder.binding.comeAddress, holder.binding.shippingComeAddress, context)
               timeLineSelected(holder.binding.paid, holder.binding.comeAddressPaid, context)
           }

       }

        holder.binding.executePendingBindings()
    }

    private fun timeLineSelected(timeLine: ImageView, line: ImageView?, context: Context) {
        timeLine.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.oval_blue, null))
        line?.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.line_blue, null))
    }

    fun setList(list: List<Order>){
        this.list = list
        notifyDataSetChanged()
    }
}