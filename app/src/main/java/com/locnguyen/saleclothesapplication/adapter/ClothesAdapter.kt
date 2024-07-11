package com.locnguyen.saleclothesapplication.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.ItemClothesBinding
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.model.ClothesColor
import java.text.NumberFormat
import java.util.Locale

class ClothesAdapter(var list: ArrayList<Clothes>, private val onClicked: (Clothes) -> Unit): RecyclerView.Adapter<ClothesAdapter.ClothesVH>() {

    val defaultList = list

    class ClothesVH(val binding: ItemClothesBinding): RecyclerView.ViewHolder(binding.root){
        fun bindImg(context: Context, img: String) {
            Glide.with(context)
                .load(img)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading_err)
                .into(binding.clothesImg)
        }

        fun bindColor(context: Context, colors: List<ClothesColor>) {
            try{
                binding.firstColor.setBackgroundColor(Color.parseColor(colors[0].hexCode))
            }
            catch(e: IllegalArgumentException){
                binding.firstColor.setBackgroundColor(context.resources.getColor(R.color.black, null))
            }

            try{
                binding.secondColor.setBackgroundColor(Color.parseColor(colors[1].hexCode))
            }
            catch(e: IllegalArgumentException){
                binding.secondColor.setBackgroundColor(context.resources.getColor(R.color.black, null))
            }

            try{
                binding.thirdColor.setBackgroundColor(Color.parseColor(colors[2].hexCode))
            }
            catch(e: IllegalArgumentException){
                binding.thirdColor.setBackgroundColor(context.resources.getColor(R.color.black, null))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesVH {
        return ClothesVH(ItemClothesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: ClothesVH, position: Int) {
        val data = list[position]
        val context = holder.binding.root.context

        holder.bindImg(context, data.imgs[0])
        holder.binding.clothesName.text = data.name

        when(data.group){
            "Nam" -> holder.binding.clothesGroupValue.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_male, null))
            "Nữ" -> holder.binding.clothesGroupValue.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_female, null))
        }

        holder.bindColor(context, data.colors)

        val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)
        val formattedNumber = numberFormat.format(data.price)
        holder.binding.clothesPrice.text = context.getString(R.string.Price_regex, formattedNumber)

        holder.binding.root.setOnClickListener {
            onClicked.invoke(data)
        }
        holder.binding.executePendingBindings()
    }

    fun setListClothes(list: ArrayList<Clothes>){
        this.list = list
        notifyDataSetChanged()
    }

}