package com.aradhana.immerpactnews

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import java.io.File

class sharePlay : AppCompatActivity() {

    private lateinit var share_btn: ImageButton
    private lateinit var play_btn: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_play)

        share_btn = findViewById(R.id.share_btn)
        play_btn = findViewById(R.id.play_btn)

        val bundle: Bundle? = intent.extras
        val fileLocation = bundle!!.get("file location")
        Toast.makeText(applicationContext,fileLocation.toString(), Toast.LENGTH_LONG).show()

        val f = File(fileLocation.toString())
        val uriPath: Uri = Uri.parse(f.getPath())



        share_btn.setOnClickListener{v ->

            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Text")
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriPath)
            shareIntent.type = "video/*"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(shareIntent, "send"))

        }

        play_btn.setOnClickListener { v ->

            val intent = Intent(Intent.ACTION_VIEW)

            intent.setDataAndType(Uri.parse(fileLocation.toString()), "video/*")

            startActivity(Intent.createChooser(intent, "Complete action using"))

        }

    }






}

