package com.locnguyen.saleclothesapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.ItemPaymentBinding
import com.locnguyen.saleclothesapplication.model.SellClothes
import java.text.NumberFormat
import java.util.Locale

class PaymentAdapter(private var list: List<SellClothes>) : RecyclerView.Adapter<PaymentAdapter.PaymentVH>(){

    var totalOrderValue: MutableLiveData<Long> = MutableLiveData(0)

    class PaymentVH(val binding: ItemPaymentBinding): RecyclerView.ViewHolder(binding.root){
        fun bindImg(context: Context, img: String) {
            Glide.with(context)
                .load(img)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading_err)
                .into(binding.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentVH {
        return PaymentVH(ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PaymentVH, position: Int) {
        val data = list[position]
        val context = holder.binding.root.context

        holder.bindImg(context, data.img)
        holder.binding.clothesInfo.text = context.getString(R.string.Clothes_info_regex, data.name, data.size, data.color.name)
        holder.binding.quality.text = context.getString(R.string.Quality_regex, data.quantity)

        val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)
        val formattedPrice= numberFormat.format(data.price)
        holder.binding.price.text = context.getString(R.string.Price_regex, formattedPrice)

        val clothesPrice = data.price*data.quantity
        totalOrderValue.value = totalOrderValue.value?.plus(clothesPrice)
        val formattedTotalPrice= numberFormat.format(clothesPrice)
        holder.binding.totalPrice.text = context.getString(R.string.Price_regex, formattedTotalPrice)

        holder.binding.executePendingBindings()
    }
}