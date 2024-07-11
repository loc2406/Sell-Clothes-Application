package com.locnguyen.saleclothesapplication.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.ItemCartBinding
import com.locnguyen.saleclothesapplication.model.SellClothes
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(private var list: List<SellClothes>) : RecyclerView.Adapter<CartAdapter.CartVH>(){

    private var deleteState: Boolean = false
    private val deleteList: ArrayList<SellClothes> by lazy {ArrayList()}

    class CartVH(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root){
        fun bindImg(context: Context, img: String) {
            Glide.with(context)
                .load(img)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading_err)
                .into(binding.img)
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
        val context = holder.binding.root.context

        holder.bindImg(context, data.img)

        if (deleteState){
            holder.binding.checkbox.visibility = VISIBLE
        }
        else{
            holder.binding.checkbox.apply {
                visibility = GONE
                isChecked = false
            }
        }

        holder.binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                deleteList.add(data)
            }
            else{
                deleteList.remove(data)
            }
        }

        holder.binding.name.text = data.name
        holder.binding.quantity.text = context.getString(R.string.Quality_regex, data.quantity)
        holder.binding.size.text = context.getString(R.string.Size_regex, data.size)
        holder.binding.color.setBackgroundColor(Color.parseColor(data.color.hexCode))

        val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)
        val formattedNumber = numberFormat.format(data.price)
        holder.binding.price.text = context.getString(R.string.Price_regex, formattedNumber)

        holder.binding.executePendingBindings()
    }

    fun setList(list: List<SellClothes>){
        this.list = list
        notifyDataSetChanged()
    }

    fun getList() = this.list

    fun setDeleteState(state: Boolean){
        this.deleteState = state
        notifyDataSetChanged()
    }

    fun deleteClothesFromCart() {
        this.list -= deleteList.toSet()
        notifyDataSetChanged()
    }
}