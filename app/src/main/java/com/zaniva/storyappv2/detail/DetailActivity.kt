package com.zaniva.storyappv2.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.zaniva.storyappv2.R
import com.zaniva.storyappv2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detail()


    }

    private fun detail() {
        val photo = intent.getStringExtra(PHOTO).toString()
        val name = intent.getStringExtra(NAME).toString()
        val desc = intent.getStringExtra(DESC).toString()
        val date = intent.getStringExtra(DATE).toString()

        binding.apply {
            Glide.with(binding.root)
                .load(photo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_baseline_image_24)
                )
                .centerCrop()
                .into(imgStory)

            tvName.text = name
            tvDesc.text = desc
            tvDate.text = date
        }
    }

    companion object{
        const val PHOTO = "photo"
        const val NAME = "name"
        const val DESC = "description"
        const val DATE = "date"
    }
}