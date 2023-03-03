package com.example.giphy

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.*
import okhttp3.internal.immutableListOf
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException
import java.sql.DriverManager.println

class MainActivity : AppCompatActivity() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val BASE_URL = "https://api.giphy.com/v1/"
        val TAG = "MainActivity"

        val searchView = findViewById<SearchView>(R.id.searchView)
        //searchView.setBackgroundColor(Color.WHITE)

        supportActionBar?.hide()
        val logoImage: ImageView = findViewById(R.id.logoImageView)
        Glide.with(this).load(R.drawable.ic_logo_2).into(logoImage)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val gifs = mutableListOf<DataObject>()
        val adapter = GifsAdapter(this, gifs)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter.setOnItemClickListener(object : GifsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)

                intent.putExtra("url", gifs[position].images.ogImage.url)
                intent.putExtra("title", gifs[position].title)
                searchView.clearFocus()
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        })

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retroService = retrofit.create(DataService::class.java)
        retroService.getTrendingGifs().enqueue(object : Callback<DataResult?> {
            override fun onResponse(call: Call<DataResult?>, response: Response<DataResult?>) {
                val body = response.body()
                if (body == null) {
                    Log.d(TAG, "No response...")
                }
                gifs.addAll(body!!.res)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<DataResult?>, t: Throwable) {

            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    gifs.clear()
                    adapter.notifyDataSetChanged()

                    retroService.getSearch("gifs/search?api_key=lGPLJ9cD7aeOZ5a9qdhZ6BSrliDoSLAo&q=$query").enqueue(object : Callback<DataResult?> {
                        override fun onResponse(call: Call<DataResult?>, response: Response<DataResult?>) {
                            val body = response.body()
                            if (body == null) {
                                Log.d(TAG, "No response...")
                            }
                            gifs.addAll(body!!.res)
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<DataResult?>, t: Throwable) {

                        }

                    })
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 == ""){
                    gifs.clear()
                    adapter.notifyDataSetChanged()

                    retroService.getTrendingGifs().enqueue(object : Callback<DataResult?> {
                        override fun onResponse(call: Call<DataResult?>, response: Response<DataResult?>) {
                            val body = response.body()
                            if (body == null) {
                                Log.d(TAG, "No response...")
                            }
                            gifs.addAll(body!!.res)
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<DataResult?>, t: Throwable) {

                        }

                    })
                }
                return true
            }
        })
    }
}