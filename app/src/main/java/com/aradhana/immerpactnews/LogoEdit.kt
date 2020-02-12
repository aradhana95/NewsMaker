package com.aradhana.immerpactnews

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class LogoEdit : AppCompatActivity() {

    companion object{
        val IMAGE_BITMAP_VALUE = "image_bitmap_value"
    }

    private  lateinit var logoImg1: ImageView
    private lateinit var  logoImg2: ImageView
    private  lateinit var  previewImg : ImageView
    private lateinit var  okBtn     : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo_edit)

        logoImg1 = findViewById(R.id.first_logo)
        logoImg2 = findViewById(R.id.second_logo)
        previewImg = findViewById(R.id.preview_img)
        okBtn = findViewById(R.id.btn_ok)

        var logoIntent : Intent = getIntent()

        var byteArray :ByteArray =   logoIntent.getByteArrayExtra(MainActivity.LOGO_IMAGE_PREF)
        var bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        previewImg.setImageBitmap(bitmap)

        okBtn.setOnClickListener {
            sendImage()
        }

        logoImg1.setOnClickListener {
            logoImg1.buildDrawingCache()
            var logo1Bitmap: Bitmap = logoImg1.getDrawingCache()
            previewImg.setImageBitmap(logo1Bitmap)

        }

        logoImg2.setOnClickListener{
            logoImg2.buildDrawingCache()
            var logo2Bitmap: Bitmap = logoImg2.getDrawingCache()
            previewImg.setImageBitmap(logo2Bitmap)

        }



    }

    fun sendImage(){
        var logoIntentResult : Intent = Intent()

        previewImg.buildDrawingCache()
        var bitmapPreview : Bitmap = previewImg.getDrawingCache()
        var previewByte : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmapPreview.compress(Bitmap.CompressFormat.PNG,100,previewByte)
        var byteArrayPreview:ByteArray = previewByte.toByteArray()

        logoIntentResult.putExtra(IMAGE_BITMAP_VALUE,byteArrayPreview)
        setResult( Activity.RESULT_OK,logoIntentResult)
        finish()

        /*var logoBitmap: Bitmap = logoImage.getDrawingCache()
        var   byteArrayStream : ByteArrayOutputStream =  ByteArrayOutputStream()
        logoBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayStream)
        var byteArray :ByteArray = byteArrayStream.toByteArray()
        logorequest.putExtra(MainActivity.LOGO_IMAGE_PREF,byteArray)*/

    }
}