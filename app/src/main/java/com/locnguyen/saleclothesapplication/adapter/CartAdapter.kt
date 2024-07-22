package com.locnguyen.saleclothesapplication.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.ItemCartBinding
import com.locnguyen.saleclothesapplication.model.SellClothes

class CartAdapter(private var list: List<SellClothes>, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<CartAdapter.CartVH>(){
    var orderList: MutableLiveData<List<SellClothes>> = MutableLiveData(emptyList())
    var removeFromCart: MutableLiveData<SellClothes> = MutableLiveData()

    class CartVH(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root){
        val itemQuantity: MutableLiveData<Long> = MutableLiveData()

        fun setTotalPrice(price: Long) {
            val numberFormat = DataLocal.getInstance().priceFormat
            binding.price.text = binding.root.context.getString(R.string.Price_regex, numberFormat.format(price * itemQuantity.value!!))
        }

        fun increase1ForQuantity(){
            itemQuantity.value = itemQuantity.value?.plus(1)
        }

        fun decrease1ForQuantity(){
            itemQuantity.value = itemQuantity.value?.minus(1)
        }

        fun disableDecrease(){
            binding.decreaseQuantity.apply {
                alpha = 0.3f
                isClickable = false
            }
        }

        fun enableDecrease(){
            binding.decreaseQuantity.apply {
                alpha = 1f
                isClickable = true
            }
        }

        fun disableIncrease(){
            binding.increaseQuantity.apply {
                alpha = 0.3f
                isClickable = false
            }
        }

        fun enableIncrease(){
            binding.increaseQuantity.apply {
                alpha = 1f
                isClickable = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        return CartVH(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartVH, position: Int) {
        val data = list[position]
        val copiedData = data.copy()
        val context = holder.binding.root.context

        holder.binding.apply {
            DataLocal.getInstance().bindImg(context, data.img, img)
            name.apply {
                text = data.name
                isSelected = true
            }
            quantity.text = context.getString(R.string.Quantity_regex, data.quantity)
            size.text = context.getString(R.string.Size_regex, data.size)
            color.setBackgroundColor(Color.parseColor(data.color.hexCode))
        }

        holder.itemQuantity.value = data.quantity
        orderList.value = orderList.value!!.plus(copiedData)

        holder.itemQuantity.observe(lifecycleOwner) { quantity ->
            holder.binding.clothesQuantityValue.text = quantity.toString()

           when(quantity){
               0L -> {
                   holder.disableDecrease()
                   holder.enableIncrease()
               }

               data.quantity -> {
                   holder.disableIncrease()
                   holder.enableDecrease()
               }

               else -> {
                   holder.enableIncrease()
                   holder.enableDecrease()
               }
           }

            holder.setTotalPrice(copiedData.price)
            updateClothesQuantity(copiedData, quantity)
        }

        holder.binding.decreaseQuantity.setOnClickListener {
            if (holder.itemQuantity.value!! > 0){
                holder.decrease1ForQuantity()
            }
        }

        holder.binding.increaseQuantity.setOnClickListener {
            if (holder.itemQuantity.value!! < data.quantity){
                holder.increase1ForQuantity()
            }
        }

        holder.binding.root.setOnLongClickListener {
            removeFromCart.value = data
            return@setOnLongClickListener true
        }

        holder.binding.executePendingBindings()
    }

    private fun updateClothesQuantity(updateClothes: SellClothes, newQuantity: Long) {
        val currentOrderList = orderList.value!!

        currentOrderList.forEach { clothes ->
            if (clothes == updateClothes){
                clothes.quantity = newQuantity
            }
        }

        orderList.value = currentOrderList
    }

    fun setList(list: List<SellClothes>){
        this.list = list
        orderList.value = emptyList()
        notifyDataSetChanged()
    }
}