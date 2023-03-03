package com.example.giphy

import android.app.DownloadManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class SecondActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val downloadButton = findViewById<Button>(R.id.downloadButton)

        supportActionBar?.hide()
        val logoImage: ImageView = findViewById(R.id.logoImageView2)
        Glide.with(this).load(R.drawable.ic_logo_2).into(logoImage)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = intent.getStringExtra("title")


        val imageView = findViewById<ImageView>(R.id.imageView)

        val url = intent.getStringExtra("url")

        Glide.with(this).load(url).into(imageView)

        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        downloadButton.setOnClickListener {
            val downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("File")
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
            request.setDestinationInExternalFilesDir(this,
                downloadFolder.toString(),
                intent.getStringExtra("title") + ".gif"
            )

            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            dm.enqueue(request)

            Toast.makeText(this, "Start Downloading..", Toast.LENGTH_SHORT).show()
        }
    }
}