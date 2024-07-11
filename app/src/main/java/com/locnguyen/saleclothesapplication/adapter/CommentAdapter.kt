package com.locnguyen.saleclothesapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.ItemCommentBinding
import com.locnguyen.saleclothesapplication.model.Comment

class CommentAdapter(private var comments: List<Comment>):
    RecyclerView.Adapter<CommentAdapter.CommentVH>() {

    class CommentVH(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){
        var isFavorite: Boolean = false

        fun addStar(view: ImageView){
            view.setImageDrawable(AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_star))
        }

        fun removeStar(view: ImageView) {
            view.setImageDrawable(AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_star_empty))
        }

        fun setFavorite(){
            when(isFavorite){
                true -> binding.icFavorite.setImageDrawable(AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_favorite_checked))
                false -> binding.icFavorite.setImageDrawable(AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_favorite_unchecked))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH {
        return CommentVH(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        val data = comments[position]

        holder.binding.assessor.text = data.name
        holder.binding.time.text = data.time
        holder.binding.content.text = data.content
        holder.binding.favoriteValue.text = data.favorite.toString()

        when(data.star){
            5 -> {
                holder.addStar(holder.binding.firstStar)
                holder.addStar(holder.binding.secondStar)
                holder.addStar(holder.binding.thirdStar)
                holder.addStar(holder.binding.fouthStar)
                holder.addStar(holder.binding.fifthStar)
            }

            4 -> {
                holder.addStar(holder.binding.firstStar)
                holder.addStar(holder.binding.secondStar)
                holder.addStar(holder.binding.thirdStar)
                holder.addStar(holder.binding.fouthStar)
                holder.removeStar(holder.binding.fifthStar)
            }

            3 -> {
                holder.addStar(holder.binding.firstStar)
                holder.addStar(holder.binding.secondStar)
                holder.addStar(holder.binding.thirdStar)
                holder.removeStar(holder.binding.fouthStar)
                holder.removeStar(holder.binding.fifthStar)
            }

            2 -> {
                holder.addStar(holder.binding.firstStar)
                holder.addStar(holder.binding.secondStar)
                holder.removeStar(holder.binding.thirdStar)
                holder.removeStar(holder.binding.fouthStar)
                holder.removeStar(holder.binding.fifthStar)
            }

            1 -> {
                holder.addStar(holder.binding.firstStar)
                holder.removeStar(holder.binding.secondStar)
                holder.removeStar(holder.binding.thirdStar)
                holder.removeStar(holder.binding.fouthStar)
                holder.removeStar(holder.binding.fifthStar)
            }
        }

        holder.binding.icFavorite.setOnClickListener{
            holder.isFavorite = !holder.isFavorite
            holder.setFavorite()
        }

        
        holder.binding.executePendingBindings()
    }

    fun setFilterList(filterList: List<Comment>){
        comments = filterList
        notifyDataSetChanged()
    }

}