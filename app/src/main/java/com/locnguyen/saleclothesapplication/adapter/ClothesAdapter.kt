package com.locnguyen.saleclothesapplication.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.ItemClothesBinding
import com.locnguyen.saleclothesapplication.model.Clothes
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

        fun bindColor(context: Context, colors: List<Int>) {
            try{
                val firstColor = context.resources.getColor(colors[0], null)
                binding.firstColor.setBackgroundColor(firstColor)
            }
            catch(e: Resources.NotFoundException){
                binding.firstColorSpace.visibility =  INVISIBLE
            }

            try{
                val secondColor = context.resources.getColor(colors[1], null)
                binding.secondColor.setBackgroundColor(secondColor)
            }
            catch(e: Resources.NotFoundException){
                binding.secondColorSpace.visibility =  INVISIBLE
            }

            try{
                val thirdColor = context.resources.getColor(colors[2], null)
                binding.thirdColor.setBackgroundColor(thirdColor)
            }
            catch(e: Resources.NotFoundException){
                binding.thirdColorSpace.visibility =  INVISIBLE
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

        holder.bindImg(context, data.img)
        holder.binding.clothesName.text = data.name

        when(data.group){
            "Nam" -> holder.binding.clothesGroupValue.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_male, null))
            "Nữ" -> holder.binding.clothesGroupValue.setImageDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_female, null))
        }

        holder.bindColor(context, data.color)

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