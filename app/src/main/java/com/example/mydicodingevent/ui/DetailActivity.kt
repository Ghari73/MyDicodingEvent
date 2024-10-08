package com.example.mydicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mydicodingevent.R
import com.example.mydicodingevent.data.local.entity.FavoriteEvent
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val event = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra<ListEventsItem>(KEY_ID)
        else intent.getParcelableExtra(KEY_ID)

        if (event != null){
            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(event.imageLogo)
                    .into(binding.imgDetail)
                tvNamaDetail.text = event.name
                tvCategoryDetail.text = event.category
                tvDescDetail.text = event.description
                tvTimeDetail.text = event.beginTime
                tvOwnerDetail.text = event.ownerName
                tvQuoteDetail.text = event.quota.toString()

                val insFrag = intent.getIntExtra(INSTANCE_FRAGMENT, -1)

                if (insFrag == 2) btnShare.isEnabled = false
                else {
                    btnShare.setOnClickListener {
                        val url = event.link
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }

                viewModel.getFavEventbyId(event.id.toString()).observe(this@DetailActivity){
                    if (it == null) { //kondisi event belum difavoritkan
                        floatingActionButton2.setImageResource(R.drawable.baseline_star_border_24)
                        floatingActionButton2.setOnClickListener{
                            viewModel.insertFavEvent(
                                FavoriteEvent(
                                    event.id.toString(), event.name, event.mediaCover
                                )
                            )
                        }
                    } else{ //kondisi event favorit
                        floatingActionButton2.setImageResource(R.drawable.baseline_star_24)
                        floatingActionButton2.setOnClickListener{
                            viewModel.deleteFavEvent(event.id.toString())
                        }
                    }
                }
            }
        }

        viewModel.getLoading().observe(this){ handler ->
            handler.getContentIfNotHandled()?.let {
                showLoading(it)
            }
        }

    }


    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
    companion object{
        const val KEY_ID = "key_id"
        const val INSTANCE_FRAGMENT = "key_instance"
    }
}