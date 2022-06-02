package com.zaniva.storyappv2.story.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.zaniva.storyappv2.R
import com.zaniva.storyappv2.databinding.RvItemBinding
import com.zaniva.storyappv2.detail.DetailActivity
import com.zaniva.storyappv2.story.Stories

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>(){
    private val list = ArrayList<Stories>()
    private var onClick: OnClick? = null
    private lateinit var context: Context

    fun setOnClick(onClick: OnClick){
        this.onClick = onClick
    }

    fun setList (user: List<Stories>){
        list.clear()
        list.addAll(user)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(u: Stories){
            binding.root.setOnClickListener {
                onClick?.onClicked(u, binding)
            }
            binding.apply {
                val img: ImageView = imgStory
                val name: TextView = tvName
                val desc: TextView = tvDesc
                val date: TextView = tvDate

                Glide.with(binding.root)
                    .load(u.photo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_baseline_image_24)
                    )
                    .centerCrop()
                    .into(img)
                name.text = u.name
                desc.text = u.description
                date.text = u.date

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnClick{
        fun onClicked(data: Stories, card: RvItemBinding)
    }
}