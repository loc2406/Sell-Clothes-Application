package com.locnguyen.saleclothesapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.ItemClothesImgBinding

class AutoLoadImgAdapter(private val imgs: List< String>):  RecyclerView.Adapter<AutoLoadImgAdapter.AutoLoadImgVH>(){

        class AutoLoadImgVH(val binding: ItemClothesImgBinding): RecyclerView.ViewHolder(binding.root){
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoLoadImgVH {
        return AutoLoadImgVH(ItemClothesImgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imgs.size
    }

    override fun onBindViewHolder(holder: AutoLoadImgVH, position: Int) {
        val data = imgs[position]

        DataLocal.getInstance().bindImg(holder.binding.root.context, data, holder.binding.clothesImg)
    }

}